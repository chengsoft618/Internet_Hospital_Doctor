package cn.longmaster.hospital.doctor.core;

/**
 * 视频聊天室返回码定义类
 * Created by yangyong on 2015/7/28.
 */
public class VideoRoomResultCode {
    public static final int Video_Room_RESULT_CODE_BASE = 0;
    public static final int VIDEO_ROOM_RESULT_CODE_SUCCESS = Video_Room_RESULT_CODE_BASE + 0;// 成功
    public static final int VIDEO_ROOM_RESULT_CODE_UNKOWN = Video_Room_RESULT_CODE_BASE + 1; //未知
    public static final int VIDEO_ROOM_RESULT_CODE_TIMEOUT = Video_Room_RESULT_CODE_BASE + 2; //超时
    public static final int VIDEO_ROOM_RESULT_CODE_UNVALIDREQUEST = Video_Room_RESULT_CODE_BASE + 3; //无效请求
    public static final int VIDEO_ROOM_RESULT_CODE_NOUSERID = Video_Room_RESULT_CODE_BASE + 4; //当前用户未登陆
    public static final int VIDEO_ROOM_RESULT_CODE_STATUSERROR = Video_Room_RESULT_CODE_BASE + 5; //请求状态错误
    public static final int VIDEO_ROOM_RESULT_CODE_JOINTIMEERROR = Video_Room_RESULT_CODE_BASE + 6; //加入时间错误
    public static final int VIDEO_ROOM_RESULT_CODE_ROOMNOTFOUND = Video_Room_RESULT_CODE_BASE + 7; //诊疗室没找到
    public static final int VIDEO_ROOM_RESULT_CODE_PVSERROR = Video_Room_RESULT_CODE_BASE + 8; //PVS处理错误
    public static final int VIDEO_ROOM_RESULT_CODE_RESERVENOTFOUND = Video_Room_RESULT_CODE_BASE + 9; //预约没有找到
    public static final int VIDEO_ROOM_RESULT_CODE_USERNOPOWER = Video_Room_RESULT_CODE_BASE + 10; //用户不能进入该预约房间
    public static final int VIDEO_ROOM_RESULT_CODE_NOPS = Video_Room_RESULT_CODE_BASE + 11; //ps资源不足
    public static final int VIDEO_ROOM_RESULT_CODE_DOOTHERRESERVE = Video_Room_RESULT_CODE_BASE + 12; //房间在处理另一个预约
    public static final int VIDEO_ROOM_RESULT_CODE_JOINTOOOFTEN = Video_Room_RESULT_CODE_BASE + 13; //加入频繁
    public static final int VIDEO_ROOM_RESULT_CODE_USERHASINROOM = Video_Room_RESULT_CODE_BASE + 14; //用户已经在房间中
    public static final int VIDEO_ROOM_RESULT_CODE_USERNOTINROOM = Video_Room_RESULT_CODE_BASE + 15; //用户不在房间中
    public static final int VIDEO_ROOM_RESULT_CODE_PESDISCONNECT = Video_Room_RESULT_CODE_BASE + 16; //pes断开
}
