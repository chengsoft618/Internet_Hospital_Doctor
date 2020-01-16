package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import com.chad.library.adapter.base.entity.SectionEntity;

import java.io.Serializable;

/**
 * 值班门诊-值班医生信息
 * Created by yangyong on 2019-11-27.
 */
public class DCDoctorSectionInfo extends SectionEntity implements Serializable {
    private int userId;
    private String realName;
    private String doctorLevel;
    private int hospitalId;
    private String hospitalName;
    private int departmentId;
    private String departmentName;

    private boolean isChecked;
    //在线状态 0离线1离开2在线
    private int onlineState;
    public DCDoctorSectionInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public DCDoctorSectionInfo initWithDoctorInfo(DCDoctorInfo doctorInfo) {
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(int onlineState) {
        this.onlineState = onlineState;
    }

    @Override
    public String toString() {
        return "DCDoctorSectionInfo{" +
                "userId=" + userId +
                ", realName='" + realName + '\'' +
                ", doctorLevel='" + doctorLevel + '\'' +
                ", hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
