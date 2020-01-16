package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医生图片信息列表
 * Created by JinKe on 2016-07-26.
 */
public class DoctorPictureInfo implements Serializable, Comparable<DoctorPictureInfo> {
    @JsonField("picture_id")
    private int pictureId;//图片ID
    @JsonField("phone_num")
    private String phoneNum;//入驻联系电话
    @JsonField("data_id")
    private int dataId;//data_type=1:医生ID data_type=2:团队ID
    @JsonField("data_type")
    private int dataType;//1:医生2:团队
    @JsonField("picture_name")
    private String pictureName;//图片名称
    @JsonField("picture_type")
    private int pictureType;//图片类型 1:身份证 2:执业医师资格证 3:医生资格证 4:简历 5:头像 6:风采照7.医生签名
    @JsonField("audit_state")
    private int audit_state;//图片状态
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getPictureId() {
        return pictureId;
    }

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public int getAudit_state() {
        return audit_state;
    }

    public void setAudit_state(int audit_state) {
        this.audit_state = audit_state;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DoctorPictureInfo{" +
                "pictureId=" + pictureId +
                ", phoneNum='" + phoneNum + '\'' +
                ", dataId=" + dataId +
                ", dataType=" + dataType +
                ", pictureName='" + pictureName + '\'' +
                ", pictureType=" + pictureType +
                ", audit_state=" + audit_state +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }

    @Override
    public int compareTo(DoctorPictureInfo another) {
        return another.getInsertDt().compareTo(this.insertDt);
    }
}
