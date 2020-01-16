package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 医生财务统计
 * Created by JinKe on 2016-07-28.
 */
public class FinanceStatisticInfo implements Serializable {
    @JsonField("currency_value")
    private float currencyValue;//用户总余额（可用+冻结）
    @JsonField("cureValue")
    private float cureValue;//就诊收入
    @JsonField("otherValue")
    private float otherValue;//其他收入
    @JsonField("availaValue")
    private float availaValue;//可用金额
    @JsonField("freezeValue")
    private float freezeValue;//冻结金额
    @JsonField("punish")
    private float punish;//扣款金额
    @JsonField("income")
    private float income;//收入总金额

    public float getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(float currencyValue) {
        this.currencyValue = currencyValue;
    }

    public float getCureValue() {
        return cureValue;
    }

    public void setCureValue(float cureValue) {
        this.cureValue = cureValue;
    }

    public float getOtherValue() {
        return otherValue;
    }

    public void setOtherValue(float otherValue) {
        this.otherValue = otherValue;
    }

    public float getAvailaValue() {
        return availaValue;
    }

    public void setAvailaValue(float availaValue) {
        this.availaValue = availaValue;
    }

    public float getFreezeValue() {
        return freezeValue;
    }

    public void setFreezeValue(float freezeValue) {
        this.freezeValue = freezeValue;
    }

    public float getPunish() {
        return punish;
    }

    public void setPunish(float punish) {
        this.punish = punish;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "FinanceStatisticInfo{" +
                "currencyValue=" + currencyValue +
                ", cureValue=" + cureValue +
                ", otherValue=" + otherValue +
                ", availaValue=" + availaValue +
                ", freezeValue=" + freezeValue +
                ", punish=" + punish +
                ", income=" + income +
                '}';
    }
}
