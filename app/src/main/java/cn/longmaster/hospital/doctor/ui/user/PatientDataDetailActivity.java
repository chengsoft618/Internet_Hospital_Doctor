package cn.longmaster.hospital.doctor.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.DischargedSummaryInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.entity.user.PatientDataInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.rounds.PatientManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.PatientDataManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.appointment.AppointmentByIdRequester;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.DischargedSummaryAdapter;
import cn.longmaster.hospital.doctor.ui.user.adapter.PatientDataPicManagerAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/4 14:27
 * @description: 患者材料详情Activity
 */
public class PatientDataDetailActivity extends NewBaseActivity {
    @FindViewById(R.id.tool_bar_base)
    private Toolbar toolBarBase;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.activity_patient_data_detail_name_tv)
    private TextView activityPatientDataDetailNameTv;
    @FindViewById(R.id.activity_patient_data_detail_hospital_name_tv)
    private TextView activityPatientDataDetailHospitalNameTv;
    @FindViewById(R.id.activity_patient_data_detail_medical_id_tv)
    private TextView activityPatientDataDetailMedicalIdTv;
    @FindViewById(R.id.activity_patient_data_detail_medical_id_desc_tv)
    private TextView activityPatientDataDetailMedicalIdDescTv;
    @FindViewById(R.id.activity_patient_data_detail_in_hospital_num_tv)
    private TextView activityPatientDataDetailInHospitalNumTv;
    @FindViewById(R.id.activity_patient_data_detail_in_hospital_num_desc_tv)
    private TextView activityPatientDataDetailInHospitalNumDescTv;
    @FindViewById(R.id.activity_patient_data_detail_patient_pics_rv)
    private RecyclerView activityPatientDataDetailPatientPicsRv;
    @FindViewById(R.id.activity_patient_data_detail_patient_first_pics_ll)
    private LinearLayout activityPatientDataDetailPatientFirstPicsLl;
    @FindViewById(R.id.activity_patient_data_detail_patient_first_pics_rv)
    private RecyclerView activityPatientDataDetailPatientFirstPicsRv;
    @FindViewById(R.id.activity_patient_data_detail_patient_summary_pics_ll)
    private LinearLayout activityPatientDataDetailPatientSummaryPicsLl;
    @FindViewById(R.id.activity_patient_data_detail_patient_summary_pics_rv)
    private RecyclerView activityPatientDataDetailPatientSummaryPicsRv;
    @FindViewById(R.id.activity_patient_data_detail_up_btn)
    private Button activityPatientDataDetailUpBtn;
    //病案首页
    private PatientDataPicManagerAdapter firstPatientDataPicManagerAdapter;
    //其他
    private PatientDataPicManagerAdapter otherPatientDataPicManagerAdapter;
    //出院小结
    private DischargedSummaryAdapter summaryPatientDataPicManagerAdapter;
    @AppApplication.Manager
    private PatientDataManager mPatientDataManager;
    @AppApplication.Manager
    private PatientManager mPatientManager;
    private String medicalId;
    private final int REQUEST_CODE_FOR_REFRESH = 134;
    private final int REQUEST_CODE_FOR_VIEW_DIS = 1024;
    @AppApplication.Manager
    private ConsultManager mConsultManager;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        medicalId = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_MEDICAL_ID);
    }

    @Override
    protected void initDatas() {
        firstPatientDataPicManagerAdapter = new PatientDataPicManagerAdapter(R.layout.item_patient_data_manager_pics, new ArrayList<>(0));
        firstPatientDataPicManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            AuxiliaryMaterialInfo materialInfo = (AuxiliaryMaterialInfo) adapter.getItem(position);
            openPicDisplay(adapter.getData(), otherPatientDataPicManagerAdapter.getData(), materialInfo, position + LibCollections.size(otherPatientDataPicManagerAdapter.getData()));
        });
        otherPatientDataPicManagerAdapter = new PatientDataPicManagerAdapter(R.layout.item_patient_data_manager_pics, new ArrayList<>(0));
        otherPatientDataPicManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            AuxiliaryMaterialInfo materialInfo = (AuxiliaryMaterialInfo) adapter.getItem(position);
            openPicDisplay(firstPatientDataPicManagerAdapter.getData(), adapter.getData(), materialInfo, position);
        });
        summaryPatientDataPicManagerAdapter = new DischargedSummaryAdapter(R.layout.item_discharged_summary_pics, new ArrayList<>(0));
        summaryPatientDataPicManagerAdapter.setOnItemClickListener((adapter, view, position) -> {
            DischargedPicBrowserActivity.start(getThisActivity(), (ArrayList<DischargedSummaryInfo>) adapter.getData(), medicalId, position, REQUEST_CODE_FOR_VIEW_DIS);
        });
    }

    private void openPicDisplay(List<AuxiliaryMaterialInfo> firstMaterials, List<AuxiliaryMaterialInfo> otherMaterials, AuxiliaryMaterialInfo info, int position) {
        List<AuxiliaryMaterialInfo> materialInfos = new ArrayList<>();
        materialInfos.addAll(otherMaterials);
        materialInfos.addAll(firstMaterials);
        if (null != info) {
            List<String> serverFilePaths = new ArrayList<>();
            List<SingleFileInfo> singleFileInfos = new ArrayList<>();
            if (LibCollections.isNotEmpty(materialInfos)) {
                for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : materialInfos) {
                    SingleFileInfo singleFileInfo = new SingleFileInfo("0", SdManager.getInstance().getOrderPicPath(auxiliaryMaterialInfo.getMaterialPic(), auxiliaryMaterialInfo.getAppointmentId() + ""));
                    singleFileInfo.setLocalFileName(auxiliaryMaterialInfo.getMaterialPic());
                    singleFileInfo.setLocalFilePath(SdManager.getInstance().getOrderPicPath(auxiliaryMaterialInfo.getMaterialPic(), auxiliaryMaterialInfo.getAppointmentId() + ""));
                    singleFileInfo.setServerFileName(auxiliaryMaterialInfo.getMaterialPic());
                    singleFileInfo.setIsFromServer(true);
                    singleFileInfo.setAppointmentId(auxiliaryMaterialInfo.getAppointmentId());
                    singleFileInfo.setMaterialId(auxiliaryMaterialInfo.getMaterialId());
                    singleFileInfo.setCheckState(auxiliaryMaterialInfo.getCheckState());
                    singleFileInfo.setAuditDesc(auxiliaryMaterialInfo.getAuditDesc());
                    singleFileInfo.setMaterailType(auxiliaryMaterialInfo.getMaterialType());
                    singleFileInfo.setMediaType(auxiliaryMaterialInfo.getMediaType());
                    singleFileInfo.setDicom(auxiliaryMaterialInfo.getDicom());
                    singleFileInfo.setMaterialName(auxiliaryMaterialInfo.getMaterialName());
                    singleFileInfo.setMaterialResult(auxiliaryMaterialInfo.getMaterialResult());
                    singleFileInfo.setMaterialDt(auxiliaryMaterialInfo.getMaterialDt());
                    singleFileInfo.setMaterialHosp(auxiliaryMaterialInfo.getMaterialHosp());
                    singleFileInfos.add(singleFileInfo);
                    serverFilePaths.add(AppConfig.getMaterialDownloadUrl() + auxiliaryMaterialInfo.getMaterialPic());
                }
            }
            BrowserPicEvent browserPicEvent = new BrowserPicEvent();
            browserPicEvent.setAppointInfoId(info.getMedicalId());
            browserPicEvent.setServerFilePaths(serverFilePaths);
            browserPicEvent.setAuxiliaryMaterialInfos(materialInfos);
            browserPicEvent.setIndex(position);
            browserPicEvent.setSingleFileInfos(singleFileInfos);
            browserPicEvent.setRounds(true);
            //browserPicEvent.setAssistant(true);
            browserPicEvent.setPass(true);
            getDisplay().startPicBrowseActivity(browserPicEvent, REQUEST_CODE_FOR_REFRESH);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_patient_data_detail;
    }

    @Override
    protected void initViews() {
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        activityPatientDataDetailUpBtn.setOnClickListener(v -> {
            float appointmentId = StringUtils.str2Float(medicalId);
            if (appointmentId >= 500000) {
                getDisplay().startRoundsDataManagerActivity((int) appointmentId, false, false, REQUEST_CODE_FOR_REFRESH);
            } else {
                checkConsultationAppointment((int) appointmentId);
            }
        });
        tvToolBarTitle.setText("就诊材料");
        activityPatientDataDetailPatientPicsRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 4));
        activityPatientDataDetailPatientPicsRv.setAdapter(otherPatientDataPicManagerAdapter);
        activityPatientDataDetailPatientFirstPicsRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 4));
        activityPatientDataDetailPatientFirstPicsRv.setAdapter(firstPatientDataPicManagerAdapter);
        activityPatientDataDetailPatientSummaryPicsRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 4));
        activityPatientDataDetailPatientSummaryPicsRv.setAdapter(summaryPatientDataPicManagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPatientData(medicalId);
        getPatientSummary(medicalId);
    }

    private void checkConsultationAppointment(int medicalId) {
        AppointmentByIdRequester requester = new AppointmentByIdRequester(new DefaultResultCallback<AppointmentInfo>() {
            @Override
            public void onSuccess(AppointmentInfo appointmentInfo, BaseResult baseResult) {
                if (appointmentInfo != null) {
                    getPatientInfo(appointmentInfo, medicalId);
                }
            }
        });
        requester.appointmentId = medicalId;
        requester.doPost();
    }

    public void getPatientInfo(AppointmentInfo appointmentInfo, int appointmentId) {
        mPatientManager.getPatientInfo(appointmentId, new PatientManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null && null != appointmentInfo) {
                    getDisplay().startConsultDataManageActivity(false, false, patientInfo, appointmentInfo, 0, 0);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == REQUEST_CODE_FOR_REFRESH) {
                getPatientData(medicalId);
            } else if (requestCode == REQUEST_CODE_FOR_VIEW_DIS) {
                getPatientSummary(medicalId);
            }
        }
    }

    private void getPatientData(String id) {
        mPatientDataManager.getPatientDataById(id, new PatientDataManager.OnPatientDataLoadListener() {
            @Override
            public void onSuccess(PatientDataInfo patientDataInfo) {
                populatePatientData(patientDataInfo);
            }

            @Override
            public void onFail(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    private void getPatientSummary(String id) {
        mPatientDataManager.getPatientSummaryDataById(id, new DefaultResultCallback<List<DischargedSummaryInfo>>() {
            @Override
            public void onSuccess(List<DischargedSummaryInfo> dischargedSummaryInfos, BaseResult baseResult) {
                if (LibCollections.isEmpty(dischargedSummaryInfos)) {
                    activityPatientDataDetailPatientSummaryPicsLl.setVisibility(View.GONE);
                } else {
                    activityPatientDataDetailPatientSummaryPicsLl.setVisibility(View.VISIBLE);
                    summaryPatientDataPicManagerAdapter.setNewData(dischargedSummaryInfos);
                }
            }
        });
    }

    private void populatePatientData(PatientDataInfo patientDataInfo) {
        activityPatientDataDetailNameTv.setText(patientDataInfo.getPatientName());
        activityPatientDataDetailHospitalNameTv.setText(patientDataInfo.getHospitalName());
        activityPatientDataDetailMedicalIdDescTv.setText(patientDataInfo.getMedicalId());
        activityPatientDataDetailInHospitalNumDescTv.setText(StringUtils.isTrimEmpty(patientDataInfo.getHospitalizaId()) ? "--" : patientDataInfo.getHospitalizaId());
        List<AuxiliaryMaterialInfo> otherAuxiliaryMaterialInfos = new ArrayList<>();
        List<AuxiliaryMaterialInfo> firstAuxiliaryMaterialInfos = new ArrayList<>();
        if (LibCollections.isNotEmpty(patientDataInfo.getDataList())) {
            for (AuxiliaryMaterialInfo auxiliaryMaterialInfo : patientDataInfo.getDataList()) {
                if (auxiliaryMaterialInfo.getDataType() == 1) {
                    firstAuxiliaryMaterialInfos.add(auxiliaryMaterialInfo);
                } else {
                    otherAuxiliaryMaterialInfos.add(auxiliaryMaterialInfo);
                }
            }
        }
        if (LibCollections.isEmpty(firstAuxiliaryMaterialInfos)) {
            activityPatientDataDetailPatientFirstPicsLl.setVisibility(View.GONE);
        } else {
            activityPatientDataDetailPatientFirstPicsLl.setVisibility(View.VISIBLE);
        }
        firstPatientDataPicManagerAdapter.setNewData(firstAuxiliaryMaterialInfos);
        otherPatientDataPicManagerAdapter.setNewData(otherAuxiliaryMaterialInfos);
    }
}
