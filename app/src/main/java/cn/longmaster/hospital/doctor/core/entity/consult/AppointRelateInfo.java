package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 预约相关信息
 * Created by JinKe on 2016-08-29.
 */
public class AppointRelateInfo implements Serializable {
    @JsonField("scheduing_list")
    private List<ScheduleInfo> mScheduleList;//排班信息
    @JsonField("appointment_list")
    private List<AppointmentIdInfo> mAppointmentList;//预约信息

    public List<ScheduleInfo> getScheduleList() {
        return mScheduleList;
    }

    public void setScheduleList(List<ScheduleInfo> scheduleList) {
        mScheduleList = scheduleList;
    }

    public List<AppointmentIdInfo> getAppointmentList() {
        return mAppointmentList;
    }

    public void setAppointmentList(List<AppointmentIdInfo> appointmentList) {
        mAppointmentList = appointmentList;
    }

    @Override
    public String toString() {
        return "AppointRelateInfo{" +
                "mAppointmentList=" + mAppointmentList +
                ", mScheduleList=" + mScheduleList +
                '}';
    }
}
