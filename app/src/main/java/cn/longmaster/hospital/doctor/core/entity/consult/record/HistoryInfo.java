package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 手术治疗
 * Created by Yang² on 2016/8/3.
 */
public class HistoryInfo implements Serializable {
    @JsonField("appointment_id")
    private String appointmentId;
    @JsonField("history_type")
    private int historyType;
    @JsonField("history_desc")
    private String historyDesc;
    @JsonField("insert_dt")
    private String insertDt;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getHistoryType() {
        return historyType;
    }

    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public String getHistoryDesc() {
        return historyDesc;
    }

    public void setHistoryDesc(String historyDesc) {
        this.historyDesc = historyDesc;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "HistoryInfo{" +
                "appointmentId='" + appointmentId + '\'' +
                ", historyType='" + historyType + '\'' +
                ", historyDesc='" + historyDesc + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
