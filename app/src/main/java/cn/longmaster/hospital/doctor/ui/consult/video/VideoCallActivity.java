package cn.longmaster.hospital.doctor.ui.consult.video;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsConsultRoomActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * 呼叫界面
 *
 * @author Tengshuxiang
 */
public class VideoCallActivity extends NewBaseActivity {
    private final String TAG = VideoCallActivity.class.getSimpleName();
    @FindViewById(R.id.activity_video_call_avatar_aiv)
    private ImageView mAvatarAiv;
    @FindViewById(R.id.activity_video_call_name_tv)
    private TextView mDoctorNameTv;
    @FindViewById(R.id.activity_video_call_title_tv)
    private TextView mTitleTv;
    @FindViewById(R.id.activity_video_call_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.activity_video_call_department_tv)
    private TextView mDepartmentTv;
    @FindViewById(R.id.activity_video_call_patient_tv)
    private TextView mPatientNameTv;
    @FindViewById(R.id.activity_video_call_patient_illness_tv)
    private TextView mIllnessTv;
    @FindViewById(R.id.activity_video_call_tip_tv)
    private TextView mTipTv;
    @FindViewById(R.id.activity_video_call_calling_ll)
    private LinearLayout mCallingLl;
    @FindViewById(R.id.activity_video_call_confirm_btn)
    private Button mCloseBtn;
    @FindViewById(R.id.activity_video_call_miss_ll)
    private LinearLayout activityVideoCallMissLl;
    @AppApplication.Manager
    @SuppressWarnings("unused")
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    DoctorManager doctorManager;
    @AppApplication.Manager
    ConsultManager consultManager;
    //属性声明
    private int mPageType = 0;//页面类型
    private long mCallTime = 0;//医生呼叫时间
    private int mAppointmentId;//预约号
    private int mDoctorId;//医生id
    private int mUserType;//医生id
    private int mAppointmentType;//16值班门诊 14其他
    private MediaPlayer mPlayer;//铃声播放
    private Vibrator mVibrator;//震动

    @Override
    protected void handleIntent(Intent intent, Display display) {
        mPageType = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAGE_TYPE, 0);
        mCallTime = intent.getLongExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_CALL_TIME, 0);
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        mDoctorId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
        mUserType = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, 1);
        mAppointmentType = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_TYPE, 14);
    }

    @Override
    protected void initDatas() {
        AppApplication.getInstance().setIsCalling(true);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_video_call;
    }

    @Override
    protected void initViews() {
        showViewByType(mPageType);
        getDoctorInfo(mDoctorId);
        if (mAppointmentType != 16) {
            getPatientInfo(mAppointmentId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayerAndVibrator();
        AppApplication.getInstance().setIsCalling(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //屏蔽返回按钮
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.layout_video_call_calling_refuse_ib,
            R.id.layout_video_call_calling_accept_ib,
            R.id.activity_video_call_confirm_btn,
    R.id.activity_video_call_join_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_video_call_calling_refuse_ib:
                stopPlayerAndVibrator();
                sendRefuseMsg(mAppointmentId, mDoctorId);
                AppApplication.getInstance().setIsCalling(false);
                finish();
                break;

            case R.id.layout_video_call_calling_accept_ib:
                stopPlayerAndVibrator();
                if (mAppointmentType == 16) {
                    getDisplay().startDCRoomActivity(null, mAppointmentId, mUserType, 0);
                    finish();
                } else {
                    if (mAppointmentId < 500000) {
                        Intent intent = new Intent(this, ConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, mUserType);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, RoundsConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mAppointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, mUserType);
                        startActivity(intent);
                        finish();
                    }
                }
                break;

            case R.id.activity_video_call_confirm_btn:
                stopPlayerAndVibrator();
                finish();
                break;
            case R.id.activity_video_call_join_btn:
                stopPlayerAndVibrator();
                if (mAppointmentType == 16) {
                    getDisplay().startDCRoomActivity(null, mAppointmentId, mUserType, 0);
                    finish();
                } else {
                    if (mAppointmentId < 500000) {
                        Intent intent = new Intent(this, ConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, mUserType);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, RoundsConsultRoomActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mAppointmentId);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_USER_TYPE, mUserType);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据页面类型显示对应视图
     *
     * @param type 页面类型
     */
    private void showViewByType(int type) {
        switch (type) {
            case AppConstant.VideoCallPageType.PAGE_TYPE_CALL:
                if (mAppointmentType == 16) {
                    mTipTv.setText(getString(R.string.video_call_tip_one_dc));
                } else {
                    mTipTv.setText(getString(R.string.video_call_tip_one));
                }
                playSystemRingtone();
                startVibrator();
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        showViewByType(AppConstant.VideoCallPageType.PAGE_TYPE_MISS_CALL);
                        stopPlayerAndVibrator();
                        AppApplication.getInstance().setIsCalling(false);
                    }
                }.start();
                break;

            case AppConstant.VideoCallPageType.PAGE_TYPE_MISS_CALL:
                mCallingLl.setVisibility(View.GONE);
                activityVideoCallMissLl.setVisibility(View.VISIBLE);
                String date = TimeUtils.string2String(TimeUtils.millis2String(mCallTime * 1000),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
                if (mAppointmentType == 16) {
                    mTipTv.setText(getString(R.string.video_call_tip_five, date));
                } else {
                    if (mPageType == AppConstant.VideoCallPageType.PAGE_TYPE_DOCTOR_ASSISTANT) {
                        mTipTv.setText(getString(R.string.video_call_tip_three, date));
                    } else {
                        mTipTv.setText(getString(R.string.video_call_tip_two, date));
                    }
                }
                break;

            case AppConstant.VideoCallPageType.PAGE_TYPE_DOCTOR_ASSISTANT:
                mTipTv.setText(getString(R.string.video_call_tip_four));
                playSystemRingtone();
                startVibrator();
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        showViewByType(AppConstant.VideoCallPageType.PAGE_TYPE_MISS_CALL);
                        stopPlayerAndVibrator();
                    }
                }.start();
                break;
            default:
                break;
        }
    }

    private void getDoctorInfo(int doctorId) {
        doctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo != null) {
                    displayDoctorInfo(doctorBaseInfo);
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

    private void displayDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        mDoctorNameTv.setText(doctorBaseInfo.getRealName());
        mHospitalTv.setText(doctorBaseInfo.getHospitalName());
        mDepartmentTv.setText(doctorBaseInfo.getDepartmentName());
        if (mPageType == AppConstant.VideoCallPageType.PAGE_TYPE_DOCTOR_ASSISTANT) {
            mTitleTv.setText(R.string.doctor_assistant);
        } else {
            mTitleTv.setText(doctorBaseInfo.getDoctorLevel());
        }
        GlideUtils.showImage(mAvatarAiv, getThisActivity(), AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
    }

    private void getPatientInfo(int appointmentId) {
        consultManager.getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null && null != patientInfo.getPatientBaseInfo()) {
                    mPatientNameTv.setText(getString(R.string.video_call_patient_name, patientInfo.getPatientBaseInfo().getRealName()));
                    mIllnessTv.setText(getString(R.string.video_call_patient_illness, patientInfo.getPatientBaseInfo().getFirstCureResult()));
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
     * 发送拒绝通知给医生
     */
    private void sendRefuseMsg(int appId, int doctorId) {
        Logger.logD(Logger.ROOM, TAG + "->sendRefuseMsg()");
        int senderID = mUserInfoManager.getCurrentUserInfo().getUserId();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("smsType", getSmsRefuseType(mPageType));
            jsonObject.put("uid", senderID);
            jsonObject.put("puid", doctorId);
            jsonObject.put("aid", appId);
            AppApplication.getInstance().getManager(MessageManager.class).sendMessage(senderID, doctorId, (byte) getSmsRefuseType(mPageType), jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getSmsRefuseType(int pageType) {
        if (pageType == AppConstant.VideoCallPageType.PAGE_TYPE_CALL) {
            return MessageProtocol.SMS_TYPE_PATIENT_REFUSE;
        } else {
            return MessageProtocol.SMS_TYPE_DOCTOR_REFUSE;
        }

    }

    /**
     * 播放来电铃声
     */
    private void playSystemRingtone() {
        try {
            mPlayer = new MediaPlayer();
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            mPlayer.setDataSource(getThisActivity(), uri);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mPlayer.setLooping(true);
                mPlayer.prepare();
                mPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始震动
     */
    private void startVibrator() {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {600, 1000, 600, 1000};   // 停止 开启 停止 开启
        mVibrator.vibrate(pattern, 2);
    }

    private void stopPlayerAndVibrator() {
        if (mVibrator != null && mVibrator.hasVibrator()) {
            mVibrator.cancel();
        }
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }
}
