package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 会诊申请单
 * Created by Yang² on 2016/8/13.
 */
public class ApplyDescInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("apply_remark")
    private String applyRemark;//会诊/咨询目的
    @JsonField("apply_diagnosis_type")
    private int applyDiagnosisType;//申请类型 1：会诊模式2：医学咨询模式
    @JsonField("insert_dt")
    private String insertDt;//插入时间
    @JsonField("profile")
    private String profile;//病情摘要/病情简介
    @JsonField("narrator")
    private String narrator;//病情陈述人

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark;
    }

    public int getApplyDiagnosisType() {
        return applyDiagnosisType;
    }

    public void setApplyDiagnosisType(int applyDiagnosisType) {
        this.applyDiagnosisType = applyDiagnosisType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNarrator() {
        return narrator;
    }

    public void setNarrator(String narrator) {
        this.narrator = narrator;
    }

    @Override
    public String toString() {
        return "ApplyDescInfo{" +
                "appointmentId=" + appointmentId +
                ", applyRemark='" + applyRemark + '\'' +
                ", applyDiagnosisType=" + applyDiagnosisType +
                ", insertDt=" + insertDt +
                ", profile=" + profile +
                ", narrator=" + narrator +
                '}';
    }
}
