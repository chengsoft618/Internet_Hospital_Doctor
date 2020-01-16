package cn.longmaster.hospital.doctor.core.requests.consult;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 身份证验证requester
 * Created by Tengshuxiang on 2016-08-24.
 */
public class IDCardVerifyRequester extends SimpleHttpRequester<String> {
    public String idCard;//身份证给号
    public String realName;//诊室姓名

    public IDCardVerifyRequester(@NonNull OnResultListener<String> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getIvwsUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.IVWS_OPTYPE_IDCARD_VERIFY;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected String onDumpData(JSONObject data) throws JSONException {
        return data.optString("message", "");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("id_card", idCard);
        params.put("realname", realName);
    }
}
