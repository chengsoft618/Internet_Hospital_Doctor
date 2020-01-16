package cn.longmaster.hospital.doctor.core.entity.consult.video;

/**
 * 房间状态信息
 * Created by Tengshuxiang on 2016-06-03.
 */
public class VideoStateInfo {
    public final String time;
    public final String tip;
    public final boolean isColorDeepen;
    public final String option;

    public VideoStateInfo(String time, String tip, boolean isColorDeepen, String option) {
        this.time = time;
        this.tip = tip;
        this.isColorDeepen = isColorDeepen;
        this.option = option;
    }
}
