package cn.longmaster.hospital.doctor.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.CheckRoundsMedicalRequester;
import cn.longmaster.hospital.doctor.core.requests.user.PatientVerifyRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Mod by Biao on 2019/7/2
 * 上传资料Activity
 */
public class PatientDataUploadActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.activity_upload_data_patient_name_et)
    private EditText activityUploadDataPatientNameEt;
    @FindViewById(R.id.activity_upload_data_type_rg)
    private RadioGroup activityUploadDataTypeRg;
    @FindViewById(R.id.activity_upload_data_by_in_hospital_num_rl)
    private RelativeLayout activityUploadDataByInHospitalNumRl;
    @FindViewById(R.id.activity_upload_data_in_hospital_num_et)
    private EditText activityUploadDataInHospitalNumEt;
    @FindViewById(R.id.activity_upload_data_hospital_name_et)
    private EditText activityUploadDataHospitalNameEt;
    @FindViewById(R.id.activity_upload_data_appointment_number_ll)
    private LinearLayout activityUploadDataAppointmentNumberLl;
    @FindViewById(R.id.activity_upload_data_appointment_number_et)
    private EditText activityUploadDataAppointmentNumberEt;
    @FindViewById(R.id.activity_upload_data_determine_tv)
    private TextView activityUploadDataDetermineTv;

    private boolean isIntent = false;
    private ProgressDialog mProgressDialog;

    @AppApplication.Manager
    private ConsultManager mConsultManager;
    private boolean isUploadByMedicalId;
    private final int REQUEST_CODE_FOR_UPLOAD_SUCCESS = 126;

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload_data;
    }

    @Override
    protected void initViews() {
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        tvToolBarTitle.setText("上传患者资料");
        activityUploadDataDetermineTv.setOnClickListener(v -> {
            String patientName = getString(activityUploadDataPatientNameEt);
            if (StringUtils.isTrimEmpty(patientName)) {
                ToastUtils.showShort(getString(R.string.upload_data_input_correct_information));
                return;
            }
            if (isUploadByMedicalId) {
                uploadByMedical(patientName, getString(activityUploadDataAppointmentNumberEt));
            } else {
                uploadByInHospitalNum(patientName, getString(activityUploadDataInHospitalNumEt), getString(activityUploadDataHospitalNameEt));
            }
        });
        activityUploadDataTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.activity_upload_data_by_in_hospital_num_rb:
                    activityUploadDataAppointmentNumberEt.setText(null);
                    isUploadByMedicalId = false;
                    activityUploadDataByInHospitalNumRl.setVisibility(View.VISIBLE);
                    activityUploadDataAppointmentNumberLl.setVisibility(View.GONE);
                    break;
                case R.id.activity_upload_data_by_appointment_number_rb:
                    isUploadByMedicalId = true;
                    activityUploadDataInHospitalNumEt.setText(null);
                    activityUploadDataHospitalNameEt.setText(null);
                    activityUploadDataByInHospitalNumRl.setVisibility(View.GONE);
                    activityUploadDataAppointmentNumberLl.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * @param patientName
     * @param inHospitalNum
     * @param hospitalName
     */
    private void uploadByInHospitalNum(String patientName, String inHospitalNum, String hospitalName) {
        if (StringUtils.isTrimEmpty(inHospitalNum)) {
            ToastUtils.showShort("请输入医院住院号");
            return;
        }
        if (StringUtils.isContainChinese(inHospitalNum)) {
            ToastUtils.showShort("住院号不能包含汉字");
            return;
        }
        showProgressDialog();
        checkRoundsAppointment(patientName, inHospitalNum, hospitalName, 1);
    }

    /**
     * @param patientName
     * @param medicalId
     */
    private void uploadByMedical(String patientName, String medicalId) {
        if (StringUtils.isTrimEmpty(medicalId)) {
            ToastUtils.showShort("请输入病例号");
            return;
        }
        showProgressDialog();
        float appointmentId = StringUtils.str2Float(medicalId);
        //大于这个是查房校验
        if (appointmentId >= 500000) {
            checkRoundsAppointment(patientName, medicalId, null, 0);
        } else {
            checkConsultationAppointment(patientName, medicalId);
        }
    }

    /**
     * 校验查房
     *
     * @param patientName  患者姓名
     * @param medicalId    根据(type 0:表示病例号,1表示住院号)
     * @param hospitalName 医院名称
     * @param type         0校验病例号,1校验住院号
     */
    private void checkRoundsAppointment(final String patientName, final String medicalId, String hospitalName, int type) {
        CheckRoundsMedicalRequester requester = new CheckRoundsMedicalRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo medicalDetailsInfo, BaseResult msg) {
                getDisplay().startRoundsDataManagerActivity(medicalDetailsInfo.getMedicalId(), false, false, REQUEST_CODE_FOR_UPLOAD_SUCCESS);
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                if (result.getCode() == -102) {
                    ToastUtils.showShort("该患者不存在");
                } else if (result.getCode() == 102) {
                    ToastUtils.showShort("该住院号下存在同名患者，请输入医院名称！");
                } else {
                    ToastUtils.showShort(R.string.no_network_connection);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
        requester.setHospitalName(hospitalName);
        requester.setType(type);
        requester.setPatientName(patientName);
        requester.setMedicalId(medicalId);
        requester.start();
    }

    /**
     * 校验会诊
     *
     * @param appointmentId
     * @param patient
     */
    private void checkConsultationAppointment(String patient, final String appointmentId) {
        PatientVerifyRequester requester = new PatientVerifyRequester(new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "PatientVerifyRequester-->baseResult.getCode():" + baseResult.getCode() + "appointmentId" + appointmentId);
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    intentDataManageActivity(StringUtils.str2Integer(appointmentId));
                    isIntent = false;
                } else {
                    ToastUtils.showShort(getString(R.string.upload_data_input_correct_information));
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
            }
        });
        requester.setMedicalId(appointmentId);
        requester.setPatientName(patient);
        requester.doPost();
    }

    private void intentDataManageActivity(final int appointmentId) {
        mConsultManager.getAppointmentInfo(appointmentId, (baseResult, appointmentInfo) -> {
            Logger.logD(Logger.COMMON, "PatientVerifyRequester-->appointmentInfo: " + appointmentInfo);
            if (appointmentInfo != null) {
                getPatientInfo(appointmentInfo, appointmentId);
            }
        });
    }

    public void getPatientInfo(AppointmentInfo appointmentInfo, int appointmentId) {
        mConsultManager.getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                Logger.logD(Logger.COMMON, "PatientVerifyRequester-->patientInfo: " + patientInfo);
                if (patientInfo != null) {
                    if (!isIntent) {
                        isIntent = true;
                        startDataManageActivity(patientInfo, appointmentInfo);
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

    private void showProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void startDataManageActivity(PatientInfo patientInfo, AppointmentInfo appointmentInfo) {
        Intent intent = new Intent();
        intent.setClass(PatientDataUploadActivity.this, ConsultDataManageActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, patientInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, appointmentInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_SECOND_TEXT, false);
        startActivity(intent);
    }
}
