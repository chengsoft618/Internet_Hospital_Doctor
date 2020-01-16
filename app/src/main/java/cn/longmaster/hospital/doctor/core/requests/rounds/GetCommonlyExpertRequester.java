package cn.longmaster.hospital.doctor.core.requests.rounds;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.entity.rounds.CommonlyDoctorInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseApiUrlRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取常用专家列表requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetCommonlyExpertRequester extends BaseApiUrlRequester<List<CommonlyDoctorInfo>> {
    public int departmentId;
    public int pageIndex;
    public int pageSize;

    public GetCommonlyExpertRequester(@NonNull OnResultCallback<List<CommonlyDoctorInfo>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_COMMONLY_EXPERT_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<CommonlyDoctorInfo> onDumpData(JSONObject data) throws JSONException {
        List<CommonlyDoctorInfo> list = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CommonlyDoctorInfo info = new CommonlyDoctorInfo();
                info.setDoctorId(jsonObject.optInt("doctor_id", 0));
                info.setScore(jsonObject.optDouble("score", 0));
                list.add(info);
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("attdep_id", departmentId);
        params.put("pageindex", pageIndex);
        params.put("pagesize", pageSize);
    }
}
