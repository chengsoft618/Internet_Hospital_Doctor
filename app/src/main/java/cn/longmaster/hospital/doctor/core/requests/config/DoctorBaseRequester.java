package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 医生基本信息 requester
 * Created by JinKe on 2016-07-26.
 */
public class DoctorBaseRequester extends SimpleHttpRequester<DoctorBaseInfo> {
    public String token;//同步token标识
    public int doctorId;//医生ID

    public DoctorBaseRequester(@NonNull OnResultListener<DoctorBaseInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_BASE_INFO;
    }

    @Override
    protected String getTaskId() {
        return super.getTaskId();
    }

    @Override
    protected DoctorBaseInfo onDumpData(JSONObject data) throws JSONException {
        String JSONData = data.optString("data").toString();
        if ("".equals(JSONData)) {
            return new DoctorBaseInfo();
        }
        return JsonHelper.toObject(data.getJSONObject("data"), DoctorBaseInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
        params.put("doctor_id", doctorId);
    }
}
