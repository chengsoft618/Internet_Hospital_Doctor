package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/10/24 18:21
 * @description: 会诊提交表单
 */
public class FormForConsult implements Serializable {
    private int appointmentId;//就诊编号
    private int serialNumber;//就诊序号
    private String predictCureDt;//预计时间
    private String payPassword;//支付密码
    private String patientDesc = "";//患者描述
    private String diseaseName = "";//疾病名称
    private String filePath = "";//文件名
    private String realName = "";//患者姓名
    private String phoneNum;//电话号码
    private ScheduleOrImageInfo scheduleOrImageInfo;//排班ID
    private int attdocId;//首诊医生ID
    private int source;//预约来源
    private String cardNo;//身份证号
    private int scheduingType;//排班类型
    private int serviceType;//服务类型
    private DoctorBaseInfo doctorBaseInfo;//医生ID
    private int topAppointId;//原始预约ID 复诊必传，否则传0
    private int superiorAppointId;//上级预约ID  复诊必传，否则传0
    private int patientUserId;//用户id

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getPatientDesc() {
        return patientDesc;
    }

    public void setPatientDesc(String patientDesc) {
        this.patientDesc = patientDesc;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public ScheduleOrImageInfo getScheduleOrImageInfo() {
        return scheduleOrImageInfo;
    }

    public void setScheduleOrImageInfo(ScheduleOrImageInfo scheduleOrImageInfo) {
        this.scheduleOrImageInfo = scheduleOrImageInfo;
    }

    public int getAttdocId() {
        return attdocId;
    }

    public void setAttdocId(int attdocId) {
        this.attdocId = attdocId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public DoctorBaseInfo getDoctorBaseInfo() {
        return doctorBaseInfo;
    }

    public void setDoctorBaseInfo(DoctorBaseInfo doctorBaseInfo) {
        this.doctorBaseInfo = doctorBaseInfo;
    }

    public int getTopAppointId() {
        return topAppointId;
    }

    public void setTopAppointId(int topAppointId) {
        this.topAppointId = topAppointId;
    }

    public int getSuperiorAppointId() {
        return superiorAppointId;
    }

    public void setSuperiorAppointId(int superiorAppointId) {
        this.superiorAppointId = superiorAppointId;
    }

    public int getPatientUserId() {
        return patientUserId;
    }

    public void setPatientUserId(int patientUserId) {
        this.patientUserId = patientUserId;
    }
}
