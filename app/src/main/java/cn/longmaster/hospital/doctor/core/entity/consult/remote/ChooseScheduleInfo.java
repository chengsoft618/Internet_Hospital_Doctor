package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;

/**
 * 选择排班的相关信息
 * Created by JinKe on 2016-08-04.
 */
public class ChooseScheduleInfo implements Serializable {
    private String doctorName;//医生姓名
    private String hospitalName;//医院名称
    private String department;//部门名称
    private String doctorTitle;//医生职称
    private String doctorLevel;//医生职位
    private String beginTime;//开始时间
    private String endTime;//结束时间
    private int applyMode;//申请模式
    private String pay;//金额
    private String serviceName;
    private int doctorId;

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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getApplyMode() {
        return applyMode;
    }

    public void setApplyMode(int applyMode) {
        this.applyMode = applyMode;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "ChooseScheduleInfo{" +
                "doctorName='" + doctorName + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", department='" + department + '\'' +
                ", doctorTitle='" + doctorTitle + '\'' +
                ", doctorLevel='" + doctorLevel + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", applyMode=" + applyMode +
                ", pay='" + pay + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", doctorId='" + doctorId + '\'' +
                '}';
    }
}
