package cn.longmaster.hospital.doctor.core.requests.consult.record;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据预约ID拉取医嘱接口 requester
 * Created by JinKe on 2016-07-27.
 */
public class DoctorDiagnosisRequester extends SimpleHttpRequester<DoctorDiagnosisInfo> {
    public int appointmentId;//预约ID

    public DoctorDiagnosisRequester(@NonNull OnResultListener<DoctorDiagnosisInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_DIAGNOSIS;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected DoctorDiagnosisInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), DoctorDiagnosisInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("appointment_id", appointmentId);
    }
}
