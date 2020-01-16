package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

/**
 * 筛选回传数据实体
 * Created by Yang² on 2018/1/4.
 */

public class VisitScreenInfo implements Serializable {
    private int selectTimeResId;
    private String beginDt;
    private String endDt;
    private int doctorId;

    public int getSelectTimeResId() {
        return selectTimeResId;
    }

    public void setSelectTimeResId(int selectTimeResId) {
        this.selectTimeResId = selectTimeResId;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "VisitScreenInfo{" +
                "selectTimeResId=" + selectTimeResId +
                ", beginDt='" + beginDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", doctorId=" + doctorId +
                '}';
    }
}
