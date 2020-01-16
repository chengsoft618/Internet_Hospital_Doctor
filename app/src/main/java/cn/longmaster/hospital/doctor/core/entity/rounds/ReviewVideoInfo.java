package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/2/19.
 */

public class ReviewVideoInfo implements Serializable {
    @JsonField("label_type")
    private int labelType;
    @JsonField("file_path")
    private String filePath;
    @JsonField("begin_dt")
    private String beginDt;
    @JsonField("end_dt")
    private String endDt;
    @JsonField("insert_dt")
    private String insertDt;

    public int getLabelType() {
        return labelType;
    }

    public void setLabelType(int labelType) {
        this.labelType = labelType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBeginDt() {
        return beginDt;
    }

    public void setBeginDt(String beginDt) {
        this.beginDt = beginDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "ReviewVideoInfo{" +
                "labelType=" + labelType +
                ", filePath='" + filePath + '\'' +
                ", beginDt='" + beginDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
