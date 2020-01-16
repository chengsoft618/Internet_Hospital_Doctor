package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/7/31.
 */

public class RelationMedicalInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;
    @JsonField("insert_dt")
    private String insertDt;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "RelationMedicalInfo{" +
                "appointmentId=" + appointmentId +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
