package cn.longmaster.hospital.doctor.core.requests.config;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DrugPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.HistoryInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.OperationPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.PhysicalPlanInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 患者基本信息 requester
 * Created by JinKe on 2016-07-26.
 */
public class PatientBaseRequester extends BaseClientApiRequester<PatientInfo> {
    public String token;//同步token标识
    public int appointmentId;//预约ID

    public PatientBaseRequester(@NonNull OnResultCallback<PatientInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_PATIENT_BASE_INFO;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected PatientInfo onDumpData(JSONObject data) throws JSONException {
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPatientBaseInfo(JsonHelper.toObject(data.getJSONObject("data"), PatientBaseInfo.class));
        patientInfo.setPhysicalPlanList(JsonHelper.toList(data.getJSONArray("physical_plan"), PhysicalPlanInfo.class));
        patientInfo.setOperationPlanList(JsonHelper.toList(data.getJSONArray("operation_plan"), OperationPlanInfo.class));
        patientInfo.setDrugPlanList(JsonHelper.toList(data.getJSONArray("drug_plan"), DrugPlanInfo.class));
        patientInfo.setHistoryInfoList(JsonHelper.toList(data.getJSONArray("history_info"), HistoryInfo.class));
        return patientInfo;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("token", token);
        params.put("appointment_id", appointmentId);
    }
}
