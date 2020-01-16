package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2017/12/28.
 */

public class VisitDetailsInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("attending_doctor_user_id")
    private int attendingDoctorUserId;//首诊id
    @JsonField("recommend_hospital")
    private int recommendHospital;//发起医院id
    @JsonField("cure_type")
    private int cureType;//就诊方式
    @JsonField("launch_user_id")
    private int launchUserId;//发起人id
    @JsonField("sale_user_id")
    private int saleUserId;//接收人id
    @JsonField("appointment_stat")
    private int appointmentStat; //预约状态1
    @JsonField("stat_reason")
    private int statReason;//预约状态2
    @JsonField("insert_dt")
    private String insertDt;//预约时间
    @JsonField("patient_real_name")
    private String patientRealName;//  患者姓名
    @JsonField("att_doctor_real_name")
    private String attDoctorRealName;//首诊医生姓名
    @JsonField("scheduing_id")
    private int scheduingId;  //排班ID
    @JsonField("pay_value")
    private float payValue;   //诊金
    @JsonField("is_pay")
    private int isPay;//是否支付

    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAttendingDoctorUserId() {
        return attendingDoctorUserId;
    }

    public void setAttendingDoctorUserId(int attendingDoctorUserId) {
        this.attendingDoctorUserId = attendingDoctorUserId;
    }

    public int getRecommendHospital() {
        return recommendHospital;
    }

    public void setRecommendHospital(int recommendHospital) {
        this.recommendHospital = recommendHospital;
    }

    public int getCureType() {
        return cureType;
    }

    public void setCureType(int cureType) {
        this.cureType = cureType;
    }

    public int getLaunchUserId() {
        return launchUserId;
    }

    public void setLaunchUserId(int launchUserId) {
        this.launchUserId = launchUserId;
    }

    public int getSaleUserId() {
        return saleUserId;
    }

    public void setSaleUserId(int saleUserId) {
        this.saleUserId = saleUserId;
    }

    public int getAppointmentStat() {
        return appointmentStat;
    }

    public void setAppointmentStat(int appointmentStat) {
        this.appointmentStat = appointmentStat;
    }

    public int getStatReason() {
        return statReason;
    }

    public void setStatReason(int statReason) {
        this.statReason = statReason;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getPatientRealName() {
        return patientRealName;
    }

    public void setPatientRealName(String patientRealName) {
        this.patientRealName = patientRealName;
    }

    public String getAttDoctorRealName() {
        return attDoctorRealName;
    }

    public void setAttDoctorRealName(String attDoctorRealName) {
        this.attDoctorRealName = attDoctorRealName;
    }

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public float getPayValue() {
        return payValue;
    }

    public void setPayValue(float payValue) {
        this.payValue = payValue;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    @Override
    public String toString() {
        return "VisitDetailsInfo{" +
                "appointmentId=" + appointmentId +
                ", attendingDoctorUserId=" + attendingDoctorUserId +
                ", recommendHospital='" + recommendHospital + '\'' +
                ", cureType=" + cureType +
                ", launchUserId=" + launchUserId +
                ", saleUserId=" + saleUserId +
                ", appointmentStat=" + appointmentStat +
                ", statReason=" + statReason +
                ", insertDt='" + insertDt + '\'' +
                ", patientRealName='" + patientRealName + '\'' +
                ", attDoctorRealName='" + attDoctorRealName + '\'' +
                ", scheduingId=" + scheduingId +
                ", payValue=" + payValue +
                ", isPay=" + isPay +
                '}';
    }
}
