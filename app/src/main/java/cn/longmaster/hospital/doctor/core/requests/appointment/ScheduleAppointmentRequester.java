package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取排班预约列表 requester
 * Created by JinKe on 2016-07-28.
 */
public class ScheduleAppointmentRequester extends BaseClientApiRequester<List<Integer>> {
    public int scheduingId;//预约状态
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public ScheduleAppointmentRequester(@NonNull OnResultCallback<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SHCEDULE_APPOINTMENT_LIST;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<Integer> onDumpData(JSONObject data) throws JSONException {
        List<Integer> appointmentIds = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                appointmentIds.add(jsonObject.optInt("appointment_id", 0));
            }
        }
        return appointmentIds;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("scheduing_id", scheduingId);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
