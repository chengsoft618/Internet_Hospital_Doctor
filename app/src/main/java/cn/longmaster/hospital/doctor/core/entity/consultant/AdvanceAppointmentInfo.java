package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

/**
 * 垫付的预约信息
 * Created by W·H·K on 2018/1/2.
 */

public class AdvanceAppointmentInfo extends AdvanceInfo implements Serializable {
    private int appointmentId;
    private String patientName;
    private float payValue;
    private int hospitalId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public float getPayValue() {
        return payValue;
    }

    public void setPayValue(float payValue) {
        this.payValue = payValue;
    }

    @Override
    public int getHospitalId() {
        return hospitalId;
    }

    @Override
    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public String toString() {
        return "AdvanceAppointmentInfo{" +
                "appointmentId=" + appointmentId +
                ", patientName='" + patientName + '\'' +
                ", payValue=" + payValue +
                ", hospitalId=" + hospitalId +
                '}';
    }
}
