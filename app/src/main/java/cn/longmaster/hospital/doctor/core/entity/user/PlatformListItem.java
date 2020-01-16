package cn.longmaster.hospital.doctor.core.entity.user;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 21:20
 * @description: 平台服务费列表
 */
public class PlatformListItem {


    /**
     * id : 1 //服务费自增ID
     * hospital_id : 1 //医院ID
     * department_id : 1 //科室ID
     * type : 1 //类型 1平台服务费
     * amt_value : 500.00 //服务费用金额
     * amt_dt : 2019-06-10 //服务费用时间
     * advance_value : 50.00 //垫付单金额
     * file_name : //垫付文件名称
     * upload_uid : 1000021 //上传人ID
     * check_state : 1 //审核状态 0等待审核 1审核成功 2审核失败
     * check_desc :
     * insert_dt : 2019-07-29 09:50:03
     * hospital_name : 北京大学第一医院
     * department_name : 测试科室1
     */

    @JsonField("id")
    private String id;
    @JsonField("hospital_id")
    private String hospitalId;
    @JsonField("department_id")
    private String departmentId;
    @JsonField("type")
    private String type;
    @JsonField("amt_value")
    private String amtValue;
    @JsonField("amt_dt")
    private String amtDt;
    @JsonField("advance_value")
    private String advanceValue;
    @JsonField("file_name")
    private String fileName;
    @JsonField("upload_uid")
    private String uploadUid;
    @JsonField("check_state")
    private int checkState;
    @JsonField("check_desc")
    private String checkDesc;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("hospital_name")
    private String hospitalName;
    @JsonField("department_name")
    private String departmentName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmtValue() {
        return amtValue;
    }

    public void setAmtValue(String amtValue) {
        this.amtValue = amtValue;
    }

    public String getAmtDt() {
        return amtDt;
    }

    public void setAmtDt(String amtDt) {
        this.amtDt = amtDt;
    }

    public String getAdvanceValue() {
        return advanceValue;
    }

    public void setAdvanceValue(String advanceValue) {
        this.advanceValue = advanceValue;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadUid() {
        return uploadUid;
    }

    public void setUploadUid(String uploadUid) {
        this.uploadUid = uploadUid;
    }

    public int getCheckState() {
        return checkState;
    }

    public void setCheckState(int checkState) {
        this.checkState = checkState;
    }

    public String getCheckDesc() {
        return checkDesc;
    }

    public void setCheckDesc(String checkDesc) {
        this.checkDesc = checkDesc;
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
}