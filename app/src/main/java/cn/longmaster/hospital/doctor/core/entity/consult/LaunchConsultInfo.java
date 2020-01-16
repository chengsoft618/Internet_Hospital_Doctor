package cn.longmaster.hospital.doctor.core.entity.consult;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 发起会诊、影像会诊接口返回
 * Created by Yang² on 2016/7/28.
 */
public class LaunchConsultInfo {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("user_id")
    private int userId;//用户ID
    @JsonField("pay_password")
    private String payPassword;//支付密码
    @JsonField("share_password")
    private String sharePassword;//分享密码
    @JsonField("serial_number")
    private int serialNumber;//就诊序号
    @JsonField("predict_cure_dt")
    private String predictCureDt;//预计时间

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

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getSharePassword() {
        return sharePassword;
    }

    public void setSharePassword(String sharePassword) {
        this.sharePassword = sharePassword;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPredictCureDt() {
        return predictCureDt;
    }

    public void setPredictCureDt(String predictCureDt) {
        this.predictCureDt = predictCureDt;
    }

    @Override
    public String toString() {
        return "LaunchConsultInfo{" +
                "appointmentId=" + appointmentId +
                ", userId=" + userId +
                ", payPassword='" + payPassword + '\'' +
                ", sharePassword='" + sharePassword + '\'' +
                ", serialNumber=" + serialNumber +
                ", predictCureDt='" + predictCureDt + '\'' +
                '}';
    }
}
