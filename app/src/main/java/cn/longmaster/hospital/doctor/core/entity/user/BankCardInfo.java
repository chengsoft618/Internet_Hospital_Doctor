package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 用户银行卡信息
 * Created by Yang² on 2016/7/27.
 */
public class BankCardInfo implements Serializable {
    @JsonField("card_num")
    private String cardNum;//银行卡号
    @JsonField("user_id")
    private int userId;//用户id
    @JsonField("real_name")
    private String realName;//用户真实名字
    @JsonField("id_card")
    private String idCard;//身份证号
    @JsonField("bank_name")
    private String bankName;//开户银行
    @JsonField("phone_num")
    private String phoneNum;//电话号码
    @JsonField("is_default")
    private int isDefault;//是否默认账户0：正常1：使用2：逻辑删除
    @JsonField("is_bind")
    private int isBind;//是否绑定
    @JsonField("pay_type")
    private int payType;//支付类型 1：银行卡2：支付宝3：微信
    @JsonField("insert_dt")
    private String insertDt;//插入时间

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "BankCardInfo{" +
                "cardNum='" + cardNum + '\'' +
                ", userId=" + userId +
                ", realName='" + realName + '\'' +
                ", idCard='" + idCard + '\'' +
                ", bankName='" + bankName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", isDefault=" + isDefault +
                ", isBind=" + isBind +
                ", payType=" + payType +
                ", insertDt='" + insertDt + '\'' +
                '}';
    }
}
