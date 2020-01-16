package cn.longmaster.hospital.doctor.core.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 电话接收器
 * Created by JinKe on 2016-10-14.
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    private static List<PhoneStateListener> mPhoneStateListenerList = new ArrayList<>();

    public PhoneStateReceiver() {
    }

    public static void addPhoneListener(PhoneStateListener listener) {
        mPhoneStateListenerList.add(listener);
    }

    public static void removePhoneListener(PhoneStateListener listener) {
        mPhoneStateListenerList.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        for (PhoneStateListener listener : mPhoneStateListenerList) {
            listener.onCallStateChanged(tm.getCallState(), intent.getStringExtra("incoming_number"));
        }
    }
}
