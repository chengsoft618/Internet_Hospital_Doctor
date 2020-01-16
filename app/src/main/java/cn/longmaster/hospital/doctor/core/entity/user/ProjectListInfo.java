package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class ProjectListInfo implements Serializable {
    @JsonField("item_id")
    private int itemId;//专科建设项目ID
    @JsonField("item_num")
    private String itemNum;//专科建设项目编号
    @JsonField("hospital_id")
    private int hospitalId;//基层医院ID
    @JsonField("department_id")
    private int departmentId;//项目科室ID
    @JsonField("item_begin_dt")
    private String itemBeginDt;//项目开始时间
    @JsonField("item_end_dt")
    private String itemEndDt;//项目结束时间
    @JsonField("cur_stage")
    private int curStage;//当前阶段
    @JsonField("insert_dt")
    private String insertDt;//
    @JsonField("hospital_name")
    private String hospitalName;//基层医院名称
    @JsonField("department_name")
    private String departmentName;//项目科室名称
    @JsonField("user_type")
    private int userType;//角色身份 1专家组长，2专家组专家，3基层医生科主任，4基层医生，5管理员，6领衔专家，7首诊医生，8导医

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getItemBeginDt() {
        return itemBeginDt;
    }

    public void setItemBeginDt(String itemBeginDt) {
        this.itemBeginDt = itemBeginDt;
    }

    public String getItemEndDt() {
        return itemEndDt;
    }

    public void setItemEndDt(String itemEndDt) {
        this.itemEndDt = itemEndDt;
    }

    public int getCurStage() {
        return curStage;
    }

    public void setCurStage(int curStage) {
        this.curStage = curStage;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFullDepartmentName() {
        return hospitalName + "-" + departmentName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "ProjectListInfo{" +
                "itemId=" + itemId +
                ", itemNum='" + itemNum + '\'' +
                ", hospitalId=" + hospitalId +
                ", departmentId=" + departmentId +
                ", itemBeginDt='" + itemBeginDt + '\'' +
                ", itemEndDt='" + itemEndDt + '\'' +
                ", curStage=" + curStage +
                ", insertDt='" + insertDt + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", userType=" + userType +
                '}';
    }
}
