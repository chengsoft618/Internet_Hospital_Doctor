package cn.longmaster.hospital.doctor.core.requests.doctor;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.AppointmentStatisticDataInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 统计医生的排班、影像服务、就诊、复诊数量
 * Created by Yang² on 2016/7/28.
 */
public class AppointmentStatisticDataRequester extends SimpleHttpRequester<AppointmentStatisticDataInfo> {
    public static final int FILTER_TYPE_ALL = 0;
    public static final int FILTER_TYPE_DAY = 1;
    public static final int FILTER_TYPE_MONTH = 2;

    public int doctorId;//医生ID
    public int filterType;//统计数据类型	0：统计全部;1：按天统计(排班时间);2：按月统计(排班时间)
    public String filterDate;//日期统计时间	统计全部时传0;按天格式：yyyy-mm-dd;按月格式：yyyy-mm

    public AppointmentStatisticDataRequester(@NonNull OnResultListener<AppointmentStatisticDataInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_CONSULT_STATISTIC;
    }

    @Override
    protected String getTaskId() {
        return filterDate;
    }

    @Override
    protected AppointmentStatisticDataInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data, AppointmentStatisticDataInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("filter_type", filterType);
        params.put("filter_date", filterDate);
    }
}
