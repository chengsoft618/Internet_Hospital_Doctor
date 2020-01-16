package cn.longmaster.hospital.doctor.ui.consult.record;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lmmedia.PPAmrPlayer;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.ApplyDescInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialResult;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForRelateInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DrugPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.HistoryInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.OperationPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.PhysicalPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.VoiceDownloader;
import cn.longmaster.hospital.doctor.core.manager.common.BaseConfigManager;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.consult.RecordManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.AuxiliaryMaterialRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetHospitalAndDepartmentInfoRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.adapter.ConsultationRecordMedicalAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 患者信息，病历信息fragment
 * Created by Yang² on 2016/7/15.
 */
public class ConsultCaseInfoFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_record_information_info_aim_tv)
    private TextView mInfoAim;
    @FindViewById(R.id.fragment_record_information_info_doctor_tv)
    private TextView mInfoDoctor;
    @FindViewById(R.id.fragment_record_information_patient_name_tv)
    private TextView mPatientNameTv;
    @FindViewById(R.id.fragment_record_information_contact_information_tv)
    private TextView mContactInformationTv;
    @FindViewById(R.id.fragment_record_information_apply_time_tv)
    private TextView mApplyTimeTv;
    @FindViewById(R.id.fragment_record_information_consult_purpose_tv)
    private TextView mConsultPurposeTv;
    @FindViewById(R.id.fragment_record_information_doctor_assistant_tv)
    private TextView mDoctorAssistantTv;

    @FindViewById(R.id.fragment_record_information_first_diagnosis_tv)
    private TextView mFirstDiagnosisTv;
    @FindViewById(R.id.fragment_record_information_patient_readme_tv)
    private TextView mPatientReadmeTv;
    @FindViewById(R.id.fragment_record_information_voice_readme_ll)
    private LinearLayout mVoiceReadmeLl;
    @FindViewById(R.id.fragment_record_information_voice_readme_icon_iv)
    private ImageView mVoiceReadmeIconIv;
    @FindViewById(R.id.fragment_record_information_voice_readme_time_tv)
    private TextView mVoiceReadmeTimeTv;
    @FindViewById(R.id.fragment_record_information_illness_digest_tv)
    private TextView mIllnessDigestTv;
    @FindViewById(R.id.fragment_record_information_consult_hospital_tv)
    private TextView mConsultHospitalTv;
    @FindViewById(R.id.fragment_record_information_consult_time_tv)
    private TextView mConsultTimeTv;
    @FindViewById(R.id.fragment_record_information_present_medical_line)
    private View mPresentMedicalLineView;
    @FindViewById(R.id.fragment_record_information_present_medical_table_row)
    private TableRow mPresentMedicalTabView;
    @FindViewById(R.id.fragment_record_information_past_medical_line)
    private View mPastMedicalLineView;
    @FindViewById(R.id.fragment_record_information_past_medical_table_row)
    private TableRow mPastMedicalTabView;
    @FindViewById(R.id.fragment_record_information_operation_line)
    private View mOperationLineView;
    @FindViewById(R.id.fragment_record_information_operation_table_row)
    private TableRow mOperationTabView;
    @FindViewById(R.id.fragment_record_information_personal_line)
    private View mPersonalLineView;
    @FindViewById(R.id.fragment_record_information_personal_table_row)
    private TableRow mPersonalTabView;
    @FindViewById(R.id.fragment_record_information_obstetric_line)
    private View mObstetricLineView;
    @FindViewById(R.id.fragment_record_information_obstetric_tab)
    private TableRow mObstetricTabView;
    @FindViewById(R.id.fragment_record_information_family_line)
    private View mFamilyLineView;
    @FindViewById(R.id.fragment_record_information_family_tab)
    private TableRow mFamilyTabView;
    @FindViewById(R.id.fragment_record_information_allergic_line)
    private View mAllergicLineView;
    @FindViewById(R.id.fragment_record_information_allergic_tab)
    private TableRow mAllergicTabView;
    @FindViewById(R.id.fragment_record_information_menstrual_line)
    private View mMenstrualLineView;
    @FindViewById(R.id.fragment_record_information_menstrual_table_row)
    private TableRow mMenstrualTabView;
    @FindViewById(R.id.fragment_record_information_physical_examination_view)
    private LinearLayout mPhysicalExaminationView;
    @FindViewById(R.id.fragment_record_information_treatment_plan_view)
    private LinearLayout mTreatmentPlanView;
    @FindViewById(R.id.fragment_record_information_treatment_line)
    private View mTreatmentLineView;
    @FindViewById(R.id.fragment_record_information_treatment_tab)
    private TableRow mTreatmentTabView;
    @FindViewById(R.id.fragment_record_information_surgical_line)
    private View mSurgicalLineView;
    @FindViewById(R.id.fragment_record_information_surgical_tab)
    private TableRow mSurgicalTabView;
    @FindViewById(R.id.fragment_record_information_recovery_situation_line)
    private View mSituationeView;
    @FindViewById(R.id.fragment_record_information_recovery_situation_tab)
    private TableRow mSituationeTabView;
    @FindViewById(R.id.fragment_record_information_physics_recovery_situation_ll)
    private TextView mSituationeTv;
    @FindViewById(R.id.fragment_record_information_physics_tab)
    private TableRow mPhysicsTabView;
    @FindViewById(R.id.fragment_record_information_first_doctor_name_tv)
    private TextView mFirstDoctorNameTv;
    @FindViewById(R.id.fragment_record_information_first_doctor_hospital_department_tv)
    private TextView mFirstDoctorDepartmentTv;
    @FindViewById(R.id.fragment_record_information_consult_doctor_name_tv)
    private TextView mConsultDoctorNameTv;
    @FindViewById(R.id.fragment_record_information_consult_doctor_hospital_department_tv)
    private TextView mConsultDoctorDepartmentTv;

    @FindViewById(R.id.fragment_record_information_main_narrator_tv)
    private TextView mMainNarratorTv;
    @FindViewById(R.id.fragment_record_information_present_medical_history_tv)
    private TextView mPresentMedicalHistoryTv;
    @FindViewById(R.id.fragment_record_information_past_medical_history_tv)
    private TextView mPastMedicalHistoryTv;
    @FindViewById(R.id.fragment_record_information_personal_history_tv)
    private TextView mPersonalHistoryTv;
    @FindViewById(R.id.fragment_record_information_obstetric_history_tv)
    private TextView mObstetricHistoryTv;
    @FindViewById(R.id.fragment_record_information_menstrual_ll)
    private LinearLayout mMenstrualLl;
    @FindViewById(R.id.fragment_record_information_menstrual_start_age_tv)
    private TextView mMenstrualStartAgeTv;
    @FindViewById(R.id.fragment_record_information_menstrual_days_tv)
    private TextView mMenstrualDaysTv;
    @FindViewById(R.id.fragment_record_information_menstrual_tv_2)
    private TextView mMenstrualCycleTv;
    @FindViewById(R.id.fragment_record_information_menstrual_Last_tv)
    private TextView mMenstrualLastTv;
    @FindViewById(R.id.fragment_record_information_family_history_tv)
    private TextView mFamilyHistoryTv;
    @FindViewById(R.id.fragment_record_information_allergic_history_tv)
    private TextView mAllergicHistoryTv;
    @FindViewById(R.id.fragment_record_information_operation_history_tv)
    private TextView mOperationHistoryTv;

    @FindViewById(R.id.fragment_record_information_height_tv)
    private TextView mHeightTv;
    @FindViewById(R.id.fragment_record_information_temperature_tv)
    private TextView mTemperatureTv;
    @FindViewById(R.id.fragment_record_information_weight_tv)
    private TextView mWeightTv;
    @FindViewById(R.id.fragment_record_information_pulse_tv)
    private TextView mPulseTv;
    @FindViewById(R.id.fragment_record_information_blood_pressure_tv)
    private TextView mBloodPressureTv;
    @FindViewById(R.id.fragment_record_information_breathing_tv)
    private TextView mBreathingTv;
    @FindViewById(R.id.fragment_record_information_check_tv)
    private TextView mCheckTv;
    /*@FindViewById(R.id.fragment_record_information_check_view_tv)
    private View mCheckView;*/
    @FindViewById(R.id.fragment_record_information_medical_treatment_ll)
    private LinearLayout mMedicalTreatmentLl;
    @FindViewById(R.id.fragment_record_information_surgical_treatment_ll)
    private LinearLayout mSurgicalTreatmentLl;
    @FindViewById(R.id.fragment_record_information_physics_treatment_ll)
    private LinearLayout mPhysicsTreatmentLl;
    @FindViewById(R.id.fragment_record_information_fragment_layout_fl)
    private FrameLayout mFrameLayout;
    @FindViewById(R.id.fragment_record_information_relevance_medical_view)
    private LinearLayout mRelevanceMedicalView;
    @FindViewById(R.id.fragment_record_information_relevance_medical_recycler_view)
    private RecyclerView mRelevanceMedicalRecyclerView;

    @AppApplication.Manager
    private ConsultManager mConsultManager;
    @AppApplication.Manager
    private BaseConfigManager mConfigManager;
    @AppApplication.Manager
    UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private RecordManager mRecordManager;

    private int appointmentId;
    private SparseArray<DoctorBaseInfo> mDoctorBaseInfoMap = new SparseArray<>();
    private AppointmentInfo mAppointmentInfo;
    private PatientInfo mPatientInfo;
    private PPAmrPlayer mPlayer;
    private int mUserType;
    private OnGetAppointmentResultListener mOnGetAppointmentResultListener;
    private OnGetPatientResultListener mOnGetPatientResultListener;

    private ConsultAssistExamineFragment mConsultAssistExamineFragment;
    private ConsultationRecordMedicalAdapter mRelevanceMedicalAdapter;


    public static ConsultCaseInfoFragment getInstance(int appointmentId, boolean isVideoRoomEnter) {
        ConsultCaseInfoFragment fragment = new ConsultCaseInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, appointmentId);
        bundle.putBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, isVideoRoomEnter);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int getAppointmentId() {
        if (appointmentId == 0) {
            appointmentId = getArguments() == null ? 0 : getArguments().getInt(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
        }
        return appointmentId;
    }

    private boolean isVideoRoomEnter() {
        return getArguments() != null && getArguments().getBoolean(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_VIDEO_ROOM_ENTER, false);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mPlayer = new PPAmrPlayer();
        mRelevanceMedicalAdapter = new ConsultationRecordMedicalAdapter(R.layout.item_consultation_record_medical, new ArrayList<>(0));
        mRelevanceMedicalAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            AppointmentItemForRelateInfo info = (AppointmentItemForRelateInfo) adapter.getItem(position);
            if (null != info) {
                Intent intent = new Intent(getBaseActivity(), PatientInformationActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_RELATE_RECORD, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, info.getRelationId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_record_information;
    }

    @Override
    public void initViews(View rootView) {
        mRelevanceMedicalRecyclerView.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        mRelevanceMedicalRecyclerView.setAdapter(mRelevanceMedicalAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        getData(getAppointmentId());
    }

    private void getData(int appointmentId) {
        Logger.logI(Logger.APPOINTMENT, "getData:appointmentId：" + appointmentId);
        if (appointmentId == 0) {
            return;
        }
        getAppointmentInfo(appointmentId);
        getPatientInfo(appointmentId);
        getAuxiliaryAssistExamine(appointmentId);
        getRecordList(appointmentId);
    }

    private void getAuxiliaryAssistExamine(int appointmentId) {
        AuxiliaryMaterialRequester requester = new AuxiliaryMaterialRequester(new DefaultResultCallback<AuxiliaryMaterialResult>() {
            @Override
            public void onSuccess(AuxiliaryMaterialResult auxiliaryMaterialResult, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "initAuxiliaryAssistExamineFragment-->auxiliaryMaterialResult:" + auxiliaryMaterialResult);
                if (null != auxiliaryMaterialResult) {
                    initAuxiliaryAssistExamineFragment(auxiliaryMaterialResult.getAuxiliaryMaterialInfos());
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.appointmentId = appointmentId;
        requester.doPost();
    }

    @OnClick({R.id.fragment_record_information_first_doctor_name_tv,
            R.id.fragment_record_information_first_doctor_hospital_department_tv,
            R.id.fragment_record_information_consult_doctor_name_tv})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.fragment_record_information_first_doctor_name_tv:
                intent.setClass(getBaseActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "doctor/info/doc_id/" + mAppointmentInfo.getBaseInfo().getAttendingDoctorUserId() + "/mark/1.html");
                startActivity(intent);
                break;

            case R.id.fragment_record_information_first_doctor_hospital_department_tv:
                showIntroductionDialog();
                break;

            case R.id.fragment_record_information_consult_doctor_name_tv:
                intent.setClass(getBaseActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "doctor/info/doc_id/" + mAppointmentInfo.getBaseInfo().getDoctorUserId() + "/mark/1.html");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void showIntroductionDialog() {
        View view = LayoutInflater.from(getBaseActivity()).inflate(R.layout.layout_dialog_hospital_introduction, null);
        TextView departmentTitleTv = view.findViewById(R.id.dialog_introduction_department_title_tv);
        TextView introductionDepartmentTv = view.findViewById(R.id.dialog_introduction_department_tv);
        TextView hospitalTitleTv = view.findViewById(R.id.dialog_introduction_hospital_title_tv);
        TextView introductionHospitalTv = view.findViewById(R.id.dialog_introduction_hospital_tv);
        Button closeBtn = view.findViewById(R.id.dialog_introduction_close_btn);
        final Dialog mIntroductionDialog = new Dialog(getBaseActivity(), R.style.custom_noActionbar_window_style);
        mIntroductionDialog.show();
        mIntroductionDialog.setContentView(view);
        mIntroductionDialog.setCanceledOnTouchOutside(true);
        mIntroductionDialog.setCancelable(true);
        Window win = mIntroductionDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        closeBtn.setOnClickListener(v -> mIntroductionDialog.dismiss());
        displayIntroductionView(introductionHospitalTv, introductionDepartmentTv, departmentTitleTv, hospitalTitleTv);
    }

    private void displayIntroductionView(final TextView introductionHospitalTv, final TextView introductionDepartmentTv, final TextView departmentTv, final TextView hospitalTv) {
        GetHospitalAndDepartmentInfoRequester requester = new GetHospitalAndDepartmentInfoRequester((baseResult, hospitalAndDepartmentInfo) -> {
            Logger.logI(Logger.COMMON, "getHospitalAndDepartmentInfo：baseResult" + baseResult + " , hospitalAndDepartmentInfo" + hospitalAndDepartmentInfo);
            if (baseResult.getCode() == 0 && hospitalAndDepartmentInfo != null) {
                if (hospitalAndDepartmentInfo.getDepartmentIntroductionInfo() != null) {
                    introductionDepartmentTv.setText(Html.fromHtml(hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getIntroduction()));
                    introductionDepartmentTv.setMovementMethod(LinkMovementMethod.getInstance());
                    String string = introductionDepartmentTv.getText().toString();
                    Logger.logI(Logger.COMMON, "displayIntroductionView：introductionDepartmentTv:" + string);
                    introductionDepartmentTv.setText(Html.fromHtml(string));
                    introductionDepartmentTv.setText(Html.fromHtml(introductionDepartmentTv.getText().toString()));
                }
                if (hospitalAndDepartmentInfo.getHospitalIntroductionInfo() != null) {
                    introductionHospitalTv.setText(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalDesc());
                    hospitalTv.setText(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName() + "简介");
                }
                departmentTv.setText(((hospitalAndDepartmentInfo.getHospitalIntroductionInfo() == null || StringUtils.isEmpty(hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName())) ? "" : hospitalAndDepartmentInfo.getHospitalIntroductionInfo().getHospitalName()) +
                        " " + ((hospitalAndDepartmentInfo.getDepartmentIntroductionInfo() == null || StringUtils.isEmpty(hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getDepartmentName())) ? "" : hospitalAndDepartmentInfo.getDepartmentIntroductionInfo().getDepartmentName() + "简介"));
            }
        });
        requester.hospitalId = mDoctorBaseInfoMap.get(mAppointmentInfo.getBaseInfo().getAttendingDoctorUserId()).getHospitalId();
        requester.hosdepId = mDoctorBaseInfoMap.get(mAppointmentInfo.getBaseInfo().getAttendingDoctorUserId()).getHosdepId();
        requester.doPost();
    }

    /**
     * 获取预约信息
     *
     * @param appointmentId
     */
    private void getAppointmentInfo(int appointmentId) {
        mConsultManager.getAppointmentInfo(appointmentId, (baseResult, appointmentInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && appointmentInfo != null && isAdded()) {
                Logger.logI(Logger.APPOINTMENT, "getAppointmentInfo->appointmentInfo:" + appointmentInfo);
                mAppointmentInfo = appointmentInfo;
                displayAppointment(appointmentInfo);
                if (mOnGetAppointmentResultListener != null) {
                    mOnGetAppointmentResultListener.onResult(appointmentInfo);
                }
            }
        });
    }

    /**
     * 获取患者信息并显示
     *
     * @param appointmentId
     */
    private void getPatientInfo(int appointmentId) {
        mConsultManager.getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null && isAdded()) {
                    mPatientInfo = patientInfo;
                    setPatient(patientInfo);
                    if (mOnGetPatientResultListener != null) {
                        mOnGetPatientResultListener.onResult(patientInfo);
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

    /**
     * 设置预约
     */
    private void displayAppointment(AppointmentInfo appointmentInfo) {
        String str;
        AppointmentBaseInfo baseInfo = appointmentInfo.getBaseInfo();
        if (baseInfo == null) {
            return;
        }
        if (StringUtils.isEmpty(baseInfo.getInsertDt()) || baseInfo.getInsertDt().contains("2099")) {
            str = getString(R.string.time_to_be_determined);
        } else {
            str = baseInfo.getInsertDt();
        }
        mApplyTimeTv.setText(str);
        ApplyDescInfo applyDescInfo = appointmentInfo.getApplyDescInfo();
        if (null == applyDescInfo) {
            return;
        }
        mConsultPurposeTv.setText(applyDescInfo.getApplyRemark());
        mPatientReadmeTv.setText(baseInfo.getPatientDesc());//患者自述
        mIllnessDigestTv.setText(applyDescInfo.getProfile());//病情摘要
        //语音自述
        if (!TextUtils.isEmpty(baseInfo.getFilePath())) {
            mVoiceReadmeLl.setVisibility(View.VISIBLE);
            final String filePath = SdManager.getInstance().getOrderVoicePath(baseInfo.getFilePath(), baseInfo.getAppointmentId() + "");
            Logger.logD(Logger.APPOINTMENT, "displayAppointment->baseInfo.getFilePath():" + baseInfo.getFilePath());
            if (!FileUtil.isFileExist(filePath)) {
                new VoiceDownloader(baseInfo.getFilePath(), baseInfo.getAppointmentId(), () -> {
                    Logger.logD(Logger.APPOINTMENT, "displayAppointment->onFinished");
                    AppHandlerProxy.runOnUIThread(() -> {
                        String time = PPAmrPlayer.getFileMs(filePath) / 1000 + "\"";
                        mVoiceReadmeTimeTv.setText(time);
                    });
                }).start();
            } else {
                String time = PPAmrPlayer.getFileMs(filePath) / 1000 + "\"";
                mVoiceReadmeTimeTv.setText(time);
            }
        } else {
            mVoiceReadmeLl.setVisibility(View.GONE);
        }
        mVoiceReadmeTimeTv.setOnClickListener(v -> dealVoice(baseInfo));

        getDoctorInfo(baseInfo);

        setUserType();
        int serviceType = AppConstant.ServiceType.SERVICE_TYPE_REMOTE_CONSULT;
        if (appointmentInfo != null && appointmentInfo.getExtendsInfo() != null) {
            serviceType = appointmentInfo.getExtendsInfo().getServiceType();
        }
        Logger.logD(Logger.APPOINTMENT, "displayAppointment->serviceType" + serviceType);
       /* if (AppConstant.ServiceType.SERVICE_TYPE_RETURN_CONSULT == serviceType ||
                AppConstant.ServiceType.SERVICE_TYPE_REMOTE_CONSULT == serviceType ||
                AppConstant.ServiceType.SERVICE_TYPE_IMAGE_CONSULT == serviceType) {
            mInfoAim.setText(getString(R.string.table_consult_purpose));
            mInfoDoctor.setText(getString(R.string.table_consult_doctor));
        } else {
            mInfoAim.setText(getString(R.string.table_advice_purpose));
            mInfoDoctor.setText(getString(R.string.table_advice_doctor));
        }*/
        mInfoAim.setText(getString(R.string.table_consult_purpose));
        mInfoDoctor.setText(getString(R.string.table_consult_doctor));
    }

    /**
     * 设置userType
     */
    private void setUserType() {
        Logger.logI(Logger.APPOINTMENT, "UserId:" + mUserInfoManager.getCurrentUserInfo().getUserId());
        Logger.logI(Logger.APPOINTMENT, "DoctorUserId:" + mAppointmentInfo.getBaseInfo().getDoctorUserId());
        Logger.logI(Logger.APPOINTMENT, "AttendingDoctorUserId:" + mAppointmentInfo.getBaseInfo().getAttendingDoctorUserId());
        Logger.logI(Logger.APPOINTMENT, "AdminId:" + mAppointmentInfo.getBaseInfo().getAdminId());
        if (mUserInfoManager.getCurrentUserInfo().getUserId() == mAppointmentInfo.getBaseInfo().getDoctorUserId()) {
            mUserType = AppConstant.DoctorType.DOCTOR_TYPE_SUPERIOR_DOCTOR;//上级专家
        } else if (mUserInfoManager.getCurrentUserInfo().getUserId() == mAppointmentInfo.getBaseInfo().getAttendingDoctorUserId()) {
            mUserType = AppConstant.DoctorType.DOCTOR_TYPE_ATTENDING_DOCTOR;//首诊医生
        } else if (mUserInfoManager.getCurrentUserInfo().getUserId() == mAppointmentInfo.getBaseInfo().getAdminId()) {
            mUserType = AppConstant.DoctorType.DOCTOR_TYPE_ASSISTANT_DOCTOR;//医生助理
        }
    }

    public int getUserType() {
        return mUserType;
    }

    /**
     * 设置医生
     *
     * @param baseInfo
     */
    private void getDoctorInfo(AppointmentBaseInfo baseInfo) {
        getAttendingDoctor(baseInfo.getAttendingDoctorUserId());
        getConsultDoctor(baseInfo.getDoctorUserId());
        getAssistDoctor(baseInfo.getAdminId());
    }

    private void getConsultDoctor(int doctorId) {
        mDoctorManager.getDoctorInfo(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo != null && !TextUtils.isEmpty(doctorBaseInfo.getRealName()) && isAdded()) {
                    mDoctorBaseInfoMap.put(doctorBaseInfo.getUserId(), doctorBaseInfo);
                    mConsultDoctorNameTv.setText(getUnderLine(doctorBaseInfo.getRealName()));
                    mConsultDoctorDepartmentTv.setText(doctorBaseInfo.getHospitalName());
                    mConsultDoctorDepartmentTv.setText(doctorBaseInfo.getHospitalName() + " " + doctorBaseInfo.getDepartmentName());
                } else if (isAdded()) {
                    mConsultDoctorNameTv.setText("");
                    mConsultDoctorDepartmentTv.setText("");
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

    private void getAttendingDoctor(int attendingDoctorUserId) {
        mDoctorManager.getDoctorInfo(attendingDoctorUserId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo != null && !TextUtils.isEmpty(doctorBaseInfo.getRealName()) && isAdded()) {
                    mDoctorBaseInfoMap.put(doctorBaseInfo.getUserId(), doctorBaseInfo);
                    mFirstDoctorNameTv.setText(getUnderLine(doctorBaseInfo.getRealName()));
                    mFirstDoctorDepartmentTv.setText(getUnderLine(doctorBaseInfo.getHospitalName()));
                    mDoctorManager.getDepartmentInfo(doctorBaseInfo.getHosdepId(), true, new DoctorManager.OnDepartmentInfoLoadListener() {
                        @Override
                        public void onSuccess(DepartmentInfo departmentInfo) {
                            if (null != departmentInfo && !StringUtils.isEmpty(departmentInfo.getDepartmentName())) {
                                mFirstDoctorDepartmentTv.setText(getUnderLine(doctorBaseInfo.getHospitalName() + " " + departmentInfo.getDepartmentName()));
                            }
                        }

                        @Override
                        public void onFailed(int code, String msg) {

                        }

                        @Override
                        public void onFinish() {

                        }
                    });

                } else if (isAdded()) {
                    mFirstDoctorNameTv.setText("");
                    mFirstDoctorDepartmentTv.setText(null);
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
     * 获取医生助理信息
     *
     * @param assistId
     */
    private void getAssistDoctor(int assistId) {
        mDoctorManager.getAssistantDoctorInfo(assistId, new DoctorManager.OnAssistantDoctorLoadListener() {
            @Override
            public void onSuccess(AssistantDoctorInfo assistantDoctorInfo) {
                if (isAdded()) {
                    mDoctorAssistantTv.setText(StringUtils.isEmpty(assistantDoctorInfo.getUserName()) ? "" : assistantDoctorInfo.getUserName());
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
     * 设置患者
     */
    private void setPatient(PatientInfo patientInfo) {
        if (patientInfo.getPatientBaseInfo() != null) {
            setPatientBase(patientInfo.getPatientBaseInfo());
        }

        if (LibCollections.isNotEmpty(patientInfo.getHistoryInfoList())) {
            setHistory(patientInfo.getHistoryInfoList());
        } else {
            hidHistoryView();
        }
        if (LibCollections.isNotEmpty(patientInfo.getDrugPlanList())) {
            setMedicalTreatment(patientInfo.getDrugPlanList());
        } else {
            mTreatmentLineView.setVisibility(View.GONE);
            mTreatmentTabView.setVisibility(View.GONE);
        }
        if (LibCollections.isNotEmpty(patientInfo.getOperationPlanList())) {
            setSurgicalTreatment(patientInfo.getOperationPlanList());
        } else {
            mSurgicalLineView.setVisibility(View.GONE);
            mSurgicalTabView.setVisibility(View.GONE);
        }
        if (LibCollections.isNotEmpty(patientInfo.getPhysicalPlanList())) {
            setPhysicsTreatment(patientInfo.getPhysicalPlanList());
        } else {
            mPhysicsTabView.setVisibility(View.GONE);
            mSituationeView.setVisibility(View.GONE);
        }

        if (mTreatmentTabView.getVisibility() == View.GONE && mSurgicalTabView.getVisibility() == View.GONE && mPhysicsTabView.getVisibility() == View.GONE && mSituationeTabView.getVisibility() == View.GONE) {
            mTreatmentPlanView.setVisibility(View.GONE);
        } else {
            mTreatmentPlanView.setVisibility(View.VISIBLE);
        }
    }

    private void hidHistoryView() {
        mPresentMedicalLineView.setVisibility(View.GONE);
        mPresentMedicalTabView.setVisibility(View.GONE);
        mAllergicLineView.setVisibility(View.GONE);
        mAllergicTabView.setVisibility(View.GONE);
        mFamilyLineView.setVisibility(View.GONE);
        mFamilyTabView.setVisibility(View.GONE);
        mPastMedicalLineView.setVisibility(View.GONE);
        mPastMedicalTabView.setVisibility(View.GONE);
        mOperationLineView.setVisibility(View.GONE);
        mOperationTabView.setVisibility(View.GONE);
        mPersonalLineView.setVisibility(View.GONE);
        mPersonalTabView.setVisibility(View.GONE);
        mObstetricLineView.setVisibility(View.GONE);
        mObstetricTabView.setVisibility(View.GONE);
        mMenstrualLineView.setVisibility(View.GONE);
        mMenstrualTabView.setVisibility(View.GONE);
    }

    /**
     * 设置患者基本信息
     *
     * @param patientBaseInfo
     */
    private void setPatientBase(PatientBaseInfo patientBaseInfo) {
        if (patientBaseInfo.getFirstCureSitu() != 0) {
            mSituationeTabView.setVisibility(View.VISIBLE);
            mSituationeTv.setText(setFirstCureSituView());
        } else {
            mSituationeTabView.setVisibility(View.GONE);
        }
        StringBuilder patientInfo = new StringBuilder();
        patientInfo.append(patientBaseInfo.getRealName());
        patientInfo.append(" ");
        patientInfo.append(patientBaseInfo.getAge().startsWith("0") ? "" : patientBaseInfo.getAge());
        mPatientNameTv.setText(patientInfo);
        mContactInformationTv.setText(patientBaseInfo.getPhoneNum());
        mConsultHospitalTv.setText(patientBaseInfo.getFirstCureHosp());
        mFirstDiagnosisTv.setText(TextUtils.isEmpty(patientBaseInfo.getFirstCureResult()) ? "" : patientBaseInfo.getFirstCureResult());//首诊诊断
        mConsultTimeTv.setText(patientBaseInfo.getRecordDt().startsWith("0000") ? "" : patientBaseInfo.getRecordDt());
        mMainNarratorTv.setText(TextUtils.isEmpty(patientBaseInfo.getFirstCureDesc()) ? "" : patientBaseInfo.getFirstCureDesc());//主述
        //体格检查
        if (patientBaseInfo.getHeight() == 0 && patientBaseInfo.getTemperature() == 0 && patientBaseInfo.getWeight() == 0 &&
                patientBaseInfo.getPluse() == 0 && patientBaseInfo.getHypertension() == 0 && patientBaseInfo.getBreath() == 0 &&
                StringUtils.isEmpty(patientBaseInfo.getSignDesc())) {
            mPhysicalExaminationView.setVisibility(View.GONE);
        } else {
            mHeightTv.setText(getString(R.string.patient_height_format, patientBaseInfo.getHeight() + ""));
            mTemperatureTv.setText(getString(R.string.patient_temperature_format, patientBaseInfo.getTemperature() + ""));
            mWeightTv.setText(getString(R.string.patient_weight_format, patientBaseInfo.getWeight() + ""));
            mPulseTv.setText(getString(R.string.patient_pluse_format, patientBaseInfo.getPluse() + ""));
            mBloodPressureTv.setText(getString(R.string.patient_blood_pressure_format, patientBaseInfo.getHypertension() + "", patientBaseInfo.getHypotension() + ""));
            mBreathingTv.setText(getString(R.string.patient_breath_format, patientBaseInfo.getBreath() + ""));
            mCheckTv.setText(patientBaseInfo.getSignDesc());
        }
    }

    /**
     * 设置病史
     *
     * @param historyInfoList
     */
    private void setHistory(List<HistoryInfo> historyInfoList) {
        for (HistoryInfo historyInfo : historyInfoList) {
            switch (historyInfo.getHistoryType()) {
                case AppConstant.HistoryType.HISTORY_TYPE_PRESENT_MEDICAL://现病史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mPresentMedicalLineView.setVisibility(View.GONE);
                        mPresentMedicalTabView.setVisibility(View.GONE);
                    } else {
                        mPresentMedicalTabView.setVisibility(View.VISIBLE);
                        mPresentMedicalLineView.setVisibility(View.VISIBLE);
                        mPresentMedicalHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_ALLERGIC://过敏史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mAllergicLineView.setVisibility(View.GONE);
                        mAllergicTabView.setVisibility(View.GONE);
                    } else {
                        mAllergicLineView.setVisibility(View.VISIBLE);
                        mAllergicTabView.setVisibility(View.VISIBLE);
                        mAllergicHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_FAMILY://家族史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mFamilyLineView.setVisibility(View.GONE);
                        mFamilyTabView.setVisibility(View.GONE);
                    } else {
                        mFamilyLineView.setVisibility(View.VISIBLE);
                        mFamilyTabView.setVisibility(View.VISIBLE);
                        mFamilyHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_PAST_MEDICAL://既往史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mPastMedicalLineView.setVisibility(View.GONE);
                        mPastMedicalTabView.setVisibility(View.GONE);
                    } else {
                        mPastMedicalLineView.setVisibility(View.VISIBLE);
                        mPastMedicalTabView.setVisibility(View.VISIBLE);
                        mPastMedicalHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_OPERATION://手术史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mOperationLineView.setVisibility(View.GONE);
                        mOperationTabView.setVisibility(View.GONE);
                    } else {
                        mOperationLineView.setVisibility(View.VISIBLE);
                        mOperationTabView.setVisibility(View.VISIBLE);
                        mOperationHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_MENSTRUAL://月经史
                    getMenstrual(historyInfo.getHistoryDesc());
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_REMARK://备注
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_PERSONAL://个人史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mPersonalLineView.setVisibility(View.GONE);
                        mPersonalTabView.setVisibility(View.GONE);
                    } else {
                        mPersonalLineView.setVisibility(View.VISIBLE);
                        mPersonalTabView.setVisibility(View.VISIBLE);
                        mPersonalHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

                case AppConstant.HistoryType.HISTORY_TYPE_OBSTETRIC://婚育史
                    if (TextUtils.isEmpty(historyInfo.getHistoryDesc())) {
                        mObstetricLineView.setVisibility(View.GONE);
                        mObstetricTabView.setVisibility(View.GONE);
                    } else {
                        mObstetricLineView.setVisibility(View.VISIBLE);
                        mObstetricTabView.setVisibility(View.VISIBLE);
                        mObstetricHistoryTv.setText(historyInfo.getHistoryDesc());
                    }
                    break;

            }
        }
    }

    /**
     * 添加月经史
     *
     * @param str
     */
    private void getMenstrual(String str) {
//        str = "13|||28|||6||||||2017-02-09";
        Logger.logD(Logger.COMMON, "ConsultCaseInfoFragment-->getMenstrual:" + str);
        if (TextUtils.isEmpty(str)) {
            mMenstrualLineView.setVisibility(View.GONE);
            mMenstrualTabView.setVisibility(View.GONE);
            return;
        }
        String[] spStr = str.split("\\|\\|\\|");
        if (spStr.length == 0) {
            mMenstrualLineView.setVisibility(View.GONE);
            mMenstrualTabView.setVisibility(View.GONE);
            return;
        }
        mMenstrualLineView.setVisibility(View.VISIBLE);
        mMenstrualTabView.setVisibility(View.VISIBLE);
        mMenstrualLl.setVisibility(View.VISIBLE);
        mMenstrualStartAgeTv.setText(spStr[0]);
        if (spStr.length >= 3) {
            mMenstrualDaysTv.setText(spStr[2]);
        }
        if (spStr.length >= 2) {
            mMenstrualCycleTv.setText(spStr[1]);
        }
        if (spStr.length >= 5) {
            mMenstrualLastTv.setText(spStr[4]);
        }
    }

    /**
     * 设置药物治疗
     *
     * @param drugPlanInfoList
     */
    private void setMedicalTreatment(List<DrugPlanInfo> drugPlanInfoList) {
        mTreatmentLineView.setVisibility(View.VISIBLE);
        mTreatmentTabView.setVisibility(View.VISIBLE);
        String tempStr1 = "";
        String tempStr2 = "";
        String tempStr3 = "";
        mMedicalTreatmentLl.removeAllViews();
        for (DrugPlanInfo drugPlanInfo : drugPlanInfoList) {
            tempStr1 += drugPlanInfo.getDrugName();
            tempStr3 += drugPlanInfo.getDrugDt();
            tempStr2 += drugPlanInfo.getDrugWay();
            View medicalTreatmentLayout = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_medical_treatment, null);
            TextView mMedicalName = (TextView) medicalTreatmentLayout.findViewById(R.id.item_medical_treatment_medical_name_tv);
            mMedicalName.setText(drugPlanInfo.getDrugName());
            TextView mDosage = (TextView) medicalTreatmentLayout.findViewById(R.id.item_medical_treatment_dosage_tv);
            mDosage.setText(drugPlanInfo.getDrugWay());
            TextView mTreatmentCourseTv = (TextView) medicalTreatmentLayout.findViewById(R.id.item_medical_treatment_treatment_course_tv);
            mTreatmentCourseTv.setText(drugPlanInfo.getDrugDt());
            if (!TextUtils.isEmpty(drugPlanInfo.getDrugName())) {
                mMedicalTreatmentLl.addView(medicalTreatmentLayout);
            }
        }
        if (StringUtils.isEmpty((tempStr1 + tempStr2 + tempStr3).trim())) {
            mSurgicalLineView.setVisibility(View.GONE);
            mSurgicalTabView.setVisibility(View.GONE);
        }

    }

    /**
     * 设置手术治疗
     *
     * @param operationPlanInfoList
     */
    private void setSurgicalTreatment(List<OperationPlanInfo> operationPlanInfoList) {
        mSurgicalLineView.setVisibility(View.VISIBLE);
        mSurgicalTabView.setVisibility(View.VISIBLE);
        String tempStr = "";
        mSurgicalTreatmentLl.removeAllViews();
        for (OperationPlanInfo operationPlanInfo : operationPlanInfoList) {
            tempStr += operationPlanInfo.getOperationWay();
            View surgicalTreatmentLayout = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_surgical_treatment, null);
            TextView mSurgicalInfo = (TextView) surgicalTreatmentLayout.findViewById(R.id.item_surgical_treatment_surgical_info_tv);
            mSurgicalInfo.setText(operationPlanInfo.getOperationWay());
            mSurgicalInfo.setVisibility(!TextUtils.isEmpty(operationPlanInfo.getOperationWay()) ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(operationPlanInfo.getOperationWay())) {
                mSurgicalTreatmentLl.addView(surgicalTreatmentLayout);
            }
        }
        if (StringUtils.isEmpty(tempStr)) {
            mSurgicalLineView.setVisibility(View.GONE);
            mSurgicalTabView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置物理治疗
     *
     * @param physicalPlanInfoList
     */
    private void setPhysicsTreatment(List<PhysicalPlanInfo> physicalPlanInfoList) {
        mPhysicsTabView.setVisibility(View.VISIBLE);
        mSituationeView.setVisibility(View.VISIBLE);
        String tempStr = "";
        mPhysicsTreatmentLl.removeAllViews();
        for (PhysicalPlanInfo physicalPlanInfo : physicalPlanInfoList) {
            tempStr += physicalPlanInfo.getPhysicaWay();
            if (!TextUtils.isEmpty(physicalPlanInfo.getPhysicaWay())) {
                View physicsTreatmentLayout = LayoutInflater.from(getBaseActivity()).inflate(R.layout.item_physics_treatment, null);
                TextView mName = physicsTreatmentLayout.findViewById(R.id.item_physics_treatment_name_tv);
                mName.setText(physicalPlanInfo.getPhysicaWay());
                mPhysicsTreatmentLl.addView(physicsTreatmentLayout);
            }
        }
        if (StringUtils.isEmpty(tempStr)) {
            mPhysicsTabView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置康复情况
     *
     * @return
     */
    private String setFirstCureSituView() {
        String cureSitu = "";
        switch (mPatientInfo.getPatientBaseInfo().getFirstCureSitu()) {
            case 1:
                cureSitu = getString(R.string.recovery_situation_good);
                break;
            case 2:
                cureSitu = getString(R.string.recovery_situation_general);
                break;
            case 3:
                cureSitu = getString(R.string.recovery_situation_bad);
                break;
        }
        return cureSitu;
    }

    /**
     * 性别
     *
     * @param gender
     * @return
     */
    private String getGender(int gender) {
        String genderStr = "";
        switch (gender) {
            case AppConstant.GenderType.GENDERTYPE_MALE:
                genderStr = getString(R.string.gender_male);
                break;

            case AppConstant.GenderType.GENDERTYPE_FEMALE:
                genderStr = getString(R.string.gender_female);
                break;
        }
        return genderStr;
    }

    public SpannableStringBuilder getUnderLine(String str) {
        if (StringUtils.isTrimEmpty(str)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableStringBuilder.setSpan(underlineSpan, 0, StringUtils.length(str), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    private void initAuxiliaryAssistExamineFragment(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        if (LibCollections.isNotEmpty(auxiliaryMaterialInfos)) {
            List<AuxiliaryMaterialInfo> auxiliaryMaterialInfosTemp = extractCheckSuccessMaterials(auxiliaryMaterialInfos);
            if (LibCollections.isNotEmpty(auxiliaryMaterialInfosTemp)) {
                mFrameLayout.setVisibility(View.VISIBLE);
                if (null == getFragmentManager()) {
                    return;
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if (mConsultAssistExamineFragment == null) {
                    mConsultAssistExamineFragment = ConsultAssistExamineFragment.getInstance(getAppointmentId(), isVideoRoomEnter(), auxiliaryMaterialInfosTemp);
                    transaction.add(R.id.fragment_record_information_fragment_layout_fl, ConsultAssistExamineFragment.getInstance(getAppointmentId(), isVideoRoomEnter(), auxiliaryMaterialInfosTemp));
                }
                transaction.show(mConsultAssistExamineFragment);
                transaction.commit();
            } else {
                mFrameLayout.setVisibility(View.GONE);
            }
        } else {
            mFrameLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 提取审核通过的图片
     *
     * @param materialInfos 所有材料
     * @return 审核通过的材料
     */
    private List<AuxiliaryMaterialInfo> extractCheckSuccessMaterials(List<AuxiliaryMaterialInfo> materialInfos) {
        List<AuxiliaryMaterialInfo> newMaterialInfos = new ArrayList<>();
        for (AuxiliaryMaterialInfo materialInfo : materialInfos) {
            if (materialInfo.getCheckState() == AppConstant.MaterialCheckState.STATE_CHECK_SUCCESS) {
                newMaterialInfos.add(materialInfo);
            }
        }
        return newMaterialInfos;
    }

    private void getRecordList(int appointmentId) {
        mRecordManager.getRelateRecords(appointmentId, (baseResult, relateRecordInfos) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (LibCollections.isNotEmpty(relateRecordInfos)) {
                    mRelevanceMedicalAdapter.setNewData(relateRecordInfos);
                } else {
                    mRelevanceMedicalView.setVisibility(View.GONE);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
    }

    private void dealVoice(AppointmentBaseInfo baseInfo) {
        //测试语音文件："201608.12.16-27-04_1470990424189711857076.amr";
        final String filePath = SdManager.getInstance().getOrderVoicePath(baseInfo.getFilePath(), baseInfo.getAppointmentId() + "");
        Logger.logI(Logger.APPOINTMENT, "dealVoice->语音时长:" + PPAmrPlayer.getFileMs(filePath));
        if (FileUtil.isFileExist(filePath)) {
            Logger.logI(Logger.APPOINTMENT, "语音文件本地路径：" + filePath);
            if (!mPlayer.isPlaying()) {
                Logger.logI(Logger.APPOINTMENT, "之前无语音播放");
                mPlayer.setDataSource(filePath);
                mPlayer.start();
                toggleVoiceAnimation(true);
            } else {
                Logger.logI(Logger.APPOINTMENT, "之前正在播放语音");
                Logger.logI(Logger.APPOINTMENT, "停止之前播放语音");
                toggleVoiceAnimation(false);
                mPlayer.stop();
            }
        } else {
            Logger.logI(Logger.APPOINTMENT, "本地无语音文件");
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                toggleVoiceAnimation(false);
            }

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            new VoiceDownloader(fileName, appointmentId, () -> {
                mPlayer.setDataSource(filePath);
                mPlayer.start();
                AppHandlerProxy.runOnUIThread(() -> toggleVoiceAnimation(true));
            }).start();
        }

        mPlayer.setOnStateListener(new PPAmrPlayer.OnStateListener() {
            @Override
            public void onStart(String s) {
            }

            @Override
            public void onStop(String s) {
                if (TextUtils.equals(s, filePath)) {
                    toggleVoiceAnimation(false);
                }
            }

            @Override
            public void onError(String s) {

            }
        });

    }

    private void toggleVoiceAnimation(boolean isOpen) {
        if (isOpen) {
            mVoiceReadmeIconIv.setImageResource(R.drawable.ic_animation_voice);
            AnimationDrawable animDrawable = (AnimationDrawable) mVoiceReadmeIconIv.getDrawable();
            animDrawable.start();
        } else {
            mVoiceReadmeIconIv.setImageResource(R.drawable.ic_voice_high);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public void setOnGetAppointmentResultListener(OnGetAppointmentResultListener mOnGetAppointmentResultListener) {
        this.mOnGetAppointmentResultListener = mOnGetAppointmentResultListener;
    }

    public void setOnGetPatientResultListener(OnGetPatientResultListener mOnGetPatientResultListener) {
        this.mOnGetPatientResultListener = mOnGetPatientResultListener;
    }

    public interface OnGetAppointmentResultListener {
        void onResult(AppointmentInfo appointmentInfo);
    }

    public interface OnGetPatientResultListener {
        void onResult(PatientInfo patientInfo);
    }
}
