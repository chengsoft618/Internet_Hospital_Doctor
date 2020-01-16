package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-订单诊疗建议详情
 * Created by yangyong on 2019-11-30.
 */
public class DCSuggestionDetailInfo implements Serializable {
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
    @JsonField("disease_name")
    private String diseaseName;
    @JsonField("disease_desc")
    private String diseaseDesc;
    @JsonField("inspect")
    private String inspect;
    @JsonField("pres_list")
    private List<DCDrugInfo> drugInfos;

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

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseDesc() {
        return diseaseDesc;
    }

    public void setDiseaseDesc(String diseaseDesc) {
        this.diseaseDesc = diseaseDesc;
    }

    public String getInspect() {
        return inspect;
    }

    public void setInspect(String inspect) {
        this.inspect = inspect;
    }

    public List<DCDrugInfo> getDrugInfos() {
        return drugInfos;
    }

    public void setDrugInfos(List<DCDrugInfo> drugInfos) {
        this.drugInfos = drugInfos;
    }

    @Override
    public String toString() {
        return "DCSuggestionDetailInfo{" +
                "orderId=" + orderId +
                ", medicalId=" + medicalId +
                ", itemId=" + itemId +
                ", patientName='" + patientName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", cureDt='" + cureDt + '\'' +
                ", orderType=" + orderType +
                ", diseaseName='" + diseaseName + '\'' +
                ", diseaseDesc='" + diseaseDesc + '\'' +
                ", inspect='" + inspect + '\'' +
                ", drugInfos=" + drugInfos +
                '}';
    }
}
