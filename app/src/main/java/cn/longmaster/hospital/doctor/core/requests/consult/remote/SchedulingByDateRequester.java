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
 * 根据日期获取排班
 * Created by Yang² on 2016/7/28.
 */
public class SchedulingByDateRequester extends SimpleHttpRequester<List<Integer>> {
    public int doctorId;//医生ID
    public String beginDt;//开始时间
    public String endDt;//结束时间
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public SchedulingByDateRequester(@NonNull OnResultListener<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SCHEDULING_BY_DATE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected List<Integer> onDumpData(JSONObject data) throws JSONException {
        List<Integer> list = new ArrayList<>();
        JSONArray jsonArray = data.getJSONArray("data");
        if (jsonArray != null && jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                list.add(jsonObject.optInt("scheduing_id", 0));
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
