package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class CourseContentInfo implements Serializable {
    @JsonField("course_id")
    private int courseId;//课程id
    @JsonField("item_id")
    private int itemId;
    @JsonField("stage_id")
    private int stageId;
    @JsonField("course_type")
    private String courseType;//课程类型
    @JsonField("course_name")
    private String courseName;//课程名称
    @JsonField("class_num")
    private int classNum;//课时数
    @JsonField("remote_type")
    private String remoteType;//实地/远程 1远程，2实地
    @JsonField("class_prepar")
    private String classPrepar;//课前准备
    @JsonField("appoint_type")
    private int appointType;//预约系统类型 1会诊系统，3新查房系统
    @JsonField("appoint_num")
    private int appointNum;//已约课时
    @JsonField("insert_dt")
    private String insertDt;//插入时间

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

    public String getRemoteType() {
        return remoteType;
    }

    public void setRemoteType(String remoteType) {
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

    public int getAppointNum() {
        return appointNum;
    }

    public void setAppointNum(int appointNum) {
        this.appointNum = appointNum;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "CourseContentInfo{" +
                "courseId=" + courseId +
                ", itemId=" + itemId +
                ", stageId=" + stageId +
                ", courseType='" + courseType + '\'' +
                ", courseName='" + courseName + '\'' +
                ", classNum=" + classNum +
                ", remoteType='" + remoteType + '\'' +
                ", classPrepar='" + classPrepar + '\'' +
                ", appointType=" + appointType +
                ", appointNum=" + appointNum +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
