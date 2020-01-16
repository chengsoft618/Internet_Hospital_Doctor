package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 民族信息配置
 * Created by JinKe on 2016-07-26.
 */
public class NationConfigInfo implements Serializable {
    @JsonField("nation_id")
    private int nationId;//民族ID
    @JsonField("nation_desc")
    private String nationDesc;//民族名称
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public String getNationDesc() {
        return nationDesc;
    }

    public void setNationDesc(String nationDesc) {
        this.nationDesc = nationDesc;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "NationConfigInfo{" +
                "nationId=" + nationId +
                ", nationDesc='" + nationDesc + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
