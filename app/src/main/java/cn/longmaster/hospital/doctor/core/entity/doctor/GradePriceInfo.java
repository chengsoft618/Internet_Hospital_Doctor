package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 价格档位配置
 * Created by Yang² on 2016/7/26.
 */
public class GradePriceInfo implements Serializable {
    @JsonField("grade_price_id")
    private int gradePriceId;//价格档位ID
    @JsonField("grade_price_name")
    private String gradePriceName;//价格档位名称
    @JsonField("grade_price")
    private float gradePrice;//金额
    @JsonField("service_id")
    private int serviceId;//套餐ID
    @JsonField("is_points")
    private int isPoints;//暂时无用字段
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getGradePriceId() {
        return gradePriceId;
    }

    public void setGradePriceId(int gradePriceId) {
        this.gradePriceId = gradePriceId;
    }

    public String getGradePriceName() {
        return gradePriceName;
    }

    public void setGradePriceName(String gradePriceName) {
        this.gradePriceName = gradePriceName;
    }

    public float getGradePrice() {
        return gradePrice;
    }

    public void setGradePrice(float gradePrice) {
        this.gradePrice = gradePrice;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getIsPoints() {
        return isPoints;
    }

    public void setIsPoints(int isPoints) {
        this.isPoints = isPoints;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "GradePriceInfo{" +
                "gradePriceId=" + gradePriceId +
                ", gradePriceName='" + gradePriceName + '\'' +
                ", gradePrice=" + gradePrice +
                ", serviceId=" + serviceId +
                ", isPoints=" + isPoints +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
