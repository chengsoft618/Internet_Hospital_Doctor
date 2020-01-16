package cn.longmaster.hospital.doctor.core.requests;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.LogInterface;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.utils.EncryptUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * 基本的HTTP请求, 所有的HTTP请求都通过该类发起
 * Created by yangyong on 16/4/27.
 */
public abstract class HttpRequester implements Application.ActivityLifecycleCallbacks {
    protected final String TAG = this.getClass().getSimpleName();
    private static AsyncHttpClient asyncHttpClient;
    //是否启用加密
    private boolean isEncode = true;
    @AppApplication.Manager
    private UserInfoManager userInfoManager;
    @AppApplication.Manager
    private AuthenticationManager authenticationManager;

    public HttpRequester() {
        AppApplication.getInstance().registerActivityLifecycleCallbacks(this);
        AppApplication.getInstance().injectManager(this);
    }

    static {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setCookieStore(new PersistentCookieStore(AppApplication.getInstance()));
        asyncHttpClient.setTimeout(60 * 1000);
        asyncHttpClient.setLoggingEnabled(true);
        asyncHttpClient.setLoggingLevel(LogInterface.VERBOSE);
    }

    private AsyncHttpResponseHandler asyncHttpResponseHandler = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            Logger.logD(Logger.HTTP, TAG + "#onStart");
            super.onStart();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Logger.logW(Logger.HTTP, TAG + "#onFailure:{statusCode:" + statusCode + ", responseString:" + responseString + ",throwable.msg:" + throwable.getMessage() + "}");
            BaseResult baseResult = new BaseResult();
            baseResult.setOpType(getOpType());
            baseResult.setTaskId(getTaskId());
            if (statusCode == 0) {
                baseResult.setCode(OnResultListener.RESULT_FAILED);
                HttpRequester.this.onFinish(baseResult, null);
            } else {
                baseResult.setCode(OnResultListener.RESULT_SERVER_CODE_ERROR);
                HttpRequester.this.onFinish(baseResult, null);
            }
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Logger.logD(Logger.HTTP, TAG + "#onSuccess:{statusCode:" + statusCode + ", responseString:" + responseString + "}");
            try {
                JSONObject jsonObject = new JSONObject(responseString);
                BaseResult baseResult = JsonHelper.toObject(jsonObject, BaseResult.class);
                if (baseResult.getCode() == OnResultListener.RESULT_AUTH_CODE_ERROR) {
                    Logger.logW(Logger.HTTP, TAG + "#onFinish-->" + "鉴权校验失败-->baseResult.getCode():" + baseResult.getCode());
                    //获取鉴权信息
                    authenticationManager.addFailRequester(getThis());
                    authenticationManager.getAuthenticationInfo();
                    return;
                }
                HttpRequester.this.onFinish(baseResult, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                BaseResult baseResult = new BaseResult();
                baseResult.setOpType(getOpType());
                baseResult.setTaskId(getTaskId());
                baseResult.setCode(OnResultListener.RESULT_FAILED);
                HttpRequester.this.onFinish(baseResult, null);
            }
        }

        @Override
        public void onRetry(int retryNo) {
            Logger.logD(Logger.HTTP, TAG + "#onRetry retryNo:" + retryNo);
            super.onRetry(retryNo);
        }
    };

    /**
     * @return 返回服务器地址
     */
    protected abstract String getUrl();

    /**
     * @return 返回接口id
     */
    protected abstract int getOpType();

    /**
     * @return 任务ID，自行定义并会原样返回
     */
    protected abstract String getTaskId();

    private HttpRequester getThis() {
        return this;
    }

    /**
     * @retuan 放入请求参数
     */
    protected abstract void onPutParams(Map<String, Object> params);

    protected void putParams(Map<String, Object> params) {
        onPutParams(params);
    }

    /**
     * 请求结束
     */
    protected abstract void onFinish(BaseResult baseResult, JSONObject data);

    /**
     * 发起Post请求
     */
    public void doPost() {
        if (!NetStateReceiver.hasNetConnected(AppApplication.getInstance().getApplicationContext())) {//无网络
            ToastUtils.showShort(R.string.no_network_connection);
            BaseResult baseResult = new BaseResult();
            baseResult.setOpType(getOpType());
            baseResult.setTaskId(getTaskId());
            baseResult.setCode(OnResultListener.RESULT_FAILED);
            HttpRequester.this.onFinish(baseResult, null);
            return;
        }

        Map<String, Object> params = new HashMap<>();
        initBaseParams(params);
        putParams(params);
        JSONObject jsonObject;
        Header[] headers = new Header[1];
        if (isEncode) {
            Map<String, String> rsaMap = new HashMap<>(1);
            rsaMap.put("rsp", getRSAParams(params));
            jsonObject = new JSONObject(rsaMap);
            String signature = getSignature(params);
            headers[0] = new BasicHeader("Signature", signature);
        } else {
            jsonObject = new JSONObject(params);
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("json", jsonObject);
        Logger.logD(Logger.HTTP, TAG + "#request url:" + getUrl() + toString(params));
        if (isEncode) {
            asyncHttpClient.post(Utils.getTopActivityOrApp(), getUrl(), headers, requestParams, null, asyncHttpResponseHandler);
        } else {
            asyncHttpClient.post(Utils.getTopActivityOrApp(), getUrl(), requestParams, asyncHttpResponseHandler);
        }
    }

    protected void initBaseParams(Map<String, Object> params) {
        params.put("c_auth", SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH, ""));
        params.put("op_type", getOpType());
        params.put("task_id", getTaskId());
        params.put("c_ver", AppConfig.CLIENT_VERSION);
        params.put("c_type", "1");
        params.put("user_type", "1");
        UserInfo userInfo = userInfoManager.getCurrentUserInfo();
        params.put("user_id", userInfo.getUserId());
        params.put("gender", "0");
        if (userInfo.getUserId() != 0) {
            params.put("sid", userInfo.getLoginAuthKey());
        }
    }

    /**
     * 获取签名
     *
     * @param params
     * @return
     */
    protected String getSignature(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject(params);
        StringBuilder valuesStr = new StringBuilder();
        try {
            valuesStr.append(jsonObject.getString("user_id"));
            valuesStr.append(jsonObject.getString("user_type"));
            valuesStr.append(jsonObject.getString("op_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String values = valuesStr.toString();
        String md = EncryptUtils.encryptMD5ToString(values);
        try {
            return EncryptUtils.encryptHmacSHA1ToString(md, md);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * RSA加密
     *
     * @param params
     * @return
     */
    protected String getRSAParams(Map<String, Object> params) {
        JSONObject jsonObject = new JSONObject(params);
        String jsr = jsonObject.toString();
        InputStream inPublic = null;
        try {
            inPublic = Utils.getApp().getResources().getAssets().open("rsa_public_key.pem");
            return EncryptUtils.encryptRSA(jsr, inPublic, "RSA/ECB/PKCS1Padding").replaceAll("\\+", " ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inPublic) {
                    inPublic.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() + "#"+TAG +"#onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() + "#" + TAG + "#onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() + "#"+TAG +"#onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() +"#"+TAG + "#onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() + "#" + TAG + "#onActivityStopped");
        asyncHttpClient.cancelRequests(activity, true);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() + "#" + TAG + "#onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //Logger.logD(Logger.HTTP, activity.getClass().getSimpleName() +"#"+TAG + "#onActivityDestroyed");
    }

    private String toString(Map<String, Object> map) {
        if (null == map) {
            return "?json={}";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("?json={");
        for (String key : map.keySet()) {
            stringBuilder.append("\"");
            stringBuilder.append(key);
            stringBuilder.append("\":");
            stringBuilder.append("\"");
            if (null != map.get(key)) {
                stringBuilder.append(map.get(key).toString());
            }
            stringBuilder.append("\"");
            stringBuilder.append(",");
        }
        String result = StringUtils.substringBeforeLast(stringBuilder.toString(), ",");
        result += "}";
        return result;
    }
}
