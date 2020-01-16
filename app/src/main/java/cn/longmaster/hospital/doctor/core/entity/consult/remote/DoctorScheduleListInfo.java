package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;
import java.util.List;

/**
 * 根据医生ID获取医生的排班/影像服务列表
 * Created by Yang² on 2016/8/3.
 */
public class DoctorScheduleListInfo implements Serializable {
    private List<ScheduleOrImageInfo> completeScheduleList;
    private List<ScheduleOrImageInfo> nocompleteScheduleList;
    private List<ScheduleOrImageInfo> limitScheduleList;

    public List<ScheduleOrImageInfo> getCompleteScheduleList() {
        return completeScheduleList;
    }

    public void setCompleteScheduleList(List<ScheduleOrImageInfo> completeScheduleList) {
        this.completeScheduleList = completeScheduleList;
    }

    public List<ScheduleOrImageInfo> getNocompleteScheduleList() {
        return nocompleteScheduleList;
    }

    public void setNocompleteScheduleList(List<ScheduleOrImageInfo> nocompleteScheduleList) {
        this.nocompleteScheduleList = nocompleteScheduleList;
    }

    public List<ScheduleOrImageInfo> getLimitScheduleList() {
        return limitScheduleList;
    }

    public void setLimitScheduleList(List<ScheduleOrImageInfo> limitScheduleList) {
        this.limitScheduleList = limitScheduleList;
    }

    @Override
    public String toString() {
        return "DoctorScheduleListInfo{" +
                "completeScheduleList=" + completeScheduleList +
                ", nocompleteScheduleList=" + nocompleteScheduleList +
                ", limitScheduleList=" + limitScheduleList +
                '}';
    }
}
