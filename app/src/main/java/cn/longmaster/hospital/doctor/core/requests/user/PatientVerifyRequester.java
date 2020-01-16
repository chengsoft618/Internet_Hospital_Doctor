package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * Created by wanghaikun on 2017-03-14.
 * Mod by biao on 2019/7/16
 */
public class PatientVerifyRequester extends BaseClientApiRequester<Void> {
    // 就诊编号
    private String medicalId;
    // 患者姓名
    private String patientName;

    public PatientVerifyRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return 100207;
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
        params.put("appointment_id", medicalId);
        params.put("patient_name", patientName);
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
