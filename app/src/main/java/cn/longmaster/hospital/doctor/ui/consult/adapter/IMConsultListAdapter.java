package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.CardGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupList;
import cn.longmaster.hospital.doctor.core.entity.im.PictureGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.TextGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.VoiceGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.ConsultUtil;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 待办事项列表adapter （新）
 * Created by Yang² on 2017/8/16.
 */

public class IMConsultListAdapter extends SimpleBaseAdapter<ChatGroupInfo, IMConsultListAdapter.ViewHolder> {
    private Context mContext;
    private Map<Integer, PatientInfo> mPatientMap;
    private Map<Integer, DoctorBaseInfo> mDoctorMap;
    private Map<Integer, BaseGroupMessageInfo> mLastGroupMessageMap;//最后一条消息
    private Map<Integer, Boolean> mRedPointMap;//是否显示红点
    private boolean mLoadMoreEnable;

    public IMConsultListAdapter(Context context) {
        super(context);
        mContext = context;
        mLoadMoreEnable = false;
        mPatientMap = new HashMap<>();
        mDoctorMap = new HashMap<>();
        mLastGroupMessageMap = new HashMap<>();
        mRedPointMap = new HashMap<>();
    }

    public void setGroupList(ChatGroupList chatGroupList) {
        mLoadMoreEnable = chatGroupList.getIsFinish() == 1;
        setData(chatGroupList.getChatGroupList());
    }

    public void addGroupList(ChatGroupList chatGroupList) {
        mLoadMoreEnable = chatGroupList.getIsFinish() == 1;
        addData(chatGroupList.getChatGroupList());
    }

    public PatientBaseInfo getPatientBaseInfo(int groupId) {
        if (mPatientMap.get(groupId) != null && mPatientMap.get(groupId).getPatientBaseInfo() != null) {
            return mPatientMap.get(groupId).getPatientBaseInfo();
        }
        return null;
    }

    public void setLastGroupMessageMap(int groupId, BaseGroupMessageInfo baseGroupMessageInfo) {
        mLastGroupMessageMap.put(groupId, baseGroupMessageInfo);
    }

    public void removeGroupMessageMap(int groupId) {
        mLastGroupMessageMap.remove(groupId);
    }

    public void setRedPointMap(int groupId, boolean show) {
        mRedPointMap.put(groupId, show);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_im_consult;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ChatGroupInfo chatGroupInfo, int position) {
        viewHolder.display(chatGroupInfo, position);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_im_consult_avatar_civ)
        private CircleImageView mAvatarAiv;
        @FindViewById(R.id.item_im_consult_patient_name_tv)
        private TextView mPatientNameTv;
        @FindViewById(R.id.item_im_consult_disease_name_tv)
        private TextView mDiseaseNameTv;
        @FindViewById(R.id.item_im_consult_status_tv)
        private Button mStatusTv;
        @FindViewById(R.id.item_im_consult_role_name_tv)
        private TextView mRoleNameTv;
        @FindViewById(R.id.item_im_consult_last_message_tv)
        private TextView mLastMessageTv;
        @FindViewById(R.id.item_im_consult_time_tv)
        private TextView mTimeTv;
        @FindViewById(R.id.item_im_consult_red_point_iv)
        private ImageView mRedPointIv;
        @FindViewById(R.id.item_im_consult_space_view)
        private View mSpaceView;

        private void display(ChatGroupInfo chatGroupInfo, int position) {
            mSpaceView.setVisibility(!mLoadMoreEnable && position == getCount() - 1 ? View.VISIBLE : View.GONE);
            setViewTag(chatGroupInfo.getAppointmentId());
            setView(chatGroupInfo);
            setPatientInfo(chatGroupInfo.getGroupId(), chatGroupInfo.getAppointmentId(), null);
            setMessageView(chatGroupInfo);
        }

        private void setViewTag(int appointmentId) {
            mPatientNameTv.setTag(appointmentId);
            mDiseaseNameTv.setTag(appointmentId);
            mRoleNameTv.setTag(appointmentId);
            mLastMessageTv.setTag(appointmentId);
            mTimeTv.setTag(appointmentId);
            mRedPointIv.setTag(appointmentId);
            mPatientNameTv.setText("");
            mDiseaseNameTv.setText("");
            mRoleNameTv.setText("");
            mLastMessageTv.setText("");
            mTimeTv.setText("");
            mRedPointIv.setVisibility(View.GONE);
        }

        private void setView(ChatGroupInfo chatGroupInfo) {
            mStatusTv.setText(ConsultUtil.getIMGroupStatus(mContext, chatGroupInfo.getStatus()));
            setRedPoint(chatGroupInfo);
        }

        private void setRedPoint(final ChatGroupInfo chatGroupInfo) {
            if (mRedPointMap.containsKey(chatGroupInfo.getGroupId())) {
                mRedPointIv.setVisibility(mRedPointMap.get(chatGroupInfo.getGroupId()) ? View.VISIBLE : View.GONE);
            } else {
                AppApplication.getInstance().getManager(GroupMessageManager.class).getUnreadGroupMessageCount(chatGroupInfo.getGroupId(), new GroupMessageManager.GetUnreadCountCallback() {
                    @Override
                    public void onGetUnreadCountStateChanged(long count) {
                        mRedPointMap.put(chatGroupInfo.getGroupId(), count > 0);
                        mRedPointIv.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                    }
                });
            }
        }

        /**
         * 显示患者相关信息
         *
         * @param groupId
         * @param appointmentId
         * @param roleTextView
         */
        private void setPatientInfo(int groupId, int appointmentId, TextView roleTextView) {
            displayPatientInfo(mPatientMap.get(groupId), roleTextView);
            getPatientInfo(groupId, appointmentId, roleTextView);
        }

        /**
         * 获取患者信息并显示
         *
         * @param groupId
         * @param appointmentId
         * @param roleTextView
         */
        private void getPatientInfo(final int groupId, int appointmentId, final TextView roleTextView) {
            AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                @Override
                public void onSuccess(PatientInfo patientInfo) {
                    if (patientInfo != null) {
                        mPatientMap.put(groupId, patientInfo);
                        displayPatientInfo(patientInfo, roleTextView);
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
         * 设置患者姓名和疾病名称
         * 所有患者信息相关的 在此设置
         *
         * @param patientInfo
         * @param roleTextView
         */
        private void displayPatientInfo(PatientInfo patientInfo, TextView roleTextView) {
            if (patientInfo != null && (int) mPatientNameTv.getTag() == patientInfo.getPatientBaseInfo().getAppointmentId()) {
                if (roleTextView != null) {
                    roleTextView.setText(mContext.getString(R.string.im_group_role_name, getCutRealName(patientInfo.getPatientBaseInfo().getRealName())));
                    return;
                }
                mPatientNameTv.setText(mContext.getString(R.string.im_consult_patient_name,
                        getCutRealName(patientInfo.getPatientBaseInfo().getRealName()), patientInfo.getPatientBaseInfo().getAppointmentId()));
                mDiseaseNameTv.setText(patientInfo.getPatientBaseInfo().getFirstCureResult());
            }
        }

        /**
         * 设置最后一条消息和角色名
         *
         * @param chatGroupInfo
         */
        private void setMessageView(ChatGroupInfo chatGroupInfo) {
            if (mLastGroupMessageMap.containsKey(chatGroupInfo.getGroupId())) {
                displayLastMessage(mLastGroupMessageMap.get(chatGroupInfo.getGroupId()));
            } else {
                getLastMessage(chatGroupInfo);
            }

        }

        private void getLastMessage(final ChatGroupInfo chatGroupInfo) {
            AppApplication.getInstance().getManager(GroupMessageManager.class).getLatestGroupMessage(chatGroupInfo.getGroupId(), new GroupMessageManager.GetLatestGroupMessageCallback() {
                @Override
                public void onGetLatestGroupMessageStateChanged(BaseGroupMessageInfo baseGroupMessageInfo) {
                    mLastGroupMessageMap.put(chatGroupInfo.getGroupId(), baseGroupMessageInfo);
                    displayLastMessage(baseGroupMessageInfo);
                }
            });
        }

        private void displayLastMessage(BaseGroupMessageInfo baseGroupMessageInfo) {
            Logger.logI(Logger.APPOINTMENT, "BaseGroupMessageInfo-->" + baseGroupMessageInfo);
            if (baseGroupMessageInfo != null && (int) mPatientNameTv.getTag() == baseGroupMessageInfo.getAppointmentId()) {
                setDateTime(baseGroupMessageInfo.getSendDt());
                switch (baseGroupMessageInfo.getMsgType()) {
                    case BaseGroupMessageInfo.PICTURE_MSG:
                        PictureGroupMessageInfo pictureGroupMessageInfo = (PictureGroupMessageInfo) baseGroupMessageInfo;
                        mLastMessageTv.setText(mContext.getString(R.string.im_group_pic));
                        setRoleName(pictureGroupMessageInfo.getRole(), baseGroupMessageInfo.getGroupId(), baseGroupMessageInfo.getAppointmentId(), pictureGroupMessageInfo.getSenderId());
                        break;

                    case BaseGroupMessageInfo.TEXT_MSG:
                        TextGroupMessageInfo textGroupMessageInfo = (TextGroupMessageInfo) baseGroupMessageInfo;
                        mLastMessageTv.setText(textGroupMessageInfo.getContent());
                        setRoleName(textGroupMessageInfo.getRole(), baseGroupMessageInfo.getGroupId(), baseGroupMessageInfo.getAppointmentId(), textGroupMessageInfo.getSenderId());
                        break;

                    case BaseGroupMessageInfo.VOICE_MSG:
                        VoiceGroupMessageInfo voiceGroupMessageInfo = (VoiceGroupMessageInfo) baseGroupMessageInfo;
                        mLastMessageTv.setText(mContext.getString(R.string.im_group_voice));
                        setRoleName(voiceGroupMessageInfo.getRole(), baseGroupMessageInfo.getGroupId(), baseGroupMessageInfo.getAppointmentId(), voiceGroupMessageInfo.getSenderId());
                        break;

                    case BaseGroupMessageInfo.CARD_MSG:
                        CardGroupMessageInfo cardGroupMessageInfo = (CardGroupMessageInfo) baseGroupMessageInfo;
                        mLastMessageTv.setText(mContext.getString(R.string.im_group_new_situation));
                        setRoleName(cardGroupMessageInfo.getRole(), baseGroupMessageInfo.getGroupId(), baseGroupMessageInfo.getAppointmentId(), cardGroupMessageInfo.getSenderId());
                        break;

                    default:
                        mRoleNameTv.setText("");
                        mLastMessageTv.setText("");
                        break;

                }
            }
        }

        private void setDateTime(long dateTime) {
            if (DateUtil.isSameDay(dateTime * 1000, System.currentTimeMillis())) {
                mTimeTv.setText(DateUtil.getTime(new Date(dateTime * 1000), "HH:mm"));
            } else {
                mTimeTv.setText(DateUtil.getTime(new Date(dateTime * 1000), "yyyy/MM/dd"));
            }
        }

        /**
         * 设置角色名
         *
         * @param role
         * @param groupId
         * @param appointmentId
         * @param senderId
         */
        private void setRoleName(int role, int groupId, int appointmentId, int senderId) {
            if (role == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
                setPatientInfo(groupId, appointmentId, mRoleNameTv);
            } else {
                setDoctorInfo(appointmentId, senderId);
            }
        }

        /**
         * 设置医生
         *
         * @param appointmentId
         * @param doctorId
         */
        private void setDoctorInfo(int appointmentId, int doctorId) {
            if (!mDoctorMap.containsKey(doctorId)) {
                getDoctorInfo(appointmentId, doctorId);
            } else {
                displayDoctorInfo(appointmentId, mDoctorMap.get(doctorId));
            }
        }

        /**
         * 获取医生
         *
         * @param appointmentId
         * @param doctorId
         */
        private void getDoctorInfo(final int appointmentId, final int doctorId) {
            AppApplication.getInstance().getManager(DoctorManager.class).getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
                @Override
                public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                    mDoctorMap.put(doctorId, doctorBaseInfo);
                    displayDoctorInfo(appointmentId, doctorBaseInfo);
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
         * 设置医生角色名
         *
         * @param appointmentId
         * @param doctorBaseInfo
         */
        private void displayDoctorInfo(int appointmentId, DoctorBaseInfo doctorBaseInfo) {
            if (doctorBaseInfo != null && (int) mRoleNameTv.getTag() == appointmentId) {
                mRoleNameTv.setText(mContext.getString(R.string.im_group_role_name, doctorBaseInfo.getRealName()));
            }
        }

        /**
         * 名字最长显示4个字，超过后显示前四个字 + ...
         *
         * @param name
         * @return
         */
        private String getCutRealName(String name) {
            return name.length() > 4 ? name.substring(0, 4) + "..." : name;
        }

    }
}
