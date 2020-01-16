package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/3/14.
 */

public class RoomListInfo implements Serializable {
    private int reserveID;
    private int roomID;
    private String roomName;

    public int getReserveID() {
        return reserveID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "RoomListInfo{" +
                "reserveID=" + reserveID +
                ", roomID=" + roomID +
                ", roomName=" + roomName +
                '}';
    }
}
