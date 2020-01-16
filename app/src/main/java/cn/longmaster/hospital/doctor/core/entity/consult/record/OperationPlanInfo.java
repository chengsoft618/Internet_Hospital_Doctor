package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 手术治疗
 * Created by Yang² on 2016/8/3.
 */
public class OperationPlanInfo implements Serializable {
    @JsonField("appointment_id")
    private String appointmentId;
    @JsonField("operation_way")
    private String operationWay;
    @JsonField("operation_dt")
    private String operationDt;
    @JsonField("insert_dt")
    private String insertDt;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getOperationWay() {
        return operationWay;
    }

    public void setOperationWay(String operationWay) {
        this.operationWay = operationWay;
    }

    public String getOperationDt() {
        return operationDt;
    }

    public void setOperationDt(String operationDt) {
        this.operationDt = operationDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "OperationPlanInfo{" +
                "appointmentId='" + appointmentId + '\'' +
                ", operationWay='" + operationWay + '\'' +
                ", operationDt='" + operationDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
