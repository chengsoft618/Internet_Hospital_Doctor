package cn.longmaster.hospital.doctor.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;

import java.util.concurrent.TimeUnit;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.KickOffDialog;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

public class AppStartActivity extends NewBaseActivity {
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    @Override
    protected void initDatas() {
        /**
         * 判断程序的启动方式：打开一个新的任务、还是将后台的应用给提到前台来
         * 解决程序第一次安装，通过“打开”按钮启动，home键后点击桌面图标重新启动程序问题。
         */
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setEnableShowKickoff(false);
        Disposable disposable = PermissionHelper
                .init(getThisActivity())
                .addPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .requestEachCombined()
                .subscribe(permission -> {
                    if (permission.granted) {
                        new Handler().postDelayed(() -> start(), 1000);
                    } else {
                        new CommonDialog.Builder(getThisActivity())
                                .setTitle("权限授予")
                                .setMessage("在设置-应用管理-权限中开启存储权限，才能正常使用39互联网医院")
                                .setPositiveButton("取消", this::finish)
                                .setNegativeButton("确定", () -> {
                                    Utils.gotoAppDetailSetting();
                                    finish();
                                })
                                .show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_app_start;
    }

    @Override
    protected void initViews() {

    }

    /**
     * 判断程序入口
     */
    private void judgeEnter() {
        if (SPUtils.getInstance().getInt(AppPreference.KEY_GUIDE_PAGE_CURRENT_VERSION, -1) != AppConfig.CLIENT_VERSION) {
            Intent intent = new Intent(getThisActivity(), GuideActivity.class);
            startActivity(intent);
        } else {
            if (AppApplication.getInstance().getCurrentActivity().equals(KickOffDialog.class.getSimpleName())) {
                Logger.logD(Logger.COMMON, "账户再其它设备登录，正在显示被踢下线对话框不做界面跳转！！");
                return;
            } else {
                UserInfo userInfo = mUserInfoManager.getCurrentUserInfo();
                if (userInfo.getUserId() == 0) {
                    Intent intent = new Intent(getThisActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getThisActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        }
        finish();
    }

    private void start() {
        int count = 20;//最多等待10秒钟，直到AppStarted
        while (count > 0 && !AppApplication.getInstance().isAppStarted()) {
            long curTimes = System.currentTimeMillis();
            count--;
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long duration = System.currentTimeMillis() - curTimes;
            Logger.logD(Logger.COMMON, "duration:" + duration);
        }
        if (AppApplication.getInstance().isAppStarted()) {//APP启动成功
            Logger.logD(Logger.COMMON, "App is started!");
            judgeEnter();
        } else {
            finish();
        }
    }
}