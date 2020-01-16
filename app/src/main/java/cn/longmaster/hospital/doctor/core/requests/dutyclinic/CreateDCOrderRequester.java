package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-生成门诊订单
 * Created by yangyong on 2019-11-30.
 */
public class CreateDCOrderRequester extends BaseApiUrlRequester<Integer> {
    public int itemId;
    public int doctorId;
    public String patientName;
    public String phoneNum;
    public String cardNo;

    public CreateDCOrderRequester(@NonNull OnResultCallback<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        return data.optJSONObject("data").optInt("order_id");
    }

    @Override
    protected int getOpType() {
        return 100580;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("item_id", itemId);
        params.put("doctor_id", doctorId);
        params.put("patient_name", patientName);
        params.put("phone_num", phoneNum);
        params.put("card_no", cardNo);
    }
}
