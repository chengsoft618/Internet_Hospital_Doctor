package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 校验病历编号及患者姓名
 * Created by W·H·K on 2018/7/19.
 * Mod by biao on 2019/7/16
 */

public class CheckRoundsMedicalRequester extends BaseApiUrlRequester<RoundsMedicalDetailsInfo> {
    //患者姓名
    private String patientName;
    //病历编号
    private String medicalId;
    //类型 0病历号；1住院号
    private int type;
    //首诊医院ID
    private String hospitalName;

    public CheckRoundsMedicalRequester(@NonNull OnResultCallback<RoundsMedicalDetailsInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return 100521;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected RoundsMedicalDetailsInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), RoundsMedicalDetailsInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("patient_name", patientName);
        params.put("medical_id", medicalId);
        params.put("type", type);
        params.put("hospital_name", hospitalName);
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}

