package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/18 15:52
 * @description:
 */
public class DCDutyVisitPlantItem {
    /**
     * temp_id : 1
     * item_id : 16
     * is_sys : 1
     * medical_id : 100021265
     * temp_name : 随访模板1
     * insert_dt : 2019-12-26 14:54:29
     * followup_temp_list : [{},{"list_id":"2","temp_id":"1","list_dt":"2019-12-26 16:55:32","insert_dt":"2019-12-26 16:55:48","followup_temp_detail_list":[{"id":"1","temp_id":"1","list_id":"1","type":"1","content":"请于2019年12月26日到贵医复诊","sub_content":"","insert_dt":"2019-12-27 15:25:32"},{"id":"2","temp_id":"1","list_id":"1","type":"2","content":"请遵医嘱按时按量服药","sub_content":"","insert_dt":"2019-12-27 15:25:32"},{"id":"3","temp_id":"1","list_id":"1","type":"3","content":"请上传近期就诊后病历资料","sub_content":"","insert_dt":"2019-12-27 15:25:32"}]}]
     */

    //随访计划模板id
    @JsonField("temp_id")
    private int tempId;
    //项目id
    @JsonField("item_id")
    private int itemId;
    //是否系统模板
    @JsonField("is_sys")
    private int isSys;
    //病历id
    @JsonField("medical_id")
    private int medicalId;
    //随访计划模板名称
    @JsonField("temp_name")
    private String tempName;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("followup_temp_list")
    private List<DCDutyVisitPlantTempItem> followupTempList;

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getIsSys() {
        return isSys;
    }

    public void setIsSys(int isSys) {
        this.isSys = isSys;
    }

    public int getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(int medicalId) {
        this.medicalId = medicalId;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public List<DCDutyVisitPlantTempItem> getFollowupTempList() {
        return followupTempList;
    }

    public void setFollowupTempList(List<DCDutyVisitPlantTempItem> followupTempList) {
        this.followupTempList = followupTempList;
    }

    @Override
    public String toString() {
        return "DCDutyVisitPlantItem{" +
                "tempId=" + tempId +
                ", itemId=" + itemId +
                ", isSys=" + isSys +
                ", medicalId=" + medicalId +
                ", tempName='" + tempName + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", followupTempList=" + followupTempList +
                '}';
    }
}
