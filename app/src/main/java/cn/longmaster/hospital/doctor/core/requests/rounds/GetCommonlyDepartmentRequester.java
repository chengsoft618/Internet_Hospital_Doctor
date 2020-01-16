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
 * 获取常用科室列表requester
 * <p>
 * Created by W·H·K on 2018/5/14.
 */

public class GetCommonlyDepartmentRequester extends BaseApiUrlRequester<List<Integer>> {

    public GetCommonlyDepartmentRequester(@NonNull OnResultCallback<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_COMMONLY_DEPARTMENT;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<Integer> onDumpData(JSONObject data) throws JSONException {
        List<Integer> departmentIds = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                departmentIds.add(jsonObject.optInt("department_id", 0));
            }
        }
        return departmentIds;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
    }
}
