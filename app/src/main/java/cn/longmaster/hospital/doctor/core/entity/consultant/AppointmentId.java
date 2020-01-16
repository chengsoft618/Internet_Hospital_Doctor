package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 预约ID
 * Created by Yang² on 2017/12/28.
 */

public class AppointmentId implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public String toString() {
        return "AppointmentId{" +
                "appointmentId=" + appointmentId +
                '}';
    }
}
