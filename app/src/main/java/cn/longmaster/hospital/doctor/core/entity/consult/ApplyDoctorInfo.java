package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 会诊医生列表
 * Created by Yang² on 2016/8/13.
 */
public class ApplyDoctorInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("doctor_user_id")
    private int doctorUserId;//医生ID
    @JsonField("doctor_type")
    private int doctorType;//医生类型 1、医生助理 2、会诊医师 3、申请医师(首诊医生)
    @JsonField("doctor_phone")
    private String doctorPhone;//联系电话
    @JsonField("doctor_title")
    private String doctorTitle;//医生头衔
    @JsonField("hospital_name")
    private String hospitalName;//所在医院
    @JsonField("department_name")
    private String departmentName;//所在科室
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(int doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public int getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(int doctorType) {
        this.doctorType = doctorType;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
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

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "applyDoctorInfo{" +
                "appointmentId=" + appointmentId +
                ", doctorUserId=" + doctorUserId +
                ", doctorType=" + doctorType +
                ", doctorPhone='" + doctorPhone + '\'' +
                ", doctorTitle='" + doctorTitle + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", insertDt=" + insertDt +
                '}';
    }
}
