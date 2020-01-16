package cn.longmaster.hospital.doctor.core.requests.login;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.common.AgentServiceInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 请求代理服务器
 * Created by JinKe on 2017-02-21.
 */

public class AgentServiceRequester extends SimpleHttpRequester<AgentServiceInfo> {
    public String sign;

    public AgentServiceRequester(@NonNull OnResultListener<AgentServiceInfo> onResultListener) {
        super(onResultListener);
        sign = "";
    }

    @Override
    protected String getUrl() {
        return AppConfig.getDuwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.DUWS_OPTYPE_AGENT_SERVICE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AgentServiceInfo onDumpData(JSONObject data) throws JSONException {
        String JSONData = data.optString("data").toString();
        if ("".equals(JSONData)) {
            return new AgentServiceInfo();
        }
        return JsonHelper.toObject(data.getJSONObject("data"), AgentServiceInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("sign", sign);
    }
}
