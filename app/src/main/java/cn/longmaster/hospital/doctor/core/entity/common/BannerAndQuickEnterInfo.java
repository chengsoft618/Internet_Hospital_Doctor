package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 客户端Banner与快捷入口配置
 * Created by JinKe on 2016-07-26.
 */
public class BannerAndQuickEnterInfo implements Serializable {
    @JsonField("picture_id")
    private int pictureId;//banner编号(ID)
    @JsonField("sort_id")
    private int sortId;//排序
    @JsonField("picture_name")
    private String pictureName;//banner标题
    @JsonField("picture_path")
    private String picturePath;//banner图片
    @JsonField("link_type")
    private int linkType;//跳转类型 0:不跳转1:跳转到网页2:跳转到医生详情
    @JsonField("link_address")
    private String linkAddress;//跳转地址
    @JsonField("picture_width")
    private int pictureWidth;//图片宽度
    @JsonField("picture_length")
    private int pictureLength;//图片高度
    @JsonField("banner_type")
    private int bannerType;//banner类型 0:患者端App banner 1:医生端App banner 2:医生app快捷入口
    @JsonField("insert_dt")
    private String insert_dt;//插入时间

    public String getInsert_dt() {
        return insert_dt;
    }

    public void setInsert_dt(String insert_dt) {
        this.insert_dt = insert_dt;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public int getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(int pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public int getPictureLength() {
        return pictureLength;
    }

    public void setPictureLength(int pictureLength) {
        this.pictureLength = pictureLength;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    @Override
    public String toString() {
        return "BannerAndQuickEnterInfo{" +
                "pictureId=" + pictureId +
                ", sortId=" + sortId +
                ", pictureName='" + pictureName + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", linkType=" + linkType +
                ", linkAddress='" + linkAddress + '\'' +
                ", pictureWidth=" + pictureWidth +
                ", pictureLength=" + pictureLength +
                ", bannerType=" + bannerType +
                ", insert_dt='" + insert_dt + '\'' +
                '}';
    }
}
