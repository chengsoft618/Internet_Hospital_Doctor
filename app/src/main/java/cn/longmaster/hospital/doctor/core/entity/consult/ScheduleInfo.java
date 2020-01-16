package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 排班信息
 * Created by JinKe on 2016-08-29.
 */
public class ScheduleInfo implements Serializable {
    @JsonField("scheduing_id")
    private int ScheduingId;//排班Id
    @JsonField("begin_dt")
    private String beginDt;//排班开始时间
    @JsonField("end_dt")
    private String endDt;//排班结束时间

    public int getScheduingId() {
        return ScheduingId;
    }

    public void setScheduingId(int scheduingId) {
        ScheduingId = scheduingId;
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

    @Override
    public String toString() {
        return "ScheduleInfo{" +
                "ScheduingId=" + ScheduingId +
                ", beginDt='" + beginDt + '\'' +
                ", endDt='" + endDt + '\'' +
                '}';
    }
}
