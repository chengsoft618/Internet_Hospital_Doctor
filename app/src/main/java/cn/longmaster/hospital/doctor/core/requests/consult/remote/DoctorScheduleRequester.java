package cn.longmaster.hospital.doctor.core.requests.consult.remote;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.DoctorScheduleListInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseClientApiRequester;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;

/**
 * 获取医生排班信息 requester
 * Created by JinKe on 2016-07-28.
 */
public class DoctorScheduleRequester extends BaseClientApiRequester<DoctorScheduleListInfo> {
    public final static int FILTER_TYPE_COMPLETE = 0;//获取完成和未完成列表
    public final static int FILTER_TYPE_LIMIT = 1;//获取有72小时限制数据
    private int doctorId;//医生ID
    private int scheduleType;//排班类型 1：排班 2：影像服务
    private int filterType;//获取类型
    @AppConstant.ServiceType.ScheduleServiceType
    private int serviceType;

    public DoctorScheduleRequester(@NonNull OnResultCallback<DoctorScheduleListInfo> onResultListener) {
        super(onResultListener);
    }

    @Override
    protected int getOpType() {
        return OpTypeConfig.CLIENTAPI_OPTYE_DOCTOR_SCHEDULE;
    }

    @Override
    protected String getTaskId() {
        return getOpType() + "";
    }

    @Override
    protected DoctorScheduleListInfo onDumpData(JSONObject data) throws JSONException {
        DoctorScheduleListInfo doctorScheduleListInfo = new DoctorScheduleListInfo();
        if (filterType == FILTER_TYPE_COMPLETE) {
            doctorScheduleListInfo.setCompleteScheduleList(JsonHelper.toList(data.getJSONArray("complete"), ScheduleOrImageInfo.class));
            doctorScheduleListInfo.setNocompleteScheduleList(JsonHelper.toList(data.getJSONArray("nocomplete"), ScheduleOrImageInfo.class));
        } else {
            doctorScheduleListInfo.setLimitScheduleList(JsonHelper.toList(data.getJSONArray("data"), ScheduleOrImageInfo.class));
        }
        return doctorScheduleListInfo;
    }

    @Override
    protected void onPutParams(Map<String, Object> params) {
        params.put("doctor_id", doctorId);
        params.put("scheduing_type", scheduleType);
        params.put("filter_type", filterType);
        params.put("service_type", serviceType);
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
