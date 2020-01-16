package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 根据医生ID和日期及发起人类型拉取医生的预约列表
 * Created by Yang² on 2016/8/20.
 */
public class AppointmentIdInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约编号
    @JsonField("scheduing_id")
    private int scheduingId;//排班id
    @JsonField("serial_number")
    private int serialNumber;//就诊序号
    @JsonField("promoter")
    private int promoter;//发起人类型 1：我收到的 2：我发起的

    private int dataType = 0;//数据类型,只用作界面显示,不是服务器回调数据
    private int num = 0;//预约个数,只用作界面显示,不是服务器回调数据
    private String time = "";//时间,只用作界面显示,不是服务器回调数据

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public int getPromoter() {
        return promoter;
    }

    public void setPromoter(int promoter) {
        this.promoter = promoter;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AppointmentIdInfo{" +
                "appointmentId=" + appointmentId +
                ", scheduingId=" + scheduingId +
                ", serialNumber=" + serialNumber +
                ", promoter=" + promoter +
                ", dataType=" + dataType +
                ", num=" + num +
                ", time='" + time + '\'' +
                '}';
    }
}
