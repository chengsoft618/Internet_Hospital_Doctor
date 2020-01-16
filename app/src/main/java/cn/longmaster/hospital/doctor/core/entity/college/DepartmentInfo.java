package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/27.
 */

public class DepartmentInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;
    @JsonField("department_name")
    private String departmentName;
    @JsonField("disease")
    private List<String> diseases;
    private boolean selected;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public List<String> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<String> diseases) {
        this.diseases = diseases;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "DepartmentListInfo{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", diseases=" + diseases +
                ", selected=" + selected +
                '}';
    }
}
