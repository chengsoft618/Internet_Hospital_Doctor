package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 14:48
 * @description: 根据病历id拉取患者随访计划
 */
public class GetDCDutyVisitPlantRequester extends BaseApiUrlRequester<List<DCDutyVisitPlantTempItem>> {
    private int medicalId;

    public GetDCDutyVisitPlantRequester(@NonNull OnResultCallback<List<DCDutyVisitPlantTempItem>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected List<DCDutyVisitPlantTempItem> onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toList(data.getJSONArray("data"), DCDutyVisitPlantTempItem.class);
    }

    @Override
    protected int getOpType() {
        return 100599;
    }

    @Override
    protected String getTaskId() {
        return "100599";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }
}
