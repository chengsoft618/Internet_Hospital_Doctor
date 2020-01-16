package cn.longmaster.hospital.doctor.core.manager.room;

import org.json.JSONObject;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomMember;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomResultInfo;

/**
 * 视频聊天室接口类，用于声明视频就诊相关接口
 * Created by Yang² on 2016/12/28.
 */

public interface VideoRoomInterface {

    /**
     * 加入聊天室
     *
     * @param reserveID 预约ID
     * @param roomID    房间ID
     * @param userType  用户类型
     * @param userName  用户姓名
     * @param listener  回调接口
     * @param netType   网络类型 0/热点;1/wifi;2/2g;3/3g;4/4g;5/5g;128/未知;255/默认
     */
    void joinVideoRoom(int reserveID, int roomID, int userType, String userName, int netType, OnRoomSelfStateChangeListener listener);

    /**
     * 改变席位状态
     *
     * @param userSeat 设置的席位
     */
    void changeSeatState(byte userSeat);

    /**
     * 退出聊天室
     *
     * @param roomID   房间ID(传医生ID)
     * @param listener 回调接口
     */
    void exitVideoRoom(int roomID, OnRoomSelfStateChangeListener listener);

    /**
     * 切换自身音视频
     *
     * @param roomID   房间ID
     * @param avType   avType：0、视频，1、音频
     * @param listener 回调接口
     */
    void changeSelfAVType(int roomID, int avType, OnRoomSelfStateChangeListener listener);

    /**
     * 获取房间成员信息(备注：进入房间前调用)
     *
     * @param roomID   房间ID
     * @param listener 回调接口
     */
    void getRoomMember(int roomID, OnRoomSelfStateChangeListener listener);

    /**
     * 订阅视频列表
     *
     * @param memberList
     * @param listener
     */
    void videoSubscribe(List<VideoRoomMember> memberList, OnRoomSelfStateChangeListener listener);

    /**
     * 关闭MIC
     *
     * @param isPause 是否关闭Mic
     */
    void pauseMic(boolean isPause);

    /**
     * 一键协助
     *
     * @param name 请求协助者姓名
     */
    void seekHelp(String name);

    /**
     * 求助评价
     */
    void evaluateSeekHelp(long seqId, float evaluate);

    /**
     * 中断
     *
     * @param interruptState 1:中断 2:取消中断
     */
    void selfInterrupted(int interruptState);

    /**
     * 预约邀请
     *
     * @param result 返回码
     * @param json   服务器返回json
     */
    void onReserveInvate(int result, String json);

    /**
     * 设置Pcc参数
     *
     * @param eq
     * @param level
     * @param sourceType
     * @param streamType
     */
    void setPccParam(String eq, String level, int sourceType, int streamType);

    /**
     * 发送房间操作
     *
     * @param operateType 操作类型
     */
    void operateRoom(int operateType, JSONObject jsonData);

    /**
     * 接收到自己发起的请求回调
     *
     * @param result  返回嘛
     * @param reqType 请求类型
     * @param json    服务器返回json
     */
    void onRecvSelfReqData(int result, int reqType, String json);

    /**
     * 收到其它成员变化回调
     *
     * @param result     返回码
     * @param messageKey 消息key
     * @param json       服务器返回json
     */
    void onRecvMemberData(int result, int messageKey, String json);

    /**
     * 视频聊天室状态变化回调接口
     */
    interface OnRoomSelfStateChangeListener {
        //进入聊天室
        void onSelfJoinRoom(int result, VideoRoomResultInfo resultInfo);

        //改变说话状态
        void onSelfChangeSeatState(int result, VideoRoomResultInfo resultInfo);

        //退出聊天室
        void onSelfExitRoom(int result, VideoRoomResultInfo resultInfo);

        //切换自身音视频
        void onSelfChangeAVType(int result, VideoRoomResultInfo resultInfo);

        //获取房间成员信息
        void onSelfGetRoomMember(int result, VideoRoomResultInfo resultInfo);

        void onSelfVideoSubscribe(int result, VideoRoomResultInfo resultInfo);

        //用户求助回复
        void onSelfSeekHelp(int result, VideoRoomResultInfo roomResultInfo);

        //用户评论回复
        void onselfEvaluatesSeekHelp(int result, VideoRoomResultInfo roomResultInfo);
    }

    /**
     * 聊天室其他成员变化回调
     */
    interface OnRoomMemberStateChangeListener {
        //成员加入房间
        void onMemberJoinRoom(VideoRoomResultInfo roomResultInfo);

        //成员退出房间
        void onMemberExitRoom(VideoRoomResultInfo roomResultInfo);

        //成员切换自身音视频
        void onMemberChangeAvType(VideoRoomResultInfo roomResultInfo);

        //音视频切换（特殊权限者发起）
        void onSpecialMemberChangeAVType(VideoRoomResultInfo roomResultInfo);

        //聊天室关闭
        void onShutDown(VideoRoomResultInfo roomResultInfo);

        //讲话身份成员是否在说话
        void onMemberSpeaking(VideoRoomResultInfo roomResultInfo);

        //成员改变席位状态
        void onMemberSeatStateChanged(VideoRoomResultInfo roomResultInfo);

        //8 设置主屏，9 踢人，10禁言，11 解禁，12 主持人隐身，13主持人解除隐身
        void onRoomOperation(VideoRoomResultInfo roomResultInfo);

        //客服结束求助
        void onRoomSeekHelpEnd(VideoRoomResultInfo roomResultInfo);

    }
}
