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
 * 根据科室ID和日期区间获取排班/影像服务列表
 * Created by Yang² on 2016/8/20.
 */
public class SchedulingByDepartmentRequester extends SimpleHttpRequester<List<Integer>> {
    public int departmentId;//科室ID
    public int scheduingType;//排班类型	1：排班;2：影像服务
    public String beginDt;//开始时间 时间格式（年-月-日 时:分:秒
    public String endDt;//结束时间 时间格式（年-月-日 时:分:秒）
    public int symbol;//分页参数 非必传参数：从第几条开始获取*需要分页功能时必传
    public int pageSize;//分页尺寸 非必传参数：每页获取几条*需要分页功能时必传


    public SchedulingByDepartmentRequester(@NonNull OnResultListener<List<Integer>> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_GET_SCHEDULING_BY_DEPARTMENT;
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
        params.put("department_id", departmentId);
        params.put("scheduing_type", scheduingType);
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("symbol", symbol);
        params.put("page_size", pageSize);
    }
}
