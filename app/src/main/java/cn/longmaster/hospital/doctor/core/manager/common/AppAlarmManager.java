package cn.longmaster.hospital.doctor.core.manager.common;


import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.receiver.AlarmReceiver;

/**
 * Alarm管理类
 * Created by yangyong on 2015/7/29.
 */
public class AppAlarmManager {
    private static final String TAG = AppAlarmManager.class.getSimpleName();
    private static AppAlarmManager mInstance;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    public static AppAlarmManager getInstance() {
        if (mInstance == null) {
            synchronized (AppAlarmManager.class) {
                if (mInstance == null) {
                    mInstance = new AppAlarmManager();
                }
            }

        }
        return mInstance;
    }

    private AppAlarmManager() {

        // TODO: 2019/5/24 高版本的cancel方法会报空指针异常
        Intent intent = new Intent(AppApplication.getInstance(), AlarmReceiver.class);
        intent.setAction(AppConstant.ACTION_ALARM_MANAGER);
        mPendingIntent = PendingIntent.getBroadcast(AppApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager = (AlarmManager) AppApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(mPendingIntent);
    }

    public boolean register() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), AlarmReceiver.SEND_KEEP_ALIVE_INTERVAL_TIME, mPendingIntent);
        } else {
            setAlarmFromKitkat(SystemClock.elapsedRealtime());
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setAlarmFromKitkat(long triggerAtMillis) {
        mAlarmManager.cancel(mPendingIntent);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, mPendingIntent);
    }

    public boolean unregister() {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(mPendingIntent);
        }

        mPendingIntent = null;
        return true;
    }
}
