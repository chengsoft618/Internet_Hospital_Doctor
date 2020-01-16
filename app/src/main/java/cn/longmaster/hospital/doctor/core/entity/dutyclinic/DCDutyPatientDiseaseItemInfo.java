package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/20 10:32
 * @description: 患者病程列表
 */
public class DCDutyPatientDiseaseItemInfo {

    /**
     * record_id : 12
     * medical_id : 10002185
     * record_dt : 2019-12-27 00:00:00
     * attr : 2
     * op_type : 1
     * op_uid : 34
     * insert_dt : 2019-12-26 11:04:28
     * op_name : 张
     */
    //病程记录id
    @JsonField("record_id")
    private int recordId;
    //病历id
    @JsonField("medical_id")
    private int medicalId;
    //病程记录时间
    @JsonField("record_dt")
    private String recordDt;
    //病程记录类型 1：首诊 2：复诊 3：处方医嘱 4：病例 5：影像 6：化验 7：体征  8：入院 9：出院 10：手术 11：随访记录 12：患者上传
    @JsonField("attr")
    private int attr;
    //病程记录类型描述
    @JsonField("attr_desc")
    private String attrDesc;
    //操作人类型 1导医 2后台 3医生 4患者
    @JsonField("op_type")
    private int opType;
    //操作人id
    @JsonField("op_uid")
    private int opUid;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    //操作人姓名
    @JsonField("op_name")
    private String opName;
    @JsonField("type_list")
    private List<DCDutyPrognoteInfo> prognoteInfoList;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public String getRecordDt() {
        return recordDt;
    }

    public void setRecordDt(String recordDt) {
        this.recordDt = recordDt;
    }

    public int getAttr() {
        return attr;
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public String getAttrDesc() {
        return attrDesc;
    }

    public void setAttrDesc(String attrDesc) {
        this.attrDesc = attrDesc;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getOpUid() {
        return opUid;
    }

    public void setOpUid(int opUid) {
        this.opUid = opUid;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public List<DCDutyPrognoteInfo> getPrognoteInfoList() {
        return prognoteInfoList;
    }

    public void setPrognoteInfoList(List<DCDutyPrognoteInfo> prognoteInfoList) {
        this.prognoteInfoList = prognoteInfoList;
    }
}
