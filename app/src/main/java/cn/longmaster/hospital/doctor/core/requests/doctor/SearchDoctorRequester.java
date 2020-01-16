package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.entity.rounds.SearchDoctorInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 据医生姓名搜索医生 requester
 * Created by JinKe on 2016-07-27.
 */
public class SearchDoctorRequester extends BaseClientApiRequester<List<SearchDoctorInfo>> {
    public String realName;//医生姓名

    public SearchDoctorRequester(@NonNull OnResultCallback<List<SearchDoctorInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SEARCH_DOCTOR_BY_NAME;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<SearchDoctorInfo> onDumpData(JSONObject data) throws JSONException {
        List<SearchDoctorInfo> doctorIds = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SearchDoctorInfo info = new SearchDoctorInfo();
                info.setDoctorId(jsonObject.optInt("user_id", 0));
                info.setScore(jsonObject.optInt("score", 0));
                doctorIds.add(info);
            }
        }
        return doctorIds;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("real_name", realName);
    }
}
