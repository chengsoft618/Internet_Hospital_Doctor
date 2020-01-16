package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * Created by lm-pc on 16/7/26.
 */
public class HospitalRequester extends SimpleHttpRequester<HospitalInfo> {
    public String token;//同步token标识
    public int hospitalId;//医院ID

    public HospitalRequester(@NonNull OnResultListener<HospitalInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_HOSPITAL_INFO;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected HospitalInfo onDumpData(JSONObject data) throws JSONException {
        String JSONData = data.optString("data").toString();
        if ("".equals(JSONData)) {
            return new HospitalInfo();
        }
        return JsonHelper.toObject(data.getJSONObject("data"), HospitalInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
        params.put("hospital_id", hospitalId);
    }
}
