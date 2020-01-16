package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

/**
 * @author ABiao_Abiao
 * @date 2019/12/26 10:29
 * @description: 值班门诊患者详情
 */
public class DCDutyRoomPatientInfo {
    private String time;
    private List<String> picUtrls;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPicUtrls() {
        return picUtrls;
    }

    public void setPicUtrls(List<String> picUtrls) {
        this.picUtrls = picUtrls;
    }
}
