package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleRelateInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * 提交成功
 * 发起会诊，填写信息，提交成功后进入
 * Created by Yang² on 2016/6/15.
 * Mod by biao on 2019/11/20
 */
public class SubmitSuccessfulActivity extends NewBaseActivity {
    @FindViewById(R.id.act_consult_add_success_note_tv)
    private TextView actConsultAddSuccessNoteTv;
    @FindViewById(R.id.act_consult_add_success_consult_number_tv)
    private TextView actConsultAddSuccessConsultNumberTv;
    @FindViewById(R.id.act_consult_add_success_consult_number_desc_tv)
    private TextView actConsultAddSuccessConsultNumberDescTv;
    @FindViewById(R.id.act_consult_add_success_relation_password_tv)
    private TextView actConsultAddSuccessRelationPasswordTv;
    @FindViewById(R.id.act_consult_add_success_relation_password_desc_tv)
    private TextView actConsultAddSuccessRelationPasswordDescTv;
    @FindViewById(R.id.act_consult_add_success_patient_name_tv)
    private TextView actConsultAddSuccessPatientNameTv;
    @FindViewById(R.id.act_consult_add_success_patient_name_desc_tv)
    private TextView actConsultAddSuccessPatientNameDescTv;
    @FindViewById(R.id.act_consult_add_success_phone_num_tv)
    private TextView actConsultAddSuccessPhoneNumTv;
    @FindViewById(R.id.act_consult_add_success_phone_num_desc_tv)
    private TextView actConsultAddSuccessPhoneNumDescTv;
    @FindViewById(R.id.act_consult_add_success_choose_doctor_rl)
    private RelativeLayout actConsultAddSuccessChooseDoctorRl;
    @FindViewById(R.id.act_consult_add_success_consult_doctor_name_tv)
    private TextView actConsultAddSuccessConsultDoctorNameTv;
    @FindViewById(R.id.act_consult_add_success_consult_doctor_name_desc_tv)
    private TextView actConsultAddSuccessConsultDoctorNameDescTv;
    @FindViewById(R.id.act_consult_add_success_consult_time_tv)
    private TextView actConsultAddSuccessConsultTimeTv;
    @FindViewById(R.id.act_consult_add_success_consult_time_desc_tv)
    private TextView actConsultAddSuccessConsultTimeDescTv;
    @FindViewById(R.id.act_consult_add_success_sequence_number_tv)
    private TextView actConsultAddSuccessSequenceNumberTv;
    @FindViewById(R.id.act_consult_add_success_sequence_number_desc_tv)
    private TextView actConsultAddSuccessSequenceNumberDescTv;
    @FindViewById(R.id.act_consult_add_success_consult_cost_tv)
    private TextView actConsultAddSuccessConsultCostTv;
    @FindViewById(R.id.act_consult_add_success_consult_cost_desc_tv)
    private TextView actConsultAddSuccessConsultCostDescTv;
    @FindViewById(R.id.act_consult_add_success_tips_tv)
    private TextView actConsultAddSuccessTipsTv;
    @FindViewById(R.id.act_consult_add_success_data_manager_tv)
    private TextView actConsultAddSuccessDataManagerTv;
    @FindViewById(R.id.act_consult_add_success_back_home)
    private TextView actConsultAddSuccessBackHome;

    private final String DEFAULT_PERIOD = "时间待定";
    private final String DEFAULT_CAST = "";
    private FormForConsult mSuccessInfo;

    @Override
    protected void initDatas() {
        mSuccessInfo = (FormForConsult) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SUBMIT_SUCCESS_INFO);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_submit_successful;
    }

    @Override
    protected void initViews() {
        if (mSuccessInfo != null) {
            actConsultAddSuccessConsultNumberDescTv.setText(StringUtils.integer2Str(mSuccessInfo.getAppointmentId()));
            actConsultAddSuccessRelationPasswordDescTv.setText(mSuccessInfo.getPayPassword());
            actConsultAddSuccessPatientNameDescTv.setText(mSuccessInfo.getRealName());
            actConsultAddSuccessPhoneNumDescTv.setText(mSuccessInfo.getPhoneNum());
            DoctorBaseInfo doctorBaseInfo = mSuccessInfo.getDoctorBaseInfo();
            if (null != doctorBaseInfo) {
                actConsultAddSuccessChooseDoctorRl.setVisibility(View.VISIBLE);
                StringBuilder doctorInfo = new StringBuilder();
                doctorInfo.append(doctorBaseInfo.getHospitalName())
                        .append("、")
                        .append(doctorBaseInfo.getDepartmentName())
                        .append("、")
                        .append(doctorBaseInfo.getDoctorLevel())
                        .append("、")
                        .append(doctorBaseInfo.getRealName());
                String timeDesc = DEFAULT_PERIOD;
                String castDesc = DEFAULT_CAST;
                ScheduleOrImageInfo scheduleOrImageInfo = mSuccessInfo.getScheduleOrImageInfo();
                if (scheduleOrImageInfo != null) {
                    if (!scheduleOrImageInfo.getBeginDt().contains("2099")) {
                        StringBuilder time = new StringBuilder();
                        time.append(TimeUtils.string2String(scheduleOrImageInfo.getBeginDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy年 MM月dd日 E HH:mm", Locale.getDefault())));
                        time.append("至");
                        time.append(TimeUtils.string2String(scheduleOrImageInfo.getEndDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("HH:mm", Locale.getDefault())));
                        timeDesc = time.toString();
                        if (null != scheduleOrImageInfo.getScheduingRelationList()) {
                            for (ScheduleRelateInfo relateInfo : scheduleOrImageInfo.getScheduingRelationList()) {
                                if (relateInfo.getServiceType() == mSuccessInfo.getServiceType()) {
                                    castDesc = relateInfo.getAdmissionPrice() + getString(R.string.unit_money_rmb);
                                    break;
                                }
                            }
                        }
                    }
                }
                StringBuilder sequenceNumber = new StringBuilder();

                if (mSuccessInfo.getSerialNumber() != 0) {
                    sequenceNumber.append(mSuccessInfo.getSerialNumber())
                            .append("(预计就诊时间:");
                    if (StringUtils.isEmpty(mSuccessInfo.getPredictCureDt()) && mSuccessInfo.getPredictCureDt().contains("2099")) {
                        sequenceNumber.append("时间待定");
                    } else {
                        sequenceNumber.append(StringUtils.isEmpty(mSuccessInfo.getPredictCureDt()) ? "待定" : mSuccessInfo.getPredictCureDt());
                    }
                    sequenceNumber.append(")");
                }
                actConsultAddSuccessConsultDoctorNameDescTv.setText(doctorInfo);
                actConsultAddSuccessConsultTimeDescTv.setText(timeDesc);
                actConsultAddSuccessSequenceNumberDescTv.setText(sequenceNumber);
                actConsultAddSuccessConsultCostDescTv.setText(castDesc);
                actConsultAddSuccessTipsTv.setText(R.string.consult_tips_has_doctor);
            } else {
                actConsultAddSuccessChooseDoctorRl.setVisibility(View.GONE);
                actConsultAddSuccessTipsTv.setText(R.string.consult_tips_no_doctor);
            }
        }
        initListener();
    }

    @Override
    public void onBackPressed() {
        goMainActivity();
    }

    private void initListener() {
        actConsultAddSuccessDataManagerTv.setOnClickListener(v -> {
            getDisplay().startConsultDataManageActivity(false, false, null, null, mSuccessInfo.getAppointmentId(), 0);
            finish();
        });
        actConsultAddSuccessBackHome.setOnClickListener(v -> {
            goMainActivity();
        });
    }

    private void goMainActivity() {
        Intent intent = new Intent(SubmitSuccessfulActivity.this, MainActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACTION, AppConstant.ACTION_CHANGE_TAB);
        startActivity(intent);
        finish();
    }
}
