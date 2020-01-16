package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/7/16.
 */

public class IntentionTimeInfo implements Serializable {
    private double duration;
    private String unit;

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "IntentionTimeInfo{" +
                "duration=" + duration +
                ", unit='" + unit + '\'' +
                '}';
    }
}
