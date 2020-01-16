package cn.longmaster.hospital.doctor.core.entity.account;

import java.io.Serializable;

/**
 * Created by W·H·K on 2019/1/24.
 */
public class AccountCashInfo implements Serializable {
    private int incomeId;//收入id
    private int userId;//用户id
    private int type;//类型  1收入 2欠款
    private int role;//角色
    private int appointmentId;//预约id
    private int appType;//就诊类型
    private float incomeValue;//价格
    private int state;//状态 101未结算 102已打款 103提现中 104待提现 201欠款未处理 202欠款处理中 203欠款已处理
    private String remark;//备注
    private String updateDt;//业务更新时间
    private int opType;//操作人类型 1导医端 2管理后台 3医生类账号
    private int opUid;//操作人id
    private String insertDt;//插入时间
    private String doctorName;//上级专家名字
    private String launchHospitalName;//发起医院名称
    private String cureDt;//业务时间
    private int cashType;//1 收入类型；2其他类型；3欠款类型
    private boolean selected;//是否选中

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

    public int getCashType() {
        return cashType;
    }

    public void setCashType(int cashType) {
        this.cashType = cashType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "AccountCashInfo{" +
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
                ", cashType=" + cashType +
                ", selected=" + selected +
                '}';
    }
}
