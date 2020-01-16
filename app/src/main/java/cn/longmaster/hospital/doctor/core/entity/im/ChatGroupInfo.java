package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 群组列表 群实体类
 * Created by Yang² on 2017/8/28.
 */

public class ChatGroupInfo implements Serializable {
    @JsonField("user_id")
    private int userId;//用户ID
    @JsonField("group_id")
    private int groupId;//群组ID
    @JsonField("appointment_id")
    private int appointmentId;//患者预约ID
    @JsonField("role")
    private int role;//角色-0 患者，1首诊医生，2 管理员，3上级专家，4 医生助理，5 会议人员，6 MDT医生，7 MDT患者家属，255 系统自动
    @JsonField("status")
    private int status;//群组状态- 0 初始状态， 1 接诊， 2拒诊，3 关闭，4 随诊
    @JsonField("insert_dt")
    private String insertDt;//创建时间
    @JsonField("msg_user_id")
    private int msgUserId;//最新消息发送者ID
    @JsonField("msg_role")
    private int msgRole;//最新消息发送者角色
    @JsonField("msg_id")
    private int msgId;//最新消息ID
    @JsonField("msg_type")
    private int msgType;//最新消息类型
    @JsonField("msg_content")
    private String msgContent;//最新消息内容
    @JsonField("upd_dt")
    private String updDt;//最新消息时间
    //上级专家id
    @JsonField("super_doctor_id")
    private int superDoctorId;
    //上级专家名称
    @JsonField("super_doctor_name")
    private String superDoctorName;
    //上级专家职称
    @JsonField("super_doctor_title")
    private String superDoctorTitle;
    //上级专家职位
    @JsonField("super_doctor_level")
    private String superDoctorLevel;
    //上级专家所属科室
    @JsonField("super_department_name")
    private String superDepartmentName;
    //上级专家所属医院
    @JsonField("super_hospital_name")
    private String superHospitalName;
    //上级专家头像
    @JsonField("super_doctor_img")
    private String superDoctorImg;
    //首诊医生id
    @JsonField("attend_doctor_id")
    private int attendDoctorId;
    //首诊医生姓名
    @JsonField("attend_doctor_name")
    private String attendDoctorName;
    //首诊医生科室
    @JsonField("attend_department_name")
    private String attendDepartmentName;
    //首诊医生医院
    @JsonField("attend_hospital_name")
    private String attendHospitalName;
    //首诊医生医院logo
    @JsonField("attend_hospita_logo")
    private String attendHospitalLogo;
    //患者姓名
    @JsonField("patient_name")
    private String patientName;
    //病例分级 0：普通病例 1：危重病例
    @JsonField("case_level")
    private int caseLevel;
    //完成时间
    @JsonField("complete_dt")
    private String completeDt;
    //预约时间
    @JsonField("appointment_dt")
    private String appointmentDt;
    //预约状态
    @JsonField("appointment_stat")
    private int appointmentStat;
    //预约状态原因
    @JsonField("stat_reason")
    private int statReason;
    @JsonField("predict_cure_dt")
    private String predictCureDt;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getMsgUserId() {
        return msgUserId;
    }

    public void setMsgUserId(int msgUserId) {
        this.msgUserId = msgUserId;
    }

    public int getMsgRole() {
        return msgRole;
    }

    public void setMsgRole(int msgRole) {
        this.msgRole = msgRole;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getUpdDt() {
        return updDt;
    }

    public void setUpdDt(String updDt) {
        this.updDt = updDt;
    }

    public int getSuperDoctorId() {
        return superDoctorId;
    }

    public void setSuperDoctorId(int superDoctorId) {
        this.superDoctorId = superDoctorId;
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

    public int getAttendDoctorId() {
        return attendDoctorId;
    }

    public void setAttendDoctorId(int attendDoctorId) {
        this.attendDoctorId = attendDoctorId;
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

    public String getAttendHospitalLogo() {
        return attendHospitalLogo;
    }

    public void setAttendHospitalLogo(String attendHospitalLogo) {
        this.attendHospitalLogo = attendHospitalLogo;
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

    public String getCompleteDt() {
        return completeDt;
    }

    public void setCompleteDt(String completeDt) {
        this.completeDt = completeDt;
    }

    public String getAppointmentDt() {
        return appointmentDt;
    }

    public void setAppointmentDt(String appointmentDt) {
        this.appointmentDt = appointmentDt;
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

    public String getPredictCureDt() {
        return predictCureDt;
    }

    public void setPredictCureDt(String predictCureDt) {
        this.predictCureDt = predictCureDt;
    }

    @Override
    public String toString() {
        return "ChatGroupInfo{" +
                "userId=" + userId +
                ", groupId=" + groupId +
                ", appointmentId=" + appointmentId +
                ", role=" + role +
                ", status=" + status +
                ", insertDt='" + insertDt + '\'' +
                ", msgUserId=" + msgUserId +
                ", msgRole=" + msgRole +
                ", msgId=" + msgId +
                ", msgType=" + msgType +
                ", msgContent='" + msgContent + '\'' +
                ", updDt='" + updDt + '\'' +
                ", superDoctorId=" + superDoctorId +
                ", superDoctorName='" + superDoctorName + '\'' +
                ", superDoctorTitle='" + superDoctorTitle + '\'' +
                ", superDepartmentName='" + superDepartmentName + '\'' +
                ", superHospitalName='" + superHospitalName + '\'' +
                ", superDoctorImg='" + superDoctorImg + '\'' +
                ", attendDoctorId=" + attendDoctorId +
                ", attendDoctorName='" + attendDoctorName + '\'' +
                ", attendDepartmentName='" + attendDepartmentName + '\'' +
                ", attendHospitalName='" + attendHospitalName + '\'' +
                ", attendHospitalLogo='" + attendHospitalLogo + '\'' +
                ", patientName='" + patientName + '\'' +
                ", caseLevel=" + caseLevel +
                ", completeDt='" + completeDt + '\'' +
                ", appointmentDt='" + appointmentDt + '\'' +
                '}';
    }
}
