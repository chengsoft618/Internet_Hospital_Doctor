package cn.longmaster.hospital.doctor.core.entity.consult;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/11/14 13:55
 * @description:
 */
public class AppointmentItemInfo {

    /**
     * appointment_id : 19423
     * doctor_user_id : 1004064
     * appointment_stat : 2
     * stat_reason : 0
     * attending_doctor_user_id : 1005055
     * complete_dt :
     * super_doctor_name : 王大妈
     * super_doctor_title : 硕士研究生
     * super_department_name : 口腔颌面外科
     * super_hospital_name : 贵州省骨科医院
     * super_doctor_img : https://test-dfs.39hospital.com/dfs/3001/0/1004064.png
     * attend_doctor_name : 唐颷
     * attend_department_name :
     * attend_hospital_name : 39互联网医院总院
     * attend_hospita_logo : https://test-dfs.39hospital.com/dfs/3019/0/hospital_icon.png
     * patient_name : 唐彪
     * case_level : 0
     * appointment_dt : 2019-10-26 13:39:06
     * is_diagnosis : 0
     * pay_surplus_dt : -1
     * scheduing_type : 1
     */
    //预约id
    @JsonField("appointment_id")
    private int appointmentId;
    //上级专家id
    @JsonField("doctor_user_id")
    private int doctorUserId;
    //订单状态
    @JsonField("appointment_stat")
    private int appointmentStat;
    //状态原因
    @JsonField("stat_reason")
    private int statReason;
    //首诊医生id
    @JsonField("attending_doctor_user_id")
    private int attendingDoctorUserId;
    //完成时间
    @JsonField("complete_dt")
    private String completeDt;
    //上级医生姓名
    @JsonField("super_doctor_name")
    private String superDoctorName;
    //上级医生职称
    @JsonField("super_doctor_title")
    private String superDoctorTitle;
    //上级专家职位
    @JsonField("super_doctor_level")
    private String superDoctorLevel;
    //上级医生科室
    @JsonField("super_department_name")
    private String superDepartmentName;
    //上级医生医院
    @JsonField("super_hospital_name")
    private String superHospitalName;
    //上级医生头像
    @JsonField("super_doctor_img")
    private String superDoctorImg;
    //首诊医生姓名
    @JsonField("attend_doctor_name")
    private String attendDoctorName;
    //首诊医生科室
    @JsonField("attend_department_name")
    private String attendDepartmentName;
    //首诊医生医院
    @JsonField("attend_hospital_name")
    private String attendHospitalName;
    //首诊医院头像
    @JsonField("attend_hospita_logo")
    private String attendHospitaLogo;
    //患者姓名
    @JsonField("patient_name")
    private String patientName;
    //病例分级 0：普通病例 1：危重病例
    @JsonField("case_level")
    private int caseLevel;
    //预约时间
    @JsonField("appointment_dt")
    private String appointmentDt;
    //是否出具医嘱 0：未出具 1：已出具
    @JsonField("is_diagnosis")
    private int isDiagnosis;
    //预约支付状态：大于0时为该预约的剩余支付时间；等于0为支付成功；等于-1支付超时；等于-2不在支付状态
    @JsonField("pay_surplus_dt")
    private int paySurplusDt;
    //排班/影像 1：排班 2：影像
    @JsonField("scheduing_type")
    private int scheduingType;
    //预计就诊时间
    @JsonField("predict_cure_dt")
    private String predictCureDt;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;

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

    public int getAttendingDoctorUserId() {
        return attendingDoctorUserId;
    }

    public void setAttendingDoctorUserId(int attendingDoctorUserId) {
        this.attendingDoctorUserId = attendingDoctorUserId;
    }

    public String getCompleteDt() {
        return completeDt;
    }

    public void setCompleteDt(String completeDt) {
        this.completeDt = completeDt;
    }

    public String getSuperDoctorName() {
        return superDoctorName;
    }

    public void setSuperDoctorName(String superDoctorName) {
        this.superDoctorName = superDoctorName;
    }

    public String getSuperDoctorLevel() {
        return superDoctorLevel;
    }

    public void setSuperDoctorLevel(String superDoctorLevel) {
        this.superDoctorLevel = superDoctorLevel;
    }

    public String getSuperDoctorTitle() {
        return superDoctorTitle;
    }

    public void setSuperDoctorTitle(String superDoctorTitle) {
        this.superDoctorTitle = superDoctorTitle;
    }

    public String getSuperDepartmentName() {
        return superDepartmentName;
    }

    public void setSuperDepartmentName(String superDepartmentName) {
        this.superDepartmentName = superDepartmentName;
    }

    public String getSuperHospitalName() {
        return superHospitalName;
    }

    public void setSuperHospitalName(String superHospitalName) {
        this.superHospitalName = superHospitalName;
    }

    public String getSuperDoctorImg() {
        return superDoctorImg;
    }

    public void setSuperDoctorImg(String superDoctorImg) {
        this.superDoctorImg = superDoctorImg;
    }

    public String getAttendDoctorName() {
        return attendDoctorName;
    }

    public void setAttendDoctorName(String attendDoctorName) {
        this.attendDoctorName = attendDoctorName;
    }

    public String getAttendDepartmentName() {
        return attendDepartmentName;
    }

    public void setAttendDepartmentName(String attendDepartmentName) {
        this.attendDepartmentName = attendDepartmentName;
    }

    public String getAttendHospitalName() {
        return attendHospitalName;
    }

    public void setAttendHospitalName(String attendHospitalName) {
        this.attendHospitalName = attendHospitalName;
    }

    public String getAttendHospitaLogo() {
        return attendHospitaLogo;
    }

    public void setAttendHospitaLogo(String attendHospitaLogo) {
        this.attendHospitaLogo = attendHospitaLogo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public boolean isWorseCase() {
        return caseLevel == 1;
    }

    public int getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(int caseLevel) {
        this.caseLevel = caseLevel;
    }

    public String getAppointmentDt() {
        return appointmentDt;
    }

    public void setAppointmentDt(String appointmentDt) {
        this.appointmentDt = appointmentDt;
    }

    public int getIsDiagnosis() {
        return isDiagnosis;
    }

    public void setIsDiagnosis(int isDiagnosis) {
        this.isDiagnosis = isDiagnosis;
    }

    public int getPaySurplusDt() {
        return paySurplusDt;
    }

    public void setPaySurplusDt(int paySurplusDt) {
        this.paySurplusDt = paySurplusDt;
    }

    public int getScheduingType() {
        return scheduingType;
    }

    public void setScheduingType(int scheduingType) {
        this.scheduingType = scheduingType;
    }

    public String getPredictCureDt() {
        return predictCureDt;
    }

    public void setPredictCureDt(String predictCureDt) {
        this.predictCureDt = predictCureDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "AppointmentItemInfo{" +
                "appointmentId='" + appointmentId + '\'' +
                ", doctorUserId='" + doctorUserId + '\'' +
                ", appointmentStat='" + appointmentStat + '\'' +
                ", statReason='" + statReason + '\'' +
                ", attendingDoctorUserId='" + attendingDoctorUserId + '\'' +
                ", completeDt='" + completeDt + '\'' +
                ", superDoctorName='" + superDoctorName + '\'' +
                ", superDoctorTitle='" + superDoctorTitle + '\'' +
                ", superDepartmentName='" + superDepartmentName + '\'' +
                ", superHospitalName='" + superHospitalName + '\'' +
                ", superDoctorImg='" + superDoctorImg + '\'' +
                ", attendDoctorName='" + attendDoctorName + '\'' +
                ", attendDepartmentName='" + attendDepartmentName + '\'' +
                ", attendHospitalName='" + attendHospitalName + '\'' +
                ", attendHospitaLogo='" + attendHospitaLogo + '\'' +
                ", patientName='" + patientName + '\'' +
                ", caseLevel='" + caseLevel + '\'' +
                ", appointmentDt='" + appointmentDt + '\'' +
                ", isDiagnosis='" + isDiagnosis + '\'' +
                ", paySurplusDt='" + paySurplusDt + '\'' +
                ", scheduingType='" + scheduingType + '\'' +
                '}';
    }
}
