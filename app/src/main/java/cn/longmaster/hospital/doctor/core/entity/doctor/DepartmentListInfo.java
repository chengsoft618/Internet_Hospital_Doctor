package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/11/23.
 */
public class DepartmentListInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;//科室id
    @JsonField("department_name")
    private String departmentName;//科室名称

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

    @Override
    public String toString() {
        return "DepartmentListInfo{" +
                "departmentId=" + departmentId +
                ", departmentName=" + departmentName +
                '}';
    }
}

