package cn.longmaster.hospital.doctor.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import cn.longmaster.doctorlibrary.util.UtilStatus;
import cn.longmaster.doctorlibrary.util.fileloader.FileLoader;
import cn.longmaster.doctorlibrary.util.imageloader.ImageLoader;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.download.MediaDownloadManager;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.AuxiliaryInspectInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.HTTPDNSManager;
import cn.longmaster.hospital.doctor.core.manager.LocationManager;
import cn.longmaster.hospital.doctor.core.manager.common.BaseConfigManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.common.FileDownLoadManager;
import cn.longmaster.hospital.doctor.core.manager.common.HospitalManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.consult.RecordManager;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.im.AudioPlayManager;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.manager.rounds.PatientManager;
import cn.longmaster.hospital.doctor.core.manager.rounds.RoundsManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.AvatarManager;
import cn.longmaster.hospital.doctor.core.manager.user.ConfirmListManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.PatientDataManager;
import cn.longmaster.hospital.doctor.core.manager.user.PlatformManager;
import cn.longmaster.hospital.doctor.core.manager.user.RepresentFunctionManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.personalmaterial.PersonalMaterialManager;
import cn.longmaster.hospital.doctor.core.upload.UploadTaskManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.utils.AppUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.Utils;

/**
 * Created by yangyong on 2016/5/30.
 */
@SuppressWarnings("ALL")
public class AppApplication extends Application implements Application.ActivityLifecycleCallbacks {
    public static final Handler HANDLER = new Handler();

    public static final String TAG = AppApplication.class.getSimpleName();

    private static AppApplication mApplication;

    /**
     * 所有管理类的容器管理
     */
    private HashMap<String, BaseManager> mManagers = new HashMap<>();

    /**
     * 锁，用于保证进程初始化完成后才能启动Activity和Service
     */
    private CountDownLatch mCountDownLatch;
    /**
     * 应用是否启动
     */
    private boolean mIsAppStarted;
    /**
     * 是否设置GK
     */
    private boolean mIsSetGKDomain;
    /**
     * 用户是否已经进入视频诊室
     */
    private boolean mEnterVideoRoom;

    private boolean isCalling;

    private String mAppKey = "";

    /**
     * 在线状态
     */
    private int mOnlineState;
    private boolean mIsLogin;
    private boolean mIsOnLogin;

    private HashMap<String, BaseManager> managers = new HashMap<>();

    //图片预览
    private List<AuxiliaryMaterialInfo> mAuxiliaryMaterialInfos = new ArrayList<>();
    private List<AuxiliaryInspectInfo> mAuxiliaryInspectInfo = new ArrayList<>();
    private List<String> mLocalFilePaths = new ArrayList<>();
    private List<String> mServerUrls = new ArrayList<>();
    //当前显示的Activity
    private String mCurrentActivity = "";
    //当前医生权限
    private int currentDoctorIdentity;

    public void setCurrentIdentity(int doctorIdentity) {
        currentDoctorIdentity = doctorIdentity;
    }

    public int getCurrentDoctorIdentity() {
        return currentDoctorIdentity;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                ClassicsHeader classicsHeader = new ClassicsHeader(context);
                classicsHeader.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.pull_refresh_view_refresh_custom));
                classicsHeader.setArrowDrawable(ContextCompat.getDrawable(context, R.drawable.pull_refresh_view_refresh_custom));
                classicsHeader.setEnableLastTime(false);
                classicsHeader.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
                return classicsHeader;//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                ClassicsFooter classicsFooter = new ClassicsFooter(context).setDrawableSize(20);
                classicsFooter.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.pull_refresh_view_refresh_custom));
                return classicsFooter;
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        ImageLoader.getInstance().init(this);
        FileLoader.getInstance().init(this);
        if (!Utils.isApkInDebug()) {
            CrashReport.initCrashReport(getApplicationContext(), "3c13be57df", Utils.isApkInDebug());
        }

        String pidName = getUIPName();
        if (pidName.equals(getPackageName())) {
            initManager();
            mCountDownLatch = new CountDownLatch(1);
            // 启动服务
            AppConfig.setUrl();
            SdManager.getInstance().init();

            AppExecutors.getInstance().networkIO().execute(mInitRunable);
            UtilStatus.init(this, Utils.isApkInDebug(), AppConfig.CLIENT_VERSION);
            registerActivityLifecycleCallbacks(this);
        }
        SPUtils.getInstance().remove(AppPreference.KEY_FIRST_ADD_SCHEDULE);
        SPUtils.getInstance().remove("action_view");
        AppUtils.addOnAppStatusChangedListener(this, new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground() {
                Logger.logD(TAG, "addOnAppStatusChangedListener#onForeground");
                setUserOnlineState(1);
            }

            @Override
            public void onBackground() {
                Logger.logD(TAG, "addOnAppStatusChangedListener#onBackground");
                setUserOnlineState(0);
            }
        });
    }


    private void setUserOnlineState(int state) {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int userId = getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
                if (userId != 0) {
                    getManager(DcpManager.class).enterUserOnLineState(userId, state, new DcpManager.OnUserOnlineStateLoadListener() {
                        @Override
                        public void onLoad(int state, int userId) {

                        }
                    });
                }
            }
        });
    }

    @NonNull
    private String getUIPName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private final Runnable mInitRunable = new Runnable() {
        @Override
        public void run() {
            initMainService();
            // 释放锁
            mCountDownLatch.countDown();
        }
    };

    public static AppApplication getInstance() {
        return mApplication;
    }

    /**
     * 在线程中 初始化主进程
     */
    private void initMainService() {
        AppService.enqueueWork(this, new Intent());
    }

    /**
     * 等待Application的基本数据准备好
     */
    public void waitToInit() {
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用是否已经启动
     *
     * @return true 启动完成， false 启动未完成
     */
    public boolean isAppStarted() {
        return mIsAppStarted;
    }

    /**
     * 设置应用是否启动完成
     *
     * @param isAppStarted true 启动完成， false 启动未完成
     */
    public void setAppStarted(boolean isAppStarted) {
        mIsAppStarted = isAppStarted;
    }

    /**
     * 获取是否设置GK
     *
     * @return true 设置， false 未设置
     */
    public boolean isSetGKDomain() {
        return mIsSetGKDomain;
    }

    /**
     * 设置是否设置GK
     *
     * @param isSetGKDomain true 设置， false 未设置
     */
    public void setIsSetGKDomain(boolean isSetGKDomain) {
        mIsSetGKDomain = isSetGKDomain;
    }

    public String getAppKey() {
        return mAppKey;
    }

    private void setAppKey(String appKey) {
        mAppKey = appKey;
    }

    /**
     * 获取用户在线状态
     *
     * @return 在线状态
     */
    public int getOnlineState() {
        return mOnlineState;
    }

    /**
     * 设置用户在线状态
     *
     * @param onlineState 用户在线状态
     */
    public void setOnlineState(int onlineState) {
        mOnlineState = onlineState;
        if (onlineState != AppConstant.ONLINE_STATE_ONLINE) {
            mIsLogin = false;
            mIsOnLogin = false;
        }
    }

    /**
     * 用户是否登录
     *
     * @return true登录 false 未登录
     */
    public boolean isOnLogin() {
        return mIsOnLogin;
    }

    public void setIsOnLogin(boolean mIsOnLogin) {
        this.mIsOnLogin = mIsOnLogin;
    }

    /**
     * 是否调用了login
     *
     * @return true是 false 否
     */
    public boolean isLogin() {
        return mIsLogin;
    }

    /**
     * 设置用户是否登录
     */
    public void setIsLogin(boolean isLogin) {
        this.mIsLogin = isLogin;
    }

    /**
     * 用户是否进入视频诊室
     *
     * @return true 进入；false 未进入
     */
    public boolean isEnterVideoRoom() {
        return mEnterVideoRoom;
    }

    /**
     * 用户是否正在被呼叫
     *
     * @return true 呼叫；false 未被呼叫
     */
    public boolean isCallsing() {
        return isCalling;
    }

    /**
     * 设置是否进入诊室
     *
     * @param b true 进入；false 未进入
     */
    public void setIsEnterVideoRoom(boolean b) {
        mEnterVideoRoom = b;
    }

    public void setIsCalling(boolean isCall) {
        isCalling = isCall;
    }

    private void initManager() {
        List<BaseManager> managerList = new ArrayList<>();
        registerManager(managerList);
        for (BaseManager baseManager : managerList) {
            injectManager(baseManager);
            baseManager.onManagerCreate(this);
            managers.put(baseManager.getClass().getName(), baseManager);
        }

        for (Map.Entry<String, BaseManager> entry : managers.entrySet()) {
            entry.getValue().onAllManagerCreated();
        }
    }

    @UiThread
    public <V extends BaseManager> V getManager(Class<V> cls) {
        return (V) managers.get(cls.getName());
    }

    public void injectManager(Object object) {
        Class<?> aClass = object.getClass();
        while (aClass != Object.class && aClass != View.class && aClass != BaseManager.class && aClass != Activity.class) {
            Field[] declaredFields = aClass.getDeclaredFields();
            if (declaredFields != null && declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                        // 忽略掉static 和 final 修饰的变量
                        continue;
                    }

                    if (!field.isAnnotationPresent(Manager.class)) {
                        continue;
                    }

                    Class<?> type = field.getType();
                    if (!BaseManager.class.isAssignableFrom(type)) {
                        throw new RuntimeException("@Manager 注解只能应用到BaseManager的子类");
                    }

                    BaseManager baseManager = getManager((Class<? extends BaseManager>) type);

                    if (baseManager == null) {
                        throw new RuntimeException(type.getSimpleName() + " 管理类还未初始化！");
                    }

                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    try {
                        field.set(object, baseManager);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            aClass = aClass.getSuperclass();
        }
    }

    protected void registerManager(List<BaseManager> lists) {
        lists.add(new HospitalManager());
        lists.add(new DcpManager());
        lists.add(new DBManager());
        lists.add(new PlatformManager());
        lists.add(new PatientDataManager());
        lists.add(new ConfirmListManager());
        lists.add(new UserInfoManager());
        lists.add(new AccountManager());
        lists.add(new LocalNotificationManager());
        lists.add(new LocalManager());
        lists.add(new MessageManager());
        lists.add(new AudioAdapterManager());
        lists.add(new PatientManager());
        lists.add(new RoundsManager());
        lists.add(new ConsultRoomManager());
        lists.add(new BaseConfigManager());
        lists.add(new AvatarManager());
        lists.add(new ConsultManager());
        lists.add(new RecordManager());
        lists.add(new UploadTaskManager());
        lists.add(new DoctorManager());
        lists.add(new MediaDownloadManager());
        lists.add(new LocationManager());
        lists.add(new AuthenticationManager());
        lists.add(new GroupMessageManager());
        lists.add(new AudioPlayManager());
        lists.add(new MaterialTaskManager());
        lists.add(new RepresentFunctionManager());
        lists.add(new FileDownLoadManager());
        lists.add(new HTTPDNSManager());
        lists.add(new PersonalMaterialManager());
        lists.add(new DCManager());
    }

    public List<AuxiliaryInspectInfo> getmAuxiliaryInspectInfo() {
        return mAuxiliaryInspectInfo;
    }

    public void setmAuxiliaryInspectInfo(List<AuxiliaryInspectInfo> mAuxiliaryInspectInfo) {
        this.mAuxiliaryInspectInfo = mAuxiliaryInspectInfo;
    }

    public List<AuxiliaryMaterialInfo> getAuxilirayMaterialInfos() {
        return mAuxiliaryMaterialInfos;
    }

    public void setAuxilirayMaterialInfos(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        mAuxiliaryMaterialInfos = auxiliaryMaterialInfos;
    }

    public List<String> getLocalFilePaths() {
        return mLocalFilePaths;
    }

    public void setLocalFilePaths(List<String> localFilePaths) {
        mLocalFilePaths = localFilePaths;
    }

    public List<String> getServerUrls() {
        return mServerUrls;
    }

    public void setServerUrls(List<String> serverUrls) {
        mServerUrls = serverUrls;
    }

    public String getCurrentActivity() {
        return mCurrentActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityCreated()->" + activity.getClass().getName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityStarted()->" + activity.getClass().getName());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityResumed()->" + activity.getClass().getName());
        mCurrentActivity = activity.getClass().getSimpleName();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityPaused()->" + activity.getClass().getName());
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityStopped()->" + activity.getClass().getName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.logD(Logger.COMMON, TAG + "->onActivitySaveInstanceState()->" + activity.getClass().getName());
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.logD(Logger.COMMON, TAG + "->onActivityDestroyed()->" + activity.getClass().getName());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    public @interface Manager {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
