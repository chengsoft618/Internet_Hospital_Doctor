package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;
import cn.longmaster.utils.StringUtils;

/**
 * //医生信息扩展字段
 * Created by JinKe on 2016-07-26.
 */
public class DoctorBriefInfo implements Serializable {
    @JsonField("introduce")
    private String introduce;//医生简介
    @JsonField("research")
    private String research;//研究方向
    @JsonField("work_exper")
    private String workExper;//工作经历
    @JsonField("academic")
    private String academic;//学术兼职
    @JsonField("awards")
    private String awards;//获奖情况

    public String getIntroduce() {
        if (StringUtils.isTrimEmpty(introduce)) {
            return "该医生暂未上传资料";
        }
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }

    public String getWorkExper() {
        return workExper;
    }

    public void setWorkExper(String workExper) {
        this.workExper = workExper;
    }

    public String getAcademic() {
        return academic;
    }

    public void setAcademic(String academic) {
        this.academic = academic;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    @Override
    public String toString() {
        return "DoctorBriefInfo{" +
                "introduce='" + introduce + '\'' +
                ", research='" + research + '\'' +
                ", workExper='" + workExper + '\'' +
                ", academic='" + academic + '\'' +
                ", awards='" + awards + '\'' +
                '}';
    }
}
