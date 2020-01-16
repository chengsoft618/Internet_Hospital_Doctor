package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/2/19.
 */

public class DepartmentIntroductionInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;
    @JsonField("parent_id")
    private int parentId;
    @JsonField("department_name")
    private String departmentName;
    @JsonField("introduction")//科室介绍
    private String introduction;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        return "DepartmentIntroductionInfo{" +
                "departmentId=" + departmentId +
                ", parentId=" + parentId +
                ", departmentName='" + departmentName + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}
