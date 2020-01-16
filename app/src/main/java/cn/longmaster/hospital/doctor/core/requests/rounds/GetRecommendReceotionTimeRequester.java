package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 拉取推荐接诊时间requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetRecommendReceotionTimeRequester extends BaseApiUrlRequester<List<String>> {
    public int doctorId;
    public int timeSliceNum;

    public GetRecommendReceotionTimeRequester(@NonNull OnResultCallback<List<String>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_RECOMMEND_RECEPTION_TIME;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<String> onDumpData(JSONObject data) throws JSONException {
        List<String> cureDts = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                cureDts.add(jsonObject.optString("cure_dt"));
            }
        }
        return cureDts;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("time_slice_num", timeSliceNum);
    }
}
