package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.fragment.DCDutyPatientDiseaseCourseFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.fragment.DCDutyPatientVisitPlanFragment;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.hospital.doctor.util.TabLayoutManager;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 11:31
 * @description: 门诊患者详情界面(患者健康档案)
 */
public class DCDutyPatientDetailActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dcduty_patient_detail_aab)
    private AppActionBar actDcdutyPatientDetailAab;
    @FindViewById(R.id.act_dcduty_patient_avatar_civ)
    private CircleImageView actDcdutyPatientAvatarCiv;
    @FindViewById(R.id.act_dcduty_patient_info_ll)
    private LinearLayout actDcdutyPatientInfoLl;
    @FindViewById(R.id.act_dcduty_patient_gender_tv)
    private TextView actDcdutyPatientGenderTv;
    @FindViewById(R.id.act_dcduty_patient_name_tv)
    private TextView actDcdutyPatientNameTv;
    @FindViewById(R.id.act_dcduty_patient_age_tv)
    private TextView actDcdutyPatientAgeTv;
    @FindViewById(R.id.act_dcduty_patient_hospital_name_tv)
    private TextView actDcdutyPatientHospitalNameTv;
    @FindViewById(R.id.act_dcduty_patient_detail_rg)
    private RadioGroup actDcDutyPatientDetailRg;
    @FindViewById(R.id.act_dcduty_patient_detail_disease_course_rb)
    private RadioButton actDcDutyPatientDetailDiseaseCourseRb;
    @FindViewById(R.id.act_dcduty_patient_detail_visit_plan_rb)
    private RadioButton actDcDutyPatientDetailVisitPlanRb;
    @FindViewById(R.id.act_dcduty_patient_detail_fl)
    private FrameLayout actDcdutyPatientDetailFl;
    private final int REQUEST_CODE_FOR_DATA_UP_LOAD = 100;
    @AppApplication.Manager
    private DCManager dcManager;
    private int patientId;
    private int projectId;
    private int medicalId;
    private DCDutyPatientVisitPlanFragment dcDutyPatientVisitPlanFragment;
    private DCDutyPatientDiseaseCourseFragment dcDutyPatientDiseaseCourseFragment;
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private TabLayoutManager tabLayoutManager;
    private DCDutyPatientItemInfo mDcDutyPatientInfo;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        patientId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PATIENT_ID, 0);
        projectId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, 0);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dcduty_patient_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != dcDutyPatientDiseaseCourseFragment && null != mDcDutyPatientInfo) {
            dcDutyPatientDiseaseCourseFragment.refresh(medicalId, mDcDutyPatientInfo.getHospitalName());
        }
        if (null != dcDutyPatientVisitPlanFragment && null != mDcDutyPatientInfo) {
            dcDutyPatientVisitPlanFragment.refresh(medicalId, projectId,mDcDutyPatientInfo.getHospitalName());
        }
    }

    @Override
    protected void initViews() {
        dcDutyPatientDiseaseCourseFragment = new DCDutyPatientDiseaseCourseFragment();
        dcDutyPatientVisitPlanFragment = new DCDutyPatientVisitPlanFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(dcDutyPatientDiseaseCourseFragment);
        fragments.add(dcDutyPatientVisitPlanFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.act_dcduty_patient_detail_fl)
                .setFragmentList(fragments)
                .setFragmentManager(getSupportFragmentManager())
                .setCurrentTab(0)
                .build();
        radioTabFragmentHelper.initFragment();
        initListener();
        getPatientInfo(patientId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_DATA_UP_LOAD) {
                showDCDutyPatientInfo(mDcDutyPatientInfo);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListener() {
        actDcdutyPatientDetailAab.setLeftOnClickListener(v -> {
            onBackPressed();
        });
        actDcDutyPatientDetailRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (null != radioTabFragmentHelper) {
                    switch (checkedId) {
                        case R.id.act_dcduty_patient_detail_disease_course_rb:
                            radioTabFragmentHelper.setFragment(0);
                            actDcDutyPatientDetailDiseaseCourseRb.setTextSize(18);
                            actDcDutyPatientDetailDiseaseCourseRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_049eff));
                            actDcDutyPatientDetailVisitPlanRb.setTextSize(16);
                            actDcDutyPatientDetailVisitPlanRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_1a1a1a));
                            break;
                        case R.id.act_dcduty_patient_detail_visit_plan_rb:
                            radioTabFragmentHelper.setFragment(1);
                            actDcDutyPatientDetailVisitPlanRb.setTextSize(18);
                            actDcDutyPatientDetailVisitPlanRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_049eff));
                            actDcDutyPatientDetailDiseaseCourseRb.setTextSize(16);
                            actDcDutyPatientDetailDiseaseCourseRb.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.color_1a1a1a));
                            break;
                        default:
                            break;
                    }
                }

            }
        });
    }

    private void getPatientInfo(int patientId) {
        dcManager.getPatientInfo(patientId, new DefaultResultCallback<DCDutyPatientItemInfo>() {
            @Override
            public void onSuccess(DCDutyPatientItemInfo dcDutyPatientInfo, BaseResult baseResult) {
                if (null != dcDutyPatientInfo) {
                    mDcDutyPatientInfo = dcDutyPatientInfo;
                    showDCDutyPatientInfo(mDcDutyPatientInfo);
                }
            }
        });
    }

    private void showDCDutyPatientInfo(@NonNull DCDutyPatientItemInfo info) {
        GlideUtils.showImage(actDcdutyPatientAvatarCiv, getThisActivity(), info.getAvatar());
        actDcdutyPatientGenderTv.setText(info.getGenderStr());
        actDcdutyPatientNameTv.setText(info.getPatientName());
        actDcdutyPatientAgeTv.setText(info.getAge());
        actDcdutyPatientHospitalNameTv.setText(info.getHospitalName());
        medicalId = info.getMedicalId();
        if (null != dcDutyPatientVisitPlanFragment) {
            dcDutyPatientVisitPlanFragment.refresh(medicalId, projectId,info.getHospitalName());
        }
        if (null != dcDutyPatientDiseaseCourseFragment) {
            dcDutyPatientDiseaseCourseFragment.refresh(medicalId, info.getHospitalName());
        }
    }
}
