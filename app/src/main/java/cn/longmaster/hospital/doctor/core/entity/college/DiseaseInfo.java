package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/3/27.
 */

public class DiseaseInfo implements Serializable {
    private String diseaseName;
    private boolean selected;

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "DiseaseInfo{" +
                "diseaseName='" + diseaseName + '\'' +
                ", selected=" + selected +
                '}';
    }
}
