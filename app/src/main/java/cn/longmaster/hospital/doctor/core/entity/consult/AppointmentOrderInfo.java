package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 根据预约ID拉取预约订单信息
 * Created by Yang² on 2016/8/20.
 */
public class AppointmentOrderInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("first_dia_doctor_money")
    private float firstDiaDoctorMoney;//首诊医生分成
    @JsonField("pay_value")
    private float payValue;//订单金额
    @JsonField("package_id")
    private int packageId;//服务[套餐]ID
    @JsonField("pay_dt")
    private String payDt;//支付时间
    @JsonField("doctor_pay")
    private float doctorPay;//医生分成
    @JsonField("order_no")
    private String orderNo;//支付编号
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public float getFirstDiaDoctorMoney() {
        return firstDiaDoctorMoney;
    }

    public void setFirstDiaDoctorMoney(float firstDiaDoctorMoney) {
        this.firstDiaDoctorMoney = firstDiaDoctorMoney;
    }

    public float getPayValue() {
        return payValue;
    }

    public void setPayValue(float payValue) {
        this.payValue = payValue;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPayDt() {
        return payDt;
    }

    public void setPayDt(String payDt) {
        this.payDt = payDt;
    }

    public float getDoctorPay() {
        return doctorPay;
    }

    public void setDoctorPay(float doctorPay) {
        this.doctorPay = doctorPay;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "AppointmentOrderInfo{" +
                "appointmentId=" + appointmentId +
                ", firstDiaDoctorMoney=" + firstDiaDoctorMoney +
                ", payValue=" + payValue +
                ", packageId=" + packageId +
                ", payDt='" + payDt + '\'' +
                ", doctorPay=" + doctorPay +
                ", orderNo='" + orderNo + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
