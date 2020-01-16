package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by WangHaiKun on 2017/8/29.
 */

public class MemberListInfo implements Serializable {
    @JsonField("user_id")//成员ID
    private int userId;
    @JsonField("group_id")//群组ID
    private int groupId;
    @JsonField("appointment_id")//预约ID
    private int appointmentId;
    @JsonField("role")//角色类型
    private int role;
    @JsonField("insert_dt")//加入时间
    private String insertDt;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "MemberListInfo{" +
                "userId=" + userId +
                ", groupId=" + groupId +
                ", appointmentId=" + appointmentId +
                ", role=" + role +
                ", insertDt=" + insertDt +
                '}';
    }
}
