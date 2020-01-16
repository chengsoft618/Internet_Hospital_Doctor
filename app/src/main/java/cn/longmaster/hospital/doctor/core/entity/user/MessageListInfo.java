package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 离线活动消息列表
 * Created by Yang² on 2016/11/2.
 */

public class MessageListInfo implements Serializable {
    @JsonField("_msgContent")
    private String msgContent;
    @JsonField("_senderID")
    private int senderID;
    @JsonField("_recverID")
    private int recverID;
    @JsonField("_sendDT")
    private long sendDT;

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getRecverID() {
        return recverID;
    }

    public void setRecverID(int recverID) {
        this.recverID = recverID;
    }

    public long getSendDT() {
        return sendDT;
    }

    public void setSendDT(long sendDT) {
        this.sendDT = sendDT;
    }

    @Override
    public String toString() {
        return "MessageListInfo{" +
                "msgContent='" + msgContent + '\'' +
                ", senderID=" + senderID +
                ", recverID=" + recverID +
                ", sendDT=" + sendDT +
                '}';
    }
}
