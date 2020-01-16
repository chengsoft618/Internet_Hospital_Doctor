package cn.longmaster.doctorlibrary.util;

import android.app.Application;

/**
 * Util Package的状态
 * Created by LJ.Cao on 2016/4/10.
 */
public class UtilStatus {

    private static boolean IS_DEBUG_MODE = true;
    private static int CLIENT_VERSION;
    private static Application sApplication;

    /**
     * TODO 必须在Application中初始化
     *
     * @param application App的唯一application实例;
     * @param isDebugMode 是否为Debug模式
     */
    public static void init(Application application, boolean isDebugMode, int clientVersion) {
        if (sApplication != null) {
            return;
        }
        sApplication = application;
        IS_DEBUG_MODE = isDebugMode;
        CLIENT_VERSION = clientVersion;
        doInitialization();
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static boolean isDebugMode() {
        return IS_DEBUG_MODE;
    }

    public static int getClientVersion() {
        return CLIENT_VERSION;
    }

    /**
     * 未来有些工具类可能也需要初始化状态，统一加进该方法中
     */
    private static void doInitialization() {
    }
}
