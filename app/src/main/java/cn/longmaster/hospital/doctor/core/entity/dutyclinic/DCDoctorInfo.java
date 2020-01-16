package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-值班医生信息
 * Created by yangyong on 2019-11-27.
 */
public class DCDoctorInfo implements Serializable {
    @JsonField("user_id")
    private int userId;
    @JsonField("real_name")
    private String realName;
    @JsonField("doctor_level")
    private String doctorLevel;
    @JsonField("hospital_id")
    private int hospitalId;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("department_id")
    private int departmentId;
    @JsonField("department_name")
    private String departmentName;

    public DCDoctorInfo initWithDoctorSessionInfo(DCDoctorSectionInfo doctorInfo) {
        setUserId(doctorInfo.getUserId());
        setRealName(doctorInfo.getRealName());
        setDoctorLevel(doctorInfo.getDoctorLevel());
        setHospitalId(doctorInfo.getHospitalId());
        setHospitalName(doctorInfo.getHospitalName());
        setDepartmentId(doctorInfo.getDepartmentId());
        setDepartmentName(doctorInfo.getDepartmentName());
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDoctorLevel() {
        return doctorLevel;
    }

    public void setDoctorLevel(String doctorLevel) {
        this.doctorLevel = doctorLevel;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DCDoctorInfo{" +
                "userId=" + userId +
                ", realName='" + realName + '\'' +
                ", doctorLevel='" + doctorLevel + '\'' +
                ", hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
