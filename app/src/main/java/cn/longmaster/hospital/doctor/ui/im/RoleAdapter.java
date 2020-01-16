package cn.longmaster.hospital.doctor.ui.im;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * Created by WangHaiKun on 2017/8/22.
 */
public class RoleAdapter extends BaseQuickAdapter<MemberListInfo, BaseViewHolder> {
    private Map<Integer, PatientInfo> mPatientInfos = new HashMap<>();
    private Map<Integer, DoctorBaseInfo> mDoctorInfos = new HashMap<>();

    public RoleAdapter(@LayoutRes int layoutResId, @Nullable List<MemberListInfo> memberListInfos) {
        super(layoutResId, memberListInfos);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberListInfo item) {
        LinearLayout mRecyclerViewItem = helper.getView(R.id.item_consult_role_ll);
        ImageView itemRoleImg = helper.getView(R.id.item_consult_role_avatar_civ);
        if (getData().get(0) == item) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRecyclerViewItem.getLayoutParams();
            lp.setMargins(0, 0, 40, 0);
            mRecyclerViewItem.setLayoutParams(lp);
        }
        if (item.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT || item.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT) {
            GlideUtils.showPatientChatAvatar(itemRoleImg, mContext, AvatarUtils.getAvatar(false, item.getUserId(), "0"));
        } else {
            GlideUtils.showDoctorChatAvatar(itemRoleImg, mContext, AvatarUtils.getAvatar(false, item.getUserId(), "0"));
        }

        int patientId = item.getUserId();
        if (item.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT) {
            int appointmentId = item.getAppointmentId();
            if (mPatientInfos.get(patientId) != null) {
                helper.setText(R.id.item_consult_role_name_tv, mPatientInfos.get(patientId).getPatientBaseInfo().getRealName());
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfos.put(patientId, patientInfo);
                            helper.setText(R.id.item_consult_role_name_tv, patientInfo.getPatientBaseInfo().getRealName());
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
            if (mDoctorInfos.containsKey(patientId)) {
                helper.setText(R.id.item_consult_role_name_tv, mDoctorInfos.get(patientId).getRealName());
            } else {
                AppApplication.getInstance().getManager(DoctorManager.class).getDoctor(patientId, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        if (null == doctorBaseInfo) {
                            return;
                        }
                        mDoctorInfos.put(patientId, doctorBaseInfo);
                        helper.setText(R.id.item_consult_role_name_tv, doctorBaseInfo.getRealName());
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
