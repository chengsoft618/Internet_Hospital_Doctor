package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 支付确认单
 * Created by Yang² on 2017/12/28.
 */

public class PaymentInfo implements Serializable {
    @JsonField("scheduing_id")
    private int scheduingId;//排班ID
    @JsonField("payment_type")
    private int paymentType;//方式    1：病例、2：时长
    @JsonField("begin_dt")
    private String beginDt;//开始时间
    @JsonField("end_dt")
    private String endDt;//结束时间
    @JsonField("dt_length")
    private String dtLength;//时长
    @JsonField("pay_value")
    private String payValue;//金额
    @JsonField("insert_dt")
    private String insertDt;//编辑时间
    @JsonField("pic_head")
    private String picHead;//图片地址URL头部
    @JsonField("pic_list")
    private List<PaymentPic> paymentPicList;

    public int getScheduingId() {
        return scheduingId;
    }

    public void setScheduingId(int scheduingId) {
        this.scheduingId = scheduingId;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
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

    public String getDtLength() {
        return dtLength;
    }

    public void setDtLength(String dtLength) {
        this.dtLength = dtLength;
    }

    public String getPayValue() {
        return payValue;
    }

    public void setPayValue(String payValue) {
        this.payValue = payValue;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    public String getPicHead() {
        return picHead;
    }

    public void setPicHead(String picHead) {
        this.picHead = picHead;
    }

    public List<PaymentPic> getPaymentPicList() {
        return paymentPicList;
    }

    public void setPaymentPicList(List<PaymentPic> paymentPicList) {
        this.paymentPicList = paymentPicList;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "scheduingId=" + scheduingId +
                ", paymentType=" + paymentType +
                ", beginDt='" + beginDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", dtLength='" + dtLength + '\'' +
                ", payValue='" + payValue + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", picHead='" + picHead + '\'' +
                ", paymentPicList=" + paymentPicList +
                '}';
    }
}
