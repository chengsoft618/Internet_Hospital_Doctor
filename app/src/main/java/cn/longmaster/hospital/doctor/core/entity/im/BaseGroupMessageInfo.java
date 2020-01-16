package cn.longmaster.hospital.doctor.core.entity.im;

import android.support.annotation.IntDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.longmaster.doctorlibrary.util.common.DateUtil;

/**
 * Created by YY on 17/8/24.
 */

public class BaseGroupMessageInfo implements Serializable {
    public static final int PICTURE_MSG = 200;
    public static final int TEXT_MSG = 201;
    public static final int VOICE_MSG = 202;
    public static final int CARD_MSG = 203;
    public static final int WAITING_ADMISSION_MSG = 204;
    public static final int VIDEO_DATE_MSG = 205;
    public static final int MEMBER_JOIN_MSG = 206;
    public static final int MEMBER_EXIT_MSG = 207;
    public static final int REJECT_MSG = 208;
    public static final int TRIAGE_MSG = 209;
    public static final int ISSUE_ADVICE_MSG = 210;
    public static final int MEDICAL_ADVICE_MSG = 211;
    public static final int CALL_SERVICE_MSG = 212;
    public static final int VIDEO_START_MSG = 213;
    public static final int GUIDE_SET_DATE_MSG = 214;
    public static final int STATE_CHANGE_MSG = 215;

    @IntDef({PICTURE_MSG, TEXT_MSG, VOICE_MSG, CARD_MSG, WAITING_ADMISSION_MSG, VIDEO_DATE_MSG, MEMBER_JOIN_MSG, MEMBER_EXIT_MSG, REJECT_MSG, TRIAGE_MSG, ISSUE_ADVICE_MSG, MEDICAL_ADVICE_MSG, CALL_SERVICE_MSG, VIDEO_START_MSG, GUIDE_SET_DATE_MSG, STATE_CHANGE_MSG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupMessageType {

    }

    public static final int STATE_SENDING = 0;//发送中
    public static final int STATE_SUCCESS = 1;//发送成功
    public static final int STATE_FAILED = 2;//发送失败
    public static final int STATE_UPLOAD_SUCCESS = 3;//文件上传成功，消息发送失败
    public static final int STATE_UNREAD = 4;//未读
    public static final int STATE_READED = 5;//已读

    @IntDef({STATE_SENDING, STATE_SUCCESS, STATE_FAILED, STATE_UPLOAD_SUCCESS, STATE_UNREAD, STATE_READED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GroupMessageState {
    }

    private int groupId;
    private int reserveId;
    private int msgType;

    private long seqId;
    private long msgId;
    private int senderId;
    private String msgContent;
    private long sendDt;
    private int msgState;
    private int appointmentId;
    private int imId;
    private int role;
    private int owerId;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getSendDt() {
        return sendDt;
    }

    public void setSendDt(long sendDt) {
        this.sendDt = sendDt;
    }

    public int getMsgState() {
        return msgState;
    }

    public void setMsgState(int msgState) {
        this.msgState = msgState;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getImId() {
        return imId;
    }

    public void setImId(int imId) {
        this.imId = imId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getOwerId() {
        return owerId;
    }

    public void setOwerId(int owerId) {
        this.owerId = owerId;
    }

    @Override
    public String toString() {
        return "BaseGroupMessageInfo{" +
                "groupId=" + groupId +
                ", reserveId=" + reserveId +
                ", msgType=" + msgType +
                ", seqId=" + seqId +
                ", msgId=" + msgId +
                ", senderId=" + senderId +
                ", msgContent='" + msgContent + '\'' +
                ", sendDt=" + sendDt +
                ", msgState=" + msgState +
                ", appointmentId=" + appointmentId +
                ", imId=" + imId +
                ", role=" + role +
                ", owerId=" + owerId +
                '}';
    }

    public BaseGroupMessageInfo jsonToObject(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        setReserveId(jsonObject.optInt("_recverID", 0));
        setMsgType(jsonObject.optInt("_msgType", 0));
        setMsgId(jsonObject.optInt("_msgID", 0));
        setSendDt(jsonObject.optLong("_sendDT", 0));
        setSenderId(jsonObject.optInt("_senderID", 0));
        setSeqId(jsonObject.optInt("_seqID", 0));

        setMsgContent(jsonObject.optString("_msgContent"));
        JSONObject msgContentJson = new JSONObject(getMsgContent());
        setAppointmentId(msgContentJson.optInt("aid", 0));
        setImId(msgContentJson.optInt("IMid", 0));
        setGroupId(getImId());
        setRole(msgContentJson.optInt("role", 0));
        return this;
    }

    public JSONObject objectToJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_groupID", getGroupId());
        jsonObject.put("_reserveID", getReserveId());
        jsonObject.put("_msgType", getMsgType());
        jsonObject.put("_seqID", getSeqId());
        if (getMsgContent() != null) {
            jsonObject.put("_msgContent", getMsgContent());
        }
        return jsonObject;
    }

    public BaseGroupMessageInfo objectToObject(ChatMsgInfo chatMsgInfo) throws JSONException {
        setMsgType(chatMsgInfo.getMsgType());
        setMsgId(chatMsgInfo.getMsgId());
        setSendDt(DateUtil.dateToMillisecond(chatMsgInfo.getInsertDt()) / 1000);
        setSenderId(chatMsgInfo.getUserId());
        setGroupId(chatMsgInfo.getGroupId());

        setMsgContent(chatMsgInfo.getMsgContent());
        JSONObject msgContentJson = new JSONObject(getMsgContent());
        setAppointmentId(msgContentJson.optInt("aid", 0));
        setImId(msgContentJson.optInt("IMid", 0));
        setMsgState(STATE_SUCCESS);
        return this;
    }
}
