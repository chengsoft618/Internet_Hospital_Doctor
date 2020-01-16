package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-转诊单提交
 * Created by yangyong on 2019-12-02.
 */
public class SubmitDCReferralRequester extends BaseApiUrlRequester<Void> {
    public int orderId;
    public int medicalId;
    public String patientName;
    public String phoneNum;
    public String cardNo;
    public String cureDt;
    public int referral;
    public int receive;
    public String referralDesc;
    public String inspect;

    public SubmitDCReferralRequester(@NonNull OnResultCallback<Void> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Void onDumpData(JSONObject data) throws JSONException {
        return null;
    }

    @Override
    protected int getOpType() {
        return 100587;
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
        params.put("referral", referral);
        params.put("receive", receive);
        params.put("referral_desc", referralDesc);
        params.put("inspect", inspect);
    }
}
