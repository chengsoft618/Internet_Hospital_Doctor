package cn.longmaster.hospital.doctor.core.requests.dutyclinic;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCSuggestionDetailInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;

/**
 * 值班门诊-拉取订单诊疗建议详情
 * Created by yangyong on 2019-11-30.
 */
public class GetDCSuggestionDetailRequester extends BaseApiUrlRequester<DCSuggestionDetailInfo> {
    public int medicalId;

    public GetDCSuggestionDetailRequester(@NonNull OnResultCallback<DCSuggestionDetailInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected DCSuggestionDetailInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.optJSONObject("data"), DCSuggestionDetailInfo.class);
    }

    @Override
    protected int getOpType() {
        return 100585;
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
