package cn.longmaster.hospital.doctor.core.entity.department;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 科室信息图片列表
 * Created by Yang² on 2016/7/26.
 */
public class DepartmentPictureInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;//科室ID
    @JsonField("picture_name")
    private String pictureName;//科室扩展信息
    @JsonField("picture_type")
    private int pictureType;//扩展类型 1:客户端科室标题图片2:客户端科室列表图标3:客户端科室头衔图标4:官网首页科室图片5:官网科室头衔图标 6:科室宣传语(多个之间用|隔开)
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
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

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DepartmentPictureInfo{" +
                "departmentId=" + departmentId +
                ", pictureName='" + pictureName + '\'' +
                ", pictureType='" + pictureType + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
