package cn.longmaster.hospital.doctor.core.manager.common;


import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.BaseManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.ui.consult.consultant.RepresentFunctionActivity;
import cn.longmaster.hospital.doctor.ui.im.ChatActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.user.MessageCenterActivity;
import cn.longmaster.hospital.doctor.ui.user.MyAssessActivity;
import cn.longmaster.utils.NotificationHelper;

/**
 * 本地通知管理类
 * Created by yangyong on 2015/8/6.
 */
public class LocalNotificationManager extends BaseManager {
    public static final int NOTIFICATION_TYPE_VIDEO_ROOM_INVATE = 0;//聊天室邀请通知
    public static final int NOTIFICATION_TYPE_CURE_TIME_REACH = 1;//抵达就诊时间
    public static final int NOTIFICATION_TYPE_STAT_CHANGE = 2;//预约状态改变
    public static final int NOTIFICATION_TYPE_NEW_MESSAGE = 3;//锁屏状态下新消息
    public static final int NOTIFICATION_TYPE_NEW_GROUP_MESSAGE = 4;//锁屏状态下新群组消息
    private static final String channal = "LocalNotificationManager";
    private NotificationHelper helper;
    private Application mApplication;

    @Override
    public void onManagerCreate(AppApplication application) {
        mApplication = application;
        helper = new NotificationHelper(application);
    }

    /**
     * 取消指定类型通知
     *
     * @param type 通知类型
     */
    public void cancleNotification(int type) {
        helper.cancel(type);
    }

    /**
     * 取消所有通知
     */
    public void cancleAllNotification() {
        helper.cancelAll();
    }


    /**
     * 预约状态改变时发送通知
     *
     * @param ap_stat     预约状态
     * @param stat_reason 预约备注
     */
    public void sendAppointmentChangeNotice(int ap_stat, int stat_reason) {
        // TODO 预约状态改变通知
        //如果患者已在视频诊室，则不发送通知
        if (AppApplication.getInstance().isEnterVideoRoom()) {
            return;
        }
        String message = "";
        switch (ap_stat) {
            case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                if (stat_reason == AppConstant.StatReason.WAIT_ASSISTANT_CALL_FINISH) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.WAIT_UPLOAD_DATA:
                if (stat_reason == 0) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.WAIT_DOCTOR_RECEPTION:
                if (stat_reason == 0) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                if (stat_reason == 0) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.DOCTOR_CHANGE_RECEPTION_TIME:
                if (stat_reason == 0) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED:
                if (stat_reason == 0) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.RECURE_ADD_MATERIAL:
                if (stat_reason == 1) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            case AppConstant.AppointmentState.PATIENT_ACCEPT_REPORT:
                if (stat_reason == 1) {
                    message = mApplication.getString(R.string.notification_text_stat_change);
                }
                break;
            default:
                break;
        }
    }

    public void sendNewMessageNotice(boolean isAssess) {
        Intent intent = new Intent();
        if (isAssess) {
            intent.setClass(mApplication, MyAssessActivity.class);
        } else {
            intent.setClass(mApplication, MessageCenterActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = helper.getNotification(NotificationHelper.IMPORTANCE_HIGH)
                .setContentTitle(mApplication.getString(R.string.message_new_message_notice_tips))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher_small);
        helper.notify(NOTIFICATION_TYPE_NEW_MESSAGE, mBuilder);
    }

    public void showGroupMessageNotification(BaseGroupMessageInfo baseGroupMessageInfo) {
        Intent intent = new Intent(mApplication, ChatActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_GROUP_ID, baseGroupMessageInfo.getGroupId());
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_APPOINTMENT_ID, baseGroupMessageInfo.getAppointmentId());
        Bitmap bigIcon = BitmapFactory.decodeResource(mApplication.getResources(), R.mipmap.ic_launcher);
        PendingIntent pendingIntent = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = helper.getNotification(NotificationHelper.IMPORTANCE_HIGH)
                .setContentTitle(mApplication.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setContentText(mApplication.getString(R.string.chat_statusbar_notification_content))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(bigIcon)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_small);
        helper.notify(NOTIFICATION_TYPE_NEW_GROUP_MESSAGE, mBuilder);
    }

    public void showRoundsAppointmentStateChange(BaseMessageInfo baseMessageInfo) {
        try {
            Logger.logD(Logger.ROOM, "onNewMessage()->showRoundsAppointmentStateChange:" + baseMessageInfo.getMsgType());
            Intent intent = new Intent();
            String content = "";
            JSONObject contentJson = new JSONObject(baseMessageInfo.getMsgContent());
            switch (baseMessageInfo.getMsgType()) {
                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS://等待分诊
                case MessageProtocol.SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS://等待分诊（暂不接诊）
                    intent.setClass(mApplication, RepresentFunctionActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_diagnosis, contentJson.getString("hn"), contentJson.getString("dph")));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_FAST_DIAGNOSIS://尽快分诊
                    intent.setClass(mApplication, RepresentFunctionActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_possible_diagnosis, contentJson.getString("hn")));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH://分诊完成
                    intent.setClass(mApplication, RepresentFunctionActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_finish_diagnosis, contentJson.getString("hn"), contentJson.getString("dph")));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE://等待接诊
                    intent.setClass(mApplication, MainActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_have_new_order, contentJson.getInt("aid") + ""));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH://接诊完成：接诊医生
                    intent.setClass(mApplication, MainActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_receive, contentJson.getInt("aid") + ",", contentJson.getString("dt")));
                    break;

                case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH://接诊完成：发起医生
                    intent.setClass(mApplication, MainActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_expert_receive, contentJson.getInt("aid") + "", contentJson.getString("dt")));
                    break;
                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE://发起医生关联代表。等待接诊
                    intent.setClass(mApplication, MainActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_representative_wait_receive_content, contentJson.getString("dcn") + "", contentJson.getInt("aid") + ""));
                    break;
                case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE://起医生关联代表。等待分诊
                    intent.setClass(mApplication, MainActivity.class);
                    showNotification(intent, mApplication.getString(R.string.rounds_message_representative_wait_diagnosis_content, contentJson.getString("dcn") + "", contentJson.getInt("aid") + ""));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Intent intent, String content) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mApplication, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Bitmap bigIcon = BitmapFactory.decodeResource(mApplication.getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder mBuilder = helper.getNotification(NotificationHelper.IMPORTANCE_DEFAULT)
                .setContentTitle(mApplication.getString(R.string.app_name))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(bigIcon)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.mipmap.ic_launcher_small);
        helper.notify(NOTIFICATION_TYPE_NEW_GROUP_MESSAGE, mBuilder);
    }
}
