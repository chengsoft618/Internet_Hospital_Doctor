package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.hospital.doctor.core.entity.consult.remote.ChooseScheduleInfo;

/**
 * 提交预约成功后数据
 * Created by JinKe on 2016-08-04.
 */
public class SubmitSuccessInfo implements Serializable {
    private int schedulingType;//就诊类型
    private int appointmentID;//预约id
    private String relateKey;//关联密码
    private String realName;//患者姓名
    private String phoneNum;//电话号码
    private String cardNo;//身份证号
    private boolean isChooseDoc;
    private ChooseScheduleInfo scheduleInfo;//选择的医生排班信息
    private int serialNumber;//就诊序号
    private String predictDt;//预计时间

    public int getSchedulingType() {
        return schedulingType;
    }

    public void setSchedulingType(int schedulingType) {
        this.schedulingType = schedulingType;
    }

    public boolean getIsChooseDoc() {
        return isChooseDoc;
    }

    public void setIsChooseDoc(boolean isChooseDoc) {
        this.isChooseDoc = isChooseDoc;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getRelateKey() {
        return relateKey;
    }

    public void setRelateKey(String relateKey) {
        this.relateKey = relateKey;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public ChooseScheduleInfo getScheduleInfo() {
        return scheduleInfo;
    }

    public void setScheduleInfo(ChooseScheduleInfo scheduleInfo) {
        this.scheduleInfo = scheduleInfo;
    }

    public boolean isChooseDoc() {
        return isChooseDoc;
    }

    public void setChooseDoc(boolean chooseDoc) {
        isChooseDoc = chooseDoc;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPredictDt() {
        return predictDt;
    }

    public void setPredictDt(String predictDt) {
        this.predictDt = predictDt;
    }

    @Override
    public String toString() {
        return "SubmitSuccessInfo{" +
                "schedulingType=" + schedulingType +
                ", appointmentID=" + appointmentID +
                ", relateKey='" + relateKey + '\'' +
                ", realName='" + realName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", isChooseDoc=" + isChooseDoc +
                ", scheduleInfo=" + scheduleInfo +
                ", serialNumber=" + serialNumber +
                ", predictDt='" + predictDt + '\'' +
                '}';
    }
}
