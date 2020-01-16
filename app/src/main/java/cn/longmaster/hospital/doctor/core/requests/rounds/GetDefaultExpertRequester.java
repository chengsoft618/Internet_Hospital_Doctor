package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.entity.rounds.RecommendDoctorInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取推荐专家列表requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetDefaultExpertRequester extends BaseApiUrlRequester<List<RecommendDoctorInfo>> {
    public int filterType;
    public int symbol;
    public int region = 0;
    public int pageSize;

    public GetDefaultExpertRequester(@NonNull OnResultCallback<List<RecommendDoctorInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_RECOMMEND_DOCTOR_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<RecommendDoctorInfo> onDumpData(JSONObject data) throws JSONException {
        List<RecommendDoctorInfo> list = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                RecommendDoctorInfo info = new RecommendDoctorInfo();
                info.setDoctorId(object.optInt("user_id", 0));
                info.setScore(object.optInt("score", 0));
                list.add(info);
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
