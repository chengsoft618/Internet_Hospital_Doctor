package cn.longmaster.hospital.doctor.core.entity.common;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by lm-pc on 16/7/28.
 */
public class UserResultInfo {
    @JsonField("userID")
    private int userID;//用户ID
    @JsonField("pesAddr")
    private String pesAddr;//对应的pes的地址
    @JsonField("pesIP")
    private long pesIP;//pes的ip
    @JsonField("pesPort")
    private int pesPort;//pes的端口
    @JsonField("loginAuthKey")
    private String loginAuthKey;//登陆鉴权Key
    @JsonField("isDoctor")
    private int isDoctor;//用户身份信息：0用户，1医生，2销售代表，3医生助理
    @JsonField("user_id")
    private int userid;//用户ID
    @JsonField("newpwd")
    private String newPwd;//要修改的新密码
    @JsonField("bindInfo")
    private String bindInfo;//该账号绑定的信息
    @JsonField("url")
    private String url;//域名

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPesAddr() {
        return pesAddr;
    }

    public void setPesAddr(String pesAddr) {
        this.pesAddr = pesAddr;
    }

    public long getPesIP() {
        return pesIP;
    }

    public void setPesIP(long pesIP) {
        this.pesIP = pesIP;
    }

    public int getPesPort() {
        return pesPort;
    }

    public void setPesPort(int pesPort) {
        this.pesPort = pesPort;
    }

    public String getLoginAuthKey() {
        return loginAuthKey;
    }

    public void setLoginAuthKey(String loginAuthKey) {
        this.loginAuthKey = loginAuthKey;
    }

    public int getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(int isDoctor) {
        this.isDoctor = isDoctor;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(String bindInfo) {
        this.bindInfo = bindInfo;
    }

    @Override
    public String toString() {
        return "UserResultInfo{" +
                "userID=" + userID +
                ", pesAddr='" + pesAddr + '\'' +
                ", pesIP=" + pesIP +
                ", pesPort=" + pesPort +
                ", loginAuthKey='" + loginAuthKey + '\'' +
                ", isDoctor=" + isDoctor +
                ", userid=" + userid +
                ", newPwd='" + newPwd + '\'' +
                ", bindInfo='" + bindInfo + '\'' +
                '}';
    }
}
