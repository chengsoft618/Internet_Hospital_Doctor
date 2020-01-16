package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.entity.rounds.RecommendInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取推荐专家列表requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetRecommendExpertRequester extends BaseApiUrlRequester<RecommendInfo> {
    public int departmentId;
    public int symbol;
    public int region = 0;
    public int pageSize;

    public GetRecommendExpertRequester(@NonNull OnResultCallback<RecommendInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_RECOMMEND_DOCTOR_DEPARTMENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected RecommendInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), RecommendInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("department_id", departmentId);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
        params.put("region", region);
    }
}
