package cn.longmaster.hospital.doctor.util;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;

/**
 * 通用工具类
 * Created by JinKe on 2016-08-02.
 */
public class ConsultUtil {

    /**
     * 获取拉取就诊列表接口请求状态参数
     *
     * @param staNums 状态
     * @return 请求状态参数
     */
    public static String getStaNumReqParams(int... staNums) {
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < staNums.length; i++) {
            stringBuffer.append(staNums[i]);
            if (staNums.length != 1 && i != staNums.length - 1) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 获取预约状态对应文案
     *
     * @param context
     * @return
     */
    public static String getAppointStateDesc(Context context, AppointmentInfo appointmentInfo) {
        String stateDesc = "";
        if (appointmentInfo.getBaseInfo() != null) {
            switch (appointmentInfo.getBaseInfo().getAppointmentStat()) {
                //等待沟通
                case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                    stateDesc = context.getString(R.string.appoint_state_wait_communication);
                    break;
                //等待支付或支付超时
                case AppConstant.AppointmentState.WAIT_USER_PAY:
                    if (appointmentInfo.getExtendsInfo() != null &&
                            appointmentInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_IMAGE_CONSULT) {
                        stateDesc = context.getString(R.string.appoint_state_wait_pay);
                    } else {
                        if (appointmentInfo.getBaseInfo() != null && appointmentInfo.getBaseInfo().getPaySurplusDt() == -1) {//支付超时
                            stateDesc = context.getString(R.string.appoint_state_pay_timeout);
                        } else if (appointmentInfo.getBaseInfo() != null && appointmentInfo.getBaseInfo().getPaySurplusDt() > 0) {//等待支付
                            stateDesc = context.getString(R.string.appoint_state_wait_pay);
                        }
                    }
                    break;

                case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                    if (appointmentInfo.getExtendsInfo() == null || appointmentInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT) {
                        if (appointmentInfo.getBaseInfo().getStatReason() == 0) {
                            stateDesc = context.getString(R.string.waiting_for_video);//等待视频
                        } else {
                            stateDesc = context.getString(R.string.appoint_state_close);//就诊关闭
                        }
                    } else {
                        if (appointmentInfo.getBaseInfo().getStatReason() == 0) {
                            stateDesc = context.getString(R.string.waiting_for_advice);//等待医嘱
                        } else {
                            stateDesc = context.getString(R.string.appoint_state_close);//就诊关闭
                        }
                    }
                    break;

                case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED://10
                    if (appointmentInfo.getBaseInfo().getIsDiagnosis() == 0) {
                        stateDesc = context.getString(R.string.waiting_for_advice);//等待医嘱
                    } else {
                        stateDesc = context.getString(R.string.report_trim);//报告整理
                    }
                    break;

                case AppConstant.AppointmentState.ASSISTANT_LOGGING_REPORT://12
                case AppConstant.AppointmentState.DOCTOR_SIGN_REPORT:
                    stateDesc = context.getString(R.string.report_trim);//报告整理
                    break;

                case AppConstant.AppointmentState.PATIENT_ACCEPT_REPORT://14
                    stateDesc = context.getString(R.string.appoint_state_check_report);//查看报告
                    break;

                case AppConstant.AppointmentState.APPOINTMENT_FINISHED://15
                    if (appointmentInfo.getBaseInfo().getStatReason() == 3) {
                        stateDesc = context.getString(R.string.appoint_state_complete);//就诊完成
                    } else {
                        stateDesc = context.getString(R.string.appoint_state_close);//就诊关闭
                    }
                    break;

                default:
                    stateDesc = context.getString(R.string.appoint_action_no_state);
                    break;
            }
        } else {

        }
        return stateDesc;
    }

    /**
     * 获取操作按钮对应文案
     *
     * @param context
     * @param appointmentInfo
     * @return
     */
    public static String getAppointActionDesc(Context context, AppointmentInfo appointmentInfo) {
        String appointActionDesc = "";
        if (appointmentInfo.getBaseInfo() != null) {
            switch (appointmentInfo.getBaseInfo().getAppointmentStat()) {
                case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL://1
                    appointActionDesc = context.getString(R.string.appoint_action_manage_data);
                    break;

                case AppConstant.AppointmentState.WAIT_USER_PAY://2
                    if (appointmentInfo.getExtendsInfo() != null &&
                            appointmentInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_IMAGE_CONSULT) {
                        if (getUserType(appointmentInfo) == AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR) {//上级专家
                            appointActionDesc = context.getString(R.string.appoint_action_give_advice);
                        } else {//首诊医生
                            appointActionDesc = context.getString(R.string.appoint_action_view_record);
                        }
                    } else {
                        if (appointmentInfo.getBaseInfo() != null && appointmentInfo.getBaseInfo().getPaySurplusDt() == -1) {//支付超时
                            appointActionDesc = context.getString(R.string.appoint_action_view_record);
                        } else if (appointmentInfo.getBaseInfo() != null && appointmentInfo.getBaseInfo().getPaySurplusDt() > 0) {//等待支付
                            appointActionDesc = context.getString(R.string.appoint_action_enter_the_consulting_room);
                        }
                    }
                    break;
                case AppConstant.AppointmentState.ASSISTANT_LOGGING_REPORT://12
                case AppConstant.AppointmentState.DOCTOR_SIGN_REPORT://13
                case AppConstant.AppointmentState.PATIENT_ACCEPT_REPORT://14
                case AppConstant.AppointmentState.APPOINTMENT_FINISHED://15
                    appointActionDesc = context.getString(R.string.appoint_action_view_record);
                    break;

                case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION://8等待视频或等待医嘱
                    if (appointmentInfo.getExtendsInfo() == null || appointmentInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT) {
                        if (appointmentInfo.getBaseInfo().getStatReason() == 0) {
                            appointActionDesc = context.getString(R.string.appoint_action_enter_the_consulting_room);
                        } else {
                            appointActionDesc = context.getString(R.string.appoint_action_view_record);
                        }
                    } else {
                        if (appointmentInfo.getBaseInfo().getStatReason() == 0) {
                            if (getUserType(appointmentInfo) == AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR) {//上级专家
                                appointActionDesc = context.getString(R.string.appoint_action_give_advice);
                            } else {
                                appointActionDesc = context.getString(R.string.appoint_action_view_record);
                            }
                        } else {
                            appointActionDesc = context.getString(R.string.appoint_action_view_record);
                        }
                    }
                    break;

                case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED://10
                    if (appointmentInfo.getBaseInfo().getIsDiagnosis() == 0) {
                        if (getUserType(appointmentInfo) == AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR) {//上级专家
                            appointActionDesc = context.getString(R.string.appoint_action_give_advice);
                        } else {
                            appointActionDesc = context.getString(R.string.appoint_action_view_record);
                        }
                    } else {
                        appointActionDesc = context.getString(R.string.appoint_action_view_record);
                    }
                    break;

                default:
                    appointActionDesc = context.getString(R.string.appoint_action_no_state);
                    break;
            }
        }
        return appointActionDesc;
    }

    /**
     * 把秒转换成分′秒″
     * 只试用于60分以内的情况
     *
     * @param second
     * @return
     */

    public static String formatSecond(int second) {
        String html = "0″";
        if (second > 0) {
            String format;
            Object[] array;
            Integer minutes = second / 60;
            Integer seconds = second - minutes * 60;
            if (minutes > 0) {
                format = "%1$d′%2$d″";
                array = new Object[]{minutes, seconds};
            } else {
                format = "%1$d″";
                array = new Object[]{seconds};
            }
            html = String.format(format, array);
        }
        return html;
    }

    /**
     * 时间截取
     *
     * @param time
     * @return
     */
    public static String dateCut(String time) {
        Date date = new Date();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = formatter.format(date);
            return dateString;
        }
        return "";
    }

    /**
     * 获取userType
     *
     * @param appointmentInfo
     * @return
     */
    public static int getUserType(AppointmentInfo appointmentInfo) {
        int userType = 0;
        if (AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId() == appointmentInfo.getBaseInfo().getDoctorUserId()) {
            userType = AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR;//上级专家
        } else if (AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId() == appointmentInfo.getBaseInfo().getAttendingDoctorUserId()) {
            userType = AppConstant.DoctorType.DOCTOR_TYPE_ATTENDING_DOCTOR;//首诊医生
        } else if (AppApplication.getInstance().getManager(UserInfoManager.class).getCurrentUserInfo().getUserId() == appointmentInfo.getBaseInfo().getAdminId()) {
            userType = AppConstant.DoctorType.DOCTOR_TYPE_ASSISTANT_DOCTOR;//医生助理
        }
        return userType;
    }

    public static String getServiceTitle(Context context, int serviceType) {
        String title;
        switch (serviceType) {
            case AppConstant.ServiceType.SERVICE_TYPE_REMOTE_CONSULT:
                title = context.getString(R.string.service_type_remote_consult);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_MEDICAL_ADVICE:
                title = context.getString(R.string.service_type_remote_advice);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_RETURN_CONSULT:
                title = context.getString(R.string.service_type_remote_consult);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_RETURN_ADVICE:
                title = context.getString(R.string.service_type_remote_advice);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_REMOTE_OUTPATIENT:
                title = context.getString(R.string.service_type_remote_outpatient);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_REMOTE_WARDS:
                title = context.getString(R.string.service_type_remote_wards);
                break;

            case AppConstant.ServiceType.SERVICE_TYPE_IMAGE_CONSULT:
                title = context.getString(R.string.service_type_remote_image_consult);
                break;
            default:
                title = context.getString(R.string.appoint_state_communicating);
                break;
        }
        return title;
    }

    public static String getIMGroupStatus(Context context, int status) {
        String groupStatus = "";
        switch (status) {
            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_ORIGINAL:
                groupStatus = context.getString(R.string.im_group_status_waiting);
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_RECEPTION:
                groupStatus = context.getString(R.string.im_group_status_consulting);
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_REFUSE:
                groupStatus = context.getString(R.string.im_group_status_refused);
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_CLOSE:
                groupStatus = context.getString(R.string.im_group_status_complete);
                break;

            case AppConstant.IMGroupStatus.IM_GROUP_STATUS_FOLLOW_UP:
                groupStatus = context.getString(R.string.im_group_status_following);
                break;

        }
        return groupStatus;
    }

    public static String getOrderState(Context context, int state, int reason) {
        String orderState = "";
        switch (state) {
            case AppConstant.AppointmentState.WAIT_ASSISTANT_CALL:
                orderState = context.getString(R.string.rounds_waiting_diagnosis);
                break;

            case AppConstant.AppointmentState.DATA_CHECK_FAIL:
                orderState = context.getString(R.string.rounds_wait_receive);
                break;

            case AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION:
                orderState = context.getString(R.string.wait_for_consult);
                break;

            case AppConstant.AppointmentState.DOCTOR_PATIENT_VIDEOED:
            case AppConstant.AppointmentState.APPOINTMENT_FINISHED:
                orderState = context.getString(R.string.appoint_state_complete);
                break;
        }
        return orderState;
    }
}
