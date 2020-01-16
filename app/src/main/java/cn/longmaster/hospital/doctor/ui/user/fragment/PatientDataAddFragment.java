package cn.longmaster.hospital.doctor.ui.user.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.CommonUtils;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.CheckRoundsMedicalRequester;
import cn.longmaster.hospital.doctor.core.requests.user.PatientHospitalListGetRequest;
import cn.longmaster.hospital.doctor.core.requests.user.PatientVerifyRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.utils.KeyboardUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/9 15:23
 * @description:
 */
public class PatientDataAddFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_upload_data_upload_tv)
    private TextView fragmentUploadDataUploadTv;
    @FindViewById(R.id.fragment_upload_data_upload_iv)
    private ImageView fragmentUploadDataUploadIv;
    @FindViewById(R.id.fragment_upload_data_patient_name_tv)
    private TextView fragmentUploadDataPatientNameTv;
    @FindViewById(R.id.fragment_upload_data_patient_name_et)
    private EditText fragmentUploadDataPatientNameEt;
    @FindViewById(R.id.fragment_upload_data_patient_name_v)
    private View fragmentUploadDataPatientNameV;
    @FindViewById(R.id.fragment_upload_data_upload_type_tv)
    private TextView fragmentUploadDataUploadTypeTv;
    @FindViewById(R.id.fragment_upload_data_type_rg)
    private RadioGroup fragmentUploadDataTypeRg;
    @FindViewById(R.id.fragment_upload_data_by_in_hospital_num_rb)
    private RadioButton fragmentUploadDataByInHospitalNumRb;
    @FindViewById(R.id.fragment_upload_data_by_appointment_number_rb)
    private RadioButton fragmentUploadDataByAppointmentNumberRb;
    @FindViewById(R.id.fragment_upload_data_by_in_hospital_num_rl)
    private RelativeLayout fragmentUploadDataByInHospitalNumRl;
    @FindViewById(R.id.fragment_upload_data_in_hospital_num_tv)
    private TextView fragmentUploadDataInHospitalNumTv;
    @FindViewById(R.id.fragment_upload_data_in_hospital_num_et)
    private EditText fragmentUploadDataInHospitalNumEt;
    @FindViewById(R.id.fragment_upload_data_in_hospital_num_v)
    private View fragmentUploadDataInHospitalNumV;
    @FindViewById(R.id.fragment_upload_data_hospital_name_tv)
    private TextView fragmentUploadDataHospitalNameTv;
    @FindViewById(R.id.fragment_upload_data_hospital_name_desc_tv)
    private EditText fragmentUploadDataHospitalNameDescTv;
    @FindViewById(R.id.fragment_upload_data_appointment_number_ll)
    private LinearLayout fragmentUploadDataAppointmentNumberLl;
    @FindViewById(R.id.fragment_upload_data_appointment_number_tv)
    private TextView fragmentUploadDataAppointmentNumberTv;
    @FindViewById(R.id.fragment_upload_data_appointment_number_et)
    private EditText fragmentUploadDataAppointmentNumberEt;
    @FindViewById(R.id.fragment_upload_data_determine_tv)
    private TextView fragmentUploadDataDetermineTv;

    private boolean isIntent = false;
    private ProgressDialog mProgressDialog;
    private List<HospitalInfo> mHospitalInfos = new ArrayList<>();
    @AppApplication.Manager
    private ConsultManager mConsultManager;
    private boolean isUploadByMedicalId;
    private final int REQUEST_CODE_FOR_UPLOAD_SUCCESS = 126;
    private ListPopupWindow mListPop;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_patient_data_add;
    }

    @Override
    public void initViews(View rootView) {
        KeyboardUtils.registerSoftInputChangedListener(getBaseActivity(), height -> {
            if (height <= 0) {
                if (!StringUtils.isTrimEmpty(getString(fragmentUploadDataPatientNameEt)) && !StringUtils.isTrimEmpty(getString(fragmentUploadDataInHospitalNumEt))) {
                    getPatientHospitalInfo(getString(fragmentUploadDataPatientNameEt), getString(fragmentUploadDataInHospitalNumEt));
                }
            }
        });
        fragmentUploadDataDetermineTv.setOnClickListener(v -> {
            String patientName = getString(fragmentUploadDataPatientNameEt);
            if (StringUtils.isTrimEmpty(patientName)) {
                ToastUtils.showShort(getString(R.string.upload_data_input_correct_information));
                return;
            }
            if (isUploadByMedicalId) {
                uploadByMedical(patientName, getString(fragmentUploadDataAppointmentNumberEt));
            } else {
                uploadByInHospitalNum(patientName, getString(fragmentUploadDataInHospitalNumEt), getString(fragmentUploadDataHospitalNameDescTv));
            }
        });
        fragmentUploadDataTypeRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.fragment_upload_data_by_in_hospital_num_rb:
                    fragmentUploadDataAppointmentNumberEt.setText(null);
                    isUploadByMedicalId = false;
                    fragmentUploadDataByInHospitalNumRl.setVisibility(View.VISIBLE);
                    fragmentUploadDataAppointmentNumberLl.setVisibility(View.GONE);
                    break;
                case R.id.fragment_upload_data_by_appointment_number_rb:
                    isUploadByMedicalId = true;
                    fragmentUploadDataInHospitalNumEt.setText(null);
                    fragmentUploadDataByInHospitalNumRl.setVisibility(View.GONE);
                    fragmentUploadDataAppointmentNumberLl.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        });
        fragmentUploadDataUploadIv.setOnClickListener(v -> {
            getDisplay().startPatientDataListActivity(0);
        });

        fragmentUploadDataPatientNameEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                CommonUtils.hideSoftInput(v);
                fragmentUploadDataHospitalNameDescTv.setText(null);
                getPatientHospitalInfo(getString(fragmentUploadDataPatientNameEt), getString(fragmentUploadDataInHospitalNumEt));
                return true;
            }
            return false;
        });

        fragmentUploadDataInHospitalNumEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                CommonUtils.hideSoftInput(v);
                fragmentUploadDataHospitalNameDescTv.setText(null);
                getPatientHospitalInfo(getString(fragmentUploadDataPatientNameEt), getString(fragmentUploadDataInHospitalNumEt));
                return true;
            }
            return false;
        });
        fragmentUploadDataHospitalNameDescTv.setOnClickListener(v -> {
            if (LibCollections.size(mHospitalInfos) > 1) {
                showChooseHospital(mHospitalInfos);
            }
        });
    }

    private void getPatientHospitalInfo(String patientName, String inHospitalNum) {
        if (StringUtils.isTrimEmpty(patientName) || StringUtils.isTrimEmpty(inHospitalNum)) {
            return;
        }
        PatientHospitalListGetRequest request = new PatientHospitalListGetRequest(new DefaultResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(hospitalInfos)) {
                    fragmentUploadDataHospitalNameDescTv.setText(hospitalInfos.get(0).getHospitalName());
                }
                if (LibCollections.size(hospitalInfos) > 1) {
                    showChooseHospital(hospitalInfos);
                }
                Logger.logD(TAG, baseResult.toString());
            }
        });
        request.setHospitalizaId(inHospitalNum);
        request.setPatientName(patientName);
        request.start();
    }

    private void showChooseHospital(List<HospitalInfo> hospitalInfos) {
        if (mListPop == null) {
            mListPop = new ListPopupWindow(getBaseActivity());
        }
        if (LibCollections.isEmpty(mHospitalInfos)) {
            mHospitalInfos = hospitalInfos;
        }
        List<String> lists = createItem(mHospitalInfos);
        mListPop.setAdapter(new ArrayAdapter<>(getBaseActivity(), android.R.layout.simple_list_item_1, lists));
        mListPop.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        mListPop.setAnchorView(fragmentUploadDataHospitalNameDescTv);//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
        mListPop.setModal(true);//设置是否是模式
        mListPop.setOnItemClickListener((parent, view, position, id) -> {
            fragmentUploadDataHospitalNameDescTv.setText(lists.get(position));
            mListPop.dismiss();
        });
        mListPop.show();
    }

    private List<String> createItem(List<HospitalInfo> hospitalInfos) {
        List<String> results = new ArrayList<>();
        if (LibCollections.isNotEmpty(hospitalInfos)) {
            for (HospitalInfo hospitalInfo : hospitalInfos) {
                results.add(hospitalInfo.getHospitalName());
            }
        }
        return results;
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
        PatientVerifyRequester requester = new PatientVerifyRequester(new OnResultCallback<Void>() {
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
                dismissProgressDialog();
            }

            @Override
            public void onFail(BaseResult baseResult) {
                ToastUtils.showShort(getString(R.string.upload_data_input_correct_information));
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
        if (isDetached()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (isDetached()) {
            return;
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void startDataManageActivity(PatientInfo patientInfo, AppointmentInfo appointmentInfo) {
        Intent intent = new Intent();
        intent.setClass(getBaseActivity(), ConsultDataManageActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_INFO, patientInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_INFO, appointmentInfo);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_SECOND_TEXT, false);
        startActivity(intent);
    }
}
