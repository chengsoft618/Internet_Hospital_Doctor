package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/27.
 */

public class YearInfo implements Serializable {
    @JsonField("mat_year")
    private String matYear;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getMatYear() {
        return matYear;
    }

    public void setMatYear(String matYear) {
        this.matYear = matYear;
    }

    @Override
    public String toString() {
        return "YearInfo{" +
                "matYear='" + matYear + '\'' +
                ", selected=" + selected +
                '}';
    }
}
