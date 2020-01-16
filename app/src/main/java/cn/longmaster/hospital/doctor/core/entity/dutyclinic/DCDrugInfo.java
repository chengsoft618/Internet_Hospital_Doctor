package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-处方
 * Created by yangyong on 2019-12-04.
 */
public class DCDrugInfo implements Serializable {
    @JsonField("id")
    private int id;
    @JsonField("drug_name")
    private String drugName;
    @JsonField("cycle")
    private String cycle;
    @JsonField("cycle_num")
    private String cycleNum;
    @JsonField("dose")
    private String dose;
    @JsonField("dose_unit")
    private String doseUnit;
    @JsonField("remark")
    private String remark;
    @JsonField("insert_dt")
    private String insertDt;

    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(String cycleNum) {
        this.cycleNum = cycleNum;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(String doseUnit) {
        this.doseUnit = doseUnit;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DCDrugInfo{" +
                "id=" + id +
                ", drugName='" + drugName + '\'' +
                ", cycle='" + cycle + '\'' +
                ", cycleNum='" + cycleNum + '\'' +
                ", dose='" + dose + '\'' +
                ", doseUnit='" + doseUnit + '\'' +
                ", remark='" + remark + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
