package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/7/16 13:51
 * @description: 查看当前病历下所有自己上传的患者材料
 */
public class PatientDataDetailGetRequest extends BaseApiUrlRequester<PatientDataInfo> {
    private String medicalId;

    public PatientDataDetailGetRequest(@NonNull OnResultCallback<PatientDataInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected PatientDataInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), PatientDataInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100562;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }
}
