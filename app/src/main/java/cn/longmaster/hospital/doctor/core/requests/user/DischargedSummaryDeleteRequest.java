package cn.longmaster.hospital.doctor.core.requests.user;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2019/9/23 14:24
 * @description: 删除当前病历下的住院小结
 */
public class DischargedSummaryDeleteRequest extends BaseApiUrlRequester<Void> {
    private String medicalId;
    private String fileName;

    public DischargedSummaryDeleteRequest(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100576;
    }

    @Override
    protected String getTaskId() {
        return "123";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
        params.put("file_name", fileName);
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
