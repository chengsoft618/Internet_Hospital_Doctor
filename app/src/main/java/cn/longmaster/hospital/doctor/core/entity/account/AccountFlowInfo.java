package cn.longmaster.hospital.doctor.core.entity.account;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/1/24.
 */
public class AccountFlowInfo implements Serializable {
    @JsonField("serial_id")
    private String serialId;//明细
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("user_id")
    private int userId;//用户id
    @JsonField("order_id")
    private String orderId;//流水订单id
    @JsonField("order_value")
    private float orderValue;//流水价格
    @JsonField("reason")
    private int reason;//原因
    @JsonField("remark")
    private String remark;//备注
    @JsonField("insert_dt")
    private String insertDt;//流水生成时间
    @JsonField("doctor_name")
    private String doctorName;//上级专家名字
    @JsonField("launch_hospital_name")
    private String launchHospitalName;//发起医院名称
    @JsonField("cure_dt")
    private String cureDt;//业务时间
    @JsonField("role")
    private int role;//角色
    @JsonField("app_type")
    private int appType;//就诊类型
    @JsonField("is_merge")
    private int isMerge;//是否是提现
    @JsonField("cure_name")
    private String cureName;//类型
    @JsonField("reason_desc")
    private String reasonDesc;//流水类型

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(float orderValue) {
        this.orderValue = orderValue;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getLaunchHospitalName() {
        return launchHospitalName;
    }

    public void setLaunchHospitalName(String launchHospitalName) {
        this.launchHospitalName = launchHospitalName;
    }

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getIsMerge() {
        return isMerge;
    }

    public void setIsMerge(int isMerge) {
        this.isMerge = isMerge;
    }

    public String getCureName() {
        return cureName;
    }

    public void setCureName(String cureName) {
        this.cureName = cureName;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    @Override
    public String toString() {
        return "AccountFlowInfo{" +
                "serialId='" + serialId + '\'' +
                ", appointmentId=" + appointmentId +
                ", userId=" + userId +
                ", orderId='" + orderId + '\'' +
                ", orderValue=" + orderValue +
                ", reason=" + reason +
                ", remark='" + remark + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", launchHospitalName='" + launchHospitalName + '\'' +
                ", cureDt='" + cureDt + '\'' +
                ", role=" + role +
                ", appType=" + appType +
                ", isMerge=" + isMerge +
                ", cureName='" + cureName + '\'' +
                ", reasonDesc='" + reasonDesc + '\'' +
                '}';
    }
}
