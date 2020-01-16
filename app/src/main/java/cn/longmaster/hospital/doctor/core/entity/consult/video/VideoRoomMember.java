package cn.longmaster.hospital.doctor.core.entity.consult.video;

import java.io.Serializable;

import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;


/**
 * 视频聊天成员实体类
 * Created by yangyong on 2015/7/28.
 */
public class VideoRoomMember implements Serializable {
    public static final int STATE_UNSUBSCRIBED = 0;
    public static final int STATE_SUBSCRIBED = 1;

    private int userId;// 成员用户id
    private String userName = "";// 成员用户名称
    private int userType;// 成员用户类型
    private int avType = ConsultRoomManager.AV_TYPE_VIDEO;// 成员用户当前音视频模式
    private int userState = STATE_UNSUBSCRIBED;
    private int forbidType;
    private int separateType;
    private byte seat;
    private byte status;//(uint32_t)第一位 屏幕共享，第二位 主屏，第三位 禁言，第四位 隐身，第五位 播放视频文件
    private int videoType;

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setSubscribed() {
        userState = STATE_SUBSCRIBED;
    }

    public void setUnsubscribed() {
        userState = STATE_UNSUBSCRIBED;
    }

    public boolean isSubscribed() {
        return userState == STATE_SUBSCRIBED;
    }

    public boolean isAdmin() {
        return userType == 1;
    }

    public int getForbidType() {
        return forbidType;
    }

    public void setForbidType(int forbidType) {
        this.forbidType = forbidType;
    }

    public int getSeparateType() {
        return separateType;
    }

    public void setSeparateType(int separateType) {
        this.separateType = separateType;
    }

    public boolean isSeparated() {
        return separateType == 1;
    }

    public int getAvType() {
        return avType;
    }

    public void setAvType(int avType) {
        this.avType = avType;
    }

    public byte getSeat() {
        return seat;
    }

    public void setSeat(byte seat) {
        this.seat = seat;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    @Override
    public String toString() {
        return "VideoRoomMember{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userType=" + userType +
                ", avType=" + avType +
                ", userState=" + userState +
                ", forbidType=" + forbidType +
                ", separateType=" + separateType +
                ", seat=" + seat +
                ", status=" + status +
                ", videoType=" + videoType +
                '}';
    }
}
