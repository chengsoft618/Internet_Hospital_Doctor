package cn.longmaster.hospital.doctor.core.manager.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.timeout.TimeoutHelper;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.requests.HttpRequester;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 鉴权信息
 * Created by Yang² on 2017/3/23.
 */

public class AuthenticationManager extends BaseManager {

    private static final int MAX_WAITING_TIME = 20 * 1000;//最长等待时间
    private UserInfoManager mUserInfoManager;
    private DcpManager mDcpManager;
    private Deque<HttpRequester> failRequesterList = new ArrayDeque<>();
    private TimeoutHelper<Integer> mTimeoutHelper;
    private GetAuthenticationListener mGetAuthenticationListener;
    private boolean isGetingAuthentication;
    private boolean isInForEach;

    @Override
    public void onManagerCreate(AppApplication application) {
        mTimeoutHelper = new TimeoutHelper<>();
        mTimeoutHelper.setCallback(callback);
        mUserInfoManager = getManager(UserInfoManager.class);
        mDcpManager = getManager(DcpManager.class);
    }

    /**
     * 获取鉴权信息
     */
    public void getAuthenticationInfo() {
        Logger.logD(Logger.COMMON, TAG + "#getAuthenticationInfo()#isGetingAuthentication:" + isGetingAuthentication);
        if (isGetingAuthentication) {
            return;
        }
        isGetingAuthentication = true;
        JSONObject json = new JSONObject();
        mTimeoutHelper.request(mUserInfoManager.getCurrentUserInfo().getUserId(), MAX_WAITING_TIME);
        mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_GET_AUTHENTICATION_INFO, json.toString());
    }

    /**
     * 获取鉴权信息
     */
    public void getAuthenticationInfo(GetAuthenticationListener getAuthenticationListener) {
        Logger.logD(Logger.COMMON, TAG + "#getAuthenticationInfo(GetAuthenticationListener)");
        mGetAuthenticationListener = getAuthenticationListener;
        JSONObject json = new JSONObject();
        mTimeoutHelper.request(mUserInfoManager.getCurrentUserInfo().getUserId(), MAX_WAITING_TIME);
        mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_GET_AUTHENTICATION_INFO, json.toString());
    }

    /**
     * 获取鉴权信息
     *
     * @param result
     * @param json
     */
    public void onGetAuthenticationInfo(int result, String json) {
        Logger.logD(Logger.COMMON, TAG + "->onGetAuthenticationInfo()->result:" + result + ",json:" + json);
        Logger.logD(Logger.COMMON, TAG + "#onGetAuthenticationInfo thread name:" + Thread.currentThread().getName());
        AppHandlerProxy.runOnUIThread(() -> {
            mTimeoutHelper.cancel(mUserInfoManager.getCurrentUserInfo().getUserId());
            if (result == DcpErrorcodeDef.RET_SUCCESS) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    SPUtils.getInstance().put(AppPreference.KEY_AUTHENTICATION_AUTH, jsonObject.optString("_authentication", ""));
                    SPUtils.getInstance().put(AppPreference.KEY_AUTHENTICATION_OUT_TIME, System.currentTimeMillis() + (jsonObject.optLong("_duration", 0)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!failRequesterList.isEmpty()) {
                    isInForEach = true;
                    for (Iterator<HttpRequester> i = failRequesterList.iterator(); i.hasNext(); ) {
                        HttpRequester httpRequester = i.next();
                        Logger.logD(Logger.COMMON, TAG + "#onGetAuthenticationInfo#AppHandlerProxy#run:" + httpRequester.getClass().getSimpleName());
                        httpRequester.doPost();
                        i.remove();
                    }
                    failRequesterList.clear();
                    isInForEach = false;
                }
            }
            if (mGetAuthenticationListener != null) {
                mGetAuthenticationListener.onGetAuthentication();
                mGetAuthenticationListener = null;
            }
            isGetingAuthentication = false;
        });
    }

    /**
     * 把鉴权错误失败的接口存入Map
     *
     * @param httpRequester
     */
    public void addFailRequester(HttpRequester httpRequester) {
        Logger.logD(Logger.COMMON, TAG + "#addFailRequester thread name:" + Thread.currentThread().getName());
        Logger.logD(Logger.COMMON, TAG + "#addFailRequester" + isInForEach);
        if (isInForEach) {
            return;
        }
        failRequesterList.add(httpRequester);
    }

    //请求超时处理
    private TimeoutHelper.OnTimeoutCallback<Integer> callback = new TimeoutHelper.OnTimeoutCallback<Integer>() {
        @Override
        public void onTimeout(TimeoutHelper<Integer> timeoutHelper, Integer taskId) {
            Logger.logD(Logger.COMMON, TAG + "#onTimeout#thread name:" + Thread.currentThread().getName());
            if (mUserInfoManager.getCurrentUserInfo().getUserId() == taskId) {
                AppHandlerProxy.runOnUIThread(() -> {
                    Logger.logD(Logger.COMMON, TAG + "#onTimeout#AppHandlerProxy#run:" + Thread.currentThread().getName());
                    ToastUtils.showShort("获取鉴权值超时");
                    mTimeoutHelper.cancel(mUserInfoManager.getCurrentUserInfo().getUserId());
                    if (mGetAuthenticationListener != null) {
                        mGetAuthenticationListener.onTimeOut();
                        mGetAuthenticationListener = null;
                    }
                });
            }
        }
    };

    public interface GetAuthenticationListener {
        void onGetAuthentication();

        void onTimeOut();
    }
}
