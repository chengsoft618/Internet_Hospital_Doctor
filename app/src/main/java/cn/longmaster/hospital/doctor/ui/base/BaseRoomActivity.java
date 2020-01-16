package cn.longmaster.hospital.doctor.ui.base;

import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.manager.LocationManager;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.view.SmallVideoView;
import cn.longmaster.hospital.doctor.view.ToastLayoutView;

/**
 * @author ABiao_Abiao
 * @date 2019/12/3 9:09
 * @description:
 */
public abstract class BaseRoomActivity extends NewBaseActivity implements NetStateReceiver.NetworkStateChangedListener, MessageStateChangeListener, SmallVideoView.onSmallVideoClickListener, ToastLayoutView.OnToastPositiveClickListener {
    //强制要求播放低清码流。
    private final int SUBSTREAMSTATEADAPTIVE = 0;

    //由服务器根据丢包率自动切换高清低清。
    private final int SUBSTREAMSTATEON = 1;

    //强制要求接收高清码流
    private final int SUBSTREAMSTATEOFF = 2;

    private final double RATIO_DIFFERENCE = 0.01;

    /**
     * 计时器类型
     */
    private final int TIMER_TYPE_CALL_DOCTOR = 0;
    private final int TIMER_TYPE_CALL_PATIENT = 1;
    //呼叫客服
    private final int TIMER_TYPE_CALL_CUSTOMER_SERVICE = 2;

    private final int TIMER_TYPE_CHAT = 2;
    private final int TIMER_TYPE_RESPOND_DOCTOR = 3;
    private final int TIMER_TYPE_RESPOND_PATIENT = 4;
    private final int TIMER_TYPE_CALLED = 5;
    /**
     * 扬声器开关
     */
    private final int SPEAKER_TYPE_OPEN = 1;
    private final int SPEAKER_TYPE_CLOSE = 0;

    //呼叫等待时间
    private final long CALL_WAIT_DURATION = 60 * 1000;

    //上级响应视频请求时间限制
    private final long DOCTOR_RESPOND_DURATION = 60 * 1000;


    /**
     * 点击呼叫按钮，当roomId不一致时，记录呼叫动作，退出诊室，加入房间，成功后自动呼叫
     */
    //默认状态
    private final int CALL_ACTION_TYPE_DEFAULT = 0;
    //一键呼叫
    private final int CALL_ACTION_TYPE_ALL = 1;
    //呼叫患者
    private final int CALL_ACTION_TYPE_PATIENT = 2;
    //呼叫首诊医生
    private final int CALL_ACTION_TYPE_DOCTOR = 3;
    //大医生点击完成
    private final int CALL_ACTION_TYPE_FINISH = 4;
    //以上角色是大医生
    //被呼叫
    private final int CALL_ACTION_TYPE_CALLED = 5;
    @AppApplication.Manager
    protected UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    protected ConsultRoomManager mConsultRoomManager;
    @AppApplication.Manager
    protected AudioAdapterManager mAudioAdapterManager;
    @AppApplication.Manager
    protected MessageManager mMessageManager;
    @AppApplication.Manager
    protected ConsultManager mConsultManager;
    @AppApplication.Manager
    protected DoctorManager mDoctorManager;
    @AppApplication.Manager
    protected LocationManager mLocationManager;


}
