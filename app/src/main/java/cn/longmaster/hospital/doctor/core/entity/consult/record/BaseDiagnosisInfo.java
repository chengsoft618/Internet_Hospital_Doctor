package cn.longmaster.hospital.doctor.core.entity.consult.record;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医嘱基类
 * Created by yangyong on 16/8/24.
 */
public class BaseDiagnosisInfo implements Serializable {
    @JsonField("appointment_id")
    private int appointmentId;//预约ID
    @JsonField("recure_num")
    private int recureNum;//复诊次数
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    private boolean isMedia;//是否多媒体医嘱,当为文本时为false
    private boolean isLocalData;//是否本地数据,默认为false

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRecureNum() {
        return recureNum;
    }

    public void setRecureNum(int recureNum) {
        this.recureNum = recureNum;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public boolean isMedia() {
        return isMedia;
    }

    public void setMedia(boolean media) {
        isMedia = media;
    }

    public boolean isLocalData() {
        return isLocalData;
    }

    public void setLocalData(boolean localData) {
        isLocalData = localData;
    }

    @Override
    public String toString() {
        return "BaseDiagnosisInfo{" +
                "appointmentId=" + appointmentId +
                ", recureNum=" + recureNum +
                ", insertDt='" + insertDt + '\'' +
                ", isMedia=" + isMedia +
                ", isLocalData=" + isLocalData +
                '}';
    }
}
