package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class ProjectAppointTimeInfo implements Serializable {
    @JsonField("course_appid")
    private int courseAppid;//课程预约ID
    @JsonField("course_id")
    private int courseId;//课程ID
    @JsonField("item_id")
    private int itemId;
    @JsonField("stage_id")
    private int stageId;
    @JsonField("appoint_time")
    private String appointTime;//预约时间
    @JsonField("user_id")
    private int userId;
    @JsonField("upd_dt")
    private String updDt;//后台更新时间
    @JsonField("course_type")
    private String courseType;//课程类型
    @JsonField("course_name")
    private String courseName;//课程内容
    @JsonField("class_num")
    private int classNum;//课时
    @JsonField("remote_type")
    private int remoteType;//实地/远程 1远程，2实地
    @JsonField("class_prepar")
    private String classPrepar;//课前准备
    @JsonField("appoint_type")
    private int appointType;///预约系统类型 1会诊系统，3新查房系统
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("appointment_stat_remark")
    private String appointmentStatRemark;//预约状态
    @JsonField("appoint_num")
    private int appointNum;//已约课时

    public int getCourseAppid() {
        return courseAppid;
    }

    public void setCourseAppid(int courseAppid) {
        this.courseAppid = courseAppid;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public int getRemoteType() {
        return remoteType;
    }

    public void setRemoteType(int remoteType) {
        this.remoteType = remoteType;
    }

    public String getClassPrepar() {
        return classPrepar;
    }

    public void setClassPrepar(String classPrepar) {
        this.classPrepar = classPrepar;
    }

    public int getAppointType() {
        return appointType;
    }

    public void setAppointType(int appointType) {
        this.appointType = appointType;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentStatRemark() {
        return appointmentStatRemark;
    }

    public void setAppointmentStatRemark(String appointmentStatRemark) {
        this.appointmentStatRemark = appointmentStatRemark;
    }

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    @Override
    public String toString() {
        return "ProjectAppointTimeInfo{" +
                "courseAppid=" + courseAppid +
                ", courseId=" + courseId +
                ", itemId=" + itemId +
                ", stageId=" + stageId +
                ", appointTime='" + appointTime + '\'' +
                ", userId=" + userId +
                ", updDt='" + updDt + '\'' +
                ", courseType='" + courseType + '\'' +
                ", courseName='" + courseName + '\'' +
                ", classNum=" + classNum +
                ", remoteType=" + remoteType +
                ", classPrepar='" + classPrepar + '\'' +
                ", appointType=" + appointType +
                ", appointmentId=" + appointmentId +
                ", appointmentStatRemark='" + appointmentStatRemark + '\'' +
                ", appointNum=" + appointNum +
                '}';
    }
}
