package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * @author ABiao_Abiao
 * @date 2020/1/3 14:22
 * @description: 发送转账单/诊疗建议确认短信
 */
public class SendMsgForIssueAndSuggestRequester extends BaseApiUrlRequester<Void> {
    //病例ID
    private int medicalId;
    //0：转诊单 1：诊疗建议
    private int type;
    public SendMsgForIssueAndSuggestRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100603;
    }

    @Override
    protected String getTaskId() {
        return "100603";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id",medicalId);
        params.put("send_type",type);
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
