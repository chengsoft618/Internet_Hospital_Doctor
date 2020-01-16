package cn.longmaster.hospital.doctor.core.requests.consult.remote;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 获取已完成排班列表 requester
 * Created by JinKe on 2016-07-28.
 */
public class FinishedScheduleRequester extends SimpleHttpRequester<List<Integer>> {
    public int doctorId;//医生ID
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public FinishedScheduleRequester(@NonNull OnResultListener<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_FINISHED_SCHEDULE_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<Integer> onDumpData(JSONObject data) throws JSONException {
        List<Integer> scheduleIds = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                scheduleIds.add(jsonObject.optInt("scheduing_id", 0));
            }
        }
        return scheduleIds;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
