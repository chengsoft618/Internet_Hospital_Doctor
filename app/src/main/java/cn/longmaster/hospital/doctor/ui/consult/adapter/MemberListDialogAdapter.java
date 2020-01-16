package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.video.VideoRoomMember;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 直播中成员列表适配器
 * Created by W·H·K on 2018/3/9.
 * Mod by biao on 2019/11/27
 */

public class MemberListDialogAdapter extends BaseQuickAdapter<VideoRoomMember, BaseViewHolder> {
    private int mAppointmentId;

    public MemberListDialogAdapter(int layoutResId, @Nullable List<VideoRoomMember> data, int appointmentId) {
        super(layoutResId, data);
        mAppointmentId = appointmentId;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, VideoRoomMember item) {
        CircleImageView doctorAvatar = helper.getView(R.id.item_live_video_img);
        GlideUtils.showDoctorAvatar(doctorAvatar, mContext, AvatarUtils.getAvatar(false, item.getUserId(), "0"));
        helper.setText(R.id.item_live_video_user_type_tv, getDoctorType(item));
        if (item.getUserType() == AppConstant.UserType.USER_TYPE_PATIENT) {
            getPatientInfo(helper);
        } else {
            getDoctorInfo(helper, item);
        }
    }

    public void getPatientInfo(BaseViewHolder helper) {
        AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(mAppointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    helper.setText(R.id.item_live_video_name_tv, patientInfo.getPatientBaseInfo().getRealName());
                } else {
                    helper.setText(R.id.item_live_video_name_tv, "");
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                helper.setText(R.id.item_live_video_name_tv, "");
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private String getDoctorType(VideoRoomMember videoRoomMember) {
        switch (videoRoomMember.getUserType()) {
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT:
                return mContext.getString(R.string.chat_role_patient);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR:
                return mContext.getString(R.string.chat_role_attending_doctor);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ADMINISTRATOR:
                return mContext.getString(R.string.chat_role_administrator);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR:
                return mContext.getString(R.string.chat_role_superior_doctor);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ASSISTANT_DOCTOR:
                return mContext.getString(R.string.chat_role_assistant_doctor);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_CONFERENCE_STAFF:
                return mContext.getString(R.string.chat_role_conference_staff);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_DOCTOR:
                return mContext.getString(R.string.chat_role_mdt_doctor);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT:
                return mContext.getString(R.string.chat_role_mdt_patient);
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_SYSTEM_AUTO:
                return mContext.getString(R.string.chat_role_system_auto);
            default:
                return "";
        }
    }

    private void getDoctorInfo(BaseViewHolder helper, VideoRoomMember videoRoomMember) {
        AppApplication.getInstance().getManager(DoctorManager.class).getDoctorInfo(videoRoomMember.getUserId(), true, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null == doctorBaseInfo) {
                    helper.setText(R.id.item_live_video_name_tv, "");
                    return;
                }
                helper.setText(R.id.item_live_video_name_tv, doctorBaseInfo.getRealName());
            }

            @Override
            public void onFailed(int code, String msg) {
                helper.setText(R.id.item_live_video_name_tv, "");
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
