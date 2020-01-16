package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/3/18.
 */

public class ProjectDetailsInfo implements Serializable {
    @JsonField("item_id")
    private int itemId;//专科建设项目ID
    @JsonField("item_num")
    private String itemNum;
    @JsonField("hospital_id")
    private int hospitalId;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("department_id")
    private int departmentId;
    @JsonField("department_name")
    private String departmentName;
    @JsonField("cur_stage")
    private int curStage;
    @JsonField("stage_id")
    private int stageId;//阶段id
    @JsonField("stage_name")
    private String stageName;//阶段名称
    @JsonField("until_appoint")
    private int untilAppoint;//提前预约天数
    @JsonField("course_frequency")
    private int courseFrequency;///课程频率
    @JsonField("stage_begin_dt")
    private String stageBeginDt;//阶段开始时间
    @JsonField("stage_end_dt")
    private int stageEndDt;//阶段结束时间
    @JsonField("time_list")
    private List<ProjectOptionalTimeInfo> optionalTimeList;//可约时间list
    @JsonField("app_list")
    private List<ProjectAppointTimeInfo> appointTimeList;//已约时间list
    @JsonField("class_app_num")
    private int classAppNum;//已约课时数
    @JsonField("class_complete_num")
    private int classCompleteNum;//已完成课时数
    @JsonField("class_total_num")
    private int classTotalNum;//总课时数
    @JsonField("class_last_num")
    private int classLastNum;//未约课时数

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

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

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

    public int getCurStage() {
        return curStage;
    }

    public void setCurStage(int curStage) {
        this.curStage = curStage;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public int getUntilAppoint() {
        return untilAppoint;
    }

    public void setUntilAppoint(int untilAppoint) {
        this.untilAppoint = untilAppoint;
    }

    public int getCourseFrequency() {
        return courseFrequency;
    }

    public void setCourseFrequency(int courseFrequency) {
        this.courseFrequency = courseFrequency;
    }

    public String getStageBeginDt() {
        return stageBeginDt;
    }

    public void setStageBeginDt(String stageBeginDt) {
        this.stageBeginDt = stageBeginDt;
    }

    public int getStageEndDt() {
        return stageEndDt;
    }

    public void setStageEndDt(int stageEndDt) {
        this.stageEndDt = stageEndDt;
    }

    public List<ProjectOptionalTimeInfo> getOptionalTimeList() {
        return optionalTimeList;
    }

    public void setOptionalTimeList(List<ProjectOptionalTimeInfo> optionalTimeList) {
        this.optionalTimeList = optionalTimeList;
    }

    public List<ProjectAppointTimeInfo> getAppointTimeList() {
        return appointTimeList;
    }

    public void setAppointTimeList(List<ProjectAppointTimeInfo> appointTimeList) {
        this.appointTimeList = appointTimeList;
    }

    public int getClassAppNum() {
        return classAppNum;
    }

    public void setClassAppNum(int classAppNum) {
        this.classAppNum = classAppNum;
    }

    public int getClassCompleteNum() {
        return classCompleteNum;
    }

    public void setClassCompleteNum(int classCompleteNum) {
        this.classCompleteNum = classCompleteNum;
    }

    public int getClassTotalNum() {
        return classTotalNum;
    }

    public void setClassTotalNum(int classTotalNum) {
        this.classTotalNum = classTotalNum;
    }

    public int getClassLastNum() {
        return classLastNum;
    }

    public void setClassLastNum(int classLastNum) {
        this.classLastNum = classLastNum;
    }

    @Override
    public String toString() {
        return "ProjectDetailsInfo{" +
                "itemId=" + itemId +
                ", itemNum='" + itemNum + '\'' +
                ", hospitalId=" + hospitalId +
                ", hospitalName='" + hospitalName + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", curStage=" + curStage +
                ", stageId=" + stageId +
                ", stageName='" + stageName + '\'' +
                ", untilAppoint=" + untilAppoint +
                ", courseFrequency=" + courseFrequency +
                ", stageBeginDt='" + stageBeginDt + '\'' +
                ", stageEndDt=" + stageEndDt +
                ", optionalTimeList=" + optionalTimeList +
                ", appointTimeList=" + appointTimeList +
                ", classAppNum=" + classAppNum +
                ", classCompleteNum=" + classCompleteNum +
                ", classTotalNum=" + classTotalNum +
                ", classLastNum=" + classLastNum +
                '}';
    }

    public String getFullDepartmentName() {
        return hospitalName + "-" + departmentName;
    }
}
