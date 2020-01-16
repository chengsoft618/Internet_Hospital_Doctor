package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 支付确认单图片
 * Created by Yang² on 2017/12/28.
 */

public class PaymentPic implements Serializable {
    @JsonField("scheduing_id")
    private int scheduingId;//排班ID
    @JsonField("file_name")
    private String fileName;//图片地址
    @JsonField("insert_dt")
    private String insertDt;

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "PaymentPic{" +
                "scheduingId=" + scheduingId +
                ", fileName='" + fileName + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
