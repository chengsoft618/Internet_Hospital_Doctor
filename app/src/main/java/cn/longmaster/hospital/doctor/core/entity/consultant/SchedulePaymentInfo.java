package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 排班支付确认单
 * Crated by Yang² on 2017/12/28.
 */

public class SchedulePaymentInfo implements Serializable {
    @JsonField("payment_info")
    private PaymentInfo paymentInfo;
    @JsonField("app_data")
    private List<AppointmentId> appointmentList;

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public List<AppointmentId> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<AppointmentId> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @Override
    public String toString() {
        return "SchedulePaymentInfo{" +
                "paymentInfo=" + paymentInfo +
                ", appointmentList=" + appointmentList +
                '}';
    }
}
