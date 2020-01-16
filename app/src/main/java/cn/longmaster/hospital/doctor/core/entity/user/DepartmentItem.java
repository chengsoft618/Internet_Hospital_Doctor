package cn.longmaster.hospital.doctor.core.entity.user;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 18:21
 * @description: 科室
 */
public class DepartmentItem {

    /**
     * hospital_id : 58
     * hosdep_id : 59
     * parent_hosdep_id : 0
     * introduction :
     * amt_value : 0.00
     * is_recom : 0
     * is_important : 0
     * insert_dt : 2018-04-14 13:44:19
     * hosdep_name : 外科
     */

    @JsonField("hospital_id")
    private String hospitalId;
    @JsonField("hosdep_id")
    private String hosdepId;
    @JsonField("parent_hosdep_id")
    private String parentHosdepId;
    @JsonField("introduction")
    private String introduction;
    @JsonField("amt_value")
    private String amtValue;
    @JsonField("is_recom")
    private String isRecom;
    @JsonField("is_important")
    private String isImportant;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("hosdep_name")
    private String hosdepName;

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHosdepId() {
        return hosdepId;
    }

    public void setHosdepId(String hosdepId) {
        this.hosdepId = hosdepId;
    }

    public String getParentHosdepId() {
        return parentHosdepId;
    }

    public void setParentHosdepId(String parentHosdepId) {
        this.parentHosdepId = parentHosdepId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAmtValue() {
        return amtValue;
    }

    public void setAmtValue(String amtValue) {
        this.amtValue = amtValue;
    }

    public String getIsRecom() {
        return isRecom;
    }

    public void setIsRecom(String isRecom) {
        this.isRecom = isRecom;
    }

    public String getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(String isImportant) {
        this.isImportant = isImportant;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getHosdepName() {
        return hosdepName;
    }

    public void setHosdepName(String hosdepName) {
        this.hosdepName = hosdepName;
    }
}
