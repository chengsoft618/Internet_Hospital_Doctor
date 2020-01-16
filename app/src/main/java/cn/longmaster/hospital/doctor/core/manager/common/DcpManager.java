package cn.longmaster.hospital.doctor.core.manager.common;

import com.ppcp.manger.CallbackInterface;
import com.ppcp.manger.PpcpInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.AppUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.HTTPDNSManager;
import cn.longmaster.hospital.doctor.util.NetWorkUtil;
import cn.longmaster.utils.StringUtils;

/**
 * dcp操作管理类
 * Created by yangyong on 16/7/26.
 */
public class DcpManager extends BaseManager {
    private static final String TAG = DcpManager.class.getSimpleName();

    private AppApplication mApplication;
    private PpcpInterface mDcpInterface;//  dcp接口
    private CallbackInterface mDcpJniCallback;// dcp jin回调
    private boolean mIsOpenClientSuccess;
    private long mHttpDnsIp;

    private Map<Integer, OnUserOnlineStateLoadListener> onUserOnlineStateLoacListener;

    @Override
    public void onManagerCreate(AppApplication application) {
        mApplication = application;
        mDcpInterface = new PpcpInterface();
        mDcpJniCallback = new CallbackInterface();
        mDcpJniCallback.setCallBackListener(new DcpListener());
        onUserOnlineStateLoacListener = new LinkedHashMap<>();
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
    }

    public PpcpInterface getDcpInterface() {
        return mDcpInterface;
    }

    public CallbackInterface getDcpJniCallback() {
        return mDcpJniCallback;
    }

    public boolean isOpenClientSuccess() {
        return mIsOpenClientSuccess;
    }

    public long getIp() {
        return mHttpDnsIp;
    }

    public void openClient() {
        String packageName = AppApplication.getInstance().getFilesDir().getParent() + "/lib/";
        Logger.logD(Logger.COMMON, TAG + "->openClient()#pacageName:" + packageName);
        mIsOpenClientSuccess = getDcpInterface().openClient(mDcpJniCallback, packageName, AppConfig.IS_DEBUG_MODE ? 6 : -1);
        Logger.logD(Logger.COMMON, TAG + "->openClient()->mIsOpenClientSuccess:" + mIsOpenClientSuccess);
    }

    /**
     * 设置GK
     *
     * @return 接口调用结果
     */
    public boolean setGKDomain() {
        try {
            JSONObject json = new JSONObject();
            json.put("_gkPort", AppConfig.getServerPort());
            json.put("_gkDomain", AppUtil.getIP(AppConfig.getServerAddress()));
            String result = getDcpInterface().Request(DcpFuncConfig.FUN_NAME_SETGKDEMAIN, json.toString());
            Logger.logD(Logger.COMMON, TAG + "->setGKDomain()->result:" + result);
            if (StringUtils.equals(result, "true")) {
                AppApplication.getInstance().setIsSetGKDomain(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String keepAlive(int userId) {
        String result = "false";
        try {
            JSONObject json = new JSONObject();
            json.put("_userID", userId);
            result = getDcpInterface().Request(DcpFuncConfig.FUN_NAME_KEEPALIVE, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置pes
     *
     * @param pesIp   ip地址
     * @param pesPort 端口
     */
    public void setPesInfo(final long pesIp, final int pesPort, final boolean isSubmitIp) {
        mHttpDnsIp = -1;
        Logger.logD(Logger.COMMON, TAG + "->setPesInfo()->pesIp:" + pesIp + ", pesPort:" + pesPort);
        getManager(HTTPDNSManager.class).getHttpDnsUrl(AppConstant.DomainNameType.DOMAIN_NAME_TYPE_LOGIN, url -> {
            final long httpDnsIp = getManager(HTTPDNSManager.class).getLongDnsIp(url);
            try {
                JSONObject json = new JSONObject();
                json.put("_pesIP", httpDnsIp == 0L ? pesIp : httpDnsIp);
                json.put("_pesPort", pesPort);
                Logger.logD(Logger.COMMON, TAG + "->setPesInfo()->json:" + json.toString());
                getDcpInterface().Request(DcpFuncConfig.FUN_NAME_SETPESINFO, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (isSubmitIp) {
                mHttpDnsIp = httpDnsIp;
            }
        });
    }

    /**
     * 登录
     *
     * @param userId       用户id
     * @param loginAuthKey 鉴权
     */
    public void login(int userId, String loginAuthKey) {
        Logger.logD(Logger.COMMON, TAG + "->login()->userId:" + userId + ", loginAuthKey:" + loginAuthKey);
        try {
            JSONObject json = new JSONObject();
            json.put("_userID", userId);
            json.put("_loginAuthKey", loginAuthKey);
            json.put("_clientVersion", AppConfig.CLIENT_VERSION);
            json.put("_netType", NetWorkUtil.getNetworkType(mApplication.getApplicationContext()));
            json.put("_reserved", 1);
            Logger.logD(Logger.COMMON, TAG + "->login()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_LOGIN, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出登录
     *
     * @param userId 用户id
     */
    public void logout(int userId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_userID", userId);
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_LOGOUT, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            JSONObject jsonObject = new JSONObject();
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_DISCONNECT, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param senderID   发送者id
     * @param recverID   接受者id
     * @param msgType    消息类型
     * @param msgContent 消息内容
     */
    public void sendMessage(int senderID, int recverID, byte msgType, String msgContent) {
        try {
            JSONObject json = new JSONObject();
            json.put("_senderID", senderID);
            json.put("_recverID", recverID);
            json.put("_msgType", msgType);
            json.put("_seqID", System.currentTimeMillis() / 1000);
            json.put("_msgContent", msgContent);
            Logger.logD(Logger.COMMON, TAG + "->sendMessage()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_SEND_MESSAGE, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收消息
     *
     * @param userId
     */
    public void getMessage(int userId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_userID", userId);

            PpcpInterface.request(DcpFuncConfig.FUN_NAME_GET_MESSAGE, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询余额
     */
    public void inquireBalance() {
        try {
            JSONObject json = new JSONObject();
            json.put("_userType", 2);
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_INQUIRE_BALANCE_NEW, json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测管理员
     */
    public void checkAdminInfo() {
        JSONObject json = new JSONObject();
        getDcpInterface().Request(DcpFuncConfig.FUN_NAME_CHECK_ADMIN_INFO, json.toString());
    }

    /**
     * 拉取在线房间列表
     */
    public void getRoomListInfo(int actionType) {
        JSONObject json = new JSONObject();
        try {
            json.put("_actionType", actionType);
            json.put("_reserved", "");
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_GET_ROOM_LIST_INFO, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证会议室密码
     */
    public void checkPwdInfo(int actionType, int roomId, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("_actionType", actionType);
            json.put("_roomID", roomId);
            json.put("_password", MD5Util.md5(password));
            json.put("_reserved", "");
            Logger.logD(Logger.COMMON, TAG + "->checkPwdInfo()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_CHECK_PWD_INFO, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 静音
     */
    public void pausePlay(boolean isPause) {
        JSONObject json = new JSONObject();
        try {
            json.put("isPause", isPause);
            Logger.logD(Logger.COMMON, TAG + "->pausePlay()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_PAUSE_PLAY, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户当前在线状态
     *
     * @param userId      当前用户ID
     * @param queryUserId 查询用户ID
     */
    public void getUserOnLineState(int userId, int queryUserId, OnUserOnlineStateLoadListener onUserOnlineStateLoacListener) {
        this.onUserOnlineStateLoacListener.put(queryUserId, onUserOnlineStateLoacListener);
        JSONObject json = new JSONObject();
        try {
            json.put("_userID", userId);
            json.put("_queryUserID", queryUserId);
            Logger.logD(Logger.COMMON, TAG + "->pausePlay()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_QUERY_USER_ONLINE_STATE, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param userId                        用户ID
     * @param state                         状态//0 前台->后台;   1 后台->前台
     * @param onUserOnlineStateLoacListener
     */
    public void enterUserOnLineState(int userId, int state, OnUserOnlineStateLoadListener onUserOnlineStateLoacListener) {
        this.onUserOnlineStateLoacListener.put(userId, onUserOnlineStateLoacListener);
        JSONObject json = new JSONObject();
        try {
            json.put("_userID", userId);
            json.put("_enterType", state);
            Logger.logD(Logger.COMMON, TAG + "->pausePlay()->json:" + json.toString());
            getDcpInterface().Request(DcpFuncConfig.FUN_NAME_ENTER_USER_BACKGROUND, json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onUserOnLineStateRecv(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            int queryUserId = jsonObject.getInt("_queryUserID");
            int onlineState = jsonObject.getInt("_onlineState");
            if (null != onUserOnlineStateLoacListener.get(queryUserId)) {
                onUserOnlineStateLoacListener.get(queryUserId).onLoad(onlineState, queryUserId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnUserOnlineStateLoadListener {
        /**
         * @param state  //被查询人在线情况 0：下线 1：在线 2：在线-后台
         * @param userId
         */
        void onLoad(int state, int userId);
    }
}
