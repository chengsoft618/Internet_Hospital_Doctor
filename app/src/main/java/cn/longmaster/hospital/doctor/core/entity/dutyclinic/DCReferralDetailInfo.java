package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 订单转诊单详情
 * Created by yangyong on 2019-11-30.
 */
public class DCReferralDetailInfo implements Serializable {
    @JsonField("order_id")
    private int orderId;
    @JsonField("medical_id")
    private int medicalId;
    @JsonField("item_id")
    private int itemId;
    @JsonField("patient_name")
    private String patientName;
    @JsonField("card_no")
    private String cardNo;
    @JsonField("phone_num")
    private String phoneNum;
    @JsonField("cure_dt")
    private String cureDt;
    @JsonField("order_type")
    private int orderType;
    @JsonField("referral")
    private int referral;
    @JsonField("referral_name")
    private String referralName;
    @JsonField("receive")
    private int receive;
    @JsonField("receive_name")
    private String receiveName;
    @JsonField("referral_desc")
    private String referralDesc;
    @JsonField("inspect")
    private String inspect;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getReferral() {
        return referral;
    }

    public void setReferral(int referral) {
        this.referral = referral;
    }

    public String getReferralName() {
        return referralName;
    }

    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReferralDesc() {
        return referralDesc;
    }

    public void setReferralDesc(String referralDesc) {
        this.referralDesc = referralDesc;
    }

    public String getInspect() {
        return inspect;
    }

    public void setInspect(String inspect) {
        this.inspect = inspect;
    }

    @Override
    public String toString() {
        return "DCReferralDetailInfo{" +
                "orderId=" + orderId +
                ", medicalId=" + medicalId +
                ", itemId=" + itemId +
                ", patientName='" + patientName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", cureDt='" + cureDt + '\'' +
                ", orderType=" + orderType +
                ", referral=" + referral +
                ", referralName='" + referralName + '\'' +
                ", receive=" + receive +
                ", receiveName='" + receiveName + '\'' +
                ", referralDesc='" + referralDesc + '\'' +
                ", inspect='" + inspect + '\'' +
                '}';
    }
}
