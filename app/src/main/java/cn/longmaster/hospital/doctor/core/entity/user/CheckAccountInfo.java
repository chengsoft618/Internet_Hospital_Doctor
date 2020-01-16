package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 检测账号信息实体类
 * Created by yangyong on 16/9/8.
 */
public class CheckAccountInfo implements Serializable {
    @JsonField("user_id")
    private int userId;//返回的userid
    @JsonField("bindInfo")
    private String bindInfo;//该账号绑定的信息 返回绑定的类型字符串，如：“2，4，5”，如没有则为空
    @JsonField("isDoctor")
    private int isDoctor;//用户身份信息：0用户 1医生 2销售代表 3医生助理

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBindInfo() {
        return bindInfo;
    }

    public void setBindInfo(String bindInfo) {
        this.bindInfo = bindInfo;
    }

    public int getIsDoctor() {
        return isDoctor;
    }

    public void setIsDoctor(int isDoctor) {
        this.isDoctor = isDoctor;
    }

    @Override
    public String toString() {
        return "CheckAccountInfo{" +
                "userId=" + userId +
                ", bindInfo='" + bindInfo + '\'' +
                ", isDoctor=" + isDoctor +
                '}';
    }
}
