package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 根据用户ID拉取用户账单明细
 * Created by JinKe on 2016-07-27.
 */
public class UserBillInfo implements Serializable {
    @JsonField("serial_id")
    private String serialId;//明细ID
    @JsonField("department_id")
    private int departmentId;//预约ID
    @JsonField("user_id")
    private int userId;//用户ID
    @JsonField("order_id")
    private String orderId;//订单ID
    @JsonField("order_value")
    private float orderValue;//变化金额，单位：分
    @JsonField("reason")
    private int reason;//变化原因101：给医生打款102：充值103：消费104：医生提现105：医生提成106: 消费失败107: 消费者退款108：医生补偿109：更换医生110：医生奖励111：医生惩罚
    @JsonField("remark")
    private String remark;//备注
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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

    @Override
    public String toString() {
        return "UserBillInfo{" +
                "serialId=" + serialId +
                ", departmentId=" + departmentId +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", orderValue=" + orderValue +
                ", reason=" + reason +
                ", remark='" + remark + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
