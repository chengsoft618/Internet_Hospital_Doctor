package cn.longmaster.hospital.doctor.core.entity.im;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.doctorlibrary.util.common.DateUtil;

/**
 * 视频时间群组消息
 * Created by YY on 17/8/25.
 * {st：“204”，aid:“预约id”,IMid:“IM房间id”，t：“视频会诊时间” ，role：“发送者角色”}
 */

public class VideoDateGroupMessageInfo extends BaseGroupMessageInfo {
    private int messageType;
    private long date;
    private int role;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
        return "VideoDateGroupMessageInfo{" +
                "messageType=" + messageType +
                ", date=" + date +
                ", role=" + role +
                '}' + super.toString();
    }

    @Override
    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        super.jsonToObject(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject msgContentJson = new JSONObject(jsonObject.optString("_msgContent"));
        setMessageType(msgContentJson.optInt("st", 0));
        setDate(DateUtil.dateToMillisecond(msgContentJson.optString("t")) / 1000);
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
        msgContentJsonObject.put("t", DateUtil.millisecondToStandardDate(getDate() * 1000));
        msgContentJsonObject.put("role", getRole());
        jsonObject.put("_msgContent", msgContentJsonObject.toString());
        return jsonObject;
    }

    @Override
    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        super.objectToObject(chatMsgInfo);
        JSONObject msgContentJson = new JSONObject(chatMsgInfo.getMsgContent());
        setMessageType(msgContentJson.optInt("st", 0));
        setDate(DateUtil.dateToMillisecond(msgContentJson.optString("t")) / 1000);
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }
}
