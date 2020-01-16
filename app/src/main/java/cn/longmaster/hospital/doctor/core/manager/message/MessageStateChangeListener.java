package cn.longmaster.hospital.doctor.core.manager.message;


import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;

/**
 * 消息状态变化监听
 * Created by yangyong on 2015/8/4.
 */
public interface MessageStateChangeListener {
    /**
     * 收到新消息
     *
     * @param baseMessageInfo 消息实例
     */
    public void onNewMessage(BaseMessageInfo baseMessageInfo);
}
