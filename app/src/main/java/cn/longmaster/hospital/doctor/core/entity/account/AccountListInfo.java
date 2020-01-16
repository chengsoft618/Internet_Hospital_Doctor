package cn.longmaster.hospital.doctor.core.entity.account;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/1/24.
 */

public class AccountListInfo implements Serializable {
    @JsonField("income_id")
    private int incomeId;//收入id
    @JsonField("user_id")
    private int userId;//用户id
    @JsonField("type")
    private int type;//类型  1收入 2欠款
    @JsonField("role")
    private int role;//角色
    @JsonField("appointment_id")
    private int appointmentId;//预约id
    @JsonField("app_type")
    private int appType;//就诊类型
    @JsonField("income_value")
    private float incomeValue;//价格
    @JsonField("state")
    private int state;//状态 101未结算 102已打款 103提现中 104待提现 201欠款未处理 202欠款处理中 203欠款已处理
    @JsonField("remark")
    private String remark;//备注
    @JsonField("update_dt")
    private String updateDt;//业务更新时间
    @JsonField("op_type")
    private int opType;//操作人类型 1导医端 2管理后台 3医生类账号
    @JsonField("op_uid")
    private int opUid;//操作人id
    @JsonField("insert_dt")
    private String insertDt;//插入时间
    @JsonField("doctor_name")
    private String doctorName;//上级专家名字
    @JsonField("launch_hospital_name")
    private String launchHospitalName;//发起医院名称
    @JsonField("cure_dt")
    private String cureDt;//业务时间
    @JsonField("cure_name")
    private String cureName;//会诊类型
    @JsonField("reason")
    private int reason;//类型
    @JsonField("reason_desc")
    private String reasonDesc;//流水类型

    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public float getIncomeValue() {
        return incomeValue;
    }

    public void setIncomeValue(float incomeValue) {
        this.incomeValue = incomeValue;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getOpUid() {
        return opUid;
    }

    public void setOpUid(int opUid) {
        this.opUid = opUid;
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

    public String getCureName() {
        return cureName;
    }

    public void setCureName(String cureName) {
        this.cureName = cureName;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    @Override
    public String toString() {
        return "AccountListInfo{" +
                "incomeId=" + incomeId +
                ", userId=" + userId +
                ", type=" + type +
                ", role=" + role +
                ", appointmentId=" + appointmentId +
                ", appType=" + appType +
                ", incomeValue=" + incomeValue +
                ", state=" + state +
                ", remark='" + remark + '\'' +
                ", updateDt='" + updateDt + '\'' +
                ", opType=" + opType +
                ", opUid=" + opUid +
                ", insertDt='" + insertDt + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", launchHospitalName='" + launchHospitalName + '\'' +
                ", cureDt='" + cureDt + '\'' +
                ", cureName='" + cureName + '\'' +
                ", reason=" + reason +
                ", reasonDesc='" + reasonDesc + '\'' +
                '}';
    }
}
