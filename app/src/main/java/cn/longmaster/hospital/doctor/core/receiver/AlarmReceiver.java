package cn.longmaster.hospital.doctor.core.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.manager.common.AppAlarmManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;

/**
 * Alarm接收器
 * Created by yangyong on 2015/7/29.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static String TAG = AlarmReceiver.class.getSimpleName();
    public static final long SEND_KEEP_ALIVE_INTERVAL_TIME = 60 * 1000;//发送激活包间隔时间
    private static long mLatestSendDT = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppApplication.getInstance().injectManager(this);

        long currentTimeMillis = System.currentTimeMillis();
        Logger.logD(Logger.USER, TAG + "->onReceive()->发送激活包间隔时间:" + (currentTimeMillis - mLatestSendDT));

        if (!AppConstant.ACTION_ALARM_MANAGER.equals(intent.getAction())) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppAlarmManager.getInstance().setAlarmFromKitkat(currentTimeMillis + SEND_KEEP_ALIVE_INTERVAL_TIME);
            Logger.logD(Logger.USER, TAG + "->onReceive()->4.4以上版本,重置alarm定时器！！");
        }

        if (!AppApplication.getInstance().isAppStarted()) {
            Logger.logD(Logger.USER, TAG + "->onReceive()->app未启动！！");
            return;
        }

        if (currentTimeMillis - mLatestSendDT < 0) {
            mLatestSendDT = currentTimeMillis;
            Logger.logD(Logger.USER, TAG + "->onReceive()->第一次进入！！");
        }

        if (currentTimeMillis - mLatestSendDT < SEND_KEEP_ALIVE_INTERVAL_TIME - 10) {
            Logger.logD(Logger.USER, TAG + "->onReceive()->小于发送激活包间隔时间，返回！两次发包间隔时间:" + (currentTimeMillis - mLatestSendDT));
            return;
        }

        mLatestSendDT = currentTimeMillis;

        UserInfo userInfo = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo();
        if (userInfo.getUserId() == 0) {
            Logger.logD(Logger.USER, TAG + "->onReceive()->userinfo为空！！");
            return;
        }

        if (!AppApplication.getInstance().isSetGKDomain()) {
            AppApplication.getInstance().getManager(DcpManager.class).setGKDomain();
        }

        if (AppApplication.getInstance().getOnlineState() == AppConstant.ONLINE_STATE_ONLINE) {
            Logger.logD(Logger.USER, TAG + "->onReceive()->用户处于在线状态, 发送激活包！！");
            String result = AppApplication.getInstance().getManager(DcpManager.class).keepAlive(userInfo.getUserId());
            Logger.logD(Logger.USER, TAG + "->onReceive()->用户处于在线状态, 发送激活包结果:" + result);
            if ("false".equals(result)
                    && !AppApplication.getInstance().isEnterVideoRoom()
                    && NetStateReceiver.hasNetConnected(AppApplication.getInstance())) {
                Logger.logD(Logger.USER, TAG + "->onReceive()->用户处于在线状态, 发送激活包失败，重新登录！");
                AppApplication.getInstance().getManager(DcpManager.class).setPesInfo(userInfo.getPesIp(), userInfo.getPesPort(), false);
            }
        } else if (AppApplication.getInstance().getOnlineState() == AppConstant.ONLINE_STATE_OFFLINE && NetStateReceiver.hasNetConnected(AppApplication.getInstance())) {
            Logger.logD(Logger.USER, TAG + "->onReceive()->用户处于离线状态, 登录！！");
            AppApplication.getInstance().getManager(DcpManager.class).setPesInfo(userInfo.getPesIp(), userInfo.getPesPort(), false);
        }
    }
}
