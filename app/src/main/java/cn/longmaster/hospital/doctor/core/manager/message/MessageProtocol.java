package cn.longmaster.hospital.doctor.core.manager.message;

import android.support.annotation.IntDef;

/**
 * 消息协议类
 * Created by yangyong on 2015/8/3.
 */
public class MessageProtocol {
    public static final String TAG = MessageProtocol.class.getSimpleName();
    //=====================消息状态=====================
    public static final int MSG_STATE_UNSENDED = 0;// 发送失败
    public static final int MSG_STATE_SENDING = 1;// 发送中
    public static final int MSG_STATE_SENDED = 2;// 发送成功
    public static final int MSG_STATE_UNREAD = 3;// 未读
    public static final int MSG_STATE_READED = 4;// 已读


    //=====================余额变化原因=====================
    public static final int SMS_BALANCE_CHANGE_WITHDRAW = 101;//提现到账
    public static final int SMS_BALANCE_CHANGE_DOCTOR_WITHDRAW = 104;//医生提现

    //=====================消息类型=====================
    public static final int SMS_TYPE_DOCTOR_39_ACTIVITY = 30;//39活动

    private static final int SMS_TYPE_NOTIFICATION_BASE = 40;//通知类型消息起始,40–69
    public static final int SMS_TYPE_BALANCE_CHANGE = SMS_TYPE_NOTIFICATION_BASE + 0;//余额变化
    public static final int SMS_TYPE_APPOINTMENT_AFFIRM = SMS_TYPE_NOTIFICATION_BASE + 1;//预约确认
    public static final int SMS_TYPE_APPOINTMENT_MATERIAL_CHECK = SMS_TYPE_NOTIFICATION_BASE + 2;//预约材料审核
    public static final int SMS_TYPE_CONSULTATION_NOTIC = SMS_TYPE_NOTIFICATION_BASE + 3;//就诊提醒
    public static final int SMS_TYPE_RECONSULTATION_NOTIC = SMS_TYPE_NOTIFICATION_BASE + 4;//复诊提醒
    public static final int SMS_TYPE_REPORT_NOTIC = SMS_TYPE_NOTIFICATION_BASE + 5;//报告提醒

    private static final int SMS_TYPE_DOCTOR_BASE = 70;//通知类型消息起始,医生端70–99
    public static final int SMS_TYPE_DOCTOR_TIME_IS_UP = SMS_TYPE_DOCTOR_BASE + 1;//预约时间已到
    public static final int SMS_TYPE_DOCTOR_IMAGE_UNcOMPILE = SMS_TYPE_DOCTOR_BASE + 2;//未完成问诊提醒
    public static final int SMS_TYPE_DOCTOR_CANCEL_ORDER = SMS_TYPE_DOCTOR_BASE + 3;//成功取消预约
    public static final int SMS_TYPE_DOCTOR_ADD_PATIENT = SMS_TYPE_DOCTOR_BASE + 4;//医生助理转入患者
    public static final int SMS_TYPE_DOCTOR_REMOVE_PATIENT = SMS_TYPE_DOCTOR_BASE + 5;//医生助理转出患者
    public static final int SMS_TYPE_DOCTOR_CONSULT_CLOSE = SMS_TYPE_DOCTOR_BASE + 6;//患者取消预约，就诊关闭
    public static final int SMS_TYPE_DOCTOR_PAY_SUCESS = SMS_TYPE_DOCTOR_BASE + 7;//远程会诊就诊的患者支付成功
    public static final int SMS_TYPE_DOCTOR_VERIFY_PASS = SMS_TYPE_DOCTOR_BASE + 8;//就诊提供的检查资料审核通过
    public static final int SMS_TYPE_DOCTOR_CLOSE = SMS_TYPE_DOCTOR_BASE + 9;//预约就诊因各种原因在导医平台停止关闭
    public static final int SMS_TYPE_DOCTOR_PLATFORM_PAY = SMS_TYPE_DOCTOR_BASE + 10;//平台打款给医生
    public static final int SMS_TYPE_DOCTOR_CUT_PAY = SMS_TYPE_DOCTOR_BASE + 11;//平台扣款给医生
    public static final int SMS_TYPE_DOCTOR_CALL_FIRST_DOCTOR = SMS_TYPE_DOCTOR_BASE + 12;//上级专家端发起呼叫首诊医生
    public static final int SMS_TYPE_DOCTOR_BALANCE_CHANGE = SMS_TYPE_DOCTOR_BASE + 13;//余额变更
    public static final int SMS_TYPE_DOCTOR_APPOINTMENT_TODAY = SMS_TYPE_DOCTOR_BASE + 14;//今日预约

    public static final int SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS = SMS_TYPE_DOCTOR_BASE + 17;//等待分诊
    public static final int SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS = SMS_TYPE_DOCTOR_BASE + 18;//等待分诊（暂不接诊）
    public static final int SMS_TYPE_DOCTOR_FAST_DIAGNOSIS = SMS_TYPE_DOCTOR_BASE + 19;//尽快分诊
    public static final int SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH = SMS_TYPE_DOCTOR_BASE + 20;//分诊完成
    public static final int SMS_TYPE_DOCTOR_WAIT_RECEIVE = SMS_TYPE_DOCTOR_BASE + 21;//等待接诊
    public static final int SMS_TYPE_DOCTOR_RECEIVE_FINISH = SMS_TYPE_DOCTOR_BASE + 22;//接诊完成
    public static final int SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH = SMS_TYPE_DOCTOR_BASE + 23;//接诊完成
    public static final int SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE = SMS_TYPE_DOCTOR_BASE + 24;//发起医生关联代表。等待接诊
    public static final int SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE = SMS_TYPE_DOCTOR_BASE + 25;//发起医生关联代表。等待分诊
    public static final int SMS_TYPE_MATERIAL_UPLOAD_NOTE = SMS_TYPE_DOCTOR_BASE + 28;//材料上传提醒
    public static final int SMS_TYPE_DOCTOR_ASSESS = SMS_TYPE_DOCTOR_BASE + 27;//专科建设上级对首诊提交日常评估

    public static final int SMS_TYPE_USER_DEFINED_BASE = 100;//自定义短信类型,从100开始
    public static final int SMS_TYPE_COMMON = SMS_TYPE_USER_DEFINED_BASE + 1;//普通短信
    public static final int SMS_TYPE_RECIPE = SMS_TYPE_USER_DEFINED_BASE + 2;//开具处方
    public static final int SMS_TYPE_DOCTOR_CALL = SMS_TYPE_USER_DEFINED_BASE + 3;//医生呼叫
    public static final int SMS_TYPE_PATIENT_REFUSE = SMS_TYPE_USER_DEFINED_BASE + 4;//患者拒绝
    public static final int SMS_TYPE_VISIT_REMINDER = SMS_TYPE_USER_DEFINED_BASE + 5;//就诊提醒
    public static final int SMS_TYPE_PATIENT_REQUEST_VIDEO = SMS_TYPE_USER_DEFINED_BASE + 6;//患者发起视频请求
    public static final int SMS_TYPE_DOCTOR_RESPONSE_VIDEO = SMS_TYPE_USER_DEFINED_BASE + 7;//医生回应视频请求
    public static final int SMS_TYPE_DOCTOR_SKIP_VIDEO = SMS_TYPE_USER_DEFINED_BASE + 8;//医生跳过视频
    public static final int SMS_TYPE_DOCTOR_ASSISTANT_CALL = SMS_TYPE_USER_DEFINED_BASE + 9;//医生助理呼叫
    public static final int SMS_TYPE_DOCTOR_REFUSE = SMS_TYPE_USER_DEFINED_BASE + 10;//医生拒接
    public static final int SMS_TYPE_CALL_CUSTOMER_SERVICE = SMS_TYPE_USER_DEFINED_BASE + 13;//呼叫客服
    public static final int SMS_TYPE_CUSTOMER_SERVICE_REJECT = SMS_TYPE_USER_DEFINED_BASE + 14;//客服拒接

    @IntDef({SMS_TYPE_DOCTOR_BALANCE_CHANGE, SMS_TYPE_DOCTOR_APPOINTMENT_TODAY, SMS_TYPE_DOCTOR_39_ACTIVITY, SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS, SMS_TYPE_DOCTOR_TEMPORARILY_NOT_DIAGNOSIS,
            SMS_TYPE_DOCTOR_FAST_DIAGNOSIS, SMS_TYPE_DOCTOR_DIAGNOSIS_FINISH, SMS_TYPE_DOCTOR_WAIT_RECEIVE, SMS_TYPE_DOCTOR_RECEIVE_FINISH, SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH,
            SMS_TYPE_DOCTOR_WAIT_RECEIVE_REPRESENTATIVE, SMS_TYPE_DOCTOR_WAIT_DIAGNOSIS_REPRESENTATIVE, SMS_TYPE_MATERIAL_UPLOAD_NOTE})
    public @interface MsgCenterType {
    }
}
