package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 预约基本信息
 * Created by JinKe on 2016-08-03.
 */
public class AppointmentBaseInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("disease_id")
    private int diseaseId;//疾病ID
    @JsonField("team_type")
    private int teamType;//团队类型
    @JsonField("user_id")
    private int userId;//患者ID
    @JsonField("doctor_user_id")
    private int doctorUserId;//医生ID
    @JsonField("patient_desc")
    private String patientDesc;//病人简单描述
    @JsonField("patient_desc_type")
    private int patientDescType;//自述类型
    @JsonField("file_path")
    private String filePath;//语音文件路径
    @JsonField("file_time")
    private int fileTime;//语音时长
    @JsonField("appointment_stat")
    private int appointmentStat;//预约状态
    @JsonField("effec_end_dt")
    private String effecEndDt;//病历的有效时间
    @JsonField("stat_reason")
    private int statReason;//预约状态原因 默认为0
    @JsonField("is_recure")
    private int isRecure;//是否复诊
    @JsonField("serial_number")
    private int serialNum;//排序号
    @JsonField("source")
    private int source;//来源
    @JsonField("call_user")
    private int callUser;//呼叫用户
    @JsonField("is_material_pass")
    private int isMaterialPass;//材料是否通过
    @JsonField("insert_dt")
    private String insertDt;//插入时间
    @JsonField("patient_stat")
    private int patientStat;//病人状态
    @JsonField("scheduing_id")
    private int scheduingId;//排班ID
    @JsonField("attending_doctor_user_id")
    private int attendingDoctorUserId;//首诊医生ID
    @JsonField("pay_password")
    private String payPassword;//支付密码
    @JsonField("share_password")
    private String sharePassword;//分享密码
    @JsonField("is_tips_sms")
    private int isTipsSms;//是否有提示消息
    @JsonField("admin_id")
    private int adminId;//医生助理ID
    @JsonField("serial_number")
    private int serialNumber;//就诊序号
    @JsonField("predict_cure_dt")
    private String predictCureDt;//预计就诊时间
    @JsonField("is_diagnosis")
    private int isDiagnosis;//是否出具医嘱 0：未出具 1：已出具
    @JsonField("order_end_dt")
    private String orderEndDt;//最迟支付时间
    @JsonField("pay_surplus_dt")
    private int paySurplusDt;//预约支付状态
    @JsonField("case_level")
    private int caseLevel;//病例分级 0：普通病例 1：危重病例

    @JsonField("material_pass_dt")
    private String materialPassDt;//材料通过时间
    @JsonField("cure_desc")
    private String cureDesc;
    @JsonField("sale_user_id")
    private int saleUserId;
    @JsonField("area_id")
    private int areaId;
    @JsonField("recommend_user_id")
    private int recommendUserId;
    @JsonField("cure_ype")
    private int cure_Type;
    @JsonField("app_pay_type")
    private int appPayType;
    @JsonField("launch_user_id")
    private int launchUserId;
    @JsonField("receiveSectionId")
    private int receiveSectionId;
    @JsonField("complete_dt")
    private String completeDt;
    @JsonField("admin_cure_dt")
    private String adminCureDt;
    @JsonField("attending_doctor_hosdep_id")
    private int attendingDoctorHosdepId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(int doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public String getPatientDesc() {
        return patientDesc;
    }

    public void setPatientDesc(String patientDesc) {
        this.patientDesc = patientDesc;
    }

    public int getPatientDescType() {
        return patientDescType;
    }

    public void setPatientDescType(int patientDescType) {
        this.patientDescType = patientDescType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileTime() {
        return fileTime;
    }

    public void setFileTime(int fileTime) {
        this.fileTime = fileTime;
    }

    public int getAppointmentStat() {
        return appointmentStat;
    }

    public void setAppointmentStat(int appointmentStat) {
        this.appointmentStat = appointmentStat;
    }

    public String getEffecEndDt() {
        return effecEndDt;
    }

    public void setEffecEndDt(String effecEndDt) {
        this.effecEndDt = effecEndDt;
    }

    public int getStatReason() {
        return statReason;
    }

    public void setStatReason(int statReason) {
        this.statReason = statReason;
    }

    public int getIsRecure() {
        return isRecure;
    }

    public void setIsRecure(int isRecure) {
        this.isRecure = isRecure;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getCallUser() {
        return callUser;
    }

    public void setCallUser(int callUser) {
        this.callUser = callUser;
    }

    public int getIsMaterialPass() {
        return isMaterialPass;
    }

    public void setIsMaterialPass(int isMaterialPass) {
        this.isMaterialPass = isMaterialPass;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getPatientStat() {
        return patientStat;
    }

    public void setPatientStat(int patientStat) {
        this.patientStat = patientStat;
    }

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public int getAttendingDoctorUserId() {
        return attendingDoctorUserId;
    }

    public void setAttendingDoctorUserId(int attendingDoctorUserId) {
        this.attendingDoctorUserId = attendingDoctorUserId;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getSharePassword() {
        return sharePassword;
    }

    public void setSharePassword(String sharePassword) {
        this.sharePassword = sharePassword;
    }

    public int getIsTipsSms() {
        return isTipsSms;
    }

    public void setIsTipsSms(int isTipsSms) {
        this.isTipsSms = isTipsSms;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPredictCureDt() {
        return predictCureDt;
    }

    public void setPredictCureDt(String predictCureDt) {
        this.predictCureDt = predictCureDt;
    }

    public int getIsDiagnosis() {
        return isDiagnosis;
    }

    public void setIsDiagnosis(int isDiagnosis) {
        this.isDiagnosis = isDiagnosis;
    }

    public String getOrderEndDt() {
        return orderEndDt;
    }

    public void setOrderEndDt(String orderEndDt) {
        this.orderEndDt = orderEndDt;
    }

    public int getPaySurplusDt() {
        return paySurplusDt;
    }

    public void setPaySurplusDt(int paySurplusDt) {
        this.paySurplusDt = paySurplusDt;
    }

    public int getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(int caseLevel) {
        this.caseLevel = caseLevel;
    }

    public String getMaterialPassDt() {
        return materialPassDt;
    }

    public void setMaterialPassDt(String materialPassDt) {
        this.materialPassDt = materialPassDt;
    }

    public String getCureDesc() {
        return cureDesc;
    }

    public void setCureDesc(String cureDesc) {
        this.cureDesc = cureDesc;
    }

    public int getSaleUserId() {
        return saleUserId;
    }

    public void setSaleUserId(int saleUserId) {
        this.saleUserId = saleUserId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(int recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public int getCure_Type() {
        return cure_Type;
    }

    public void setCure_Type(int cure_Type) {
        this.cure_Type = cure_Type;
    }

    public int getAppPayType() {
        return appPayType;
    }

    public void setAppPayType(int appPayType) {
        this.appPayType = appPayType;
    }

    public int getLaunchUserId() {
        return launchUserId;
    }

    public void setLaunchUserId(int launchUserId) {
        this.launchUserId = launchUserId;
    }

    public int getReceiveSectionId() {
        return receiveSectionId;
    }

    public void setReceiveSectionId(int receiveSectionId) {
        this.receiveSectionId = receiveSectionId;
    }

    public String getCompleteDt() {
        return completeDt;
    }

    public void setCompleteDt(String completeDt) {
        this.completeDt = completeDt;
    }

    public String getAdminCureDt() {
        return adminCureDt;
    }

    public void setAdminCureDt(String adminCureDt) {
        this.adminCureDt = adminCureDt;
    }

    public int getAttendingDoctorHosdepId() {
        return attendingDoctorHosdepId;
    }

    public void setAttendingDoctorHosdepId(int attendingDoctorHosdepId) {
        this.attendingDoctorHosdepId = attendingDoctorHosdepId;
    }

    @Override
    public String toString() {
        return "AppointmentBaseInfo{" +
                "appointmentId=" + appointmentId +
                ", diseaseId=" + diseaseId +
                ", teamType=" + teamType +
                ", userId=" + userId +
                ", doctorUserId=" + doctorUserId +
                ", patientDesc='" + patientDesc + '\'' +
                ", patientDescType=" + patientDescType +
                ", filePath='" + filePath + '\'' +
                ", fileTime=" + fileTime +
                ", appointmentStat=" + appointmentStat +
                ", effecEndDt='" + effecEndDt + '\'' +
                ", statReason=" + statReason +
                ", isRecure=" + isRecure +
                ", serialNum=" + serialNum +
                ", source=" + source +
                ", callUser=" + callUser +
                ", isMaterialPass=" + isMaterialPass +
                ", insertDt='" + insertDt + '\'' +
                ", patientStat=" + patientStat +
                ", scheduingId=" + scheduingId +
                ", attendingDoctorUserId=" + attendingDoctorUserId +
                ", payPassword='" + payPassword + '\'' +
                ", sharePassword='" + sharePassword + '\'' +
                ", isTipsSms=" + isTipsSms +
                ", adminId=" + adminId +
                ", serialNumber=" + serialNumber +
                ", predictCureDt='" + predictCureDt + '\'' +
                ", isDiagnosis=" + isDiagnosis +
                ", orderEndDt='" + orderEndDt + '\'' +
                ", paySurplusDt=" + paySurplusDt +
                ", caseLevel=" + caseLevel +
                ", materialPassDt='" + materialPassDt + '\'' +
                ", cureDesc='" + cureDesc + '\'' +
                ", saleUserId=" + saleUserId +
                ", areaId=" + areaId +
                ", recommendUserId=" + recommendUserId +
                ", cure_Type=" + cure_Type +
                ", appPayType=" + appPayType +
                ", launchUserId=" + launchUserId +
                ", receiveSectionId=" + receiveSectionId +
                ", completeDt='" + completeDt + '\'' +
                ", adminCureDt='" + adminCureDt + '\'' +
                ", attendingDoctorHosdepId=" + attendingDoctorHosdepId +
                '}';
    }
}
