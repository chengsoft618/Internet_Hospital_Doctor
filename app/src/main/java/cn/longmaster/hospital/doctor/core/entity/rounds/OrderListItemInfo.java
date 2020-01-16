package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/16.
 */

public class OrderListItemInfo implements Serializable {
    @JsonField("order_id")
    private int orderId;
    //主题
    @JsonField("order_title")
    private String orderTitle;
    //订单类型
    @JsonField("order_type")
    private int orderType;
    //订单状态
    @JsonField("order_state")
    private int orderState;
    //状态原因
    @JsonField("state_reason")
    private int stateReason;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    //上级专家
    @JsonField("doctor_id")
    private int doctorId;
    //上级专家姓名
    @JsonField("super_doctor_name")
    private String superDoctorName;
    //上级专家职称
    @JsonField("super_doctor_title")
    private String superDoctorTitle;
    //上级专家职位
    @JsonField("super_doctor_level")
    private String superDoctorLevel;
    //上级专家科室
    @JsonField("super_department_name")
    private String superDepartmentName;
    //上级专家医院
    @JsonField("super_hospital_name")
    private String superHospitalName;
    //上级专家头像
    @JsonField("super_doctor_img")
    private String superDoctorImg;
    //首诊id
    @JsonField("launch_doctor_id")
    private int launchDoctorId;
    //首诊名字
    @JsonField("launch_doctor_name")
    private String launchDoctorName;
    //首诊科室
    @JsonField("department_name")
    private String departmentName;
    //首诊医院
    @JsonField("atthos_id")
    private int atthosId;
    //首诊医院
    @JsonField("atthos_name")
    private String atthosName;
    //首诊医院LOGO
    @JsonField("atthos_logo")
    private String atthosLogo;
    //首诊评价
    @JsonField("attend_evaluate_id")
    private int attendEvaluateId;
    //就诊时间
    @JsonField("cure_dt")
    private String cureDt;
    //是否需要ppt
    @JsonField("is_ppt")
    private int isNeedPpt;
    @JsonField("order_medical_count")
    private int orderMedicalCount;

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

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getStateReason() {
        return stateReason;
    }

    public void setStateReason(int stateReason) {
        this.stateReason = stateReason;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getAtthosId() {
        return atthosId;
    }

    public void setAtthosId(int atthosId) {
        this.atthosId = atthosId;
    }

    public int getAttendEvaluateId() {
        return attendEvaluateId;
    }

    public void setAttendEvaluateId(int attendEvaluateId) {
        this.attendEvaluateId = attendEvaluateId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getLaunchDoctorId() {
        return launchDoctorId;
    }

    public void setLaunchDoctorId(int launchDoctorId) {
        this.launchDoctorId = launchDoctorId;
    }

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public String getSuperDoctorName() {
        return superDoctorName;
    }

    public void setSuperDoctorName(String superDctorName) {
        this.superDoctorName = superDctorName;
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

    public String getLaunchDoctorName() {
        return launchDoctorName;
    }

    public void setLaunchDoctorName(String launchDoctorName) {
        this.launchDoctorName = launchDoctorName;
    }

    public String getAtthosName() {
        return atthosName;
    }

    public void setAtthosName(String atthosName) {
        this.atthosName = atthosName;
    }

    public String getAtthosLogo() {
        return atthosLogo;
    }

    public void setAtthosLogo(String atthosLogo) {
        this.atthosLogo = atthosLogo;
    }

    public int getIsNeedPpt() {
        return isNeedPpt;
    }

    public void setIsNeedPpt(int isNeedPpt) {
        this.isNeedPpt = isNeedPpt;
    }

    public int getOrderMedicalCount() {
        return orderMedicalCount;
    }

    public void setOrderMedicalCount(int orderMedicalCount) {
        this.orderMedicalCount = orderMedicalCount;
    }

    @Override
    public String toString() {
        return "OrderListItemInfo{" +
                "orderId=" + orderId +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderType=" + orderType +
                ", orderState=" + orderState +
                ", stateReason=" + stateReason +
                ", insertDt='" + insertDt + '\'' +
                ", doctorId=" + doctorId +
                ", superDoctorName='" + superDoctorName + '\'' +
                ", superDoctorTitle='" + superDoctorTitle + '\'' +
                ", superDepartmentName='" + superDepartmentName + '\'' +
                ", superHospitalName='" + superHospitalName + '\'' +
                ", superDoctorImg='" + superDoctorImg + '\'' +
                ", launchDoctorId=" + launchDoctorId +
                ", launchDoctorName='" + launchDoctorName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", atthosId=" + atthosId +
                ", atthosName='" + atthosName + '\'' +
                ", atthosLogo='" + atthosLogo + '\'' +
                ", attendEvaluateId=" + attendEvaluateId +
                ", cureDt='" + cureDt + '\'' +
                '}';
    }
}
