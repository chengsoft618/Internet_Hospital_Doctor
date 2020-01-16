package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 下发报告相关信息
 * Created by JinKe on 2016-09-07.
 */
public class AppDialogInfo implements Serializable {
    @JsonField("complex_suggest")
    private String complexSuggest;//二诊建议
    @JsonField("guide_report_dt")
    private String guideReportDt;//下发报告时间

    public String getComplexSuggest() {
        return complexSuggest;
    }

    public void setComplexSuggest(String complexSuggest) {
        this.complexSuggest = complexSuggest;
    }

    public String getGuideReportDt() {
        return guideReportDt;
    }

    public void setGuideReportDt(String guideReportDt) {
        this.guideReportDt = guideReportDt;
    }

    @Override
    public String toString() {
        return "AppDialogInfo{" +
                "complexSuggest=" + complexSuggest +
                ", guideReportDt=" + guideReportDt +
                '}';
    }
}

