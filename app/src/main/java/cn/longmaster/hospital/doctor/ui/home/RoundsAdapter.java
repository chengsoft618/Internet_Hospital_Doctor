package cn.longmaster.hospital.doctor.ui.home;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListItemInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by W·H·K on 2018/5/10.
 * mod by biao on 2019/10/22
 */
public class RoundsAdapter extends BaseQuickAdapter<OrderListItemInfo, BaseViewHolder> {
    private int currentDoctorId;

    public RoundsAdapter(int layoutResId, @Nullable List<OrderListItemInfo> data) {
        super(layoutResId, data);
        currentDoctorId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListItemInfo item) {
        if (currentDoctorId != item.getDoctorId() && currentDoctorId != item.getLaunchDoctorId()) {
            helper.setVisible(R.id.item_home_rounds_is_same_dep, true);
        } else {
            helper.setGone(R.id.item_home_rounds_is_same_dep, false);
        }
        displayOrderState(helper, item);
        displayBottomHospital(helper, item);
        if (0 == item.getDoctorId()) {
            helper.setVisible(R.id.item_home_rounds_no_doctor_info_tv, true);
            helper.setGone(R.id.item_home_rounds_doctor_info_rl, false);
            CircleImageView avatar = helper.getView(R.id.item_home_rounds_doctor_civ);
            GlideUtils.showHospitalLogo(avatar, mContext, item.getAtthosLogo());
        } else {
            helper.setGone(R.id.item_home_rounds_no_doctor_info_tv, false);
            helper.setVisible(R.id.item_home_rounds_doctor_info_rl, true);
            displayDoctorInfo(helper, item);
        }
        helper.setText(R.id.item_home_rounds_time_desc_tv, getFriendlyTimeSpanByNow(item.getInsertDt()));
        helper.setText(R.id.item_home_rounds_order_num_tv, "就诊编号:" + item.getOrderId());
        helper.setText(R.id.item_home_rounds_theme_desc_tv, getOrderTitle(item));
    }

    private void displayBottomHospital(BaseViewHolder helper, OrderListItemInfo item) {
        if (currentDoctorId == item.getDoctorId()) {
            helper.setText(R.id.item_home_rounds_hospital_type_tv, "接收医院");
            helper.setTextColor(R.id.item_home_rounds_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_049eff));
            helper.setBackgroundRes(R.id.item_home_rounds_hospital_type_tv, R.drawable.bg_solid_d5efff_radius_2);
            helper.setText(R.id.item_home_rounds_hospital_tv, item.getSuperHospitalName());
        } else {
            helper.setText(R.id.item_home_rounds_hospital_type_tv, "发起医院");
            helper.setTextColor(R.id.item_home_rounds_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_fc8404));
            helper.setBackgroundRes(R.id.item_home_rounds_hospital_type_tv, R.drawable.bg_solid_fff0e0_radius_2);
            helper.setText(R.id.item_home_rounds_hospital_tv, item.getAtthosName());
        }
    }

    private void displayDoctorInfo(BaseViewHolder helper, OrderListItemInfo item) {
        CircleImageView avatar = helper.getView(R.id.item_home_rounds_doctor_civ);
        if (currentDoctorId == item.getDoctorId()) {
            //上级医生
            helper.setGone(R.id.item_home_rounds_doctor_level_tv, false);
            GlideUtils.showHospitalLogo(avatar, mContext, item.getAtthosLogo());
            helper.setText(R.id.item_home_rounds_doctor_name_tv, item.getAtthosName());
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getDepartmentName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getLaunchDoctorName());
            helper.setText(R.id.item_home_rounds_doctor_hospital_info_tv, doctorInfo);
        } else {
            //首诊医生
            if (0 == item.getDoctorId()) {
                GlideUtils.showHospitalLogo(avatar, mContext, item.getAtthosLogo());
            } else {
                GlideUtils.showDoctorAvatar(avatar, mContext, AvatarUtils.getAvatar(false, item.getDoctorId(), "0"));
            }
            helper.setText(R.id.item_home_rounds_doctor_name_tv, item.getSuperDoctorName());
            helper.setVisible(R.id.item_home_rounds_doctor_level_tv, true);
            helper.setText(R.id.item_home_rounds_doctor_level_tv, item.getSuperDoctorLevel());
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getSuperHospitalName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getSuperDepartmentName());
            helper.setText(R.id.item_home_rounds_doctor_hospital_info_tv, doctorInfo);
        }
    }

    private void displayOrderState(BaseViewHolder helper, OrderListItemInfo item) {
        int orderState = R.string.rounds_waiting_diagnosis;
        int orderColor = R.color.color_fc8404;
        int orderBg = R.drawable.bg_solid_f8f8f8_stroke_fc8404_radius_2;
        switch (item.getOrderState()) {
            case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                orderState = R.string.rounds_waiting_diagnosis;
                break;
            case AppConstant.AppointmentState.DATA_CHECK_FAIL:
                orderState = R.string.rounds_wait_receive;
                break;
            case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                orderState = R.string.wait_for_consult;
                break;
            case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED:
                orderState = R.string.appoint_state_complete;
                orderBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                orderColor = R.color.color_666666;
                break;
            case AppConstant.AppointmentState.APPOINTMENT_FINISHED:
                if (item.getStateReason() == 2) {
                    orderState = R.string.appoint_state_close;
                } else if (item.getStateReason() == 3) {
                    orderState = R.string.appoint_state_all_complete;
                }
                orderBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                orderColor = R.color.color_666666;
                break;
            default:
                break;
        }
        helper.setBackgroundRes(R.id.item_home_rounds_order_state_tv, orderBg);
        helper.setTextColor(R.id.item_home_rounds_order_state_tv, ContextCompat.getColor(mContext, orderColor));
        helper.setText(R.id.item_home_rounds_order_state_tv, orderState);
    }

    private String getOrderTitle(OrderListItemInfo item) {
        //是否需要PPT
        //患者数量
        int patientCount = item.getOrderMedicalCount();
        if (item.getIsNeedPpt() == 1) {
            if (patientCount <= 0) {
                return StringUtils.isEmpty(item.getOrderTitle()) ? "" : mContext.getString(R.string.rounds_teaching, item.getOrderTitle());
            } else {
                return mContext.getString(R.string.rounds_theme_add_num, StringUtils.isEmpty(item.getOrderTitle()) ? "" : item.getOrderTitle(), patientCount + "");
            }
        } else {
            if (patientCount <= 0) {
                return StringUtils.isEmpty(item.getOrderTitle()) ? "" : item.getOrderTitle();
            } else {
                return mContext.getString(R.string.rounds_theme_add_num_t, patientCount + "");
            }
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
