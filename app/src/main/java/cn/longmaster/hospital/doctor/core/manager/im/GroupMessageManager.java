package cn.longmaster.hospital.doctor.core.manager.im;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.json.JsonHelper;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.thread.AsyncResult;
import cn.longmaster.doctorlibrary.util.timeout.TimeoutHelper;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpFuncConfig;
import cn.longmaster.hospital.doctor.core.db.DBHelper;
import cn.longmaster.hospital.doctor.core.db.contract.GroupMessageContract;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CallServiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CardGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatMsgInfo;
import cn.longmaster.hospital.doctor.core.entity.im.GuideSetDateGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.IssueAdviceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MedicalAdviceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberExitGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberJoinGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.entity.im.PictureGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.RejectGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.StateChangeGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.TextGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.TriageGroupMessagInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VideoDateGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VideoStartGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VoiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.WaitingAddmissionGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DBManager;
import cn.longmaster.hospital.doctor.core.manager.storage.DatabaseTask;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatMemberListRequester;
import cn.longmaster.hospital.doctor.core.upload.simple.FileUploadResult;
import cn.longmaster.hospital.doctor.core.upload.simple.IMPictureUploader;
import cn.longmaster.hospital.doctor.core.upload.simple.IMVoiceUploader;
import cn.longmaster.upload.OnNginxUploadStateCallback;
import cn.longmaster.utils.StringUtils;

/**
 * 群组消息管理类
 * Created by YY on 17/8/24.
 */

public class GroupMessageManager extends BaseManager implements TimeoutHelper.OnTimeoutCallback<Long> {
    private static final String TAG = GroupMessageManager.class.getSimpleName();
    private final int TIMER_ID_SEND_GROUP_MESSAGE = 0;
    private final int TIMER_ID_GET_GROUP_STATE = 1;
    private final int TIMER_ID_UPDATE_GROUP_STATE = 2;

    private static final int MAX_WAITING_TIME = 10 * 1000;
    private AppApplication mApplication;
    private DcpManager mDcpManager;
    private UserInfoManager mUserInfoManager;
    private DBManager mDBManager;
    private LocalNotificationManager mLocalNotificationManager;
    private List<GroupMessageStateChangeListener> mGroupMessageStateChangeListeners = new ArrayList<>();
    private List<GroupStateChangeListener> mGroupStateChangeListeners = new ArrayList<>();
    private List<GroupMessageUnreadCountStateChangeListener> mUnreadCountStateChangeListeners = new ArrayList<>();
    private Map<Long, GroupMessageSendStateChangeListener> mGroupMessageSendStateChangeListeners = new HashMap<>();
    private TimeoutHelper<Long> mSendGroupMessageTimeoutHelper = new TimeoutHelper<>();
    private TimeoutHelper<Long> mGetGroupStateTimeoutHelper = new TimeoutHelper<>();
    private TimeoutHelper<Long> mUpdateGroupStateTimeoutHelper = new TimeoutHelper<>();
    private int mCurentImId;

    @Override
    public void onManagerCreate(AppApplication application) {
        mApplication = application;
    }

    @Override
    public void onAllManagerCreated() {
        super.onAllManagerCreated();
        mDcpManager = getManager(DcpManager.class);
        mUserInfoManager = getManager(UserInfoManager.class);
        mDBManager = getManager(DBManager.class);
        mLocalNotificationManager = getManager(LocalNotificationManager.class);
        mSendGroupMessageTimeoutHelper.setCallback(this);
        mSendGroupMessageTimeoutHelper.setId(TIMER_ID_SEND_GROUP_MESSAGE);
        mGetGroupStateTimeoutHelper.setCallback(this);
        mGetGroupStateTimeoutHelper.setId(TIMER_ID_GET_GROUP_STATE);
        mUpdateGroupStateTimeoutHelper.setCallback(this);
        mUpdateGroupStateTimeoutHelper.setId(TIMER_ID_UPDATE_GROUP_STATE);
    }

    /**
     * 注册/注销群组消息监听器
     *
     * @param listener   监听器
     * @param isRegister true注册、false注销
     */
    public void registerGroupMessageStateChangeListener(GroupMessageStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mGroupMessageStateChangeListeners.add(listener);
        } else {
            mGroupMessageStateChangeListeners.remove(listener);
        }
    }

    /**
     * 注册/注销群组监听器
     *
     * @param listener   监听器
     * @param isRegister true注册、false注销
     */
    public void registerGroupStateChangeListener(GroupStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mGroupStateChangeListeners.add(listener);
        } else {
            mGroupStateChangeListeners.remove(listener);
        }
    }

    /**
     * 注册/注销群组消息未读条数变化监听器
     *
     * @param listener   监听器
     * @param isRegister true注册、false注销
     */
    public void registerUnreadCountStateChangeListener(GroupMessageUnreadCountStateChangeListener listener, boolean isRegister) {
        if (isRegister) {
            mUnreadCountStateChangeListeners.add(listener);
        } else {
            mUnreadCountStateChangeListeners.remove(listener);
        }
    }

    public void setCurentImId(int imId) {
        mCurentImId = imId;
    }

    public int getCurentImId() {
        return mCurentImId;
    }

    /**
     * 发送图片群组消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param pictureName   图片名称
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendPictureGroupMessage(final int appointmentId, final int imId, final String pictureName, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", pictureName:" + pictureName + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<PictureGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<PictureGroupMessageInfo> runOnDBThread(AsyncResult<PictureGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    PictureGroupMessageInfo messageInfo = new PictureGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.PICTURE_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setPictureName(pictureName);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<PictureGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                final PictureGroupMessageInfo messageInfo = asyncResult.getData();
                listener.onSaveDBStateChanged(code, messageInfo);
                if (code == 0) {
                    uploadPictureFile(messageInfo, listener);
                }
            }
        });
    }

    /**
     * 发送呼叫客户群组消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param doctorId      医生id
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendCallServiceGroupMessage(final int appointmentId, final int imId, final int doctorId, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendCallServiceGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", doctorId:" + doctorId + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<CallServiceGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<CallServiceGroupMessageInfo> runOnDBThread(AsyncResult<CallServiceGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    CallServiceGroupMessageInfo messageInfo = new CallServiceGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.CALL_SERVICE_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setDoctorId(doctorId);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<CallServiceGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendCallServiceGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                listener.onSaveDBStateChanged(code, asyncResult.getData());
                if (code == 0) {
                    sendGroupMsg(asyncResult.getData(), listener);
                }
            }
        });
    }

    /**
     * 发送名片消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param doctorId      医生id
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendCardGroupMessage(final int appointmentId, final int imId, final int doctorId, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendCardGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", doctorId:" + doctorId + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<CardGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<CardGroupMessageInfo> runOnDBThread(AsyncResult<CardGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    CardGroupMessageInfo messageInfo = new CardGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.CARD_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setDoctorId(doctorId);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<CardGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendCardGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                listener.onSaveDBStateChanged(code, asyncResult.getData());
                if (code == 0) {
                    sendGroupMsg(asyncResult.getData(), listener);
                }
            }
        });
    }

    /**
     * 发送拒接群组消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param reason        原因
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendRejectGroupMessage(final int appointmentId, final int imId, final String reason, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendRejectGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", reason:" + reason + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<RejectGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<RejectGroupMessageInfo> runOnDBThread(AsyncResult<RejectGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    RejectGroupMessageInfo messageInfo = new RejectGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.REJECT_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setReason(reason);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<RejectGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendRejectGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                listener.onSaveDBStateChanged(code, asyncResult.getData());
                if (code == 0) {
                    sendGroupMsg(asyncResult.getData(), listener);
                }
            }
        });
    }

    /**
     * 发送文本消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param content       文本内容
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendTextGroupMessage(final int appointmentId, final int imId, final String content, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendTextGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", content:" + content + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<TextGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<TextGroupMessageInfo> runOnDBThread(AsyncResult<TextGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    TextGroupMessageInfo messageInfo = new TextGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.TEXT_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setContent(content);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<TextGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendTextGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                listener.onSaveDBStateChanged(code, asyncResult.getData());
                if (code == 0) {
                    sendGroupMsg(asyncResult.getData(), listener);
                }
            }
        });
    }

    /**
     * 发送语音聊天消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param fileName      文件名
     * @param timeLength    语音时长
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendVoiceGroupMessage(final int appointmentId, final int imId, final String fileName, final int timeLength, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", fileName:" + fileName + ", timeLength:" + timeLength + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<VoiceGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<VoiceGroupMessageInfo> runOnDBThread(AsyncResult<VoiceGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    VoiceGroupMessageInfo messageInfo = new VoiceGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.VOICE_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setFileName(fileName);
                    messageInfo.setTimeLength(timeLength);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<VoiceGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                final VoiceGroupMessageInfo messageInfo = asyncResult.getData();
                listener.onSaveDBStateChanged(code, messageInfo);
                if (code == 0) {
                    uploadVoiceFile(messageInfo, listener);
                }
            }
        });
    }

    /**
     * 发送设置视频时间群组消息
     *
     * @param appointmentId 预约id
     * @param imId          imid
     * @param date          日期
     * @param role          角色
     * @param listener      回调接口
     */
    public void sendSetDateGroupMessage(final int appointmentId, final int imId, final long date, final int role, final GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendSetDateGroupMessage()->appointmentId:" + appointmentId + ", imId:" + imId + ", date:" + date + ", role:" + role);
        mDBManager.submitDatabaseTask(new DatabaseTask<VideoDateGroupMessageInfo>() {
            int code = 0;

            @Override
            public AsyncResult<VideoDateGroupMessageInfo> runOnDBThread(AsyncResult<VideoDateGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                try {
                    VideoDateGroupMessageInfo messageInfo = new VideoDateGroupMessageInfo();
                    messageInfo.setMessageType(BaseGroupMessageInfo.VIDEO_DATE_MSG);
                    messageInfo.setAppointmentId(appointmentId);
                    messageInfo.setImId(imId);
                    messageInfo.setDate(date);
                    messageInfo.setRole(role);
                    messageInfo.setMsgType(messageInfo.getMessageType());
                    messageInfo.setGroupId(imId);
                    assemblyParametersAndSave(messageInfo, dbHelper);
                    asyncResult.setData(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                    code = -1;
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<VideoDateGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->sendSetDateGroupMessage()->保存数据库结果:" + code + ", messageInfo:" + asyncResult.getData());
                final VideoDateGroupMessageInfo messageInfo = asyncResult.getData();
                listener.onSaveDBStateChanged(code, messageInfo);
                if (code == 0) {
                    sendGroupMsg(messageInfo, listener);
                }
            }
        });
    }

    /**
     * 组装参数并保存
     *
     * @param messageInfo 消息实体
     * @param dbHelper    数据库
     * @throws JSONException 抛出异常，需要做异常处理
     */
    public void assemblyParametersAndSave(BaseGroupMessageInfo messageInfo, DBHelper dbHelper) throws JSONException {
        Logger.logD(Logger.IM, TAG + "->assemblyParametersAndSave()->messageInfo:" + messageInfo);
        messageInfo.setSenderId(mUserInfoManager.getCurrentUserInfo().getUserId());
        messageInfo.setReserveId(messageInfo.getGroupId());
        messageInfo.setMsgContent(messageInfo.objectToJson().optString("_msgContent"));
        messageInfo.setSendDt(System.currentTimeMillis() / 1000);
        messageInfo.setMsgState(BaseGroupMessageInfo.STATE_SENDING);
        saveGroupMessage(messageInfo, dbHelper);
    }

    /**
     * 保存群组消息
     *
     * @param messageInfo 群组消息
     * @param dbHelper    数据库
     */
    public void saveGroupMessage(BaseGroupMessageInfo messageInfo, DBHelper dbHelper) {
        Logger.logD(Logger.IM, TAG + "->saveGroupMessage()->messageInfo:" + messageInfo);
        messageInfo.setOwerId(mUserInfoManager.getCurrentUserInfo().getUserId());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_ID, messageInfo.getMsgId());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SENDER_ID, messageInfo.getSenderId());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_RECVER_ID, messageInfo.getReserveId());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_TYPE, messageInfo.getMsgType());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT, messageInfo.getMsgContent());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT, messageInfo.getSendDt());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE, messageInfo.getMsgState());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID, messageInfo.getGroupId());
            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_OWER_ID, messageInfo.getOwerId());
            long seqId = database.insert(GroupMessageContract.GroupMessageEntry.TABLE_NAME, null, values);
            Logger.logD(Logger.IM, TAG + "->saveGroupMessage()->seqId:" + seqId);
            messageInfo.setSeqId(seqId);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * 发送群组消息
     *
     * @param baseGroupMessageInfo 群组消息
     */
    public void sendGroupMsg(BaseGroupMessageInfo baseGroupMessageInfo, GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->sendGroupMsg()->baseGroupMessageInfo:" + baseGroupMessageInfo);
        if (!NetStateReceiver.hasNetConnected(mApplication)) {
            Logger.logD(Logger.IM, TAG + "->sendGroupMsg()->没有网络！");
            listener.onSendStateChanged(-1, baseGroupMessageInfo.getSeqId());
            return;
        }

        try {
            JSONObject jsonObject = baseGroupMessageInfo.objectToJson();
            jsonObject.put("_reserved", "");
            mGroupMessageSendStateChangeListeners.put(baseGroupMessageInfo.getSeqId(), listener);
            mSendGroupMessageTimeoutHelper.request(baseGroupMessageInfo.getSeqId(), MAX_WAITING_TIME);
            Logger.logD(Logger.IM, TAG + "->sendGroupMsg()->json:" + jsonObject.toString());
            mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_SEND_GROUP_MSG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重发失败群组消息
     *
     * @param messageInfo 群组消息
     * @param listener    回调接口
     */
    public void resendGroupMessage(BaseGroupMessageInfo messageInfo, GroupMessageSendStateChangeListener listener) {
        Logger.logD(Logger.IM, TAG + "->resendGroupMessage()->messageInfo:" + messageInfo.toString());
        int messageType = messageInfo.getMsgType();
        switch (messageType) {
            case BaseGroupMessageInfo.TEXT_MSG:
                sendGroupMsg(messageInfo, listener);
                break;

            case BaseGroupMessageInfo.PICTURE_MSG:
                if (messageInfo.getMsgState() == BaseGroupMessageInfo.STATE_FAILED) {
                    uploadPictureFile((PictureGroupMessageInfo) messageInfo, listener);
                } else {
                    sendGroupMsg(messageInfo, listener);
                }
                break;

            case BaseGroupMessageInfo.VOICE_MSG:
                if (messageInfo.getMsgState() == BaseGroupMessageInfo.STATE_FAILED) {
                    uploadVoiceFile((VoiceGroupMessageInfo) messageInfo, listener);
                } else {
                    sendGroupMsg(messageInfo, listener);
                }
                break;

            case BaseGroupMessageInfo.CARD_MSG:
                sendGroupMsg(messageInfo, listener);
                break;
        }
    }

    private void uploadPictureFile(final PictureGroupMessageInfo messageInfo, final GroupMessageSendStateChangeListener listener) {
        IMPictureUploader uploader = new IMPictureUploader(new OnNginxUploadStateCallback() {
            @Override
            public void onUploadComplete(String sessionId, final int httpResultCode, final String content) {
                Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->onUploadComplete()->sessionId:" + sessionId + ", code:" + httpResultCode + ", content:" + content);
                if (httpResultCode == 200 && !StringUtils.isEmpty(content)) {
                    try {
                        FileUploadResult result = JsonHelper.toObject(new JSONObject(content), FileUploadResult.class);
                        if (result.getCode() == 0) {
                            listener.onFileUploadStateChanged(messageInfo.getSeqId(), result.getCode(), result.getFileName());
                            FileUtil.renameFile(SdManager.getInstance().getIMPic() + messageInfo.getPictureName(), SdManager.getInstance().getIMPic() + result.getFileName());
                            messageInfo.setPictureName(result.getFileName());
                            updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_UPLOAD_SUCCESS);
                            JSONObject jsonObject = messageInfo.objectToJson();
                            updateGroupMessageContent(messageInfo.getSeqId(), jsonObject.optString("_msgContent"));
                            sendGroupMsg(messageInfo, listener);
                        } else {
                            listener.onFileUploadStateChanged(messageInfo.getSeqId(), httpResultCode, null);
                            updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    listener.onFileUploadStateChanged(messageInfo.getSeqId(), httpResultCode, null);
                    updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
                }
            }

            @Override
            public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {
                float progressLength = (float) (successBytes + blockByte) / totalBytes * 100;
                Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->onUploadProgresssChange()->progressLength:" + progressLength);
            }

            @Override
            public void onUploadException(String sessionId, Exception exception) {
                Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->onUploadException()->exception:" + exception.toString());
                listener.onFileUploadStateChanged(messageInfo.getSeqId(), -1, null);
                updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
            }

            @Override
            public void onUploadCancle(String sessionId) {
                Logger.logD(Logger.IM, TAG + "->onUploadCancle()");
                listener.onFileUploadStateChanged(messageInfo.getSeqId(), -1, null);
                updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
            }
        });
        uploader.filePath = SdManager.getInstance().getIMPic() + messageInfo.getPictureName();
        uploader.groupId = messageInfo.getGroupId();
        uploader.startUpload();
    }

    private void uploadVoiceFile(final VoiceGroupMessageInfo messageInfo, final GroupMessageSendStateChangeListener listener) {
        IMVoiceUploader uploader = new IMVoiceUploader(new OnNginxUploadStateCallback() {
            @Override
            public void onUploadComplete(String sessionId, final int httpResultCode, final String content) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onUploadComplete()->sessionId:" + sessionId + ", code:" + httpResultCode + ", content:" + content);
                if (httpResultCode == 200 && !StringUtils.isEmpty(content)) {
                    try {
                        FileUploadResult result = JsonHelper.toObject(new JSONObject(content), FileUploadResult.class);
                        if (result.getCode() == 0) {
                            listener.onFileUploadStateChanged(messageInfo.getSeqId(), result.getCode(), result.getFileName());
                            FileUtil.renameFile(SdManager.getInstance().getIMVoice() + messageInfo.getFileName(), SdManager.getInstance().getIMVoice() + result.getFileName());
                            messageInfo.setFileName(result.getFileName());
                            updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_UPLOAD_SUCCESS);
                            JSONObject jsonObject = messageInfo.objectToJson();
                            updateGroupMessageContent(messageInfo.getSeqId(), jsonObject.optString("_msgContent"));
                            sendGroupMsg(messageInfo, listener);
                        } else {
                            updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
                            listener.onFileUploadStateChanged(messageInfo.getSeqId(), result.getCode(), null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
                    listener.onFileUploadStateChanged(messageInfo.getSeqId(), httpResultCode, null);
                }
            }

            @Override
            public void onUploadProgresssChange(String sessionId, long successBytes, long blockByte, long totalBytes) {
                float progressLength = (float) (successBytes + blockByte) / totalBytes * 100;
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onUploadProgresssChange()->progressLength:" + progressLength);
            }

            @Override
            public void onUploadException(String sessionId, Exception exception) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onUploadException()->exception:" + exception.toString());
                listener.onFileUploadStateChanged(messageInfo.getSeqId(), -1, null);
                updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
            }

            @Override
            public void onUploadCancle(String sessionId) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onUploadCancle()");
                listener.onFileUploadStateChanged(messageInfo.getSeqId(), -1, null);
                updateGroupMessageState(messageInfo.getSeqId(), BaseGroupMessageInfo.STATE_FAILED);
            }
        });
        uploader.filePath = SdManager.getInstance().getIMVoice() + messageInfo.getFileName();
        uploader.groupId = messageInfo.getGroupId();
        uploader.startUpload();
    }

    /**
     * 发送群组消息回调
     *
     * @param result 发送返回码
     * @param json   回调json
     */
    public void onSendGroupMsg(final int result, final String json) {
        Logger.logD(Logger.IM, TAG + "->onSendGroupMsg()->result:" + result + ", json:" + json);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    final long seqId = jsonObject.optLong("_seqID", 0);
                    mSendGroupMessageTimeoutHelper.cancel(seqId);
                    Logger.logD(Logger.IM, TAG + "->onSendGroupMsg()->seqId:" + seqId);
                    updateGroupMessageState(seqId, result == 0 ? BaseGroupMessageInfo.STATE_SUCCESS : BaseGroupMessageInfo.STATE_FAILED);

                    GroupMessageSendStateChangeListener sendStateChangeListener = mGroupMessageSendStateChangeListeners.remove(seqId);
                    if (sendStateChangeListener != null) {
                        Logger.logD(Logger.IM, TAG + "->onSendGroupMsg()->回调给界面" + seqId + "发送结果" + result);
                        sendStateChangeListener.onSendStateChanged(result, seqId);
                    } else {
                        Logger.logE(Logger.IM, TAG + "->onSendGroupMsg()->没有找到" + seqId + "的回调接口，请检查代码!");
                    }

                    getGroupMessageBySeqId(seqId, new GetGroupMessageCommonCallback() {
                        @Override
                        public void onGetGroupMessage(BaseGroupMessageInfo groupMessageInfo) {
                            for (GroupMessageStateChangeListener listener : mGroupMessageStateChangeListeners) {
                                listener.onSendGroupMsgStateChanged(result, groupMessageInfo);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateGroupMessageState(final long seqId, @BaseGroupMessageInfo.GroupMessageState final int messageState) {
        Logger.logD(Logger.IM, TAG + "->updateGroupMessageState()->seqId:" + seqId + ", messageState:" + messageState);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE, messageState);
                    String whereClause = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID + " =? ";
                    String[] whereArgs = new String[]{String.valueOf(seqId)};
                    database.update(GroupMessageContract.GroupMessageEntry.TABLE_NAME, values, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 更新群组消息msgContent
     *
     * @param seqId      消息序列号
     * @param msgContent 消息msgContent
     */
    public void updateGroupMessageContent(final long seqId, final String msgContent) {
        Logger.logD(Logger.IM, TAG + "->updateGroupMessageState()->seqId:" + seqId + ", msgContent:" + msgContent);
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                database.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT, msgContent);
                    String whereClause = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID + " =? ";
                    String[] whereArgs = new String[]{String.valueOf(seqId)};
                    database.update(GroupMessageContract.GroupMessageEntry.TABLE_NAME, values, whereClause, whereArgs);
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {

            }
        });
    }

    /**
     * 从数据库获取群组消息列表
     *
     * @param imId      群组id
     * @param fromIndex 起始位置
     * @param callback  回调接口
     */
    public void getGroupMessages(final int imId, final int fromIndex, final GetLocalGroupMessagesCallback callback) {
        Logger.logD(Logger.IM, TAG + "->getGroupMessages()->imId:" + imId + ", fromIndex:" + fromIndex);
        mDBManager.submitDatabaseTask(new DatabaseTask<List<BaseGroupMessageInfo>>() {
            @Override
            public AsyncResult<List<BaseGroupMessageInfo>> runOnDBThread(AsyncResult<List<BaseGroupMessageInfo>> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                List<BaseGroupMessageInfo> messageInfos = new ArrayList<>();
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + GroupMessageContract.GroupMessageEntry.TABLE_NAME
                            + " WHERE "
                            + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID
                            + " = ? AND "
                            + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_OWER_ID
                            + " = ? ORDER BY "
                            + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT
                            + " DESC LIMIT 30"
                            + " OFFSET " + fromIndex;

                    cursor = database.rawQuery(sql, new String[]{String.valueOf(imId), String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())});
                    while (cursor.moveToNext()) {
                        BaseGroupMessageInfo messageInfo = new BaseGroupMessageInfo();
                        messageInfo.setSeqId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID)));
                        messageInfo.setMsgId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_ID)));
                        messageInfo.setSenderId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SENDER_ID)));
                        messageInfo.setReserveId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_RECVER_ID)));
                        messageInfo.setMsgType(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_TYPE)));
                        messageInfo.setMsgContent(cursor.getString(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT)));
                        messageInfo.setSendDt(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT)));
                        messageInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        messageInfo.setMsgState(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE)));
                        messageInfo.setImId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID)));

                        messageInfos.add(0, conversionGroupMessage(messageInfo));
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(messageInfos);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<List<BaseGroupMessageInfo>> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->getGroupMessages()->list:" + asyncResult.getData());
                callback.onGetLocalGroupMessagesStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 从数据库获取群组最新一条群组消息
     *
     * @param imId     imid
     * @param callback 回调接口
     */
    public void getLatestGroupMessage(final int imId, final GetLatestGroupMessageCallback callback) {
        Logger.logD(Logger.IM, TAG + "->getLatestGroupMessage()->imId:" + imId);
        mDBManager.submitDatabaseTask(new DatabaseTask<BaseGroupMessageInfo>() {
            @Override
            public AsyncResult<BaseGroupMessageInfo> runOnDBThread(AsyncResult<BaseGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                BaseGroupMessageInfo messageInfo = null;
                database.beginTransaction();
                try {
                    String table = GroupMessageContract.GroupMessageEntry.TABLE_NAME;
                    String selection = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID + " = ? AND " + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_OWER_ID + " = ?";
                    String[] selectionArgs = new String[]{String.valueOf(imId), String.valueOf(mUserInfoManager.getCurrentUserInfo().getUserId())};
                    String groupBy = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID;
                    String orderBy = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT;
                    String limit = "1";
                    cursor = database.query(table, null, selection, selectionArgs, groupBy, null, orderBy, limit);
                    while (cursor.moveToNext()) {
                        messageInfo = new BaseGroupMessageInfo();
                        messageInfo.setSeqId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID)));
                        messageInfo.setMsgId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_ID)));
                        messageInfo.setSenderId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SENDER_ID)));
                        messageInfo.setReserveId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_RECVER_ID)));
                        messageInfo.setMsgType(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_TYPE)));
                        messageInfo.setMsgContent(cursor.getString(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT)));
                        messageInfo.setSendDt(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT)));
                        messageInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        messageInfo.setMsgState(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE)));
                        messageInfo.setImId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID)));

                        messageInfo = conversionGroupMessage(messageInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(messageInfo);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<BaseGroupMessageInfo> asyncResult) {
                Logger.logD(Logger.IM, TAG + "->getLatestGroupMessage()->messageInfo:" + asyncResult.getData());
                callback.onGetLatestGroupMessageStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 获取群组消息未读条数
     *
     * @param groupId  群组id
     * @param callback 回调接口
     */
    public void getUnreadGroupMessageCount(final int groupId, final GetUnreadCountCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Long>() {
            @Override
            public AsyncResult<Long> runOnDBThread(AsyncResult<Long> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                String tableName = GroupMessageContract.GroupMessageEntry.TABLE_NAME;
                String selection = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID
                        + " = ? AND "
                        + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE
                        + " = ?";
                String[] selectionArgs = new String[]{String.valueOf(groupId), String.valueOf(BaseGroupMessageInfo.STATE_UNREAD)};
                long count = DatabaseUtils.queryNumEntries(database, tableName, selection, selectionArgs);
                asyncResult.setData(count);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Long> asyncResult) {
                callback.onGetUnreadCountStateChanged(asyncResult.getData());
            }
        });
    }

    /**
     * 通过seq查找群组消息
     *
     * @param seqId    群组id
     * @param callback 回调接口
     */
    public void getGroupMessageBySeqId(final long seqId, final GetGroupMessageCommonCallback callback) {
        mDBManager.submitDatabaseTask(new DatabaseTask<BaseGroupMessageInfo>() {
            @Override
            public AsyncResult<BaseGroupMessageInfo> runOnDBThread(AsyncResult<BaseGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Cursor cursor = null;
                BaseGroupMessageInfo messageInfo = null;
                database.beginTransaction();
                try {
                    String sql = "SELECT * FROM "
                            + GroupMessageContract.GroupMessageEntry.TABLE_NAME
                            + " WHERE " + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID
                            + " = ?";

                    cursor = database.rawQuery(sql, new String[]{String.valueOf(seqId)});
                    while (cursor.moveToNext()) {
                        messageInfo = new BaseGroupMessageInfo();
                        messageInfo.setSeqId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEQ_ID)));
                        messageInfo.setMsgId(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_ID)));
                        messageInfo.setSenderId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SENDER_ID)));
                        messageInfo.setReserveId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_RECVER_ID)));
                        messageInfo.setMsgType(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_TYPE)));
                        messageInfo.setMsgContent(cursor.getString(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_CONTENT)));
                        messageInfo.setSendDt(cursor.getLong(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_SEND_DT)));
                        messageInfo.setAppointmentId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_APPOINTMENT_ID)));
                        messageInfo.setMsgState(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE)));
                        messageInfo.setImId(cursor.getInt(cursor.getColumnIndex(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID)));

                        messageInfo = conversionGroupMessage(messageInfo);
                    }
                    database.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
                asyncResult.setData(messageInfo);
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<BaseGroupMessageInfo> asyncResult) {
                callback.onGetGroupMessage(asyncResult.getData());
            }
        });
    }

    /**
     * 根据群组id更新消息未读
     *
     * @param groupId 群组id
     */
    public void updateUnreadByGroupId(final int groupId) {
        mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
            @Override
            public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                mDBManager.submitDatabaseTask(new DatabaseTask<Void>() {
                    @Override
                    public AsyncResult<Void> runOnDBThread(AsyncResult<Void> asyncResult, DBHelper dbHelper) {
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        database.beginTransaction();
                        try {
                            ContentValues values = new ContentValues();
                            values.put(GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE, BaseGroupMessageInfo.STATE_READED);
                            String whereClause = GroupMessageContract.GroupMessageEntry.COLUMN_NAME_IM_ID
                                    + " = ? AND "
                                    + GroupMessageContract.GroupMessageEntry.COLUMN_NAME_MSG_STATE
                                    + " = ?";
                            String[] whereArgs = new String[]{String.valueOf(groupId), String.valueOf(BaseGroupMessageInfo.STATE_UNREAD)};
                            database.update(GroupMessageContract.GroupMessageEntry.TABLE_NAME, values, whereClause, whereArgs);
                            database.setTransactionSuccessful();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            database.endTransaction();
                        }
                        return asyncResult;
                    }

                    @Override
                    public void runOnUIThread(AsyncResult<Void> asyncResult) {

                    }
                });
                return asyncResult;
            }

            @Override
            public void runOnUIThread(AsyncResult<Void> asyncResult) {
                for (GroupMessageUnreadCountStateChangeListener listener : mUnreadCountStateChangeListeners) {
                    listener.onGroupMessageUnreadCountStateChanged(groupId, 0);
                }
            }
        });
    }

    /**
     * 转换群组消息
     *
     * @param baseGroupMessageInfo 基本群组消息
     * @return 转换后的群组消息
     * @throws JSONException 消息内容错误时抛出异常
     */
    private BaseGroupMessageInfo conversionGroupMessage(BaseGroupMessageInfo baseGroupMessageInfo) throws JSONException {
        Logger.logD(Logger.IM, TAG + "->conversionGroupMessage()->转换前->baseGroupMessageInfo:" + baseGroupMessageInfo);
        BaseGroupMessageInfo messageInfo = new BaseGroupMessageInfo();
        String json = baseGroupMessageInfo.objectToJson().toString();
        switch (baseGroupMessageInfo.getMsgType()) {
            case BaseGroupMessageInfo.PICTURE_MSG:
                messageInfo = new PictureGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.TEXT_MSG:
                messageInfo = new TextGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.VOICE_MSG:
                messageInfo = new VoiceGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.CARD_MSG:
                messageInfo = new CardGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.WAITING_ADMISSION_MSG:
                messageInfo = new WaitingAddmissionGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.VIDEO_DATE_MSG:
                messageInfo = new VideoDateGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.MEMBER_JOIN_MSG:
                messageInfo = new MemberJoinGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.MEMBER_EXIT_MSG:
                messageInfo = new MemberExitGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.REJECT_MSG:
                messageInfo = new RejectGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.TRIAGE_MSG:
                messageInfo = new TriageGroupMessagInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.ISSUE_ADVICE_MSG:
                messageInfo = new IssueAdviceGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.MEDICAL_ADVICE_MSG:
                messageInfo = new MedicalAdviceGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.CALL_SERVICE_MSG:
                messageInfo = new CallServiceGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.VIDEO_START_MSG:
                messageInfo = new VideoStartGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.GUIDE_SET_DATE_MSG:
                messageInfo = new GuideSetDateGroupMessageInfo().jsonToObject(json);
                break;

            case BaseGroupMessageInfo.STATE_CHANGE_MSG:
                messageInfo = new StateChangeGroupMessageInfo().jsonToObject(json);
                break;

            default:
                Logger.logD(Logger.IM, TAG + "->conversionGroupMessage()->不支持的消息类型，请检查消息类型是否正确！");
                break;
        }
        messageInfo.setSenderId(baseGroupMessageInfo.getSenderId());
        messageInfo.setSendDt(baseGroupMessageInfo.getSendDt());
        messageInfo.setReserveId(baseGroupMessageInfo.getReserveId());
        messageInfo.setMsgId(baseGroupMessageInfo.getMsgId());
        messageInfo.setMsgState(baseGroupMessageInfo.getMsgState());
        Logger.logD(Logger.IM, TAG + "->conversionGroupMessage()->转换后->messageInfo:" + messageInfo);
        return messageInfo;
    }

    /**
     * 拉取群组状态
     *
     * @param groupId 群组id
     */
    public void getGroupStatus(int groupId) {
        Logger.logD(Logger.IM, TAG + "->getGroupStatus()->groupId:" + groupId);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_groupID", groupId);
            jsonObject.put("_reserved", "");
            mGetGroupStateTimeoutHelper.request((long) groupId, MAX_WAITING_TIME);
            mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_GET_GROUP_STATUS, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取群组状态回调
     *
     * @param result 接口调用结果
     * @param json   返回数据
     */
    public void onGetGroupStatus(int result, String json) {
        Logger.logD(Logger.IM, TAG + "->onGetGroupStatus()->result:" + result + ", json:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int groupId = jsonObject.optInt("_groupID");
            int status = jsonObject.optInt("_status");
            mGetGroupStateTimeoutHelper.cancel((long) groupId);
            for (GroupStateChangeListener listener : mGroupStateChangeListeners) {
                listener.onGetGroupStatusStateChanged(result, groupId, status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新群组状态
     *
     * @param groupId 群组id
     * @param status  状态
     */
    public void updateGroupStatus(int groupId, int status) {
        Logger.logD(Logger.IM, TAG + "->updateGroupStatus()->groupId:" + groupId + ", status:" + status);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_groupID", groupId);
            jsonObject.put("_status", status);
            jsonObject.put("_reserved", "");
            mUpdateGroupStateTimeoutHelper.request((long) groupId, MAX_WAITING_TIME);
            mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_UPDATE_GROUP_STATUS, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新群组状态回调
     *
     * @param result 接口调用结果
     * @param json   返回数据
     */
    public void onUpdGroupStatus(int result, String json) {
        Logger.logD(Logger.IM, TAG + "->onUpdGroupStatus()->result:" + result + ", json:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int groupId = jsonObject.optInt("_groupID");
            int status = jsonObject.optInt("_status");
            mUpdateGroupStateTimeoutHelper.cancel((long) groupId);
            for (GroupStateChangeListener listener : mGroupStateChangeListeners) {
                listener.onUpdGroupStatusStateChanged(result, groupId, status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取群组列表
     *
     * @param opType 操作类型
     */
    public void getGroupList(int opType) {
        Logger.logD(Logger.IM, TAG + "->getGroupList()->opType:" + opType);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_opType", opType);
            jsonObject.put("_reserved", "");
            mDcpManager.getDcpInterface().Request(DcpFuncConfig.FUN_NAME_GET_GROUP_LIST, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拉取群组列表回调
     *
     * @param result 接口请求结果
     * @param json   回调数据
     */
    public void onGetGroupList(int result, String json) {
        Logger.logD(Logger.IM, TAG + "->onGetGroupList()->result:" + result + ", json:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            int opType = jsonObject.optInt("_opType");
            int count = jsonObject.optInt("_count");
            List<Integer> groupList = new ArrayList<>();
            if (count > 0) {
                JSONArray jsonArray = jsonObject.getJSONArray("_groupList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    groupList.add(jsonArray.getInt(i));
                }
            }
            for (GroupStateChangeListener listener : mGroupStateChangeListeners) {
                listener.onGetGroupListStateChanged(result, opType, count, groupList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到新的群组消息
     *
     * @param result 返回码
     * @param json   json数据
     */
    public void onReceiveNewGroupMessage(int result, String json) {
        Logger.logD(Logger.IM, TAG + "->onReceiveNewGroupMessage()->result:" + result + ", json:" + json);
//        json = "{\"_msgContent\":\"{\\\"role\\\":4,\\\"aid\\\":14459,\\\"st\\\":207,\\\"uid \\\":\\\"1002583\\\",\\\"IMid\\\":14459}\",\"_msgID\":8999,\"_msgType\":207,\"_recverID\":1002583,\"_sendDT\":1504778772,\"_senderID\":1003143,\"_seqID\":10255}";
        if (result == 0) {
            try {
                BaseGroupMessageInfo messageInfo = null;
                JSONObject jsonObject = new JSONObject(json);
                final int messageType = jsonObject.optInt("_msgType");
                switch (messageType) {
                    case BaseGroupMessageInfo.PICTURE_MSG:
                        messageInfo = new PictureGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.TEXT_MSG:
                        messageInfo = new TextGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.VOICE_MSG:
                        messageInfo = new VoiceGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.CARD_MSG:
                        messageInfo = new CardGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.WAITING_ADMISSION_MSG:
                        judgeRole(json);
                        return;

                    case BaseGroupMessageInfo.VIDEO_DATE_MSG:
                        messageInfo = new VideoDateGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.MEMBER_JOIN_MSG:
                        messageInfo = new MemberJoinGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.MEMBER_EXIT_MSG:
                        messageInfo = new MemberExitGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.REJECT_MSG:
                        messageInfo = new RejectGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.TRIAGE_MSG:
                        messageInfo = new TriageGroupMessagInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.ISSUE_ADVICE_MSG:
                        judgeRole(json);
                        return;

                    case BaseGroupMessageInfo.MEDICAL_ADVICE_MSG:
                        judgeRole(json);
                        return;

                    case BaseGroupMessageInfo.CALL_SERVICE_MSG:
                        judgeRole(json);
                        return;

                    case BaseGroupMessageInfo.VIDEO_START_MSG:
                        messageInfo = new VideoStartGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.GUIDE_SET_DATE_MSG:
                        messageInfo = new GuideSetDateGroupMessageInfo().jsonToObject(json);
                        break;

                    case BaseGroupMessageInfo.STATE_CHANGE_MSG:
                        messageInfo = new StateChangeGroupMessageInfo().jsonToObject(json);
                        break;

                    default:
                        if (AppConfig.IS_DEBUG_MODE) {
                            throw new RuntimeException("不支持的消息类型，请检查消息类型是否正确！");
                        }
                        break;
                }

                saveReceiverGroupMessage(messageInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断角色
     *
     * @param json 返回json
     * @throws JSONException
     */
    private void judgeRole(final String json) throws JSONException {
        final JSONObject jsonObject = new JSONObject(json);
        JSONObject contentJSONObject = new JSONObject(jsonObject.optString("_msgContent"));
        final int appointmenId = contentJSONObject.optInt("aid", 0);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                GetChatMemberListRequester requester = new GetChatMemberListRequester(new DefaultResultCallback<List<MemberListInfo>>() {
                    @Override
                    public void onSuccess(List<MemberListInfo> memberListInfos, BaseResult baseResult) {
                        int userId = mUserInfoManager.getCurrentUserInfo().getUserId();
                        for (int i = 0; i < memberListInfos.size(); i++) {
                            if (memberListInfos.get(i).getUserId() == userId) {
                                int role = memberListInfos.get(i).getRole();
                                onReceiverNeedJudgeRoleGroupMessage(role, json);
                                break;
                            }
                        }
                    }
                });
                requester.appointmentId = appointmenId;
                requester.doPost();
            }
        });

    }

    /**
     * 收到需要判断角色的群组消息
     *
     * @param role 角色
     * @param json 返回json
     */
    private void onReceiverNeedJudgeRoleGroupMessage(int role, String json) {
        if (role == -1) {
            Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->角色错误！");
            return;
        }

        Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->我的角色：" + role);
        BaseGroupMessageInfo messageInfo = null;
        try {
            final JSONObject jsonObject = new JSONObject(json);
            int messageType = jsonObject.optInt("_msgType");
            switch (messageType) {
                case BaseGroupMessageInfo.WAITING_ADMISSION_MSG:
                    if (role != AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
                        Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->你不是大医生，收到等待就诊消息不予处理！");
                        return;
                    }
                    messageInfo = new WaitingAddmissionGroupMessageInfo().jsonToObject(json);
                    break;

                case BaseGroupMessageInfo.ISSUE_ADVICE_MSG:
                    if (role != AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
                        Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->你不是大医生，收到完成就诊消息不予处理！");
                        return;
                    }
                    messageInfo = new IssueAdviceGroupMessageInfo().jsonToObject(json);
                    break;

                case BaseGroupMessageInfo.MEDICAL_ADVICE_MSG:
                    if (role != AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR
                            && role != AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                        Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->你不是首诊医生或者患者，收到下发报告消息不予处理！");
                        return;
                    }
                    messageInfo = new MedicalAdviceGroupMessageInfo().jsonToObject(json);
                    break;

                case BaseGroupMessageInfo.CALL_SERVICE_MSG:
                    if (role != AppConstant.IMGroupRole.IM_GROUP_ROLE_ASSISTANT_DOCTOR) {
                        Logger.logD(Logger.IM, TAG + "->onReceiverNeedJudgeRoleGroupMessage()->你不是导医，收到呼叫请求消息不予处理！");
                        return;
                    }
                    messageInfo = new CallServiceGroupMessageInfo().jsonToObject(json);
                    break;

                default:
                    if (AppConfig.IS_DEBUG_MODE) {
                        throw new RuntimeException("不支持的消息类型，请检查消息类型是否正确！");
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveReceiverGroupMessage(messageInfo);
    }

    /**
     * 保存接收到的消息
     *
     * @param baseGroupMessageInfo 群组消息
     */
    private void saveReceiverGroupMessage(final BaseGroupMessageInfo baseGroupMessageInfo) {
        Logger.logD(Logger.IM, TAG + "->saveReceiverGroupMessage()->baseGroupMessageInfo:" + baseGroupMessageInfo);

        if (baseGroupMessageInfo == null) {
            return;
        }

        if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_EXIT_MSG) {
            if (((MemberExitGroupMessageInfo) baseGroupMessageInfo).getUserId() == mUserInfoManager.getCurrentUserInfo().getUserId()) {
                for (GroupMessageStateChangeListener listener : mGroupMessageStateChangeListeners) {
                    listener.onReceiveNewGroupMessageStateChanged(baseGroupMessageInfo);
                }
            }
        } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.STATE_CHANGE_MSG) {
            for (GroupMessageStateChangeListener listener : mGroupMessageStateChangeListeners) {
                listener.onReceiveNewGroupMessageStateChanged(baseGroupMessageInfo);
            }
        } else {
            mDBManager.submitDatabaseTask(new DatabaseTask.SimpleDatabaseTask<BaseGroupMessageInfo>() {
                @Override
                public AsyncResult<BaseGroupMessageInfo> runOnDBThread(AsyncResult<BaseGroupMessageInfo> asyncResult, DBHelper dbHelper) {
                    if (mCurentImId == baseGroupMessageInfo.getImId()) {
                        baseGroupMessageInfo.setMsgState(BaseGroupMessageInfo.STATE_READED);
                    } else {
                        baseGroupMessageInfo.setMsgState(BaseGroupMessageInfo.STATE_UNREAD);
                    }
                    saveGroupMessage(baseGroupMessageInfo, dbHelper);
                    asyncResult.setData(baseGroupMessageInfo);
                    Logger.logD(Logger.IM, TAG + "->saveReceiverGroupMessage()->收到新的群组消息，经过处理后的消息为:" + baseGroupMessageInfo);
                    return asyncResult;
                }

                @Override
                public void runOnUIThread(AsyncResult<BaseGroupMessageInfo> asyncResult) {
                    for (GroupMessageStateChangeListener listener : mGroupMessageStateChangeListeners) {
                        listener.onReceiveNewGroupMessageStateChanged(asyncResult.getData());
                    }

                    if (mCurentImId != asyncResult.getData().getImId()) {
                        mLocalNotificationManager.showGroupMessageNotification(asyncResult.getData());
                    }
                }
            });
        }
    }

    /**
     * 将历史消息转换为基本群组消息
     *
     * @param chatMsgInfo 历史消息
     * @return 基本群组消息
     */
    public BaseGroupMessageInfo getBaseGroupMessageInfo(ChatMsgInfo chatMsgInfo) {
        BaseGroupMessageInfo messageInfo = null;
        try {
            switch (chatMsgInfo.getMsgType()) {
                case BaseGroupMessageInfo.PICTURE_MSG:
                    messageInfo = new PictureGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.TEXT_MSG:
                    messageInfo = new TextGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.VOICE_MSG:
                    messageInfo = new VoiceGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.CARD_MSG:
                    messageInfo = new CardGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.WAITING_ADMISSION_MSG:
                    messageInfo = new WaitingAddmissionGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.VIDEO_DATE_MSG:
                    messageInfo = new VideoDateGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.MEMBER_JOIN_MSG:
                    messageInfo = new MemberJoinGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.MEMBER_EXIT_MSG:
                    messageInfo = new MemberExitGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.REJECT_MSG:
                    messageInfo = new RejectGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.TRIAGE_MSG:
                    messageInfo = new TriageGroupMessagInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.ISSUE_ADVICE_MSG:
                    messageInfo = new IssueAdviceGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.MEDICAL_ADVICE_MSG:
                    messageInfo = new MedicalAdviceGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.CALL_SERVICE_MSG:
                    messageInfo = new CallServiceGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.VIDEO_START_MSG:
                    messageInfo = new VideoStartGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.GUIDE_SET_DATE_MSG:
                    messageInfo = new GuideSetDateGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                case BaseGroupMessageInfo.STATE_CHANGE_MSG:
                    messageInfo = new StateChangeGroupMessageInfo().objectToObject(chatMsgInfo);
                    break;

                default:
                    Logger.logD(Logger.IM, TAG + "->getBaseGroupMessageInfo()->不支持的消息类型，请检查消息类型是否正确！");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return messageInfo;
    }

    @Override
    public void onTimeout(TimeoutHelper<Long> timeoutHelper, Long taskId) {
        Logger.logD(Logger.IM, TAG + "->onTimeout()->taskId:" + taskId);
        timeoutHelper.cancel(taskId);
        switch (timeoutHelper.getId()) {
            case TIMER_ID_SEND_GROUP_MESSAGE:
                updateGroupMessageState(taskId, BaseGroupMessageInfo.STATE_FAILED);

                GroupMessageSendStateChangeListener sendStateChangeListener = mGroupMessageSendStateChangeListeners.remove(taskId);
                if (sendStateChangeListener != null) {
                    sendStateChangeListener.onSendStateChanged(-1, taskId);
                }

                for (GroupMessageStateChangeListener listener : mGroupMessageStateChangeListeners) {
                    listener.onSendGroupMsgStateChanged(-1, null);
                }
                break;

            case TIMER_ID_GET_GROUP_STATE:
                for (GroupStateChangeListener listener : mGroupStateChangeListeners) {
                    listener.onGetGroupStatusStateChanged(-1, Integer.valueOf(taskId + ""), 0);
                }
                break;

            case TIMER_ID_UPDATE_GROUP_STATE:
                for (GroupStateChangeListener listener : mGroupStateChangeListeners) {
                    listener.onUpdGroupStatusStateChanged(-1, Integer.valueOf(taskId + ""), 0);
                }
                break;
        }
    }

    public interface GroupMessageSendStateChangeListener {
        void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo);

        void onFileUploadStateChanged(long seqId, int code, String fileName);

        void onSendStateChanged(int result, long seqId);
    }

    public interface GroupMessageStateChangeListener {
        void onSendGroupMsgStateChanged(int result, BaseGroupMessageInfo baseGroupMessageInfo);

        void onReceiveNewGroupMessageStateChanged(BaseGroupMessageInfo baseGroupMessageInfo);
    }

    public interface GroupStateChangeListener {
        void onGetGroupStatusStateChanged(int result, int groupId, int status);

        void onUpdGroupStatusStateChanged(int result, int groupId, int status);

        void onGetGroupListStateChanged(int result, int opType, int count, List<Integer> groupList);
    }

    public interface GroupMessageUnreadCountStateChangeListener {
        void onGroupMessageUnreadCountStateChanged(int groupId, int count);
    }

    public interface GetLocalGroupMessagesCallback {
        void onGetLocalGroupMessagesStateChanged(List<BaseGroupMessageInfo> messageInfos);
    }

    public interface GetLatestGroupMessageCallback {
        void onGetLatestGroupMessageStateChanged(BaseGroupMessageInfo baseGroupMessageInfo);
    }

    public interface GetUnreadCountCallback {
        void onGetUnreadCountStateChanged(long count);
    }

    public interface GetGroupMessageCommonCallback {
        void onGetGroupMessage(BaseGroupMessageInfo groupMessageInfo);
    }
}
