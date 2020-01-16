package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientDiseaseItemInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.core.entity.event.BrowserPicEvent;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyDataUploadPicAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyDataUploadVoiceAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2020/1/7 9:25
 * @description: 患者/导医上传的病例资料
 */
public class DCDutyPatientDiseaseCheckActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dc_duty_patient_data_check_survey_et)
    private EditText actDcDutyPatientDataCheckSurveyEt;
    @FindViewById(R.id.act_dc_duty_patient_data_check_survey_pic_rv)
    private RecyclerView actDcDutyPatientDataCheckSurveyPicRv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_survey_voice_rv)
    private RecyclerView actDcDutyPatientDataCheckSurveyVoiceRv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_survey_pic_add_iv)
    private ImageView actDcDutyPatientDataCheckSurveyPicAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_survey_voice_add_iv)
    private ImageView actDcDutyPatientDataCheckSurveyVoiceAddIv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_operator_ll)
    private LinearLayout actDcDutyPatientDataCheckOperatorLl;
    @FindViewById(R.id.act_dc_duty_patient_data_check_operator_tv)
    private TextView actDcDutyPatientDataCheckOperatorTv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_delete_tv)
    private TextView actDcDutyPatientDataCheckDeleteTv;
    @FindViewById(R.id.act_dc_duty_patient_data_check_confirm_tv)
    private TextView actDcDutyPatientDataCheckConfirmTv;
    @AppApplication.Manager
    DCManager dcManager;
    private int mMedicalId;
    private int mDiseaseId;
    private int DISEASE_TYPE_PATIENT = 5;
    private int DISEASE_TYPE_DOCTOR = 6;

    private DCDutyDataUploadPicAdapter dcDutyDataUploadSurveyPicAdapter;
    private DCDutyDataUploadVoiceAdapter dcDutyDataUploadSurveyVoiceAdapter;


    @Override
    protected void initDatas() {
        mMedicalId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, 0);
        mDiseaseId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_DISEASE_ID, 0);
        dcDutyDataUploadSurveyPicAdapter = new DCDutyDataUploadPicAdapter(R.layout.item_dc_duty_data_up_load_pic, new ArrayList<>(0));
        dcDutyDataUploadSurveyPicAdapter.setMod(mDiseaseId != 0);
        dcDutyDataUploadSurveyPicAdapter.setOnItemClickListener((adapter, view, position) -> {
            showPic(position, dcDutyDataUploadSurveyPicAdapter.getData());
        });
        dcDutyDataUploadSurveyPicAdapter.setOnItemChildClickListener((adapter, view, position) -> adapter.remove(position));
        dcDutyDataUploadSurveyVoiceAdapter = new DCDutyDataUploadVoiceAdapter(R.layout.item_dc_duty_data_up_load_voice, new ArrayList<>(0));
        dcDutyDataUploadSurveyVoiceAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPrognoteDataInfo item = (DCDutyPrognoteDataInfo) adapter.getItem(position);
            if (null != item) {
                dcDutyDataUploadSurveyVoiceAdapter.playVoice(position);
            }
        });
        dcDutyDataUploadSurveyVoiceAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            adapter.remove(position);
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_patient_data_check;
    }

    @Override
    protected void initViews() {
        actDcDutyPatientDataCheckSurveyVoiceRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyPatientDataCheckSurveyVoiceRv.setAdapter(dcDutyDataUploadSurveyVoiceAdapter);
        actDcDutyPatientDataCheckSurveyPicRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 4));
        actDcDutyPatientDataCheckSurveyPicRv.setAdapter(dcDutyDataUploadSurveyPicAdapter);
        getDiseaseInfo(mDiseaseId);
    }

    private void getDiseaseInfo(int diseaseId) {
        if (diseaseId != 0) {
            dcManager.getDiseaseInfo(diseaseId, new DefaultResultCallback<DCDutyPatientDiseaseItemInfo>() {
                @Override
                public void onSuccess(DCDutyPatientDiseaseItemInfo dcDutyPatientDiseaseInfo, BaseResult baseResult) {
                    if (null != dcDutyPatientDiseaseInfo) {
                        displayDiseaseInfo(dcDutyPatientDiseaseInfo);
                    }
                }
            });
        }
    }

    private void showPic(int position, List<DCDutyPrognoteDataInfo> items) {
        DCDutyPrognoteDataInfo info = items.get(position);
        if (null != info) {
            if (info.getType() == 5) {
                List<String> picUrls = new ArrayList<>();
                for (int i = 0; i < LibCollections.size(items); i++) {
                    picUrls.add(AppConfig.getFirstJourneyUrl() + items.get(i).getMaterialPic());
                }
                BrowserPicEvent browserPicEvent = new BrowserPicEvent();
                browserPicEvent.setIndex(position);
                browserPicEvent.setAssistant(false);
                browserPicEvent.setServerFilePaths(picUrls);
                //browserPicEvent.setAuxiliaryMaterialInfos(checkInfos);
                getDisplay().startPicBrowseActivity(browserPicEvent, 0);
            } else {
                getDisplay().startPDFActivity(info.getMaterialPic(),null,mMedicalId,0);
            }
        }
    }

    private void displayDiseaseInfo(DCDutyPatientDiseaseItemInfo info) {
        if (info.getOpType() == DISEASE_TYPE_PATIENT) {
            actDcDutyPatientDataCheckSurveyVoiceRv.setVisibility(View.GONE);
        } else {
            actDcDutyPatientDataCheckSurveyVoiceRv.setVisibility(View.VISIBLE);
            dcDutyDataUploadSurveyVoiceAdapter.setNewData(dcManager.getPrognoteVoices(info, info.getOpType()));
        }

        actDcDutyPatientDataCheckOperatorTv.setText(info.getOpName());
        actDcDutyPatientDataCheckSurveyEt.setText(dcManager.getPrognoteContent(info, info.getOpType()));
        dcDutyDataUploadSurveyPicAdapter.setNewData(dcManager.getPrognotePics(info, info.getOpType()));

    }
}
