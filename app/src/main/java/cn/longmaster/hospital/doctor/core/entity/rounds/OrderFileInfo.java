package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/15.
 */

public class OrderFileInfo implements Serializable {
    @JsonField("id")
    private int id;//
    @JsonField("order_id")
    private int orderId;//
    @JsonField("type")
    private int type;//
    @JsonField("file_name")
    private String fileName;//
    @JsonField("remark")
    private String remark;//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderFileInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", type=" + type +
                ", fileName=" + fileName +
                ", remark=" + remark +
                '}';
    }
}
