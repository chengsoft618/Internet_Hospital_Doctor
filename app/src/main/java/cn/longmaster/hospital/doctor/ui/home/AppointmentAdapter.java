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
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentItemInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的患者界面 预约适配器
 * Created by JinKe on 2016-08-02.
 */
public class AppointmentAdapter extends BaseQuickAdapter<AppointmentItemInfo, BaseViewHolder> {
    private int currentUserId = AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId();

    public AppointmentAdapter(int layoutResId, @Nullable List<AppointmentItemInfo> data) {
        super(layoutResId, data);
    }

    public void removeLocalAppointment(int appointmentId) {
        for (int i = 0; i < getItemCount(); i++) {
            if (appointmentId == getData().get(i).getAppointmentId()) {
                getData().remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, AppointmentItemInfo item) {
        helper.setGone(R.id.item_home_consult_is_worse, item.isWorseCase());
        if (currentUserId != item.getDoctorUserId() && currentUserId != item.getAttendingDoctorUserId()) {
            helper.setVisible(R.id.item_home_consult_is_same_dep, true);
        } else {
            helper.setGone(R.id.item_home_consult_is_same_dep, false);
        }
        //公共
        helper.setText(R.id.item_home_consult_order_num_desc_tv, item.getAppointmentId() + "");
        displayAppointmentState(helper, item);
        displayTime(helper, item);
        helper.setText(R.id.item_home_consult_patient_name_desc_tv, item.getPatientName());
        //分诊
        if (item.getDoctorUserId() == 0) {
            helper.setGone(R.id.item_home_consult_doctor_info_rl, false);
            helper.setVisible(R.id.item_home_consult_no_doctor_info_tv, true);
            CircleImageView avatar = helper.getView(R.id.item_home_consult_doctor_civ);
            GlideUtils.showHospitalLogo(avatar, mContext, item.getAttendHospitaLogo());
        } else {
            helper.setGone(R.id.item_home_consult_no_doctor_info_tv, false);
            helper.setVisible(R.id.item_home_consult_doctor_info_rl, true);
            displayDoctorInfo(helper, item);
        }
        displayBottomHospital(helper, item);
    }

    private void displayTime(BaseViewHolder helper, AppointmentItemInfo item) {
        if (item != null) {
            int state = item.getAppointmentStat();
            int reason = item.getStatReason();
            if (AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION == state && 0 == reason) {
                helper.setText(R.id.item_home_consult_time_tv, R.string.time_description_predict);
                if (StringUtils.isEmpty(item.getPredictCureDt()) || item.getPredictCureDt().contains("2099")) {
                    helper.setText(R.id.item_home_consult_time_desc_tv, R.string.time_to_be_determined);
                } else {
                    helper.setText(R.id.item_home_consult_time_desc_tv, getFriendlyTimeSpanByNow(item.getPredictCureDt()));
                }
            } else {
                if (AppConstant.AppointmentState.APPOINTMENT_FINISHED == state) {
                    helper.setText(R.id.item_home_consult_time_tv, R.string.time_description_finished);
                } else {
                    helper.setText(R.id.item_home_consult_time_tv, R.string.time_description_application);
                }
                if (StringUtils.isEmpty(item.getInsertDt()) || item.getInsertDt().contains("2099")) {
                    helper.setText(R.id.item_home_consult_time_desc_tv, R.string.time_to_be_determined);
                } else {
                    helper.setText(R.id.item_home_consult_time_desc_tv, getFriendlyTimeSpanByNow(item.getInsertDt()));
                }
            }
        }
    }

    private void displayDoctorInfo(BaseViewHolder helper, AppointmentItemInfo item) {
        CircleImageView avatar = helper.getView(R.id.item_home_consult_doctor_civ);

        if (currentUserId == item.getDoctorUserId()) {
            GlideUtils.showHospitalLogo(avatar, mContext, item.getAttendHospitaLogo());
            helper.setText(R.id.item_home_consult_doctor_name_tv, item.getAttendHospitalName());
            helper.setGone(R.id.item_home_consult_doctor_level_tv, false);
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getAttendDepartmentName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getAttendDoctorName());
            helper.setText(R.id.item_home_consult_doctor_hospital_info_tv, doctorInfo);
        } else {
            //基层医生
            GlideUtils.showDoctorAvatar(avatar, mContext, AvatarUtils.getAvatar(false, item.getDoctorUserId(), "0"));
            helper.setText(R.id.item_home_consult_doctor_name_tv, item.getSuperDoctorName());
            helper.setVisible(R.id.item_home_consult_doctor_level_tv, true);
            helper.setText(R.id.item_home_consult_doctor_level_tv, item.getSuperDoctorLevel());
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(item.getSuperHospitalName());
            doctorInfo.append(" ");
            doctorInfo.append(item.getSuperDepartmentName());
            helper.setText(R.id.item_home_consult_doctor_hospital_info_tv, doctorInfo);
        }
    }

    private void displayBottomHospital(BaseViewHolder helper, AppointmentItemInfo item) {
        if (currentUserId == item.getDoctorUserId()) {
            helper.setText(R.id.item_home_consult_hospital_type_tv, "接收医院");
            helper.setTextColor(R.id.item_home_consult_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_049eff));
            helper.setBackgroundRes(R.id.item_home_consult_hospital_type_tv, R.drawable.bg_solid_d5efff_radius_2);
            helper.setText(R.id.item_home_consult_hospital_tv, item.getSuperHospitalName());
        } else {
            //上级专家
            helper.setText(R.id.item_home_consult_hospital_type_tv, "发起医院");
            helper.setTextColor(R.id.item_home_consult_hospital_type_tv, ContextCompat.getColor(mContext, R.color.color_fc8404));
            helper.setBackgroundRes(R.id.item_home_consult_hospital_type_tv, R.drawable.bg_solid_fff0e0_radius_2);
            helper.setText(R.id.item_home_consult_hospital_tv, item.getAttendHospitalName());
        }
    }

    private void displayAppointmentState(BaseViewHolder helper, AppointmentItemInfo item) {
        int stateDesc = R.string.appoint_state_wait_communication;
        int stateColor = R.color.color_fc8404;
        int stateBg = R.drawable.bg_solid_ffffff_stroke_fc8404_radius_2;
        switch (item.getAppointmentStat()) {
            //等待沟通
            case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                stateDesc = R.string.appoint_state_wait_communication;
                //等待支付或支付超时
                break;
            case AppConstant.AppointmentState.WAIT_USER_PAY:
                if (item.getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_IMAGE_CONSULT) {
                    stateDesc = R.string.appoint_state_wait_pay;
                } else {
                    if (item.getPaySurplusDt() == -1) {//支付超时
                        stateDesc = R.string.appoint_state_pay_timeout;
                    } else if (item.getPaySurplusDt() > 0) {//等待支付
                        stateDesc = R.string.appoint_state_wait_pay;
                    }
                }
                break;
            case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                if (item.getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT) {
                    if (item.getStatReason() == 0) {
                        stateDesc = R.string.waiting_for_video;//等待视频
                    } else {
                        stateDesc = R.string.appoint_state_close;//就诊关闭
                    }
                } else {
                    if (item.getStatReason() == 0) {
                        stateDesc = R.string.waiting_for_advice;//等待医嘱
                    } else {
                        stateDesc = R.string.appoint_state_close;//就诊关闭
                        stateBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                        stateColor = R.color.color_666666;
                    }
                }
                break;
            case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED://10
                if (item.getIsDiagnosis() == 0) {
                    stateDesc = R.string.waiting_for_advice;//等待医嘱
                } else {
                    stateDesc = R.string.report_trim;//报告整理
                }
                break;
            case AppConstant.AppointmentState.ASSISTANT_LOGGING_REPORT://12
            case AppConstant.AppointmentState.DOCTOR_SIGN_REPORT:
                stateDesc = R.string.report_trim;//报告整理
                break;
            case AppConstant.AppointmentState.PATIENT_ACCEPT_REPORT://14
                stateDesc = R.string.appoint_state_check_report;//查看报告
                break;
            case AppConstant.AppointmentState.APPOINTMENT_FINISHED://15
                if (item.getStatReason() == 3) {
                    stateDesc = R.string.appoint_state_complete;//就诊完成
                } else {
                    stateDesc = R.string.appoint_state_close;//就诊关闭
                }
                stateBg = R.drawable.bg_solid_ffffff_stroke_666666_radius_2;
                stateColor = R.color.color_666666;
                break;
            default:
                stateDesc = R.string.appoint_action_no_state;
                break;
        }

        helper.setText(R.id.item_home_consult_order_state_tv, stateDesc);
        helper.setTextColor(R.id.item_home_consult_order_state_tv, ContextCompat.getColor(mContext, stateColor));
        helper.setBackgroundRes(R.id.item_home_consult_order_state_tv, stateBg);
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
