package cn.longmaster.hospital.doctor.core.entity.im;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 等待接诊群组消息
 * Created by YY on 17/8/25.
 * <p>
 * {st：“204”，aid:“预约id”,IMid:“IM房间id”，docId：“大医生id” ，role：“发送者角色”}
 */

public class WaitingAddmissionGroupMessageInfo extends BaseGroupMessageInfo {
    private int messageType;
    private int doctorId;
    private int role;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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
        return "WaitingAddmissionGroupMessageInfo{" +
                "messageType=" + messageType +
                ", doctorId=" + doctorId +
                ", role=" + role +
                '}' + super.toString();
    }

    @Override
    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        super.jsonToObject(json);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject msgContentJson = new JSONObject(jsonObject.optString("_msgContent"));
        setMessageType(msgContentJson.optInt("st", 0));
        setDoctorId(msgContentJson.optInt("docId", 0));
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
        msgContentJsonObject.put("docId", getDoctorId());
        msgContentJsonObject.put("role", getRole());
        jsonObject.put("_msgContent", msgContentJsonObject.toString());
        return jsonObject;
    }

    @Override
    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        super.objectToObject(chatMsgInfo);
        JSONObject msgContentJson = new JSONObject(chatMsgInfo.getMsgContent());
        setMessageType(msgContentJson.optInt("st", 0));
        setDoctorId(msgContentJson.optInt("docId", 0));
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }
}
