package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 互动实体类
 * Created by Yang² on 2018/3/27.
 */

public class InteractionInfo implements Serializable {
    @JsonField("msg_id")
    private int msgId;//评论消息ID
    @JsonField("course_id")
    private int courseId;//课程ID
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("user_id")
    private int userId;//
    @JsonField("msg_type")
    private int msgType;//评论类型 301评论[发言] 302提问 303被举手占用
    @JsonField("msg_content")
    private String msgContent;//评论内容
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

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

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "InteractionInfo{" +
                "msgId=" + msgId +
                ", courseId=" + courseId +
                ", appointmentId=" + appointmentId +
                ", userId=" + userId +
                ", msgType=" + msgType +
                ", msgContent='" + msgContent + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
