package cn.longmaster.hospital.doctor.core.entity.im;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 语音群组消息
 * Created by YY on 17/8/25.
 * <p>
 * {st：“202”，aid:“预约id”,IMid:“IM房间id”，aName：“语音文件名”，t：“语音时长” ，role：“发送者角色”}
 */

public class VoiceGroupMessageInfo extends BaseGroupMessageInfo {
    private int messageType;
    private String fileName;
    private int timeLength;
    private int role;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
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
        return "VoiceGroupMessageInfo{" +
                "messageType=" + messageType +
                ", fileName='" + fileName + '\'' +
                ", timeLength='" + timeLength + '\'' +
                ", role=" + role +
                '}' + super.toString();
    }

    @Override
    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        super.jsonToObject(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject msgContentJson = new JSONObject(jsonObject.optString("_msgContent"));
        setMessageType(msgContentJson.optInt("st", 0));
        setFileName(msgContentJson.optString("aName", ""));
        setTimeLength(msgContentJson.optInt("t"));
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
        msgContentJsonObject.put("aName", getFileName());
        msgContentJsonObject.put("t", getTimeLength());
        msgContentJsonObject.put("role", getRole());
        jsonObject.put("_msgContent", msgContentJsonObject.toString());
        return jsonObject;
    }

    @Override
    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        super.objectToObject(chatMsgInfo);
        JSONObject msgContentJson = new JSONObject(chatMsgInfo.getMsgContent());
        setMessageType(msgContentJson.optInt("st", 0));
        setFileName(msgContentJson.optString("aName", ""));
        setTimeLength(msgContentJson.optInt("t"));
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }
}
