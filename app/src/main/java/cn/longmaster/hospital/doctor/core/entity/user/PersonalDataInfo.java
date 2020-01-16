package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/7/31.
 */

public class PersonalDataInfo implements Serializable {
    @JsonField("material_id")
    private int materialId;//
    @JsonField("user_id")
    private int userId;//
    @JsonField("type")
    private int type;
    @JsonField("ppj_result")
    private String ppjResult;
    @JsonField("material_name")
    private String materialName;//
    @JsonField("content_url")
    private String content;//
    // 0 全部可见 ，1 仅自己可见
    @JsonField("is_display")
    private int isDisplay;//
    @JsonField("ppj_uptdt")
    private String ppjUptdt;//
    @JsonField("release_dt")
    private String releaseDt;
    @JsonField("insert_dt")
    private String insertDt;
    // 0 全部可见 ，1 仅自己可见
    @JsonField("self_visible")
    private int selfVisible;

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPpjResult() {
        return ppjResult;
    }

    public void setPpjResult(String ppjResult) {
        this.ppjResult = ppjResult;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getPpjUptdt() {
        return ppjUptdt;
    }

    public void setPpjUptdt(String ppjUptdt) {
        this.ppjUptdt = ppjUptdt;
    }

    public String getReleaseDt() {
        return releaseDt;
    }

    public void setReleaseDt(String releaseDt) {
        this.releaseDt = releaseDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public int getSelfVisible() {
        return selfVisible;
    }

    public boolean isSelfVisible() {
        return selfVisible == 0;
    }

    public void setSelfVisible(int selfVisible) {
        this.selfVisible = selfVisible;
    }

    @Override
    public String toString() {
        return "PersonalDataInfo{" +
                "materialId=" + materialId +
                ", userId=" + userId +
                ", type=" + type +
                ", ppjResult='" + ppjResult + '\'' +
                ", materialName='" + materialName + '\'' +
                ", content='" + content + '\'' +
                ", isDisplay=" + isDisplay +
                ", ppjUptdt='" + ppjUptdt + '\'' +
                ", releaseDt='" + releaseDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
