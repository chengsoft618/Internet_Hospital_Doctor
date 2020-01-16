package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 15:29
 * @description:
 */
public class DCProjectDetailInfo {
    @JsonField("item_id")
    private int itemId;
    @JsonField("item_name")
    private String itemName;
    @JsonField("item_desc")
    private String itemDesc;
    @JsonField("item_no")
    private String itemNo;
    @JsonField("duty_state")
    private int dutyState;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getDutyState() {
        return dutyState;
    }

    public void setDutyState(int dutyState) {
        this.dutyState = dutyState;
    }

    @Override
    public String toString() {
        return "DCProjectDetailInfo{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemNo='" + itemNo + '\'' +
                ", dutyState=" + dutyState +
                '}';
    }
}
