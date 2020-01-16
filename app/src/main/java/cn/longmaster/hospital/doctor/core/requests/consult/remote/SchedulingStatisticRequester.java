package cn.longmaster.hospital.doctor.core.requests.consult.remote;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleStatisticInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.StatisticInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据医生ID和服务模式统计排班模式价格区间
 * Created by Yang² on 2016/8/20.
 */
public class SchedulingStatisticRequester extends SimpleHttpRequester<ScheduleStatisticInfo> {
    public int doctorId;//医生id
    public int serviceType;//服务模式	0 统计全部;101001远程会诊;101002 远程咨询;101003 远程会诊复诊;102001 远程影像会诊

    public SchedulingStatisticRequester(@NonNull OnResultListener<ScheduleStatisticInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_SCHEDULING_STATISTIC;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected ScheduleStatisticInfo onDumpData(JSONObject data) throws JSONException {
        ScheduleStatisticInfo scheduleStatisticInfo = new ScheduleStatisticInfo();
        if (data.has("remote_consult")) {
            scheduleStatisticInfo.setRemoteConsult(JsonHelper.toObject(data.getJSONObject("remote_consult"), StatisticInfo.class));
        }
        if (data.has("remote_advice")) {
            scheduleStatisticInfo.setRemoteAdvice(JsonHelper.toObject(data.getJSONObject("remote_advice"), StatisticInfo.class));
        }
        if (data.has("remote_consult_recure")) {
            scheduleStatisticInfo.setRemoteConsultRecure(JsonHelper.toObject(data.getJSONObject("remote_consult_recure"), StatisticInfo.class));
        }
        if (data.has("image_consult")) {
            scheduleStatisticInfo.setImageConsult(JsonHelper.toObject(data.getJSONObject("image_consult"), StatisticInfo.class));
        }
        return scheduleStatisticInfo;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("service_type", serviceType);
    }
}
