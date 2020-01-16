package cn.longmaster.doctorlibrary.util.common;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.FileReader;

import cn.longmaster.doctorlibrary.util.UtilStatus;
import cn.longmaster.utils.Utils;

/**
 * 获取Android 系统相关信息
 * Created by ddc on 2015-07-18.
 */
public class OsUtil {
    private static final String HEADSET_STATE_PATH = "/sys/class/switch/h2w/state";

    /**
     * 获取客户端操作系统类型(最多支持20个字符)
     *
     * @return 客户端操作系统类型
     */
    public static String getPhoneType() {
        return Build.MODEL;
    }

    /**
     * 获取客户端操作系统版本
     *
     * @return
     */
    public static String getPhoneOSVersion() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 获取客户端Android的rom版本
     *
     * @return
     */
    public static String getRomVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取sdk系统版本
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机生产商
     *
     * @return
     */
    public static String getVendor() {
        String vendor = Build.BRAND + "_";
        vendor += Build.MANUFACTURER + "_";
        vendor += Build.MODEL;
        return vendor;
    }

    /**
     * 获取MacAddress
     *
     * @return
     */
    public static String getMacAddress() {
        String macAddress = "";
        WifiManager wifi = (WifiManager) UtilStatus.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            // 如果Wifi关闭的时候，硬件设备可能无法返回MAC ADDRESS
            if (null != info) {
                macAddress = null == info.getMacAddress() ? "" : info.getMacAddress();
            }
        }
        return macAddress;
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 判断耳机是否插入
     *
     * @return true 插入, fasle未插入
     */
    public static boolean isHeadsetExists() {
        char[] buffer = new char[1024];
        int newState = 0;
        try {
            FileReader file = new FileReader(HEADSET_STATE_PATH);
            int len = file.read(buffer, 0, 1024);
            newState = Integer.valueOf((new String(buffer, 0, len)).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newState != 0;
    }
}
