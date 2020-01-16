package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 16:19
 * @description: 根据病历id及病程id删除病程
 */
public class DeleteMedicalDataRequester extends BaseApiUrlRequester<Void> {
    private int medicalId;
    private int recordId;

    public DeleteMedicalDataRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100596;
    }

    @Override
    protected String getTaskId() {
        return "100596";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("record_id", recordId);
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}
