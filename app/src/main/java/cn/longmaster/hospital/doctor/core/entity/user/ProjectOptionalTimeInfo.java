package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class ProjectOptionalTimeInfo implements Serializable {
    @JsonField("week")
    private int week;//可约时间周数
    @JsonField("hour")
    private int hour;//可约时间小时

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "ProjectOptionalTimeInfo{" +
                "week=" + week +
                ", hour=" + hour +
                '}';
    }
}
