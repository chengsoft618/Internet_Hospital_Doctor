package cn.longmaster.hospital.doctor.core.requests.consult.remote;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 修改排班 requester
 * Created by JinKe on 2016-07-28.
 */
public class ReviseScheduleRequester extends SimpleHttpRequester<Integer> {
    public int scheduingId;//排班ID
    public int doctorId;//医生ID
    public String beginDt;//开始时间
    public String endDt;//结束时间
    public JSONArray serviceInfoListnfo;//服务档位信息
    public int admissionNum;//排班人数
    public float admissionPrice;//排班价格

    public ReviseScheduleRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_REVISE_SCHEDULE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        return data.optInt("scheduing_id");
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("scheduing_id", scheduingId);
        params.put("doctor_id", doctorId);
        params.put("begin_dt", beginDt);
        params.put("end_dt", endDt);
        params.put("service_info", serviceInfoListnfo);
        params.put("admission_num", admissionNum);
        params.put("admission_price", admissionPrice);
    }
}
