package cn.longmaster.hospital.doctor.ui.consult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.MD5Util;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.DcpErrorcodeDef;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.LaunchConsultInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.DoctorScheduleListInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.remote.DoctorScheduleRequester;
import cn.longmaster.hospital.doctor.core.requests.login.CheckAccountExistRequester;
import cn.longmaster.hospital.doctor.core.requests.login.QueryAccountRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.ConsultScheduleAdapter;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 填写会诊信息
 * Created by Yang² on 2016/6/7.
 * Mod by biao on 2019/10/23
 */
public class ConsultAddActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.act_consult_add_check_type_rl)
    private RelativeLayout actConsultAddCheckTypeRl;
    @FindViewById(R.id.act_consult_add_choose_expert_tv)
    private TextView actConsultAddChooseExpertTv;
    @FindViewById(R.id.act_consult_add_choose_triage_tv)
    private TextView actConsultAddChooseTriageTv;
    @FindViewById(R.id.act_consult_add_choose_triage_iv)
    private ImageView actConsultAddChooseTriageIv;
    @FindViewById(R.id.act_consult_add_choose_type_note_tv)
    private TextView actConsultAddChooseTypeNoteTv;
    @FindViewById(R.id.act_consult_add_choose_type_note_desc_tv)
    private TextView actConsultAddChooseTypeNoteDescTv;
    @FindViewById(R.id.act_consult_add_doctor_info_rl)
    private RelativeLayout actConsultAddDoctorInfoRl;
    @FindViewById(R.id.act_consult_add_doctor_tv)
    private TextView actConsultAddDoctorTv;
    @FindViewById(R.id.act_consult_add_doctor_desc_tv)
    private TextView actConsultAddDoctorDescTv;
    @FindViewById(R.id.act_consult_add_expert_hospital_tv)
    private TextView actConsultAddExpertHospitalTv;
    @FindViewById(R.id.act_consult_add_cast_tv)
    private TextView actConsultAddCastTv;
    @FindViewById(R.id.act_consult_add_cast_desc_tv)
    private TextView actConsultAddCastDescTv;
    @FindViewById(R.id.act_consult_mould_modify_expert_iv)
    private ImageView actConsultMouldModifyExpertIv;
    @FindViewById(R.id.act_consult_add_consult_type_rl)
    private RelativeLayout actConsultAddConsultTypeRl;
    @FindViewById(R.id.act_consult_add_consult_type_v)
    private View actConsultAddConsultTypeV;
    @FindViewById(R.id.act_consult_add_consult_type_tv)
    private TextView actConsultAddConsultTypeTv;
    @FindViewById(R.id.act_consult_add_consult_type_rg)
    private RadioGroup actConsultAddConsultTypeRg;
    @FindViewById(R.id.act_consult_add_consult_remote_consult_rb)
    private RadioButton actConsultAddConsultRemoteConsultRb;
    @FindViewById(R.id.act_consult_add_consult_time_v)
    private View actConsultAddConsultTimeV;
    @FindViewById(R.id.act_consult_add_consult_time_tv)
    private TextView actConsultAddConsultTimeTv;
    @FindViewById(R.id.act_consult_add_consult_time_rv)
    private RecyclerView actConsultAddConsultTimeRv;
    @FindViewById(R.id.act_consult_add_consult_no_time_tv)
    private TextView actConsultAddConsultNoTimeTv;
    @FindViewById(R.id.act_consult_add_patient_info_v)
    private View actConsultAddPatientInfoV;
    @FindViewById(R.id.act_consult_add_patient_info_tv)
    private TextView actConsultAddPatientInfoTv;
    @FindViewById(R.id.act_consult_add_patient_name_et)
    private EditText actConsultAddPatientNameEt;
    @FindViewById(R.id.act_consult_add_patient_phone_num_et)
    private EditText actConsultAddPatientPhoneNumEt;
    @FindViewById(R.id.act_consult_add_patient_overview_v)
    private View actConsultAddPatientOverviewV;
    @FindViewById(R.id.act_consult_add_patient_overview_tv)
    private TextView actConsultAddPatientOverviewTv;
    @FindViewById(R.id.act_consult_add_patient_overview_et)
    private EditText actConsultAddPatientOverviewEt;
    @FindViewById(R.id.act_consult_add_info_next_tv)
    private TextView actConsultAddInfoNextTv;
    private final int REQUEST_CODE_FOR_DOCTOR = 200;

    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private ConsultManager mConsultManager;
    private int doctorId = 0;
    private PatientInfo mPatientInfo;
    private ProgressDialog mProgressDialog;
    private ConsultScheduleAdapter consultScheduleAdapter;
    private FormForConsult formForConsult = new FormForConsult();
    private @AppConstant.ServiceType.ScheduleServiceType
    int mServiceType = AppConstant.ServiceType.SERVICE_TYPE_RETURN_CONSULT;

    @Override
    protected void initDatas() {
        doctorId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
        mPatientInfo = (PatientInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO);
        consultScheduleAdapter = new ConsultScheduleAdapter(R.layout.item_consult_schedule_time, new ArrayList<>(0));
        consultScheduleAdapter.setOnItemClickListener((adapter, view, position) -> {
            consultScheduleAdapter.setCheck(position);
        });
        consultScheduleAdapter.setOnItemCheckedListener((adapter, position) -> {
            ScheduleOrImageInfo info = (ScheduleOrImageInfo) adapter.getItem(position);
            actConsultAddCastDescTv.setText("￥" + info.getAdmissionPrice(mServiceType));
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_consult_add;
    }

    @Override
    protected void initViews() {
        ivToolBarBack.setOnClickListener(v -> showFinishDialog());
        tvToolBarSub.setOnClickListener(v -> {
            showClearDialog();
        });
        actConsultAddChooseTriageTv.setOnClickListener(v -> {
            showClearDialog();
        });
        actConsultAddChooseExpertTv.setOnClickListener(v -> {
            getDisplay().startDoctorListActivity(false, null, REQUEST_CODE_FOR_DOCTOR);
        });
        actConsultMouldModifyExpertIv.setOnClickListener(v -> {
            getDisplay().startDoctorListActivity(false, null, REQUEST_CODE_FOR_DOCTOR);
        });
        actConsultAddInfoNextTv.setOnClickListener(v -> {
            checkInput(formForConsult, doctorId <= 0);
        });
        actConsultAddConsultTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.act_consult_add_consult_remote_advice_rb) {
                mServiceType = AppConstant.ServiceType.SERVICE_TYPE_MEDICAL_ADVICE;
                actConsultAddCastDescTv.setText("￥");
            } else if (checkedId == R.id.act_consult_add_consult_remote_consult_rb) {
                mServiceType = AppConstant.ServiceType.SERVICE_TYPE_REMOTE_CONSULT;
                actConsultAddCastDescTv.setText("￥");
            }
            getScheduleInfo(mServiceType, doctorId);
        });
        actConsultAddConsultRemoteConsultRb.setChecked(true);
        actConsultAddConsultTimeRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actConsultAddConsultTimeRv.setAdapter(consultScheduleAdapter);
        setDoctorInfo(doctorId);
        displayPatient(mPatientInfo);
    }

    private void setDoctorInfo(int doctorId) {
        consultScheduleAdapter.setNewData(null);
        updateConsultView(doctorId);
        getDoctorInfo(doctorId);
        getScheduleInfo(mServiceType, doctorId);
    }

    private void updateConsultView(int doctorId) {
        actConsultAddConsultRemoteConsultRb.setChecked(true);
        setTriageButton(doctorId);
        formForConsult = new FormForConsult();
        actConsultAddPatientOverviewEt.setText(null);
        if (doctorId > 0) {
            tvToolBarSub.setVisibility(View.VISIBLE);
            actConsultAddConsultTypeRl.setVisibility(View.VISIBLE);
            actConsultAddDoctorInfoRl.setVisibility(View.VISIBLE);
            actConsultAddCheckTypeRl.setVisibility(View.GONE);
        } else {
            setConfirmButton(true);
            tvToolBarSub.setVisibility(View.GONE);
            actConsultAddConsultTypeRl.setVisibility(View.GONE);
            actConsultAddDoctorInfoRl.setVisibility(View.GONE);
            actConsultAddCheckTypeRl.setVisibility(View.VISIBLE);
        }
    }

    private void setTriageButton(int doctorId) {
        if (doctorId > 0) {
            actConsultAddChooseTriageTv.setTextColor(ContextCompat.getColor(getThisActivity(), R.color.color_white));
            actConsultAddChooseTriageTv.setBackgroundResource(R.drawable.bg_solid_0290ef_radius_45);
            actConsultAddChooseTriageIv.setVisibility(View.GONE);
        } else {
            actConsultAddChooseTriageIv.setVisibility(View.VISIBLE);
            actConsultAddChooseTriageTv.setTextColor(ContextCompat.getColor(getThisActivity(), R.color.color_049eff));
            actConsultAddChooseTriageTv.setBackgroundResource(R.drawable.bg_solid_ffffff_radius_45);
        }
    }

    private void setConfirmButton(boolean isInit) {
        if (isInit) {
            actConsultAddInfoNextTv.setText("下一步");
            actConsultAddInfoNextTv.setBackgroundResource(R.drawable.bg_solid_049eff_radius_45);
        } else {
            actConsultAddInfoNextTv.setText("分诊中心推荐");
            actConsultAddInfoNextTv.setBackgroundResource(R.drawable.bg_solid_ff9a01_radius_45);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_DOCTOR) {
                if (data != null) {
                    doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
                    formForConsult = new FormForConsult();
                } else {
                    doctorId = 0;
                }
                setDoctorInfo(doctorId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        showFinishDialog();
    }

    /**
     * 输入检查
     *
     * @return
     */
    private void checkInput(FormForConsult formForConsult, boolean isTriage) {
        if (TextUtils.isEmpty(getString(actConsultAddPatientNameEt))) {
            ToastUtils.showShort(R.string.patient_name_tip);
            return;
        }
        if (TextUtils.isEmpty(getString(actConsultAddPatientPhoneNumEt))) {
            ToastUtils.showShort(R.string.patient_phone_num_tip);
            return;
        }
        if (!PhoneUtil.isPhoneNumber(getString(actConsultAddPatientPhoneNumEt))) {
            ToastUtils.showShort(R.string.patient_phone_num_error_format_tip);
            return;
        }
        if (TextUtils.isEmpty(getString(actConsultAddPatientOverviewEt))) {
            ToastUtils.showShort(R.string.patient_summarize_tip);
            return;
        }
        if (!isTriage) {
            if (formForConsult.getDoctorBaseInfo() == null) {
                ToastUtils.showShort("请等待获取医生信息");
                return;
            }
        } else {
            formForConsult.setDoctorBaseInfo(null);
            formForConsult.setScheduleOrImageInfo(null);
        }
        formForConsult.setScheduleOrImageInfo(consultScheduleAdapter.getChecked());
        formForConsult.setRealName(getString(actConsultAddPatientNameEt));
        formForConsult.setPhoneNum(getString(actConsultAddPatientPhoneNumEt));
        formForConsult.setPatientDesc(getString(actConsultAddPatientOverviewEt));
        formForConsult.setSource(4);
        formForConsult.setAttdocId(mUserInfoManager.getCurrentUserInfo().getUserId());
        formForConsult.setServiceType(mServiceType);
        formForConsult.setScheduingType(AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT);
        formForConsult.setTopAppointId(0);
        formForConsult.setSuperiorAppointId(0);
        showConfirmDialog(formForConsult);
    }

    /**
     * 显示确认弹窗
     */
    private void showConfirmDialog(FormForConsult formForConsult) {
        ConsultAddConfirmDialog dialog = ConsultAddConfirmDialog.getInstance(formForConsult);
        dialog.setListener(new ConsultAddConfirmDialog.OnConfirmDialogClickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                checkAccountExit(formForConsult);
            }
        });
        dialog.show(getSupportFragmentManager(), "consult_confirm_dialog");
    }

    /**
     * 判断号码是否存在及账号类型
     */
    private void checkAccountExit(FormForConsult formForConsult) {
        CheckAccountExistRequester requester = new CheckAccountExistRequester((baseResult, checkAccountInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                int accountType = checkAccountInfo.getIsDoctor();
                if (accountType == 1 || accountType == 2 || accountType == 3) {
                    new CommonDialog.Builder(getThisActivity()).setMessage("患者联系电话不能填写医生或者代表的电话")
                            .setPositiveButton("我知道了", () -> actConsultAddPatientPhoneNumEt.setFocusable(true)).show();
                    return;
                }
                formForConsult.setPatientUserId(checkAccountInfo.getUserId());
                submitConsult(formForConsult);
            } else if (baseResult.getReason() == DcpErrorcodeDef.RET_ACCOUNT_NOT_EXISTS) {
                queryAccount(formForConsult);
            } else {
                ToastUtils.showShort(R.string.data_upload_faild);
            }
        });
        requester.userName = "86" + formForConsult.getPhoneNum();
        requester.accountType = AppConstant.UserAccountType.ACCOUNT_PHONE_NUMBER;
        requester.doPost();
    }

    /**
     * 注册账号
     */
    private void queryAccount(FormForConsult formForConsult) {
        String phoneNum = formForConsult.getPhoneNum();
        QueryAccountRequester requester = new QueryAccountRequester((baseResult, userResultInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                formForConsult.setPatientUserId(userResultInfo.getUserID());
                submitConsult(formForConsult);
            } else {
                ToastUtils.showShort(R.string.data_upload_faild);
            }
        });
        requester.account = "86" + phoneNum;
        requester.accountType = AppConstant.UserAccountType.ACCOUNT_PHONE_NUMBER;
        requester.pwd = MD5Util.md5(phoneNum.substring(phoneNum.length() - 6));
        requester.doPost();
    }

    private void getDoctorInfo(int doctorId) {
        if (doctorId == 0) {
            return;
        }
        mDoctorManager.getDoctorInfo(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                displayDoctorInfo(doctorId, doctorBaseInfo);
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showShort(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 获取医生排班信息 requester
     *
     * @param doctorId
     */
    private void getScheduleInfo(int serviceType, int doctorId) {
        consultScheduleAdapter.setNewData(null);
        actConsultAddCastDescTv.setText("￥待定");
        if (doctorId <= 0) {
            return;
        }
        DoctorScheduleRequester requester = new DoctorScheduleRequester(new DefaultResultCallback<DoctorScheduleListInfo>() {
            @Override
            public void onSuccess(DoctorScheduleListInfo doctorScheduleListInfo, BaseResult baseResult) {
                displayDoctorSchedule(doctorScheduleListInfo.getLimitScheduleList());
            }
        });
        requester.setDoctorId(doctorId);
        requester.setScheduleType(AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT);
        requester.setFilterType(DoctorScheduleRequester.FILTER_TYPE_LIMIT);
        requester.setServiceType(serviceType);
        requester.doPost();
    }

    /**
     * 发起会诊
     */
    private void submitConsult(FormForConsult formForConsult) {
        mProgressDialog = createProgressDialog(getString(R.string.data_uploading));
        mConsultManager.addConsult(formForConsult, new DefaultResultCallback<LaunchConsultInfo>() {
            @Override
            public void onSuccess(LaunchConsultInfo launchConsultInfo, BaseResult baseResult) {
                formForConsult.setAppointmentId(launchConsultInfo.getAppointmentId());
                formForConsult.setPayPassword(launchConsultInfo.getPayPassword());
                formForConsult.setSerialNumber(launchConsultInfo.getSerialNumber());
                formForConsult.setPredictCureDt(launchConsultInfo.getPredictCureDt());
                getDisplay().startSubmitSuccessfulActivity(formForConsult, 0);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == -105) {
                    showFailDialog();
                } else {
                    ToastUtils.showShort(R.string.data_upload_faild);
                }
            }

            @Override
            public void onFinish() {
                mProgressDialog.cancel();
            }
        });
    }

    /**
     * 填充医生信息
     *
     * @param doctorId
     * @param doctorBaseInfo
     */
    private void displayDoctorInfo(int doctorId, DoctorBaseInfo doctorBaseInfo) {
        if (doctorId > 0) {
            formForConsult.setDoctorBaseInfo(doctorBaseInfo);
            actConsultAddDoctorDescTv.setText(doctorBaseInfo.getRealName());
            actConsultAddExpertHospitalTv.setText(doctorBaseInfo.getHospitalName() + " " + doctorBaseInfo.getDepartmentName() + " " + doctorBaseInfo.getDoctorLevel());
        } else if (doctorId < 0) {
            StringBuilder overView = new StringBuilder();
            overView.append("您当前选择的医生：");
            overView.append(doctorBaseInfo.getHospitalName());
            overView.append(doctorBaseInfo.getDepartmentName());
            overView.append(doctorBaseInfo.getDoctorLevel());
            overView.append(doctorBaseInfo.getRealName());
            overView.append("(未入驻);我们将尽快与该医生进行沟通，并在两小时内以电话的形式给您回复，您可补充患者病情，会诊诉求与意向就诊医院等..");
            actConsultAddPatientOverviewEt.setText(overView);
        } else {
            formForConsult.setDoctorBaseInfo(doctorBaseInfo);
            actConsultAddDoctorDescTv.setText(null);
            actConsultAddExpertHospitalTv.setText(null);
        }
    }

    /**
     * 填充患者信息
     */
    private void displayPatient(PatientInfo patientInfo) {
        if (patientInfo != null) {
            actConsultAddPatientNameEt.setText(patientInfo.getPatientBaseInfo().getRealName());
            actConsultAddPatientPhoneNumEt.setText(patientInfo.getPatientBaseInfo().getPhoneNum());
        }
    }

    /**
     * 填充医生排班
     *
     * @param scheduleOrImageInfos
     */
    private void displayDoctorSchedule(List<ScheduleOrImageInfo> scheduleOrImageInfos) {
        if (LibCollections.isEmpty(scheduleOrImageInfos)) {
            //无排班
            actConsultAddConsultNoTimeTv.setVisibility(View.VISIBLE);
            actConsultAddConsultTimeRv.setVisibility(View.GONE);
            setConfirmButton(false);
        } else {
            setConfirmButton(true);
            actConsultAddConsultNoTimeTv.setVisibility(View.GONE);
            actConsultAddConsultTimeRv.setVisibility(View.VISIBLE);
            consultScheduleAdapter.setNewData(scheduleOrImageInfos);
            consultScheduleAdapter.setCheck(0);
        }
    }

    private void showClearDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage("点击确认进入分诊中心页面，分诊中心将为您分配合适的专家，当前页面的信息将会清除")
                .setPositiveButton(R.string.cancel, () -> {

                })
                .setNegativeButton(R.string.confirm, () -> {
                    doctorId = 0;
                    setDoctorInfo(doctorId);
                }).show();
    }

    private void showFailDialog() {
        new CommonDialog.Builder(getThisActivity()).setTitle(R.string.consult_confirm_fail)
                .setMessage(R.string.consult_confirm_fail_text)
                .setNegativeButton(R.string.consult_confirm_fail_reselect, () -> {

                }).show();
    }

    /**
     * 显示结束预约弹框
     */
    private void showFinishDialog() {
        new CommonDialog.Builder(this)
                .setTitle(R.string.fill_consult_dialog_finish_title)
                .setMessage(R.string.fill_consult_dialog_finish_message)
                .setPositiveButton(R.string.fill_consult_dialog_cancel, () -> {

                })
                .setNegativeButton(R.string.fill_consult_dialog_ok, this::finish)
                .show();
    }
}
