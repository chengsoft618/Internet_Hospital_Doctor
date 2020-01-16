package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 备注信息
 * Created by JinKe on 2016-07-28.
 */
public class CaseRemarkInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("user_id")
    private int userId;//用户id
    @JsonField("user_type")
    private int userType;//用户类型
    @JsonField("remark_desc")
    private String remarkDesc;//备注描述
    @JsonField("remark_dt")
    private String remarkDt;//备注插入时间

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getRemarkDesc() {
        return remarkDesc;
    }

    public void setRemarkDesc(String remarkDesc) {
        this.remarkDesc = remarkDesc;
    }

    public String getRemarkDt() {
        return remarkDt;
    }

    public void setRemarkDt(String remarkDt) {
        this.remarkDt = remarkDt;
    }

    @Override
    public String toString() {
        return "CaseRemarkInfo{" +
                "appointmentId=" + appointmentId +
                ", userId=" + userId +
                ", userType=" + userType +
                ", remarkDesc='" + remarkDesc + '\'' +
                ", remarkDt='" + remarkDt + '\'' +
                '}';
    }
}

