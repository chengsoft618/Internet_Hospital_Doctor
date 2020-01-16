package cn.longmaster.doctorlibrary.util.common;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.net.InetAddress;

import cn.longmaster.doctorlibrary.util.UtilStatus;

/**
 * 获取 应用App相关信息
 */
@SuppressWarnings("ALL")
public class AppUtil {
    /**
     * 获取渠道号
     *
     * @return 渠道号
     */
    public static int getChannelCode() {
        String code = getMetaData("channel");
        if (code != null) {
            try {
                return Integer.valueOf(code);
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }

    /**
     * 获取META_DATA值
     *
     * @param key 键
     * @return 当存在key时返回数值否则返回null
     */
    private static String getMetaData(String key) {
        try {
            ApplicationInfo ai = UtilStatus.getApplication().getPackageManager().getApplicationInfo(UtilStatus.getApplication().getPackageName(), PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                Object value = ai.metaData.get(key);
                if (value != null) {
                    return value.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据域名获取ip地址
     *
     * @param name 域名
     * @return ip地址
     */
    public static String getIP(String name) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address == null ? "getIP_FAILED" : address.getHostAddress().toString();
    }
}
