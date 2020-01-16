package cn.longmaster.hospital.doctor.core.entity.college;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/3/27.
 */

public class GuideDataInfo implements Serializable {
    @JsonField("is_finish")
    private int isFinish;
    @JsonField("material_id")
    private int materialId;
    @JsonField("user_id")
    private int userId;
    @JsonField("author")
    private String author;
    @JsonField("material_name")
    private String materialName;
    @JsonField("material_pic")
    private String materialPic;
    @JsonField("type")
    private int type;
    @JsonField("content")
    private String content;
    @JsonField("content_url")
    private String contentUrl;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialPic() {
        return materialPic;
    }

    public void setMaterialPic(String materialPic) {
        this.materialPic = materialPic;
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

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return "GuideDataInfo{" +
                "materialId=" + materialId +
                ", userId=" + userId +
                ", author='" + author + '\'' +
                ", materialName='" + materialName + '\'' +
                ", materialPic='" + materialPic + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
