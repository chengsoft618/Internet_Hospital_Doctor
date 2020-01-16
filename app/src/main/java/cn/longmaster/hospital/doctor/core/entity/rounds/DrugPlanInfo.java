package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/16.
 */

public class DrugPlanInfo implements Serializable {
    @JsonField("drug_id")
    private int drugId;
    @JsonField("drug_name")
    private String drugName;
    @JsonField("drug_way")
    private String drugWay;
    @JsonField("drug_dt")
    private String drugDt;

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugWay() {
        return drugWay;
    }

    public void setDrugWay(String drugWay) {
        this.drugWay = drugWay;
    }

    public String getDrugDt() {
        return drugDt;
    }

    public void setDrugDt(String drugDt) {
        this.drugDt = drugDt;
    }

    @Override
    public String toString() {
        return "DrugPlanInfo{" +
                "drugId=" + drugId +
                ", drugName='" + drugName + '\'' +
                ", drugWay='" + drugWay + '\'' +
                ", drugDt='" + drugDt + '\'' +
                '}';
    }
}
