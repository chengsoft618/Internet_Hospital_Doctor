package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医生职称等级配置
 * Created by Yang² on 2016/7/26.
 */
public class TitleGradeInfo implements Serializable {
    @JsonField("first_grade_id")
    private int firstGradeId;//职称等级ID
    @JsonField("first_grade_name")
    private String firstGradeName;//职称等级名称
    @JsonField("use_state")
    private int useState;//可用状态，0可用，1不可
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getFirstGradeId() {
        return firstGradeId;
    }

    public void setFirstGradeId(int firstGradeId) {
        this.firstGradeId = firstGradeId;
    }

    public String getFirstGradeName() {
        return firstGradeName;
    }

    public void setFirstGradeName(String firstGradeName) {
        this.firstGradeName = firstGradeName;
    }

    public int getUseState() {
        return useState;
    }

    public void setUseState(int useState) {
        this.useState = useState;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "TitleGradeInfo{" +
                "firstGradeId=" + firstGradeId +
                ", firstGradeName='" + firstGradeName + '\'' +
                ", useState=" + useState +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
