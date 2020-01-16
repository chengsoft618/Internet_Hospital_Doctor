package cn.longmaster.hospital.doctor.core.entity.im;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 成员加入群组消息
 * Created by YY on 17/8/25.
 * <p>
 * {st：“204”，aid:“预约id”,Imid:“IM房间id”，uid：“加入者id” ，role：“加入者角色”}
 */

public class MemberJoinGroupMessageInfo extends BaseGroupMessageInfo {
    private int messageType;
    private int userId;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MemberJoinGroupMessageInfo{" +
                "messageType=" + messageType +
                "userId=" + userId +
                '}' + super.toString();
    }

    @Override
    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        super.jsonToObject(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject msgContentJson = new JSONObject(jsonObject.optString("_msgContent"));
        setMessageType(msgContentJson.optInt("st", 0));
        setUserId(msgContentJson.optInt("uid", 0));
        return this;
    }

    @Override
    public JSONObject objectToJson() throws JSONException {
        JSONObject jsonObject = super.objectToJson();
        JSONObject msgContentJsonObject = new JSONObject();
        msgContentJsonObject.put("st", getMessageType());
        msgContentJsonObject.put("aid", getAppointmentId());
        msgContentJsonObject.put("IMid", getImId());
        msgContentJsonObject.put("uid", getUserId());
        jsonObject.put("_msgContent", msgContentJsonObject.toString());
        return jsonObject;
    }

    @Override
    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        super.objectToObject(chatMsgInfo);
        JSONObject msgContentJson = new JSONObject(chatMsgInfo.getMsgContent());
        setMessageType(msgContentJson.optInt("st", 0));
        setUserId(msgContentJson.optInt("uid"));
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }
}