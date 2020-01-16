package cn.longmaster.hospital.doctor.core.entity.department;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 15:47
 * @description: 科室列表信息
 */
public class DepartmentItemInfo {

    /**
     * parent_id : 57
     * parent_department : 内科
     * department_list : [{"department_id":"63","department_name":"心血管内科"},{"department_id":"58","department_name":"呼吸内科"},{"department_id":"61","department_name":"消化内科"},{"department_id":"62","department_name":"神经内科"},{"department_id":"64","department_name":"血液内科"},{"department_id":"65","department_name":"肾内科"},{"department_id":"66","department_name":"风湿免疫科"},{"department_id":"67","department_name":"内分泌科"},{"department_id":"100","department_name":"老年病科"}]
     */
    @JsonField("parent_id")
    private String parentId;
    @JsonField("parent_department")
    private String parentDepartment;
    @JsonField("department_list")
    private List<DepartmentInfo> departmentList;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public List<DepartmentInfo> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentInfo> departmentList) {
        this.departmentList = departmentList;
    }
}
