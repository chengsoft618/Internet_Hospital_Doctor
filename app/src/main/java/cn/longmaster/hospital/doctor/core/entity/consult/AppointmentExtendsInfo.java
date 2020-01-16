package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 预约扩展信息
 * Created by Yang² on 2016/7/28.
 */
public class AppointmentExtendsInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约编号
    @JsonField("scheduing_type")
    private int scheduingType;//排班/影像 1：排班 2：影像
    @JsonField("service_type")
    private int serviceType;//服务类型 101001远程会诊 101002远程咨询 101003远程会诊复诊 101004远程咨询复诊 102001远程影像会诊
    @JsonField("superior_appointment_id")
    private int superiorAppointmentId;//上级预约ID
    @JsonField("top_appointment_id")
    private int topAppointmentId;//顶级预约ID
    @JsonField("doctor_is_consult")
    private int doctorIsConsult;//医生是否参加视频
    @JsonField("doctor_is_recure")
    private int doctorIsRecure;//首诊医生是否参加复诊
    @JsonField("is_popup")
    private int isPopup;//是否弹窗
    @JsonField("recure_remind_dt")
    private String recureRemindDt;//设置复诊时间
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    @JsonField("recommend_hospital")
    private String recommendHospital;
    @JsonField("is_spont")
    private int isSpont;
    @JsonField("cure_type")
    private int cureType;
    @JsonField("hospital_id")
    private int hospitalId;
    @JsonField("department_id")
    private int departmentId;
    @JsonField("room_id")
    private int roomId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getScheduingType() {
        return scheduingType;
    }

    public void setScheduingType(int scheduingType) {
        this.scheduingType = scheduingType;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getSuperiorAppointmentId() {
        return superiorAppointmentId;
    }

    public void setSuperiorAppointmentId(int superiorAppointmentId) {
        this.superiorAppointmentId = superiorAppointmentId;
    }

    public int getTopAppointmentId() {
        return topAppointmentId;
    }

    public void setTopAppointmentId(int topAppointmentId) {
        this.topAppointmentId = topAppointmentId;
    }

    public int getDoctorIsConsult() {
        return doctorIsConsult;
    }

    public void setDoctorIsConsult(int doctorIsConsult) {
        this.doctorIsConsult = doctorIsConsult;
    }

    public int getDoctorIsRecure() {
        return doctorIsRecure;
    }

    public void setDoctorIsRecure(int doctorIsRecure) {
        this.doctorIsRecure = doctorIsRecure;
    }

    public int getIsPopup() {
        return isPopup;
    }

    public void setIsPopup(int isPopup) {
        this.isPopup = isPopup;
    }

    public String getRecureRemindDt() {
        return recureRemindDt;
    }

    public void setRecureRemindDt(String recureRemindDt) {
        this.recureRemindDt = recureRemindDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getRecommendHospital() {
        return recommendHospital;
    }

    public void setRecommendHospital(String recommendHospital) {
        this.recommendHospital = recommendHospital;
    }

    public int getIsSpont() {
        return isSpont;
    }

    public void setIsSpont(int isSpont) {
        this.isSpont = isSpont;
    }

    public int getCureType() {
        return cureType;
    }

    public void setCureType(int cureType) {
        this.cureType = cureType;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "AppointmentExtendsInfo{" +
                "appointmentId=" + appointmentId +
                ", scheduingType=" + scheduingType +
                ", serviceType=" + serviceType +
                ", superiorAppointmentId=" + superiorAppointmentId +
                ", topAppointmentId=" + topAppointmentId +
                ", doctorIsConsult=" + doctorIsConsult +
                ", doctorIsRecure=" + doctorIsRecure +
                ", isPopup=" + isPopup +
                ", recureRemindDt='" + recureRemindDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", recommendHospital='" + recommendHospital + '\'' +
                ", isSpont=" + isSpont +
                ", cureType=" + cureType +
                ", hospitalId=" + hospitalId +
                ", departmentId=" + departmentId +
                ", roomId=" + roomId +
                '}';
    }
}
