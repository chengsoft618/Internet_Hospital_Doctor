package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;

/**
 * 根据医生ID和服务模式统计排班模式价格区间
 * Created by Yang² on 2016/8/20.
 */
public class ScheduleStatisticInfo implements Serializable {
    private StatisticInfo remoteConsult;//会诊模式价格区间 service_type=0或者101001时输出
    private StatisticInfo remoteAdvice;//咨询模式价格区间service_type=0或者101002时输出
    private StatisticInfo remoteConsultRecure;//会诊复诊模式价格区间service_type=0或者101003时输出
    private StatisticInfo imageConsult;//影像会诊模式价格区间service_type=0或者102001时输出

    public StatisticInfo getRemoteConsult() {
        return remoteConsult;
    }

    public void setRemoteConsult(StatisticInfo remoteConsult) {
        this.remoteConsult = remoteConsult;
    }

    public StatisticInfo getRemoteAdvice() {
        return remoteAdvice;
    }

    public void setRemoteAdvice(StatisticInfo remoteAdvice) {
        this.remoteAdvice = remoteAdvice;
    }

    public StatisticInfo getRemoteConsultRecure() {
        return remoteConsultRecure;
    }

    public void setRemoteConsultRecure(StatisticInfo remoteConsultRecure) {
        this.remoteConsultRecure = remoteConsultRecure;
    }

    public StatisticInfo getImageConsult() {
        return imageConsult;
    }

    public void setImageConsult(StatisticInfo imageConsult) {
        this.imageConsult = imageConsult;
    }

    @Override
    public String toString() {
        return "ScheduleStatisticInfo{" +
                "remoteConsult=" + remoteConsult +
                ", remoteAdvice=" + remoteAdvice +
                ", remoteConsultRecure=" + remoteConsultRecure +
                ", imageConsult=" + imageConsult +
                '}';
    }
}
