package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 排班服务相关信息
 * Created by JinKe on 2016-07-28.
 */
public class ScheduleRelateInfo implements Serializable {
    @JsonField("scheduing_id")
    private int scheduingId;//排班/影像服务ID
    @JsonField("package_id")
    private int packageId;//服务信息ID
    @JsonField("service_type")
    private int serviceType;//模式：101001远程会诊 101002远程咨询 101003远程会诊复诊 101004远程咨询复诊 102001远程影像会诊
    @JsonField("grade_price_id")
    private int gradePriceId;//价格档位ID
    @JsonField("doctor_pay")
    private float doctorPay;//医生分成金额
    @JsonField("first_dia_doctor_money")
    private float firstDiaDoctorMoney;//首诊医生分成金额
    @JsonField("admission_price")
    private float admissionPrice;//排班或者影像服务金额
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public int getGradePriceId() {
        return gradePriceId;
    }

    public void setGradePriceId(int gradePriceId) {
        this.gradePriceId = gradePriceId;
    }

    public float getDoctorPay() {
        return doctorPay;
    }

    public void setDoctorPay(float doctorPay) {
        this.doctorPay = doctorPay;
    }

    public float getFirstDiaDoctorMoney() {
        return firstDiaDoctorMoney;
    }

    public void setFirstDiaDoctorMoney(float firstDiaDoctorMoney) {
        this.firstDiaDoctorMoney = firstDiaDoctorMoney;
    }

    public float getAdmissionPrice() {
        return admissionPrice;
    }

    public void setAdmissionPrice(float admissionPrice) {
        this.admissionPrice = admissionPrice;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "ScheduleRelateInfo{" +
                "scheduingId=" + scheduingId +
                ", packageId=" + packageId +
                ", serviceType=" + serviceType +
                ", gradePriceId=" + gradePriceId +
                ", doctorPay=" + doctorPay +
                ", firstDiaDoctorMoney=" + firstDiaDoctorMoney +
                ", admissionPrice=" + admissionPrice +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}