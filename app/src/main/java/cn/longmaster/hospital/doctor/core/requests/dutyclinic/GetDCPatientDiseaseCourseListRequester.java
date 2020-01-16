package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 10:32
 * @description: 根据病历id拉取患者病程
 */
public class GetDCPatientDiseaseCourseListRequester extends BaseApiUrlRequester<List<DCDutyPatientDiseaseItemInfo>> {
    private int medicalId;

    public GetDCPatientDiseaseCourseListRequester(@NonNull OnResultCallback<List<DCDutyPatientDiseaseItemInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyPatientDiseaseItemInfo> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.optJSONArray("data"), DCDutyPatientDiseaseItemInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100594;
    }

    @Override
    protected String getTaskId() {
        return "100594";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }
}
