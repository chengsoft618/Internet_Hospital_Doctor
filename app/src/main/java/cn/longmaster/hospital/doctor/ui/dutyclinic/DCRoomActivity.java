package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.longmaster.video.LMVLog;
import com.longmaster.video.LMVideoMgr;
import com.longmaster.video.VideoRendererGui;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import cn.longmaster.doctorlibrary.util.common.CommonUtils;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.VideoRoomResultCode;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomMember;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomResultInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoTagInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCAdviceEditorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCReferralDetailInfo;
import cn.longmaster.hospital.doctor.core.manager.HTTPDNSManager;
import cn.longmaster.hospital.doctor.core.manager.LocationManager;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.manager.room.VideoRoomInterface;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.LoginStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.receiver.PhoneStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadLocationNetworkRequester;
import cn.longmaster.hospital.doctor.core.requests.dutyclinic.SetDCOrderTempStateRequester;
import cn.longmaster.hospital.doctor.core.requests.user.SubmitDomainNameRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.video.ConsultRoomUtils;
import cn.longmaster.hospital.doctor.ui.consult.video.KickOffActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.dialog.DCDutyRoomPatientListDialog;
import cn.longmaster.hospital.doctor.ui.dutyclinic.view.DoctorChoiceDialog;
import cn.longmaster.hospital.doctor.ui.dutyclinic.view.IssueReferralAndSuggestionDialog;
import cn.longmaster.hospital.doctor.ui.dutyclinic.view.ReferralMakeSureDialog;
import cn.longmaster.hospital.doctor.ui.dutyclinic.view.SuggestionMakeSureDialog;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.util.NetWorkUtil;
import cn.longmaster.hospital.doctor.view.HelpToastView;
import cn.longmaster.hospital.doctor.view.SlideMenu;
import cn.longmaster.hospital.doctor.view.SmallVideoView;
import cn.longmaster.hospital.doctor.view.ToastLayoutView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.InformedConsentDialog;
import cn.longmaster.phoneplus.audioadapter.model.AudioAdapter;
import cn.longmaster.phoneplus.audioadapter.model.AudioModule;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

import static cn.longmaster.hospital.doctor.core.AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR;
import static cn.longmaster.hospital.doctor.core.AppConstant.UserType.USER_TYPE_MDT_DOCTOR;
import static cn.longmaster.hospital.doctor.core.AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;

/**
 * 值班门诊房间
 * <p/>
 * 代码结构：
 * 复写的父类方法{@link DCRoomActivity#onCreate(Bundle)}
 * <p/>
 * 初始化{@link DCRoomActivity#initData()}
 * <p/>
 * 视图点击事件监听{@link DCRoomActivity#onClick(View)}
 * <p/>
 * 房间状态操作相关{@link DCRoomActivity#setPccParam()}
 * <p/>
 * 视频事件回调{@link DCRoomActivity.InnerLMVideoEvents}
 * <p/>
 * 自身状态变化通知{@link DCRoomActivity.InnerRoomSelfStateListener}
 * <p/>
 * 其他成员状态变化通知{@link DCRoomActivity.InnerRoomMemberStateListener}
 *
 * @author yangyong
 */
@SuppressWarnings("ALL")
public class DCRoomActivity extends NewBaseActivity implements NetStateReceiver.NetworkStateChangedListener, MessageStateChangeListener, SmallVideoView.onSmallVideoClickListener, ToastLayoutView.OnToastPositiveClickListener {
    private final String TAG = DCRoomActivity.class.getSimpleName();
    private final int SUBSTREAMSTATEADAPTIVE = 0;//强制要求播放低清码流。
    private final int SUBSTREAMSTATEON = 1;//由服务器根据丢包率自动切换高清低清。
    private final int SUBSTREAMSTATEOFF = 2;//强制要求接收高清码流
    private final double RATIO_DIFFERENCE = 0.01;

    //计时器类型
    private final int TIMER_TYPE_CALL_PATIENT = 1;
    private final int TIMER_TYPE_CALL_CUSTOMER_SERVICE = 2;//呼叫客服
    private final int TIMER_TYPE_CHAT = 2;
    private final int TIMER_TYPE_RESPOND_DOCTOR = 3;
    private final int TIMER_TYPE_RESPOND_PATIENT = 4;
    private final int TIMER_TYPE_CALLED = 5;
    //扬声器开关
    private final int SPEAKER_TYPE_OPEN = 1;
    private final int SPEAKER_TYPE_CLOSE = 0;

    private final long CALL_WAIT_DURATION = 60 * 1000;//呼叫等待时间
    private final long DOCTOR_RESPOND_DURATION = 60 * 1000;//上级响应视频请求时间限制

    /**
     * 点击呼叫按钮，当roomId不一致时，记录呼叫动作，退出诊室，加入房间，成功后自动呼叫
     */
    private final int CALL_ACTION_TYPE_DEFAULT = 0;//默认状态
    private final int CALL_ACTION_TYPE_ALL = 1;//一键呼叫
    private final int CALL_ACTION_TYPE_PATIENT = 2;//呼叫患者
    private final int CALL_ACTION_TYPE_DOCTOR = 3;//呼叫首诊医生
    private final int CALL_ACTION_TYPE_FINISH = 4;//大医生点击完成
    //以上角色是大医生
    private final int CALL_ACTION_TYPE_CALLED = 5;//被呼叫

    @FindViewById(R.id.activity_dcroom_screen_rl)
    private RelativeLayout mScreenRl;
    @FindViewById(R.id.activity_dcroom_bottom_palette_ll)
    private LinearLayout mBottomPaletteLl;
    @FindViewById(R.id.activity_dcroom_hide_show_video_btn)
    private ImageView mHideShowVideoIv;
    @FindViewById(R.id.activity_dcroom_small_window_ll)
    private LinearLayout mSmallWindowLl;
    @FindViewById(R.id.activity_dcroom_small_video_scv)
    private HorizontalScrollView mHorizontalScrollView;
    @FindViewById(R.id.activity_dcroom_small_video_ll)
    private LinearLayout mVideoLl;
    @FindViewById(R.id.activity_dcroom_exit_room_tv)
    private TextView mExitRoomTv;
    @FindViewById(R.id.activity_dcroom_time_tv)
    private TextView mTimeTv;
    @FindViewById(R.id.act_dcroom_record_iv)
    private ImageView mRecordTv;
    //大视频信息
    @FindViewById(R.id.activity_dcroom_big_video_rl)
    private RelativeLayout mBigVideoRl;
    @FindViewById(R.id.activity_dcroom_mode_voice_tip_tv)
    private TextView mVoiceTipTv;
    @FindViewById(R.id.activity_dcroom_user_avatar_iv)
    private ImageView mUserAvatarIv;
    @FindViewById(R.id.activity_dcroom_user_type_tv)
    private TextView mUserTypeTv;
    @FindViewById(R.id.activity_dcroom_name_tv)
    private TextView mUserNameTv;
    @FindViewById(R.id.activity_dcroom_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.activity_dcroom_network_iv)
    private ImageView mBigVideoSignalIv;
    @FindViewById(R.id.activity_dcroom_voice_iv)
    private ImageView mBigVideoVoiceIv;
    @FindViewById(R.id.activity_dcroom_bottom_sm)
    private SlideMenu mBottomSm;
    @FindViewById(R.id.activity_dcroom_bottom_ll)
    private LinearLayout mBottomLl;
    @FindViewById(R.id.activity_dcroom_speaker_ib)
    private ImageButton mSpeakerIb;
    @FindViewById(R.id.activity_dcroom_mic_ib)
    private ImageButton mMicIb;
    @FindViewById(R.id.activity_dcroom_av_mode_ib)
    private ImageButton mAvModeIb;
    @FindViewById(R.id.activity_dcroom_cam_ib)
    private ImageButton mCamIb;
    @FindViewById(R.id.activity_dcroom_call_tv)
    private TextView mCallTv;
    @FindViewById(R.id.activity_dcroom_bigVideo_info_ll)
    private LinearLayout mBigVideoInfoLl;
    @FindViewById(R.id.activity_dcroom_user_avatar_ll)
    private LinearLayout mUserAvatarLl;
    @FindViewById(R.id.activity_dcroom_toast_tlv)
    private ToastLayoutView mToastTlv;
    @FindViewById(R.id.activity_dcroom_help_toast_tlv)
    private HelpToastView mHelpToastView;
    @FindViewById(R.id.activity_dcroom_members_leave_tv)
    private TextView mMembersLeaveTv;
    @FindViewById(R.id.activity_dcroom_consult_list_fl)
    private FrameLayout mConsultListFl;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultRoomManager mConsultRoomManager;
    @AppApplication.Manager
    private AudioAdapterManager mAudioAdapterManager;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private LocationManager mLocationManager;
    @AppApplication.Manager
    private DCManager mDCManager;
    @AppApplication.Manager
    private DcpManager dcpManager;
    //逻辑控件
    private CountDownTimer mHideBottomTimeCdt;//自动隐藏底部栏计时器
    //逻辑数据
    private Map<VideoTagInfo, VideoInfo> mVideoInfoMap = new HashMap<>();
    private Map<Integer, Integer> mCompereMap = new HashMap<>();
    private AsyncTask<String, String, String> mChatTimerTask;
    private long mChatTime;
    private VideoInfo mBackgroundSwitchVideoInfo;

    //状态变量区
    private boolean mIsSlideInit = false;
    private Boolean mFlagChatTimerOn = false;
    private ProgressDialog mProgressDialog;
    private RecyclerView mCallDoctorListRv;
    private IssueReferralAndSuggestionDialog mIssueOrderDialog;

    private GLSurfaceView mBigScreenSv;//大视频窗口
    private CommonDialog mDoctorReqDialog;//首诊医生请求进入诊室dialog
    private CommonDialog mPatientReqDialog;//患者请求进入诊室dialog
    private CommonDialog mConsultationFinishDialog;//结束会诊时首诊医生dialog
    private CommonDialog mCreataRommFailedDialog;//创建房间失败Dialog
    private Dialog mCallDialog;//呼叫Dialog
    private ReferralMakeSureDialog mReferralMakeSureDialog;
    private SuggestionMakeSureDialog mSuggestionMakeSureDialog;
    //视频逻辑变量
    private LMVideoMgr mLMVideoMgr;//视频管理
    private LMVideoMgr.LMVideoEvents mVideoEvents;//视频事件回调监听
    private VideoRoomInterface.OnRoomSelfStateChangeListener mSelfStateChangeListener;//自身房间状态变化通知
    private SparseArray<Timer> mTimers = new SparseArray<>();
    private String mRemoteIP;
    private int mRemotePort;
    private int mVideoSsrc;
    private int mCurrentAvType = ConsultRoomManager.AV_TYPE_VIDEO;
    private int mStateSpeaker = SPEAKER_TYPE_OPEN;
    private boolean mFlagUseBackCam = false;//是否使用后置摄像头
    private boolean mIsMicOpen = false;
    private int mLatestVideoNetState = -1;//底层视频网络状态
    private int mOrderId;

    private DCOrderDetailInfo mOrderDetailInfo;
    private DCReferralDetailInfo mReferralDetailInfo;
    private boolean mIssueable;
    private boolean mIsTempOrder = true;

    private int mRoomId; //当前诊室的房间ID
    @AppConstant.RoomUserType
    private int userType;
    private int mPatientId;
    private int mAttendingDoctorId;//申请医师(首诊医生)
    private int mSuperiorDoctorId; //会诊医师(上级专家)
    private int mCallingPatientId;//正在呼叫的患者ID
    private int mCurrentAppointIndex;//当前预约信息索引
    private SparseArray<Byte> mUserSeats = new SparseArray<>();
    private int currentUserId;
    private int mCallActionType = CALL_ACTION_TYPE_DEFAULT;
    private int mCustomerServiceId = -100;//客服默认id
    private DCDoctorInfo mReceiveDoctor;
    private DoctorBaseInfo mMyDoctorInfo;
    //状态变量区域
    private boolean mFlagHigherDoctor = true;//是否大医生
    private boolean mFlagInRoom = false;//是否在房间
    private boolean mFlagCloseUI = true;//退出房间时是否关闭UI
    private boolean mFlagGetOrder = false;//是否已经获取了预约信息
    private boolean mFlagJoined = false;//是否已经加入过房间
    private boolean mFlagExitRoom = false;//是否退出房间
    private boolean mFlagFinishCurrent = false;//是否结束当前预约
    private boolean mEnableVideo = true;
    private boolean mEnableAudio = true;
    private boolean mCamFrontNormal = true;
    private boolean mCamBackNormal = true;
    private boolean mFlagKickOff = false;//是否被提出房间
    private boolean mFlagShowNetworkDisconnect = true;//是否弹网络连接失败提示
    private int mRawWidth;
    private int mRawHeight;
    private boolean mIsDestroyed = false;
    private boolean mCustomerServiceCalling = false;
    private boolean mEnableAutoAdjust = true;//
    private boolean mOutsideSwitch = true;//外放开关
    private boolean mSwitchCamera = true;
    private boolean mIsIdle = true;//是否空闲，可以去做操作
    private boolean mRoomOperationChangeSeat = false;//被主持人更改席位
    private float mDownLoadLostRate;
    private boolean isAnimationEnd = false;
    private boolean mIsSwitchIngVideo = false;
    private String mPatientName;
    private Animation animShadowAlphaIn, animShadowAlphaOut;
    private long mCurrentSeqId = 0;
    private boolean mHaveCustomerService = false;
    private int mCurrentCustomerServiceId = 0;
    private boolean mIsMeCall = false;
    //呼叫弹窗中的变量
    private String mCallDialogDcotor = "";
    private String mCallDialogHospital = "";
    private String mCallDialogDepartment = "";
    private String mCustomerServiceName;
    private boolean mIsFirsit = true;
    private Map<Long, Double> mRatioMap = new HashMap<>();
    private boolean mInitiativeAdjust = false;//主动调整分辨率
    //是否发送短信给医生
    private boolean isSendMsg = false;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0: //拔出耳机
                        Logger.logD(Logger.ROOM, TAG + "->拔出耳机");
                        //  拔出耳机
                        mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_on));
                        mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKERON);
                        mStateSpeaker = SPEAKER_TYPE_OPEN;
                        mOutsideSwitch = true;
                        break;

                    case 1: //插耳机
                        Logger.logD(Logger.ROOM, TAG + "->插耳机");
                        //  插耳机
                        mOutsideSwitch = false;
                        mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_off));
                        mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKEROFF);
                        mStateSpeaker = SPEAKER_TYPE_CLOSE;
                        break;

                    default:
                        break;
                }
            } else if (intent.getAction().equals(BluetoothHeadset.EXTRA_STATE)) {
                Logger.logD(Logger.ROOM, "广播：" + BluetoothHeadset.EXTRA_STATE);
            } else if (intent.getAction().equals(ExtraDataKeyConfig.ACTION_BROADCAST_RECEIVER_FINISH_VIDEO_ROOM)) {
                goMainActivity();
            }
        }
    };
    private VideoRoomResultInfo mAssistantDoctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLMVideoMgr.resume();
        if (mBackgroundSwitchVideoInfo != null) {
            judgeSwitchVideoView(mBackgroundSwitchVideoInfo.getUseId(), mBackgroundSwitchVideoInfo.getType());
        }
    }

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mReceiveDoctor = (DCDoctorInfo) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_INFO);
        mRoomId = mOrderId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        userType = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, 0);
    }

    @Override
    protected void initDatas() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 关键：添加该句可以调节音量大小
        AppApplication.getInstance().getManager(LocalNotificationManager.class).cancleAllNotification();
        AppApplication.getInstance().setIsEnterVideoRoom(true);
        currentUserId = mUserInfoManager.getCurrentUserInfo().getUserId();
        initLMVideoMgr();
        setPccParam();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_dcroom;
    }

    @Override
    protected void initViews() {
        initView();
        initAnim();
        initListener();
        regBroadcastReceiver();
        initPage();
        initHideBottomTimer();
        showProtocolDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLMVideoMgr.pause();
    }

    @Override
    public void finish() {
        super.finish();
        Logger.logD(Logger.ROOM, TAG + "finish()");
        AppApplication.getInstance().setIsEnterVideoRoom(false);
        mConsultRoomManager.unRegisterMemberChangeListener();
        mConsultRoomManager.unRegisterSelfChangeSeatListener();
        mMessageManager.unRegMsgStateChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mAudioAdapterManager.setMode(AudioModule.NAME_RESET);
        NetStateReceiver.removeNetworkStateChangedListener(this);
        unregisterReceiver(mBroadcastReceiver);
        PhoneStateReceiver.removePhoneListener(mPhoneStateListener);
        pauseChatTimer();
        mHideBottomTimeCdt.cancel();
        mLMVideoMgr = null;
        Logger.logD(Logger.ROOM, TAG + "onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0) {
                joinRoom(mRoomId);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化视频管理类
     */
    private void initLMVideoMgr() {
        mLMVideoMgr = LMVideoMgr.getInstance();
        mLMVideoMgr.initLog(LMVideoMgr.kLevelNolog, LMVideoMgr.kLevelNolog, null);//初始化日志配置
        mLMVideoMgr.init(this);//初始化LMVideoMgr对象
        mVideoEvents = new InnerLMVideoEvents();
    }

    /**
     * 设置音频参数，解决音频兼容性问题
     */
    private void setPccParam() {
        AudioAdapter adapter = mAudioAdapterManager.getAudioAdapter();
        String eq = adapter.getAudioConfig().getEqualizerValue();
        String level = adapter.getAudioConfig().getSamplingRates();
        int sourceType = adapter.getAudioConfig().getRecordSourceType();
        int streamType = adapter.getAudioConfig().getStreamType();
        mConsultRoomManager.setPccParam(eq, level, sourceType, streamType);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBigScreenSv = new GLSurfaceView(this);
        mHideShowVideoIv.setSelected(true);
        mBigVideoRl.setTag(null);
    }

    private void initAnim() {
        animShadowAlphaIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shadow_alpha_in);
        animShadowAlphaOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shadow_alpha_out);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mToastTlv.setPositiveClickListener(this);
        NetStateReceiver.setOnNetworkStateChangedListener(this);
        mMessageManager.regMsgStateChangeListener(this);
        mSelfStateChangeListener = new InnerRoomSelfStateListener();
        mConsultRoomManager.registerMemberChangeListener(new InnerRoomMemberStateListener());
        mConsultRoomManager.registerSelfChangeSeatListener(mSelfStateChangeListener);
    }

    /**
     * 注册广播监听器
     */
    private void regBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(BluetoothHeadset.EXTRA_STATE);
        filter.addAction(ExtraDataKeyConfig.ACTION_BROADCAST_RECEIVER_FINISH_VIDEO_ROOM);
        registerReceiver(mBroadcastReceiver, filter);
        PhoneStateReceiver.addPhoneListener(mPhoneStateListener);
    }

    private void initPage() {
        showProgressDialog();
        mFlagJoined = false;
        mFlagGetOrder = false;

        mDCManager.getDCOrderDetail(mOrderId, new DefaultResultCallback<DCOrderDetailInfo>() {
            @Override
            public void onSuccess(DCOrderDetailInfo dcOrderDetailInfo, BaseResult baseResult) {
                if (dcOrderDetailInfo != null) {
                    mOrderDetailInfo = dcOrderDetailInfo;
                    mFlagGetOrder = true;
                    mBottomPaletteLl.setVisibility(View.VISIBLE);
                    AppHandlerProxy.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!mIsSlideInit) {
                                mIsSlideInit = true;
                                mBottomSm.setScrollLength(mBottomPaletteLl.getHeight());
                            }
                        }
                    });
                    mToastTlv.showToastView(getString(R.string.video_room_state_creating_room));
                    dismissProgressDialog();
                    joinRoom(mRoomId);
                }
            }
        });
    }

    /**
     * 获取医生信息
     *
     * @param doctorId
     */
    private void getDoctorInfo(final int doctorId) {
        mDoctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                Logger.logD(Logger.ROOM, TAG + "->getDoctorInfo()->onGetDoctor()->doctorBaseInfo:" + doctorBaseInfo);
                if (doctorBaseInfo == null) {
                    return;
                }
                String mDoctorName = doctorBaseInfo.getRealName();
                String mHospitalName = doctorBaseInfo.getHospitalName();
                List<VideoInfo> videoInfos = getVideoInfos(doctorId);
                Logger.logD(Logger.ROOM, TAG + "->getDoctorInfo()->onGetDoctor()->videoInfos:" + videoInfos);
                if (videoInfos.size() == 0) {
                    return;
                }

                for (VideoInfo videoInfo : videoInfos) {
                    videoInfo.setUseName(mDoctorName);
                    if (videoInfo.getType() != VideoTagInfo.VIDEO_TYPE_CAMERA) {
                        videoInfo.setHospitalName(mHospitalName);
                    }
                }

                VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                if (bigVideoTagInfo != null
                        && doctorId == bigVideoTagInfo.getUserId() && bigVideoTagInfo.getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                    mUserNameTv.setText(mDoctorName);
                    mHospitalTv.setText(mHospitalName);
                }

                for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                    if (((VideoTagInfo) mVideoLl.getChildAt(i).getTag()).getUserId() == doctorId && ((VideoTagInfo) mVideoLl.getChildAt(i).getTag()).getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                        ((SmallVideoView) mVideoLl.getChildAt(i)).setName(mDoctorName);
                    }
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void reJoinRoom(int callActionType) {
        mCallActionType = callActionType;
        exitVideoRoom();
    }

    private void addMembersJoinVideo(int userId) {
        VideoRoomMember videoRoomMember = new VideoRoomMember();
        videoRoomMember.setUserId(userId);
        int userType = AppConstant.UserType.USER_TYPE_MDT_DOCTOR;
        if (userId == mOrderDetailInfo.getLaunchDoctorId()) {
            userType = USER_TYPE_ATTENDING_DOCTOR;
        } else if (userId == mOrderDetailInfo.getDoctorId()) {
            userType = USER_TYPE_SUPERIOR_DOCTOR;
        }
        videoRoomMember.setUserType(userType);
        VideoInfo videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo == null) {
            callAddOtherVideo(videoRoomMember);
        }
        videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo != null) {
            setVideoState(userId, AppConstant.RoomState.STATE_CALLING);
            videoInfo.setVideoState(AppConstant.RoomState.STATE_CALLING);
        }
    }

    /**
     * 成员是否已在诊室
     *
     * @param id
     * @return
     */
    private boolean isMemberInRoom(int id) {
        VideoInfo videoInfo = getVideoInfo(id, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo == null) {
            return false;
        }
        if (videoInfo.getVideoState() == AppConstant.RoomState.STATE_CALLING
                || videoInfo.getVideoState() == AppConstant.RoomState.STATE_MISS) {
            return false;
        }
        return true;
    }

    /**
     * 初始化底部操作区域隐藏的计时器
     */
    private void initHideBottomTimer() {
        mHideBottomTimeCdt = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                mBottomSm.toggle();
            }
        }.start();
    }

    /**
     * 重置底部操作区域隐藏的计时器
     * 当点过底部按钮和相关操作后重新计时，以免在操作过程中计时结束而收起
     */
    private void reStartHideBottomTimer() {
        mHideBottomTimeCdt.cancel();
        mHideBottomTimeCdt.start();
    }

    /**
     * 设置角色头像和名称
     *
     * @param userType
     */
    public void setUserType(VideoInfo videoInfo) {
        int userType = videoInfo.getUserType();
        int videoType = videoInfo.getType();
        if (videoType == VideoTagInfo.VIDEO_TYPE_CAMERA) {
            mUserTypeTv.setVisibility(View.VISIBLE);
        } else {
            mUserTypeTv.setVisibility(View.GONE);
        }
        if (userType == USER_TYPE_ATTENDING_DOCTOR) {
            mUserAvatarIv.setImageResource(R.drawable.ic_big_video_first_expert);
            mUserTypeTv.setText("呼叫方");
        } else {
            mUserAvatarIv.setImageResource(R.drawable.ic_big_video_super_expert);
            mUserTypeTv.setText("接通方");
        }
    }

    private String getUserTypeTitle(int userType) {
        String title = userType == USER_TYPE_ATTENDING_DOCTOR ? "呼叫方" : "接通方";
        return title;
    }

    /**
     * 设置大视频信号图标
     *
     * @param signalState
     */
    public void setBigVideoSignal(int signalState) {
        if (signalState == AppConstant.SignalState.SIGNAL_BAD) {
            mBigVideoSignalIv.setImageResource(R.drawable.ic_big_video_signal_bad);
        } else if (signalState == AppConstant.SignalState.SIGNAL_GENERAL) {
            mBigVideoSignalIv.setImageResource(R.drawable.ic_big_video_signal_general);
        } else {
            mBigVideoSignalIv.setImageResource(R.drawable.ic_big_video_signal_good);
        }
    }

    /**
     * 设置视频信号
     *
     * @param userId      用户id
     * @param signalState 信号状态
     */
    private void setVideoSignal(int userId, int signalState) {
        VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (videoTagInfo == null) {
            return;
        }

        if (videoTagInfo.getUserId() == userId) {
            setBigVideoSignal(signalState);
        } else {
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo smallVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if (smallVideoTagInfo == null) {
                    continue;
                }
                if (smallVideoTagInfo.getUserId() == userId) {
                    ((SmallVideoView) mVideoLl.getChildAt(i)).setSignal(signalState);
                    break;
                }
            }
        }
    }

    /**
     * 成员切换音视频模式
     *
     * @param userId       用户id
     * @param remoteAvType AV模式
     */
    private void memberChangeAvType(int userId, int remoteAvType) {
        Logger.logD(Logger.ROOM, TAG + "->memberChangeAvType()->userId:" + userId + ", remoteAvType:" + remoteAvType);
        int state = remoteAvType == ConsultRoomManager.AV_TYPE_VIDEO ? AppConstant.RoomState.STATE_BEING_VIDEO : AppConstant.RoomState.STATE_BEING_VOICE;
        VideoInfo videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo == null) {
            return;
        }
        videoInfo.setVideoState(state);
        if (remoteAvType == ConsultRoomManager.AV_TYPE_VIDEO) {
            if (videoInfo.getSsrc() != 0) {
                setVideoDisPlay(videoInfo);
            } else {
                VideoRoomMember videoRoomMember = new VideoRoomMember();
                videoRoomMember.setUserId(userId);
                videoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_CAMERA);
                List<VideoRoomMember> ssrcList = new ArrayList<>();
                ssrcList.add(videoRoomMember);
                mConsultRoomManager.videoSubscribe(ssrcList, mSelfStateChangeListener);
            }
        }
        VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (bigVideoTagInfo != null
                && bigVideoTagInfo.getUserId() == userId) {
            setBigVideoState(state);
        }
        for (int i = 0; i < mVideoLl.getChildCount(); i++) {
            VideoTagInfo smallVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
            if (smallVideoTagInfo.getUserId() == userId) {
                ((SmallVideoView) mVideoLl.getChildAt(i)).setRoomState(mCurrentAvType, state);
                break;
            }
        }
    }

    /**
     * 设置是否显示语音图标
     *
     * @param isShow true 显示
     */
    public void setShowBigVideoVoice(boolean isShow) {
        this.mBigVideoVoiceIv.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置视频语音图标是否显示
     *
     * @param userId
     * @param isShow
     */
    private void setVideoVoice(int userId, boolean isShow) {
        VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (videoTagInfo != null
                && videoTagInfo.getUserId() == userId) {
            setShowBigVideoVoice(isShow);
        }
        for (int i = 0; i < mVideoLl.getChildCount(); i++) {
            VideoTagInfo smallVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
            if (smallVideoTagInfo.getUserId() == userId) {
                ((SmallVideoView) mVideoLl.getChildAt(i)).setShowVoice(isShow);
                break;
            }
        }
    }

    /**
     * 设置指定窗口的视频状态
     *
     * @param userId
     * @param state
     */
    private void setVideoState(int userId, int state) {
        VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (videoTagInfo != null
                && videoTagInfo.getUserId() == userId) {
            setBigVideoState(state);
        } else {
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo smallVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if (smallVideoTagInfo.getUserId() == userId) {
                    ((SmallVideoView) mVideoLl.getChildAt(i)).setRoomState(mCurrentAvType, state);
                    if (null != mReceiveDoctor && mReceiveDoctor.getUserId() == userId && state == AppConstant.RoomState.STATE_MISS) {
                        AppExecutors.getInstance().networkIO().execute(() -> dcpManager.getUserOnLineState(currentUserId, userId, new DcpManager.OnUserOnlineStateLoadListener() {
                            @Override
                            public void onLoad(int state, int userId) {
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (state == 0 || state == 2) {
                                            showSendMsgDialog(mReceiveDoctor);
                                        } else {
                                            showNoteMsgDialog();
                                        }
                                    }
                                });

                            }
                        }));
                    }
                    break;
                }
            }
        }
    }

    private void showNoteMsgDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage("本次呼叫对方未接通，您可以点击对方视频窗口重新呼叫或者呼叫其他值班医生")
                .setNegativeButton("确定", new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {

                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    private void showSendMsgDialog(DCDoctorInfo dcDoctorInfo) {
        if (!isSendMsg) {
            new CommonDialog.Builder(getThisActivity())
                    .setMessage("该医生不在线，是否向" + dcDoctorInfo.getRealName() + "医生发送提醒短信，通知对方上线视频沟通？")
                    .setPositiveButton(R.string.cancel, new CommonDialog.OnPositiveBtnClickListener() {
                        @Override
                        public void onPositiveBtnClicked() {

                        }
                    })
                    .setNegativeButton("确定", new CommonDialog.OnNegativeBtnClickListener() {
                        @Override
                        public void onNegativeBtnClicked() {
                            mDoctorManager.getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
                                @Override
                                public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                                    Logger.logD(TAG, "#showSendMsgDialog#onNegativeBtnClicked");
                                    mDCManager.sendCommendMsg(dcDoctorInfo.getUserId(), doctorBaseInfo.getRealName() + "医生现在向您发起门诊视频沟通，打开「39医生工作站」可以与该医生立即沟通", new DefaultResultCallback<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid, BaseResult baseResult) {
                                            isSendMsg = true;
                                            ToastUtils.showShort("短信发送成功");
                                        }
                                    });
                                }

                                @Override
                                public void onFailed(int code, String msg) {

                                }

                                @Override
                                public void onFinish() {

                                }
                            });

                        }
                    })
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .show();
        }
    }

    /**
     * 设置所有视频的状态
     */
    private void setAllVideoState() {
        VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        VideoInfo bigVideoInfo = null;
        if (bigVideoTagInfo != null) {
            bigVideoInfo = getVideoInfo(bigVideoTagInfo.getUserId(), bigVideoTagInfo.getType());
        }
        if (bigVideoInfo != null) {
            setBigVideoState(bigVideoInfo.getVideoState());
        }
        for (int i = 0; i < mVideoLl.getChildCount(); i++) {
            VideoTagInfo smallVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
            if (smallVideoTagInfo == null) {
                continue;
            }
            VideoInfo smallVideoInfo = getVideoInfo(smallVideoTagInfo.getUserId(), smallVideoTagInfo.getType());
            if (smallVideoInfo == null) {
                continue;
            }
            int state = smallVideoInfo.getVideoState();
            if (state != AppConstant.RoomState.STATE_CALLING
                    && state != AppConstant.RoomState.STATE_MISS) {
                ((SmallVideoView) mVideoLl.getChildAt(i)).setRoomState(mCurrentAvType, state);
            }
        }
    }

    /**
     * 设置大视频状态
     *
     * @param state
     */
    private void setBigVideoState(int state) {
        switch (state) {
            case AppConstant.RoomState.STATE_CALLING:
                mBigVideoRl.setVisibility(View.VISIBLE);
                break;

            case AppConstant.RoomState.STATE_BEING_VIDEO:
                if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VOICE) {
                    mBigVideoRl.setVisibility(View.GONE);
                    mVoiceTipTv.setVisibility(View.VISIBLE);
                    mVoiceTipTv.setText(getString(R.string.consult_room_voice_mode_voice_tip));
                } else {
                    mBigVideoRl.setVisibility(View.VISIBLE);
                    mVoiceTipTv.setVisibility(View.GONE);
                }
                break;

            case AppConstant.RoomState.STATE_BEING_VOICE:
                mBigVideoRl.setVisibility(View.GONE);
                mVoiceTipTv.setVisibility(View.VISIBLE);
                if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VOICE) {
                    mVoiceTipTv.setText(getString(R.string.consult_room_voice_mode_voice_tip));
                } else {
                    mVoiceTipTv.setText(getString(R.string.consult_room_voice_mode_other_party_voice_tip));
                }
                break;

            default:
                mBigVideoRl.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 重新设置状态
     */
    private void resetState() {
        ConsultRoomUtils.clearAllTimer(mTimers);
        setVideoState(currentUserId, AppConstant.RoomState.STATE_BEING_VIDEO);
        mCustomerServiceCalling = false;
        mIsMicOpen = false;
        mVideoInfoMap.clear();
        mUserSeats.clear();
        mStateSpeaker = 1;
        mBigVideoRl.setTag(null);
        mBigVideoRl.removeAllViews();
        mVideoLl.removeAllViews();
        mBigVideoInfoLl.setVisibility(View.GONE);
    }

    /**
     * 添加所有视频
     *
     * @param videoRoomMemberList
     */
    private void addVideos(List<VideoRoomMember> videoRoomMemberList) {
        addSelfVideo();
        //优先设置屏幕共享或播放视频文件为大屏
        for (VideoRoomMember videoRoomMember : videoRoomMemberList) {
            if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_SCREEN_SHARE)) {
                addBigVideo(videoRoomMember);
                break;
            }
            if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_PLAY_VIDEO_FILE)) {
                addBigVideo(videoRoomMember);
                break;
            }
        }
        //其次优先设置主屏为大屏
        if (mBigVideoRl.getTag() == null) {
            for (VideoRoomMember videoRoomMember : videoRoomMemberList) {
                if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_MAIN_SCREEN)) {
                    addBigVideo(videoRoomMember);
                    break;
                }
            }
        }

        for (VideoRoomMember videoRoomMember : videoRoomMemberList) {
            if (videoRoomMember.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
                mToastTlv.showToastView(getString(R.string.video_room_state_entered_room, getString(R.string.consult_room_doctor_assistant)));
            }

            if (videoRoomMember.getUserType() != AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                if (mBigVideoRl.getTag() == null || videoRoomMember.getUserId() != ((VideoTagInfo) mBigVideoRl.getTag()).getUserId()) {
                    int videoType = VideoTagInfo.VIDEO_TYPE_CAMERA;
                    addOtherVideo(videoRoomMember, videoType);
                    if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_SCREEN_SHARE)) {
                        videoType = VideoTagInfo.VIDEO_TYPE_SHARE;
                        addOtherVideo(videoRoomMember, videoType);
                    } else if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_PLAY_VIDEO_FILE)) {
                        videoType = VideoTagInfo.VIDEO_TYPE_VIDEO_FILE;
                        addOtherVideo(videoRoomMember, videoType);
                    }
                } else {
                    int videoType = VideoTagInfo.VIDEO_TYPE_CAMERA;
                    if (((VideoTagInfo) mBigVideoRl.getTag()).getType() != VideoTagInfo.VIDEO_TYPE_CAMERA) {
                        addOtherVideo(videoRoomMember, videoType);
                    } else {
                        if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_SCREEN_SHARE)) {
                            videoType = VideoTagInfo.VIDEO_TYPE_SHARE;
                            addOtherVideo(videoRoomMember, videoType);
                        } else if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_PLAY_VIDEO_FILE)) {
                            videoType = VideoTagInfo.VIDEO_TYPE_VIDEO_FILE;
                            addOtherVideo(videoRoomMember, videoType);
                        }
                    }
                }
                mUserSeats.put(videoRoomMember.getUserId(), videoRoomMember.getSeat());
            }
        }
    }

    /**
     * 判断beJudgestatus与judgeStatus是否同种状态
     *
     * @param beJudgestatus 被判断状态
     * @param judgeStatus   判断状态 2、设置主屏幕 8、隐身
     * @return true 同种状态 false 不是同种状态
     */
    private boolean isSameStatus(byte beJudgestatus, int judgeStatus) {
        return (beJudgestatus & judgeStatus) != 0;
    }

    /**
     * 获取视频信息
     *
     * @param userId    用户id
     * @param videoType 视频类型
     * @return 没有找到视频信息时返回空
     */
    private VideoInfo getVideoInfo(int userId, int videoType) {
        Logger.logD(Logger.ROOM, TAG + "->getVideoInfo()->userId:" + userId + ", videoType:" + videoType);
        VideoInfo videoInfo = null;
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (entry.getKey().getUserId() == userId
                    && entry.getKey().getType() == videoType) {
                videoInfo = entry.getValue();
            }
        }
        return videoInfo;
    }

    /**
     * 获取视频信息
     *
     * @param userId 用户id
     * @return 没有找到视频信息时返回空列表
     */
    private List<VideoInfo> getVideoInfos(int userId) {
        List<VideoInfo> videoInfos = new ArrayList<>();
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (entry.getKey().getUserId() == userId) {
                videoInfos.add(entry.getValue());
            }
        }
        return videoInfos;
    }

    private void removeVideoInfo(int userId) {
        List<VideoTagInfo> videoTagInfos = getVideoTagInfos(userId);
        for (VideoTagInfo videoTagInfo : videoTagInfos) {
            mVideoInfoMap.remove(videoTagInfo);
        }
    }

    private void removeVideoInfo(int userId, int videoType) {
        VideoTagInfo videoTagInfo = getVideoTagInfo(userId, videoType);
        if (videoTagInfo != null) {
            mVideoInfoMap.remove(videoTagInfo);
        }
    }

    private List<VideoTagInfo> getVideoTagInfos(int userId) {
        List<VideoTagInfo> videoTagInfos = new ArrayList<>();
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (entry.getKey().getUserId() == userId) {
                videoTagInfos.add(entry.getKey());
            }
        }
        return videoTagInfos;
    }

    private VideoTagInfo getVideoTagInfo(int userId, int videoType) {
        VideoTagInfo videoTagInfo = null;
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (entry.getKey().getUserId() == userId
                    && entry.getKey().getType() == videoType) {
                videoTagInfo = entry.getKey();
                break;
            }
        }
        return videoTagInfo;
    }

    private List<Integer> getMemberList() {
        List<Integer> memberList = new ArrayList<>();
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (memberList.contains(entry.getKey().getUserId())) {
                continue;
            }
            memberList.add(entry.getKey().getUserId());
        }
        return memberList;
    }

    /**
     * 添加自己视频
     */
    private void addSelfVideo() {
        Logger.logD(Logger.ROOM, TAG + "-->addSelfVideo()-->");
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        MyVideoRendererGui rendererGui = new MyVideoRendererGui(glSurfaceView, this);
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setVideoState(AppConstant.RoomState.STATE_BEING_VIDEO);
        videoInfo.setUseId(currentUserId);
        videoInfo.setUserType(getUserType(currentUserId));
        videoInfo.setUseName(mUserInfoManager.getCurrentUserInfo().getUserName());
        videoInfo.setGlSurfaceView(glSurfaceView);
        videoInfo.setRendererGui(rendererGui);
        VideoTagInfo videoTagInfo = new VideoTagInfo(currentUserId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        videoInfo.setType(videoTagInfo.getType());
        mVideoInfoMap.put(videoTagInfo, videoInfo);
        addSmallVideo(videoInfo, VideoTagInfo.VIDEO_TYPE_CAMERA);
    }

    /**
     * 添加其他视频
     *
     * @param videoRoomMember
     */
    private void addOtherVideo(VideoRoomMember videoRoomMember, int videoType) {
        Logger.logD(Logger.ROOM, TAG + "->addOtherVideo()->videoRoomMember:" + videoRoomMember);
        initVideoInfo(videoRoomMember, videoType);
        if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_HIDE)) {
            return;
        }
        if (mBigVideoRl.getTag() == null) {
            initBigVideo(getVideoInfo(videoRoomMember.getUserId(), videoType), videoType);
        } else {
            addSmallVideo(getVideoInfo(videoRoomMember.getUserId(), videoType), videoType);
        }
        Logger.logD(Logger.ROOM, TAG + "->addOtherVideo()->member size:" + mVideoInfoMap.size());
    }

    /**
     * 只添加大视频
     *
     * @param videoRoomMember
     */
    private void addBigVideo(VideoRoomMember videoRoomMember) {
        int videoType = VideoTagInfo.VIDEO_TYPE_CAMERA;
        if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_SCREEN_SHARE)) {
            videoType = VideoTagInfo.VIDEO_TYPE_SHARE;
        } else if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_PLAY_VIDEO_FILE)) {
            videoType = VideoTagInfo.VIDEO_TYPE_VIDEO_FILE;
        }
        initVideoInfo(videoRoomMember, videoType);

        if (!isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_HIDE)) {
            initBigVideo(getVideoInfo(videoRoomMember.getUserId(), videoType), videoType);
        }
    }

    /**
     * 添加大视频初始化
     */
    private void initBigVideo(VideoInfo videoInfo, int videoType) {
        mBigVideoInfoLl.setVisibility(View.VISIBLE);
        mMembersLeaveTv.setVisibility(View.GONE);
        if (videoInfo.getUserType() == USER_TYPE_ATTENDING_DOCTOR ||
                videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR ||
                videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
            mHospitalTv.setVisibility(View.VISIBLE);
            getDoctorInfo(videoInfo.getUseId());
        } else {
            mHospitalTv.setVisibility(View.GONE);
        }
        if (videoType != VideoTagInfo.VIDEO_TYPE_CAMERA) {
            mHospitalTv.setVisibility(View.GONE);
        }
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(lp);
        rl.setBackgroundColor(getColor(R.color.color_2f2f2f));
        mBigVideoRl.addView(rl);
        if ((videoInfo.getParentRl() != null)) {
            videoInfo.getParentRl().removeAllViews();
            reSetView(videoInfo);
        }
        videoInfo.setParentRl(rl);
        videoInfo.getParentRl().addView(videoInfo.getGlSurfaceView());
        Logger.logI(Logger.ROOM, "initBigVideo-->videoInfo.getGlSurfaceView():" + videoInfo.getGlSurfaceView().getWidth() + "*" + videoInfo.getGlSurfaceView().getHeight());
        setUserType(videoInfo);
        if (videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_MDT_PATIENT) {
            mUserNameTv.setText(getString(R.string.consult_room_patient_family));
        } else if (videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_PATIENT) {
            mUserNameTv.setText(mPatientName);
        } else if (videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
            mUserNameTv.setText(mCustomerServiceName);
        } else {
            mUserNameTv.setText(videoInfo.getUseName());
        }
        if (videoType != VideoTagInfo.VIDEO_TYPE_CAMERA) {
            mUserNameTv.setText(getString(R.string.consult_room_somebody_share, getUserTypeTitle(videoInfo.getUserType())));
        }
        VideoTagInfo videoTagInfo = new VideoTagInfo(videoInfo.getUseId(), videoType);
        mBigVideoRl.setTag(videoTagInfo);
        setBigVideoState(videoInfo.getVideoState());
    }

    private void reSetView(VideoInfo videoInfo) {
        int screenWidth = ScreenUtil.getScreenWidth();
        int screenHeight = ScreenUtil.getScreenHeight();
        int viewWidth = videoInfo.getGlSurfaceView().getWidth();
        int viewHeight = videoInfo.getGlSurfaceView().getHeight();
        double scale;
        if (screenWidth * viewHeight < screenHeight * viewWidth) {
            scale = Float.valueOf(Double.valueOf(screenWidth + "") / Double.valueOf(viewWidth + "") + "");
        } else {
            scale = Float.valueOf(Double.valueOf(screenHeight + "") / Double.valueOf(viewHeight + "") + "");
        }
        int bigViewWidth = (int) (viewWidth * scale);
        int bigViewHeight = (int) (viewHeight * scale);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(bigViewWidth, bigViewHeight);
        lp.topMargin = (screenHeight - bigViewHeight) / 2;
        lp.leftMargin = (screenWidth - bigViewWidth) / 2;
        videoInfo.getGlSurfaceView().setLayoutParams(lp);
    }

    /**
     * 添加其他视频初始化
     */
    private void initVideoInfo(VideoRoomMember videoRoomMember, int videoType) {
        Logger.logD(Logger.ROOM, TAG + "->initVideoInfo()->videoRoomMember:" + videoRoomMember + ", videoType:" + videoType);
        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        MyVideoRendererGui rendererGui = new MyVideoRendererGui(glSurfaceView, this);
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setUseId(videoRoomMember.getUserId());
        videoInfo.setUserType(videoRoomMember.getUserType());
        videoInfo.setGlSurfaceView(glSurfaceView);
        videoInfo.setRendererGui(rendererGui);
        videoInfo.setVideoState(videoRoomMember.getAvType() == ConsultRoomManager.AV_TYPE_VIDEO ? AppConstant.RoomState.STATE_BEING_VIDEO : AppConstant.RoomState.STATE_BEING_VOICE);
        getDoctorInfo(videoInfo.getUseId());
        VideoTagInfo videoTagInfo = new VideoTagInfo(videoRoomMember.getUserId(), videoType);
        videoInfo.setType(videoTagInfo.getType());
        mVideoInfoMap.put(videoTagInfo, videoInfo);
        Logger.logI(Logger.ROOM, TAG + "->initVideoInfo()->key:" + videoInfo + ", value:" + videoTagInfo);
    }

    /**
     * 呼叫添加小视频
     */
    private void callAddOtherVideo(VideoRoomMember videoRoomMember) {
        initVideoInfo(videoRoomMember, VideoTagInfo.VIDEO_TYPE_CAMERA);
        VideoInfo videoInfo = getVideoInfo(videoRoomMember.getUserId(), VideoTagInfo.VIDEO_TYPE_CAMERA);
        addSmallVideo(videoInfo, VideoTagInfo.VIDEO_TYPE_CAMERA);
    }

    /**
     * 添加小视频
     *
     * @param videoInfo
     */
    private void addSmallVideo(VideoInfo videoInfo, int videoType) {
        Logger.logD(Logger.ROOM, TAG + "-->addSmallVideo()-->videoInfo.getParentRl()!=null:" + (videoInfo.getParentRl() != null));
        SmallVideoView smallVideoView = new SmallVideoView(this);
        if ((videoInfo.getParentRl() != null)) {
            videoInfo.getParentRl().removeAllViews();
        }
        videoInfo.setParentRl(smallVideoView.getVideoParentRl());
        smallVideoView.getVideoParentRl().addView(videoInfo.getGlSurfaceView());
        smallVideoView.removeSurfaceView(videoInfo.getParentRl());
        smallVideoView.setTag(getVideoTagInfo(videoInfo.getUseId(), videoType));
        smallVideoView.setSmallVideoClickListener(this);
        smallVideoView.initSmallVideo(mCurrentAvType, videoInfo);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DisplayUtil.dip2px(100), DisplayUtil.dip2px(75));
        lp.setMargins(0, 0, DisplayUtil.dip2px(10), 0);
        mVideoLl.addView(smallVideoView, lp);
//        videoInfo.getGlSurfaceView().setZOrderOnTop(true);
        videoInfo.getGlSurfaceView().setZOrderMediaOverlay(true);
        if (videoInfo.getUserType() == USER_TYPE_ATTENDING_DOCTOR
                || videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_MDT_DOCTOR
                || videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR
                || videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
            getDoctorInfo(videoInfo.getUseId());
        } else if (videoInfo.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
            displayCustomerService(mCustomerServiceId);
        }
    }

    private void displayCustomerService(final int userId) {
        mDoctorManager.getDoctorInfo(userId, true, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null != doctorBaseInfo) {
                    return;
                }
                Logger.logD(Logger.ROOM, TAG + "->displayCustomerService()->doctorBaseInfo:" + doctorBaseInfo);
                mCustomerServiceName = doctorBaseInfo.getRealName();
                List<VideoInfo> videoInfos = getVideoInfos(userId);

                if (videoInfos.size() == 0) {
                    return;
                }

                for (VideoInfo Info : videoInfos) {
                    Info.setUseName(mCustomerServiceName);
                }

                VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                if (bigVideoTagInfo != null && userId == bigVideoTagInfo.getUserId() && bigVideoTagInfo.getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                    mUserNameTv.setText(mCustomerServiceName);
                }

                for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                    if (((VideoTagInfo) mVideoLl.getChildAt(i).getTag()).getUserId() == userId) {
                        ((SmallVideoView) mVideoLl.getChildAt(i)).setName(getString(R.string.consult_help_customer_service_personnel));
                    }
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 移除视频
     *
     * @param userId
     */
    private void removeVideo(int userId, int videoType) {
        Logger.logD(Logger.ROOM, TAG + "->removeVideo()->userId:" + userId + "，videoType:" + videoType);
        VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        VideoInfo bigVideoInfo = null;
        if (bigVideoTagInfo != null
                && bigVideoTagInfo.getUserId() == userId
                && bigVideoTagInfo.getType() == videoType) {
            bigVideoInfo = getVideoInfo(bigVideoTagInfo.getUserId(), bigVideoTagInfo.getType());
        }
        if (bigVideoInfo != null) {
            mBigVideoRl.removeView(bigVideoInfo.getParentRl());
            mBigVideoRl.setTag(null);
            mBigVideoInfoLl.setVisibility(View.GONE);
        }

        for (int i = 0; i < mVideoLl.getChildCount(); i++) {
            VideoTagInfo smllVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
            Logger.logD(Logger.ROOM, TAG + "->removeVideo()->smllVideoTagInfo:" + smllVideoTagInfo);
            if (smllVideoTagInfo != null
                    && smllVideoTagInfo.getUserId() == userId
                    && smllVideoTagInfo.getType() == videoType) {
                mVideoLl.removeView(mVideoLl.getChildAt(i));
            }
        }
        if (mVideoLl.getChildCount() <= 0) {
            mHideShowVideoIv.setSelected(true);
        }
    }

    private void removeMember(int userId, int... videoTypes) {
        showSmallVideo(true);
        Logger.logD(Logger.ROOM, TAG + "->removeMember()->userId:" + userId + ", types:" + videoTypes);
        for (int type : videoTypes) {
            removeVideo(userId, type);
            VideoInfo videoInfo = getVideoInfo(userId, type);
            if (videoInfo == null) {
                continue;
            }
            if (type == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                videoInfo.destoryRendererGui();
            }
            if (videoInfo.getSsrc() != 0) {
                mRatioMap.remove(videoInfo.getSsrc());
            }
            removeVideoInfo(userId, type);
        }
        Logger.logD(Logger.ROOM, TAG + "->removeMember()->member size:" + mVideoInfoMap.size());
        Logger.logD(Logger.ROOM, TAG + "->removeMember()->userId:" + userId + ", isMemberInRoom:" + isMemberInRoom(userId));
    }

    private void judgeSwitchVideoView(final int userId, final int type) {
        if (!isActivityIsForeground()) {
            mBackgroundSwitchVideoInfo = getVideoInfo(userId, type);
            return;
        }
        Logger.logD(Logger.ROOM, TAG + "->judgeSwitchVideoView()->userId:" + userId + ",type:" + type + ",mIsSwitchIngVideo:" + mIsSwitchIngVideo);
        if (mIsSwitchIngVideo) {
            mBackgroundSwitchVideoInfo = getVideoInfo(userId, type);
        } else {
            mBackgroundSwitchVideoInfo = null;
            mIsSwitchIngVideo = true;
            boolean hasVideo = false;
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo smalllVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if (smalllVideoTagInfo != null
                        && smalllVideoTagInfo.getUserId() == userId && smalllVideoTagInfo.getType() == type) {
                    switchVideoView((SmallVideoView) mVideoLl.getChildAt(i));
                    showSwitchVideoToast(userId);
                    hasVideo = true;
                    break;
                }
            }
            if (!hasVideo) {
                mIsSwitchIngVideo = false;
            }
        }
    }

    /**
     * 切换视频界面
     *
     * @param smallView 被点击的小视频用户
     */
    private void switchVideoView(SmallVideoView smallView) {
        mIsSwitchIngVideo = true;
        setEnableSmallViews(false);
        VideoTagInfo smallVideoTagInfo = (VideoTagInfo) smallView.getTag();
        Logger.logI(Logger.ROOM, "switchVideoView-->smallVideoTagInfo:" + smallVideoTagInfo);
        VideoInfo smallVideoInfo = getVideoInfo(smallVideoTagInfo.getUserId(), smallVideoTagInfo.getType());
        if (smallVideoInfo == null || smallVideoInfo.getGlSurfaceView() == null) {
            return;
        }
        if (smallVideoInfo.getSsrc() != 0) {
            mRatioMap.remove(smallVideoInfo.getSsrc());
        }
        VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        VideoInfo bigVideoInfo = null;
        if (bigVideoTagInfo != null) {
            bigVideoInfo = getVideoInfo(bigVideoTagInfo.getUserId(), bigVideoTagInfo.getType());
            if (bigVideoInfo.getSsrc() != 0) {
                mRatioMap.remove(bigVideoInfo.getSsrc());
            }
        }
        mBigVideoRl.setTag(smallVideoTagInfo);

        int[] location = new int[2];
        smallVideoInfo.getGlSurfaceView().getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        smallView.removeSurfaceView(smallVideoInfo.getParentRl());

        if (bigVideoInfo != null
                && bigVideoInfo.getGlSurfaceView() != null) {
            mBigVideoRl.removeView(bigVideoInfo.getParentRl());
            Logger.logD(Logger.ROOM, TAG + "->switchVideoView()->mBigVideoRl.removeView-->yes");
            //小视频
            smallView.initSmallVideo(mCurrentAvType, bigVideoInfo);
            smallView.setTag(bigVideoTagInfo);
//            bigVideoInfo.getGlSurfaceView().setZOrderOnTop(true);
            bigVideoInfo.getGlSurfaceView().setZOrderMediaOverlay(true);
            setVideoQuality(bigVideoInfo, true);
        } else {
            Logger.logD(Logger.ROOM, TAG + "->switchVideoView()->mBigVideoRl.removeView-->no");
            mBigVideoInfoLl.setVisibility(View.VISIBLE);
            mVideoLl.removeView(smallView);
            if (mVideoLl.getChildCount() <= 0) {
                mHideShowVideoIv.setSelected(true);
            }
        }
        if (smallVideoInfo.getUserType() == USER_TYPE_ATTENDING_DOCTOR
                || smallVideoInfo.getUserType() == AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR
                || smallVideoInfo.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
            mHospitalTv.setVisibility(View.VISIBLE);
            mHospitalTv.setText(smallVideoInfo.getHospitalName());
        } else {
            mHospitalTv.setVisibility(View.GONE);
        }
        if (smallVideoInfo.getType() != VideoTagInfo.VIDEO_TYPE_CAMERA) {
            mHospitalTv.setVisibility(View.GONE);
            mUserNameTv.setText(getString(R.string.consult_room_somebody_share, getUserTypeTitle(smallVideoInfo.getUserType())));
        } else {
            mUserNameTv.setText(smallVideoInfo.getUseName());
        }
        //大视频
        setUserType(smallVideoInfo);
        setVideoQuality(smallVideoInfo, false);
        setBigVideo(smallVideoInfo, viewX, viewY);
    }

    /**
     * 设置视频质量
     *
     * @param videoInfo    视频信息
     * @param isLowQuality true->低质量 false->高质量
     */

    private void setVideoQuality(VideoInfo videoInfo, boolean isLowQuality) {
        if (videoInfo.getUseId() != currentUserId && videoInfo.getSsrc() != 0) {
            // 停用老接口，isLowQuality为true时相当于新接口中的SUBSTREAMSTATEON,false相当于SUBSTREAMSTATEADAPTIVE
            // mLMVideoMgr.toggleSubPlayStream(videoInfo.getSsrc(), isLowQuality);
            mLMVideoMgr.setPlayStreamState(videoInfo.getSsrc(), isLowQuality ? SUBSTREAMSTATEON : SUBSTREAMSTATEADAPTIVE);
        }
    }

    /**
     * 获取空闲席位
     *
     * @return
     */
    private byte getSeat() {
        for (byte i = 1; i <= 9; i++) {
            if (mUserSeats.indexOfValue(i) < 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 设置大视频
     *
     * @param videoInfo
     */
    private void setBigVideo(VideoInfo videoInfo, int viewX, int viewY) {
        Logger.logD(Logger.ROOM, TAG + "->setBigVideo()->videoInfo:" + videoInfo);
        mBigVideoRl.setVisibility(View.VISIBLE);
        mVoiceTipTv.setVisibility(View.GONE);
        mEnableAutoAdjust = false;
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(videoInfo.getGlSurfaceView().getLayoutParams());
        margin.setMargins(viewX, viewY, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        mBigVideoRl.removeAllViews();
        mBigVideoRl.addView(videoInfo.getParentRl(), layoutParams);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        videoInfo.getGlSurfaceView().setLayoutParams(lp);
//        videoInfo.getGlSurfaceView().setZOrderOnTop(true);
        videoInfo.getGlSurfaceView().setZOrderMediaOverlay(false);
        smallVideoAnimation(videoInfo, viewX, viewY);
    }

    /**
     * 小视频点击后缩放和移动效果
     *
     * @param videoInfo 视频信息
     * @param viewX     起始x坐标
     * @param viewY     起始y坐标
     */
    private void smallVideoAnimation(final VideoInfo videoInfo, final int viewX, final int viewY) {
        isAnimationEnd = false;
        int screenWidth = ScreenUtil.getScreenWidth();
        int screenHeight = ScreenUtil.getScreenHeight();
        final int viewWidth = videoInfo.getGlSurfaceView().getWidth();
        final int viewHeight = videoInfo.getGlSurfaceView().getHeight();
        final float scale;//缩放大小
        final float moveX;//x方向移动距离
        final float moveY;//y方向移动距离
        if (screenWidth * viewHeight < screenHeight * viewWidth) {
            //宽度缩放小于高度的缩放
            double scaleTemp = Double.valueOf(screenWidth + "") / Double.valueOf(viewWidth + "");
            scale = Float.valueOf(scaleTemp + "");
            moveX = viewX;
            moveY = viewY - (screenHeight - viewHeight * scale) / 2;
        } else {
            double scaleTemp = Double.valueOf(screenHeight + "") / Double.valueOf(viewHeight + "");
            scale = Float.valueOf(scaleTemp + "");
            moveY = viewY;
            moveX = viewX - (screenWidth - viewWidth * scale) / 2;
        }
        Log.i(TAG, "smallVideoAnimation->scale:" + scale);

        //尺寸缩放设置
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, scale, 1f, scale,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //平移设置
        Animation translateAnimation = new TranslateAnimation(1.0f, -moveX + (viewWidth / 2.0f) * (scale - 1),
                1.0f, -moveY + (viewHeight / 2.0f) * (scale - 1));

        final ImageView imageView = new ImageView(getThisActivity());

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.setDuration(400);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Logger.logD(Logger.ROOM, TAG + "-->smallVideoAnimation->onAnimationStart()-->");
                if (videoInfo.getVideoState() == AppConstant.RoomState.STATE_BEING_VIDEO) {
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    videoInfo.getParentRl().addView(imageView, lp);
                    ((MyVideoRendererGui) videoInfo.getRendererGui()).setCutBitmap(viewWidth, viewHeight, new MyVideoRendererGui.OnCutBitmapListener() {
                        @Override
                        public void OnCutBitmapfinished(final Bitmap bmp) {
                            AppHandlerProxy.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isAnimationEnd) {
                                        Logger.logD(Logger.ROOM, TAG + "-->smallVideoAnimation->OnCutBitmapfinished()-->");
                                        if (imageView != null) {
                                            imageView.setImageBitmap(bmp);
                                        } else {
                                            imageView.setImageResource(getCompatColor(R.color.color_2f2f2f));
                                        }
                                    } else {
                                        imageView.setImageResource(getCompatColor(R.color.color_2f2f2f));
                                    }
                                }
                            });
                        }
                    });
                } else {
                    imageView.setImageResource(getColor(R.color.color_2f2f2f));
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    videoInfo.getParentRl().addView(imageView, lp);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationEnd = true;
                Logger.logD(Logger.ROOM, TAG + "-->smallVideoAnimation->onAnimationEnd()-->");
//                int left = (int) (viewX - moveX);
//                int top = (int) (viewY - moveY);
//                int width = (int) (viewWidth * scale);
//                int height = (int) (viewHeight * scale);
                videoInfo.getParentRl().clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                lp.setMargins(left, top, left, top);
                videoInfo.getParentRl().setLayoutParams(lp);
                mEnableAutoAdjust = true;
                setBigVideoState(videoInfo.getVideoState());
                AppHandlerProxy.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        videoInfo.getParentRl().removeView(imageView);
                    }
                }, 100);
                setEnableSmallViews(true);

                if (videoInfo.getUseId() == currentUserId) {
                    mLMVideoMgr.resetCaptureVideoView(videoInfo.getGlSurfaceView(), videoInfo.getRendererGui());
                } else {
                    mInitiativeAdjust = true;
                    mLMVideoMgr.resetDisplayVideoView(videoInfo.getSsrc(), videoInfo.getGlSurfaceView(), videoInfo.getRendererGui());
                }

                mIsSwitchIngVideo = false;
                if (mBackgroundSwitchVideoInfo != null) {
                    judgeSwitchVideoView(mBackgroundSwitchVideoInfo.getUseId(), mBackgroundSwitchVideoInfo.getType());
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        videoInfo.getParentRl().startAnimation(animationSet);
    }


    /**
     * 设置小视屏是否能点击
     *
     * @param isEnable
     */
    private void setEnableSmallViews(boolean isEnable) {
        for (int i = 0; i < mVideoLl.getChildCount(); i++) {
            mVideoLl.getChildAt(i).setEnabled(isEnable);
        }
    }

    private void showSmallVideo(boolean show) {
        int[] location = new int[2];
        mHorizontalScrollView.getLocationOnScreen(location);

        if (show && !isSmallVideoShown()) {
            showHideSmallVideo(-mVideoLl.getWidth());
        } else if (!show && isSmallVideoShown()) {
            showHideSmallVideo(ScreenUtil.getScreenWidth() - location[0]);
            mHorizontalScrollView.scrollTo(0, 0);
        }
    }

    private boolean isSmallVideoShown() {
        return mHideShowVideoIv.isSelected();
    }

    /**
     * 隐藏或显示小视屏
     *
     * @param xDelta 平移距离
     */
    private void showHideSmallVideo(final float xDelta) {
        mHideShowVideoIv.setEnabled(false);
        if (mVideoLl.getChildCount() > 0) {
            PropertyValuesHolder translationX;
            if (xDelta > 0) {
                translationX = PropertyValuesHolder.ofFloat("translationX", 0, xDelta);
            } else {
                translationX = PropertyValuesHolder.ofFloat("translationX", -xDelta, 0);
            }
            ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mSmallWindowLl, translationX);
            animator.setDuration(500);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    setEnableSmallViews(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    setEnableSmallViews(true);
                    mSmallWindowLl.clearAnimation();
                    mHideShowVideoIv.setEnabled(true);
                    mHideShowVideoIv.setSelected(xDelta < 0);
                }
            });
            animator.start();
        } else {
            mHideShowVideoIv.setEnabled(true);
        }
    }

    /*********************************************
     * 视频操作区start
     **************************************************/

    /**
     * 开始视频就诊
     */
    private void joinRoom(final int roomId) {
        Logger.logD(Logger.ROOM, "joinRoom()-->" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO));
        if (NetStateReceiver.hasNetConnected(this)) {
            mAudioAdapterManager.setMode(AudioModule.NAME_PROCESSINCALL);
            Disposable disposable = PermissionHelper
                    .init(getThisActivity())
                    .addPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .requestEachCombined()
                    .subscribe(permission -> {
                        if (permission.granted) {
                            joinVideoRoom(roomId);
                            // `permission.name` is granted !
                        } else {
                            new CommonDialog.Builder(getThisActivity())
                                    .setTitle("权限授予")
                                    .setMessage("在设置-应用管理-权限中开启相机和录音权限，才能正常使用视频就诊")
                                    .setPositiveButton("取消", this::goMainActivity)
                                    .setCancelable(false)
                                    .setNegativeButton("确定", () -> {
                                        Utils.gotoAppDetailSetting();
                                        goMainActivity();
                                    })
                                    .show();
                        }
                    });
            compositeDisposable.add(disposable);
        } else {
            mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
            ToastUtils.showShort(R.string.no_network_connection);
        }
    }

    private void joinVideoRoom(int roomId) {
        if (!mIsIdle) {
            return;
        }
        mIsIdle = false;
        showProgressDialog();
        Logger.logD(Logger.ROOM, "joinVideoRoom()-->roomId:" + roomId);
        mConsultRoomManager.joinVideoRoom(mOrderId, roomId, getUserType(currentUserId), "", NetWorkUtil.getNetworkType(getThisActivity()), mSelfStateChangeListener);
    }

    /**
     * 被移出房间
     */
    private void kickOff() {
        mFlagKickOff = true;
        mConsultRoomManager.exitVideoRoom(mRoomId, mSelfStateChangeListener);
        stopVideo();
        ConsultRoomUtils.clearAllTimer(mTimers);
        mAudioAdapterManager.setMode(AudioModule.NAME_RESET);
        mVideoInfoMap.clear();
        Intent intent = new Intent(getThisActivity(), KickOffActivity.class);
        startActivity(intent);
        goMainActivity();
    }

    /**
     * 拉取成员席位信息
     */
    private void getRoomMemberSeat() {
        mConsultRoomManager.getRoomMember(mRoomId, mSelfStateChangeListener);
    }

    /**
     * 更新席位信息
     *
     * @param resultInfo
     */
    private void updateMemberSeat(VideoRoomResultInfo resultInfo) {
        if (resultInfo == null || resultInfo.getMemberList() == null || resultInfo.getMemberList().size() == 0) {
            return;
        }
        for (VideoRoomMember videoRoomMember : resultInfo.getMemberList()) {
            if (videoRoomMember.getUserType() != AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                mUserSeats.put(videoRoomMember.getUserId(), videoRoomMember.getSeat());
            }
            if (videoRoomMember.getUserId() == currentUserId) {
                mIsMicOpen = videoRoomMember.getSeat() > 0;
                setMicState();
            }
        }
    }

    /**
     * 切换AV模式视图
     */
    private void switchAvModeView() {
        VideoInfo myVideoInfo = getVideoInfo(currentUserId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (myVideoInfo == null) {
            return;
        }
        if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VIDEO) {
            mAvModeIb.setImageDrawable(getDrawable(R.drawable.ic_video_room_video_on));
            myVideoInfo.setVideoState(AppConstant.RoomState.STATE_BEING_VIDEO);
            mLMVideoMgr.startVideoCapture(myVideoInfo.getCaptureConfig());
            setAllVideoState();
            VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
            if (bigVideoTagInfo == null) {
                return;
            }
            VideoInfo bigVideoInfo = getVideoInfo(bigVideoTagInfo.getUserId(), bigVideoTagInfo.getType());
            if (bigVideoInfo == null) {
                return;
            }
            mToastTlv.showToastView(getString(R.string.video_room_state_switch_video_mode));
        } else if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VOICE) {
            mAvModeIb.setImageDrawable(getDrawable(R.drawable.ic_video_room_video_off));
            myVideoInfo.setVideoState(AppConstant.RoomState.STATE_BEING_VOICE);
            mLMVideoMgr.stopVideoCapture();
            setAllVideoState();
            mToastTlv.showToastView(getString(R.string.video_room_state_switch_voice_mode));
        }
    }

    /**
     * 开启视频捕获
     */
    public void startCaptureVideo() {
        VideoInfo myVideoInfo = getVideoInfo(currentUserId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (myVideoInfo == null) {
            return;
        }
        LMVideoMgr.LMVideoCaptureConfig captureConfig = new LMVideoMgr.LMVideoCaptureConfig();
        captureConfig.mVideoView = myVideoInfo.getGlSurfaceView();
        captureConfig.mRender = myVideoInfo.getRendererGui();
        captureConfig.mSurfaceViewContainer = myVideoInfo.getParentRl();
        captureConfig.mVideoResolutionType = LMVideoMgr.kVideoResolutionMedium;//图像分辨率
        captureConfig.mUsingFrontCamera = true;
        captureConfig.mUIOrientation = LMVideoMgr.UIOrientationType.kUIOrientationPortrait;
        captureConfig.mRTPCodecType = 96;//视频编码的类型，100表示vp8编码，98表示h264编码
        captureConfig.mMaxBitRate = 800;//视频编码码率上限
        captureConfig.mMinBitrate = 0;//频编码码率下限
        captureConfig.mFps = 15;
        myVideoInfo.setCaptureConfig(captureConfig);
        mLMVideoMgr.startVideoCapture(captureConfig);
    }

    /**
     * 配置视频参数
     *
     * @param videoInfo 需配置视频的信息
     */
    private void setVideoDisPlay(VideoInfo videoInfo) {
        Logger.logI(Logger.ROOM, TAG + "setVideoDisPlay()->videoInfo:" + videoInfo);
        if (videoInfo == null) {
            return;
        }

        if (videoInfo.getSsrc() != 0) {
            //开启视频播放
            LMVideoMgr.LMVideoDisplayConfig displayConfig = new LMVideoMgr.LMVideoDisplayConfig();
            displayConfig.mVideoView = videoInfo.getGlSurfaceView();
            displayConfig.mRender = videoInfo.getRendererGui();
            displayConfig.mPlayStreamSsrc = videoInfo.getSsrc();
            videoInfo.setDisplayConfig(displayConfig);
            mLMVideoMgr.startVideoDisplay(displayConfig);

            VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
            if (bigVideoTagInfo != null && bigVideoTagInfo.getUserId() == videoInfo.getUseId() && bigVideoTagInfo.getType() == videoInfo.getType()) {
                setVideoQuality(videoInfo, false);
            } else {
                setVideoQuality(videoInfo, true);
            }
        }
    }

    private void startVideoSession() {
        //开启视频会话
        LMVideoMgr.LMVideoSessionConfig sessionConfig = new LMVideoMgr.LMVideoSessionConfig();
        sessionConfig.mRemoteIP = mRemoteIP;
        sessionConfig.mRemoteVideoPort = mRemotePort;
        sessionConfig.mP2pLocalVideoPort = 0;//P2P模式下视频输入流的接收端口（非P2P模式需将此参数置零）
        sessionConfig.mVideoOutputSsrc = mVideoSsrc;//视频输出流的SSRC标识，自己捕获的视频上传给视频服务器时的标识
        mLMVideoMgr.startVideoSession(sessionConfig, mVideoEvents);
    }


    /**
     * 停止视频通话
     */
    public void stopVideo() {
        Logger.logI(Logger.ROOM, "stopVideo-->mLMVideoMgr != null:" + (mLMVideoMgr != null));
        if (mLMVideoMgr != null) {
            mLMVideoMgr.stopVideoSession();
        }
    }

    /**
     * 设置mic状态
     */
    private void setMicState() {
        mMicIb.setImageDrawable(mIsMicOpen ? getDrawable(R.drawable.ic_btn_video_room_mic_on) : getDrawable(R.drawable.ic_btn_video_room_mic_off));
        mConsultRoomManager.pauseMic(!mIsMicOpen);
    }

    /**
     * 变更席位
     *
     * @param seat
     */
    private void changeSeatState(byte seat) {
        if (!mIsIdle) {
            return;
        }
        mIsIdle = false;
        mConsultRoomManager.changeSeatState(seat);
    }

    private void setCamState() {
        mToastTlv.showToastView(getString(mFlagUseBackCam ? R.string.consult_room_toast_change_back_cam : R.string.consult_room_toast_change_front_cam));
    }

    /**
     * 获取网络状态
     *
     * @param v 丢包率
     * @return
     */
    private int getNetState(float v) {
        int state = LMVideoMgr.kVideoNetworkStateVeryGood;
        if (v <= 0.1) {
            state = LMVideoMgr.kVideoNetworkStateVeryGood;
        } else if (v <= 0.5) {
            state = LMVideoMgr.kVideoNetworkStateOK;
        } else {
            state = LMVideoMgr.kVideoNetworkStateNotGood;
        }
//        Logger.logI(Logger.ROOM, "InnerLMVideoEvents->getNetState->v" + v + "->v" + v + "->state:" + state);
        return state;
    }

    /**
     * 查询网络状态
     */
    private void startGetNetState() {
        if (mLMVideoMgr == null) {
            return;
        }
        mLMVideoMgr.getCaptureStreamLostRate();
        for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
            if (entry.getKey().getUserId() != currentUserId) {
                if (entry.getValue().getSsrc() != 0) {
                    mLMVideoMgr.getPlayStreamLostRate(entry.getValue().getSsrc());
                }
            }
        }
    }

    /**
     * 调整surfaceView大小
     *
     * @param glSurfaceView
     * @param videoWidth
     * @param videoHeight
     * @return
     */
    private void adjustSurfaceSize(final GLSurfaceView glSurfaceView, final int videoWidth, final int videoHeight) {
        Logger.logI(Logger.ROOM, "adjustSurfaceSize");
        if (!mEnableAutoAdjust) {
            return;
        }
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                int rawWidth;
                int rawHeight;
                VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                VideoInfo bigVideoInfo = null;
                if (bigVideoTagInfo != null) {
                    bigVideoInfo = getVideoInfo(bigVideoTagInfo.getUserId(), bigVideoTagInfo.getType());
                }
                if (bigVideoInfo != null
                        && glSurfaceView == bigVideoInfo.getGlSurfaceView()) {
                    rawWidth = ScreenUtil.getScreenWidth();
                    rawHeight = ScreenUtil.getScreenHeight();
                } else {
                    rawWidth = DisplayUtil.dip2px(100);
                    rawHeight = DisplayUtil.dip2px(75);
                }
                Logger.logI(Logger.ROOM, "adjustSurfaceSize（）->rawWidth:" + rawWidth + "->rawHeight:" + rawHeight);
                int newWidth = 0;
                int newHeight = 0;
                if (rawWidth * videoHeight > videoWidth * rawHeight) {
                    newHeight = rawHeight;
                    newWidth = videoWidth * newHeight / videoHeight;
                } else {
                    newWidth = rawWidth;
                    newHeight = videoHeight * newWidth / videoWidth;
                }

                RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(newWidth, newHeight);
                viewLp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                glSurfaceView.setLayoutParams(viewLp);
            }
        });
    }


    /**
     * 切换自己的AV模式
     */
    private void changeSelfAvType(int avType) {
        if (avType == ConsultRoomManager.AV_TYPE_VIDEO && !mEnableVideo) {
            mToastTlv.showToastView(getString(R.string.room_no_video_permission));
            return;
        }
        if (avType == ConsultRoomManager.AV_TYPE_VOICE && !mEnableAudio) {
            mToastTlv.showToastView(getString(R.string.room_no_audio_permission));
            return;
        }
        if (!mCamFrontNormal && !mCamBackNormal) {
            mToastTlv.showToastView(getString(R.string.room_camera_abnormal));
            Logger.logI(Logger.ROOM, "changeSelfAvType（）->异常");
            return;
        }
        Logger.logI(Logger.ROOM, "changeSelfAvType（）->切换模式" + avType);
        mConsultRoomManager.changeSelfAVType(mRoomId, avType, mSelfStateChangeListener);
    }

    /**
     * 切换前后摄像头
     */
    public void toggleCamera() {
        if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VIDEO) {
            if (mFlagUseBackCam && !mCamFrontNormal) {
                Logger.logI(Logger.ROOM, "toggleCamera（）->异常");
                mToastTlv.showToastView(getString(R.string.room_camera_abnormal));
                return;
            }
            mFlagUseBackCam = !mFlagUseBackCam;
            setCamState();
            mLMVideoMgr.toggleCamera();
        } else {
            mToastTlv.showToastView(getString(R.string.video_room_state_using_voice_mode));
        }
    }

    /**
     * 退出聊天室
     */
    private void exitVideoRoom() {
        if (mCallActionType == CALL_ACTION_TYPE_DEFAULT && !mFlagInRoom) {
            goMainActivity();
            return;
        }
        Logger.logD(Logger.ROOM, "exitVideoRoom（）->请求退出视频房间");
        if (mOrderId == 0) {
            return;
        }
        if (!mIsIdle) {
            return;
        }
        mIsIdle = false;
        showProgressDialog();
        mFlagExitRoom = true;
        Logger.logD(Logger.ROOM, "exitVideoRoom（）->mFlagExitRoom:" + mFlagExitRoom);
        Logger.logD(Logger.ROOM, "exitVideoRoom（）->mOrderId:" + mOrderId + "->mSelfStateChangeListener:" + mSelfStateChangeListener);
        stopVideo();
        mConsultRoomManager.exitVideoRoom(mRoomId, mSelfStateChangeListener);
    }

    /**
     * 退出房间处理
     */
    private void exitRoomDeal() {
        Logger.logD(Logger.ROOM, "是否退出房间：" + mFlagExitRoom);
        if (!mFlagExitRoom) {
            return;
        }
        dismissProgressDialog();
        ConsultRoomUtils.clearAllTimer(mTimers);
        Logger.logD(Logger.ROOM, "是否退出界面：" + mFlagCloseUI);
        mAudioAdapterManager.setMode(AudioModule.NAME_RESET);
        mVideoInfoMap.clear();
        if (mFlagCloseUI) {
            mIsDestroyed = true;
            goMainActivity();
        } else {
            ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_CHAT);
            if (mFlagHigherDoctor) {
                if (mFlagFinishCurrent) {
                    goMainActivity();
                }
            }
        }
        mFlagExitRoom = false;
        Logger.logD(Logger.ROOM, "exitRoomDeal（）->mFlagExitRoom:" + mFlagExitRoom);
        mFlagCloseUI = true;
    }

    public void startChatTimer() {
        showHideChatTime(true);
        synchronized (mFlagChatTimerOn) {
            mFlagChatTimerOn = true;
        }
        mChatTimerTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                while (true) {
                    SystemClock.sleep(500);
                    synchronized (mFlagChatTimerOn) {
                        if (!mFlagChatTimerOn) {
                            break;
                        }
                    }
                    publishProgress();
                    SystemClock.sleep(500);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                startGetNetState();
                synchronized (mFlagChatTimerOn) {
                    if (mFlagChatTimerOn) {
                        mTimeTv.setText(getHHMMFromSeconds(mChatTime++));
                    }
                }
            }
        }.execute();
    }

    /**
     * 获取格式化的时间
     *
     * @param seconds
     * @return
     */
    private String getHHMMFromSeconds(long seconds) {
        String res = "";
        long min = seconds / 60;
        long sec = seconds % 60;
        res += (min > 9 ? (min) : ("0" + min)) + ":" + (sec > 9 ? (sec) : ("0" + sec));
        return res;
    }

    /**
     * 取消计时
     */
    public void pauseChatTimer() {
        synchronized (mFlagChatTimerOn) {
            mFlagChatTimerOn = false;
        }
        if (mChatTimerTask != null) {
            mChatTimerTask.cancel(true);
        }
    }

    /**
     * 时间显示隐藏
     *
     * @param isShow
     */
    public void showHideChatTime(boolean isShow) {
        mTimeTv.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }


    /*********************************************
     * 视频操作区end
     **************************************************/

    @Override
    public void onNetworkStateChanged(int netWorkType) {
        Logger.logD(Logger.ROOM, "onNetworkStateChanged()网络状态（-1无网络）：" + netWorkType);
        if (netWorkType == NetStateReceiver.NETWORK_TYPE_NONE) {
            stopVideo();
            mFlagInRoom = false;
            if (mFlagShowNetworkDisconnect) {
                Logger.logD(Logger.ROOM, "onNetworkStateChanged()（-1无网络）Toast：");
                mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
                mFlagShowNetworkDisconnect = false;
            }
        }
    }

    @Override
    public void onNewMessage(final BaseMessageInfo baseMessageInfo) {
        Logger.logD(Logger.ROOM, "onNewMessage()->baseMessageInfo:" + baseMessageInfo.toString());
        if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_CALL || baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_ASSISTANT_CALL) {

            //角色：首诊医生，处理被大医生、导医 呼叫
            if (baseMessageInfo.getAppointmentId() != mOrderId) {
                mDCManager.getDCOrderDetail(baseMessageInfo.getAppointmentId(), new DefaultResultCallback<DCOrderDetailInfo>() {
                    @Override
                    public void onSuccess(DCOrderDetailInfo dcOrderDetailInfo, BaseResult baseResult) {
                        if (dcOrderDetailInfo != null) {
                            int userType = -1;
                            try {
                                JSONObject contentJson = new JSONObject(baseMessageInfo.getMsgContent());
                                userType = contentJson.optInt("ut");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            showCallDialog(dcOrderDetailInfo, baseMessageInfo, baseMessageInfo.getSenderID(), userType);
                        }
                    }
                });
            } else {
                Logger.logD(Logger.ROOM, "onNewMessage()->mFlagInRoom:" + mFlagInRoom);
                if (mFlagInRoom) {
                    return;
                }
                joinRoom(mRoomId);
            }
        } else if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_PATIENT_REQUEST_VIDEO) {
            //角色：大医生，处理被首诊医生、患者 发起视频敲门的请求
            if (baseMessageInfo.getAppointmentId() != mOrderId) {
                return;
            }
            showDoctorVideoReqDialog();
            Timer respondDoctorTimer = new Timer();
            respondDoctorTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mDoctorReqDialog.dismiss();
                }
            }, DOCTOR_RESPOND_DURATION);
            mTimers.append(TIMER_TYPE_RESPOND_DOCTOR, respondDoctorTimer);
        } else if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_PATIENT_REFUSE || baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_DOCTOR_REFUSE || baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_CUSTOMER_SERVICE_REJECT) {
            //角色：大医生，处理被首诊医生、患者 拒绝视频呼叫
            Logger.logD(Logger.ROOM, "onNewMessage()->getAppointmentId:" + baseMessageInfo.getAppointmentId() + ",mOrderId:" + mOrderId);
            if (baseMessageInfo.getAppointmentId() != mOrderId) {
                return;
            }
            if (isMemberInRoom(baseMessageInfo.getSenderID())) {
                return;
            }
            Logger.logD(Logger.ROOM, "onNewMessage()->baseMessageInfo:" + baseMessageInfo);
            Logger.logD(Logger.ROOM, "onNewMessage()->SenderID:" + baseMessageInfo.getSenderID());
            Logger.logD(Logger.ROOM, "onNewMessage()->mPatientId:" + mPatientId);
            if (baseMessageInfo.getSenderID() == mPatientId) {
                ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_CALL_PATIENT);
                setVideoState(mPatientId, AppConstant.RoomState.STATE_MISS);
                VideoInfo videoInfo = getVideoInfo(mPatientId, VideoTagInfo.VIDEO_TYPE_CAMERA);
                if (videoInfo != null) {
                    videoInfo.setVideoState(AppConstant.RoomState.STATE_MISS);
                }
                mToastTlv.showToastView(getString(R.string.video_room_state_patient_busy));
            } else if (baseMessageInfo.getSenderID() == mCustomerServiceId) {
                mCustomerServiceCalling = false;
                mIsMeCall = false;
                ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_CALL_CUSTOMER_SERVICE);
                setVideoState(mCustomerServiceId, AppConstant.RoomState.STATE_MISS);
                VideoInfo videoInfo = getVideoInfo(mCustomerServiceId, VideoTagInfo.VIDEO_TYPE_CAMERA);
                if (videoInfo != null) {
                    videoInfo.setVideoState(AppConstant.RoomState.STATE_MISS);
                }
                mHelpToastView.showToastView(getString(R.string.video_room_no_customer_service), false);
            } else {
                ConsultRoomUtils.clearTimer(mTimers, baseMessageInfo.getSenderID());
                setVideoState(baseMessageInfo.getSenderID(), AppConstant.RoomState.STATE_MISS);
                VideoInfo videoInfo = getVideoInfo(baseMessageInfo.getSenderID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
                if (videoInfo != null) {
                    videoInfo.setVideoState(AppConstant.RoomState.STATE_MISS);
                }
                mToastTlv.showToastView("接通发目前正忙，请稍后重试");
            }
        } else if (baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_APPOINTMENT_AFFIRM ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_APPOINTMENT_MATERIAL_CHECK ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_CONSULTATION_NOTIC ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_RECONSULTATION_NOTIC ||
                baseMessageInfo.getMsgType() == MessageProtocol.SMS_TYPE_REPORT_NOTIC) {
            //预约变化通知
            if (baseMessageInfo.getAppointmentId() != mOrderId) {
                return;
            }
            if (mFlagHigherDoctor) {
                return;
            }
            int stat = -1;
            try {
                JSONObject jsonObject = new JSONObject(baseMessageInfo.getMsgContent());
                stat = jsonObject.optInt("ap_stat");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*******************************************************
     * 各种dialog弹窗 start
     ****************************************************************/

    /**
     * 首诊医生被大医生呼叫
     * 弹呼叫Dialog
     */
    private void showCallDialog(final DCOrderDetailInfo orderDetailInfo, BaseMessageInfo baseMessageInfo, final int doctorId, final int userType) {
        if (isFinishing()) {
            return;
        }

        Timer callTimer = new Timer();
        mTimers.append(TIMER_TYPE_CALLED, callTimer);
        callTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallDialog != null && mCallDialog.isShowing()) {
                            mCallDialog.dismiss();
                        }
                    }
                });
            }
        }, CALL_WAIT_DURATION);

        if (mCallDialog != null && mCallDialog.isShowing()) {
            return;
        }

        View layout = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_common_dialog, null);
        mCallDialog = new AlertDialog.Builder(getThisActivity(), R.style.custom_noActionbar_window_style).create();
        mCallDialog.show();
        mCallDialog.setContentView(layout);
        mCallDialog.setCanceledOnTouchOutside(false);
        mCallDialog.setCancelable(false);

        Window win = mCallDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        final TextView titleTv = (TextView) layout.findViewById(R.id.layout_common_dialog_title_tv);
        final TextView messageTv = (TextView) layout.findViewById(R.id.layout_common_dialog_message_tv);
        TextView leftTv = (TextView) layout.findViewById(R.id.layout_common_dialog_positive_tv);
        TextView rightTv = (TextView) layout.findViewById(R.id.layout_common_dialog_negative_tv);
        titleTv.setVisibility(View.VISIBLE);
        leftTv.setText(R.string.video_call_refuse);
        rightTv.setText(R.string.video_call_accept);

        titleTv.setText(R.string.consult_room_call_title);
        messageTv.setText(mCallDialogDcotor + (TextUtils.isEmpty(mCallDialogHospital) ? "" : ("\n" + mCallDialogHospital))
                + (TextUtils.isEmpty(mCallDialogDepartment) ? "" : ("\n" + mCallDialogDepartment)));
        leftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallDialog.dismiss();
                ConsultRoomUtils.sendRefuseMsg(doctorId, orderDetailInfo.getOrderId());
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallDialog.dismiss();
                mReceiveDoctor = null;
                mOrderDetailInfo = orderDetailInfo;
                if (mFlagInRoom) {
                    reJoinRoom(CALL_ACTION_TYPE_CALLED);
                    return;
                }
                joinRoom(orderDetailInfo.getOrderId());
            }
        });
        mDoctorManager.getDoctor(baseMessageInfo.getSenderID(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                Logger.logD(Logger.ROOM, "onNewMessage()->DoctorBaseInfo:" + doctorBaseInfo);
                if (doctorBaseInfo == null) {
                    return;
                }
                mCallDialogDcotor = doctorBaseInfo.getRealName();
                if (mCallDialog.isShowing()) {
                    messageTv.setText(doctorBaseInfo.getRealName() + (TextUtils.isEmpty(doctorBaseInfo.getHospitalName()) ? "" : ("\n" + doctorBaseInfo.getHospitalName()))
                            + (TextUtils.isEmpty(doctorBaseInfo.getDepartmentName()) ? "" : ("\n" + doctorBaseInfo.getDepartmentName())));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示用户协议框
     */
    private void showProtocolDialog() {
        if (isFinishing()) {
            return;
        }
        if (!SPUtils.getInstance().getBoolean(AppPreference.KEY_IS_FIRST_ENTER_VIDEO_ROOM, true)) {
            return;
        }
        new InformedConsentDialog.Builder(getThisActivity())
                .setMessage(R.string.video_dialog_user_protocol_message)
                .setButton(R.string.video_dialog_user_protocol_ok, new InformedConsentDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClicked() {
                        SPUtils.getInstance().put(AppPreference.KEY_IS_FIRST_ENTER_VIDEO_ROOM, false);
                    }
                })
                .show();
    }

    /**
     * 退出Dialog
     */
    private void showExitDialog() {
        if (isFinishing()) {
            return;
        }
        if (!CommonUtils.checkActivityExist(this, AppApplication.getInstance().getPackageName())) {
            return;
        }
        new CommonDialog.Builder(this)
                .setTitle(R.string.video_dialog_exit_title)
                .setMessage("是否结束本次问诊并退出")
                .setNegativeButton("退出", new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        exitVideoRoom();
                    }
                })
                .setPositiveButton("取消", new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {

                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    /**
     * 首诊医生加入诊室dialog
     */
    private void showDoctorVideoReqDialog() {
        if (isFinishing()) {
            return;
        }
        mDoctorReqDialog = new CommonDialog.Builder(this)
                .setMessage(getString(R.string.video_dialog_request_doctor_message))
                .setPositiveButton(R.string.video_dialog_no, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                        ConsultRoomUtils.respondVideoReq(currentUserId, mAttendingDoctorId, mOrderId, ConsultRoomUtils.CALL_RESULT_REFUSE);
                        ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_RESPOND_DOCTOR);
                    }
                })
                .setNegativeButton(R.string.video_dialog_yes, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        ConsultRoomUtils.respondVideoReq(currentUserId, mAttendingDoctorId, mOrderId, ConsultRoomUtils.CALL_RESULT_ALLOW);
                        ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_RESPOND_DOCTOR);
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    /**
     * 显示权限对话框
     *
     * @param isCameraException 是否相机异常
     */
    private void showPermissionDialog(boolean isCameraException) {
        if (isFinishing()) {
            return;
        }
        new CommonDialog.Builder(getThisActivity())
                .setTitle(isCameraException ? R.string.video_dialog_permission_camera_title : R.string.video_dialog_permission_microphone_title)
                .setMessage(isCameraException ? R.string.video_dialog_permission_camera : R.string.video_dialog_permission_microphone)
                .setPositiveButton(R.string.video_dialog_going_setting, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                        startActivityForResult(Utils.getAppDetailSettingIntent(), 0);
                        goMainActivity();
                    }
                })
                .show();
    }


    /**
     * 创建房间失败Dialog
     */
    private void showCreateRoomFailedDialog() {
        if (isFinishing()) {
            return;
        }
        mCreataRommFailedDialog = new CommonDialog.Builder(this)
                .setMessage(getString(R.string.video_room_state_create_room_failed))
                .setPositiveButton(R.string.video_dialog_close, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                        ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_RESPOND_DOCTOR);
//                        finish();
                    }
                })
                .setNegativeButton(R.string.video_dialog_retry, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        ConsultRoomUtils.clearTimer(mTimers, TIMER_TYPE_RESPOND_DOCTOR);
                        resetState();
                        joinRoom(mRoomId);
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    private void dismissConsultList() {
        mConsultListFl.setVisibility(View.GONE);
        mConsultListFl.startAnimation(animShadowAlphaOut);
    }

    /*******************************************************各种dialog弹窗 end****************************************************************/

    /**
     * 成员加入房间Toast
     *
     * @param roomResultInfo
     * @param videoRoomMember
     */
    private void showMemberJoinRoomToast(VideoRoomResultInfo roomResultInfo, VideoRoomMember videoRoomMember) {
        ConsultRoomUtils.clearTimer(mTimers, roomResultInfo.getUserID());
        mToastTlv.showToastView(getString(R.string.video_room_state_entered_room, getUserTypeTitle(roomResultInfo.getUserType())));
    }

    /**
     * 成员退出房间Toast
     *
     * @param userId
     */
    private void showMemberExitRoomToast(int userId) {
        VideoInfo videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo == null) {
            return;
        }
        showToastViewDelayed(getString(R.string.video_room_state_leave_room, getUserTypeTitle(videoInfo.getUserType())));
    }

    /**
     * 切换视频Toast
     *
     * @param userId
     */
    private void showSwitchVideoToast(int userId) {
        VideoInfo videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
        if (videoInfo == null) {
            return;
        }
//        mToastTlv.showToastView(getString(R.string.video_room_state_subscribe, getUserTypeTitle(videoInfo.getUserType())));
    }

    /**
     * 延迟弹Toast提示
     *
     * @param strToast
     */
    private void showToastViewDelayed(final String strToast) {
        AppHandlerProxy.postDelayed(new Runnable() {
            @Override
            public void run() {
                mToastTlv.showToastView(strToast);
            }
        }, 1000);
    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            Logger.logD(Logger.ROOM, "regBroadcastReceiver->state:" + state);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://1响铃
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK://2接听
                    mConsultRoomManager.pauseMic(true);
                    mConsultRoomManager.selfInterrupted(ConsultRoomManager.VIDEO_VOICE_CLOSED);
                    break;

                case TelephonyManager.CALL_STATE_IDLE://0挂断
                    mConsultRoomManager.pauseMic(!mIsMicOpen);
                    mConsultRoomManager.selfInterrupted(ConsultRoomManager.VIDEO_VOICE_OPEN);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (mConsultListFl.getVisibility() == View.VISIBLE) {
            dismissConsultList();
        } else {
            showExitDialog();
        }
    }

    @OnClick({R.id.activity_dcroom_screen_rl,
            R.id.activity_dcroom_exit_room_tv,
            R.id.act_dcroom_record_iv,
            R.id.act_dcroom_update_data,
            R.id.activity_dcroom_hide_show_video_btn,
            R.id.activity_dcroom_speaker_ib,
            R.id.activity_dcroom_mic_ib,
            R.id.activity_dcroom_av_mode_ib,
            R.id.activity_dcroom_cam_ib,
            R.id.activity_dcroom_call_tv,
            R.id.activity_dcroom_consult_list_fl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_dcroom_screen_rl:
                if (mBottomSm.isShown()) {
                    mHideBottomTimeCdt.cancel();
                }
                mBottomSm.toggle();
                break;

            case R.id.activity_dcroom_exit_room_tv:
                showExitDialog();
                break;

            case R.id.act_dcroom_record_iv:
                setDCAdviceEditState(1, IssueReferralAndSuggestionDialog.ISSUE_TYPE_REFERRAL);
                break;

            case R.id.act_dcroom_update_data:
                showPatientInfoDialog(1);
                break;
            case R.id.activity_dcroom_hide_show_video_btn:
                showSmallVideo(!mHideShowVideoIv.isSelected());
                break;

            case R.id.activity_dcroom_speaker_ib:
                AudioManager audioManager = (AudioManager) getThisActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isWiredHeadsetOn()) {
                    return;
                }
                mAudioAdapterManager.setMode(mStateSpeaker == SPEAKER_TYPE_OPEN ? AudioModule.NAME_SPEAKEROFF : AudioModule.NAME_SPEAKERON);
                mStateSpeaker = (mStateSpeaker == SPEAKER_TYPE_OPEN ? SPEAKER_TYPE_CLOSE : SPEAKER_TYPE_OPEN);
                if (mOutsideSwitch) {
                    mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_off));
                    mOutsideSwitch = false;
                    mToastTlv.showToastView(getString(R.string.video_room_state_close_speaker));
                } else {
                    mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_on));
                    mOutsideSwitch = true;
                    mToastTlv.showToastView(getString(R.string.video_room_state_open_speaker));
                }
                break;

            case R.id.activity_dcroom_mic_ib:
                Logger.logI(Logger.ROOM, "mic_ib-->mFlagInRoom:" + mFlagInRoom + ",!mFlagFinishCurrent:" + !mFlagFinishCurrent + ",mEnableAudio:" + mEnableAudio);
                if (mFlagInRoom && !mFlagFinishCurrent) {
                    if (mEnableAudio) {
                        if (mIsMicOpen) {
                            changeSeatState((byte) 0);
                        } else {
                            if (getSeat() != 0) {
                                changeSeatState(getSeat());
                            } else {
                                mToastTlv.showToastView(getString(R.string.video_room_state_no_seat_tips));
                            }
                        }
                    } else {
                        showPermissionDialog(false);
                    }
                }
                break;

            case R.id.activity_dcroom_av_mode_ib:
                if (mFlagInRoom && !mFlagFinishCurrent) {
                    if (!mIsIdle) {
                        return;
                    }
                    mIsIdle = false;
                    changeSelfAvType(mCurrentAvType == ConsultRoomManager.AV_TYPE_VIDEO ? ConsultRoomManager.AV_TYPE_VOICE : ConsultRoomManager.AV_TYPE_VIDEO);
                }
                break;

            case R.id.activity_dcroom_cam_ib:
                if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VOICE) {
                    mToastTlv.showToastView(getString(R.string.video_room_state_using_voice_mode));
                    return;
                }
                if (mSwitchCamera) {
                    mToastTlv.showToastView(getString(R.string.video_room_state_rear_camera));
                    mSwitchCamera = false;
                } else {
                    mToastTlv.showToastView(getString(R.string.video_room_state_before_camera));
                    mSwitchCamera = true;
                }
                mLMVideoMgr.toggleCamera();
                break;

            case R.id.activity_dcroom_call_tv:
                showCallDoctorListDialog();
                break;

            case R.id.activity_dcroom_consult_list_fl:
                break;
            default:
                break;
        }
    }

    private void showPatientInfoDialog(int editState) {
        DCDutyRoomPatientListDialog dialog = new DCDutyRoomPatientListDialog();
        dialog.setOrderId(mOrderId);
        dialog.setDismissListener(new DCDutyRoomPatientListDialog.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDCManager.setDCAdviceEditState(mOrderId, 0, new OnResultListener<DCAdviceEditorInfo>() {
                    @Override
                    public void onResult(BaseResult baseResult, DCAdviceEditorInfo dcAdviceEditorInfo) {

                    }
                });
            }
        });
        mDCManager.setDCAdviceEditState(mOrderId, editState, new OnResultListener<DCAdviceEditorInfo>() {
            @Override
            public void onResult(BaseResult baseResult, DCAdviceEditorInfo dcAdviceEditorInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (editState == 1) {
                        dialog.show(getSupportFragmentManager(), "DCDutyRoomPatientListDialog");
                    } else {
                        if (mIssueOrderDialog.isShowing()) {
                            mIssueOrderDialog.dismiss();
                        }
                    }
                } else if (baseResult.getCode() == RESULT_DB_EXISTED) {
                    if (editState == 1) {
                        new CommonDialog.Builder(getThisActivity())
                                .setTitle("提示")
                                .setMessage(dcAdviceEditorInfo.getEditName() + "医生正在编辑患者资料")
                                .setCancelable(false)
                                .setNegativeButton("确定", () -> {
                                })
                                .show();
                    }
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            }
        });

    }

    private void seekHelp() {
        mDoctorManager.getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mConsultRoomManager.seekHelp(doctorBaseInfo.getRealName());
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void callServicePersonnel(VideoRoomResultInfo roomResultInfo) {
        if (mCustomerServiceCalling) {
            return;
        }
        mCustomerServiceCalling = true;
        mIsMeCall = true;
        final Timer CustomerServiceTimer = new Timer();
        addMembersJoinVideo(AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE);
        ConsultRoomUtils.sendCallustomerServiceMessage(currentUserId, roomResultInfo.getUserID(), mOrderId, AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE, roomResultInfo.getSeqId());
        mTimers.append(TIMER_TYPE_CALL_CUSTOMER_SERVICE, CustomerServiceTimer);
        CustomerServiceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setVideoState(mCustomerServiceId, AppConstant.RoomState.STATE_MISS);
                        VideoInfo videoInfo = getVideoInfo(mCustomerServiceId, VideoTagInfo.VIDEO_TYPE_CAMERA);
                        if (videoInfo != null) {
                            videoInfo.setVideoState(AppConstant.RoomState.STATE_MISS);
                        }
                        mCustomerServiceCalling = false;
                    }
                });
            }
        }, 30 * 1000);
    }

    private void showHelpEndDialog(VideoRoomResultInfo roomResultInfo) {
        View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_help_end_dialog, null);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.layout_help_end_dialog_rating_bar);
        ImageView delete = (ImageView) view.findViewById(R.id.layout_help_end_dialog_delete);
        final TextView doctorName = (TextView) view.findViewById(R.id.layout_help_end_dialog_name);
        TextView helpTime = (TextView) view.findViewById(R.id.layout_help_end_dialog_time);
        final Dialog dialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        helpTime.setText(getString(R.string.consult_help_time, secToTime(roomResultInfo.getDuration())));
        Logger.logD(Logger.ROOM, "showHelpEndDialog->roomResultInfo.getDuration():" + roomResultInfo.getDuration());
        mDoctorManager.getDoctorInfo(mCustomerServiceId, true, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                Logger.logD(Logger.ROOM, "showHelpEndDialog->doctorBaseInfo:" + doctorBaseInfo);
                if (doctorBaseInfo != null) {
                    doctorName.setText(getString(R.string.consult_help_doctor_assistant, doctorBaseInfo.getRealName()));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                mConsultRoomManager.evaluateSeekHelp(mCurrentSeqId, rating);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    /**
     * 小视频点击事件监听
     *
     * @param view
     */
    @Override
    public void onSmallVideoClicked(SmallVideoView view) {
        VideoTagInfo smallVideoTagInfo = (VideoTagInfo) view.getTag();
        Logger.logD(Logger.ROOM, "onSmallVideoClicked->smallVideoTagInfo:" + smallVideoTagInfo);
        VideoInfo smallVideoInfo = null;
        if (smallVideoTagInfo != null) {
            smallVideoInfo = getVideoInfo(smallVideoTagInfo.getUserId(), smallVideoTagInfo.getType());
        }
        if (smallVideoInfo == null) {
            return;
        }
        Logger.logD(Logger.ROOM, "onSmallVideoClicked->smallVideoInfo:" + smallVideoInfo);
        int roomState = smallVideoInfo.getVideoState();
        switch (roomState) {
            case AppConstant.RoomState.STATE_CALLING://正在呼叫
                break;

            case AppConstant.RoomState.STATE_BEING_VIDEO://正在视频
            case AppConstant.RoomState.STATE_DEFAULT:
            case AppConstant.RoomState.STATE_BEING_VOICE://正在音频
                if (mMembersLeaveTv.getVisibility() == View.VISIBLE) {
                    mMembersLeaveTv.setVisibility(View.GONE);
                }
                showSwitchVideoToast(smallVideoTagInfo.getUserId());
                switchVideoView(view);
                break;

            case AppConstant.RoomState.STATE_MISS://拒接呼叫或没有接听
                callDoctor(smallVideoTagInfo.getUserId());
                break;
        }
    }

    @Override
    public void onPositiveClicked(String clickText) {
        if (clickText.equals(getString(R.string.video_dialog_retry))) {
            if (!mFlagInRoom) {
                if (NetStateReceiver.NETWORK_TYPE_WIFI == NetStateReceiver.getCurrentNetType(this) ||
                        NetStateReceiver.NETWOKR_TYPE_MOBILE == NetStateReceiver.getCurrentNetType(this)) {
                    showProgressDialog();
                    mFlagShowNetworkDisconnect = true;
                    mRatioMap.clear();
                    Logger.logD(Logger.ROOM, "onPositiveClicke-->isOnLogin:" + AppApplication.getInstance().isOnLogin());
                    if (!AppApplication.getInstance().isOnLogin()) {
                        mUserInfoManager.reLogin(new LoginStateChangeListener() {
                            @Override
                            public void onLoginStateChanged(int code, int msg) {
                                Logger.logD(Logger.ROOM, "onPositiveClicke-->onLoginStateChanged:code:" + code + ",isLogin:" + AppApplication.getInstance().isLogin());
                                if (code == 0 && AppApplication.getInstance().isLogin()) {
                                    AppHandlerProxy.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            resetState();
                                            joinRoom(mRoomId);
                                        }
                                    });
                                } else {
                                    AppHandlerProxy.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dismissProgressDialog();
                                            mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        resetState();
                        joinRoom(mRoomId);
                    }
                } else {
                    mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
                    ToastUtils.showShort(R.string.room_state_net_disconnected);
                }
            }
        }
    }

    /**
     * 视频通话事件通知接口
     */
    private class InnerLMVideoEvents implements LMVideoMgr.LMVideoEvents {

        @Override
        public void onVideoSessionStarted(int error) {
            Logger.logD(Logger.ROOM, "启动视频会话结果：" + error);
        }

        @Override
        public void onVideoSessionStopped() {
            Logger.logD(Logger.ROOM, "视频会话结束");
//            exitRoomDeal();
        }

        @Override
        public void onVideoCaptureStarted(int error) {
            Logger.logD(Logger.ROOM, "视频捕获（输出）结果：" + error);
            Logger.logI(Logger.ROOM, "InnerLMVideoEvents->mFlagUseBackCam：" + mFlagUseBackCam + "->mEnableVideo：" + mEnableVideo);

            if (mFlagUseBackCam) {
                if (error != LMVideoMgr.kVideoErrorNone && error != LMVideoMgr.kVideoErrorAlreadyStarted) {
                    if (error == LMVideoMgr.kVideoErrorCameraError) {
                        mCamBackNormal = false;
                    }
                    changeSelfAvType(ConsultRoomManager.AV_TYPE_VOICE);
                }
            } else {
                if (error == LMVideoMgr.kVideoErrorNone) {
                    AppHandlerProxy.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
//                            setVideoState(AppConstant.RoomState.STATE_DEFAULT);
                        }
                    });
                } else if (mEnableVideo) {
                    if (error == LMVideoMgr.kVideoErrorCameraError) {
                        mCamFrontNormal = false;
                    }
                    mFlagUseBackCam = true;
                } else {//无视频权限，切换为语音模式
                    changeSelfAvType(ConsultRoomManager.AV_TYPE_VOICE);
                }
            }
        }

        @Override
        public void onVideoCaptureStopped() {
            Logger.logD(Logger.ROOM, "视频捕获（输出）结束");
        }

        @Override
        public void onVideoDisplayStarted(long streamSsrc, int error) {
            Logger.logD(Logger.ROOM, "视频播放（输入）结果：" + error);
        }

        @Override
        public void onVideoDisplayStopped(long streamSsrc) {
            Logger.logD(Logger.ROOM, "视频播放（输入）结束");
        }

        @Override
        public void onCapturePlaybackVideoSizeReset(final GLSurfaceView glSurfaceView, final int width, final int height) {
            // TODO Auto-generated method stub
            LMVLog.info("<<LMVideoEvents>> Video capture playback video size notified: width = " + width + ", height = " + height);
            if (width == 0 || height == 0) {
                return;
            }
            adjustSurfaceSize(glSurfaceView, width, height);
        }

        @Override
        public void onDisplayVideoSizeReset(final GLSurfaceView glSurfaceView, long ssrc, final int width, final int height) {
            // TODO Auto-generated method stub
            LMVLog.info("<<LMVideoEvents>> Displaying video size notified: ssrc = " + ssrc + ", width = " + width + ", height = " + height);
            double displayRatio = mRatioMap.containsKey(ssrc) ? mRatioMap.get(ssrc) : 0;
            double tempDisplayRatio = new BigDecimal((float) width / height).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            LMVLog.info("<<LMVideoEvents>> displayRatio = " + displayRatio + ", tempDisplayRatio = " + tempDisplayRatio);
            if (displayRatio == 0 || Math.abs(displayRatio - tempDisplayRatio) > RATIO_DIFFERENCE || mInitiativeAdjust) {
                mInitiativeAdjust = false;
                adjustSurfaceSize(glSurfaceView, width, height);
                LMVLog.info("<<LMVideoEvents>> adjustSurfaceSize-->已执行");
            }
            mRatioMap.put(ssrc, tempDisplayRatio);
        }

        @Override
        public void onVideoNetworkState(long streamSsrc, final int state) {
//            Logger.logD(Logger.ROOM, "onVideoNetworkState->streamSsrc:" + streamSsrc + "->state:" + state);
        }

        @Override
        public void onDisplayingImageCaptured(long streamSsrc, Bitmap bitmap) {
            Logger.logD(Logger.ROOM, "视频播放截图完成");
        }

        @Override
        public void onAlivePacketTimeOut() {
            Logger.logD(Logger.ROOM, "视频模块与视频服务器之间的心跳包超时");
            stopVideo();
            AppHandlerProxy.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
                }
            });
        }

        @Override
        public void onCaptureStreamLostRateGet(final float v) {
            AppHandlerProxy.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    int state;
                    VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                    if (videoTagInfo != null
                            && videoTagInfo.getUserId() == currentUserId) {
                        state = getNetState(v);
                    } else {
                        state = getNetState(v > mDownLoadLostRate ? v : mDownLoadLostRate);
                    }
                    setVideoSignal(currentUserId, state);

                    // 在视频模式下网络状态差时自动切换到语音模式,切换过程为异步方式,需要等到上一次切换结果回调后才能开始下一次切换
                    if (!mFlagInRoom || mCurrentAvType != ConsultRoomManager.AV_TYPE_VIDEO || NetStateReceiver.getCurrentNetType(getThisActivity()) == NetStateReceiver.NETWORK_TYPE_NONE || mLatestVideoNetState == state) {
                        return;
                    }

                    mLatestVideoNetState = state;
                    if (state == LMVideoMgr.kVideoNetworkStateNotGood) {
                      /*  if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VIDEO) {
                            mToastTlv.showToastView(getString(R.string.consult_room_state_net_bad));
                        } else {
                            mToastTlv.showToastView(getString(R.string.consult_room_state_net_bad));
                        }*/
                    } else {
                        showToastViewDelayed(getString(R.string.video_room_state_appoint_going));
                    }
                }
            });
        }

        @Override
        public void onPlayStreamLostRateGet(final long l, final float v, final float v1) {
            if (mVideoInfoMap == null) {
                return;
            }
            AppHandlerProxy.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    VideoInfo videoInfo = null;
                    for (final Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
                        if (entry.getValue() != null && entry.getValue().getSsrc() == l) {
                            videoInfo = entry.getValue();
                            break;
                        }
                    }
                    if (videoInfo == null) {
                        return;
                    }
                    int state = videoInfo.getVideoState() == AppConstant.RoomState.STATE_BEING_VOICE ? LMVideoMgr.kVideoNetworkStateVeryGood : getNetState(v);
                    setVideoSignal(videoInfo.getUseId(), state);
                    VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                    if (bigVideoTagInfo == null) {
                        return;
                    }
                    if (videoInfo.getUseId() == bigVideoTagInfo.getUserId()) {
                        mDownLoadLostRate = v1;
                    }
                }
            });

        }
    }

    /**
     * 房间自身状态变化监听
     */
    private class InnerRoomSelfStateListener implements VideoRoomInterface.OnRoomSelfStateChangeListener {
        @Override
        public void onSelfJoinRoom(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, "onSelfJoinRoom()->>result:" + result + ", resultInfo:" + resultInfo);
            Logger.logD(Logger.ROOM, "onSelfJoinRoom()->isAdmin->" + mUserInfoManager.isAdmin());
            mIsIdle = true;
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {//加入成功
                Logger.logD(Logger.ROOM, "onSelfJoinRoom()->加入成功->");
                mInitiativeAdjust = true;
                mRoomId = resultInfo.getRoomID();
                List<VideoRoomMember> memberList = resultInfo.getMemberList();
                if (memberList.size() > 0) {
                    mIssueable = true;
                    startChatTimer();
                }
                mFlagInRoom = true;
                Logger.logD(Logger.ROOM, "onSelfJoinRoom->mFlagInRoom:" + mFlagInRoom);
                mFlagJoined = true;
                mAudioAdapterManager.setMode(AudioModule.NAME_AFTERVOICE);
                //设置放音模式,有耳机时为耳机否则为外放
                AudioManager audioManager = (AudioManager) getThisActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isWiredHeadsetOn()) {
                    mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKEROFF);
                    mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_off));
                    mOutsideSwitch = false;
                } else {
                    mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKERON);
                    mSpeakerIb.setImageDrawable(getDrawable(R.drawable.ic_btn_video_room_speaker_on));
                    mOutsideSwitch = true;
                }
                mAudioAdapterManager.setMode(mStateSpeaker == SPEAKER_TYPE_OPEN ? AudioModule.NAME_SPEAKERON : AudioModule.NAME_SPEAKEROFF);
                String httpDnsIp = AppApplication.getInstance().getManager(HTTPDNSManager.class).getStringDnsIp(resultInfo.getPvsUrl());
                submitDomainNameRequester(httpDnsIp);
                Logger.logD(Logger.COMMON, "->setPesInfo()->ip:" + httpDnsIp);
                mRemoteIP = ((StringUtils.isEmpty(httpDnsIp) ? resultInfo.getPvsIP() : httpDnsIp));
                Logger.logD(Logger.ROOM, "onSelfJoinRoom->resultInfo.getPvsUrl():" + resultInfo.getPvsUrl() + ",resultInfo.getPvsIP():" + resultInfo.getPvsIP() + ",httpDnsIp:" + httpDnsIp + ", mRemoteIP：" + mRemoteIP);

                mRemotePort = resultInfo.getVideoPort();
                mVideoSsrc = resultInfo.getVideoSsrc();
                for (int i = 0; i < memberList.size(); i++) {
                    if (memberList.get(i).getUserType() == USER_TYPE_ATTENDING_DOCTOR
                            || memberList.get(i).getUserType() == USER_TYPE_MDT_DOCTOR
                            || memberList.get(i).getUserType() == AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR
                            || memberList.get(i).getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
                        getDoctorInfo(memberList.get(i).getUserId());
                    } else if (memberList.get(i).getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                        mHaveCustomerService = true;
                        mCurrentCustomerServiceId = memberList.get(i).getUserId();
                        displayCustomerService(memberList.get(i).getUserId());
                    }
                }

                addVideos(resultInfo.getMemberList());
                startVideoSession();
                startCaptureVideo();
                if (mCurrentAvType == ConsultRoomManager.AV_TYPE_VOICE) {
                    changeSelfAvType(ConsultRoomManager.AV_TYPE_VOICE);
                }
                List<VideoRoomMember> videoRoomMembers = new ArrayList<>();
                for (VideoRoomMember videoRoomMember : resultInfo.getMemberList()) {
                    videoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_CAMERA);
                    videoRoomMembers.add(videoRoomMember);

                    if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_SCREEN_SHARE)) {//共享屏幕
                        VideoRoomMember tempVideoRoomMember = new VideoRoomMember();
                        tempVideoRoomMember.setUserType(videoRoomMember.getUserType());
                        tempVideoRoomMember.setUserId(videoRoomMember.getUserId());
                        tempVideoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_SHARE);
                        videoRoomMembers.add(tempVideoRoomMember);
                    } else if (isSameStatus(videoRoomMember.getStatus(), AppConstant.VideoUintStatus.VIDEO_UINT_STATUS_PLAY_VIDEO_FILE)) {//播放视频文件
                        VideoRoomMember tempVideoRoomMember = new VideoRoomMember();
                        tempVideoRoomMember.setUserType(videoRoomMember.getUserType());
                        tempVideoRoomMember.setUserId(videoRoomMember.getUserId());
                        tempVideoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
                        videoRoomMembers.add(tempVideoRoomMember);
                    }
                }
                mConsultRoomManager.videoSubscribe(videoRoomMembers, mSelfStateChangeListener);
                setCamState();
                setMicState();
                changeSeatState(getSeat());
                //上传定位信息和IP地址
                uploadLocationNetwork();
                if (mCamBackNormal && mFlagUseBackCam && mCurrentAvType == ConsultRoomManager.AV_TYPE_VIDEO) {
                    mLMVideoMgr.toggleCamera();
                }
                if (mReceiveDoctor != null) {
                    mHelpToastView.showToastView("等待医生进入视频房间...", true);
                    callDoctor(mOrderDetailInfo.getDoctorId());
                }
                mCallActionType = CALL_ACTION_TYPE_DEFAULT;
            } else {
                showCreateRoomFailedDialog();
            }
            dismissProgressDialog();
        }

        /**
         * 百度定位，然后提交至服务器
         * 然后获取外网IP地址，把定位信息IP信息上传上去
         */
        private void uploadLocationNetwork() {
            mLocationManager.start(mOrderId, new LocationManager.OnGetLocationListener() {
                @Override
                public void onLocationGet(final BDLocation bdLocation) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String ip = NetWorkUtil.GetNetIp();
                            AppHandlerProxy.runOnUIThread(new Runnable() {
                                @Override
                                public void run() {
                                    UploadLocationNetworkRequester uploadLocationNetworkRequester = new UploadLocationNetworkRequester(new OnResultListener<Void>() {
                                        @Override
                                        public void onResult(BaseResult baseResult, Void aVoid) {

                                        }
                                    });
                                    uploadLocationNetworkRequester.appointmentId = mOrderId;
                                    uploadLocationNetworkRequester.ipHome = ip;
                                    uploadLocationNetworkRequester.latitude = bdLocation != null ? bdLocation.getLatitude() + "" : "";
                                    uploadLocationNetworkRequester.longitude = bdLocation != null ? bdLocation.getLongitude() + "" : "";
                                    uploadLocationNetworkRequester.address = bdLocation != null ? bdLocation.getAddrStr() : "";
                                    uploadLocationNetworkRequester.doPost();
                                }
                            });
                        }
                    }).start();
                }
            });
        }

        @Override
        public void onSelfChangeSeatState(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, "onSelfChangeSeatState->result:" + result + "->resultInfo:" + resultInfo);
            mIsIdle = true;
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                if (resultInfo.getUserSeat() > 0) {
                    mIsMicOpen = true;
                    mToastTlv.showToastView(getString(R.string.video_room_state_open_mic));
                } else {
                    mIsMicOpen = false;
                    mToastTlv.showToastView(getString(R.string.video_room_state_close_mic));
                }
                mUserSeats.put(currentUserId, resultInfo.getUserSeat());
                setMicState();
            } else {
                if (!mRoomOperationChangeSeat) {//自己申请或关闭麦克风失败才会弹提示，主持人关麦或者开麦失败不弹提示
                    mToastTlv.showToastView(mIsMicOpen ? getString(R.string.video_room_state_close_mic_error)
                            : getString(R.string.video_room_state_open_mic_error));
                }
                getRoomMemberSeat();
            }
            mRoomOperationChangeSeat = false;
        }

        @Override
        public void onSelfExitRoom(int result, VideoRoomResultInfo resultInfo) {
            mIsIdle = true;
            Logger.logD(Logger.ROOM, "onSelfExitRoom->result:" + result + "->resultInfo:" + resultInfo + "->mFlagInRoom:" + mFlagInRoom);
            mLocationManager.setGetLocation(false);//不需要在获取位置
            showHideChatTime(false);
            if (mFlagKickOff) { //被移出房间不做处理,
                return;
            }
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                mFlagInRoom = false;
                Logger.logD(Logger.ROOM, "onSelfExitRoom->mFlagExitRoom:" + mFlagExitRoom);
                Logger.logD(Logger.ROOM, "onSelfExitRoom（）->Thread.currentThread().getName():" + Thread.currentThread().getName());
                Logger.logD(Logger.ROOM, "onSelfExitRoom->mCurrentAvType:" + mCurrentAvType);
                if (mCallActionType == CALL_ACTION_TYPE_DEFAULT) {
                    exitRoomDeal();
                } else {
                    resetState();
                    dismissProgressDialog();
                    mOrderId = mOrderDetailInfo.getOrderId();
                    joinRoom(mOrderDetailInfo.getOrderId());
                }
            } else {
                dismissProgressDialog();
                ToastUtils.showShort(getString(R.string.video_room_state_exist_room_failed, result));
            }
        }

        @Override
        public void onSelfChangeAVType(int result, VideoRoomResultInfo resultInfo) {
            mIsIdle = true;
            Logger.logD(Logger.ROOM, "onSelfChangeAVType-->resultInfo:" + resultInfo);
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                mCurrentAvType = resultInfo.getAvType();
                switchAvModeView();
            } else {
                mToastTlv.showToastView(String.format("%03d", result) + getString(R.string.video_room_exception_change_av_mode));
            }
        }

        @Override
        public void onSelfGetRoomMember(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, "onSelfGetRoomMember-->result:" + result + "-->resultInfo:" + resultInfo);
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                updateMemberSeat(resultInfo);
            }
        }

        /**
         * 成员是否在房间
         *
         * @param members
         * @param userId
         * @return
         */
        private boolean isRoomMemberInRoom(List<VideoRoomMember> members, int userId) {
            for (VideoRoomMember member : members) {
                if (member.getUserId() == userId) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onSelfVideoSubscribe(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onSelfVideoSubscribe()->resultInfo:" + resultInfo);
            if (resultInfo == null) {
                return;
            }
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                if (resultInfo.getSsrcList() != null && resultInfo.getSsrcList().size() > 0) {
                    for (VideoInfo videoInfo : resultInfo.getSsrcList()) {
                        VideoInfo tempVideoInfo = getVideoInfo(videoInfo.getUseId(), videoInfo.getType());
                        Logger.logD(Logger.ROOM, TAG + "->onSelfVideoSubscribe()->videoInfo:" + videoInfo + ", tempVideoInfo:" + tempVideoInfo);
                        if (tempVideoInfo == null) {
                            continue;
                        }
                        if (videoInfo.getSsrc() != 0 && videoInfo.getUseId() != currentUserId) {
                            tempVideoInfo.setSsrc(videoInfo.getSsrc());
                            setVideoDisPlay(tempVideoInfo);
                        }
                    }
                }
            }
        }

        @Override
        public void onSelfSeekHelp(int result, VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onSelfSeekHelp()->：result:" + result + " ,roomResultInfo:" + roomResultInfo);
            if (result == AppConstant.HelpCode.HELP_CODE_SUCCESS && roomResultInfo != null) {
                mCurrentSeqId = roomResultInfo.getSeqId();
                if (mCustomerServiceId != -100) {
                    for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                        VideoTagInfo videoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                        Logger.logD(Logger.ROOM, "callServicePersonnel->navideoTagInfo.getType()me:" + videoTagInfo.getUserId() + ",videoTagInfo.getType() :" + videoTagInfo.getType());
                        if (videoTagInfo.getUserId() == mCustomerServiceId) {
                            mVideoLl.removeView(mVideoLl.getChildAt(i));
                            if (mVideoInfoMap.containsKey(videoTagInfo)) {
                                mVideoInfoMap.remove(videoTagInfo);
                            }
                            break;
                        }
                    }
                }
                mCustomerServiceId = roomResultInfo.getUserID();
                callServicePersonnel(roomResultInfo);
            }
            if (result == AppConstant.HelpCode.HELP_CODE_NO_CUSTOM_SERVICE) {
                mHelpToastView.showToastView(getString(R.string.video_room_no_customer_service), false);
            }
            /*if (result == AppConstant.HelpCode.HELP_CODE_NO_SCHEDULE) {
                mHelpToastView.showToastView(getString(R.string.video_room_time_interval), false);
            }*/
        }

        @Override
        public void onselfEvaluatesSeekHelp(int result, VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onselfEvaluatesSeekHelp()->：result:" + result + " ,roomResultInfo:" + roomResultInfo);
            if (result == 0) {
                mHelpToastView.showToastView(getString(R.string.consult_help_thanks_evaluation), true);
            }
        }
    }

    private void submitDomainNameRequester(String httpDnsIp) {
        SubmitDomainNameRequester requester = new SubmitDomainNameRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                Logger.logD(Logger.COMMON, TAG + "->setPesInfo()->SubmitDomainNameRequester:" + baseResult);
            }
        });
        requester.ipAddress = httpDnsIp + "";
        requester.type = AppConstant.DomainNameType.DOMAIN_NAME_TYPE_VIDEO;
        requester.doPost();
    }

    /**
     * 房间其他成员状态变化监听
     */
    private class InnerRoomMemberStateListener implements VideoRoomInterface.OnRoomMemberStateChangeListener {
        @Override
        public void onMemberJoinRoom(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onMemberJoinRoom-->roomResultInfo:" + roomResultInfo);
            Logger.logD(Logger.ROOM, "onMemberJoinRoom--->" + roomResultInfo.getUserID() + "---->:" + roomResultInfo.getUserType());
            if (roomResultInfo == null || roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_CENTRAL_CONTROL
                    || roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_LIVE_BROADCAST_WATCH
                    || roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                return;
            }
            mIssueable = true;
            mCompereMap.remove(roomResultInfo.getUserID());
            if (roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                mHaveCustomerService = true;
                mCurrentCustomerServiceId = roomResultInfo.getUserID();
            }
            startChatTimer();
            if (roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
                getDoctorInfo(roomResultInfo.getUserID());
            } else if (roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                mCustomerServiceCalling = false;
                displayCustomerService(roomResultInfo.getUserID());
            } else {
                if (mDoctorReqDialog != null && mDoctorReqDialog.isShowing()) {
                    mDoctorReqDialog.dismiss();
                }
                if (roomResultInfo.getUserType() != AppConstant.UserType.USER_TYPE_MDT_DOCTOR
                        && roomResultInfo.getUserType() != AppConstant.UserType.USER_TYPE_MDT_PATIENT) {
                    getDoctorInfo(roomResultInfo.getUserID());
                }
            }
            mUserSeats.put(roomResultInfo.getUserID(), roomResultInfo.getUserSeat());
            VideoRoomMember videoRoomMember = new VideoRoomMember();
            videoRoomMember.setUserId(roomResultInfo.getUserID());
            videoRoomMember.setUserName(roomResultInfo.getUserName());
            videoRoomMember.setUserType(roomResultInfo.getUserType());
            videoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_CAMERA);
            if (roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_ASSISTANT_DOCTOR) {
                initVideoInfo(videoRoomMember, VideoTagInfo.VIDEO_TYPE_CAMERA);
                List<VideoRoomMember> ssrcList = new ArrayList<>();
                ssrcList.add(videoRoomMember);
                mMembersLeaveTv.setVisibility(View.GONE);
                mConsultRoomManager.videoSubscribe(ssrcList, mSelfStateChangeListener);
                return;
            }
            List<VideoRoomMember> ssrcList = new ArrayList<>();
            ssrcList.add(videoRoomMember);
            mMembersLeaveTv.setVisibility(View.GONE);
            mConsultRoomManager.videoSubscribe(ssrcList, mSelfStateChangeListener);
            showSmallVideo(true);
            if (videoRoomMember.getUserType() == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE && mIsMeCall && videoRoomMember.getUserId() == mCustomerServiceId) {
                for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                    VideoTagInfo smalllVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                    if (smalllVideoTagInfo != null && smalllVideoTagInfo.getUserId() == videoRoomMember.getUserId() && smalllVideoTagInfo.getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                        switchVideoView((SmallVideoView) mVideoLl.getChildAt(i));
                        break;
                    }
                }
            } else {
                setMemberVideo(videoRoomMember);
            }

            showMemberJoinRoomToast(roomResultInfo, videoRoomMember);
            int state = roomResultInfo.getAvType() == ConsultRoomManager.AV_TYPE_VOICE ? AppConstant.RoomState.STATE_BEING_VOICE : AppConstant.RoomState.STATE_BEING_VIDEO;
            setVideoState(roomResultInfo.getUserID(), state);
            VideoInfo videoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            if (videoInfo != null) {
                videoInfo.setVideoState(state);
            }
            Logger.logD(Logger.ROOM, TAG + "->onMemberJoinRoom()->mVideoInfoMap:" + mVideoInfoMap.size());
            //订单转诊
            if (currentUserId == mOrderDetailInfo.getLaunchDoctorId() && mIsTempOrder) {
                SetDCOrderTempStateRequester requester = new SetDCOrderTempStateRequester(new OnResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, BaseResult baseResult) {
                        mIsTempOrder = false;
                    }

                    @Override
                    public void onFail(BaseResult baseResult) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                requester.orderId = mOrderId;
                requester.tempState = 1;
                requester.start();
            }
        }

        /**
         * 设置成员进入诊室视频显示，包括呼叫进入，敲门进入
         */
        private void setMemberVideo(VideoRoomMember videoRoomMember) {
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo videoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if ((videoTagInfo.getUserId() == videoRoomMember.getUserId() && videoRoomMember.getVideoType() == videoTagInfo.getType()) || videoTagInfo.getUserId() == mCustomerServiceId && videoRoomMember.getVideoType() == videoTagInfo.getType()) {
                    mVideoLl.removeView(mVideoLl.getChildAt(i));
                    if (mVideoInfoMap.containsKey(videoTagInfo)) {
                        mVideoInfoMap.remove(videoTagInfo);
                    }
                    break;
                }
            }
            addOtherVideo(videoRoomMember, videoRoomMember.getVideoType());
        }

        @Override
        public void onMemberExitRoom(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onMemberExitRoom-->roomResultInfo:" + roomResultInfo + ",mCurrentCustomerServiceId:" + mCurrentCustomerServiceId);
            if (roomResultInfo == null) {
                return;
            }
            List<VideoInfo> videoInfos = getVideoInfos(roomResultInfo.getUserID());
            if (videoInfos.size() == 0) {//不在map里面，可能是管理员、监控端
                return;
            }
            if (roomResultInfo.getUserID() == mCurrentCustomerServiceId) {
                mHaveCustomerService = false;
                mIsMeCall = false;
            }
            if (mFlagInRoom) {
                mUserSeats.remove(roomResultInfo.getUserID());
                if (roomResultInfo.getUserID() != 0 && currentUserId == roomResultInfo.getUserID()) {
                    //被后台移出房间
                    kickOff();
                } else {
                    VideoTagInfo bigVideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                    if (bigVideoTagInfo != null && roomResultInfo.getUserID() == bigVideoTagInfo.getUserId()) {
                        mMembersLeaveTv.setVisibility(View.VISIBLE);
                    }
                    showMemberExitRoomToast(roomResultInfo.getUserID());
                    removeMember(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA, VideoTagInfo.VIDEO_TYPE_SHARE, VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
                }
            }
        }

        @Override
        public void onMemberChangeAvType(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onMemberChangeAvType->roomResultInfo:" + roomResultInfo);
            Logger.logD(Logger.ROOM, TAG + "->onMemberChangeAvType->userId:" + roomResultInfo.getUserID() + ", avType:" + roomResultInfo.getAvType() + ", mCurrentAvType:" + mCurrentAvType);
            if (!mFlagInRoom) {
                return;
            }
            if (roomResultInfo.getUserType() != AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                memberChangeAvType(roomResultInfo.getUserID(), roomResultInfo.getAvType());
            }
        }

        @Override
        public void onSpecialMemberChangeAVType(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onSpecialMemberChangeAVType-->roomResultInfo:" + roomResultInfo);
            if (roomResultInfo == null) {
                return;
            }
            changeSelfAvType(roomResultInfo.getAvType());
        }

        @Override
        public void onShutDown(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onShutDown-->roomResultInfo:" + roomResultInfo);
            stopVideo();
            mFlagInRoom = false;
            mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
        }

        @Override
        public void onMemberSpeaking(VideoRoomResultInfo roomResultInfo) {
//            Logger.logD(Logger.ROOM, TAG + "->onMemberSpeaking()->userId:" + roomResultInfo.getUserID() + ", seatState:" + roomResultInfo.getSeatSate());
            for (Map.Entry<VideoTagInfo, VideoInfo> entry : mVideoInfoMap.entrySet()) {
                byte seatIndex = mUserSeats.get(entry.getKey().getUserId()) != null ? (byte) mUserSeats.get(entry.getKey().getUserId()) : 0;
                if (seatIndex >= 1) {
                    int seat = 1 << (seatIndex - 1);
                    setVideoVoice(entry.getValue().getUseId(), (roomResultInfo.getSeatSate() & seat) == seat);
                }
            }
        }

        @Override
        public void onMemberSeatStateChanged(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onMemberSeatStateChanged()->userId:" + roomResultInfo.getUserID() + ", userSeat:" + roomResultInfo.getUserSeat());
            mUserSeats.put(roomResultInfo.getUserID(), roomResultInfo.getUserSeat());
            if (roomResultInfo.getUserID() == currentUserId) {
                mIsMicOpen = roomResultInfo.getUserSeat() > 0;
                setMicState();
                mToastTlv.showToastView(mIsMicOpen ? getString(R.string.video_room_state_open_mic_by_admin)
                        : getString(R.string.video_room_state_close_mic_by_admin));
            }
        }

        @Override
        public void onRoomOperation(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->roomResultInfo：" + roomResultInfo);
            if (roomResultInfo == null) {
                return;
            }
            roomOperation(roomResultInfo);
        }

        @Override
        public void onRoomSeekHelpEnd(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onRoomSeekHelpEnd()->roomResultInfo：" + roomResultInfo);
            if (roomResultInfo.getUserID() == mCustomerServiceId && mIsMeCall) {
                showHelpEndDialog(roomResultInfo);
            }
        }

        private void roomOperation(VideoRoomResultInfo roomResultInfo) {
            switch (roomResultInfo.getOperation()) {
                //设置主屏
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_MAIN_SCREEN:
                    operationMainScreen(roomResultInfo);
                    break;

                //被医生助理移出房间
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_KICK_OFF:
                    operationKickOff(roomResultInfo);
                    break;

                //禁言
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_FORBID_SPEAKING:
                    operationForbidSpeaking(roomResultInfo);
                    break;

                //解除禁言
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_RELIEVE_SPEAKING:
                    operationRelieveSpeaking(roomResultInfo);
                    break;

                //医生助理隐身
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_HIDE:
                    showSmallVideo(true);
                    operationHide(roomResultInfo);
                    break;

                //医生助理取消隐身
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_DISPLAY:
                    showSmallVideo(true);
                    operationDisplay(roomResultInfo);
                    break;

                //取消主屏
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_CANCLE_MAIN_SCREEN:
                    break;

                //播放视频文件
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_PLAY_VIDEO_FILE:
                    Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->播放视频文件");
                    showSmallVideo(true);
                    operationPlayVideoFile(roomResultInfo);
                    break;

                //停止播放视频文件
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_STOP_PLAY_VIDEO_FILE:
                    Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->停止播放视频文件");
                    VideoInfo tagertVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
                    Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->停止播放视频文件－>tagertVideoInfo:" + tagertVideoInfo);
                    if (tagertVideoInfo == null) {//不在map里面，可能是管理员、监控端
                        return;
                    }
                    operationStopPlayVideoFile(roomResultInfo);
                    break;

                //屏幕共享
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_SCREEN_SHARE:
                    Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->屏幕共享");
                    showSmallVideo(true);
                    operationScreenShare(roomResultInfo);
                    break;

                //取消屏幕共享
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_CANCLE_SCREEN_SHARE:
                    Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->取消屏幕共享");
                    VideoInfo cancleShareVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_SHARE);
                    if (cancleShareVideoInfo == null) {//不在map里面，可能是管理员、监控端
                        return;
                    }
                    operationCancelScreenShare(roomResultInfo);
                    break;

                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_DOCTOR_ADVICE_MAKE_SURE:
                    if (roomResultInfo.getVideoType() == 0) {
                        showReferralMakeSureDialog();
                    } else if (roomResultInfo.getVideoType() == 1) {
                        showSuggestionMakeSureDialog();
                    }
                    break;
                case AppConstant.RoomOperationCode.ROOM_OPERATION_CODE_DOCTOR_PATIENT_ADD:
                    String patientName = "";
                    try {
                        JSONObject jsonObject = new JSONObject(roomResultInfo.getMsgContent());
                        patientName = jsonObject.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showToastViewDelayed(patientName + "患者资料已上传");
                    break;
            }
        }

        /**
         * 设置主屏
         *
         * @param roomResultInfo
         */
        private void operationMainScreen(VideoRoomResultInfo roomResultInfo) {
            if (roomResultInfo.getUserID() != currentUserId) {
                judgeSwitchVideoView(roomResultInfo.getUserID(), roomResultInfo.getVideoType());
            }
        }

        /**
         * 被医生助理移出房间
         *
         * @param roomResultInfo
         */
        private void operationKickOff(VideoRoomResultInfo roomResultInfo) {
            if (roomResultInfo.getUserID() == currentUserId) {
                kickOff();
            } else {
                VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
                if (videoTagInfo != null
                        && videoTagInfo.getUserId() == roomResultInfo.getUserID()) {
                    mMembersLeaveTv.setVisibility(View.VISIBLE);
                }
                showMemberExitRoomToast(roomResultInfo.getUserID());
                removeMember(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA, VideoTagInfo.VIDEO_TYPE_SHARE, VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
            }
        }

        /**
         * 禁言
         *
         * @param roomResultInfo
         */
        private void operationForbidSpeaking(VideoRoomResultInfo roomResultInfo) {
            if (roomResultInfo.getUserID() == currentUserId) {
                mRoomOperationChangeSeat = true;
                changeSeatState((byte) 0);
            }
        }

        /**
         * 解除禁言
         *
         * @param roomResultInfo
         */
        private void operationRelieveSpeaking(VideoRoomResultInfo roomResultInfo) {
            if (roomResultInfo.getUserID() == currentUserId && getSeat() != 0) {
                mRoomOperationChangeSeat = true;
                changeSeatState(getSeat());
            }
        }

        /**
         * 医生助理隐身
         *
         * @param roomResultInfo
         */
        private void operationHide(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->operationDisplay()->operationHide->roomResultInfo:" + getUserType(roomResultInfo));
            VideoTagInfo videoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
            if (videoTagInfo != null
                    && videoTagInfo.getUserId() == roomResultInfo.getUserID()
                    && videoTagInfo.getType() == VideoTagInfo.VIDEO_TYPE_CAMERA) {
                mMembersLeaveTv.setVisibility(View.VISIBLE);
            }
            if (getUserType(roomResultInfo) == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                mToastTlv.showToastView(getString(R.string.video_room_state_leave_room, getString(R.string.consult_help_customer_service_personnel)));
            } else if (getUserType(roomResultInfo) != AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE && mCompereMap.containsKey(roomResultInfo.getUserID())) {
                mToastTlv.showToastView(getString(R.string.video_room_state_leave_room, getString(R.string.consult_room_doctor_assistant)));
            }
            removeVideo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            mCompereMap.put(roomResultInfo.getUserID(), roomResultInfo.getUserID());
        }

        public int getUserType(VideoRoomResultInfo roomResultInfo) {
            int type = 0;
            for (VideoInfo info : mVideoInfoMap.values()) {
                if (roomResultInfo.getUserID() == info.getUseId()) {
                    type = info.getUserType();
                    break;
                }
            }
            return type;
        }

        /**
         * 医生助理在线
         *
         * @param roomResultInfo
         */
        private void operationDisplay(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->operationDisplay()->roomResultInfo:" + getUserType(roomResultInfo));
            if (getUserType(roomResultInfo) == AppConstant.UserType.USER_TYPE_CUSTOMER_SERVICE) {
                mToastTlv.showToastView(getString(R.string.video_room_state_entered_room, getString(R.string.consult_help_customer_service_personnel)));
            } else {
                mToastTlv.showToastView(getString(R.string.video_room_state_entered_room, getString(R.string.consult_room_doctor_assistant)));
            }
            VideoInfo tempVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            if (mBigVideoRl.getTag() == null) {
                setVideoQuality(tempVideoInfo, false);
                initBigVideo(tempVideoInfo, VideoTagInfo.VIDEO_TYPE_CAMERA);
            } else {
                setVideoQuality(tempVideoInfo, true);
                addSmallVideo(tempVideoInfo, VideoTagInfo.VIDEO_TYPE_CAMERA);
            }
            mEnableAutoAdjust = true;
            mInitiativeAdjust = true;
            mLMVideoMgr.resetDisplayVideoView(tempVideoInfo.getSsrc(), tempVideoInfo.getGlSurfaceView(), tempVideoInfo.getRendererGui());
        }

        /**
         * 播放视频文件
         *
         * @param roomResultInfo
         */
        private void operationPlayVideoFile(final VideoRoomResultInfo roomResultInfo) {
            if (!mFlagInRoom) {
                return;
            }
            final VideoRoomMember videoRoomMember = new VideoRoomMember();
            videoRoomMember.setUserId(roomResultInfo.getUserID());
            videoRoomMember.setUserName(roomResultInfo.getUserName());
            VideoInfo getVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            videoRoomMember.setUserType(getVideoInfo == null ? AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR : getVideoInfo.getUserType());
            videoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
            List<VideoRoomMember> videoRoomMembers = new ArrayList<>();
            videoRoomMembers.add(videoRoomMember);

            removeMemberView();
            mConsultRoomManager.videoSubscribe(videoRoomMembers, mSelfStateChangeListener);
            setMemberVideo(videoRoomMember);

            int state = roomResultInfo.getAvType() == ConsultRoomManager.AV_TYPE_VOICE ? AppConstant.RoomState.STATE_BEING_VOICE : AppConstant.RoomState.STATE_BEING_VIDEO;
            setVideoState(roomResultInfo.getUserID(), state);
            VideoInfo videoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
            if (videoInfo != null) {
                videoInfo.setVideoState(state);
            }
            judgeSwitchVideoView(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
        }

        /**
         * 停止播放视频文件
         *
         * @param roomResultInfo
         */
        private void operationStopPlayVideoFile(VideoRoomResultInfo roomResultInfo) {
            if (mFlagInRoom) {
                removeMember(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_VIDEO_FILE);
                judgeSwitchVideoView(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            }
        }

        /**
         * 屏幕共享
         *
         * @param roomResultInfo
         */
        private void operationScreenShare(final VideoRoomResultInfo roomResultInfo) {
            final VideoRoomMember shareVideoRoomMember = new VideoRoomMember();
            shareVideoRoomMember.setUserId(roomResultInfo.getUserID());
            shareVideoRoomMember.setUserName(roomResultInfo.getUserName());
            VideoInfo getShareVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            shareVideoRoomMember.setUserType(getShareVideoInfo == null ? AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR : getShareVideoInfo.getUserType());
            shareVideoRoomMember.setVideoType(VideoTagInfo.VIDEO_TYPE_SHARE);
            List<VideoRoomMember> shareVideoRoomMembers = new ArrayList<>();
            shareVideoRoomMembers.add(shareVideoRoomMember);

            removeMemberView();
            mConsultRoomManager.videoSubscribe(shareVideoRoomMembers, mSelfStateChangeListener);
            setMemberVideo(shareVideoRoomMember);

            int shareState = roomResultInfo.getAvType() == ConsultRoomManager.AV_TYPE_VOICE ? AppConstant.RoomState.STATE_BEING_VOICE : AppConstant.RoomState.STATE_BEING_VIDEO;
            setVideoState(roomResultInfo.getUserID(), shareState);
            VideoInfo shareVideoInfo = getVideoInfo(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_SHARE);
            if (shareVideoInfo != null) {
                shareVideoInfo.setVideoState(shareState);
            }
            judgeSwitchVideoView(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_SHARE);
        }

        /**
         * 取消屏幕共享
         *
         * @param roomResultInfo
         */
        private void operationCancelScreenShare(VideoRoomResultInfo roomResultInfo) {
            if (mFlagInRoom) {
                removeMember(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_SHARE);
                judgeSwitchVideoView(roomResultInfo.getUserID(), VideoTagInfo.VIDEO_TYPE_CAMERA);
            }
        }

    }

    /**
     * 成员播放视频时，移除成员的共享视频
     *
     * @param userId
     * @param type
     */
    private void removeMemberView() {
        VideoTagInfo bigideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (bigideoTagInfo != null
                && bigideoTagInfo.getType() != VideoTagInfo.VIDEO_TYPE_CAMERA) {
            removeMember(bigideoTagInfo.getUserId(), VideoTagInfo.VIDEO_TYPE_CAMERA);
        } else {
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo tempVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if (tempVideoTagInfo != null
                        && tempVideoTagInfo.getType() != VideoTagInfo.VIDEO_TYPE_CAMERA) {
                    removeMember(tempVideoTagInfo.getUserId(), VideoTagInfo.VIDEO_TYPE_CAMERA);
                    break;
                }
            }
        }
    }

    /**
     * 成员播放视频时，移除成员的共享视频
     *
     * @param userId
     * @param type
     */
    private void removeMemberView(int userId, int type) {
        VideoTagInfo bigideoTagInfo = (VideoTagInfo) mBigVideoRl.getTag();
        if (bigideoTagInfo != null
                && bigideoTagInfo.getUserId() == userId && bigideoTagInfo.getType() == type) {
            removeMember(userId, VideoTagInfo.VIDEO_TYPE_SHARE);
        } else {
            for (int i = 0; i < mVideoLl.getChildCount(); i++) {
                VideoTagInfo tempVideoTagInfo = (VideoTagInfo) mVideoLl.getChildAt(i).getTag();
                if (tempVideoTagInfo != null
                        && tempVideoTagInfo.getUserId() == userId && tempVideoTagInfo.getType() == type) {
                    removeMember(userId, VideoTagInfo.VIDEO_TYPE_SHARE);
                    break;
                }
            }
        }
    }

    private static class MyVideoRendererGui extends VideoRendererGui {
        private int mWidth;
        private int mHeight;
        private boolean mIsCutBitmap;
        private OnCutBitmapListener mOnCutBitmapListener;

        public MyVideoRendererGui(GLSurfaceView surface, Context context) {
            super(surface, context);
        }

        public void setCutBitmap(int width, int height, OnCutBitmapListener listener) {
            mWidth = width;
            mHeight = height;
            mIsCutBitmap = true;
            mOnCutBitmapListener = listener;
        }

        @Override
        public void onDrawFrame(GL10 unused) {
            super.onDrawFrame(unused);
            if (mIsCutBitmap) {
                mIsCutBitmap = false;
                Bitmap bmp = createBitmapFromGLSurface(0, 0, mWidth,
                        mHeight, unused);
                mOnCutBitmapListener.OnCutBitmapfinished(bmp);
            }
        }

        private Bitmap createBitmapFromGLSurface(int x, int y, int w, int h, GL10 gl) {
            if (w <= 0 || h < 0) {
                return null;
            }
            int[] bitmapBuffer = new int[w * h];
            int[] bitmapSource = new int[w * h];
            IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
            intBuffer.position(0);
            try {
                gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
                        intBuffer);
                int offset1, offset2;
                for (int i = 0; i < h; i++) {
                    offset1 = i * w;
                    offset2 = (h - i - 1) * w;
                    for (int j = 0; j < w; j++) {
                        int texturePixel = bitmapBuffer[offset1 + j];
                        int blue = (texturePixel >> 16) & 0xff;
                        int red = (texturePixel << 16) & 0x00ff0000;
                        int pixel = (texturePixel & 0xff00ff00) | red | blue;
                        bitmapSource[offset2 + j] = pixel;
                    }
                }
            } catch (GLException e) {
                return null;
            }
            return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_4444);
        }

        public interface OnCutBitmapListener {
            void OnCutBitmapfinished(Bitmap bmp);
        }
    }

    private void showCallDoctorListDialog() {
        DoctorChoiceDialog dialog = new DoctorChoiceDialog(this, R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setListener(new DoctorChoiceDialog.DoctorChoiceDialogStateChangeListener() {
            @Override
            public void onChoicedDoctorList(List<DCDoctorSectionInfo> doctorSectionInfos) {
                for (DCDoctorSectionInfo info : doctorSectionInfos) {
                    callDoctor(info.getUserId());
                }
            }
        });
        dialog.setMemberList(getMemberList());
        dialog.setItemId(mOrderDetailInfo.getItemId());
    }

    /**
     * @param editState
     * @param issueType
     */
    private void setDCAdviceEditState(int editState, int issueType) {
        if (mIssueable || userType == AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR) {
            mDCManager.setDCAdviceEditState(mOrderId, editState, new OnResultListener<DCAdviceEditorInfo>() {
                @Override
                public void onResult(BaseResult baseResult, DCAdviceEditorInfo dcAdviceEditorInfo) {
                    if (baseResult.getCode() == RESULT_SUCCESS) {
                        if (editState == 1) {
                            showIssueDialog(issueType);
                        } else {
                            if (mIssueOrderDialog.isShowing()) {
                                mIssueOrderDialog.dismiss();
                            }
                        }
                    } else if (baseResult.getCode() == RESULT_DB_EXISTED) {
                        if (editState == 1) {
                            new CommonDialog.Builder(getThisActivity())
                                    .setTitle("提示")
                                    .setMessage(dcAdviceEditorInfo.getEditName() + "医生正在编辑患者资料")
                                    .setCancelable(false)
                                    .setNegativeButton("确定", () -> {
                                    })
                                    .show();
                        }
                    } else {
                        ToastUtils.showShort(R.string.no_network_connection);
                    }
                }
            });
        }
    }

    private void showIssueDialog(int issueType) {
        if (mIssueOrderDialog != null && mIssueOrderDialog.isShowing()) {
            mIssueOrderDialog.dismiss();
        }
        mIssueOrderDialog = new IssueReferralAndSuggestionDialog(this, R.style.custom_noActionbar_window_style);
        mIssueOrderDialog.setCanceledOnTouchOutside(false);
        mIssueOrderDialog.setCancelable(true);
        mIssueOrderDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setDCAdviceEditState(0, IssueReferralAndSuggestionDialog.ISSUE_TYPE_REFERRAL);
            }
        });
        mIssueOrderDialog.display(issueType, mOrderDetailInfo, new IssueReferralAndSuggestionDialog.OnIssueReferralAndSuggestionDialogStateChangeListener() {
            @Override
            public void onSubmitReferralSuccess() {
                showReferralMakeSureDialog();
                mConsultRoomManager.sendDoctorAdviceMakeSureOpt(0);
            }

            @Override
            public void onSubmitSuggestionSuccess() {
                showSuggestionMakeSureDialog();
                mConsultRoomManager.sendDoctorAdviceMakeSureOpt(1);
            }
        });
    }

    private void showReferralMakeSureDialog() {
        if (mReferralMakeSureDialog != null && mReferralMakeSureDialog.isShowing()) {
            mReferralMakeSureDialog.dismiss();
        }
        mReferralMakeSureDialog = new ReferralMakeSureDialog(DCRoomActivity.this, R.style.custom_noActionbar_window_style);
        mReferralMakeSureDialog.setCanceledOnTouchOutside(false);
        mReferralMakeSureDialog.setCancelable(true);
        mReferralMakeSureDialog.show();
        mReferralMakeSureDialog.setOrderDetailInfo(mOrderDetailInfo);
        mReferralMakeSureDialog.setReferralMakeSureDialogStateChangeListener(new ReferralMakeSureDialog.ReferralMakeSureDialogStateChangeListener() {
            @Override
            public void onModifyClicked() {
                setDCAdviceEditState(1, IssueReferralAndSuggestionDialog.ISSUE_TYPE_REFERRAL);
            }
        });
    }

    private void showSuggestionMakeSureDialog() {
        if (mSuggestionMakeSureDialog != null && mSuggestionMakeSureDialog.isShowing()) {
            mSuggestionMakeSureDialog.dismiss();
        }
        mSuggestionMakeSureDialog = new SuggestionMakeSureDialog(DCRoomActivity.this, R.style.custom_noActionbar_window_style);
        mSuggestionMakeSureDialog.setCanceledOnTouchOutside(false);
        mSuggestionMakeSureDialog.setCancelable(true);
        mSuggestionMakeSureDialog.show();
        mSuggestionMakeSureDialog.setOrderDetailInfo(mOrderDetailInfo);
        mSuggestionMakeSureDialog.setSuggestionMakeSureDialogStateChangeListener(new SuggestionMakeSureDialog.SuggestionMakeSureDialogStateChangeListener() {
            @Override
            public void onModifyClicked() {
                setDCAdviceEditState(1, IssueReferralAndSuggestionDialog.ISSUE_TYPE_SUGGESTION);
            }
        });
    }

    private void callDoctor(int userId) {
        if (!mFlagInRoom) {
            return;
        }
        //呼叫医生计时器
        final Timer doctorTimer = new Timer();
        addMembersJoinVideo(userId);
        int userType = AppConstant.UserType.USER_TYPE_MDT_DOCTOR;
        if (userId == mOrderDetailInfo.getDoctorId()) {
            userType = AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;
        } else if (userId == mOrderDetailInfo.getLaunchDoctorId()) {
            userType = USER_TYPE_ATTENDING_DOCTOR;
        }
        ConsultRoomUtils.sendCallMessage(currentUserId, userId, mOrderId, userType, 16);
        mTimers.append(userId, doctorTimer);
        doctorTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setVideoState(userId, AppConstant.RoomState.STATE_MISS);
                        VideoInfo videoInfo = getVideoInfo(userId, VideoTagInfo.VIDEO_TYPE_CAMERA);
                        if (videoInfo != null) {
                            videoInfo.setVideoState(AppConstant.RoomState.STATE_MISS);
                        }
                        mToastTlv.showToastView("对方无应答，请稍后重试");
                    }
                });
            }
        }, CALL_WAIT_DURATION);
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACTION, AppConstant.ACTION_CHANGE_TAB);
        startActivity(intent);
        finish();
    }

    private int getUserType(int userId) {
        if (mOrderDetailInfo == null) {
            return AppConstant.UserType.USER_TYPE_MDT_DOCTOR;
        }
        int userType = AppConstant.UserType.USER_TYPE_MDT_DOCTOR;
        if (userId == mOrderDetailInfo.getDoctorId()) {
            userType = AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;
        } else if (userId == mOrderDetailInfo.getLaunchDoctorId()) {
            userType = USER_TYPE_ATTENDING_DOCTOR;
        }
        return userType;
    }
}
