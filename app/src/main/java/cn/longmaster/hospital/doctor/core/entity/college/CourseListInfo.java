package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/26.
 */

public class CourseListInfo implements Serializable {
    @JsonField("course_id")
    private int courseId;
    @JsonField("course_title")
    private String courseTitle;
    @JsonField("appointment_id")
    private int appointmentId;
    @JsonField("department_id")
    private int departmentId;
    @JsonField("hospital_id")
    private int hospitalId;
    @JsonField("doctor_id")
    private int doctorId;
    @JsonField("thumbnail")
    private String thumbnail;
    @JsonField("is_recommend")
    private int isRecommend;
    @JsonField("is_medical_auth")
    private int medicalAuth;
    @JsonField("course_introduce")
    private String courseIntroduce;
    @JsonField("file_name")
    private String fileName;
    @JsonField("file_from")
    private String fileFrom;
    @JsonField("file_size")
    private String fileSize;
    @JsonField("is_display")
    private int isDisplay;
    @JsonField("play_total")
    private int playTotal;
    @JsonField("likes_total")
    private int likesTotal;
    @JsonField("sort_num")
    private int sortNum;
    @JsonField("label_id")
    private int labelId;
    @JsonField("doctor_name")
    private String doctorName;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("department_name")
    private String departmentName;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getMedicalAuth() {
        return medicalAuth;
    }

    public void setMedicalAuth(int medicalAuth) {
        this.medicalAuth = medicalAuth;
    }

    public String getCourseIntroduce() {
        return courseIntroduce;
    }

    public void setCourseIntroduce(String courseIntroduce) {
        this.courseIntroduce = courseIntroduce;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileFrom() {
        return fileFrom;
    }

    public void setFileFrom(String fileFrom) {
        this.fileFrom = fileFrom;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public int getPlayTotal() {
        return playTotal;
    }

    public void setPlayTotal(int playTotal) {
        this.playTotal = playTotal;
    }

    public int getLikesTotal() {
        return likesTotal;
    }

    public void setLikesTotal(int likesTotal) {
        this.likesTotal = likesTotal;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "CourseListInfo{" +
                "courseId=" + courseId +
                ", courseTitle='" + courseTitle + '\'' +
                ", appointmentId=" + appointmentId +
                ", departmentId=" + departmentId +
                ", hospitalId=" + hospitalId +
                ", doctorId=" + doctorId +
                ", thumbnail='" + thumbnail + '\'' +
                ", isRecommend=" + isRecommend +
                ", medicalAuth=" + medicalAuth +
                ", courseIntroduce='" + courseIntroduce + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileFrom=" + fileFrom +
                ", fileSize=" + fileSize +
                ", isDisplay=" + isDisplay +
                ", playTotal=" + playTotal +
                ", likesTotal=" + likesTotal +
                ", sortNum=" + sortNum +
                ", labelId=" + labelId +
                ", doctorName='" + doctorName + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
