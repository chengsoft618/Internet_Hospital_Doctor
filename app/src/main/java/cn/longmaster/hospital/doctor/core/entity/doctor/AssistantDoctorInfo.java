package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 助理医师信息
 * Created by Yang² on 2016/7/27.
 */
public class AssistantDoctorInfo implements Serializable {
    @JsonField("user_id")
    private int userId;//助理医师ID
    @JsonField("user_name")
    private String userName;//助理医师姓名
    @JsonField("avater_file")
    private String avaterFile;//头像图片名称
    @JsonField("phone_num")
    private String phoneNum;//电话号码

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvaterFile() {
        return avaterFile;
    }

    public void setAvaterFile(String avaterFile) {
        this.avaterFile = avaterFile;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "AssistantDoctorInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avaterFile='" + avaterFile + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
