package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 14:49
 * @description: 根据病历id更新病历未读状态
 */
public class UpdateMedicalDataStateRequester extends BaseApiUrlRequester<Void> {

    private int medicalId;

    public UpdateMedicalDataStateRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100593;
    }

    @Override
    protected String getTaskId() {
        return "100593";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }
}
