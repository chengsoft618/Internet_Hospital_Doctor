package cn.longmaster.hospital.doctor.core.requests;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.hospital.doctor.core.AppConfig;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 14:50
 * @description: clientApi基础请求
 */
public abstract class BaseClientApiRequester<Data> extends HttpRequester {
    private OnResultCallback<Data> onResultListener;

    public BaseClientApiRequester(@NonNull OnResultCallback<Data> onResultListener) {
        this.onResultListener = onResultListener;
    }

    public OnResultCallback<Data> getOnResultListener() {
        return onResultListener;
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected void onFinish(BaseResult baseResult, JSONObject data) {
        if (baseResult.getCode() == OnResultCallback.RESULT_SUCCESS) {
            try {
                onResultListener.onSuccess(onDumpData(data), baseResult);
            } catch (JSONException e) {
                e.printStackTrace();
                baseResult.setCode(OnResultCallback.RESULT_PARSE_FAILED);
                onResultListener.onFail(baseResult);
            }
        } else {
            onResultListener.onFail(baseResult);
        }
        onResultListener.onFinish();
    }

    /**
     * 解析服务器数据  当且仅当 服务器返回成功才会执行本方法
     *
     * @param data 解析好的数据
     */
    protected abstract Data onDumpData(JSONObject data) throws JSONException;

    public void start() {
        doPost();
    }
}
