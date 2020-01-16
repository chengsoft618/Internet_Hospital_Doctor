package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/12/30 15:08
 * @description:
 */
public class DCDutyVisitPlantTempDetailItem {
    /**
     * id : 1
     * temp_id : 1
     * list_id : 1
     * type : 1
     * content : 请于2019年12月26日到贵医复诊
     * sub_content :
     * insert_dt : 2019-12-27 15:25:32
     */
    //随访计划详情id
    @JsonField("id")
    private int id;
    //随访计划模板id
    @JsonField("temp_id")
    private int tempId;
    //随访计划模板节点id
    @JsonField("list_id")
    private int listId;
    //随访计划类型 1：复诊提醒 2、用药提醒 3、上传资料提醒
    @JsonField("type")
    private int type;
    //随访计划显示文字
    @JsonField("content")
    private String content;
    @JsonField("sub_content")
    private String subContent;
    //插入时间
    @JsonField("insert_dt")
    private String insertDt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTempId() {
        return tempId;
    }

    public void setTempId(int tempId) {
        this.tempId = tempId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubContent() {
        return subContent;
    }

    public void setSubContent(String subContent) {
        this.subContent = subContent;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DCDutyVisitPlantTempDetailItem{" +
                "id=" + id +
                ", tempId=" + tempId +
                ", listId=" + listId +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", subContent='" + subContent + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
