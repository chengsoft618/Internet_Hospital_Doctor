package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 15:49
 * @description: 根据病历id更新随访计划
 */
public class UpdateVisitPlantsRequester extends BaseApiUrlRequester<Void> {
    //病历ID
    private int medicalId;
    //随访计划id
    private int tempId;

    /**
     * 删除节点id
     */
    private String delListIds;
    //随访计划详情
    private JSONArray followupTempList;

    public UpdateVisitPlantsRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100601;
    }

    @Override
    protected String getTaskId() {
        return "100601";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("temp_id", tempId);
        params.put("del_list_ids",delListIds);
        params.put("followup_temp_list", followupTempList);
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public void setDelListIds(String delListIds) {
        this.delListIds = delListIds;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public JSONArray getFollowupTempList() {
        return followupTempList;
    }

    public void setFollowupTempList(JSONArray followupTempList) {
        this.followupTempList = followupTempList;
    }
}
