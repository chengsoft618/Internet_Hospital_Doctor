package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 根据医生ID获取关联的价格档位
 * Created by Yang² on 2016/7/27.
 */
public class DoctorGradePriceInfo implements Serializable, Comparable<DoctorGradePriceInfo> {
    @JsonField("first_grade_id")
    private int firstGradeId;//医生职称等级ID
    @JsonField("second_grade_id")
    private int secondGradeId;//医生区域等级ID
    @JsonField("grade_price_id")
    private int gradePriceId;//档位ID
    @JsonField("grade_price")
    private float gradePrice;//档位价格
    @JsonField("service_type")
    private int serviceType;//服务类型
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getFirstGradeId() {
        return firstGradeId;
    }

    public void setFirstGradeId(int firstGradeId) {
        this.firstGradeId = firstGradeId;
    }

    public int getSecondGradeId() {
        return secondGradeId;
    }

    public void setSecondGradeId(int secondGradeId) {
        this.secondGradeId = secondGradeId;
    }

    public int getGradePriceId() {
        return gradePriceId;
    }

    public void setGradePriceId(int gradePriceId) {
        this.gradePriceId = gradePriceId;
    }

    public float getGradePrice() {
        return gradePrice;
    }

    public void setGradePrice(float gradePrice) {
        this.gradePrice = gradePrice;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "DoctorGradePriceInfo{" +
                "firstGradeId=" + firstGradeId +
                ", secondGradeId=" + secondGradeId +
                ", gradePriceId=" + gradePriceId +
                ", gradePrice=" + gradePrice +
                ", serviceType=" + serviceType +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }


    @Override
    public int compareTo(DoctorGradePriceInfo another) {
        return (int) (this.getGradePrice() - another.getGradePrice());
    }
}
