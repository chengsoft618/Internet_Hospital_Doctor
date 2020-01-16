package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 聊天记录实体类
 * Created by Yang² on 2017/8/30.
 */

public class ChatMsgInfo implements Serializable {
    @JsonField("msg_id")
    private int msgId;
    @JsonField("group_id")
    private int groupId;//群组ID
    @JsonField("user_id")
    private int userId;
    @JsonField("msg_type")
    private int msgType;
    @JsonField("msg_content")
    private String msgContent;
    @JsonField("appointment_id")
    private int appointmentId;
    @JsonField("role")
    private int role;
    @JsonField("insert_dt")
    private String insertDt;

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
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
        return "ChatMsgInfo{" +
                "msgId=" + msgId +
                ", groupId=" + groupId +
                ", userId=" + userId +
                ", msgType=" + msgType +
                ", msgContent='" + msgContent + '\'' +
                ", appointmentId=" + appointmentId +
                ", role=" + role +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
