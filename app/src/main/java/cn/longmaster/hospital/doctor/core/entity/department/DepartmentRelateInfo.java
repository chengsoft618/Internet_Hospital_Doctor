package cn.longmaster.hospital.doctor.core.entity.department;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 科室关联信息
 * Created by JinKe on 2016-07-27.
 */
public class DepartmentRelateInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;//科室ID
    @JsonField("relation_id")
    private int relationId;//科室关联ID，暂时为二级科室
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DepartmentRelateInfo{" +
                "departmentId=" + departmentId +
                ", relationId=" + relationId +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
