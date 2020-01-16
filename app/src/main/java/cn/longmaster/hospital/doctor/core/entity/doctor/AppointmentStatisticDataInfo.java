package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 统计医生的排班、影像服务、就诊、复诊数量
 * Created by Yang² on 2016/7/28.
 */
public class AppointmentStatisticDataInfo implements Serializable {
    @JsonField("scheduing_count")
    private int scheduingCount;//排班数量
    @JsonField("scheduingImg_count")
    private int scheduingImgCount;//影像服务数量
    @JsonField("appointment_count")
    private int appointmentCount;//预约数量
    @JsonField("recure_count")
    private int recureCount;//复诊数量
    @JsonField("receive_count")
    private int receiveCount;//收到的
    @JsonField("launch_count")
    private int launchCount;//发起的

    public int getScheduingCount() {
        return scheduingCount;
    }

    public void setScheduingCount(int scheduingCount) {
        this.scheduingCount = scheduingCount;
    }

    public int getScheduingImgCount() {
        return scheduingImgCount;
    }

    public void setScheduingImgCount(int scheduingImgCount) {
        this.scheduingImgCount = scheduingImgCount;
    }

    public int getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(int appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public int getRecureCount() {
        return recureCount;
    }

    public void setRecureCount(int recureCount) {
        this.recureCount = recureCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    @Override
    public String toString() {
        return "AppointmentStatisticDataInfo{" +
                "scheduingCount=" + scheduingCount +
                ", scheduingImgCount=" + scheduingImgCount +
                ", appointmentCount=" + appointmentCount +
                ", recureCount=" + recureCount +
                ", receiveCount=" + receiveCount +
                ", launchCount=" + launchCount +
                '}';
    }
}
