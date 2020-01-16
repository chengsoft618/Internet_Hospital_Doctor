package cn.longmaster.hospital.doctor.core.entity.im;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 图片群组消息
 * Created by YY on 17/8/24.
 * <p>
 * {st:“200”,aid:“预约id”,IMid:“IM房间id”，pName：“图片名称”，role：“发送者角色”}
 */

public class PictureGroupMessageInfo extends BaseGroupMessageInfo {
    private int messageType;
    private String pictureName;
    private int role;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    @Override
    public int getRole() {
        return role;
    }

    @Override
    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "PictureGroupMessageInfo{" +
                "messageType=" + messageType +
                ", pictureName='" + pictureName + '\'' +
                ", role=" + role +
                '}' + super.toString();
    }

    @Override
    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        super.jsonToObject(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject msgContentJson = new JSONObject(jsonObject.optString("_msgContent"));
        setMessageType(msgContentJson.optInt("st", 0));
        setPictureName(msgContentJson.optString("pName", ""));
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }

    @Override
    public JSONObject objectToJson() throws JSONException {
        JSONObject jsonObject = super.objectToJson();
        JSONObject msgContentJsonObject = new JSONObject();
        msgContentJsonObject.put("st", getMessageType());
        msgContentJsonObject.put("aid", getAppointmentId());
        msgContentJsonObject.put("IMid", getImId());
        msgContentJsonObject.put("pName", getPictureName());
        msgContentJsonObject.put("role", getRole());
        jsonObject.put("_msgContent", msgContentJsonObject.toString());
        return jsonObject;
    }

    @Override
    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        super.objectToObject(chatMsgInfo);
        JSONObject msgContentJson = new JSONObject(chatMsgInfo.getMsgContent());
        setMessageType(msgContentJson.optInt("st", 0));
        setPictureName(msgContentJson.optString("pName", ""));
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }
}
