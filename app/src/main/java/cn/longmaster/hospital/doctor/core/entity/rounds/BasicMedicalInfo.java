package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/5/18.
 * 主要用于传参
 */

public class BasicMedicalInfo implements Serializable {
    private int medicalId;
    private int dgwsUid;
    private int doctorId;
    private int orderId;
    private int orderState;
    private String visitUrl;
    private boolean room;
    private boolean isExperts;
    private boolean isRelateRecord;
    private boolean isHaveAuthority;
    private int userType;

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public int getDgwsUid() {
        return dgwsUid;
    }

    public void setDgwsUid(int dgwsUid) {
        this.dgwsUid = dgwsUid;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getVisitUrl() {
        return visitUrl;
    }

    public void setVisitUrl(String visitUrl) {
        this.visitUrl = visitUrl;
    }

    public boolean isRoom() {
        return room;
    }

    public void setRoom(boolean room) {
        this.room = room;
    }

    public boolean isExperts() {
        return isExperts;
    }

    public void setExperts(boolean experts) {
        isExperts = experts;
    }

    public boolean isRelateRecord() {
        return isRelateRecord;
    }

    public void setRelateRecord(boolean relateRecord) {
        isRelateRecord = relateRecord;
    }

    public boolean isHaveAuthority() {
        return isHaveAuthority;
    }

    public void setHaveAuthority(boolean haveAuthority) {
        isHaveAuthority = haveAuthority;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "BasicMedicalInfo{" +
                "medicalId=" + medicalId +
                ", dgwsUid=" + dgwsUid +
                ", doctorId=" + doctorId +
                ", orderId=" + orderId +
                ", orderState=" + orderState +
                ", visitUrl='" + visitUrl + '\'' +
                ", room=" + room +
                ", isExperts=" + isExperts +
                '}';
    }
}
