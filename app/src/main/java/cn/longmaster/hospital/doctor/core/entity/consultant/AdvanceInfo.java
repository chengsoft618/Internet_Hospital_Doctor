package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by W·H·K on 2018/1/2.
 */

public class AdvanceInfo implements Serializable {
    private int hospitalId;
    public List<AdvanceAppointmentInfo> appointmentInfos = new ArrayList<>();

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public List<AdvanceAppointmentInfo> getAppointmentInfos() {
        return appointmentInfos;
    }

    public void setAppointmentInfos(List<AdvanceAppointmentInfo> appointmentInfos) {
        this.appointmentInfos = appointmentInfos;
    }

    @Override
    public String toString() {
        return "AdvanceInfo{" +
                "hospitalId=" + hospitalId +
                ", appointmentInfos=" + appointmentInfos +
                '}';
    }
}
