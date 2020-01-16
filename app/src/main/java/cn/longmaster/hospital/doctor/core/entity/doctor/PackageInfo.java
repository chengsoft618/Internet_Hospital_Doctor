package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 套餐信息配置
 * Created by Yang² on 2016/7/26.
 */
public class PackageInfo implements Serializable {
    @JsonField("package_id")
    private int packageId;//套餐ID
    @JsonField("package_name")
    private String packageName;//套餐名称
    @JsonField("package_desc")
    private String packageDesc;//套餐描述
    @JsonField("package_price")
    private String packagePrice;//套餐价格
    @JsonField("package_label")
    private String packageLabel;//套餐标签
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDesc() {
        return packageDesc;
    }

    public void setPackageDesc(String packageDesc) {
        this.packageDesc = packageDesc;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageLabel() {
        return packageLabel;
    }

    public void setPackageLabel(String packageLabel) {
        this.packageLabel = packageLabel;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "PackageInfo{" +
                "packageId=" + packageId +
                ", packageName='" + packageName + '\'' +
                ", packageDesc='" + packageDesc + '\'' +
                ", packagePrice='" + packagePrice + '\'' +
                ", packageLabel='" + packageLabel + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
