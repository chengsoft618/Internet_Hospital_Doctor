package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDoctorSectionInfo;
import cn.longmaster.hospital.doctor.core.manager.common.DcpManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 值班门诊-选择医生列表适配器
 * Created by yangyong on 2019-11-27.
 */
public class DCRoomCallDoctorListAdapter extends BaseSectionQuickAdapter<DCDoctorSectionInfo, BaseViewHolder> {
    private DcpManager dcpManager;
    private int currentUserID;
    private final int ONLINE_STATE_OUT = 2;
    private final int ONLINE_STATE_ON = 1;
    public DCRoomCallDoctorListAdapter(int layoutResId, int sectionHeadResId, List<DCDoctorSectionInfo> data) {
        super(layoutResId, sectionHeadResId, data);
        dcpManager = AppApplication.getInstance().getManager(DcpManager.class);
        currentUserID = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
    }

    @Override
    protected void convertHead(BaseViewHolder helper, DCDoctorSectionInfo item) {
        int position = getData().indexOf(item);
        if (position == 0) {
            helper.setVisible(R.id.layout_dcroom_call_doctor_list_header_top_view, false);
        }
        helper.setText(R.id.layout_dcroom_call_doctor_list_header_title_tv, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, DCDoctorSectionInfo item) {
        helper.setText(R.id.item_dcroom_call_doctor_list_doctor_name_tv, item.getRealName());
        helper.setText(R.id.item_dcroom_call_doctor_list_doctor_title_tv, item.getDoctorLevel());
        helper.setText(R.id.item_dcroom_call_doctor_list_doctor_hospital_department_tv, item.getHospitalName() + "    " + item.getDepartmentName());
        GlideUtils.showDoctorAvatar(helper.getView(R.id.item_dcroom_call_doctor_list_civ), AppApplication.getInstance().getApplicationContext(), AvatarUtils.getAvatar(false, item.getUserId(), "0"));
        helper.setImageResource(R.id.item_dcroom_call_doctor_list_check_iv, item.isChecked() ? R.mipmap.ic_dcroom_call_choice_doctor_checked : R.mipmap.ic_dcroom_call_choice_doctor_uncheck);
        helper.setOnClickListener(R.id.item_dcroom_call_doctor_list_check_iv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChecked(!item.isChecked());
                notifyDataSetChanged();
            }
        });
        AppExecutors.getInstance().networkIO().execute(() -> dcpManager.getUserOnLineState(currentUserID, item.getUserId(), (state, userId) -> AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                item.setOnlineState(state);
                helper.setText(R.id.item_dcdcotor_list_doctor_online_state_tv, getStateTv(state));
                helper.setBackgroundRes(R.id.item_dcdcotor_list_doctor_online_state_v, getStateVBg(state));
                helper.setTextColor(R.id.item_dcdcotor_list_doctor_online_state_tv, ContextCompat.getColor(mContext, getStateTvColor(state)));
            }
        })));
    }
    private int getStateVBg(int state) {
        if (ONLINE_STATE_OUT == state) {
            return R.drawable.bg_solid_ff8301_radius_45;
        } else if (ONLINE_STATE_ON == state) {
            return R.drawable.bg_solid_1ada48_radius_45;
        }
        return R.drawable.bg_solid_9a9a9a_radius_45;
    }

    private int getStateTvColor(int state) {
        if (ONLINE_STATE_OUT == state) {
            return R.color.color_ff8301;
        } else if (ONLINE_STATE_ON == state) {
            return R.color.color_1ada48;
        }
        return R.color.color_9a9a9a;
    }

    private String getStateTv(int state) {
        if (ONLINE_STATE_OUT == state) {
            return "离开";
        } else if (ONLINE_STATE_ON == state) {
            return "在线";
        }
        return "离线";
    }
}
