package cn.longmaster.hospital.doctor.ui.main;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.PersonalMaterialContract;
import cn.longmaster.hospital.doctor.core.entity.TabEntity;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.manager.user.VersionChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.VersionManager;
import cn.longmaster.hospital.doctor.core.upload.newupload.UploadState;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.ui.user.PatientMaterialManagerActivity;
import cn.longmaster.hospital.doctor.util.TabLayoutManager;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by yangyong on 16/5/30.
 * Mod by biao on 2019/11/06
 */
public class MainActivity extends NewBaseActivity implements MessageStateChangeListener {
    @FindViewById(R.id.act_main_tab_ctl)
    private CommonTabLayout actMainTabCtl;
    @FindViewById(R.id.act_main_newbie_guide_layout_rl)
    private RelativeLayout mNewbieGuideLayoutRl;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private DBManager mDBManager;
    private TabLayoutManager tabLayoutManager;
    private static final long BACKKEY_PRESS_MAX_INTERVAL = 800;
    private long mBackKeyClickedTime;
    private boolean mRounds;
    private int currentPage = 0;
    private HomePageFragment homePageFragment;
    private CollegeFragment collegeFragment;
    private DoctorListFragment doctorListFragment;
    private UsercenterFragment usercenterFragment;
    private VersionChangeListener versionChangeListener = new VersionChangeListener() {
        @Override
        public void onNewVersion() {
            VersionManager.getInstance().upgrade(MainActivity.this, isActivityIsForeground());
        }

        @Override
        public void onVersionLimited() {
            VersionManager.getInstance().upgrade(MainActivity.this, isActivityIsForeground());
        }
    };

    @Override
    protected void onResume() {
        initMessageTipState();
        super.onResume();
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        homePageFragment = new HomePageFragment();
        collegeFragment = new CollegeFragment();
        doctorListFragment = new DoctorListFragment();
        usercenterFragment = new UsercenterFragment();
        tabLayoutManager = new TabLayoutManager.Builder()
                .init(MainActivity.this, actMainTabCtl, R.id.act_main_fl)
                .addTab(new TabEntity("39医院", R.mipmap.ic_tab_home_sel, R.mipmap.ic_tab_home_pre), homePageFragment)
                .addTab(new TabEntity("医学院", R.mipmap.ic_tab_college_sel, R.mipmap.ic_tab_college_pre), collegeFragment)
                .addTab(new TabEntity("名医在", R.mipmap.ic_tab_doctor_sel, R.mipmap.ic_tab_doctor_pre), doctorListFragment)
                .addTab(new TabEntity("个人中心", R.mipmap.ic_tab_mine_sel, R.mipmap.ic_tab_mine_pre), usercenterFragment)
                .setOnTabSelectListener(getOnTabSelectListener())
                .build();
        tabLayoutManager.start();
        checkType();
        getIntentData(getIntent());
        mUserInfoManager.regVersionChangeListener(versionChangeListener);
        mMessageManager.regMsgStateChangeListener(this);
        VersionManager.getInstance().setMainActivityExit(true);
        checkVersion();
        initMyData();
        updateMaterialTaskState();
    }

    private OnTabSelectListener getOnTabSelectListener() {
        return new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                currentPage = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VersionManager.getInstance().setMainActivityExit(false);
        mUserInfoManager.unRegVersionChangeListener(versionChangeListener);
        //清除所有通知
        AppApplication.getInstance().getManager(LocalNotificationManager.class).cancleAllNotification();
        mMessageManager.unRegMsgStateChangeListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
        setIntent(intent);
    }

    private void initMyData() {
        String str = SPUtils.getInstance().getString("action_view");
        Logger.logI(Logger.COMMON, "MainActivity：str：" + str);
        if (!StringUtils.isEmpty(str)) {
            Intent intent = new Intent(MainActivity.this, PatientMaterialManagerActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PATH, str);
            startActivity(intent);
        }
    }

    private void checkVersion() {
        if (VersionManager.getInstance().getCurentClientVersion() < SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LIMIT_VERSION, 0)) {
            versionChangeListener.onVersionLimited();
        } else if (VersionManager.getInstance().getCurentClientVersion() < SPUtils.getInstance().getInt(AppPreference.KEY_SERVER_LASTEST_VERSION, 0)) {
            versionChangeListener.onNewVersion();
        }
    }

    private void getIntentData(final Intent intent) {
        AppApplication.HANDLER.post(() -> handlerIntentData(intent));
    }

    private void handlerIntentData(Intent intent) {
        String action = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACTION);
        mRounds = intent.getBooleanExtra("rounds", false);
        Logger.logD(Logger.COMMON, TAG + "->handlerIntentData()->action:" + action + ",rounds:" + mRounds);
        if (action != null) {
            if (action.equals(AppConstant.ACTION_CHANGE_TAB)) {
                tabLayoutManager.setCurrentPage(0);
            }
        }

        if (mRounds) {
            tabLayoutManager.setCurrentPage(0);
        }
    }

    private void checkType() {
        tabLayoutManager.setCurrentPage(0);
    }


    @OnClick({R.id.act_main_newbie_guide_ib,
            R.id.act_main_newbie_guide_close_ib,
            R.id.act_main_newbie_guide_layout_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_main_newbie_guide_ib:
                mNewbieGuideLayoutRl.setVisibility(View.GONE);
                SPUtils.getInstance().getBoolean(AppPreference.KEY_IS_SHOW_NEWBIE_GUIDE, false);
                Intent intent = new Intent(getThisActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getNewbieGuideUrl());
                startActivity(intent);
                break;

            case R.id.act_main_newbie_guide_close_ib:
                mNewbieGuideLayoutRl.setVisibility(View.GONE);
                SPUtils.getInstance().getBoolean(AppPreference.KEY_IS_SHOW_NEWBIE_GUIDE, false);
                break;

            case R.id.act_main_newbie_guide_layout_rl:
                //什么也不干，只为拦截点击和滑动事件
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentPage != 0) {
                currentPage = 0;
                tabLayoutManager.setCurrentPage(currentPage);
                return true;
            }
            if (System.currentTimeMillis() - mBackKeyClickedTime > BACKKEY_PRESS_MAX_INTERVAL) {
                ToastUtils.showShort(R.string.press_again_back_home);
                mBackKeyClickedTime = System.currentTimeMillis();
                return true;
            } else {
                // finish();
                exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        if ((baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_BALANCE_CHANGE ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_APPOINTMENT_TODAY ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_39_ACTIVITY ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_MATERIAL_UPLOAD_NOTE) && !mMessageManager.isInMessageCenter()) {
            tabLayoutManager.showDot(3);
            Logger.logI(Logger.USER, "onNewMessage->被调用");
        } else if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_ASSESS && !mMessageManager.isInAssess()) {
            tabLayoutManager.showDot(3);
            Logger.logI(Logger.USER, "onNewMessage->被调用");
        }
    }

    /**
     * 初始化消息提示红点的状态
     */
    private void initMessageTipState() {
        if (SPUtils.getInstance().getBoolean(AppPreference.KEY_MESSAGE_CENTER_NEW_MESSAGE + mUserInfoManager.getCurrentUserInfo().getUserId(), false)
                || SPUtils.getInstance().getBoolean(AppPreference.KEY_NEW_ASSESS + mUserInfoManager.getCurrentUserInfo().getUserId(), false)) {
            tabLayoutManager.showDot(3);
        } else {
            tabLayoutManager.hideMsg(3);
        }
    }

    /**
     * 更新任务状态
     */
    private void updateMaterialTaskState() {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PersonalMaterialContract.PersonalMaterialEntry.UPLOAD_STATE, UploadState.UPLOAD_FAILED);
                    String whereClause = PersonalMaterialContract.PersonalMaterialEntry.USER_ID + "=?";
                    String[] whereArgs = new String[]{mUserInfoManager.getCurrentUserInfo().getUserId() + ""};
                    database.update(PersonalMaterialContract.PersonalMaterialEntry.TABLE_NAME, contentValues, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    private void exit() {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        } else {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }
}
