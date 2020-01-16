package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * httpdns域名解析ip结果上报
 */
public class SubmitDomainNameRequester extends SimpleHttpRequester<Void> {
    public int type;
    public String ipAddress;

    public SubmitDomainNameRequester(@NonNull OnResultListener<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SUBMIT_DOMAIN_NAME;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {

        return null;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("type", type);
        params.put("ip_address", ipAddress);
    }
}
