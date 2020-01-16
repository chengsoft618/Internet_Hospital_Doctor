package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

/**
 * @author: wangyang
 * @date: 2020-01-04 16:24
 * @description: 随访计划已提醒详情
 */
public class DCDutyVisitPlanIsRemindDetailsItemData {
    private int listId;
    private String time;
    private String content;

    public DCDutyVisitPlanIsRemindDetailsItemData(int listId, String time, String content) {
        this.time = time;
        this.content = content;
        this.listId = listId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
