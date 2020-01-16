package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据复诊获取上级就诊信息 requester
 * Created by JinKe on 2016-07-27.
 */
public class SuperConsultRequester extends SimpleHttpRequester<Integer> {
    public int appointmentId;//预约ID

    public SuperConsultRequester(@NonNull OnResultListener<Integer> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SUPER_CONSULT_BY_RECURE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected Integer onDumpData(JSONObject data) throws JSONException {
        return data.getJSONObject("data").optInt("appointment_id", 0);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("department_id", appointmentId);
    }
}
