package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 15:05
 * @description:
 */
public class DCDutyVisitPlantTempItem implements MultiItemEntity {
    public final static int ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_LAYOUT = 0;
    public final static int ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_ADD_LAYOUT = 1;
    /**
     * list_id : 2
     * temp_id : 1
     * list_dt : 2019-12-26 16:55:32
     * insert_dt : 2019-12-26 16:55:48
     * followup_temp_detail_list : [{"id":"1","temp_id":"1","list_id":"1","type":"1","content":"请于2019年12月26日到贵医复诊","sub_content":"","insert_dt":"2019-12-27 15:25:32"},{"id":"2","temp_id":"1","list_id":"1","type":"2","content":"请遵医嘱按时按量服药","sub_content":"","insert_dt":"2019-12-27 15:25:32"},{"id":"3","temp_id":"1","list_id":"1","type":"3","content":"请上传近期就诊后病历资料","sub_content":"","insert_dt":"2019-12-27 15:25:32"}]
     */
    //随访计划模板节点id
    @JsonField("list_id")
    private int listId;
    //随访计划模板id
    @JsonField("temp_id")
    private int tempId;
    //随访计划时间
    @JsonField("list_dt")
    private String listDt;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;
    //提醒状态 0：未提醒 1：已提醒
    @JsonField("tip_state")
    private int tipState;
    @JsonField("followup_temp_detail_list")
    private List<DCDutyVisitPlantTempDetailItem> followupTempDetailList;
    @JsonField("temp_name")
    private String tempName;
    private int itemType;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public String getListDt() {
        return listDt;
    }

    public void setListDt(String listDt) {
        this.listDt = listDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getTipState() {
        return tipState;
    }

    public void setTipState(int tipState) {
        this.tipState = tipState;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public List<DCDutyVisitPlantTempDetailItem> getFollowupTempDetailList() {
        return followupTempDetailList;
    }

    public void setFollowupTempDetailList(List<DCDutyVisitPlantTempDetailItem> followupTempDetailList) {
        this.followupTempDetailList = followupTempDetailList;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public String toString() {
        return "DCDutyVisitPlantTempItem{" +
                "listId=" + listId +
                ", tempId=" + tempId +
                ", listDt='" + listDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", tipState=" + tipState +
                ", followupTempDetailList=" + followupTempDetailList +
                ", tempName='" + tempName + '\'' +
                ", itemType=" + itemType +
                '}';
    }
}
