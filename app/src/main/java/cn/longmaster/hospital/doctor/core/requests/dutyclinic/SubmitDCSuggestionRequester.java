package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-上传诊疗建议
 * Created by yangyong on 2019-12-02.
 */
public class SubmitDCSuggestionRequester extends BaseApiUrlRequester<Void> {
    public int orderId;
    public int medicalId;
    public String patientName;
    public String phoneNum;
    public String cardNo;
    public String cureDt;
    public String diseaseName;
    public String diseaseDesc;
    public String inspect;
    public JSONArray presJsonArray;

    public SubmitDCSuggestionRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100588;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("order_id", orderId);
        params.put("medical_id", medicalId);
        params.put("patient_name", patientName);
        params.put("phone_num", phoneNum);
        params.put("card_no", cardNo);
        params.put("cure_dt", cureDt);
        params.put("disease_name", diseaseName);
        params.put("disease_desc", diseaseDesc);
        params.put("inspect", inspect);
        params.put("pres_list", presJsonArray);
    }
}
