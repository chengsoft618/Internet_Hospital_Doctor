package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 值班门诊-检查检验
 * Created by yangyong on 2019-12-02.
 */
public class DCInspectInfo implements Serializable {
    @JsonField("inspect")
    private String inspect;

    private boolean isChecked;

    public String getInspect() {
        return inspect;
    }

    public void setInspect(String inspect) {
        this.inspect = inspect;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DCInspectInfo{" +
                "inspect='" + inspect + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
