package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医生详情推荐理由列表
 * Created by Yang² on 2017/8/24.
 */

public class RecmdInfo implements Serializable {
    @JsonField("id")
    private int id;
    @JsonField("doctor_id")
    private int doctorId;
    @JsonField("data_type")
    private int dataType;
    @JsonField("content")
    private String content;
    @JsonField("url_link")
    private String urlLink;
    @JsonField("insert_dt")
    private String insertDt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "RecmdInfo{" +
                "id=" + id +
                ", doctorId=" + doctorId +
                ", dataType=" + dataType +
                ", content='" + content + '\'' +
                ", urlLink='" + urlLink + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
