package cn.longmaster.hospital.doctor.core.entity.department;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 科室信息
 * Created by Yang² on 2016/7/26.
 */
public class DepartmentInfo implements Serializable {
    @JsonField("department_id")
    private int departmentId;//科室ID
    @JsonField("parent_id")
    private int parentId;//父科室ID
    @JsonField("department_name")
    private String departmentName;//科室名称
    @JsonField("english_name")
    private String englishName;//科室英文名称
    @JsonField("background_color")
    private String backgroundColor;//科室背景颜色
    @JsonField("short_desc")
    private String shortDesc;//科室描述
    @JsonField("order_id")
    private int orderId;//排序编号
    @JsonField("is_recommend")
    private int isRecommend;//是否推荐 0:不推荐 1:推荐
    @JsonField("department_type")
    private int departmentType;////科室类型 0:非重点 1:重点
    @JsonField("is_display")
    private int isDisplay;//是否显示 0:不显示 1:显示
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    @JsonField("picture_list")
    private List<DepartmentPictureInfo> pictureList;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public int getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(int departmentType) {
        this.departmentType = departmentType;
    }

    public int getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(int isDisplay) {
        this.isDisplay = isDisplay;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public List<DepartmentPictureInfo> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<DepartmentPictureInfo> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public String toString() {
        return "DepartmentInfo{" +
                "departmentId=" + departmentId +
                ", parentId=" + parentId +
                ", departmentName='" + departmentName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", shortDesc='" + shortDesc + '\'' +
                ", orderId=" + orderId +
                ", isRecommend=" + isRecommend +
                ", departmentType=" + departmentType +
                ", isDisplay=" + isDisplay +
                ", insertDt='" + insertDt + '\'' +
                ", pictureList=" + pictureList +
                '}';
    }
}
