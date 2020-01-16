package cn.longmaster.hospital.doctor.ui.im;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lmmedia.MaxAmplitudeChangeListener;
import com.lmmedia.MsgAudioRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.CommonUtils;
import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.img.BitmapUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CallServiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.entity.im.PictureFileNameInfo;
import cn.longmaster.hospital.doctor.core.entity.im.PictureGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.RejectGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.StateChangeGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VideoDateGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VoiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.common.LocalNotificationManager;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.room.AudioAdapterManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.UploadMaterialMakeSureRequest;
import cn.longmaster.hospital.doctor.core.requests.im.AddMedicalRecordRequester;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatMemberListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultAddActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;
import cn.longmaster.hospital.doctor.ui.consult.video.ConsultRoomActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorListActivity;
import cn.longmaster.hospital.doctor.ui.rounds.IssueDoctorOrderActivity;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.hospital.doctor.util.FileProvider4Camera;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.HeightChangeLinearLayout;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.phoneplus.audioadapter.model.AudioConfig;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * 聊天界面
 * Created by YY on 17/8/14.
 */

public class ChatActivity extends NewBaseActivity implements ChatListAdapter.OnPictureGroupMessageClickListener, GroupMessageManager.GroupMessageStateChangeListener, GroupMessageManager.GroupStateChangeListener {
    private final String TAG = ChatActivity.class.getSimpleName();

    private final int REQUEST_CODE_PHOTO = 100;//页面请求码:选择相片
    private final int REQUEST_CODE_CAMERA = 101; //页面请求码:选择相机
    private final int REQUEST_CODE_DOCTOR_CARD = 102; //页面请求码:医生名片
    private final int REQUEST_CODE_VIDEO_DATE = 103; //页面请求码:大医生设置视频时间

    @FindViewById(R.id.chat_height_change_layout)
    private HeightChangeLinearLayout mHeightChangeView;
    @FindViewById(R.id.chat_message_list)
    private ListView mMessageListView;
    @FindViewById(R.id.chat_message_list_refresh)
    private SwipeRefreshLayout mRefreshLl;

    @FindViewById(R.id.chat_record_container_layout)
    private FrameLayout mRecordContainerView;
    @FindViewById(R.id.chat_record_start_layout)
    private LinearLayout mRecordStartView;
    @FindViewById(R.id.chat_record_cancle_layout)
    private LinearLayout mRecordCancleView;
    @FindViewById(R.id.chat_record_voice_volume)
    private ImageView mVoiceVolumeView;

    @FindViewById(R.id.chat_input_microphone)
    private ImageButton mMicrophoneView;
    @FindViewById(R.id.chat_input_keyboard)
    private ImageButton mKeyboardView;
    @FindViewById(R.id.chat_input_container_layout)
    private LinearLayout mInputContainerView;
    @FindViewById(R.id.chat_input_content)
    private EditText mContentView;
    @FindViewById(R.id.chat_input_press_speak)
    private Button mPressSpeakView;
    @FindViewById(R.id.chat_input_more)
    private ImageButton mMoreView;
    @FindViewById(R.id.chat_bottom_function_layout)
    private LinearLayout mBottomFunctionContainerView;
    @FindViewById(R.id.chat_bottom_function_recyclerview)
    private RecyclerView mBottomFunctionView;
    @FindViewById(R.id.chat_input_send)
    private TextView mInputSendTv;
    @FindViewById(R.id.chat_shortcut_button)
    private LinearLayout mShortcutButtonLl;
    @FindViewById(R.id.chat_save_success_ll)
    private LinearLayout mChatSaveSuccessLl;
    @FindViewById(R.id.chat_save_success)
    private TextView mChatSavePictureSuccess;
    @FindViewById(R.id.chat_bottom_layout)
    private LinearLayout mBottomLayoutLl;
    @FindViewById(R.id.chat_launch_again_btn)
    private Button mLaunchAgainBtn;

    @FindViewById(R.id.chat_actionbar)
    private AppActionBar mActionBar;
    @AppApplication.Manager
    private GroupMessageManager mMessageManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private LocalNotificationManager mLocalNotificationManager;
    @AppApplication.Manager
    private AudioAdapterManager audioAdapterManager;
    private boolean mIsRecording;
    private List<BaseGroupMessageInfo> mBaseGroupMessageInfos = new ArrayList<>();
    private PopupWindow mPopupWindow;
    private ChatListAdapter mAdapter;
    private int mMyRole = AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR;
    private int mAppointmentId;
    private int mGroupId;
    private MsgAudioRecord mMsgAudioRecord;
    private String mRecordFileName = "";
    private PowerManager.WakeLock mWakeLock;
    private long mRecordStartMills = 0;
    private long mRecordMs = 0;
    private boolean mValidRecord = false;
    private boolean mHasRecordPermission = false;
    private String mPhotoFileName;
    private PopupWindow mPictureLongClickPopupwindow;
    private PatientInfo mPatientInfo;
    private DoctorBaseInfo mDoctorBaseInfo;
    private AppointmentInfo mAppointmentInfo;
    private int mRejectReasonPosition = 0;
    private int mCurrentRoomStatus = 0;
    private List<BottomFunctionInfo> mBottomFunctionInfos;
    private BottomFunctionAdapter mBottomFunctionAdapter;


    public static void startChatActivity(Context context, int appointmentId, int groupId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_APPOINTMENT_ID, appointmentId);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int appointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_APPOINTMENT_ID, 0);
        int groupId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_GROUP_ID, 0);
        Logger.logD(Logger.IM, TAG + "->onNewIntent()->appointmentId:" + appointmentId + ", groupId:" + groupId);
        if (mAppointmentId != appointmentId) {
            mMessageManager.setCurentImId(0);
            mMessageManager.registerGroupMessageStateChangeListener(this, false);
            mMessageManager.registerGroupStateChangeListener(this, false);
            init(intent);
        }
    }

    private void init(Intent intent) {
        initData(intent);
        initView();
        initSystemBar();
        initRecorder();
        initAdapter();
        regListener();
        getMemberList();
        getGroupMessages();
        getPatientInfo();
        mMessageManager.updateUnreadByGroupId(mGroupId);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initViews() {
        init(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.logD(Logger.IM, "-->onDestroy");
        mMessageManager.setCurentImId(0);
        mMessageManager.registerGroupMessageStateChangeListener(this, false);
        mMessageManager.registerGroupStateChangeListener(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_PHOTO:
                ArrayList<String> imagePaths = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                Logger.logD(Logger.APPOINTMENT, "onActivityResult()通过相册选择的图片路径：" + imagePaths);
                sendPictureGroupMessage(revisePictureFormat(imagePaths));
                break;

            case REQUEST_CODE_CAMERA:
                Logger.logD(Logger.APPOINTMENT, "onActivityResult()通过相机拍摄的图片路径：" + mPhotoFileName);
                List<String> imagePath = new ArrayList<>();
                imagePath.add(mPhotoFileName);
                sendPictureGroupMessage(revisePictureFormat(imagePath));
                break;

            case REQUEST_CODE_DOCTOR_CARD:
                int doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHARE_DOCTOR_ID, 0);
                setCardGroupMessage(doctorId);
                break;

            case REQUEST_CODE_VIDEO_DATE:
                setSetDateGroupMessage(data.getLongExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_VIDEO_DATE, 0));
                break;
        }
    }

    private List<String> revisePictureFormat(List<String> imagePaths) {
        List<String> newFileNames = new ArrayList<>();
        for (String path : imagePaths) {
            if (TextUtils.isEmpty(path)) {
                continue;
            }
            String newFileName = MD5Util.md5(System.currentTimeMillis() + "" + Math.random() * 1000) + ".jpg";
            String newFilePath = SdManager.getInstance().getIMPic() + newFileName;
            String extraName = path.substring(path.lastIndexOf(".") + 1);
            Logger.logD(Logger.APPOINTMENT, TAG + "->startUpload()->文件后缀名:" + extraName);
            //需要将png或者jpeg格式的图片保存为jpg格式,不然服务器无法生成缩略图
            if (extraName.matches("[pP][nN][gG]")
                    || extraName.matches("[jJ][pP][eE][gG]")
                    || extraName.matches("[jJ][pP][gG]")) {
                BitmapUtil.savePNG2JPEG(path, newFilePath);
                BitmapUtil.compressImageFile(newFilePath);
            }
            newFileNames.add(newFileName);
        }
        return newFileNames;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBottomFunctionContainerView.getVisibility() == View.VISIBLE) {
            mBottomFunctionContainerView.setVisibility(View.GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.chat_input_microphone,
            R.id.chat_input_keyboard,
            R.id.chat_input_more,
            R.id.chat_input_send,
            R.id.chat_admissions,
            R.id.chat_launch_again_btn})
    public void onClick(View view) {
        if (mIsRecording) {
            return;
        }

        switch (view.getId()) {
            case R.id.chat_input_microphone:
                CommonUtils.hideSoftInput(mContentView);
                mBottomFunctionContainerView.setVisibility(View.GONE);
                mMicrophoneView.setVisibility(View.GONE);
                mContentView.setVisibility(View.GONE);
                mKeyboardView.setVisibility(View.VISIBLE);
                mPressSpeakView.setVisibility(View.VISIBLE);
                break;

            case R.id.chat_input_keyboard:
                mBottomFunctionContainerView.setVisibility(View.GONE);
                mKeyboardView.setVisibility(View.GONE);
                mPressSpeakView.setVisibility(View.GONE);
                mMicrophoneView.setVisibility(View.VISIBLE);
                mContentView.setVisibility(View.VISIBLE);
                mContentView.requestFocus();
                CommonUtils.showSoftInput(mContentView);
                break;

            case R.id.chat_input_more:
                CommonUtils.hideSoftInput(mContentView);
                mBottomFunctionContainerView.setVisibility(View.VISIBLE);
                mKeyboardView.setVisibility(View.GONE);
                mPressSpeakView.setVisibility(View.GONE);
                mMicrophoneView.setVisibility(View.VISIBLE);
                mContentView.setVisibility(View.VISIBLE);
                break;

            case R.id.chat_input_send:
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                setTextGroupMessage();
                break;

            case R.id.chat_admissions:
                Intent intent = new Intent(getThisActivity(), AdmissionsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_VIDEO_DATE);
                break;

            case R.id.chat_launch_again_btn:
                if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR) {//首诊医生
                    Intent intentLaunch = new Intent(getThisActivity(), ConsultAddActivity.class);
                    intentLaunch.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_BASE_INFO, mDoctorBaseInfo);
                    intentLaunch.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_HOSPITAL, mDoctorBaseInfo.getHospitalName());
                    intentLaunch.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_DEPARTMENT, mDoctorBaseInfo.getDepartmentName());
                    intentLaunch.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, mPatientInfo);
                    startActivity(intentLaunch);
                } else if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {//上级专家
                    showFollowDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPictureGroupMessageClicked(View v, final PictureGroupMessageInfo pictureGroupMessageInfo) {
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.big_pic_popwindow, null);
        ImageView bigImg = contentView.findViewById(R.id.pop_window_big_pic);
        ImageView keepPic = contentView.findViewById(R.id.pop_window_keep_pic);
        final RelativeLayout loadWait = contentView.findViewById(R.id.pop_window_load_wait);
        final LinearLayout saveSuccess = contentView.findViewById(R.id.pop_window_keep_success);
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        loadWait.setVisibility(View.VISIBLE);
        String url = AppConfig.getImPictureDownloadUrl() + pictureGroupMessageInfo.getPictureName();
        GlideUtils.showChatBigImage(bigImg, getThisActivity(), url);
        loadWait.setVisibility(View.GONE);
        loadWait.setOnClickListener(v1 -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        bigImg.setOnClickListener(v12 -> {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        });
        keepPic.setOnClickListener(v13 -> {
            new Thread(new MyRunnable(AppConfig.getImPictureDownloadUrl() + pictureGroupMessageInfo.getPictureName())).start();
            saveSuccess.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> saveSuccess.setVisibility(View.GONE), 3000);
        });
    }

    @Override
    public void onSendGroupMsgStateChanged(int result, BaseGroupMessageInfo baseGroupMessageInfo) {

    }

    @Override
    public void onReceiveNewGroupMessageStateChanged(final BaseGroupMessageInfo baseGroupMessageInfo) {
        Logger.logD(Logger.IM, TAG + "->onReceiveNewGroupMessageStateChanged()->baseGroupMessageInfo:" + baseGroupMessageInfo);
        if (baseGroupMessageInfo.getGroupId() != mGroupId) {
            return;
        }
        AppHandlerProxy.runOnUIThread(() -> {
            if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_EXIT_MSG) {
                new CommonDialog.Builder(getThisActivity())
                        .setTitle(R.string.chat_make_sure_leave)
                        .setMessage(R.string.chat_bekick)
                        .setPositiveButton(R.string.video_dialog_ok, () -> finish())
                        .setCanceledOnTouchOutside(false)
                        .setCancelable(false)
                        .show();
            } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.STATE_CHANGE_MSG) {
                StateChangeGroupMessageInfo messageInfo = (StateChangeGroupMessageInfo) baseGroupMessageInfo;
                if (messageInfo.getState() == 3) {
                    new CommonDialog.Builder(getThisActivity())
                            .setTitle(R.string.chat_make_sure_leave)
                            .setMessage(R.string.chat_appointment_force_close)
                            .setPositiveButton(R.string.video_dialog_ok, () -> finish())
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(false)
                            .show();
                }
            } else {
                if (mPictureLongClickPopupwindow != null && mPictureLongClickPopupwindow.isShowing()) {
                    mPictureLongClickPopupwindow.dismiss();
                }
                addNewGroupMessageToList(baseGroupMessageInfo);
            }
        });
    }

    @Override
    public void onGetGroupStatusStateChanged(final int result, final int groupId, final int status) {
        Logger.logD(Logger.IM, TAG + "->onGetGroupStatusStateChanged()->result:" + result + ", groupId:" + groupId + ", status:" + status);
        if (groupId != mGroupId && result != 0) {
            return;
        }
        mCurrentRoomStatus = status;
        AppHandlerProxy.runOnUIThread(() -> initGroupState(status));
    }

    @Override
    public void onUpdGroupStatusStateChanged(int result, int groupId, final int status) {
        if (result != 0 && groupId != mGroupId) {
            return;
        }
        AppHandlerProxy.runOnUIThread(() -> initGroupState(status));
    }

    @Override
    public void onGetGroupListStateChanged(int result, int opType, int count, List<Integer> groupList) {
    }

    private void initData(Intent intent) {
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_APPOINTMENT_ID, 0);
        mGroupId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CHAT_GROUP_ID, 0);
        Logger.logD(Logger.IM, TAG + "->initData()->mAppointmentId:" + mAppointmentId + ", mGroupId:" + mGroupId);

        mBaseGroupMessageInfos.clear();
        mMessageManager.setCurentImId(mGroupId);
        mLocalNotificationManager.cancleNotification(LocalNotificationManager.NOTIFICATION_TYPE_NEW_GROUP_MESSAGE);
        getAppointmentInfo(mAppointmentId);
    }

    private void initView() {
        initBottomFunctionView();
    }

    /**
     * 初始化录音相关
     */
    private void initRecorder() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "DPA");

        AudioConfig audioConfig = audioAdapterManager.getAudioAdapter().getAudioConfig();
        mMsgAudioRecord = new MsgAudioRecord(audioConfig.getRecordSourceType());
        mMsgAudioRecord.setListener(new MaxAmplitudeChangeListener() {
            @Override
            public void onPrepare() {
            }

            @Override
            public void onMaxAmplitudeChanged(final int maxAmplitude) {
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsRecording) {
                            mMsgAudioRecord.stop();
                            return;
                        }

                        if (!mMsgAudioRecord.isRecording()) {
                            return;
                        }

                        if (maxAmplitude >= 0 && maxAmplitude <= 10) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_1);
                        } else if (maxAmplitude > 10 && maxAmplitude <= 20) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_2);
                        } else if (maxAmplitude > 20 && maxAmplitude <= 30) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_3);
                        } else if (maxAmplitude > 30 && maxAmplitude <= 40) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_4);
                        } else if (maxAmplitude > 40 && maxAmplitude <= 50) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_5);
                        } else if (maxAmplitude > 50 && maxAmplitude <= 60) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_6);
                        } else if (maxAmplitude > 60 && maxAmplitude <= 70) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_7);
                        } else if (maxAmplitude > 70) {
                            mVoiceVolumeView.setImageResource(R.drawable.ic_chat_record_8);
                        }
                    }
                });
            }
        }, 80);
    }

    private void initAdapter() {
        mAdapter = new ChatListAdapter(this);
        mAdapter.setData(mBaseGroupMessageInfos);
        mAdapter.setOnPictureGroupMessageClickListener(this);
        mMessageListView.setAdapter(mAdapter);
        View view = new View(this);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(5));
        view.setLayoutParams(layoutParams);
        mMessageListView.addFooterView(view);
        mRefreshLl.setOnRefreshListener(() -> getGroupMessages());
    }

    private void regListener() {
        mMessageManager.registerGroupMessageStateChangeListener(this, true);
        mMessageManager.registerGroupStateChangeListener(this, true);
        mHeightChangeView.setOnHeightChangeListener((newHeight, oldHeight) -> {
            if (newHeight < oldHeight) {
                AppApplication.HANDLER.post(() -> mBottomFunctionContainerView.setVisibility(View.GONE));
            }
        });
        mPressSpeakView.setOnTouchListener((v, event) -> {
            setPressSpeakOnTouchListener(v, event);
            return false;
        });
        mActionBar.setRightBtnOnClickListener(v -> medicalAdvice());
        mContentView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                setTextGroupMessage();
            }
            return false;
        });
        mContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mContentView.length() == 0) {
                    mInputSendTv.setVisibility(View.GONE);
                    mMoreView.setVisibility(View.VISIBLE);
                } else {
                    mInputSendTv.setVisibility(View.VISIBLE);
                    mMoreView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mAdapter.setOnPictureGroupMessageLongClickListener((v, pictureGroupMessageInfo, type) -> {
            showPictureLongPressedDialog(v, pictureGroupMessageInfo, type);
        });
        mMessageListView.setOnTouchListener((v, event) -> {
            mBottomFunctionContainerView.setVisibility(View.GONE);
            return false;
        });
        mAdapter.setOnSendFailGroupMessageClickListener((baseGroupMessageInfo, position, progressBar, view) -> {
            progressBar.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            resendGroupMessage(baseGroupMessageInfo, position, progressBar, view);
        });
        mAdapter.setOnAdmissionsGroupMessageClickListener(view -> {
            Intent intent = new Intent(getThisActivity(), AdmissionsActivity.class);
            startActivityForResult(intent, REQUEST_CODE_VIDEO_DATE);
        });
        mAdapter.setOnRefuseAdmissionsGroupMessageClickListener(view -> showRejectDialog());
        mAdapter.setOnIssueAdviseGroupMessageClickListener(view -> issueAdvise());

        mAdapter.seOnMedicalAdviceGroupMessageClickListener(view -> medicalAdvice());
    }

    private void medicalAdvice() {
        Intent intent = new Intent(getThisActivity(), PatientInformationActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointmentId);
        startActivity(intent);
    }

    private void setPressSpeakOnTouchListener(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startRecord();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            stopRecord();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] location = new int[2];
            mInputContainerView.getLocationOnScreen(location);
            int minX = 0;
            int minY = location[1];
            int maxX = ScreenUtil.getScreenWidth();
            int maxY = ScreenUtil.getScreenHeight();
            float rawX = event.getRawX();
            float rawY = event.getRawY();
            if (rawX >= minX && rawX <= maxX && rawY >= minY && rawY <= maxY) {
                mRecordCancleView.setVisibility(View.GONE);
                mRecordStartView.setVisibility(View.VISIBLE);
                mValidRecord = true;
            } else {
                mRecordStartView.setVisibility(View.GONE);
                mRecordCancleView.setVisibility(View.VISIBLE);
                mValidRecord = false;
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            mRecordContainerView.setVisibility(View.GONE);
        }
    }

    private void showPictureLongPressedDialog(ImageView v, final PictureGroupMessageInfo pictureGroupMessageInfo, boolean type) {
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_picture_long_press_pop, null);
        TextView addPressRecord = (TextView) contentView.findViewById(R.id.picture_long_press_add_medical_record);
        TextView saveToLocal = (TextView) contentView.findViewById(R.id.picture_long_press_save_to_local);
        mPictureLongClickPopupwindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPictureLongClickPopupwindow.setBackgroundDrawable(new ColorDrawable());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = contentView.getMeasuredHeight();
        int popupWidth = contentView.getMeasuredWidth();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        mPictureLongClickPopupwindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);

        addPressRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPictureLongClickPopupwindow.dismiss();
                savePictureToRecord(pictureGroupMessageInfo);
            }
        });
        saveToLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePictureToGallery(pictureGroupMessageInfo);
            }
        });
    }

    private void resendGroupMessage(BaseGroupMessageInfo baseGroupMessageInfo, int position, final ProgressBar progressBar, final View view) {
        Logger.logD(Logger.IM, TAG + "->onSendFailGroupMessageClicked()->baseGroupMessageInfo:" + baseGroupMessageInfo + ", position:" + position);
        mMessageManager.resendGroupMessage(baseGroupMessageInfo, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                Logger.logD(Logger.IM, TAG + "->onSendFailGroupMessageClicked->onSaveDBStateChanged()->baseGroupMessageInfo:" + baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, final int code, String fileName) {
                Logger.logD(Logger.IM, TAG + "->resendGroupMessage->onFileUploadStateChanged()->seqId:" + seqId + " ,code" + code + ",fileName" + fileName);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code != 0) {
                            progressBar.setVisibility(View.GONE);
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                Logger.logD(Logger.IM, TAG + "->onSendFailGroupMessageClicked->onSendStateChanged()->result:" + result + " ,seqId" + seqId);
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (result == 0) {
                            view.setVisibility(View.GONE);
                        } else {
                            view.setVisibility(View.VISIBLE);
                        }
                        displaySendStateChanged(result, seqId);
                    }
                });
            }
        });
    }

    private void savePictureToRecord(final PictureGroupMessageInfo pictureGroupMessageInfo) {
        AddMedicalRecordRequester addMedicalRecordRequester = new AddMedicalRecordRequester(new DefaultResultCallback<PictureFileNameInfo>() {
            @Override
            public void onSuccess(PictureFileNameInfo pictureFileNameInfo, BaseResult baseResult) {
                Logger.logD(Logger.IM, TAG + "->savePictureToRecord->baseResult:" + baseResult + ",fileName" + pictureFileNameInfo);
                if (baseResult.getCode() == 0) {
                    onUploadFileMaterialRequest(pictureFileNameInfo);
                }
            }
        });
        addMedicalRecordRequester.fileName = pictureGroupMessageInfo.getPictureName();
        addMedicalRecordRequester.appointmentId = mAppointmentId;
        addMedicalRecordRequester.doPost();
    }

    private void onUploadFileMaterialRequest(PictureFileNameInfo pictureFileNameInfo) {
        UploadMaterialMakeSureRequest request = new UploadMaterialMakeSureRequest(new OnResultListener<Integer>() {
            @Override
            public void onResult(BaseResult baseResult, Integer integer) {
                Logger.logD(Logger.IM, TAG + "->onUploadFileMaterialRequest->baseResult:" + baseResult);
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mChatSaveSuccessLl.setVisibility(View.VISIBLE);
                    mChatSavePictureSuccess.setText(getString(R.string.chat_save_picture_records));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatSaveSuccessLl.setVisibility(View.GONE);
                        }
                    }, 3000);
                }
            }
        });
        request.appointmentId = mAppointmentId;
        request.materialPic = pictureFileNameInfo.getNew_file();
        request.userId = mUserInfoManager.getCurrentUserInfo().getUserId();
        request.uploadType = 0;
        request.checkState = 1;
        request.doPost();
    }

    private void savePictureToGallery(final PictureGroupMessageInfo pictureGroupMessageInfo) {
        mPictureLongClickPopupwindow.dismiss();
        mChatSaveSuccessLl.setVisibility(View.VISIBLE);
        mChatSavePictureSuccess.setText(getString(R.string.chat_keep_pic));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mChatSaveSuccessLl.setVisibility(View.GONE);
            }
        }, 3000);
        new Thread(new MyRunnable(AppConfig.getImPictureDownloadUrl() + pictureGroupMessageInfo.getPictureName())).start();
        mMessageListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                CommonUtils.hideSoftInput(mContentView);
                mBottomFunctionContainerView.setVisibility(View.GONE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void getMemberList() {
        GetChatMemberListRequester requester = new GetChatMemberListRequester(new DefaultResultCallback<List<MemberListInfo>>() {
            @Override
            public void onSuccess(List<MemberListInfo> memberListInfos, BaseResult baseResult) {
                int userId = mUserInfoManager.getCurrentUserInfo().getUserId();
                for (int i = 0; i < memberListInfos.size(); i++) {
                    if (memberListInfos.get(i).getUserId() == userId) {
                        mMyRole = memberListInfos.get(i).getRole();
                    }
                    if (memberListInfos.get(i).getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
                        getDoctorInfo(memberListInfos.get(i).getUserId());
                    }
                }
                mMessageManager.getGroupStatus(mAppointmentId);
                initBottomFunctionView();
                initBottomLaunchBtn();
                mAdapter.setMyRole(mMyRole);
            }
        });
        requester.appointmentId = mAppointmentId;
        requester.doPost();
    }

    public void getGroupMessages() {
        mMessageManager.getGroupMessages(mAppointmentId, mBaseGroupMessageInfos.size(), new GroupMessageManager.GetLocalGroupMessagesCallback() {
            @Override
            public void onGetLocalGroupMessagesStateChanged(final List<BaseGroupMessageInfo> messageInfos) {
                mRefreshLl.setRefreshing(false);
                Logger.logD(Logger.IM, TAG + "->getGroupMessages()->onGetLocalGroupMessagesStateChanged()->messageInfos:" + messageInfos);
                if (messageInfos.size() == 0) {
                    return;
                }
                mBaseGroupMessageInfos.addAll(0, messageInfos);
                mAdapter.setRoomStatus(mCurrentRoomStatus);
                mAdapter.addToFirst(messageInfos);
                mMessageListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mMessageListView.setSelection(messageInfos.size() - 1);
                    }
                });
            }
        });
    }

    private void getPatientInfo() {
        AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(mAppointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    mPatientInfo = patientInfo;
                    mActionBar.setTitle(getString(R.string.chat_title, patientInfo.getPatientBaseInfo().getRealName(), mAppointmentId + ""));
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
     * 拉取医生信息
     */
    private void getDoctorInfo(int id) {
        mDoctorManager.getDoctor(id, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDoctorBaseInfo = doctorBaseInfo;
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
     * 设置就诊关闭时底部按钮的动作
     */
    private void initBottomLaunchBtn() {
        if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR) {
            mLaunchAgainBtn.setText(R.string.chat_launch_again);
        } else if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
            mLaunchAgainBtn.setText(R.string.chat_launch_followed);
        } else {
            mLaunchAgainBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 根据不同的状态设置UI
     *
     * @param status
     */
    private void initGroupState(int status) {
        if (status == AppConstant.IMGroupStatus.IM_GROUP_STATUS_ORIGINAL && mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
            mShortcutButtonLl.setVisibility(View.VISIBLE);
        } else {
            mShortcutButtonLl.setVisibility(View.GONE);
            for (int i = 0; i < mBottomFunctionInfos.size(); i++) {
                if (mBottomFunctionInfos.get(i).getFunctionType() == BottomFunctionInfo.REJECT) {
                    mBottomFunctionInfos.remove(i);
                    mBottomFunctionAdapter.notifyDataSetChanged();
                    break;
                }
            }
            for (int i = 0; i < mBaseGroupMessageInfos.size(); i++) {
                if (mBaseGroupMessageInfos.get(i).getMsgType() == BaseGroupMessageInfo.WAITING_ADMISSION_MSG) {
                    mAdapter.setRoomStatus(status);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        //只有首诊医生和上级专家才会显示再次预约或发起随诊
        if (status == AppConstant.IMGroupStatus.IM_GROUP_STATUS_CLOSE) {
            if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR || mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
                mLaunchAgainBtn.setVisibility(View.VISIBLE);
            } else {
                mLaunchAgainBtn.setVisibility(View.GONE);
            }
            mBottomLayoutLl.setVisibility(View.GONE);
        } else {
            mBottomLayoutLl.setVisibility(View.VISIBLE);
            mLaunchAgainBtn.setVisibility(View.GONE);
        }
    }

    private void initBottomFunctionView() {
        mBottomFunctionInfos = new ArrayList<>();
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.ALBUM, R.drawable.bg_chat_album, getString(R.string.chat_album)));
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.CAMERA, R.drawable.bg_chat_camera, getString(R.string.chat_camera)));
        if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
            mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.SET_SCHEDULING, R.drawable.bg_chat_set_scheduling, getString(R.string.chat_set_scheduling)));
            mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.ISSUE_ADVISE, R.drawable.bg_chat_issue_advise, getString(R.string.chat_issue_advise)));
            if (mCurrentRoomStatus == AppConstant.IMGroupStatus.IM_GROUP_STATUS_ORIGINAL) {
                mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.REJECT, R.drawable.bg_chat_reject, getString(R.string.chat_reject)));
            }
        }
        if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR) {
            mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.ADD_MATERIAL, R.drawable.bg_chat_add_material, getString(R.string.chat_add_material)));
        }
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.CALL_SERVICE, R.drawable.bg_chat_call_service, getString(R.string.chat_call_service)));
//        if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR || mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR) {
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.ENTER_ROOM, R.drawable.bg_chat_enter_room, getString(R.string.chat_enter_room)));
//        }
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.DOCTOR_CARD, R.drawable.bg_chat_doctor_card, getString(R.string.chat_doctor_card)));
        mBottomFunctionInfos.add(new BottomFunctionInfo(BottomFunctionInfo.HISTORY_MESSAGE, R.drawable.bg_chat_history_message, getString(R.string.chat_history_message)));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        mBottomFunctionView.setLayoutManager(layoutManager);
        mBottomFunctionAdapter = new BottomFunctionAdapter(this, R.layout.item_chat_bottom_function, mBottomFunctionInfos);
        mBottomFunctionView.setAdapter(mBottomFunctionAdapter);
        mBottomFunctionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                switch (mBottomFunctionInfos.get(position).getFunctionType()) {
                    case BottomFunctionInfo.ALBUM:
                        intent.setClass(getThisActivity(), PickPhotoActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_PHOTO);
                        break;

                    case BottomFunctionInfo.CAMERA:
                        mPhotoFileName = SdManager.getInstance().getTempPath() + System.currentTimeMillis() + ".jpg";
                        Disposable disposable = PermissionHelper.init(getThisActivity()).addPermissionGroups(PermissionConstants.CAMERA)
                                .requestEachCombined()
                                .subscribe(permission -> {
                                    if (permission.granted) {
                                        startCameraActivity(getThisActivity(), null, mPhotoFileName, REQUEST_CODE_CAMERA);
                                        // `permission.name` is granted !
                                    } else {
                                        new CommonDialog.Builder(getThisActivity())
                                                .setTitle("权限授予")
                                                .setMessage("在设置-应用管理-权限中开启相机权限，才能正常使用相机")
                                                .setPositiveButton("取消", () -> {
                                                })
                                                .setCancelable(false)
                                                .setNegativeButton("确定", () -> {
                                                    Utils.gotoAppDetailSetting();
                                                })
                                                .show();
                                    }
                                });
                        compositeDisposable.add(disposable);
                        break;

                    case BottomFunctionInfo.SET_SCHEDULING:
                        intent.setClass(getThisActivity(), AdmissionsActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_VIDEO_DATE);
                        break;

                    case BottomFunctionInfo.ISSUE_ADVISE:
                        issueAdvise();
                        break;

                    case BottomFunctionInfo.REJECT:
                        showRejectDialog();
                        break;

                    case BottomFunctionInfo.ADD_MATERIAL:
                        intent.setClass(getThisActivity(), ConsultDataManageActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, mAppointmentInfo);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, mPatientInfo);
                        startActivity(intent);
                        break;

                    case BottomFunctionInfo.CALL_SERVICE:
                        showCallDialog();
                        break;

                    case BottomFunctionInfo.ENTER_ROOM:
                        intent.setClass(getThisActivity(), ConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, mMyRole);
                        startActivity(intent);
                        break;

                    case BottomFunctionInfo.DOCTOR_CARD:
                        intent.setClass(getThisActivity(), DoctorListActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, true);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_BASE_INFO, mPatientInfo.getPatientBaseInfo());
                        startActivityForResult(intent, REQUEST_CODE_DOCTOR_CARD);
                        break;

                    case BottomFunctionInfo.HISTORY_MESSAGE:
                        HistoryMessageActivity.startHistoryMessageActivity(getThisActivity(), mAppointmentId, mGroupId);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showRejectDialog() {
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_reject_visit_pop, null);
        final TextView rejectCancel = contentView.findViewById(R.id.reject_pop_cancel);
        final TextView rejectSubmit = contentView.findViewById(R.id.reject_pop_submit);
        LinearLayout rejectOfRangeLl = contentView.findViewById(R.id.reject_pop_out_of_range_ll);
        LinearLayout rejectNyLl = contentView.findViewById(R.id.reject_pop_ny_ll);
        LinearLayout rejectOtherLl = contentView.findViewById(R.id.reject_pop_other_ll);
        final ImageView rejectOfRangeImg = contentView.findViewById(R.id.reject_pop_out_of_range_img);
        final ImageView rejectNyImg = contentView.findViewById(R.id.reject_pop_ny_img);
        final ImageView rejectOtherImg = contentView.findViewById(R.id.reject_pop_other_img);
        final TextView rejectOfRangeTv = contentView.findViewById(R.id.reject_pop_out_of_range_tv);
        final TextView rejectNyTv = contentView.findViewById(R.id.reject_pop_ny_tv);
        final EditText rejectEdt = contentView.findViewById(R.id.reject_pop_edt);
        final LinearLayout rejectEdtLl = contentView.findViewById(R.id.reject_pop_edt_ll);
        final PopupWindow PopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        PopupWindow.setBackgroundDrawable(new BitmapDrawable());
        PopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        PopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        rejectOfRangeLl.setOnClickListener(v -> {
            mRejectReasonPosition = 0;
            rejectEdtLl.setVisibility(View.GONE);
            rejectOfRangeImg.setImageResource(R.drawable.ic_reject_selected);
            rejectNyImg.setImageResource(R.drawable.ic_reject_not_selected);
            rejectOtherImg.setImageResource(R.drawable.ic_reject_not_selected);
        });
        rejectNyLl.setOnClickListener(v -> {
            mRejectReasonPosition = 1;
            rejectEdtLl.setVisibility(View.GONE);
            rejectOfRangeImg.setImageResource(R.drawable.ic_reject_not_selected);
            rejectNyImg.setImageResource(R.drawable.ic_reject_selected);
            rejectOtherImg.setImageResource(R.drawable.ic_reject_not_selected);
        });
        rejectOtherLl.setOnClickListener(v -> {
            mRejectReasonPosition = 2;
            rejectEdtLl.setVisibility(View.VISIBLE);
            rejectEdt.requestFocus();
            rejectOfRangeImg.setImageResource(R.drawable.ic_reject_not_selected);
            rejectNyImg.setImageResource(R.drawable.ic_reject_not_selected);
            rejectOtherImg.setImageResource(R.drawable.ic_reject_selected);
        });

        rejectCancel.setOnClickListener(v -> {
            hideInputMethod(rejectEdt);
            PopupWindow.dismiss();
        });
        rejectSubmit.setOnClickListener(v -> {
            hideInputMethod(rejectEdt);
            String reason = "";
            if (mRejectReasonPosition == 0) {
                reason = rejectOfRangeTv.getText().toString();
            }
            if (mRejectReasonPosition == 1) {
                reason = rejectNyTv.getText().toString();
            }
            if (mRejectReasonPosition == 2) {
                reason = rejectEdt.getText().toString().trim();
            }
            sendRejectGroupMessage(reason);
            PopupWindow.dismiss();
        });
    }

    private void hideInputMethod(EditText rejectEdt) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rejectEdt.getWindowToken(), 0);
    }

    private void sendRejectGroupMessage(String reason) {
        mMessageManager.sendRejectGroupMessage(mAppointmentId, mGroupId, reason, mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                mMessageManager.updateGroupStatus(mGroupId, AppConstant.IMGroupStatus.IM_GROUP_STATUS_REFUSE);
                AppHandlerProxy.runOnUIThread(() -> {
                    for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                        if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                            RejectGroupMessageInfo messagInfo = (RejectGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                            if (result == 0) {
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取预约信息
     *
     * @param appointmentId
     */
    private void getAppointmentInfo(final int appointmentId) {
        AppApplication.getInstance().getManager(ConsultManager.class).getAppointmentInfo(appointmentId, (baseResult, appointmentInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && appointmentInfo != null) {
                Logger.logD(Logger.IM, "getAppointmentInfo->appointmentInfo:" + appointmentInfo);
                mAppointmentInfo = appointmentInfo;
            }
        });
    }

    private void showCallDialog() {
        new CommonDialog.Builder(this)
                .setTitle(R.string.chat_definite_call_title)
                .setMessage(R.string.chat_definite_call_context)
                .setPositiveButton(R.string.video_dialog_cancel, () -> {
                })
                .setNegativeButton(R.string.video_dialog_ok, () -> sendCallServerGroupMessage())
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    private void showFollowDialog() {
        new CommonDialog.Builder(this)
                .setTitle(R.string.chat_sure_to_launch_followed)
                .setMessage(R.string.chat_sure_to_launch_followed_tips)
                .setPositiveButton(R.string.cancel, () -> {
                })
                .setNegativeButton(R.string.confirm, () -> mMessageManager.updateGroupStatus(mGroupId, AppConstant.IMGroupStatus.IM_GROUP_STATUS_FOLLOW_UP))
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .show();
    }

    private void startCameraActivity(FragmentActivity activity, Fragment fragment, String fileName, int requestCode) {
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.addCategory(Intent.CATEGORY_DEFAULT);
        //mUploadPhotoName = SdManager.getInstance().getTempPath() + TimeUtils.getNowMills() + ".jpg";
        File file = new File(fileName);
        if (file.exists()) {
            FileUtil.deleteFile(fileName);
        }
        Uri uri = getUri(activity, file);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (fragment != null) {
            fragment.startActivityForResult(intentCamera, requestCode);
        } else {
            activity.startActivityForResult(intentCamera, requestCode);
        }
    }

    private Uri getUri(FragmentActivity activity, File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider4Camera.getUriForFile(activity, activity.getPackageName() + ".FileProvider4Camera", path);
        } else {
            return Uri.fromFile(path);
        }
    }

    private void startRecord() {
        if (isFastClick() || mIsRecording) {
            ToastUtils.showShort(R.string.chat_record_too_short);
            return;
        }
        Logger.logD(Logger.IM, TAG + "->startRecord()");
        mIsRecording = true;
        mValidRecord = false;
        if (!mHasRecordPermission) {
            mHasRecordPermission = isHasPermission();
        }

        if (mHasRecordPermission) {
            Logger.logD(Logger.IM, TAG + "->startRecord()->有录音权限！");
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ToastUtils.showShort(R.string.sdcard_unable);
                mIsRecording = false;
                return;
            }
            mRecordFileName = MD5Util.md5(System.currentTimeMillis() + "") + ".amr";
            int result = 10;
            try {
                result = mMsgAudioRecord.start(SdManager.getInstance().getIMVoice(), mRecordFileName);
                if (result == MsgAudioRecord.RECORD_SUCCESS
                        && mMsgAudioRecord.aRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    onRecordStart();
                } else {
                    mIsRecording = false;
                    ToastUtils.showShort(R.string.chat_record_failed);
                }
            } catch (Exception e) {
                mIsRecording = false;
                e.printStackTrace();
            }
        } else {
            Logger.logD(Logger.IM, TAG + "->startRecord()->没有录音权限！");
            mIsRecording = false;
            showPermissionDialog();
        }
    }

    private void onRecordStart() {
        mValidRecord = true;
        mRecordContainerView.setVisibility(View.VISIBLE);
        mRecordCancleView.setVisibility(View.GONE);
        mRecordStartView.setVisibility(View.VISIBLE);
        mWakeLock.acquire();
        mRecordStartMills = SystemClock.elapsedRealtime();
    }

    private void stopRecord() {
        mIsRecording = false;
        mRecordContainerView.setVisibility(View.GONE);
        if (mMsgAudioRecord.isRecording()) {
            mMsgAudioRecord.stop();
        }
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mRecordMs = SystemClock.elapsedRealtime() - mRecordStartMills;
        if (mValidRecord) {
            sendVoiceGroupMessage();
        }
        mValidRecord = false;
    }

    public boolean isHasPermission() {
        int audioSource = MediaRecorder.AudioSource.MIC;
        int sampleRateInHz = 44100;
        int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        try {
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            mIsRecording = false;
            e.printStackTrace();
        }
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            return false;
        }
        audioRecord.stop();
        audioRecord.release();
        return true;
    }

    /**
     * 显示权限对话框
     */
    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_tip)
                .setMessage(R.string.apply_appointment_permission_setting_tip)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                    }
                })
                .create()
                .show();
    }


    private void setCardGroupMessage(int doctorId) {
        mMessageManager.sendCardGroupMessage(mAppointmentId, mGroupId, doctorId, mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                Logger.logD(Logger.IM, TAG + "->setCardGroupMessage()->onSaveDBStateChanged()->code:" + code + ", BaseGroupMessageInfo:" + baseGroupMessageInfo);
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                Logger.logD(Logger.IM, TAG + "->setCardGroupMessage()->onSendStateChanged()->result:" + result + ", seqId:" + seqId);
                displaySendStateChanged(result, seqId);
            }
        });
    }

    private void setTextGroupMessage() {
        String content = mContentView.getText().toString().trim();
        if (StringUtils.isEmpty(content)) {
            ToastUtils.showShort(getString(R.string.chat_please_enter_content));
            return;
        }
        mMessageManager.sendTextGroupMessage(mAppointmentId, mGroupId, content, mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                Logger.logD(Logger.IM, TAG + "->setTextGroupMessage()->onSendStateChanged()->result:" + result + ", seqId:" + seqId);
                displaySendStateChanged(result, seqId);
            }
        });
        mContentView.setText("");
    }

    private void displaySendStateChanged(final int result, final long seqId) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                    if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                        BaseGroupMessageInfo baseGroupMessageInfo = mBaseGroupMessageInfos.get(i);
                        if (result == 0) {
                            baseGroupMessageInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                        } else {
                            baseGroupMessageInfo.setMsgState(BaseGroupMessageInfo.STATE_FAILED);
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
    }

    private void sendPictureGroupMessage(List<String> fileNames) {
        Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->fileNames:" + fileNames);
        for (int i = 0; i < fileNames.size(); i++) {
            mMessageManager.sendPictureGroupMessage(mAppointmentId, mGroupId, fileNames.get(i), mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
                @Override
                public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                    addNewGroupMessageToList(baseGroupMessageInfo);
                }

                @Override
                public void onFileUploadStateChanged(final long seqId, final int code, final String fileName) {
                    Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage()->onFileUploadStateChanged()->seqId:" + seqId + ", code:" + code + ", fileName" + fileName);
                    AppHandlerProxy.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                                if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                                    PictureGroupMessageInfo messagInfo = (PictureGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                                    if (code == 0) {
                                        messagInfo.setPictureName(fileName);
                                        messagInfo.setMsgState(BaseGroupMessageInfo.STATE_UPLOAD_SUCCESS);
                                    } else {
                                        messagInfo.setMsgState(BaseGroupMessageInfo.STATE_FAILED);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    });
                }

                @Override
                public void onSendStateChanged(final int result, final long seqId) {
                    Logger.logD(Logger.IM, TAG + "->sendPictureGroupMessage->onSaveDBStateChanged()：result:" + result + ", seqId:" + seqId);
                    AppHandlerProxy.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                                if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                                    PictureGroupMessageInfo messagInfo = (PictureGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                                    if (result == 0) {
                                        messagInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void sendVoiceGroupMessage() {
        Logger.logD(Logger.IM, TAG + "->stopRecord()->mRecordMs:" + mRecordMs);
        if (mRecordMs < 1000) {
            ToastUtils.showShort(R.string.chat_voice_too_short);
            return;
        }

        File file = new File(SdManager.getInstance().getIMVoice(), mRecordFileName);
        if (!file.exists()) {
            ToastUtils.showShort(R.string.chat_record_failed);
            return;
        }

        mMessageManager.sendVoiceGroupMessage(mAppointmentId, mGroupId, mRecordFileName, (int) (mRecordMs / 1000), mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(final long seqId, final int code, final String fileName) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onFileUploadStateChanged()->seqId:" + seqId + ", code:" + code + ", fileName" + fileName);
                AppHandlerProxy.runOnUIThread(() -> {
                    for (int i = 0; i < mBaseGroupMessageInfos.size(); i++) {
                        if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                            VoiceGroupMessageInfo messagInfo = (VoiceGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                            if (code == 0) {
                                messagInfo.setFileName(fileName);
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_UPLOAD_SUCCESS);
                            } else {
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_FAILED);
                            }
                            Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->messagInfo:" + messagInfo);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                Logger.logD(Logger.IM, TAG + "->sendVoiceGroupMessage()->onSendStateChanged()：result:" + result + ", seqId:" + seqId);
                AppHandlerProxy.runOnUIThread(() -> {
                    for (int i = 0; i < mBaseGroupMessageInfos.size(); i++) {
                        if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                            VoiceGroupMessageInfo messagInfo = (VoiceGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                            if (result == 0) {
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
            }
        });
    }

    private void sendCallServerGroupMessage() {
        mMessageManager.sendCallServiceGroupMessage(mAppointmentId, mGroupId, 10000, mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {

            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                AppHandlerProxy.runOnUIThread(() -> {
                    for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                        if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                            CallServiceGroupMessageInfo messagInfo = (CallServiceGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                            if (result == 0) {
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
            }
        });
    }

    private void addNewGroupMessageToList(BaseGroupMessageInfo baseGroupMessageInfo) {
        Logger.logD(Logger.IM, TAG + "->addNewGroupMessageToList()->：baseGroupMessageInfo:" + baseGroupMessageInfo);
        if (baseGroupMessageInfo.getGroupId() != mGroupId) {
            return;
        }
        mBaseGroupMessageInfos.add(baseGroupMessageInfo);
        mAdapter.addToLast(baseGroupMessageInfo);
    }

    public void setSetDateGroupMessage(long videoTime) {
        mMessageManager.sendSetDateGroupMessage(mAppointmentId, mGroupId, videoTime, mMyRole, new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                addNewGroupMessageToList(baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                mMessageManager.updateGroupStatus(mGroupId, AppConstant.IMGroupStatus.IM_GROUP_STATUS_RECEPTION);
                AppHandlerProxy.runOnUIThread(() -> {
                    for (int i = mBaseGroupMessageInfos.size() - 1; i >= 0; i--) {
                        if (mBaseGroupMessageInfos.get(i).getSeqId() == seqId) {
                            VideoDateGroupMessageInfo messagInfo = (VideoDateGroupMessageInfo) mBaseGroupMessageInfos.get(i);
                            if (result == 0) {
                                messagInfo.setMsgState(BaseGroupMessageInfo.STATE_SUCCESS);
                            }
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                });
            }
        });
    }

    public void issueAdvise() {
        Intent intent = new Intent(getThisActivity(), IssueDoctorOrderActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, mAppointmentInfo);
        startActivity(intent);
    }

    public class MyRunnable implements Runnable {
        private String mUrl;

        public MyRunnable(String url) {
            this.mUrl = url;
        }

        @Override
        public void run() {
            Bitmap bitmap = null;
            try {
                URL url = new URL(mUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                BitmapUtils.savePictureToLocal(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
