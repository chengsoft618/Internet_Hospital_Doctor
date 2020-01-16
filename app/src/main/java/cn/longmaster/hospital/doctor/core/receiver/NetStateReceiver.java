package cn.longmaster.hospital.doctor.core.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.upload.NginxUploadTask;

/**
 * 网络状态变化广播接收器
 * Created by yangyong on 2015/7/18.
 */
public class NetStateReceiver extends BroadcastReceiver {
    private final String TAG = NetStateReceiver.class.getSimpleName();

    //无网络连接
    public static final int NETWORK_TYPE_NONE = -0x1;
    // WiFi
    public static final int NETWORK_TYPE_WIFI = 0x1;
    // gprs
    public static final int NETWOKR_TYPE_MOBILE = 0x2;

    // 没有网络
    public static final byte NET_OFF = -1;
    // pc在线
    public static final byte NET_PC = 0;
    //3G在线
    public static final byte NET_3G = 1;
    // wifi在线
    public static final byte NET_WIFI = 2;

    // 当前网络状态
    private int mNetWorkState;

    private static List<NetworkStateChangedListener> mNetworkStateChangedListenerArrayList = new ArrayList<NetworkStateChangedListener>();

    public NetStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!AppApplication.getInstance().isAppStarted()) {
            return;
        }

        if (!initNetState()) {
            return;
        }

        Logger.logD(Logger.COMMON, TAG + "->onReceive()->网络状态：" + mNetWorkState);

        if (mNetWorkState == NET_OFF) {
            AppApplication.getInstance().setOnlineState(AppConstant.ONLINE_STATE_OFFLINE);
        }
        if (mNetWorkState != NET_OFF && AppApplication.getInstance().getOnlineState() == AppConstant.ONLINE_STATE_OFFLINE) {
            UserInfo userInfo = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo();
            AppApplication.getInstance().getManager(DcpManager.class).setPesInfo(userInfo.getPesIp(), userInfo.getPesPort(), false);
        }

        setMobileNetType(context);
        if (mNetworkStateChangedListenerArrayList.size() > 0) {
            for (NetworkStateChangedListener listener : mNetworkStateChangedListenerArrayList) {
                if (listener != null) {
                    listener.onNetworkStateChanged(getCurrentNetType(context));
                }
            }
        }
    }

    /**
     * 初始化网络
     *
     * @return 初始化结果
     */
    private boolean initNetState() {
        boolean result = true;
        try {
            ConnectivityManager manager = (ConnectivityManager) AppApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifi != null && wifi.isConnected()) {
                if (mNetWorkState == NET_WIFI) {
                    result = false;
                }

                mNetWorkState = NET_WIFI;
            } else if (gprs != null && gprs.isConnected()) {
                if (mNetWorkState == NET_3G) {
                    result = false;
                }
                mNetWorkState = NET_3G;
            } else {
                if (mNetWorkState == NET_OFF) {
                    result = false;
                }
                mNetWorkState = NET_OFF;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断是否有网络
     *
     * @return true：有，false：无
     */
    public static boolean hasNetConnected(Context context) {
        int netType = NetStateReceiver.getCurrentNetType(context);
        boolean netState = false;
        if (netType != NETWORK_TYPE_NONE) {
            netState = true;
        }
        return netState;
    }

    /**
     * 获取当前网络状态
     *
     * @param context 上下文
     * @return 网络类型
     */
    public static int getCurrentNetType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
        NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs

        if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
            return NETWORK_TYPE_WIFI;
        } else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
            return NETWOKR_TYPE_MOBILE;
        }
        return NETWORK_TYPE_NONE;
    }

    public interface NetworkStateChangedListener {
        public abstract void onNetworkStateChanged(int netWorkType);
    }

    public static void setOnNetworkStateChangedListener(NetworkStateChangedListener listener) {
        mNetworkStateChangedListenerArrayList.add(listener);
    }

    public static void removeNetworkStateChangedListener(NetworkStateChangedListener listener) {
        if (mNetworkStateChangedListenerArrayList != null) {
            mNetworkStateChangedListenerArrayList.remove(listener);
        }
    }

    /**
     * 获取当前网络状态
     *
     * @param context 上下文
     */
    public void setMobileNetType(Context context) {
        final int ALL2G_SIZE = 1024 * 32;
        final int WIFI_SIZE = 1024 * 256;
        final int OTHER3G_SIZE = 1024 * 128;
        final int G4_SIZE = 1024 * 256;
        final int DEFAULT_SIZE = 1024 * 64;

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // wifi
        NetworkInfo gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // gprs
        if (wifi != null && wifi.getState() == NetworkInfo.State.CONNECTED) {
            NginxUploadTask.setDefaultBlockSize(WIFI_SIZE);
        } else if (gprs != null && gprs.getState() == NetworkInfo.State.CONNECTED) {
            switch (gprs.getSubtype()) {
                /** Network type is unknown */
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is GPRS */
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    NginxUploadTask.setDefaultBlockSize(ALL2G_SIZE);
                    break;
                /** Current network is EDGE */
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    NginxUploadTask.setDefaultBlockSize(ALL2G_SIZE);
                    break;
                /** Current network is UMTS */
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    NginxUploadTask.setDefaultBlockSize(OTHER3G_SIZE);
                    break;
                /** Current network is CDMA: Either IS95A or IS95B */
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    NginxUploadTask.setDefaultBlockSize(ALL2G_SIZE);
                    break;
                /** Current network is EVDO revision 0 */
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    NginxUploadTask.setDefaultBlockSize(OTHER3G_SIZE);
                    break;
                /** Current network is EVDO revision A */
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    NginxUploadTask.setDefaultBlockSize(OTHER3G_SIZE);
                    break;
                /** Current network is 1xRTT */
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is HSDPA */
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    NginxUploadTask.setDefaultBlockSize(OTHER3G_SIZE);
                    break;
                /** Current network is HSUPA */
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is HSPA */
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is iDen */
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is EVDO revision B */
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is LTE */
                case TelephonyManager.NETWORK_TYPE_LTE:
                    NginxUploadTask.setDefaultBlockSize(G4_SIZE);
                    break;
                /** Current network is eHRPD */
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                /** Current network is HSPA+ */
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
                default:
                    NginxUploadTask.setDefaultBlockSize(DEFAULT_SIZE);
                    break;
            }
        }
    }
}
