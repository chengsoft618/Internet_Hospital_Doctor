package cn.longmaster.hospital.doctor.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 屏幕状态接收器
 * Created by JinKe on 2016-11-22.
 */
public class ScreenReceiver extends BroadcastReceiver {
    private String action = null;
    private ScreenStateListener mScreenStateListener;

    public ScreenReceiver(ScreenStateListener screenStateListener) {
        mScreenStateListener = screenStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏
            mScreenStateListener.onScreenOn();
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏
            mScreenStateListener.onScreenOff();
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
            mScreenStateListener.onUserPresent();
        }
    }

    public interface ScreenStateListener {// 返回给调用者屏幕状态信息

        public void onScreenOn();

        public void onScreenOff();

        public void onUserPresent();
    }
}
