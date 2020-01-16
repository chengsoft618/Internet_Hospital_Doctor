package cn.longmaster.hospital.doctor.core.manager.common;

import com.ppcp.manger.PpcpListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.utils.StringUtils;

/**
 * 客户端底层接口实现
 * Created by yangyong on 2015/7/17.
 */
public class DcpListener implements PpcpListener {
    private final String TAG = DcpListener.class.getSimpleName();

    @Override
    public void onRecvData(int result, String funcName, String json) {
        if (StringUtils.isTrimEmpty(funcName)) {
            return;
        }
        AppApplication.getInstance().injectManager(this);
        if (!StringUtils.equals(funcName, "onMemberSpeaking")) {
            Logger.logD(Logger.COMMON, TAG + "->onRecvData()->result:" + result + ", funcName:" + funcName + ", json:" + json);
        }

        if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SETPESINFO)) {
            //设置pes回调接口
            AppApplication.getInstance().getManager(UserInfoManager.class).onRecvData(result, DcpFuncConfig.ACTION_TYPE_SETPESINFO, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_LOGIN)) {
            //登陆回调
            AppApplication.getInstance().getManager(UserInfoManager.class).onRecvData(result, DcpFuncConfig.ACTION_TYPE_LOGIN, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ONKICKOFF)) {
            //被踢下线
            AppApplication.getInstance().getManager(UserInfoManager.class).onKickOff(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ONOFFLINE)) {
            //离线
            AppApplication.getInstance().getManager(UserInfoManager.class).onOffline(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SEND_ACTION)) {
            //发送激活包
            AppApplication.getInstance().getManager(UserInfoManager.class).onSendAction(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MESSAGE_NOTIFICATION)) {
            //收到在线消息
            try {
                JSONObject jsonObject = new JSONObject(json);
                int messageType = jsonObject.optInt("_msgType");
                if (messageType >= BaseGroupMessageInfo.PICTURE_MSG && messageType <= BaseGroupMessageInfo.STATE_CHANGE_MSG) {
                    AppApplication.getInstance().getManager(GroupMessageManager.class).onReceiveNewGroupMessage(result, json);
                } else {
                    AppApplication.getInstance().getManager(MessageManager.class).onMessageNotification(result, json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_GET_MESSAGE)) {
            //拉取离线消息回调
            AppApplication.getInstance().getManager(MessageManager.class).onGetMessage(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SEND_GROUP_MSG)) {
            AppApplication.getInstance().getManager(GroupMessageManager.class).onSendGroupMsg(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_GET_GROUP_STATUS)) {
            AppApplication.getInstance().getManager(GroupMessageManager.class).onGetGroupStatus(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_UPDATE_GROUP_STATUS)) {
            AppApplication.getInstance().getManager(GroupMessageManager.class).onUpdGroupStatus(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_GET_GROUP_LIST)) {
            AppApplication.getInstance().getManager(GroupMessageManager.class).onGetGroupList(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_INQUIRE_BALANCE_NEW)) {
            //获取余额回调
            AppApplication.getInstance().getManager(AccountManager.class).onInquireBalance(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_CHECK_ADMIN_INFO)) {
            //查询管理员
            AppApplication.getInstance().getManager(UserInfoManager.class).onCheckAdminInfo(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_GET_AUTHENTICATION_INFO)) {
            //获取鉴权信息
            AppApplication.getInstance().getManager(AuthenticationManager.class).onGetAuthenticationInfo(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_GET_ROOM_LIST_INFO)) {
            //拉取在线房间列表
            AppApplication.getInstance().getManager(UserInfoManager.class).onGetRoomListInfo(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_CHECK_PWD_INFO)) {
            //验证会议室密码
            AppApplication.getInstance().getManager(UserInfoManager.class).onCheckPwdInfo(result, json);
        }
        /*******************************************视频相关start***********************************************************/
        else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_JOIN_VIDEO_ROOM)) {
            //自己加入聊天室

            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_JOIN_VIDEO_ROOM, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_SEAT_CHANGE)) {
            //自己改变说话状态
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_CHANGE_SEAT, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_EXIT_VIDEO_ROOM)) {
            //自己退出聊天室
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_EXIT_VIDEO_ROOM, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_CHANGE_AV_TYPE)) {
            //自己切换音视频模式
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_CHANGE_AV_TYPE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_SEND_VIDEO_ROOM_MESSAGE)) {
            //自己发送聊天室消息
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_SEND_VIDEO_ROOM_MESSAGE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_VIDEO_SUBSCRIBE)) {
            //观看视频列表
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_VIDEO_SUBSCRIBE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_MAV_DIS_SUBSCRIBE)) {
            //取消视频订阅
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_MAV_DIS_SUBSCRIBE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_CHANGE_AV_SELF_TYPE)) {
            //自己切换自己的音视频模式
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_SELF_CHANGE_AV_TYPE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_GET_ROOM_MEMBER)) {
            //获取房间成员回复
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_GET_ROOM_MEMBER, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_FORBID_SPEAK)) {
            //禁言用户回复
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_FORBID_SPEAK, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_SEPARATE_MEMBER)) {
            //隔离用户回复
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.REQUEST_TYPE_SEPARATE_MEMBER, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_JOIN_VIDEOA_ROOM)) {
            //成员加入聊天室
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_JOIN_VIDEO_ROOM, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_EXIT_VIDEO_ROOM)) {
            //成员退出聊天室
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_EXIT_VIDEO_ROOM, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_CHANGE_AV_TYPE)) {
            //成员切换音视频模式
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_MEMBER_CHANGE_AV_TYPE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_SEND_MESSAGE)) {
            //成员发送聊天室消息
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_SEND_MSG, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_RESERVE_END)) {
            //成员被踢
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_RESERVE_END, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_SUBSCRIBE)) {
            //成员视频订阅
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_MAV_SUBSCRIBE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_DIS_SUBSCRIBE)) {
            //成员取消视频订阅
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_MAV_DIS_SUBSCRIBE, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_AV_TYPE_CHANGED)) {
            //音视频切换（特殊权限者发起）
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_ON_AV_TYPE_CHANGED, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_FORBID_SPEAK)) {
            //成员禁言广播
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_FORBID_SPEAK, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_SEPARATED)) {
            //用户被隔离广播
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_SEPARATED, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_USER_INTERRUPTED)) {
            //用户阻断
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_MEMBER_USER_INTERRUPTED, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_VIDEO_ROOM_SHUT_DOWN)) {
            //聊天室关闭
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ROOM_SHUTDOWN, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_SPEAKING)) {
            //讲话身份成员是否在说话
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_MEMBER_SPEAKING, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_MEMBER_SEATSTATE_CHANGED)) {
            //讲话身份成员是否在说话
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_MEMBER_SEATSTATE_CHANGED, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_RESERVE_INVATE)) {
            //预约邀请
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onReserveInvate(result, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_ROOM_OPERATION)) {
            //8 设置主屏，9 踢人，10禁言，11 解禁，12 主持人隐身，13主持人解除隐身
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_ROOM_OPERATION, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_SEEK_HELP)) {
            //用户求助回复
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_SELF_SEEK_HELP, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_SELF_EVALUATE_SEEK_HELP)) {
            //求助者评价回复
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvSelfReqData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_SELF_EVALUATE_SEEK_HELP, json);
        } else if (funcName.equals(DcpFuncConfig.FUN_NAME_ON_ROOM_SEEK_HELP_END)) {
            //客服结束求助
            AppApplication.getInstance().getManager(ConsultRoomManager.class).onRecvMemberData(result, ConsultRoomManager.MESSAGE_TYPE_VIDEO_ON_ROOM_SEEK_HELP_END, json);
        } else if (StringUtils.equals(funcName, DcpFuncConfig.FUN_NAME_ON_USER_ONLINE_STATE_GET)) {
            AppApplication.getInstance().getManager(DcpManager.class).onUserOnLineStateRecv(json);
        }
        /*******************************************视频相关end***********************************************************/
    }
}
