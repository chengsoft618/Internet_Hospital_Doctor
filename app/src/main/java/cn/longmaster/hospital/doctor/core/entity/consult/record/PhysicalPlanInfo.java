package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 物理治疗
 * Created by Yang² on 2016/8/3.
 */
public class PhysicalPlanInfo implements Serializable {
    @JsonField("appointment_id")
    private String appointmentId;
    @JsonField("physical_way")
    private String physicaWay;
    @JsonField("physical_treatment")
    private String physicalTreatment;
    @JsonField("insert_dt")
    private String insertDt;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPhysicaWay() {
        return physicaWay;
    }

    public void setPhysicaWay(String physicaWay) {
        this.physicaWay = physicaWay;
    }

    public String getPhysicalTreatment() {
        return physicalTreatment;
    }

    public void setPhysicalTreatment(String physicalTreatment) {
        this.physicalTreatment = physicalTreatment;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "PhysicalPlanInfo{" +
                "appointmentId='" + appointmentId + '\'' +
                ", physicaWay='" + physicaWay + '\'' +
                ", physicalTreatment='" + physicalTreatment + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
