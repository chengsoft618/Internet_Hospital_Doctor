package cn.longmaster.hospital.doctor.core.requests.appointment;

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
 * 获取影像服务预约列表
 * Created by Yang² on 2016/7/28.
 */
public class ServiceAppointmentRequester extends SimpleHttpRequester<List<Integer>> {
    public int scheduingId;//排班ID
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public ServiceAppointmentRequester(@NonNull OnResultListener<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SERVICE_APPOINTMENT;
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
                list.add(jsonObject.optInt("appointment_id", 0));
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("scheduing_id", scheduingId);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
