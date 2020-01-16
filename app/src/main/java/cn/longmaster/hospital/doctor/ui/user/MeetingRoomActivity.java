package cn.longmaster.hospital.doctor.ui.user;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.VideoRoomResultCode;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomMember;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomResultInfo;
import cn.longmaster.hospital.doctor.core.entity.user.MeetingRoomInfo;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.room.ConsultRoomManager;
import cn.longmaster.hospital.doctor.core.manager.room.VideoRoomInterface;
import cn.longmaster.hospital.doctor.core.manager.user.LoginStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.user.SubmitDomainNameRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.video.KickOffActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.MeetingRoomAdapter;
import cn.longmaster.hospital.doctor.util.NetWorkUtil;
import cn.longmaster.hospital.doctor.view.MeetingToastView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.phoneplus.audioadapter.model.AudioAdapter;
import cn.longmaster.phoneplus.audioadapter.model.AudioModule;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

public class MeetingRoomActivity extends BaseActivity implements NetStateReceiver.NetworkStateChangedListener, MessageStateChangeListener, MeetingToastView.OnToastPositiveClickListener {
    private final String TAG = MeetingRoomActivity.class.getSimpleName();
    @FindViewById(R.id.activity_meeting_room_grid_view)
    private RecyclerView mGridView;
    @FindViewById(R.id.activity_meeting_room_title_view)
    private TextView mTitleView;
    @FindViewById(R.id.activity_meeting_room_microphone)
    private TextView mMicrophoneTv;
    @FindViewById(R.id.activity_meeting_room_microphone_iv)
    private ImageView mMicrophoneIv;
    @FindViewById(R.id.activity_meeting_room_mute)
    private TextView mMuteTv;
    @FindViewById(R.id.activity_meeting_room_mute_iv)
    private ImageView mMuteIv;
    @FindViewById(R.id.activity_meeting_room_toast_tlv)
    private MeetingToastView mToastTlv;
    @FindViewById(R.id.activity_meeting_room_loading)
    private RelativeLayout mLoadingView;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultRoomManager mConsultRoomManager;
    @AppApplication.Manager
    private AudioAdapterManager mAudioAdapterManager;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private DcpManager mDcpManager;

    private int mRoomId;
    private String mRoomName;
    private boolean mIsOpenMute = false;
    private MeetingRoomAdapter mMeetingRoomAdapter;
    private List<MeetingRoomInfo> mMeetingRoomMembersList = new ArrayList<>();
    //扬声器开关
    private final int SPEAKER_TYPE_OPEN = 1;
    private final int SPEAKER_TYPE_CLOSE = 0;
    private ProgressDialog mProgressDialog;
    private VideoRoomInterface.OnRoomSelfStateChangeListener mSelfStateChangeListener;//自身房间状态变化通知
    private int mCurrentAvType = ConsultRoomManager.AV_TYPE_VIDEO;
    private int mStateSpeaker = SPEAKER_TYPE_OPEN;
    private boolean mIsMicOpen = false;
    private int mUserType = 5;//5为与会人员
    private SparseArray<Byte> mUserSeats = new SparseArray<>();
    private int myUserId;
    private boolean mFlagInRoom = false;//是否在房间
    private boolean mFlagCloseUI = true;//退出房间时是否关闭UI
    private boolean mFlagExitRoom = false;//是否退出房间
    private boolean mFlagTest = false;//是否权限测试过
    private boolean mFlagKickOff = false;//是否被提出房间
    private boolean mFlagShowNetworkDisconnect = true;//是否弹网络连接失败提示
    private boolean mIsDestroyed = false;
    private boolean mIsIdle = true;//是否空闲，可以去做操作
    private int mCurrentCustomerServiceId = 0;
    private boolean mOutsideSwitch = true;//外放开关
    private boolean mEnableAudio = true;
    private long lastClickTime = 0L;
    private long lastMemberSpeakingTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 100;  // 快速点击间隔
    private static final int FAST_CLICK_DELAY_SPEAKING_TIME = 500;  // 语音回调处理时间
    // 音频获取源
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    public static int bufferSizeInBytes = 0;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0: //拔出耳机
                        Logger.logD(Logger.ROOM, TAG + "->拔出耳机");
                        //  拔出耳机
                        mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKERON);
                        mStateSpeaker = SPEAKER_TYPE_OPEN;
                        mOutsideSwitch = true;
                        break;

                    case 1: //插耳机
                        Logger.logD(Logger.ROOM, TAG + "->插耳机");
                        //  插耳机
                        mOutsideSwitch = false;
                        mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKEROFF);
                        mStateSpeaker = SPEAKER_TYPE_CLOSE;
                        break;

                    default:
                        break;
                }
            } else if (intent.getAction().equals(BluetoothHeadset.EXTRA_STATE)) {
                Logger.logD(Logger.ROOM, "广播：" + BluetoothHeadset.EXTRA_STATE);
            } else if (intent.getAction().equals(ExtraDataKeyConfig.ACTION_BROADCAST_RECEIVER_FINISH_VIDEO_ROOM)) {
                finish();
            }
        }
    };
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL); // 关键：添加该句可以调节音量大小
        AppApplication.getInstance().getManager(LocalNotificationManager.class).cancleAllNotification();
        setContentView(R.layout.activity_meeting_room);
        ViewInjecter.inject(this);
        initData();
        initView();
        initAdapter();
        setPccParam();
        initListener();
        regBroadcastReceiver();
        joinRoom(mRoomId);
    }

    private void initData() {
        mRoomId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_METING_ROOM_ID, 0);
        mRoomName = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_METING_ROOM_NAME);
        myUserId = mUserInfoManager.getCurrentUserInfo().getUserId();
    }

    private void initView() {
        mTitleView.setText(mRoomName);
    }

    private void initAdapter() {
        mMeetingRoomAdapter = new MeetingRoomAdapter(R.layout.item_meeting_room_role, mMeetingRoomMembersList);
        mGridView.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getActivity(), 4));
        mGridView.setAdapter(mMeetingRoomAdapter);
    }

    @OnClick({R.id.activity_meeting_room_microphone_view,
            R.id.activity_meeting_room_mute_view,
            R.id.activity_meeting_room_exit_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_meeting_room_microphone_view:
                setMicrophone();
                break;

            case R.id.activity_meeting_room_mute_view:
                setMute();
                break;

            case R.id.activity_meeting_room_exit_view:
                showExitDialog();
                break;
        }
    }

    private void setMicrophone() {
        if (mFlagInRoom) {
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
                showPermissionDialog();
            }
        }
    }

    private void setMute() {
        if (System.currentTimeMillis() - lastClickTime < FAST_CLICK_DELAY_TIME) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        if (mIsOpenMute) {
            mMuteTv.setText(getString(R.string.meeting_mute));
            mMuteIv.setImageResource(R.drawable.ic_meeting_mute_open);
            showCenterToast(getString(R.string.meeting_mute_cancel));
            mDcpManager.pausePlay(false);
        } else {
            mMuteTv.setText(getString(R.string.meeting_cancel_mute));
            mMuteIv.setImageResource(R.drawable.ic_meeting_mute_close);
            showCenterToast(getString(R.string.meeting_already_mute));
            mDcpManager.pausePlay(true);
        }
        mIsOpenMute = !mIsOpenMute;
    }

    private void showCenterToast(String str) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    /**
     * 退出Dialog
     */
    private void showExitDialog() {
        new CommonDialog.Builder(this)
                .setTitle(getString(R.string.meeting_exit_tip))
                .setMessage(getString(R.string.meeting_exit_meeting_room))
                .setPositiveButton(R.string.video_dialog_cancel, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                    }
                })
                .setNegativeButton(R.string.video_dialog_ok, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        exitVideoRoom();
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
        Logger.logD(Logger.ROOM, TAG + "onDestroy()");
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
    }

    /**
     * 开始视频就诊
     */
    private void joinRoom(final int roomId) {
        Logger.logD(Logger.ROOM, "joinRoom()-->");
        Logger.logD(Logger.ROOM, "joinRoom()-->" + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO));
        if (NetStateReceiver.hasNetConnected(this)) {
            mAudioAdapterManager.setMode(AudioModule.NAME_PROCESSINCALL);
            Disposable disposable = PermissionHelper
                    .init(getActivity())
                    .addPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                    .requestEachCombined()
                    .subscribe(permission -> {
                        if (permission.granted) {
                            joinVideoRoom(roomId);
                            // `permission.name` is granted !
                        } else {
                            new CommonDialog.Builder(getActivity())
                                    .setTitle("权限授予")
                                    .setMessage("在设置-应用管理-权限中开启相机和录音权限，才能正常使用视频就诊")
                                    .setPositiveButton(R.string.cancel, this::finish)
                                    .setCancelable(false)
                                    .setNegativeButton(R.string.confirm, () -> {
                                        Utils.gotoAppDetailSetting();
                                        finish();
                                    })
                                    .show();
                        }
                    });
            compositeDisposable.add(disposable);
        } else {
            mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
            showToast(R.string.no_network_connection);
        }
    }

    private void joinVideoRoom(int roomId) {
        if (!mIsIdle || mFlagInRoom) {
            return;
        }
        mIsIdle = false;
        showProgressDialog();
        Logger.logD(Logger.ROOM, "joinVideoRoom()-->roomId:" + roomId + ",mUserType:" + mUserType);
        mConsultRoomManager.joinVideoRoom(roomId, roomId, mUserType, "", NetWorkUtil.getNetworkType(getActivity()), mSelfStateChangeListener);
    }

    @Override
    public void onNetworkStateChanged(int netWorkType) {
        Logger.logD(Logger.ROOM, "onNetworkStateChanged()网络状态（-1无网络）：" + netWorkType);
        if (netWorkType == NetStateReceiver.NETWORK_TYPE_NONE) {
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPositiveClicked(String clickText) {
        if (clickText.equals(getString(R.string.video_dialog_retry))) {
            if (!mFlagInRoom) {
                if (NetStateReceiver.NETWORK_TYPE_WIFI == NetStateReceiver.getCurrentNetType(this) || NetStateReceiver.NETWOKR_TYPE_MOBILE == NetStateReceiver.getCurrentNetType(this)) {
                    Logger.logD(Logger.ROOM, "onPositiveClicke-->重试");
                    showProgressDialog();
                    mFlagShowNetworkDisconnect = true;
                    mMeetingRoomMembersList.clear();
                    mMeetingRoomAdapter.notifyDataSetChanged();
                    if (!AppApplication.getInstance().isOnLogin()) {
                        mUserInfoManager.reLogin(new LoginStateChangeListener() {
                            @Override
                            public void onLoginStateChanged(int code, int msg) {
                                Logger.logD(Logger.ROOM, "onPositiveClicke-->onLoginStateChanged:code:" + code + ",isLogin:" + AppApplication.getInstance().isLogin());
                                if (code == 0 && AppApplication.getInstance().isLogin()) {
                                    AppHandlerProxy.runOnUIThread(new Runnable() {
                                        @Override
                                        public void run() {
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
                        joinRoom(mRoomId);
                    }
                } else {
                    mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
                }
            }
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
            dismissProgressDialog();
            if (mFlagInRoom) {
                return;
            }
            mIsIdle = true;
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {//加入成功
                Logger.logD(Logger.ROOM, "onSelfJoinRoom()->加入成功->");
                mFlagInRoom = true;
                mRoomId = resultInfo.getRoomID();
                Logger.logD(Logger.ROOM, "onSelfJoinRoom->mFlagInRoom:" + mFlagInRoom);
                mAudioAdapterManager.setMode(AudioModule.NAME_AFTERVOICE);
                //设置放音模式,有耳机时为耳机否则为外放
                AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isWiredHeadsetOn()) {
                    mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKEROFF);
                    mOutsideSwitch = false;
                } else {
                    mAudioAdapterManager.setMode(AudioModule.NAME_SPEAKERON);
                    mOutsideSwitch = true;
                }
                mAudioAdapterManager.setMode(mStateSpeaker == SPEAKER_TYPE_OPEN ? AudioModule.NAME_SPEAKERON : AudioModule.NAME_SPEAKEROFF);
                Logger.logD(Logger.ROOM, "onSelfJoinRoom->resultInfo.getPvsUrl():" + resultInfo.getPvsUrl() + ",resultInfo.getPvsIP():" + resultInfo.getPvsIP());
                addVideos(resultInfo.getMemberList());
                setMicState();
                if (mIsMicOpen) {
                    changeSeatState(getSeat());
                }
                if (mIsOpenMute) {
                    mDcpManager.pausePlay(true);
                } else {
                    mDcpManager.pausePlay(false);
                }
            } else {
                showCreateRoomFailedDialog();
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

        @Override
        public void onSelfChangeSeatState(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, "onSelfChangeSeatState->result:" + result + "->resultInfo:" + resultInfo);
            mIsIdle = true;
            mLoadingView.setVisibility(View.GONE);
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                if (resultInfo.getUserSeat() > 0) {
                    mIsMicOpen = true;
                    showCenterToast(getString(R.string.meeting_mic_open));
                    mMicrophoneTv.setText(getString(R.string.meeting_mic_close));
                    mMicrophoneIv.setImageResource(R.drawable.ic_microphone_open);
                } else {
                    mIsMicOpen = false;
                    showCenterToast(getString(R.string.meeting_mic_already_close));
                    mMicrophoneTv.setText(getString(R.string.meeting_mic_already_open));
                    mMicrophoneIv.setImageResource(R.drawable.ic_microphone_close);
                }
                mUserSeats.put(myUserId, resultInfo.getUserSeat());
                for (int i = 0; i < mMeetingRoomMembersList.size(); i++) {
                    if (mMeetingRoomMembersList.get(i).getUserId() == myUserId) {
                        mMeetingRoomMembersList.get(i).setUserSeat(resultInfo.getUserSeat());
                        if (resultInfo.getUserSeat() == 0) {
                            mMeetingRoomMembersList.get(i).setSpeak(false);
                            mMeetingRoomAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
                setMicState();
            } else {
                getRoomMemberSeat();
            }
        }

        @Override
        public void onSelfExitRoom(int result, VideoRoomResultInfo resultInfo) {
            mIsIdle = true;
            Logger.logD(Logger.ROOM, "onSelfExitRoom->result:" + result + "->resultInfo:" + resultInfo + "->mFlagInRoom:" + mFlagInRoom);
            if (mFlagKickOff) { //被移出房间不做处理,
                return;
            }
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                mFlagInRoom = false;
                Logger.logD(Logger.ROOM, "onSelfExitRoom->mFlagExitRoom:" + mFlagExitRoom);
                Logger.logD(Logger.ROOM, "onSelfExitRoom（）->Thread.currentThread().getName():" + Thread.currentThread().getName());
                Logger.logD(Logger.ROOM, "onSelfExitRoom->mCurrentAvType:" + mCurrentAvType);
                exitRoomDeal();
            } else {
                dismissProgressDialog();
                showToast(getString(R.string.video_room_state_exist_room_failed, result));
            }
        }

        @Override
        public void onSelfChangeAVType(int result, VideoRoomResultInfo resultInfo) {
            mIsIdle = true;
            Logger.logD(Logger.ROOM, "onSelfChangeAVType-->resultInfo:" + resultInfo);
        }

        @Override
        public void onSelfGetRoomMember(int result, VideoRoomResultInfo resultInfo) {
            Logger.logD(Logger.ROOM, "onSelfGetRoomMember-->result:" + result + "-->resultInfo:" + resultInfo);
            if (result == VideoRoomResultCode.VIDEO_ROOM_RESULT_CODE_SUCCESS) {
                updateMemberSeat(resultInfo);
            }
        }

        @Override
        public void onSelfVideoSubscribe(int result, VideoRoomResultInfo resultInfo) {
        }

        @Override
        public void onSelfSeekHelp(int result, VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onSelfSeekHelp()->：result:" + result + " ,roomResultInfo:" + roomResultInfo);
        }

        @Override
        public void onselfEvaluatesSeekHelp(int result, VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onselfEvaluatesSeekHelp()->：result:" + result + " ,roomResultInfo:" + roomResultInfo);
        }
    }

    /**
     * 房间其他成员状态变化监听
     */
    private class InnerRoomMemberStateListener implements VideoRoomInterface.OnRoomMemberStateChangeListener {
        @Override
        public void onMemberJoinRoom(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onMemberJoinRoom-->roomResultInfo:" + roomResultInfo);
            Logger.logD(Logger.ROOM, "onMemberJoinRoom--->" + roomResultInfo.getUserID() + "---->:" + roomResultInfo.getUserType());
            if (roomResultInfo == null) {
                return;
            }
            if (roomResultInfo.getUserType() == AppConstant.UserType.USER_TYPE_ADMINISTRATOR) {
                return;
            }
            mUserSeats.put(roomResultInfo.getUserID(), roomResultInfo.getUserSeat());
            List<Integer> tempList = new ArrayList<>();
            tempList.add(roomResultInfo.getUserID());
            MeetingRoomInfo info = new MeetingRoomInfo();
            info.setUserId(roomResultInfo.getUserID());
            info.setSpeak(false);
            info.setUserSeat(roomResultInfo.getUserSeat());
            mMeetingRoomMembersList.add(info);
            mMeetingRoomAdapter.notifyDataSetChanged();
        }

        @Override
        public void onMemberExitRoom(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onMemberExitRoom-->roomResultInfo:" + roomResultInfo + ",mCurrentCustomerServiceId:" + mCurrentCustomerServiceId);
            if (roomResultInfo == null) {
                return;
            }
            for (int i = 0; i < mMeetingRoomMembersList.size(); i++) {
                if (mMeetingRoomMembersList.get(i).getUserId() == roomResultInfo.getUserID()) {
                    mMeetingRoomMembersList.remove(i);
                    break;
                }
            }
            mMeetingRoomAdapter.notifyDataSetChanged();
            if (roomResultInfo.getUserID() != 0 && myUserId == roomResultInfo.getUserID()) {
                //被后台移出房间
                kickOff();
            }
        }

        @Override
        public void onMemberChangeAvType(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onMemberChangeAvType->roomResultInfo:" + roomResultInfo);
            Logger.logD(Logger.ROOM, TAG + "->onMemberChangeAvType->userId:" + roomResultInfo.getUserID() + ", avType:" + roomResultInfo.getAvType() + ", mCurrentAvType:" + mCurrentAvType);
        }

        @Override
        public void onSpecialMemberChangeAVType(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onSpecialMemberChangeAVType-->roomResultInfo:" + roomResultInfo);
            if (roomResultInfo == null) {
                return;
            }
        }

        @Override
        public void onShutDown(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, "onShutDown-->roomResultInfo:" + roomResultInfo);
            mFlagInRoom = false;
            mToastTlv.showToastView(getString(R.string.room_state_net_disconnected), getString(R.string.room_state_btn_retry));
        }

        @Override
        public void onMemberSpeaking(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onMemberSpeaking()->roomResultInfo：" + roomResultInfo);
            if (System.currentTimeMillis() - lastMemberSpeakingTime < FAST_CLICK_DELAY_SPEAKING_TIME) {
                return;
            }
            lastMemberSpeakingTime = System.currentTimeMillis();
            for (int i = 0; i < mMeetingRoomMembersList.size(); i++) {
                byte seatIndex = mUserSeats.get(mMeetingRoomMembersList.get(i).getUserId()) != null ? (byte) mUserSeats.get(mMeetingRoomMembersList.get(i).getUserId()) : 0;
                if (seatIndex >= 1) {
                    int seat = 1 << (seatIndex - 1);
                    boolean isShow = (roomResultInfo.getSeatSate() & seat) == seat;
                    int tempSeat = mMeetingRoomMembersList.get(i).getUserSeat() & roomResultInfo.getSeatSate();
                    Logger.logD(Logger.ROOM, TAG + "->onMemberSpeaking()->roomResultInfosssssdsfsssssssss：" + mMeetingRoomMembersList.get(i).getUserSeat() + ", roomResultInfo.getSeatSate():" + roomResultInfo.getSeatSate() + ",isShow:" + isShow + ",tempSeat:" + tempSeat + ",seatIndex:" + seatIndex);

                    if (isShow) {
                        mMeetingRoomMembersList.get(i).setSpeak(true);
                        mMeetingRoomAdapter.notifyDataSetChanged();
                        final int finalI = i;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mMeetingRoomMembersList.size() >= finalI + 1) {
                                    mMeetingRoomMembersList.get(finalI).setSpeak(false);
                                }
                            }
                        }, 500);
                    }
                }
            }
        }

        @Override
        public void onMemberSeatStateChanged(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onMemberSeatStateChanged()->userId:" + roomResultInfo.getUserID() + ", userSeat:" + roomResultInfo.getUserSeat());
            mUserSeats.put(roomResultInfo.getUserID(), roomResultInfo.getUserSeat());
            for (int i = 0; i < mMeetingRoomMembersList.size(); i++) {
                if (mMeetingRoomMembersList.get(i).getUserId() == roomResultInfo.getUserID()) {
                    mMeetingRoomMembersList.get(i).setUserSeat(roomResultInfo.getUserSeat());
                    if (roomResultInfo.getUserSeat() == 0) {
                        mMeetingRoomMembersList.get(i).setSpeak(false);
                        mMeetingRoomAdapter.notifyDataSetChanged();
                    }
                }
            }

            if (roomResultInfo.getUserID() == myUserId) {
                mIsMicOpen = roomResultInfo.getUserSeat() > 0;
                setMicState();
            }
        }

        @Override
        public void onRoomOperation(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onRoomOperation()->roomResultInfo：" + roomResultInfo);
            if (roomResultInfo == null) {
                return;
            }
        }

        @Override
        public void onRoomSeekHelpEnd(VideoRoomResultInfo roomResultInfo) {
            Logger.logD(Logger.ROOM, TAG + "->onRoomSeekHelpEnd()->roomResultInfo：" + roomResultInfo);
        }
    }

    /**
     * 自己进入会议室添加自己及其他成员
     *
     * @param videoRoomMemberList
     */
    private void addVideos(List<VideoRoomMember> videoRoomMemberList) {
        mMeetingRoomMembersList.clear();
        MeetingRoomInfo info = new MeetingRoomInfo();
        info.setUserId(myUserId);
        info.setSpeak(false);
        mMeetingRoomMembersList.add(info);
        for (int i = 0; i < videoRoomMemberList.size(); i++) {
            MeetingRoomInfo inf = new MeetingRoomInfo();
            inf.setUserId(videoRoomMemberList.get(i).getUserId());
            inf.setSpeak(false);
            inf.setUserSeat(videoRoomMemberList.get(i).getSeat());
            mMeetingRoomMembersList.add(inf);
            mUserSeats.put(videoRoomMemberList.get(i).getUserId(), videoRoomMemberList.get(i).getSeat());
        }
        mMeetingRoomAdapter.notifyDataSetChanged();
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
            if (videoRoomMember.getUserId() == myUserId) {
                mIsMicOpen = videoRoomMember.getSeat() > 0;
                setMicState();
            }
            for (int i = 0; i < mMeetingRoomMembersList.size(); i++) {
                if (videoRoomMember.getUserId() == mMeetingRoomMembersList.get(i).getUserId()) {
                    mMeetingRoomMembersList.get(i).setUserSeat(videoRoomMember.getSeat());
                }
            }
        }
    }

    /**
     * 设置mic状态
     */
    private void setMicState() {
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
        mLoadingView.setVisibility(View.VISIBLE);
        mConsultRoomManager.changeSeatState(seat);
    }

    /**
     * 退出聊天室
     */
    private void exitVideoRoom() {
        if (!mFlagInRoom) {
            finish();
            return;
        }
        Logger.logD(Logger.ROOM, "exitVideoRoom（）->请求退出视频房间");
        if (!mIsIdle) {
            return;
        }
        mIsIdle = false;
        showProgressDialog();
        mFlagExitRoom = true;
        Logger.logD(Logger.ROOM, "exitVideoRoom（）->mSelfStateChangeListener:" + mSelfStateChangeListener);
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
        Logger.logD(Logger.ROOM, "是否退出界面：" + mFlagCloseUI);
        mAudioAdapterManager.setMode(AudioModule.NAME_RESET);
        if (mFlagCloseUI) {
            mIsDestroyed = true;
            finish();
        }
        mFlagExitRoom = false;
        Logger.logD(Logger.ROOM, "exitRoomDeal（）->mFlagExitRoom:" + mFlagExitRoom);
        mFlagCloseUI = true;
    }

    /**
     * 被移出房间
     */
    private void kickOff() {
        mFlagKickOff = true;
        mConsultRoomManager.exitVideoRoom(mRoomId, mSelfStateChangeListener);
        mAudioAdapterManager.setMode(AudioModule.NAME_RESET);
        Intent intent = new Intent(getActivity(), KickOffActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MEETING_ROOM, true);
        startActivity(intent);
        finish();
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
     * 显示权限对话框
     */
    private void showPermissionDialog() {
        if (isFinishing()) {
            return;
        }
        new CommonDialog.Builder(getActivity())
                .setTitle(R.string.video_dialog_permission_microphone_title)
                .setMessage(R.string.video_dialog_permission_microphone)
                .setPositiveButton(R.string.video_dialog_going_setting, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                        startActivityForResult(Utils.getAppDetailSettingIntent(), 0);
                        finish();
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
        new CommonDialog.Builder(this)
                .setMessage(getString(R.string.video_room_state_create_room_failed))
                .setPositiveButton(R.string.video_dialog_close, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {
                        finish();
                    }
                })
                .setNegativeButton(R.string.video_dialog_retry, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        joinRoom(mRoomId);
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

}