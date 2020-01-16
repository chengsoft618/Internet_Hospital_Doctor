package cn.longmaster.hospital.doctor.core.manager.room;

import com.ppcp.manger.PpcpInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.timeout.TimeoutHelper;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.VideoRoomResultCode;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomMember;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomResultInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;

import static cn.longmaster.hospital.doctor.core.AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_DOCTOR_ADVICE_MAKE_SURE;
import static cn.longmaster.hospital.doctor.core.AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_DOCTOR_PATIENT_ADD;


/**
 * 聊天室管理类实现
 * Created by Yang² on 2016/12/28.
 */

public class ConsultRoomManager extends BaseManager implements VideoRoomInterface, TimeoutHelper.OnTimeoutCallback<Integer> {
    private final String TAG = ConsultRoomManager.class.getSimpleName();

    public static final int AV_TYPE_VIDEO = 0;// 视频
    public static final int AV_TYPE_VOICE = 1;// 音频
    public static final int VIDEO_VOICE_CLOSED = 1;//视频声音关闭
    public static final int VIDEO_VOICE_OPEN = 2;//视频声音打开

    public static final int REQUEST_TYPE_JOIN_VIDEO_ROOM = 0;// 请求类型：进入聊天室
    public static final int REQUEST_TYPE_EXIT_VIDEO_ROOM = 1;// 请求类型：退出聊天室
    public static final int REQUEST_TYPE_CHANGE_AV_TYPE = 2;// 请求类型：切换音视频
    public static final int REQUEST_TYPE_SEND_VIDEO_ROOM_MESSAGE = 3;// 请求类型：发送消息
    public static final int REQUEST_TYPE_VIDEO_SUBSCRIBE = 4;// 请求类型：视频订阅列表
    public static final int REQUEST_TYPE_MAV_DIS_SUBSCRIBE = 5;// 请求类型：取消视频订阅
    public static final int REQUEST_TYPE_SELF_CHANGE_AV_TYPE = 6;// 请求类型：切换自身音视频
    public static final int REQUEST_TYPE_GET_ROOM_MEMBER = 7;// 请求类型：获取房间成员信息
    public static final int REQUEST_TYPE_FORBID_SPEAK = 8;// 请求类型：禁言用户
    public static final int REQUEST_TYPE_SEPARATE_MEMBER = 9;// 请求类型：隔离成员
    public static final int REQUEST_TYPE_PAUSE_MIC = 10;// 请求类型：关闭MIC
    public static final int REQUEST_TYPE_SELF_INTERRUPTED = 11;// 请求类型：阻断
    public static final int REQUEST_TYPE_SET_PCC_PARAM = 12;// 请求类型：设置PccParam
    public static final int REQUEST_TYPE_CHANGE_SEAT = 13;// 请求类型：改变席位
    public static final int REQUEST_TYPE_SEEK_HELP = 14;// 一件协助
    public static final int REQUEST_TYPE_EVALUATE_SEEK_HELP = 15;// 评价

    private static final int MESSAGE_TYPE_MEMBER_BASE = 100;
    public static final int MESSAGE_TYPE_MEMBER_JOIN_VIDEO_ROOM = MESSAGE_TYPE_MEMBER_BASE + 1;//成员加入房间
    public static final int MESSAGE_TYPE_MEMBER_EXIT_VIDEO_ROOM = MESSAGE_TYPE_MEMBER_BASE + 2;//成员退出房间
    public static final int MESSAGE_TYPE_MEMBER_MEMBER_CHANGE_AV_TYPE = MESSAGE_TYPE_MEMBER_BASE + 3;//成员切换音视频模式
    public static final int MESSAGE_TYPE_MEMBER_SEND_MSG = MESSAGE_TYPE_MEMBER_BASE + 4;//成员发送聊天室消息
    public static final int MESSAGE_TYPE_MEMBER_RESERVE_END = MESSAGE_TYPE_MEMBER_BASE + 5;//成员被踢
    public static final int MESSAGE_TYPE_MEMBER_MAV_SUBSCRIBE = MESSAGE_TYPE_MEMBER_BASE + 6;//成员视频订阅
    public static final int MESSAGE_TYPE_MEMBER_MAV_DIS_SUBSCRIBE = MESSAGE_TYPE_MEMBER_BASE + 7;//成员取消视频订阅
    public static final int MESSAGE_TYPE_MEMBER_ON_AV_TYPE_CHANGED = MESSAGE_TYPE_MEMBER_BASE + 8;//音视频切换（特殊权限者发起）
    public static final int MESSAGE_TYPE_MEMBER_FORBID_SPEAK = MESSAGE_TYPE_MEMBER_BASE + 9;//成员禁言广播
    public static final int MESSAGE_TYPE_MEMBER_SEPARATED = MESSAGE_TYPE_MEMBER_BASE + 10;//用户被隔离广播
    public static final int MESSAGE_TYPE_MEMBER_USER_INTERRUPTED = MESSAGE_TYPE_MEMBER_BASE + 11;//用户阻断
    public static final int MESSAGE_TYPE_VIDEO_ROOM_SHUTDOWN = MESSAGE_TYPE_MEMBER_BASE + 12;//聊天室关闭
    public static final int MESSAGE_TYPE_VIDEO_ON_MEMBER_SPEAKING = MESSAGE_TYPE_MEMBER_BASE + 13;//讲话身份成员是否在说话
    public static final int MESSAGE_TYPE_VIDEO_ON_MEMBER_SEATSTATE_CHANGED = MESSAGE_TYPE_MEMBER_BASE + 14;//成员改变席位状态
    public static final int MESSAGE_TYPE_VIDEO_ON_ROOM_OPERATION = MESSAGE_TYPE_MEMBER_BASE + 15;//8 设置主屏，9 踢人，10禁言，11 解禁，12 主持人隐身，13主持人解除隐身
    public static final int MESSAGE_TYPE_VIDEO_ON_SELF_SEEK_HELP = MESSAGE_TYPE_MEMBER_BASE + 16;//用户求助回复
    public static final int MESSAGE_TYPE_VIDEO_ON_ROOM_SEEK_HELP_END = MESSAGE_TYPE_MEMBER_BASE + 17;//客服结束求助
    public static final int MESSAGE_TYPE_VIDEO_ON_SELF_EVALUATE_SEEK_HELP = MESSAGE_TYPE_MEMBER_BASE + 18;//用户评价求助回复

    public static final int MAX_REQUEST_WAIT_TIME = 15 * 1000;// 最大操作等待时间

    //  dcp接口
    private static PpcpInterface mDcpInterface;
    private AppApplication mApplication;
    private TimeoutHelper<Integer> mTimeoutHelper;
    private Map<Integer, OnRoomSelfStateChangeListener> mCallbacks = new HashMap<Integer, OnRoomSelfStateChangeListener>();
    //为了防止医生助理切换全体语音和全体视频时有延迟而处理不到第二次请求，切换音视频单独提出来控制
    private List<OnRoomSelfStateChangeListener> mSelfChangeAVTypeListener = new ArrayList<>();
    private OnRoomMemberStateChangeListener mMemberChangeListener;
    //为了解决改变席位时先收到超时然后又收到成功不能处理的问题，单独提出来控制
    private OnRoomSelfStateChangeListener mSelfChangeSeatListener;

    @Override
    public void onManagerCreate(AppApplication application) {
        AppApplication.getInstance().injectManager(this);
        mApplication = application;
        mDcpInterface = mApplication.getManager(DcpManager.class).getDcpInterface();
        mTimeoutHelper = new TimeoutHelper<>();
        mTimeoutHelper.setCallback(this);
    }

    public void registerMemberChangeListener(OnRoomMemberStateChangeListener listener) {
        mMemberChangeListener = listener;
    }

    public void registerSelfChangeSeatListener(OnRoomSelfStateChangeListener listener) {
        mSelfChangeSeatListener = listener;
    }

    public void unRegisterMemberChangeListener() {
        mMemberChangeListener = null;
    }

    public void unRegisterSelfChangeSeatListener() {
        mSelfChangeSeatListener = null;
    }

    @Override
    public void joinVideoRoom(int reserveID, int roomID, int userType, String userName, int netType, OnRoomSelfStateChangeListener listener) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_JOIN_VIDEO_ROOM)) {
                return;
            }
            try {
                JSONObject json = new JSONObject();
                json.put("_reserveID", reserveID);
                json.put("_roomID", roomID);
                json.put("_userName", userName);
                json.put("_userType", userType);
                json.put("_clientVersion", AppConfig.CLIENT_VERSION);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("netType", netType);
                json.put("_reserved", jsonObject.toString());
                Logger.logD(Logger.ROOM, TAG + "->joinVideoRoom()->json:" + json.toString());

                mCallbacks.put(REQUEST_TYPE_JOIN_VIDEO_ROOM, listener);
                mTimeoutHelper.request(REQUEST_TYPE_JOIN_VIDEO_ROOM, MAX_REQUEST_WAIT_TIME);

                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_JOIN_VIDEO_ROOM, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void changeSeatState(byte userSeat) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            try {
                JSONObject json = new JSONObject();
                json.put("_userSeat", userSeat);
                mTimeoutHelper.request(REQUEST_TYPE_CHANGE_SEAT, MAX_REQUEST_WAIT_TIME);
                Logger.logD(Logger.ROOM, TAG + "->changeSeatState()->json:" + json.toString());

                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_VIDEO_CHANGE_SEAT, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exitVideoRoom(int roomID, OnRoomSelfStateChangeListener listener) {
        Logger.logD(Logger.ROOM, TAG + "->exitVideoRoom()->mDcpInterface:" + mDcpInterface + "->mCallbacks.containsKey(REQUEST_TYPE_EXIT_VIDEO_ROOM:" + mCallbacks.containsKey(REQUEST_TYPE_EXIT_VIDEO_ROOM));
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_EXIT_VIDEO_ROOM)) {
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("_roomID", roomID);

                mCallbacks.put(REQUEST_TYPE_EXIT_VIDEO_ROOM, listener);
                mTimeoutHelper.request(REQUEST_TYPE_EXIT_VIDEO_ROOM, MAX_REQUEST_WAIT_TIME);
                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_EXIT_VIDEO_ROOM, json.toString());
                Logger.logD(Logger.ROOM, TAG + "->exitVideoRoom()->json:" + json.toString());
            } catch (JSONException e) {
                Logger.logD(Logger.ROOM, TAG + "->exitVideoRoom()->异常:" + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void changeSelfAVType(int roomID, int avType, OnRoomSelfStateChangeListener listener) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {

            try {
                JSONObject json = new JSONObject();
                json.put("_roomID", roomID);
                json.put("_avType", avType);
                Logger.logD(Logger.ROOM, TAG + "->mavSubscribe()->json:" + json.toString());

                mSelfChangeAVTypeListener.add(listener);
                mTimeoutHelper.request(REQUEST_TYPE_SELF_CHANGE_AV_TYPE, MAX_REQUEST_WAIT_TIME);
                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_CHANGE_SELF_AV_TYPE, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getRoomMember(int roomID, OnRoomSelfStateChangeListener listener) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_GET_ROOM_MEMBER)) {
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("_roomID", roomID);
                Logger.logD(Logger.ROOM, TAG + "->getRoomMember()->json:" + json.toString());

                mCallbacks.put(REQUEST_TYPE_GET_ROOM_MEMBER, listener);
                mTimeoutHelper.request(REQUEST_TYPE_GET_ROOM_MEMBER, MAX_REQUEST_WAIT_TIME);
                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_GET_ROOM_MEMBER, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void videoSubscribe(List<VideoRoomMember> memberList, OnRoomSelfStateChangeListener listener) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_VIDEO_SUBSCRIBE)) {
                return;
            }
            try {
                if (memberList != null && memberList.size() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < memberList.size(); i++) {
                        if (memberList.get(i).getUserType() != AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                            JSONObject json = new JSONObject();
                            json.put("uid", memberList.get(i).getUserId());
                            json.put("type", memberList.get(i).getVideoType());
                            jsonArray.put(json.toString());
                        }
                    }
                    JSONObject json = new JSONObject();
                    json.put("_list", jsonArray);
                    Logger.logD(Logger.ROOM, TAG + "->videoSubscribe()->json:" + json.toString());

                    mCallbacks.put(REQUEST_TYPE_VIDEO_SUBSCRIBE, listener);
                    mTimeoutHelper.request(REQUEST_TYPE_VIDEO_SUBSCRIBE, MAX_REQUEST_WAIT_TIME);
                    mDcpInterface.Request(DcpFuncConfig.FUN_NAME_VIDEO_SUBSCRIBE, json.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pauseMic(boolean isPause) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_PAUSE_MIC)) {
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("isPause", isPause);
                Logger.logD(Logger.ROOM, TAG + "->pauseMic()->json:" + json.toString());

                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_PAUSE_MIC, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void seekHelp(String name) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_SEEK_HELP)) {
                return;
            }
            try {
                JSONObject json = new JSONObject();
                json.put("_name", name);
                json.put("_reserved", "");
                Logger.logD(Logger.ROOM, TAG + "->seekHelp()->json:" + json.toString());
                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_SEEK_HELP, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void evaluateSeekHelp(long seqId, float evaluate) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_EVALUATE_SEEK_HELP)) {
                return;
            }
            try {
                JSONObject json = new JSONObject();
                json.put("_seqID", seqId);
                json.put("_evaluate", evaluate);
                Logger.logD(Logger.ROOM, TAG + "->pauseMic()->json:" + json.toString());

                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_EVALUATE_SEEK_HELP, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void selfInterrupted(int interruptState) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_SELF_INTERRUPTED)) {
                return;
            }

            try {
                JSONObject json = new JSONObject();
                json.put("_interruptState", interruptState);
                Logger.logD(Logger.ROOM, TAG + "->selfInterrupted()->json:" + json.toString());

                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_SELF_INTERRUPTED, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReserveInvate(int result, String json) {
        if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
            return;
        }
    }

    @Override
    public void setPccParam(String eq, String level, int sourceType, int streamType) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_SET_PCC_PARAM)) {
                return;
            }
            try {
                JSONObject json = new JSONObject();
                json.put("_eq", eq);
                json.put("_joinLevel", level);
                json.put("_recordSourceType", sourceType);
                json.put("_playStreamType", streamType);
                Logger.logD(Logger.ROOM, TAG + "->setPccParam()->json:" + json.toString());
                mDcpInterface.Request(DcpFuncConfig.FUN_NAME_SET_PCC_PARAM, json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送值班门诊医嘱单确认操作
     *
     * @param type 0：转诊单 1：诊疗建议
     */
    public void sendDoctorAdviceMakeSureOpt(int type) {
        try {
            JSONObject json = new JSONObject();
            json.put("_operation", ROOM_OPERATION_CODE_DOCTOR_ADVICE_MAKE_SURE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            json.put("_reserved", jsonObject.toString());
            Logger.logD(Logger.ROOM, TAG + "->sendDoctorAdviceMakeSureOpt()->json:" + json.toString());
            operateRoom(ROOM_OPERATION_CODE_DOCTOR_ADVICE_MAKE_SURE, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendDoctorDataMakeSureOpt(String patientName){
        try {
            JSONObject json = new JSONObject();
            json.put("_operation", ROOM_OPERATION_CODE_DOCTOR_PATIENT_ADD);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", patientName);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("msgContent",jsonObject.toString());
            json.put("_reserved", jsonObject1.toString());
            Logger.logD(Logger.ROOM, TAG + "->sendDoctorDataMakeSureOpt()->json:" + json.toString());
            operateRoom(ROOM_OPERATION_CODE_DOCTOR_PATIENT_ADD, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void operateRoom(int operateType, JSONObject jsonData) {
        if (mDcpInterface == null) {
            if (AppConfig.IS_DEBUG_MODE) {
                throw (new RuntimeException("dcp接口没有初始化"));
            }
        } else {
            if (mCallbacks.containsKey(REQUEST_TYPE_SET_PCC_PARAM)) {
                return;
            }
            Logger.logD(Logger.ROOM, TAG + "->operateRoom()->json:" + jsonData.toString());
            mDcpInterface.Request(DcpFuncConfig.FUN_NAME_OPERATEROOM, jsonData.toString());
        }
    }

    @Override
    public void onRecvSelfReqData(final int result, final int reqType, final String json) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (reqType == REQUEST_TYPE_SELF_CHANGE_AV_TYPE) {
                    if (mSelfChangeAVTypeListener.size() > 0) {
                        mTimeoutHelper.cancel(reqType);
                        OnRoomSelfStateChangeListener callback = mSelfChangeAVTypeListener.remove(0);
                        if (callback == null) {
                            return;
                        }
                        VideoRoomResultInfo resultInfo = null;
                        if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                            resultInfo = VideoRoomResultInfo.parseJson(json);
                        }
                        callback.onSelfChangeAVType(result, resultInfo);
                    }
                    return;
                }
                if (reqType == MESSAGE_TYPE_VIDEO_ON_SELF_SEEK_HELP) {//呼叫客服回调
                    mTimeoutHelper.cancel(reqType);
                    VideoRoomResultInfo resultInfo = null;
                    if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                        resultInfo = VideoRoomResultInfo.parseJson(json);
                    }
                    if (mSelfChangeSeatListener != null) {
                        mSelfChangeSeatListener.onSelfSeekHelp(result, resultInfo);
                    }
                    return;
                }
                if (reqType == MESSAGE_TYPE_VIDEO_ON_SELF_EVALUATE_SEEK_HELP) {//用户评价求助回复
                    mTimeoutHelper.cancel(reqType);
                    VideoRoomResultInfo resultInfo = null;
                    if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                        resultInfo = VideoRoomResultInfo.parseJson(json);
                    }
                    if (mSelfChangeSeatListener != null) {
                        mSelfChangeSeatListener.onselfEvaluatesSeekHelp(result, resultInfo);
                    }
                    return;
                }
                if (reqType == REQUEST_TYPE_CHANGE_SEAT) {
                    mTimeoutHelper.cancel(reqType);
                    VideoRoomResultInfo resultInfo = null;
                    if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                        resultInfo = VideoRoomResultInfo.parseJson(json);
                    }
                    if (mSelfChangeSeatListener != null) {
                        mSelfChangeSeatListener.onSelfChangeSeatState(result, resultInfo);
                    }
                    return;
                }
                Logger.logD(Logger.ROOM, TAG + "->onRecvSelfReqData->result:" + result + ",reqType:" + reqType + ",json" + json);
                Logger.logD(Logger.ROOM, TAG + "->onRecvSelfReqData->mCallbacks.containsKey(reqType)?:" + mCallbacks.containsKey(reqType));
                mTimeoutHelper.cancel(reqType);
                OnRoomSelfStateChangeListener callback = mCallbacks.remove(reqType);
                if (callback == null) {
                    Logger.logD(Logger.ROOM, TAG + "->onRecvSelfReqData->callback为null?:true");
                    return;
                }
                Logger.logD(Logger.ROOM, TAG + "->onRecvSelfReqData->callback为null?:false");
                VideoRoomResultInfo resultInfo = null;
                if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                    resultInfo = VideoRoomResultInfo.parseJson(json);
                }
                distributeSelfCallback(reqType, result, resultInfo, callback);
            }
        });
    }

    @Override
    public void onRecvMemberData(int result, final int messageKey, final String json) {
        Logger.logD(Logger.ROOM,TAG+"#onRecvMemberData:"+"{\"result\":"+result+"\"messageKey:\""+messageKey+"\"json:\""+json);
        if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
            AppHandlerProxy.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    VideoRoomResultInfo resultInfo = VideoRoomResultInfo.parseJson(json);
                    if (mMemberChangeListener != null) {
                        switch (messageKey) {
                            case MESSAGE_TYPE_MEMBER_JOIN_VIDEO_ROOM:
                                mMemberChangeListener.onMemberJoinRoom(resultInfo);
                                break;

                            case MESSAGE_TYPE_MEMBER_EXIT_VIDEO_ROOM:
                                mMemberChangeListener.onMemberExitRoom(resultInfo);
                                break;

                            case MESSAGE_TYPE_MEMBER_MEMBER_CHANGE_AV_TYPE:
                                mMemberChangeListener.onMemberChangeAvType(resultInfo);
                                break;

                            case MESSAGE_TYPE_MEMBER_ON_AV_TYPE_CHANGED:
                                mMemberChangeListener.onSpecialMemberChangeAVType(resultInfo);
                                break;

                            case MESSAGE_TYPE_VIDEO_ROOM_SHUTDOWN:
                                mMemberChangeListener.onShutDown(resultInfo);
                                break;

                            case MESSAGE_TYPE_VIDEO_ON_MEMBER_SPEAKING:
                                mMemberChangeListener.onMemberSpeaking(resultInfo);
                                break;

                            case MESSAGE_TYPE_VIDEO_ON_MEMBER_SEATSTATE_CHANGED:
                                mMemberChangeListener.onMemberSeatStateChanged(resultInfo);
                                break;

                            case MESSAGE_TYPE_VIDEO_ON_ROOM_OPERATION:
                                mMemberChangeListener.onRoomOperation(resultInfo);
                                break;

                            case MESSAGE_TYPE_VIDEO_ON_ROOM_SEEK_HELP_END:
                                mMemberChangeListener.onRoomSeekHelpEnd(resultInfo);
                                break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onTimeout(TimeoutHelper<Integer> timeoutHelper, final Integer taskId) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (taskId == REQUEST_TYPE_SELF_CHANGE_AV_TYPE) {
                    if (mSelfChangeAVTypeListener.size() > 0) {
                        mTimeoutHelper.cancel(taskId);
                        OnRoomSelfStateChangeListener callback = mSelfChangeAVTypeListener.remove(0);
                        if (callback == null) {
                            return;
                        }
                        distributeSelfCallback(taskId, VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_TIMEOUT, null, callback);
                    }
                    return;
                }
                if (taskId == REQUEST_TYPE_CHANGE_SEAT) {
                    mTimeoutHelper.cancel(taskId);
                    if (mSelfChangeSeatListener != null) {
                        mSelfChangeSeatListener.onSelfChangeSeatState(VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_TIMEOUT, null);
                    }
                    return;
                }
                mTimeoutHelper.cancel(taskId);
                OnRoomSelfStateChangeListener callback = mCallbacks.remove(taskId);
                if (callback == null) {
                    return;
                }
                distributeSelfCallback(taskId, VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_TIMEOUT, null, callback);
            }
        });
    }

    private void distributeSelfCallback(int reqType, int result, VideoRoomResultInfo resultInfo, OnRoomSelfStateChangeListener callback) {
        switch (reqType) {
            case REQUEST_TYPE_JOIN_VIDEO_ROOM:
                callback.onSelfJoinRoom(result, resultInfo);
                break;

            case REQUEST_TYPE_CHANGE_SEAT:
                callback.onSelfChangeSeatState(result, resultInfo);
                break;

            case REQUEST_TYPE_EXIT_VIDEO_ROOM:
                callback.onSelfExitRoom(result, resultInfo);
                break;

            case REQUEST_TYPE_SELF_CHANGE_AV_TYPE:
                callback.onSelfChangeAVType(result, resultInfo);
                break;

            case REQUEST_TYPE_GET_ROOM_MEMBER:
                callback.onSelfGetRoomMember(result, resultInfo);
                break;

            case REQUEST_TYPE_VIDEO_SUBSCRIBE:
                callback.onSelfVideoSubscribe(result, resultInfo);
                break;
        }
    }

    /**
     * 返回用户List的JSON数组
     *
     * @param memberList
     * @return
     */
    private JSONArray getJsonArray(List<VideoRoomMember> memberList) {
        if (memberList == null || memberList.size() == 0) {
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        try {
            for (VideoRoomMember member : memberList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("uid", member.getUserId());
                jsonArray.put(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray;
        }
        return jsonArray;
    }
}
