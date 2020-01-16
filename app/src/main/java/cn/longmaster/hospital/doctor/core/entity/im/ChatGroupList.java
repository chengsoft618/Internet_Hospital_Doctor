package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 群组列表
 * Created by Yang² on 2017/8/28.
 */

public class ChatGroupList implements Serializable {
    @JsonField("is_finish")
    private int isFinish;//0：结束；1：未结束
    @JsonField("data")
    private List<ChatGroupInfo> chatGroupList;//群组列表

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public List<ChatGroupInfo> getChatGroupList() {
        return chatGroupList;
    }

    public void setChatGroupList(List<ChatGroupInfo> chatGroupList) {
        this.chatGroupList = chatGroupList;
    }

    @Override
    public String toString() {
        return "ChatGroupList{" +
                "isFinish=" + isFinish +
                ", chatGroupList=" + chatGroupList +
                '}';
    }
}
