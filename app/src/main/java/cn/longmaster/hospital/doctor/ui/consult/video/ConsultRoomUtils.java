package cn.longmaster.hospital.doctor.ui.consult.video;

import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;

/**
 * 视频房间工具类
 * Created by Tengshuxiang on 2016-07-25.
 */
public class ConsultRoomUtils {
    public final static int CALL_RESULT_ALLOW = 0;
    public final static int CALL_RESULT_REFUSE = -1;
    public final static int CALL_TIME_OUT = -2;

    /**
     * 呼叫客服
     *
     * @param senderId
     * @param receiverID
     * @param appointId
     */
    public static void sendCallustomerServiceMessage(int senderId, int receiverID, int appointId, int userType, long seqId) {
        if (receiverID == 0) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_CALL_CUSTOMER_SERVICE);
            jsonObject.put("aid", appointId);
            jsonObject.put("ut", userType);
            jsonObject.put("helpId", seqId);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderId, receiverID, (byte) MessageProtocol.SMS_TYPE_CALL_CUSTOMER_SERVICE, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 大医生
     * 发送呼叫消息
     *
     * @param senderId
     * @param receiverID
     * @param appointId
     */

    public static void sendCallMessage(int senderId, int receiverID, int appointId, int userType, int appointType) {
        if (receiverID == 0) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_DOCTOR_CALL);
            jsonObject.put("uid", senderId);
            jsonObject.put("aid", appointId);
            jsonObject.put("at", appointType);
            jsonObject.put("cd", String.valueOf(System.currentTimeMillis() / 1000));
            jsonObject.put("ut", userType);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderId, receiverID, (byte) MessageProtocol.SMS_TYPE_DOCTOR_CALL, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回应视频请求
     *
     * @param senderId
     * @param receiverID
     * @param appointId
     * @param code
     */
    public static void respondVideoReq(int senderId, int receiverID, int appointId, int code) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_DOCTOR_RESPONSE_VIDEO);
            jsonObject.put("uid", senderId);
            jsonObject.put("aid", appointId);
            jsonObject.put("puid", receiverID);
            jsonObject.put("crr", code);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderId, receiverID, (byte) MessageProtocol.SMS_TYPE_DOCTOR_RESPONSE_VIDEO, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送视频请求通知给大医生
     */
    public static void sendRequestVideoMsg(int userId, int doctorId, int appointId) {
        int recverID = doctorId;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_PATIENT_REQUEST_VIDEO);
            jsonObject.put("uid", recverID);
            jsonObject.put("puid", userId);
            jsonObject.put("aid", appointId);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(userId, Integer.valueOf(recverID), (byte) MessageProtocol.SMS_TYPE_PATIENT_REQUEST_VIDEO, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送跳过视频消息
     *
     * @param senderId   发送者id
     * @param receiverID 接受者id
     * @param appointId  预约id
     * @param num        就诊序号
     */
    public static void sendSkipMessage(int senderId, int receiverID, int appointId, int num) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_DOCTOR_SKIP_VIDEO);
            jsonObject.put("uid", senderId);
            jsonObject.put("puid", receiverID);
            jsonObject.put("aid", appointId);
            jsonObject.put("cno", num);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderId, receiverID, (byte) MessageProtocol.SMS_TYPE_DOCTOR_SKIP_VIDEO, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送拒绝通知给医生
     */
    public static void sendRefuseMsg(int doctorId, int appointId) {
        int senderID = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", MessageProtocol.SMS_TYPE_PATIENT_REFUSE);
            jsonObject.put("uid", senderID);
            jsonObject.put("puid", doctorId);
            jsonObject.put("aid", appointId);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderID, doctorId, (byte) MessageProtocol.SMS_TYPE_PATIENT_REFUSE, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除计时器
     *
     * @param timers 计时器组
     * @param key    计时器类型
     */
    public static void clearTimer(SparseArray<Timer> timers, int key) {
        if (timers.get(key) == null) {
            return;
        }
        timers.get(key).cancel();
        timers.remove(key);
    }

    /**
     * 请求所有计时器
     *
     * @param timers 计时器组
     */
    public static void clearAllTimer(SparseArray<Timer> timers) {
        for (int i = 0; i < timers.size(); i++) {
            timers.get(timers.keyAt(i)).cancel();
        }
        timers.clear();
    }
}


