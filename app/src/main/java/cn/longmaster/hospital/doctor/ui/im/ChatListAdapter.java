package cn.longmaster.hospital.doctor.ui.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lmmedia.PPAmrPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CallServiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CardGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.GuideSetDateGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.IssueAdviceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MedicalAdviceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberJoinGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.PictureGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.RejectGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.TextGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.TriageGroupMessagInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VideoDateGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VoiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.WaitingAddmissionGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.im.AudioPlayManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorDetailActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.DisplayUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * IM消息类型adapter
 * <p>
 * Created by YY on 2017/8/21.
 */

public class ChatListAdapter extends SimpleBaseAdapter<BaseGroupMessageInfo, ChatListAdapter.ViewHolder> {
    private final String TAG = ChatListAdapter.class.getSimpleName();
    private Context mContext;
    private OnPictureGroupMessageClickListener mOnPictureGroupMessageClickListener;
    private OnPictureGroupMessageLongClickListener mOnPictureGroupMessageLongClickListener;
    private OnSendFailGroupMessageClickListener mOnSendFailGroupMessageClickListener;
    private OnAdmissionsGroupMessageClickListener mOnAdmissionsGroupMessageClickListener;
    private OnRefuseAdmissionsGroupMessageClickListener mOnRefuseAdmissionsGroupMessageClickListener;
    private OnIssueAdviseGroupMessageClickListener mOnIssueAdviseGroupMessageClickListener;
    private OnMedicalAdviceGroupMessageClickListener mOnMedicalAdviceGroupMessageClickListener;
    private int mMyUserId;
    private int mMyRole;
    private int mStatus = 0;
    private Map<Integer, PatientInfo> mPatientInfos = new HashMap<>();
    private Map<Integer, DoctorBaseInfo> mDoctorInfos = new HashMap<>();
    private boolean isRightCardMsg = true;
    private AudioPlayManager mAudioPlayManager;
    private DoctorManager mDoctorManager;

    public ChatListAdapter(Context context) {
        super(context);
        mContext = context;
        mMyUserId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
        mAudioPlayManager = AppApplication.getInstance().getManager(AudioPlayManager.class);
        mDoctorManager = AppApplication.getInstance().getManager(DoctorManager.class);
    }

    public void setOnPictureGroupMessageClickListener(OnPictureGroupMessageClickListener listener) {
        this.mOnPictureGroupMessageClickListener = listener;
    }

    public void setOnPictureGroupMessageLongClickListener(OnPictureGroupMessageLongClickListener listener) {
        this.mOnPictureGroupMessageLongClickListener = listener;
    }

    public void setOnSendFailGroupMessageClickListener(OnSendFailGroupMessageClickListener listener) {
        this.mOnSendFailGroupMessageClickListener = listener;
    }

    public void setOnAdmissionsGroupMessageClickListener(OnAdmissionsGroupMessageClickListener listener) {
        this.mOnAdmissionsGroupMessageClickListener = listener;
    }

    public void setOnRefuseAdmissionsGroupMessageClickListener(OnRefuseAdmissionsGroupMessageClickListener listener) {
        this.mOnRefuseAdmissionsGroupMessageClickListener = listener;
    }

    public void setOnIssueAdviseGroupMessageClickListener(OnIssueAdviseGroupMessageClickListener listener) {
        this.mOnIssueAdviseGroupMessageClickListener = listener;
    }

    public void seOnMedicalAdviceGroupMessageClickListener(OnMedicalAdviceGroupMessageClickListener listener) {
        this.mOnMedicalAdviceGroupMessageClickListener = listener;
    }

    public void setMyRole(int role) {
        mMyRole = role;
    }

    public void setRoomStatus(int status) {
        mStatus = status;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_chat;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, BaseGroupMessageInfo baseGroupMessageInfo, int position) {
        Logger.logD(Logger.IM, TAG + "->convert()->baseGroupMessageInfo:" + baseGroupMessageInfo);
        viewHolder.resetMessageView(position, getData().size());
        viewHolder.displayMessageDate(baseGroupMessageInfo, position);
        viewHolder.displayMessageState(baseGroupMessageInfo);
        viewHolder.displayRoleInfo(baseGroupMessageInfo);
        viewHolder.displayGroupMessage(baseGroupMessageInfo, position);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_chat)
        private RelativeLayout chatItemView;
        @FindViewById(R.id.chat_center_msg_layout)
        private LinearLayout centerMessageContainerView;
        @FindViewById(R.id.chat_date_msg)
        private TextView messageDateView;
        @FindViewById(R.id.chat_medical_record_layout)
        private LinearLayout medicalRecordContainerView;
        @FindViewById(R.id.chat_medical_record_appointment_id)
        private TextView medicalRecordAppIdView;
        @FindViewById(R.id.chat_medical_record_name)
        private TextView medicalRecordNameView;
        @FindViewById(R.id.chat_medical_record_sex)
        private TextView medicalRecordSexView;
        @FindViewById(R.id.chat_medical_record_age)
        private TextView medicalRecordAgeView;
        @FindViewById(R.id.chat_medical_record_superior_expert)
        private TextView medicalRecordSuperiorExpertView;
        @FindViewById(R.id.chat_medical_record_superior_expert_hospital)
        private TextView medicalRecordSuperiorExpertHospitalView;
        @FindViewById(R.id.chat_medical_record_superior_expert_department)
        private TextView medicalRecordSuperiorExpertDepartmentView;
        @FindViewById(R.id.chat_medical_record_buttons)
        private LinearLayout medicalRecordButtonsView;
        @FindViewById(R.id.chat_system_message_layout)
        private LinearLayout systemMessageContainerView;
        @FindViewById(R.id.chat_system_msg_buttons_layout)
        private LinearLayout systemMessageButtonsView;
        @FindViewById(R.id.chat_system_message_button)
        private TextView systemMessageButtonView;
        @FindViewById(R.id.chat_left_msg)
        private LinearLayout msgTypeLeftLl;
        @FindViewById(R.id.chat_left_role)
        private TextView leftRoleView;
        @FindViewById(R.id.chat_left_name)
        private TextView leftNameView;
        @FindViewById(R.id.item_chat_msg_type_left_text_layout)
        private LinearLayout msgTypeLeftTextLl;
        @FindViewById(R.id.item_chat_msg_type_left_doctor_card_layout)
        private LinearLayout msgTypeLeftDoctorCardLl;
        @FindViewById(R.id.item_chat_msg_type_left_pic_layout)
        private RelativeLayout msgTypeLeftPicLl;
        @FindViewById(R.id.item_chat_msg_type_left_voice_layout)
        private LinearLayout msgTypeLeftVoiceLl;
        @FindViewById(R.id.chat_left_voice_volume)
        private ImageView leftVoiceIconView;
        @FindViewById(R.id.chat_right_msg)
        private RelativeLayout msgTypeRightLl;
        @FindViewById(R.id.chat_right_text_msg_layout)
        private LinearLayout msgTypeRightTextLl;
        @FindViewById(R.id.chat_right_doctor_card_msg_layout)
        private LinearLayout msgTypeRightDoctorCardLl;
        @FindViewById(R.id.chat_right_pic_msg_layout)
        private RelativeLayout msgTypeRightPicLl;
        @FindViewById(R.id.chat_right_voice_msg_layout)
        private LinearLayout msgTypeRightVoiceLl;
        @FindViewById(R.id.chat_right_pic_msg_img)
        private ImageView chatRightPicImg;
        @FindViewById(R.id.item_chat_msg_type_left_pic_img)
        private ImageView chatLeftPicImg;
        @FindViewById(R.id.chat_right_resend_ll)
        private LinearLayout chatResendLl;
        @FindViewById(R.id.chat_right_avatar)
        private CircleImageView msgTypeRightAvatarImg;
        @FindViewById(R.id.chat_right_voice_time_length)
        private TextView msgTypeRightVoiceTimeTv;
        @FindViewById(R.id.chat_right_voice_volume)
        private ImageView rightVioceIconView;
        @FindViewById(R.id.chat_right_pic_wait)
        public ProgressBar chatRightPicWait;
        @FindViewById(R.id.chat_right_voice_wait)
        private ProgressBar chatRightVoiceWait;
        @FindViewById(R.id.chat_right_voice_time_length)
        private TextView chatRightVoiceTimeLength;
        @FindViewById(R.id.chat_medical_record_admissions)
        private TextView medicalRecordAdmissions;
        @FindViewById(R.id.chat_medical_record_refuse_admissions)
        private TextView medicalRecordRefuseAdmissions;
        @FindViewById(R.id.chat_right_text_msg_tv)
        private TextView rightTextView;
        @FindViewById(R.id.item_chat_msg_type_left_tv)
        private TextView leftTextView;
        @FindViewById(R.id.item_chat_msg_type_left_voice_tv)
        private TextView leftVoiceTimeLengthView;
        @FindViewById(R.id.chat_right_doctor_card_department)
        private TextView rightCardDepartmentView;
        @FindViewById(R.id.chat_left_doctor_card_department)
        private TextView leftCardDepartmentView;
        @FindViewById(R.id.chat_right_doctor_card_hospital)
        private TextView rightCardHospitalView;
        @FindViewById(R.id.chat_left_doctor_card_hospital)
        private TextView leftCardHospitalView;
        @FindViewById(R.id.chat_right_doctor_card_name)
        private TextView rightCardNameView;
        @FindViewById(R.id.chat_right_doctor_card_title)
        private TextView rightCardTitleView;
        @FindViewById(R.id.chat_left_doctor_card_name)
        private TextView leftCardNameView;
        @FindViewById(R.id.chat_left_doctor_card_title)
        private TextView leftCardTitleView;
        @FindViewById(R.id.chat_system_message_title)
        private TextView systemMessageTitleView;
        @FindViewById(R.id.chat_system_message_content)
        private TextView systemMessageContentView;

        private void resetMessageView(int position, int size) {
            centerMessageContainerView.setVisibility(View.GONE);
            messageDateView.setVisibility(View.GONE);
            medicalRecordContainerView.setVisibility(View.GONE);
            systemMessageContainerView.setVisibility(View.GONE);
            medicalRecordButtonsView.setVisibility(View.GONE);
            systemMessageButtonsView.setVisibility(View.GONE);
            msgTypeLeftLl.setVisibility(View.GONE);
            msgTypeRightLl.setVisibility(View.GONE);
            msgTypeRightTextLl.setVisibility(View.GONE);
            msgTypeLeftTextLl.setVisibility(View.GONE);
            msgTypeRightDoctorCardLl.setVisibility(View.GONE);
            msgTypeLeftDoctorCardLl.setVisibility(View.GONE);
            msgTypeRightVoiceLl.setVisibility(View.GONE);
            msgTypeLeftVoiceLl.setVisibility(View.GONE);
            msgTypeLeftPicLl.setVisibility(View.GONE);
            msgTypeRightPicLl.setVisibility(View.GONE);
        }

        private void displayMessageDate(BaseGroupMessageInfo baseGroupMessageInfo, int position) {
            if (position % 10 == 0) {
                centerMessageContainerView.setVisibility(View.VISIBLE);
                messageDateView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (position == 0) {
                    params.topMargin = DisplayUtil.dip2px(20);
                    params.bottomMargin = DisplayUtil.dip2px(20);
                } else {
                    params.topMargin = DisplayUtil.dip2px(0);
                    params.bottomMargin = DisplayUtil.dip2px(20);
                }
                messageDateView.setLayoutParams(params);
                if (DateUtil.isSameDay(baseGroupMessageInfo.getSendDt() * 1000, System.currentTimeMillis())) {
                    messageDateView.setText(new SimpleDateFormat("HH:mm").format(new Date(baseGroupMessageInfo.getSendDt() * 1000)));
                } else {
                    messageDateView.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new Date(baseGroupMessageInfo.getSendDt() * 1000)));
                }
            } else {
                messageDateView.setVisibility(View.GONE);
            }
        }

        private void displayMessageState(BaseGroupMessageInfo baseGroupMessageInfo) {
            if (baseGroupMessageInfo.getSenderId() != mMyUserId) {
                return;
            }
            if (baseGroupMessageInfo.getMsgState() == BaseGroupMessageInfo.STATE_SUCCESS) {
                chatRightVoiceTimeLength.setVisibility(View.VISIBLE);
            } else {
                chatRightVoiceTimeLength.setVisibility(View.GONE);
            }
            if (baseGroupMessageInfo.getMsgState() == BaseGroupMessageInfo.STATE_SENDING) {
                chatRightVoiceWait.setVisibility(View.VISIBLE);
                chatRightVoiceTimeLength.setVisibility(View.GONE);
            } else {
                chatRightVoiceWait.setVisibility(View.GONE);
            }
            if (baseGroupMessageInfo.getMsgState() == BaseGroupMessageInfo.STATE_FAILED) {
                chatResendLl.setVisibility(View.VISIBLE);

            } else {
                chatResendLl.setVisibility(View.GONE);
            }
        }

        private void displayRoleInfo(BaseGroupMessageInfo baseGroupMessageInfo) {
            try {
                JSONObject obj = new JSONObject(baseGroupMessageInfo.getMsgContent());
                int role = obj.getInt("role");

                if (baseGroupMessageInfo.getSenderId() == mMyUserId) {
                    if (role == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                        GlideUtils.showPatientChatAvatar(msgTypeRightAvatarImg, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getSenderId(), ""));
                    } else {
                        GlideUtils.showDoctorChatAvatar(msgTypeRightAvatarImg, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getSenderId(), ""));
                    }
                } else {
                    CircleImageView avatar = chatItemView.findViewById(R.id.chat_left_avatar);
                    if (role == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                        GlideUtils.showPatientChatAvatar(avatar, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getSenderId(), ""));
                    } else {
                        GlideUtils.showDoctorChatAvatar(avatar, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getSenderId(), ""));
                    }
                    leftRoleView.setText(getRoleName(role) + ":");
                    if (role == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                        displayPatientName(baseGroupMessageInfo.getAppointmentId());
                    } else {
                        displayDoctorInfo(baseGroupMessageInfo.getSenderId());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String getRoleName(int role) {
            String roleName = "";
            switch (role) {
                case AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT:
                    roleName = mContext.getString(R.string.chat_role_patient);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR:
                    roleName = mContext.getString(R.string.chat_role_attending_doctor);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_ADMINISTRATOR:
                    roleName = mContext.getString(R.string.chat_role_administrator);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR:
                    roleName = mContext.getString(R.string.chat_role_superior_doctor);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_ASSISTANT_DOCTOR:
                    roleName = mContext.getString(R.string.chat_role_assistant_doctor);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_CONFERENCE_STAFF:
                    roleName = mContext.getString(R.string.chat_role_conference_staff);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_DOCTOR:
                    roleName = mContext.getString(R.string.chat_role_mdt_doctor);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT:
                    roleName = mContext.getString(R.string.chat_role_mdt_patient);
                    break;

                case AppConstant.IMGroupRole.IM_GROUP_ROLE_SYSTEM_AUTO:
                    roleName = mContext.getString(R.string.chat_role_system_auto);
                    break;
            }
            return roleName;
        }

        public void displayPatientName(final int appointmentId) {
            if (mPatientInfos.containsKey(appointmentId)) {
                leftNameView.setText(mPatientInfos.get(appointmentId).getPatientBaseInfo().getRealName());
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfos.put(appointmentId, patientInfo);
                            leftNameView.setText(patientInfo.getPatientBaseInfo().getRealName());
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
        }

        private void displayDoctorInfo(final int doctorId) {
            if (mDoctorInfos.containsKey(doctorId)) {
                leftNameView.setText(mDoctorInfos.get(doctorId).getRealName());
            } else {
                mDoctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        if (null == doctorBaseInfo) {
                            return;
                        }
                        mDoctorInfos.put(doctorId, doctorBaseInfo);
                        leftNameView.setText(doctorBaseInfo.getRealName());
                    }

                    @Override
                    public void onFailed(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        }

        private void displayGroupMessage(BaseGroupMessageInfo baseGroupMessageInfo, int position) {
            switch (baseGroupMessageInfo.getMsgType()) {
                case BaseGroupMessageInfo.PICTURE_MSG:
                    displayPictureGroupMessage((PictureGroupMessageInfo) baseGroupMessageInfo, position);
                    break;

                case BaseGroupMessageInfo.TEXT_MSG:
                    displayTextGroupMessage((TextGroupMessageInfo) baseGroupMessageInfo, position);
                    break;

                case BaseGroupMessageInfo.VOICE_MSG:
                    displayVoiceGroupMessage((VoiceGroupMessageInfo) baseGroupMessageInfo, position);
                    break;

                case BaseGroupMessageInfo.CARD_MSG:
                    displayCardGroupMessage((CardGroupMessageInfo) baseGroupMessageInfo, position);
                    break;

                case BaseGroupMessageInfo.WAITING_ADMISSION_MSG:
                    displayWaitingAddmissionGroupMessage((WaitingAddmissionGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.VIDEO_DATE_MSG:
                    displayVideoDateGroupMessage((VideoDateGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.MEMBER_JOIN_MSG:
                    displayMemberJoinGroupMessage((MemberJoinGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.REJECT_MSG:
                    displayRejectGroupGroupMessage((RejectGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.TRIAGE_MSG:
                    displayTriageGroupGroupMessage((TriageGroupMessagInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.ISSUE_ADVICE_MSG:
                    displayIssueAdviceGroupMessage((IssueAdviceGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.MEDICAL_ADVICE_MSG:
                    displayMedicalAdviceGroupMessage((MedicalAdviceGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.CALL_SERVICE_MSG:
                    displayCallServerGroupMessage((CallServiceGroupMessageInfo) baseGroupMessageInfo);
                    break;

                case BaseGroupMessageInfo.GUIDE_SET_DATE_MSG:
                    displayGuideSetDateGroupMessage((GuideSetDateGroupMessageInfo) baseGroupMessageInfo);
                    break;

                default:
                    Logger.logD(Logger.IM, TAG + "->displayGroupMessage()->不支持的消息类型->baseGroupMessageInfo:" + baseGroupMessageInfo);
                    break;
            }
        }

        public void displayPictureGroupMessage(PictureGroupMessageInfo pictureInfo, int position) {
            if (pictureInfo.getSenderId() == mMyUserId) {
                msgTypeRightLl.setVisibility(View.VISIBLE);
                msgTypeRightPicLl.setVisibility(View.VISIBLE);
                chatRightPicWait.setVisibility(View.VISIBLE);
                setRightPic(pictureInfo, position);
            } else {
                msgTypeLeftLl.setVisibility(View.VISIBLE);
                msgTypeLeftPicLl.setVisibility(View.VISIBLE);
                setLeftPic(pictureInfo);
            }
        }

        private void setRightPic(final PictureGroupMessageInfo messageInfo, final int position) {
            String fileName = messageInfo.getPictureName();
            if (messageInfo.getMsgState() == BaseGroupMessageInfo.STATE_UPLOAD_SUCCESS || messageInfo.getMsgState() == BaseGroupMessageInfo.STATE_SUCCESS) {
                chatRightPicWait.setVisibility(View.GONE);
                if (fileName.contains(".jpg")) {
                    fileName = fileName.replace(".jpg", "_s.jpg");
                } else {
                    fileName = fileName + "_s.jpg";
                }
            }
            String url = AppConfig.getImPictureDownloadUrl() + fileName;
            //chatRightPicImg.loadImage(SdManager.getInstance().getIMPic() + fileName, url);
            Logger.logD(Logger.IM, TAG + "->setRightPic->url:" + url + ", fileName:" + SdManager.getInstance().getIMPic() + fileName);
            //GlideUtils.showImage(chatRightPicImg,mContext,SdManager.getInstance().getIMPic() + fileName);
            GlideUtils.showImage(chatRightPicImg, mContext, url, new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    chatRightPicWait.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            });
            chatRightPicImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnPictureGroupMessageClickListener.onPictureGroupMessageClicked(v, messageInfo);
                }
            });
            chatRightPicImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnPictureGroupMessageLongClickListener != null) {
                        mOnPictureGroupMessageLongClickListener.onPictureGroupMessageLongClicked((ImageView) v, messageInfo, true);
                    }
                    return false;
                }
            });

            chatResendLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSendFailGroupMessageClickListener != null) {
                        mOnSendFailGroupMessageClickListener.onSendFailGroupMessageClicked(messageInfo, position, chatRightPicWait, v);
                    }
                }
            });
        }

        private void setLeftPic(final PictureGroupMessageInfo messageInfo) {
            Logger.logD(Logger.COMMON, "onSendStateChanged_roleBasicInfo:" + messageInfo.getPictureName());
            String fileName = messageInfo.getPictureName();
            if (fileName.contains(".jpg")) {
                fileName = fileName.replace(".jpg", "_s.jpg");
            } else {
                fileName = fileName + "_s.jpg";
            }
            String url = AppConfig.getImPictureDownloadUrl() + fileName;
            Logger.logD(Logger.IM, TAG + "->setLeftPic()->url:" + url);
            //GlideUtils.showImage(chatRightPicImg,mContext,SdManager.getInstance().getIMPic() + fileName);
            GlideUtils.showImage(chatLeftPicImg, mContext, url);
            chatLeftPicImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnPictureGroupMessageClickListener != null) {
                        mOnPictureGroupMessageClickListener.onPictureGroupMessageClicked(v, messageInfo);
                    }
                }
            });
            chatLeftPicImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnPictureGroupMessageLongClickListener != null) {
                        mOnPictureGroupMessageLongClickListener.onPictureGroupMessageLongClicked((ImageView) v, messageInfo, false);
                    }
                    return false;
                }
            });
        }

        private void displayTextGroupMessage(TextGroupMessageInfo textMsgInfo, int position) {
            if (textMsgInfo.getSenderId() == mMyUserId) {
                msgTypeRightLl.setVisibility(View.VISIBLE);
                msgTypeRightTextLl.setVisibility(View.VISIBLE);
                setRightView(textMsgInfo, position);
            } else {
                msgTypeLeftLl.setVisibility(View.VISIBLE);
                msgTypeLeftTextLl.setVisibility(View.VISIBLE);
                setLeftTextView(textMsgInfo);
            }
        }

        private void setRightView(final TextGroupMessageInfo roleBasicInfo, final int position) {
            rightTextView.setText(roleBasicInfo.getContent());
            chatResendLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSendFailGroupMessageClickListener != null) {
                        mOnSendFailGroupMessageClickListener.onSendFailGroupMessageClicked(roleBasicInfo, position, chatRightVoiceWait, v);
                    }
                }
            });
        }

        private void setLeftTextView(TextGroupMessageInfo roleBasicInfo) {
            leftTextView.setText(roleBasicInfo.getContent());
        }

        private void displayVoiceGroupMessage(final VoiceGroupMessageInfo voiceMsgInfo, int position) {
            if (voiceMsgInfo.getSenderId() == mMyUserId) {
                msgTypeRightLl.setVisibility(View.VISIBLE);
                msgTypeRightVoiceLl.setVisibility(View.VISIBLE);
                setRightVoice(voiceMsgInfo, position);
                msgTypeRightVoiceLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVoiceGroupMessage(voiceMsgInfo);
                    }
                });
            } else {
                msgTypeLeftLl.setVisibility(View.VISIBLE);
                msgTypeLeftVoiceLl.setVisibility(View.VISIBLE);
                setLeftVoice(voiceMsgInfo);
                msgTypeLeftVoiceLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVoiceGroupMessage(voiceMsgInfo);
                    }
                });
            }

            ImageView iconView = leftVoiceIconView;
            if (voiceMsgInfo.getSenderId() == mMyUserId) {
                iconView = rightVioceIconView;
            }

            if (mAudioPlayManager.isPlaying()) {
                if ((SdManager.getInstance().getIMVoice() + voiceMsgInfo.getFileName()).equals(mAudioPlayManager.getCurrentPlayAudio())) {
                    ((AnimationDrawable) iconView.getBackground()).start();
                } else {
                    AnimationDrawable drawable = (AnimationDrawable) iconView.getBackground();
                    drawable.selectDrawable(0);
                    drawable.stop();
                }
            }
        }

        private void setRightVoice(final VoiceGroupMessageInfo voiceGroupMessageInfo, final int position) {
            int timeLength = voiceGroupMessageInfo.getTimeLength();
            Logger.logD(Logger.IM, TAG + "->setRightVoice()->timeLength:" + timeLength);
            String timeStr = "";
            String secondStr = "";
            int minute = timeLength / 60;
            int second = timeLength % 60;
            if (second < 10) {
                secondStr = "0" + second;
            } else {
                secondStr = second + "";
            }
            if (minute == 0) {
                timeStr = secondStr + "''";
            } else {
                timeStr = minute + "'" + secondStr + "''";
            }
            chatRightVoiceTimeLength.setText(timeStr);
            chatResendLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSendFailGroupMessageClickListener != null) {
                        mOnSendFailGroupMessageClickListener.onSendFailGroupMessageClicked(voiceGroupMessageInfo, position, chatRightVoiceWait, v);
                    }
                }
            });
        }

        private void setLeftVoice(VoiceGroupMessageInfo voiceGroupMessageInfo) {
            int timeLength = voiceGroupMessageInfo.getTimeLength();
            String timeStr = "";
            String secondStr = "";
            int minute = timeLength / 60;
            int second = timeLength % 60;
            if (second < 10) {
                secondStr = "0" + second;
            }
            if (minute == 0) {
                timeStr = secondStr + "''";
            } else {
                timeStr = minute + "'" + secondStr + "''";
            }

            leftVoiceTimeLengthView.setText(timeStr);
        }

        private void playVoiceGroupMessage(final VoiceGroupMessageInfo messageInfo) {
            final String filePath = SdManager.getInstance().getIMVoice() + messageInfo.getFileName();
            final String url = AppConfig.getImVoiceDownloadUrl() + messageInfo.getFileName();
            if (mAudioPlayManager.isPlaying()) {
                mAudioPlayManager.stopPlay();
                if (mAudioPlayManager.getCurrentPlayAudio().equals(filePath)) {
                    return;
                }
            }

            AppApplication.HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAudioPlayManager.startAudioPlay(filePath, url, new PPAmrPlayer.OnStateListener() {
                        @Override
                        public void onStart(String s) {
                            if (messageInfo.getSenderId() == mMyUserId) {
                                ((AnimationDrawable) rightVioceIconView.getBackground()).start();
                            } else {
                                ((AnimationDrawable) leftVoiceIconView.getBackground()).start();
                            }
                        }

                        @Override
                        public void onStop(String s) {
                            AnimationDrawable drawable;
                            if (messageInfo.getSenderId() == mMyUserId) {
                                drawable = (AnimationDrawable) rightVioceIconView.getBackground();
                            } else {
                                drawable = (AnimationDrawable) leftVoiceIconView.getBackground();
                            }
                            drawable.selectDrawable(0);
                            drawable.stop();
                        }

                        @Override
                        public void onError(String s) {
                            Toast.makeText(mContext, R.string.chat_audio_play_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, 200);
        }

        private void displayCardGroupMessage(CardGroupMessageInfo baseGroupMessageInfo, int position) {
            if (baseGroupMessageInfo.getSenderId() == mMyUserId) {
                isRightCardMsg = true;
                msgTypeRightLl.setVisibility(View.VISIBLE);
                msgTypeRightDoctorCardLl.setVisibility(View.VISIBLE);
                setRightDoctorCard(baseGroupMessageInfo, position);
            } else {
                isRightCardMsg = false;
                msgTypeLeftLl.setVisibility(View.VISIBLE);
                msgTypeLeftDoctorCardLl.setVisibility(View.VISIBLE);
                setLeftDoctorCard(baseGroupMessageInfo);
            }
            displayCardDoctorInfo(baseGroupMessageInfo.getDoctorId());
        }

        private void setRightDoctorCard(final CardGroupMessageInfo baseGroupMessageInfo, final int position) {
            msgTypeRightDoctorCardLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DoctorDetailActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, baseGroupMessageInfo.getDoctorId());
                    mContext.startActivity(intent);
                }
            });
            CircleImageView avatar = chatItemView.findViewById(R.id.chat_right_doctor_card_avtar);
            GlideUtils.showPatientChatAvatar(avatar, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getDoctorId(), "0"));

            chatResendLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSendFailGroupMessageClickListener != null) {
                        mOnSendFailGroupMessageClickListener.onSendFailGroupMessageClicked(baseGroupMessageInfo, position, chatRightVoiceWait, v);
                    }
                }
            });
        }

        private void setLeftDoctorCard(final CardGroupMessageInfo baseGroupMessageInfo) {
            msgTypeLeftDoctorCardLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DoctorDetailActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, baseGroupMessageInfo.getDoctorId());
                    mContext.startActivity(intent);
                }
            });
            CircleImageView circleImageView = chatItemView.findViewById(R.id.chat_left_doctor_card_civ);
            GlideUtils.showDoctorAvatar(circleImageView, mContext, AvatarUtils.getAvatar(false, baseGroupMessageInfo.getDoctorId(), "0"));
        }

        private void displayCardDoctorInfo(final int doctorId) {
            if (mDoctorInfos.containsKey(doctorId)) {
                displayCardMsg(mDoctorInfos.get(doctorId));
                if (isRightCardMsg) {
                    rightCardDepartmentView.setText(mDoctorInfos.get(doctorId).getDepartmentName());
                    rightCardHospitalView.setText(mDoctorInfos.get(doctorId).getHospitalName());
                } else {
                    leftCardDepartmentView.setText(mDoctorInfos.get(doctorId).getDepartmentName());
                    leftCardHospitalView.setText(mDoctorInfos.get(doctorId).getHospitalName());
                }
            } else {
                mDoctorManager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        if (null == doctorBaseInfo) {
                            return;
                        }
                        mDoctorInfos.put(doctorId, doctorBaseInfo);
                        if (isRightCardMsg) {
                            rightCardHospitalView.setText(doctorBaseInfo.getHospitalName());
                        } else {
                            leftCardHospitalView.setText(doctorBaseInfo.getHospitalName());
                        }
                        if (isRightCardMsg) {
                            rightCardDepartmentView.setText(doctorBaseInfo.getDepartmentName());
                        } else {
                            leftCardDepartmentView.setText(doctorBaseInfo.getDepartmentName());
                        }
                        displayCardMsg(doctorBaseInfo);
                    }

                    @Override
                    public void onFailed(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        }

        private void displayCardMsg(DoctorBaseInfo doctorBaseInfo) {
            if (isRightCardMsg) {
                rightCardNameView.setText(doctorBaseInfo.getRealName());
                rightCardTitleView.setText(doctorBaseInfo.getDoctorLevel());
            } else {
                leftCardNameView.setText(doctorBaseInfo.getRealName());
                leftCardTitleView.setText(doctorBaseInfo.getDoctorLevel());
            }
        }

        private void displayWaitingAddmissionGroupMessage(final WaitingAddmissionGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            medicalRecordContainerView.setVisibility(View.VISIBLE);
            medicalRecordButtonsView.setVisibility(View.VISIBLE);
            medicalRecordAppIdView.setText(mContext.getString(R.string.chat_appointment_id, baseGroupMessageInfo.getAppointmentId()));
            if (mPatientInfos.containsKey(baseGroupMessageInfo.getAppointmentId())) {
                PatientInfo patientInfo = mPatientInfos.get(baseGroupMessageInfo.getAppointmentId());
                medicalRecordNameView.setText(patientInfo.getPatientBaseInfo().getRealName());
                medicalRecordSexView.setText(patientInfo.getPatientBaseInfo().getGender() == AppConstant.GenderType.GENDERTYPE_MALE ? mContext.getString(R.string.gender_male) : mContext.getString(R.string.gender_female));
                if ("".equals(patientInfo.getPatientBaseInfo().getAge())) {
                    medicalRecordAgeView.setText("");
                } else {
                    medicalRecordAgeView.setText(mContext.getString(R.string.chat_age, patientInfo.getPatientBaseInfo().getAge()));
                }
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(baseGroupMessageInfo.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfos.put(baseGroupMessageInfo.getAppointmentId(), patientInfo);
                            medicalRecordNameView.setText(patientInfo.getPatientBaseInfo().getRealName());
                            medicalRecordSexView.setText(patientInfo.getPatientBaseInfo().getGender() == AppConstant.GenderType.GENDERTYPE_MALE ? mContext.getString(R.string.gender_male) : mContext.getString(R.string.gender_female));
                            if ("".equals(patientInfo.getPatientBaseInfo().getAge())) {
                                medicalRecordAgeView.setText("");
                            } else {
                                medicalRecordAgeView.setText(mContext.getString(R.string.chat_age, patientInfo.getPatientBaseInfo().getAge()));
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
            if (mDoctorInfos.containsKey(baseGroupMessageInfo.getDoctorId())) {
                DoctorBaseInfo doctorBaseInfo = mDoctorInfos.get(baseGroupMessageInfo.getDoctorId());
                medicalRecordSuperiorExpertView.setText(mContext.getString(R.string.chat_superior_expert, doctorBaseInfo.getRealName()));
                displayHospitalInfoAndDepartmentInfo(doctorBaseInfo);
            } else {
                mDoctorManager.getDoctorInfo(baseGroupMessageInfo.getDoctorId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        mDoctorInfos.put(baseGroupMessageInfo.getDoctorId(), doctorBaseInfo);
                        medicalRecordSuperiorExpertView.setText(mContext.getString(R.string.chat_superior_expert, doctorBaseInfo.getRealName()));
                        displayHospitalInfoAndDepartmentInfo(doctorBaseInfo);
                    }

                    @Override
                    public void onFailed(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
            if (mStatus == AppConstant.IMGroupStatus.IM_GROUP_STATUS_ORIGINAL) {
                medicalRecordAdmissions.setEnabled(true);
                medicalRecordRefuseAdmissions.setEnabled(true);
                medicalRecordAdmissions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnAdmissionsGroupMessageClickListener != null) {
                            mOnAdmissionsGroupMessageClickListener.onAdmissionsGroupMessageClicked(v);
                        }
                    }
                });
                medicalRecordRefuseAdmissions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnRefuseAdmissionsGroupMessageClickListener != null) {
                            mOnRefuseAdmissionsGroupMessageClickListener.onRefuseAdmissionsGroupMessageClicked(v);
                        }
                    }
                });
            } else {
                medicalRecordAdmissions.setEnabled(false);
                medicalRecordRefuseAdmissions.setEnabled(false);
                medicalRecordAdmissions.setTextColor(ContextCompat.getColor(mContext, R.color.color_7d666666));
                medicalRecordRefuseAdmissions.setTextColor(ContextCompat.getColor(mContext, R.color.color_7d666666));
            }
        }

        private void displayHospitalInfoAndDepartmentInfo(DoctorBaseInfo doctorBaseInfo) {
            medicalRecordSuperiorExpertHospitalView.setText(doctorBaseInfo.getHospitalName());
            medicalRecordSuperiorExpertDepartmentView.setText(doctorBaseInfo.getDepartmentName());
        }

        private void displayVideoDateGroupMessage(VideoDateGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_set_scheduling));
            systemMessageContentView.setText(mContext.getString(R.string.chat_video_time, getVideoTime(baseGroupMessageInfo.getDate())));
        }

        private String getVideoTime(long date) {
            String dateStr = "";
            SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy-MM-dd ");
            SimpleDateFormat timesSdf = new SimpleDateFormat("HH:mm");
            String year = yearSdf.format(new Date(date * 1000));
            String time = timesSdf.format(new Date(date * 1000));
            String weekOfDate = " 星期" + getWeekOfDate(date * 1000);
            dateStr = year + weekOfDate + " " + time;
            return dateStr;
        }

        public String getWeekOfDate(long time) {
            Date date = new Date(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String week = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
            if ("1".equals(week)) {
                week = "日";
            } else if ("2".equals(week)) {
                week = "一";
            } else if ("3".equals(week)) {
                week = "二";
            } else if ("4".equals(week)) {
                week = "三";
            } else if ("5".equals(week)) {
                week = "四";
            } else if ("6".equals(week)) {
                week = "五";
            } else if ("7".equals(week)) {
                week = "六";
            }
            return week;
        }

        private void displayMemberJoinGroupMessage(final MemberJoinGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_system_msg));
            final int userId = baseGroupMessageInfo.getUserId();
            if (baseGroupMessageInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                if (mPatientInfos.containsKey(baseGroupMessageInfo.getAppointmentId())) {
                    systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, mPatientInfos.get(baseGroupMessageInfo.getAppointmentId()).getPatientBaseInfo().getRealName()));
                } else {
                    AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(baseGroupMessageInfo.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                        @Override
                        public void onSuccess(PatientInfo patientInfo) {
                            if (patientInfo != null) {
                                mPatientInfos.put(baseGroupMessageInfo.getAppointmentId(), patientInfo);
                                systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, patientInfo.getPatientBaseInfo().getRealName()));
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
            } else if (baseGroupMessageInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_DOCTOR) {
                systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, mContext.getString(R.string.chat_role_mdt_doctor)));
            } else if (baseGroupMessageInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT) {
                systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, mContext.getString(R.string.chat_role_mdt_patient)));
            } else {
                if (mDoctorInfos.containsKey(userId)) {
                    systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, mDoctorInfos.get(userId).getRealName()));
                } else {
                    mDoctorManager.getDoctor(userId, new DoctorManager.OnDoctorInfoLoadListener() {
                        @Override
                        public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                            if (null == doctorBaseInfo) {
                                return;
                            }
                            mDoctorInfos.put(userId, doctorBaseInfo);
                            systemMessageContentView.setText(mContext.getString(R.string.chat_member_join, doctorBaseInfo.getRealName()));
                        }

                        @Override
                        public void onFailed(int code, String msg) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                }
            }
        }

        private void displayRejectGroupGroupMessage(RejectGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_reject_remind));
            systemMessageContentView.setText(mContext.getString(R.string.chat_reject_visits, baseGroupMessageInfo.getReason()));
        }

        private void displayTriageGroupGroupMessage(final TriageGroupMessagInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            medicalRecordContainerView.setVisibility(View.VISIBLE);
            if (mMyRole == AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR) {
                medicalRecordButtonsView.setVisibility(View.VISIBLE);
            }
            medicalRecordAppIdView.setText(mContext.getString(R.string.chat_appointment_id, baseGroupMessageInfo.getAppointmentId()));
            if (mPatientInfos.containsKey(baseGroupMessageInfo.getAppointmentId())) {
                PatientInfo patientInfo = mPatientInfos.get(baseGroupMessageInfo.getAppointmentId());
                medicalRecordNameView.setText(patientInfo.getPatientBaseInfo().getRealName());
                medicalRecordSexView.setText(patientInfo.getPatientBaseInfo().getGender() == AppConstant.GenderType.GENDERTYPE_MALE ? mContext.getString(R.string.gender_male) : mContext.getString(R.string.gender_female));
                if ("".equals(patientInfo.getPatientBaseInfo().getAge())) {
                    medicalRecordAgeView.setText("");
                } else {
                    medicalRecordAgeView.setText(mContext.getString(R.string.chat_age, patientInfo.getPatientBaseInfo().getAge()));
                }
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(baseGroupMessageInfo.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfos.put(baseGroupMessageInfo.getAppointmentId(), patientInfo);
                            medicalRecordNameView.setText(patientInfo.getPatientBaseInfo().getRealName());
                            medicalRecordSexView.setText(patientInfo.getPatientBaseInfo().getGender() == AppConstant.GenderType.GENDERTYPE_MALE ? mContext.getString(R.string.gender_male) : mContext.getString(R.string.gender_female));
                            if ("".equals(patientInfo.getPatientBaseInfo().getAge())) {
                                medicalRecordAgeView.setText("");
                            } else {
                                medicalRecordAgeView.setText(mContext.getString(R.string.chat_age, patientInfo.getPatientBaseInfo().getAge()));
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
            if (baseGroupMessageInfo.getDoctorId() == 0) {
                medicalRecordSuperiorExpertView.setText(mContext.getString(R.string.chat_superior_expert, mContext.getString(R.string.chat_waiting_triage)));
            } else {
                if (mDoctorInfos.containsKey(baseGroupMessageInfo.getDoctorId())) {
                    DoctorBaseInfo doctorBaseInfo = mDoctorInfos.get(baseGroupMessageInfo.getDoctorId());
                    medicalRecordSuperiorExpertView.setText(mContext.getString(R.string.chat_superior_expert, doctorBaseInfo.getRealName()));
                    displayHospitalInfoAndDepartmentInfo(doctorBaseInfo);
                } else {
                    mDoctorManager.getDoctorInfo(baseGroupMessageInfo.getDoctorId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
                        @Override
                        public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                            mDoctorInfos.put(baseGroupMessageInfo.getDoctorId(), doctorBaseInfo);
                            medicalRecordSuperiorExpertView.setText(mContext.getString(R.string.chat_superior_expert, doctorBaseInfo.getRealName()));
                            displayHospitalInfoAndDepartmentInfo(doctorBaseInfo);
                        }

                        @Override
                        public void onFailed(int code, String msg) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                }
            }
        }

        private void displayIssueAdviceGroupMessage(IssueAdviceGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageButtonsView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_system_msg));
            systemMessageButtonView.setText(mContext.getString(R.string.chat_issue_advise));
            systemMessageContentView.setText(mContext.getString(R.string.chat_issue_advice_content));
            systemMessageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnIssueAdviseGroupMessageClickListener != null) {
                        mOnIssueAdviseGroupMessageClickListener.onIssueAdviseGroupMessageClicked(v);
                    }
                }
            });
        }

        private void displayMedicalAdviceGroupMessage(MedicalAdviceGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageButtonsView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_system_msg));
            systemMessageButtonView.setText(mContext.getString(R.string.chat_doctor_advice));
            systemMessageContentView.setText(mContext.getString(R.string.chat_see_advice_content));
            systemMessageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMedicalAdviceGroupMessageClickListener != null) {
                        mOnMedicalAdviceGroupMessageClickListener.onMedicalAdviceGroupMessageClicked(v);
                    }
                }
            });
        }

        private void displayCallServerGroupMessage(final CallServiceGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_call_remind));
            if (baseGroupMessageInfo.getSenderId() == mMyUserId) {
                systemMessageContentView.setText(mContext.getString(R.string.chat_call_tip));
            } else {
                if (baseGroupMessageInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                    if (mPatientInfos.containsKey(baseGroupMessageInfo.getAppointmentId())) {
                        systemMessageContentView.setText(mContext.getString(R.string.chat_receiver_call, getRoleName(baseGroupMessageInfo.getRole()), mPatientInfos.get(baseGroupMessageInfo.getAppointmentId()).getPatientBaseInfo().getRealName()));
                    } else {
                        AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(baseGroupMessageInfo.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                            @Override
                            public void onSuccess(PatientInfo patientInfo) {
                                if (patientInfo != null) {
                                    mPatientInfos.put(baseGroupMessageInfo.getAppointmentId(), patientInfo);
                                    systemMessageContentView.setText(mContext.getString(R.string.chat_receiver_call, getRoleName(baseGroupMessageInfo.getRole()), patientInfo.getPatientBaseInfo().getRealName()));
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
                } else {
                    if (mDoctorInfos.containsKey(baseGroupMessageInfo.getSenderId())) {
                        systemMessageContentView.setText(mContext.getString(R.string.chat_receiver_call, getRoleName(baseGroupMessageInfo.getRole()), mDoctorInfos.get(baseGroupMessageInfo.getSenderId()).getRealName()));
                    } else {
                        mDoctorManager.getDoctorInfo(baseGroupMessageInfo.getSenderId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
                            @Override
                            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                                mDoctorInfos.put(doctorBaseInfo.getUserId(), doctorBaseInfo);
                                systemMessageContentView.setText(mContext.getString(R.string.chat_receiver_call, getRoleName(baseGroupMessageInfo.getRole()), doctorBaseInfo.getRealName()));
                            }

                            @Override
                            public void onFailed(int code, String msg) {

                            }

                            @Override
                            public void onFinish() {

                            }
                        });
                    }
                }
            }
        }

        private void displayGuideSetDateGroupMessage(GuideSetDateGroupMessageInfo baseGroupMessageInfo) {
            centerMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageContainerView.setVisibility(View.VISIBLE);
            systemMessageTitleView.setText(mContext.getString(R.string.chat_set_scheduling));
            systemMessageContentView.setText(mContext.getString(R.string.chat_guide_set_date, getVideoTime(baseGroupMessageInfo.getDate())));
        }
    }

    public interface OnPictureGroupMessageClickListener {
        void onPictureGroupMessageClicked(View v, PictureGroupMessageInfo item);
    }

    public interface OnPictureGroupMessageLongClickListener {
        void onPictureGroupMessageLongClicked(ImageView v, PictureGroupMessageInfo item, boolean type);
    }

    public interface OnSendFailGroupMessageClickListener {
        void onSendFailGroupMessageClicked(BaseGroupMessageInfo baseGroupMessageInfo, int position, ProgressBar progressBar, View view);
    }

    public interface OnAdmissionsGroupMessageClickListener {
        void onAdmissionsGroupMessageClicked(View view);
    }

    public interface OnRefuseAdmissionsGroupMessageClickListener {
        void onRefuseAdmissionsGroupMessageClicked(View view);
    }

    public interface OnIssueAdviseGroupMessageClickListener {
        void onIssueAdviseGroupMessageClicked(View view);
    }

    public interface OnMedicalAdviceGroupMessageClickListener {
        void onMedicalAdviceGroupMessageClicked(View view);
    }
}
