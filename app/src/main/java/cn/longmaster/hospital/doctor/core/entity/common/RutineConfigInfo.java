package cn.longmaster.hospital.doctor.core.entity.common;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 常规配置
 * Created by JinKe on 2016-07-26.
 */
public class RutineConfigInfo implements Serializable {
    @JsonField("cfg_id")
    private int cfgId;//配置ID
    @JsonField("pay_over_time")
    private int payOverTime;//支付超时时间，分钟
    @JsonField("is_enable")
    private int isEnable;//是否启用，0否，1是
    @JsonField("send_code_time")
    private int sendCodeTime;//发送验证码间隔时间，秒
    @JsonField("report_share_link")
    private String reportShareLink;//报告分享链接
    @JsonField("app_share_link")
    private String appShareLink;//客户端分享链接
    @JsonField("appstat_wait_tip")
    private String appstatWaitTip;//等待就诊文字提示
    @JsonField("appstat_continue_tip")
    private String appstatContinueTip;//继续就诊文字提示
    @JsonField("refund_notice")
    private String refundNotice;//退款文字说明
    @JsonField("change_notice")
    private String changeNotice;//改签文字说明
    @JsonField("scheduling_display")
    private int schedulingDisplay;//默认排班显示时间段，小时
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public int getCfgId() {
        return cfgId;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    public int getPayOverTime() {
        return payOverTime;
    }

    public void setPayOverTime(int payOverTime) {
        this.payOverTime = payOverTime;
    }

    public int getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(int isEnable) {
        this.isEnable = isEnable;
    }

    public int getSendCodeTime() {
        return sendCodeTime;
    }

    public void setSendCodeTime(int sendCodeTime) {
        this.sendCodeTime = sendCodeTime;
    }

    public String getReportShareLink() {
        return reportShareLink;
    }

    public void setReportShareLink(String reportShareLink) {
        this.reportShareLink = reportShareLink;
    }

    public String getAppShareLink() {
        return appShareLink;
    }

    public void setAppShareLink(String appShareLink) {
        this.appShareLink = appShareLink;
    }

    public String getAppstatWaitTip() {
        return appstatWaitTip;
    }

    public void setAppstatWaitTip(String appstatWaitTip) {
        this.appstatWaitTip = appstatWaitTip;
    }

    public String getAppstatContinueTip() {
        return appstatContinueTip;
    }

    public void setAppstatContinueTip(String appstatContinueTip) {
        this.appstatContinueTip = appstatContinueTip;
    }

    public String getRefundNotice() {
        return refundNotice;
    }

    public void setRefundNotice(String refundNotice) {
        this.refundNotice = refundNotice;
    }

    public String getChangeNotice() {
        return changeNotice;
    }

    public void setChangeNotice(String changeNotice) {
        this.changeNotice = changeNotice;
    }

    public int getSchedulingDisplay() {
        return schedulingDisplay;
    }

    public void setSchedulingDisplay(int schedulingDisplay) {
        this.schedulingDisplay = schedulingDisplay;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "RutineConfigInfo{" +
                "cfgId=" + cfgId +
                ", payOverTime='" + payOverTime + '\'' +
                ", isEnable=" + isEnable +
                ", sendCodeTime='" + sendCodeTime + '\'' +
                ", reportShareLink='" + reportShareLink + '\'' +
                ", appShareLink='" + appShareLink + '\'' +
                ", appstatWaitTip='" + appstatWaitTip + '\'' +
                ", appstatContinueTip='" + appstatContinueTip + '\'' +
                ", refundNotice='" + refundNotice + '\'' +
                ", changeNotice='" + changeNotice + '\'' +
                ", schedulingDisplay='" + schedulingDisplay + '\'' +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}