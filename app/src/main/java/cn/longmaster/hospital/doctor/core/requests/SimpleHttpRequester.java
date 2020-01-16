package cn.longmaster.hospital.doctor.core.requests;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.hospital.doctor.core.AppConfig;

/**
 * 封装 简单的HTTP请求
 * Created by yangyong on 16/4/27.
 *
 * @see BaseApiUrlRequester
 * @see BaseClientApiRequester
 * @see BaseDuwsUrlRequester
 */
@Deprecated
public abstract class SimpleHttpRequester<Data> extends HttpRequester {
    private OnResultListener<Data> onResultListener;

    public SimpleHttpRequester(@NonNull OnResultListener<Data> onResultListener) {
        this.onResultListener = onResultListener;
    }

    public void setOnResultListener(OnResultListener<Data> onResultListener) {
        this.onResultListener = onResultListener;
    }

    public OnResultListener<Data> getOnResultListener() {
        return onResultListener;
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return 0;
    }

    @Override
    protected String getTaskId() {
        return "";
    }

    @Override
    protected void onFinish(BaseResult baseResult, JSONObject data) {
        Data serverData = null;
        if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS || baseResult.getCode() == OnResultListener.RESULT_DB_EXISTED) {
            try {
                serverData = onDumpData(data);
            } catch (JSONException e) {
                e.printStackTrace();
                baseResult.setCode(OnResultListener.RESULT_FAILED);
            }
        }
        onResultListener.onResult(baseResult, serverData);
    }

    /**
     * 解析服务器数据  当且仅当 服务器返回成功才会执行本方法
     *
     * @param data 解析好的数据
     */
    protected abstract Data onDumpData(JSONObject data) throws JSONException;
}
