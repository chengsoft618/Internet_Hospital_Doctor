package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 手术治疗
 * Created by Yang² on 2016/8/3.
 */
public class DrugPlanInfo implements Serializable {
    @JsonField("appointment_id")
    private String appointmentId;
    @JsonField("drug_name")
    private String drugName;
    @JsonField("drug_way")
    private String drugWay;
    @JsonField("drug_dt")
    private String drugDt;
    @JsonField("insert_dt")
    private String insertDt;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DrugPlanInfo{" +
                "appointmentId='" + appointmentId + '\'' +
                ", drugName='" + drugName + '\'' +
                ", drugWay='" + drugWay + '\'' +
                ", drugDt='" + drugDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
