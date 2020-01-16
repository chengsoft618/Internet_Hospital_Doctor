package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医生区域等级配置
 * Created by Yang² on 2016/7/26.
 */
public class AreaGradeInfo implements Serializable {
    @JsonField("second_grade_id")
    private int secondGradeId;//区域等级ID
    @JsonField("second_grade_name")
    private String secondGradeName;//区域等级名称
    @JsonField("use_state")
    private int useState;//是否可用，0可以，1否
    @JsonField("area_grade_id")
    private int areaGradeId;//暂时无用字段
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getSecondGradeId() {
        return secondGradeId;
    }

    public void setSecondGradeId(int secondGradeId) {
        this.secondGradeId = secondGradeId;
    }

    public String getSecondGradeName() {
        return secondGradeName;
    }

    public void setSecondGradeName(String secondGradeName) {
        this.secondGradeName = secondGradeName;
    }

    public int getUseState() {
        return useState;
    }

    public void setUseState(int useState) {
        this.useState = useState;
    }

    public int getAreaGradeId() {
        return areaGradeId;
    }

    public void setAreaGradeId(int areaGradeId) {
        this.areaGradeId = areaGradeId;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "AreaGradeInfo{" +
                "secondGradeId=" + secondGradeId +
                ", secondGradeName='" + secondGradeName + '\'' +
                ", useState=" + useState +
                ", areaGradeId=" + areaGradeId +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
