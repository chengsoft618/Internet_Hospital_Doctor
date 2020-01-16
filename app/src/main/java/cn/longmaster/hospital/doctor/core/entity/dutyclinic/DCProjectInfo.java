package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-项目信息
 * Created by yangyong on 2019-11-29.
 */
public class DCProjectInfo implements Serializable {
    @JsonField("item_id")
    private int itemId;
    @JsonField("item_name")
    private String itemName;
    @JsonField("item_desc")
    private String itemDesc;
    @JsonField("item_no")
    private String itemNo;
    @JsonField("duty_state")
    private int dutyState;
    @JsonField("doctor_count")
    private int doctorCount;
    @JsonField("patient_count")
    private int patientCount;
    @JsonField("qrcode_url")
    private String qrcodeUrl;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getDutyState() {
        return dutyState;
    }

    public void setDutyState(int dutyState) {
        this.dutyState = dutyState;
    }

    public int getDoctorCount() {
        return doctorCount;
    }

    public void setDoctorCount(int doctorCount) {
        this.doctorCount = doctorCount;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    @Override
    public String toString() {
        return "DCProjectInfo{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemNo='" + itemNo + '\'' +
                ", dutyState=" + dutyState +
                '}';
    }
}
