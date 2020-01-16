package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 聊天记录列表
 * Created by Yang² on 2017/8/30.
 */

public class ChatMsgList implements Serializable {
    @JsonField("is_finish")
    private int isFinish;//0：结束；1：未结束
    @JsonField("data")
    private List<ChatMsgInfo> chatMsgList;//聊天记录列表

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<ChatMsgInfo> getChatMsgList() {
        return chatMsgList;
    }

    public void setChatMsgList(List<ChatMsgInfo> chatMsgList) {
        this.chatMsgList = chatMsgList;
    }

    @Override
    public String toString() {
        return "ChatMsgList{" +
                "isFinish=" + isFinish +
                ", chatMsgList=" + chatMsgList +
                '}';
    }
}
