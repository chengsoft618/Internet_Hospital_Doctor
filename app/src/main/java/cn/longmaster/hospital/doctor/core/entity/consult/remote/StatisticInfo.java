package cn.longmaster.hospital.doctor.core.entity.consult.remote;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 统计排班模式价格区间
 * Created by Yang² on 2016/8/20.
 */
public class StatisticInfo implements Serializable {
    @JsonField("min")
    private float min;//最小价格
    @JsonField("max")
    private float max;//最大价格

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "StatisticInfo{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
