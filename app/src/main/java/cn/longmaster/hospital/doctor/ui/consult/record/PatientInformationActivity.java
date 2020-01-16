package cn.longmaster.hospital.doctor.ui.consult.record;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.ClinicInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.GetOrderInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DoctorDiagnosisInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.VideoLiveCheckRequest;
import cn.longmaster.hospital.doctor.core.requests.consult.record.DoctorDiagnosisRequester;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatMemberListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.video.ConsultRoomActivity;
import cn.longmaster.hospital.doctor.ui.im.RoleAdapter;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.RoundsReturnVisitFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.VideoPlaybackFragment;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * 患者信息Activity
 * 其实是会诊详情
 * Created by Yang² on 2016/7/13.
 * Mod by biao on2019/11/07
 */
public class PatientInformationActivity extends NewBaseActivity {
    @FindViewById(R.id.act_patient_info_aab)
    private AppActionBar actPatientInfoAab;
    @FindViewById(R.id.act_patient_info_appointment_num_tv)
    private TextView actPatientInfoAppointmentNumTv;
    @FindViewById(R.id.act_patient_info_appointment_num_desc_tv)
    private TextView actPatientInfoAppointmentNumDescTv;
    @FindViewById(R.id.act_patient_info_time_tv)
    private TextView actPatientInfoTimeTv;
    @FindViewById(R.id.act_patient_info_time_desc_tv)
    private TextView actPatientInfoTimeDescTv;
    @FindViewById(R.id.act_patient_info_time_v)
    private View actPatientInfoTimeV;
    @FindViewById(R.id.act_patient_info_member_list_ll)
    private LinearLayout actPatientInfoMemberListLl;
    @FindViewById(R.id.act_patient_info_no_member_tv)
    private TextView actPatientInfoNoMemberTv;
    @FindViewById(R.id.act_patient_info_member_load_pb)
    private ProgressBar actPatientInfoMemberLoadPb;
    @FindViewById(R.id.act_patient_info_member_load_fail_tv)
    private TextView actPatientInfoMemberLoadFailTv;
    @FindViewById(R.id.act_patient_info_member_rv)
    private RecyclerView actPatientInfoMemberRv;
    @FindViewById(R.id.act_patient_info_tab_rg)
    private RadioGroup actPatientInfoTabRg;
    @FindViewById(R.id.act_patient_info_tab_medical_rb)
    private RadioButton actPatientInfoTabMedicalRb;
    @FindViewById(R.id.act_patient_info_tab_report_rb)
    private RadioButton actPatientInfoTabReportRb;
    @FindViewById(R.id.act_patient_info_video_playback_rb)
    private RadioButton actPatientInfoVideoPlaybackRb;
    @FindViewById(R.id.act_patient_info_return_visit_rb)
    private RadioButton actPatientInfoReturnVisitRb;
    @FindViewById(R.id.act_patient_info_content_fl)
    private FrameLayout actPatientInfoContentFl;
    @FindViewById(R.id.act_patient_info_operation_ll)
    private LinearLayout actPatientInfoOperationLl;
    @FindViewById(R.id.act_patient_info_data_manage_iv)
    private ImageView actPatientInfoDataManageIv;
    @FindViewById(R.id.act_patient_info_join_room_iv)
    private ImageView actPatientInfoJoinRoomIv;

    @AppApplication.Manager
    private DoctorManager mDoctorMannager;
    @AppApplication.Manager
    private ConsultManager mConsultManager;
    @AppApplication.Manager
    UserInfoManager mUserInfoManager;

    private int mAppointmentId;
    private AppointmentInfo mAppointmentInfo;
    private GetOrderInfo mGetOrderInfo;
    private PatientInfo mPatientInfo;
    private boolean mIsVideoRoomEnter;//是否从视频诊室进入
    private boolean mIsRelateRecord;//是否是关联病历
    private boolean mIsClinic;
    private boolean mIsExperts = false;
    private ShareDialog mShareDialog;
    private int mCurrentTab = 0;

    private boolean mIsHaveAuthority;
    private int mUserType;
    private int currentDoctorId;
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private List<Fragment> fragments = new ArrayList<>(4);
    private ConsultCaseInfoFragment consultCaseInfoFragment;
    private ConsultReportFragment consultReportFragment;
    private Fragment videoPlaybackFragment;
    private Fragment returnVisitFragment;
    private RoleAdapter roleAdapter;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        mIsVideoRoomEnter = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, false);
        mIsRelateRecord = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD, false);
        mIsClinic = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_CLINIC, false);
        mIsHaveAuthority = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_IS_COURSE, false);
    }

    @Override
    protected void initDatas() {
        roleAdapter = new RoleAdapter(R.layout.item_consult_role_member, new ArrayList<>(0));
        roleAdapter.setOnItemClickListener((adapter, view, position) -> {
            MemberListInfo memberListInfo = (MemberListInfo) adapter.getItem(position);
            if (null != memberListInfo) {
                displayHeadPortraitDialog(memberListInfo);
            }
        });
        currentDoctorId = mUserInfoManager.getCurrentUserInfo().getUserId();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_patient_info;
    }

    @Override
    protected void initViews() {
        if (mIsVideoRoomEnter) {
            actPatientInfoOperationLl.setVisibility(View.GONE);
        }
        if (mIsRelateRecord) {
            actPatientInfoAab.removeFunction(AppActionBar.FUNCTION_RIGHT_BTN);
            actPatientInfoAab.addFunction(AppActionBar.FUNCTION_TITLE);
            actPatientInfoAab.setTitle(getString(R.string.title_bar_patient_information));
            actPatientInfoOperationLl.setVisibility(View.GONE);
        }

        actPatientInfoMemberRv.setLayoutManager(RecyclerViewUtils.getHorLinearLayoutManager(this));
        actPatientInfoMemberRv.setAdapter(roleAdapter);
        actPatientInfoAppointmentNumDescTv.setText(mAppointmentId + "");
        initListener();
        mShareDialog = new ShareDialog(this, createShareList());
        if (mIsHaveAuthority) {
            actPatientInfoDataManageIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMemberList(mAppointmentId);
        getPatientInfo(mAppointmentId);
        if (mIsClinic) {
            getClinicInfo(mAppointmentId);
        } else {
            getAppointmentInfo(mAppointmentId);
        }
    }

    private void initListener() {
        actPatientInfoTabRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.act_patient_info_tab_medical_rb:
                    actPatientInfoTabMedicalRb.setTextSize(18);
                    actPatientInfoTabReportRb.setTextSize(16);
                    actPatientInfoVideoPlaybackRb.setTextSize(16);
                    actPatientInfoReturnVisitRb.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.act_patient_info_tab_report_rb:
                    actPatientInfoTabMedicalRb.setTextSize(16);
                    actPatientInfoTabReportRb.setTextSize(18);
                    actPatientInfoVideoPlaybackRb.setTextSize(16);
                    actPatientInfoReturnVisitRb.setTextSize(16);
                    mCurrentTab = 1;
                    break;
                case R.id.act_patient_info_video_playback_rb:
                    actPatientInfoTabMedicalRb.setTextSize(16);
                    actPatientInfoTabReportRb.setTextSize(16);
                    actPatientInfoVideoPlaybackRb.setTextSize(18);
                    actPatientInfoReturnVisitRb.setTextSize(16);
                    mCurrentTab = 2;
                    break;
                case R.id.act_patient_info_return_visit_rb:
                    actPatientInfoTabMedicalRb.setTextSize(16);
                    actPatientInfoTabReportRb.setTextSize(16);
                    actPatientInfoVideoPlaybackRb.setTextSize(16);
                    actPatientInfoReturnVisitRb.setTextSize(18);
                    mCurrentTab = 3;
                    break;
                default:
                    mCurrentTab = 0;
            }
            if (null != radioTabFragmentHelper) {
                radioTabFragmentHelper.setFragment(mCurrentTab);
            }
        });
        actPatientInfoAab.setRightBtnOnClickListener(v -> {
            showShareDialog(getShareContent(mIsClinic));
        });
        actPatientInfoDataManageIv.setOnClickListener(v -> {
            if (null != mPatientInfo && null != mAppointmentInfo) {
                getDisplay().startConsultDataManageActivity(false, false, mPatientInfo, mAppointmentInfo, 0, 0);
            }
        });
        actPatientInfoJoinRoomIv.setOnClickListener(v -> {
            Logger.logI(Logger.APPOINTMENT, "onClick-->mIsHaveAuthority:" + mIsHaveAuthority + ",mUserType:" + mUserType);
            Intent intent = new Intent(getThisActivity(), ConsultRoomActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, mAppointmentInfo.getBaseInfo().getAppointmentId());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_USER_TYPE, mUserType);
            startActivity(intent);
        });
    }

    private void initFragment(int appointmentId, boolean isClinic, GetOrderInfo orderInfo, AppointmentInfo appointmentInfo) {
        consultCaseInfoFragment = ConsultCaseInfoFragment.getInstance(appointmentId, mIsVideoRoomEnter);
        consultReportFragment = ConsultReportFragment.getInstance(appointmentInfo, mIsExperts, mIsRelateRecord);
        if (isClinic) {
            videoPlaybackFragment = VideoPlaybackFragment.getInstance(orderInfo);
            returnVisitFragment = RoundsReturnVisitFragment.getInstance(mIsVideoRoomEnter, orderInfo.getVisitUrl());
        } else {
            videoPlaybackFragment = ConsultVideoPlaybackFragment.getInstance(appointmentInfo);
            returnVisitFragment = ConsultReturnVisitFragment.getInstance(mIsVideoRoomEnter, appointmentInfo.getVisitUrl());
        }
        fragments.clear();
        fragments.add(consultCaseInfoFragment);
        fragments.add(consultReportFragment);
        fragments.add(videoPlaybackFragment);
        fragments.add(returnVisitFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setCurrentTab(mCurrentTab)
                .setFragmentManager(getSupportFragmentManager())
                .setContainerViewId(R.id.act_patient_info_content_fl)
                .setFragmentList(fragments)
                .build();
        radioTabFragmentHelper.initFragment();
    }

    private void displayHeadPortraitDialog(MemberListInfo memberListInfo) {
        View heardView = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_head_portrait_dialog, null);
        final Dialog dialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(heardView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = DisplayUtil.dp2px(200);
        lp.height = DisplayUtil.dp2px(220);
        win.setAttributes(lp);
        LinearLayout patientLl = heardView.findViewById(R.id.heard_portrait_dialog_patient_ll);
        LinearLayout doctorLl = heardView.findViewById(R.id.heard_portrait_dialog_doctor_ll);
        TextView identityTv = heardView.findViewById(R.id.heard_portrait_dialog_identity_tv);
        CircleImageView heardPortraitDialogImg = heardView.findViewById(R.id.heard_portrait_dialog_img);
        TextView patientName = heardView.findViewById(R.id.heard_portrait_dialog_patient_name);
        TextView patientPhone = heardView.findViewById(R.id.heard_portrait_dialog_patient_phone);
        TextView doctorName = heardView.findViewById(R.id.heard_portrait_dialog_doctor_name);
        TextView doctorLevel = heardView.findViewById(R.id.heard_portrait_dialog_doctor_level);
        TextView doctorHospitalName = heardView.findViewById(R.id.heard_portrait_dialog_doctor_hospital_name);
        TextView doctorDepartment = heardView.findViewById(R.id.heard_portrait_dialog_doctor_department);

        identityTv.setText(getRoleName(memberListInfo.getRole()));
        if (memberListInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT || memberListInfo.getRole() == AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT) {
            GlideUtils.showPatientChatAvatar(heardPortraitDialogImg, getThisActivity(), AvatarUtils.getAvatar(false, memberListInfo.getUserId(), "0"));
        } else {
            GlideUtils.showDoctorChatAvatar(heardPortraitDialogImg, getThisActivity(), AvatarUtils.getAvatar(false, memberListInfo.getUserId(), "0"));
        }
        switch (memberListInfo.getRole()) {
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT:
                showPatientLlDialog(patientLl, doctorLl);
                patientName.setText(mPatientInfo.getPatientBaseInfo().getRealName());
                patientPhone.setText(getString(R.string.chat_phone_numb) + mPatientInfo.getPatientBaseInfo().getPhoneNum() + "");
                break;
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT:
                showPatientLlDialog(patientLl, doctorLl);
                patientName.setText(getString(R.string.chat_mdt_patient));
                patientPhone.setText(getString(R.string.chat_phone_numb) + getString(R.string.chat_no));
                break;
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ASSISTANT_DOCTOR:
                showPatientLlDialog(patientLl, doctorLl);
                patientName.setText(getString(R.string.chat_administrators));
                patientPhone.setText(getString(R.string.chat_phone_numb) + getString(R.string.chat_no));
                mDoctorMannager.getDoctorInfo(memberListInfo.getUserId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        if (null != doctorBaseInfo) {
                            patientName.setText(doctorBaseInfo.getRealName());
                            patientPhone.setText(getString(R.string.chat_phone_numb) + doctorBaseInfo.getPhoneNum());
                        }
                    }

                    @Override
                    public void onFailed(int code, String msg) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                break;
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_CONFERENCE_STAFF:
                showPatientLlDialog(patientLl, doctorLl);
                patientName.setText(getString(R.string.chat_conference_staff));
                patientPhone.setText(getString(R.string.chat_phone_numb) + getString(R.string.chat_no));
                break;
            default:
                patientLl.setVisibility(View.GONE);
                doctorLl.setVisibility(View.VISIBLE);
                getDoctorInfo(memberListInfo.getUserId(), doctorName, doctorLevel, doctorHospitalName, doctorDepartment);
                break;

        }
    }

    private void showPatientLlDialog(LinearLayout patientLl, LinearLayout doctorLl) {
        patientLl.setVisibility(View.VISIBLE);
        doctorLl.setVisibility(View.GONE);
    }

    private String getRoleName(int role) {
        String roleName = "";
        switch (role) {
            case AppConstant.IMGroupRole.IM_GROUP_ROLE_PATIENT:
                roleName = getString(R.string.chat_role_patient);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ATTENDING_DOCTOR:
                roleName = getString(R.string.chat_role_attending_doctor);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ADMINISTRATOR:
                roleName = getString(R.string.chat_role_administrator);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_SUPERIOR_DOCTOR:
                roleName = getString(R.string.chat_role_superior_doctor);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_ASSISTANT_DOCTOR:
                roleName = getString(R.string.chat_role_assistant_doctor);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_CONFERENCE_STAFF:
                roleName = getString(R.string.chat_role_conference_staff);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_DOCTOR:
                roleName = getString(R.string.chat_role_mdt_doctor);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_MDT_PATIENT:
                roleName = getString(R.string.chat_role_mdt_patient);
                break;

            case AppConstant.IMGroupRole.IM_GROUP_ROLE_SYSTEM_AUTO:
                roleName = getString(R.string.chat_role_system_auto);
                break;
        }
        return roleName;
    }

    private void getMemberList(int appointmentId) {
        actPatientInfoMemberLoadPb.setVisibility(View.VISIBLE);
        actPatientInfoMemberLoadFailTv.setVisibility(View.GONE);
        actPatientInfoMemberRv.setVisibility(View.GONE);
        GetChatMemberListRequester requester = new GetChatMemberListRequester(new DefaultResultCallback<List<MemberListInfo>>() {
            @Override
            public void onSuccess(List<MemberListInfo> memberListInfos, BaseResult baseResult) {
                if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                    roleAdapter.setNewData(memberListInfos);
                    actPatientInfoMemberLoadFailTv.setVisibility(View.GONE);
                    actPatientInfoMemberRv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == OnResultListener.RESULT_FAILED) {
                    actPatientInfoMemberLoadFailTv.setVisibility(View.VISIBLE);
                } else if (result.getCode() == -102) {
                    actPatientInfoMemberLoadFailTv.setVisibility(View.GONE);
                    actPatientInfoNoMemberTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                actPatientInfoMemberLoadPb.setVisibility(View.GONE);
            }
        });
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    public void getPatientInfo(final int appointmentId) {
        mConsultManager.getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    mPatientInfo = patientInfo;
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

    private void getDoctorInfo(final int doctorId, final TextView doctorName, final TextView doctorLevel, final TextView doctorHospitalName, final TextView doctorDepartment) {
        mDoctorMannager.getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null == doctorBaseInfo) {
                    return;
                }
                doctorName.setText(doctorBaseInfo.getRealName());
                doctorLevel.setText(getString(R.string.chat_level, doctorBaseInfo.getDoctorLevel()));
                doctorHospitalName.setText(doctorBaseInfo.getHospitalName());
                doctorDepartment.setText(doctorBaseInfo.getDepartmentName());
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void getClinicInfo(int appointmentId) {
        VideoLiveCheckRequest request = new VideoLiveCheckRequest((baseResult, getOrderInfo) -> {
            if (baseResult.getCode() == OnResultCallback.RESULT_SUCCESS && getOrderInfo != null) {
                mGetOrderInfo = getOrderInfo;
                mIsExperts = currentDoctorId != getOrderInfo.getDoctorUserId();
                initFragment(appointmentId, mIsClinic, getOrderInfo, null);
                displayGetOrderInfo(getOrderInfo);
            } else {
                ToastUtils.showShort(getString(R.string.consult_room_state_net_bad));
            }
        });
        request.appointmentId = appointmentId;
        request.doPost();
    }

    private void getAppointmentInfo(final int appointmentId) {
        mConsultManager.getAppointmentInfo(appointmentId, (baseResult, appointmentInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && appointmentInfo != null) {
                mAppointmentInfo = appointmentInfo;
                AppointmentBaseInfo baseInfo = appointmentInfo.getBaseInfo();
                if (null == baseInfo) {
                    return;
                }
                mIsExperts = baseInfo.getDoctorUserId() == currentDoctorId;
                initFragment(appointmentId, mIsClinic, null, appointmentInfo);
                displayConsultationRadioButton(appointmentId, appointmentInfo);
            }
        });
    }

    private void getDoctorDiagnosis(int appointmentId) {
        DoctorDiagnosisRequester doctorDiagnosisRequester = new DoctorDiagnosisRequester((baseResult, doctorDiagnosisInfo) -> {
            Logger.logD(Logger.IM, TAG + "->getDoctorDiagnosis-->baseResult():" + baseResult + ",doctorDiagnosisInfo" + doctorDiagnosisInfo);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && doctorDiagnosisInfo != null) {
                displayDiagnosisInfo(doctorDiagnosisInfo);
            } else {
                ToastUtils.showShort("数据获取失败,请重新加载!");
            }
        });
        doctorDiagnosisRequester.appointmentId = appointmentId;
        doctorDiagnosisRequester.doPost();
    }

    private void displayGetOrderInfo(GetOrderInfo getOrderInfo) {
        ClinicInfo clinicInfo = getOrderInfo.getClinicInfo();
        if (clinicInfo != null && !StringUtils.isEmpty(clinicInfo.getClinicBeginDt()) && !clinicInfo.getClinicBeginDt().contains("2099")) {
            actPatientInfoTimeDescTv.setText(clinicInfo.getClinicBeginDt());
        }
        if (currentDoctorId == getOrderInfo.getAttendingDoctorUserId()) {
            actPatientInfoDataManageIv.setVisibility(View.VISIBLE);
            mUserType = AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR;
        } else if (currentDoctorId == getOrderInfo.getDoctorUserId()) {
            actPatientInfoDataManageIv.setVisibility(View.GONE);
            mUserType = AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;
        } else {
            mUserType = AppConstant.UserType.USER_TYPE_LIVE_BROADCAST_WATCH;
        }
        if (LibCollections.isNotEmpty(getOrderInfo.getReviewVideoInfos())) {
            actPatientInfoVideoPlaybackRb.setVisibility(View.VISIBLE);
        } else {
            actPatientInfoVideoPlaybackRb.setVisibility(View.GONE);
        }
        if (StringUtils.isEmpty(getOrderInfo.getVisitUrl())) {
            actPatientInfoReturnVisitRb.setVisibility(View.GONE);
        } else {
            actPatientInfoReturnVisitRb.setVisibility(View.VISIBLE);
        }
        if (actPatientInfoTabReportRb.getVisibility() == View.GONE && actPatientInfoVideoPlaybackRb.getVisibility() == View.GONE && actPatientInfoReturnVisitRb.getVisibility() == View.GONE) {
            actPatientInfoTabRg.setVisibility(View.GONE);
        } else {
            actPatientInfoTabRg.setVisibility(View.VISIBLE);
        }
    }

    private void displayConsultationRadioButton(int appointmentId, @NonNull AppointmentInfo appointmentInfo) {
        AppointmentBaseInfo baseInfo = appointmentInfo.getBaseInfo();
        if (null == baseInfo) {
            return;
        }
        if (!StringUtils.isEmpty(baseInfo.getPredictCureDt()) && !baseInfo.getPredictCureDt().contains("2099")) {
            actPatientInfoTimeDescTv.setText(baseInfo.getPredictCureDt());
        }
        if (currentDoctorId == baseInfo.getDoctorUserId()) {
            if (baseInfo.getAppointmentStat() == AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION) {
                if (appointmentInfo.getExtendsInfo() == null || appointmentInfo.getExtendsInfo().getScheduingType() == AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT) {
                    actPatientInfoTabReportRb.setVisibility(View.GONE);
                } else {
                    actPatientInfoTabReportRb.setVisibility(View.VISIBLE);
                }
            } else if (baseInfo.getAppointmentStat() > AppConstant.AppointmentState.DOCTOR_AGREE_RECEPTION) {
                actPatientInfoTabReportRb.setVisibility(View.VISIBLE);
            } else {
                actPatientInfoTabReportRb.setVisibility(View.GONE);
            }
        } else {
            getDoctorDiagnosis(appointmentId);
        }
        if (currentDoctorId == baseInfo.getAttendingDoctorUserId()) {
            actPatientInfoDataManageIv.setVisibility(View.VISIBLE);
            mUserType = AppConstant.UserType.USER_TYPE_ATTENDING_DOCTOR;
        } else if (currentDoctorId == baseInfo.getDoctorUserId()) {
            actPatientInfoDataManageIv.setVisibility(View.GONE);
            mUserType = AppConstant.UserType.USER_TYPE_SUPERIOR_DOCTOR;
        } else {
            actPatientInfoDataManageIv.setVisibility(View.GONE);
            mUserType = AppConstant.UserType.USER_TYPE_LIVE_BROADCAST_WATCH;
        }
        if (appointmentInfo.getReviewVideoInfo().size() > 0) {
            actPatientInfoVideoPlaybackRb.setVisibility(View.VISIBLE);
        } else {
            actPatientInfoVideoPlaybackRb.setVisibility(View.GONE);
        }
        if (StringUtils.isEmpty(appointmentInfo.getVisitUrl())) {
            actPatientInfoReturnVisitRb.setVisibility(View.GONE);
        } else {
            actPatientInfoReturnVisitRb.setVisibility(View.VISIBLE);
        }
        if (actPatientInfoTabReportRb.getVisibility() == View.GONE && actPatientInfoVideoPlaybackRb.getVisibility() == View.GONE && actPatientInfoReturnVisitRb.getVisibility() == View.GONE) {
            actPatientInfoTabRg.setVisibility(View.GONE);
        } else {
            actPatientInfoTabRg.setVisibility(View.VISIBLE);
        }
    }

    private void displayDiagnosisInfo(DoctorDiagnosisInfo doctorDiagnosisInfo) {
        if (mIsExperts) {
            if (LibCollections.isNotEmpty(doctorDiagnosisInfo.getDiagnosisContentList()) || LibCollections.isNotEmpty(doctorDiagnosisInfo.getDiagnosisFileList())) {
                actPatientInfoTabReportRb.setVisibility(View.VISIBLE);
            } else {
                actPatientInfoTabReportRb.setVisibility(View.GONE);
            }
        } else {
            if (LibCollections.isNotEmpty(doctorDiagnosisInfo.getDiagnosisContentList())
                    || LibCollections.isNotEmpty(doctorDiagnosisInfo.getDiagnosisFileList())
                    && mAppointmentInfo.getBaseInfo() != null
                    && (AppConstant.AppointmentState.PATIENT_ACCEPT_REPORT == mAppointmentInfo.getBaseInfo().getAppointmentStat() ||
                    AppConstant.AppointmentState.APPOINTMENT_FINISHED == mAppointmentInfo.getBaseInfo().getAppointmentStat())) {
                actPatientInfoTabReportRb.setVisibility(View.VISIBLE);
            } else {
                actPatientInfoTabReportRb.setVisibility(View.GONE);
            }
        }
        if (actPatientInfoTabReportRb.getVisibility() == View.GONE && actPatientInfoVideoPlaybackRb.getVisibility() == View.GONE && actPatientInfoReturnVisitRb.getVisibility() == View.GONE) {
            actPatientInfoTabRg.setVisibility(View.GONE);
        } else {
            actPatientInfoTabRg.setVisibility(View.VISIBLE);
        }
    }

    private List<ShareItem> createShareList() {
        List<ShareItem> shareItemList = new ArrayList<>(4);
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        return shareItemList;
    }

    private void showShareDialog(ShareEntity shareEntity) {
        if (null == mShareDialog) {
            return;
        }
        mShareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                getShareManager().shareToWeiChat(PatientInformationActivity.this, shareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(PatientInformationActivity.this, shareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(PatientInformationActivity.this, shareEntity);
            }

            @Override
            public void onMyConsultClick() {

            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (mIsClinic) {
                    if (mGetOrderInfo == null) {
                        ToastUtils.showShort("复制失败，请重试！");
                        return;
                    }
                    if (mCurrentTab == 1) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mGetOrderInfo.getMedicalShareUrl()));
                    } else if (mCurrentTab == 2) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mGetOrderInfo.getOpinionShareUrl()));
                    } else if (mCurrentTab == 3) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mGetOrderInfo.getReviewVideoShareUrl()));
                    } else if (mCurrentTab == 4) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mGetOrderInfo.getVisitShareUrl()));
                    }
                } else {
                    if (mAppointmentInfo == null) {
                        ToastUtils.showShort("复制失败，请重试！");
                        return;
                    }
                    if (mCurrentTab == 1) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mAppointmentInfo.getMedicalShareUrl()));
                    } else if (mCurrentTab == 2) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mAppointmentInfo.getOpinionShareUrl()));
                    } else if (mCurrentTab == 3) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mAppointmentInfo.getReviewVideoShareUrl()));
                    } else if (mCurrentTab == 4) {
                        cm.setPrimaryClip(ClipData.newPlainText("Label", mAppointmentInfo.getVisitShareUrl()));
                    }
                }
                ToastUtils.showShort(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {

            }
        });
        mShareDialog.show();
    }

    private ShareEntity getShareContent(boolean isClinic) {
        return isClinic ? getShareContent(mCurrentTab, mGetOrderInfo, mPatientInfo) : getShareContent(mCurrentTab, mAppointmentInfo, mPatientInfo);
    }

    private ShareEntity getShareContent(int currentTab, GetOrderInfo getOrderInfo, PatientInfo patientInfo) {
        ShareEntity shareEntity = new ShareEntity();
        if (getOrderInfo == null) {
            return null;
        }
        shareEntity.setTitle("39互联网医院病历信息");
        if (currentTab == 0) {
            shareEntity.setUrl(getOrderInfo.getMedicalShareUrl());
        } else if (currentTab == 1) {
            shareEntity.setUrl(getOrderInfo.getOpinionShareUrl());
        } else if (currentTab == 2) {
            shareEntity.setUrl(getOrderInfo.getReviewVideoShareUrl());
        } else if (currentTab == 3) {
            shareEntity.setUrl(getOrderInfo.getVisitShareUrl());
        }
        if (null != patientInfo) {
            shareEntity.setContent(patientInfo.getPatientBaseInfo().getRealName() + " " + (patientInfo.getPatientBaseInfo().getGender() == 0 ? "男" : "女") + " " + patientInfo.getPatientBaseInfo().getAge() + " " + patientInfo.getPatientBaseInfo().getFirstCureResult());
        }
        return shareEntity;
    }

    private ShareEntity getShareContent(int currentTab, AppointmentInfo mAppointmentInfo, PatientInfo patientInfo) {
        if (mAppointmentInfo == null) {
            return null;
        }
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setTitle("39互联网医院病历信息");
        if (currentTab == 0) {
            shareEntity.setUrl(mAppointmentInfo.getMedicalShareUrl());
        } else if (currentTab == 1) {
            shareEntity.setUrl(mAppointmentInfo.getOpinionShareUrl());
        } else if (currentTab == 2) {
            shareEntity.setUrl(mAppointmentInfo.getReviewVideoShareUrl());
        } else if (currentTab == 3) {
            shareEntity.setUrl(mAppointmentInfo.getVisitShareUrl());
        }
        if (null != patientInfo) {
            shareEntity.setContent(patientInfo.getPatientBaseInfo().getRealName() + " " + (patientInfo.getPatientBaseInfo().getGender() == 0 ? "男" : "女") + " " + patientInfo.getPatientBaseInfo().getAge() + " " + patientInfo.getPatientBaseInfo().getFirstCureResult());
        }
        return shareEntity;
    }
}
