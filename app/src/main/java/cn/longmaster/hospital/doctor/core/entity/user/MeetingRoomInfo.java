package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/12/20.
 */

public class MeetingRoomInfo implements Serializable {
    private int userId;
    private byte userSeat;
    private boolean isSpeak;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getUserSeat() {
        return userSeat;
    }

    public void setUserSeat(byte userSeat) {
        this.userSeat = userSeat;
    }

    public boolean isSpeak() {
        return isSpeak;
    }

    public void setSpeak(boolean speak) {
        isSpeak = speak;
    }

    @Override
    public String toString() {
        return "MeetingRoomInfo{" +
                "userId=" + userId +
                ", userSeat=" + userSeat +
                ", isSpeak=" + isSpeak +
                '}';
    }
}
