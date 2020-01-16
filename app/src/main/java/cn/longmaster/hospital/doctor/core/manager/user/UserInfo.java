package cn.longmaster.hospital.doctor.core.manager.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 用户信息
 * Created by yangyong on 16/7/26.
 */
public class UserInfo implements Serializable {
    /**
     * 为0时代表该账号不是当前使用的账号
     */
    public static final int UNUSED = 0;
    /**
     * 为1时代表是当前使用账号
     */
    public static final int INUSE = 1;
    /**
     * 账号未激活
     */
    public static final int NOT_ACTIVATED = 0;
    /**
     * 账号已激活
     */
    public static final int HAS_ACTIVATED = 1;

    @JsonField("userID")
    private int userId;//用户ID
    @JsonField("pesAddr")
    private String pesAddr = "";//对应的pes的地址
    @JsonField("pesIP")
    private long pesIp;//pes的ip
    @JsonField("pesPort")
    private int pesPort;//pes的端口
    @JsonField("loginAuthKey")
    private String loginAuthKey = "";//登陆鉴权Key
    @JsonField("isDoctor")
    private int isDoctor = 1;//用户身份信息：0用户，1医生，2销售代表，3医生助理

    private int accountType;//帐户类型
    private String userName = "";//昵称
    private String phoneNum = "";//手机号码
    private long lastLoginDt;//最后一次登录时间
    public int isUsing = UNUSED;
    public int isActivity = NOT_ACTIVATED;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPesAddr() {
        return pesAddr;
    }

    public void setPesAddr(String pesAddr) {
        this.pesAddr = pesAddr;
    }

    public long getPesIp() {
        return pesIp;
    }

    public void setPesIp(long pesIp) {
        this.pesIp = pesIp;
    }

    public int getPesPort() {
        return pesPort;
    }

    public void setPesPort(int pesPort) {
        this.pesPort = pesPort;
    }

    public void setLoginAuthKey(String loginAuthKey) {
        this.loginAuthKey = loginAuthKey;
    }

    public String getLoginAuthKey() {
        return loginAuthKey;
    }

    public int getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(int isDoctor) {
        this.isDoctor = isDoctor;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public long getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(long lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public int getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(int isUsing) {
        this.isUsing = isUsing;
    }

    public int getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(int isActivity) {
        this.isActivity = isActivity;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", pesAddr='" + pesAddr + '\'' +
                ", pesIp=" + pesIp +
                ", pesPort=" + pesPort +
                ", loginAuthKey='" + loginAuthKey + '\'' +
                ", isDoctor=" + isDoctor +
                ", accountType=" + accountType +
                ", userName='" + userName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", lastLoginDt=" + lastLoginDt +
                ", isUsing=" + isUsing +
                ", isActivity=" + isActivity +
                '}';
    }
}
