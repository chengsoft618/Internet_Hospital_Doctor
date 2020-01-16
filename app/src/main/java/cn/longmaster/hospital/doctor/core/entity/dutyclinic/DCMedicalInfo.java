package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-病历信息
 * Created by yangyong on 2019-12-05.
 */
public class DCMedicalInfo implements Serializable {
    @JsonField("medical_id")
    private int medicalId;
    @JsonField("order_id")
    private int orderId;
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
    @JsonField("temp_state")
    private int tempState;
    @JsonField("insert_dt")
    private String insertDt;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getTempState() {
        return tempState;
    }

    public void setTempState(int tempState) {
        this.tempState = tempState;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DCMedicalInfo{" +
                "medicalId=" + medicalId +
                ", orderId=" + orderId +
                ", itemId=" + itemId +
                ", patientName=" + patientName +
                ", cardNo=" + cardNo +
                ", phoneNum=" + phoneNum +
                ", cureDt=" + cureDt +
                ", tempState=" + tempState +
                ", insertDt=" + insertDt +
                '}';
    }
}
