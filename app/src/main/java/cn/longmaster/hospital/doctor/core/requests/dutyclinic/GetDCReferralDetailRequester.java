package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCReferralDetailInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-订单转诊单详情
 * Created by yangyong on 2019-11-30.
 */
public class GetDCReferralDetailRequester extends BaseApiUrlRequester<DCReferralDetailInfo> {
    public int medicalId;

    public GetDCReferralDetailRequester(@NonNull OnResultCallback<DCReferralDetailInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected DCReferralDetailInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.optJSONObject("data"), DCReferralDetailInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100584;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("medical_id", medicalId);
    }
}
