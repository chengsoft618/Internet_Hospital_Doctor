package cn.longmaster.hospital.doctor.core.requests.appointment;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointRelateInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.SimpleHttpRequester;

/**
 * 根据医生ID和日期及发起人类型拉取医生的预约列表
 * Created by Yang² on 2016/8/20.
 */
public class AppointmentListByDoctorRequester extends SimpleHttpRequester<AppointRelateInfo> {
    public static final int FILTER_TYPE_ALL = 0;
    public static final int FILTER_TYPE_DAY = 1;
    public static final int FILTER_TYPE_MONTH = 2;

    public static final int SCHEDUING_TYPE_REMOTE = 1;//排班
    public static final int SCHEDUING_TYPE_IMG = 2;//影像服务

    public int doctorId;//医生id
    public int filterType;//获取类型	0：全部;1：按天拉取(排班时间);2：按月拉取(排班时间)
    public String datetime;//日期获取全部时传0;1：按天格式(2016-07-06);2：按月格式(2016-07)
    public int promoter = 0;//发起人类型 1：我收到的;2：我发起的 非必须：不传或0：我收到和我发起
    public int scheduingType = 0;//排班类型

    public AppointmentListByDoctorRequester(@NonNull OnResultListener<AppointRelateInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected String getUrl() {
        return AppConfig.getClientApiUrl();
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_APPOINTMENT_LIST_BY_DOCTOR;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected AppointRelateInfo onDumpData(JSONObject data) throws JSONException {
        return JsonHelper.toObject(data.getJSONObject("data"), AppointRelateInfo.class);
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("filter_type", filterType);
        params.put("datetime", datetime);
        params.put("promoter", promoter);
        params.put("scheduing_type", scheduingType);
    }
}
