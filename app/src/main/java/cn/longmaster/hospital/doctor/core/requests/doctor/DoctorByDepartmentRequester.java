package cn.longmaster.hospital.doctor.core.requests.doctor;

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
 * 按科室ID拉取医生列表
 * Created by Yang² on 2016/7/27.
 */
public class DoctorByDepartmentRequester extends SimpleHttpRequester<List<Integer>> {
    public int departmentId;//科室ID
    public int region;//职称等级	0：全部;1：主任医师;2：副主任医师;3：主治医师;4：其他
    public int symbol;//分页参数
    public int pageSize;//分页尺寸

    public DoctorByDepartmentRequester(@NonNull OnResultListener<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_BY_DEPARTMENT;
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
                list.add(jsonObject.optInt("user_id", 0));
            }
        }
        return list;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("department_id", departmentId);
        params.put("region", region);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
