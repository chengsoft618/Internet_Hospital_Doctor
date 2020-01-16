package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;

/**
 * Created by W·H·K on 2018/5/9.
 */

public class RoundsAppointmentInfo implements Serializable {
    //首诊专家ID
    private int doctorId;
    //医生姓名
    private String doctorName;
    private String doctorHospital;
    private String doctorDepartment;
    //医生职称
    private String doctorLevel;
    private String lectureTopics;
    private String appeal;
    private String lengthTime;
    private boolean needPPT;
    private List<RoundsMedicalDetailsInfo> medicalInfoList;
    private List<Integer> medicalList;
    private List<String> intentionTimeList;
    private List<DepartmentListInfo> intentionDepartmentList;
    //科室ID
    private List<Integer> departmentIdList;
    //分诊备注
    private String remarks;
    //-1：未选择1：分诊0：选择专家
    private int roundsType;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorHospital() {
        return doctorHospital;
    }

    public void setDoctorHospital(String doctorHospital) {
        this.doctorHospital = doctorHospital;
    }

    public String getDoctorDepartment() {
        return doctorDepartment;
    }

    public void setDoctorDepartment(String doctorDepartment) {
        this.doctorDepartment = doctorDepartment;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getLectureTopics() {
        return lectureTopics;
    }

    public void setLectureTopics(String lectureTopics) {
        this.lectureTopics = lectureTopics;
    }

    public String getAppeal() {
        return appeal;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public String getLengthTime() {
        return lengthTime;
    }

    public void setLengthTime(String lengthTime) {
        this.lengthTime = lengthTime;
    }

    public void setNeedPPT(boolean needPPT) {
        this.needPPT = needPPT;
    }

    public boolean isNeedPPT() {
        return needPPT;
    }

    public List<RoundsMedicalDetailsInfo> getMedicalInfoList() {
        return medicalInfoList;
    }

    public void setMedicalInfoList(List<RoundsMedicalDetailsInfo> medicalInfoList) {
        this.medicalInfoList = medicalInfoList;
    }

    public List<Integer> getMedicalList() {
        return medicalList;
    }

    public void setMedicalList(List<Integer> medicalList) {
        this.medicalList = medicalList;
    }

    public List<String> getIntentionTimeList() {
        return intentionTimeList;
    }

    public void setIntentionTimeList(List<String> intentionTimeList) {
        this.intentionTimeList = intentionTimeList;
    }

    public List<DepartmentListInfo> getIntentionDepartmentList() {
        return intentionDepartmentList;
    }

    public void setIntentionDepartmentList(List<DepartmentListInfo> intentionDepartmentList) {
        this.intentionDepartmentList = intentionDepartmentList;
    }

    public List<Integer> getDepartmentIdList() {
        return departmentIdList;
    }

    public void setDepartmentIdList(List<Integer> departmentIdList) {
        this.departmentIdList = departmentIdList;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public int getRoundsType() {
        return roundsType;
    }

    public void setRoundsType(int roundsType) {
        this.roundsType = roundsType;
    }

    @Override
    public String toString() {
        return "RoundsAppointmentInfo{" +
                "doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorHospital='" + doctorHospital + '\'' +
                ", doctorDepartment='" + doctorDepartment + '\'' +
                ", lectureTopics='" + lectureTopics + '\'' +
                ", appeal='" + appeal + '\'' +
                ", lengthTime='" + lengthTime + '\'' +
                ", needPPT=" + needPPT +
                ", medicalInfoList=" + medicalInfoList +
                ", medicalList=" + medicalList +
                ", intentionTimeList=" + intentionTimeList +
                ", intentionDepartmentList=" + intentionDepartmentList +
                ", departmentIdList=" + departmentIdList +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
