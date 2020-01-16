package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupInfo;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 待办事项列表adapter （新）
 * Created by Yang² on 2017/8/16.
 * Mod by biao on 2019/11/06
 */

public class IMConsultListNewAdapter extends BaseQuickAdapter<ChatGroupInfo, BaseViewHolder> {

    //最后一条消息
    private SparseArray<BaseGroupMessageInfo> mLastGroupMessageMap = new SparseArray<>();
    //是否显示红点
    private SparseBooleanArray mRedPointMap = new SparseBooleanArray();
    private GroupMessageManager groupMessageManager;
    private int currentUserId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();

    public IMConsultListNewAdapter(int layoutResId, @Nullable List<ChatGroupInfo> data) {
        super(layoutResId, data);
        groupMessageManager = AppApplication.getInstance().getManager(GroupMessageManager.class);
    }

    public void setRedPointMap(int groupId, boolean show) {
        mRedPointMap.put(groupId, show);
        notifyDataSetChanged();
    }

    public void updateGroupMessage(BaseGroupMessageInfo baseGroupMessageInfo, boolean isSendMsg) {
        List<ChatGroupInfo> chatGroupInfos = getData();
        for (int i = 0; i < LibCollections.size(chatGroupInfos); i++) {
            ChatGroupInfo chatGroupInfo = chatGroupInfos.get(i);
            if (chatGroupInfo.getGroupId() == baseGroupMessageInfo.getGroupId()) {
                chatGroupInfos.remove(chatGroupInfo);
                if (isSendMsg) {
                    chatGroupInfos.add(0, chatGroupInfo);
                    mLastGroupMessageMap.put(baseGroupMessageInfo.getGroupId(), baseGroupMessageInfo);
                } else {
                    if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_EXIT_MSG) {
                        //被移出，只有自己被移出才会走这里
                        mLastGroupMessageMap.remove(chatGroupInfo.getGroupId());
                    } else {
                        chatGroupInfos.add(0, chatGroupInfo);
                        mLastGroupMessageMap.put(chatGroupInfo.getGroupId(), baseGroupMessageInfo);
                        if (groupMessageManager.getCurentImId() != baseGroupMessageInfo.getGroupId()) {
                            setRedPointMap(baseGroupMessageInfo.getGroupId(), true);
                        }
                    }
                }

            }
        }
        setNewData(chatGroupInfos);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatGroupInfo item) {
//        if (currentUserId != item.getSuperDoctorId() && currentUserId != item.getAttendDoctorId()) {
//            helper.setVisible(R.id.item_im_consult_is_same_dep, true);
//        } else {
//            helper.setGone(R.id.item_im_consult_is_same_dep, false);
//        }
        displayState(helper, item);
        helper.setText(R.id.item_im_consult_order_num_desc_tv, item.getAppointmentId() + "");
        helper.setText(R.id.item_im_consult_patient_name_desc_tv, item.getPatientName());
        displayTime(helper, item);
        //分诊
        if (item.getSuperDoctorId() == 0) {
            helper.setGone(R.id.item_im_consult_doctor_info_rl, false);
            helper.setVisible(R.id.item_im_consult_no_doctor_info_tv, true);
            CircleImageView avatar = helper.getView(R.id.item_im_consult_doctor_civ);
            GlideUtils.showHospitalLogo(avatar, mContext, item.getAttendHospitalLogo());
        } else {
            helper.setGone(R.id.item_im_consult_no_doctor_info_tv, false);
            helper.setVisible(R.id.item_im_consult_doctor_info_rl, true);
            displayDoctorInfo(helper, item);
        }
        displayBottomHospital(helper, item);
        if (mRedPointMap.get(item.getGroupId())) {
            helper.setVisible(R.id.item_im_consult_red_point_v, true);
        } else {
            AppApplication.getInstance().getManager(GroupMessageManager.class).getUnreadGroupMessageCount(item.getGroupId(), count -> {
                mRedPointMap.put(item.getGroupId(), count > 0);
                if (count > 0) {
                    helper.setVisible(R.id.item_im_consult_red_point_v, true);
                } else {
                    helper.setGone(R.id.item_im_consult_red_point_v, false);
                }
            });
        }

    }

    private void displayState(BaseViewHolder helper, ChatGroupInfo item) {
        int stateStr;
        int stateColor = R.color.color_fc8404;
        int stateBg = R.drawable.bg_solid_ffffff_stroke_fc8404_radius_2;
        switch (item.getStatus()) {
            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_ORIGINAL:
                stateStr = R.string.im_group_status_waiting;
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_RECEPTION:
                stateStr = R.string.im_group_status_consulting;
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_REFUSE:
                stateStr = R.string.im_group_status_refused;
                stateColor = R.color.color_666666;
                stateBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_CLOSE:
                stateStr = R.string.im_group_status_complete;
                stateColor = R.color.color_666666;
                stateBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_FOLLOW_UP:
                stateStr = R.string.im_group_status_following;
                break;
            default:
                stateStr = R.string.im_group_status_waiting;
                break;
        }
        helper.setText(R.id.item_im_consult_order_state_tv, stateStr);
        helper.setTextColor(R.id.item_im_consult_order_state_tv, ContextCompat.getColor(mContext, stateColor));
        helper.setBackgroundRes(R.id.item_im_consult_order_state_tv, stateBg);
    }

    private void displayTime(BaseViewHolder helper, ChatGroupInfo item) {
        if (item != null) {
            int state = item.getAppointmentStat();
            int reason = item.getStatReason();
            if (AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION == state && 0 == reason) {
                helper.setText(R.id.item_im_consult_time_tv, R.string.time_description_predict);
                if (StringUtils.isEmpty(item.getPredictCureDt()) || item.getPredictCureDt().contains("2099")) {
                    helper.setText(R.id.item_im_consult_time_desc_tv, R.string.time_to_be_determined);
                } else {
                    helper.setText(R.id.item_im_consult_time_desc_tv, getFriendlyTimeSpanByNow(item.getPredictCureDt().trim()));
                }
            } else {
                if (AppConstant.AppointmentState.APPOINTMENT_FINISHED == state) {
                    helper.setText(R.id.item_im_consult_time_tv, R.string.time_description_finished);
                } else {
                    helper.setText(R.id.item_im_consult_time_tv, R.string.time_description_application);
                }
                if (StringUtils.isEmpty(item.getInsertDt()) || item.getInsertDt().contains("2099")) {
                    helper.setText(R.id.item_im_consult_time_desc_tv, R.string.time_to_be_determined);
                } else {
                    helper.setText(R.id.item_im_consult_time_desc_tv, getFriendlyTimeSpanByNow(item.getInsertDt().trim()));
                }
            }
        }
    }

    /**
     * 设置医生角色名
     *
     * @param helper
     * @param item
     */
    private void displayDoctorInfo(BaseViewHolder helper, ChatGroupInfo item) {
        CircleImageView avatar = helper.getView(R.id.item_im_consult_doctor_civ);
        helper.setGone(R.id.item_im_consult_is_worse, item.isWorseCase());
        if (currentUserId == item.getSuperDoctorId()) {
            GlideUtils.showDoctorAvatar(avatar, mContext, item.getAttendHospitalLogo());
            helper.setText(R.id.item_im_consult_doctor_name_tv, item.getAttendHospitalName());
            helper.setGone(R.id.item_im_consult_doctor_level_tv, false);
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getAttendDepartmentName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getAttendDoctorName());
            helper.setText(R.id.item_im_consult_doctor_hospital_info_tv, doctorInfo);
        } else {
            //基层医生
            GlideUtils.showDoctorAvatar(avatar, mContext, AvatarUtils.getAvatar(false, item.getSuperDoctorId(), "0"));
            helper.setText(R.id.item_im_consult_doctor_name_tv, item.getSuperDoctorName());
            helper.setVisible(R.id.item_im_consult_doctor_level_tv, true);
            helper.setText(R.id.item_im_consult_doctor_level_tv, item.getSuperDoctorLevel());
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getSuperHospitalName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getSuperDepartmentName());
            helper.setText(R.id.item_im_consult_doctor_hospital_info_tv, item.getSuperHospitalName() + item.getSuperDepartmentName());
        }
    }

    private void displayBottomHospital(BaseViewHolder helper, ChatGroupInfo item) {
        if (currentUserId == item.getSuperDoctorId()) {
            helper.setText(R.id.item_im_consult_hospital_type_tv, "接收医院");
            helper.setTextColor(R.id.item_im_consult_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_049eff));
            helper.setBackgroundRes(R.id.item_im_consult_hospital_type_tv, R.drawable.bg_solid_d5efff_radius_2);
            helper.setText(R.id.item_im_consult_hospital_tv, item.getSuperHospitalName());
            //上级专家
        } else {
            helper.setText(R.id.item_im_consult_hospital_type_tv, "发起医院");
            helper.setTextColor(R.id.item_im_consult_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_fc8404));
            helper.setBackgroundRes(R.id.item_im_consult_hospital_type_tv, R.drawable.bg_solid_fff0e0_radius_2);
            helper.setText(R.id.item_im_consult_hospital_tv, item.getAttendHospitalName());
        }
    }

    private String getFriendlyTimeSpanByNow(String time) {
        long now = System.currentTimeMillis();
        long millis = TimeUtils.string2Millis(time);
        long span = now - millis;
        if (span < 0) {
            return TimeUtils.string2String(time, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        }
        return TimeUtils.getFriendlyTimeSpanByNow(time);
    }
}
