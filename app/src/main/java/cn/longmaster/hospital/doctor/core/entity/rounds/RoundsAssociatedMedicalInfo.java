package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/2/20.
 */

public class RoundsAssociatedMedicalInfo implements Serializable {
    @JsonField("medical_id")
    private int medicalId;
    @JsonField("cure_dt")
    private String cureDt;
    @JsonField("doctor_name")
    private String doctorName;
    @JsonField("appointment_stat_remark")
    private String appointmentStatRemark;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAppointmentStatRemark() {
        return appointmentStatRemark;
    }

    public void setAppointmentStatRemark(String appointmentStatRemark) {
        this.appointmentStatRemark = appointmentStatRemark;
    }

    @Override
    public String toString() {
        return "RoundsAssociatedMedicalInfo{" +
                "medicalId=" + medicalId +
                ", cureDt='" + cureDt + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentStatRemark='" + appointmentStatRemark + '\'' +
                '}';
    }
}
