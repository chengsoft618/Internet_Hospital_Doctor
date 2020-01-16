package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-值班门诊订单详情
 * Created by yangyong on 2019-11-30.
 */
public class DCOrderDetailInfo implements Serializable {
    @JsonField("order_id")
    private int orderId;
    @JsonField("item_id")
    private int itemId;
    @JsonField("order_title")
    private String orderTitle;
    @JsonField("order_type")
    private int orderType;
    @JsonField("launch_doctor_id")
    private int launchDoctorId;
    @JsonField("launch_doctor_name")
    private String launchDoctorName;
    @JsonField("launch_hospital_id")
    private int launchHospitalId;
    @JsonField("launch_hospital_name")
    private String launchHospitalName;
    @JsonField("launch_department_id")
    private int launchDepartmentId;
    @JsonField("launch_department_name")
    private String launchDepartmentName;
    @JsonField("doctor_id")
    private int doctorId;
    @JsonField("doctor_name")
    private String doctorName;
    @JsonField("doctor_hospital_id")
    private int doctorHospitalId;
    @JsonField("doctor_hospital_name")
    private String doctorHospitalName;
    @JsonField("insert_dt")
    private String insertDt;

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

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getLaunchDoctorId() {
        return launchDoctorId;
    }

    public void setLaunchDoctorId(int launchDoctorId) {
        this.launchDoctorId = launchDoctorId;
    }

    public String getLaunchDoctorName() {
        return launchDoctorName;
    }

    public void setLaunchDoctorName(String launchDoctorName) {
        this.launchDoctorName = launchDoctorName;
    }

    public int getLaunchHospitalId() {
        return launchHospitalId;
    }

    public void setLaunchHospitalId(int launchHospitalId) {
        this.launchHospitalId = launchHospitalId;
    }

    public String getLaunchHospitalName() {
        return launchHospitalName;
    }

    public void setLaunchHospitalName(String launchHospitalName) {
        this.launchHospitalName = launchHospitalName;
    }

    public int getLaunchDepartmentId() {
        return launchDepartmentId;
    }

    public void setLaunchDepartmentId(int launchDepartmentId) {
        this.launchDepartmentId = launchDepartmentId;
    }

    public String getLaunchDepartmentName() {
        return launchDepartmentName;
    }

    public void setLaunchDepartmentName(String launchDepartmentName) {
        this.launchDepartmentName = launchDepartmentName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getDoctorHospitalId() {
        return doctorHospitalId;
    }

    public void setDoctorHospitalId(int doctorHospitalId) {
        this.doctorHospitalId = doctorHospitalId;
    }

    public String getDoctorHospitalName() {
        return doctorHospitalName;
    }

    public void setDoctorHospitalName(String doctorHospitalName) {
        this.doctorHospitalName = doctorHospitalName;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DCOrderDetailInfo{" +
                "orderId=" + orderId +
                ", itemId=" + itemId +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderType=" + orderType +
                ", launchDoctorId=" + launchDoctorId +
                ", launchDoctorName='" + launchDoctorName + '\'' +
                ", launchHospitalId=" + launchHospitalId +
                ", launchHospitalName='" + launchHospitalName + '\'' +
                ", launchDepartmentId=" + launchDepartmentId +
                ", launchDepartmentName='" + launchDepartmentName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorHospitalId=" + doctorHospitalId +
                ", doctorHospitalName='" + doctorHospitalName + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
