package cn.longmaster.hospital.doctor.core.entity.consult;

/**
 * 筛选专用
 * Created by Yang² on 2016/8/25.
 */
public class AppointmentStatus {
    private String appointmentStatus;
    private int statusType;

    public AppointmentStatus() {
        super();
    }

    public AppointmentStatus(String appointmentStatus, int statusType) {
        super();
        this.appointmentStatus = appointmentStatus;
        this.statusType = statusType;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public int getStatusType() {
        return statusType;
    }

    public void setStatusType(int statusType) {
        this.statusType = statusType;
    }
}
