package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author ABiao_Abiao
 * @date 2019/6/10 10:36
 * @description: 专家团队item
 */
public class DoctorTeamItemInfo implements Serializable {
    @JsonField("team_id")
    private String teamId;
    @JsonField("team_name")
    private String teamName;
    @JsonField("team_intro")
    private String teamIntro;
    @JsonField("insert_dt")
    private String insertDt;
    @JsonField("member_count")
    private String memberCount;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamIntro() {
        return teamIntro;
    }

    public void setTeamIntro(String teamIntro) {
        this.teamIntro = teamIntro;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }
}
