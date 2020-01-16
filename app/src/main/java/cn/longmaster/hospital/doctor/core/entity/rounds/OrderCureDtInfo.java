package cn.longmaster.hospital.doctor.core.entity.rounds;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2018/5/15.
 */

public class OrderCureDtInfo implements Serializable {
    @JsonField("id")
    private int id;//
    @JsonField("order_id")
    private int orderId;//
    @JsonField("cure_dt")
    private String cureDt;//接诊时间
    @JsonField("is_use")
    private int isUse;//是否使用

    private boolean select;

    public boolean getSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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

    public String getCureDt() {
        return cureDt;
    }

    public void setCureDt(String cureDt) {
        this.cureDt = cureDt;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    @Override
    public String toString() {
        return "OrderCureDtInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", cureDt='" + cureDt + '\'' +
                ", isUse=" + isUse +
                '}';
    }
}
