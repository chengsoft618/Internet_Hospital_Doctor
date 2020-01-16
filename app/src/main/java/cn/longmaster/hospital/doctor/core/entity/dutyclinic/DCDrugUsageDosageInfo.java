package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.io.Serializable;

/**
 * 值班门诊-药品用法用量
 * Created by yangyong on 2019-12-05.
 */
public class DCDrugUsageDosageInfo implements Serializable {
    private String title;
    private boolean isSelected;

    public DCDrugUsageDosageInfo(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "DCDrugUsageDosageInfo{" +
                "title='" + title + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
