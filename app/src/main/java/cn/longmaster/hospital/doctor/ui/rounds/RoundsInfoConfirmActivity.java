package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.SubmissionOrderRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 信息确认界面
 */
public class RoundsInfoConfirmActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_rounds_info_confirm_topics)
    private TextView mTopics;
    @FindViewById(R.id.activity_rounds_info_confirm_appeal)
    private TextView mAppeal;
    @FindViewById(R.id.activity_rounds_info_confirm_need_ppt)
    private TextView mNeedPPT;
    @FindViewById(R.id.activity_rounds_info_confirm_time_length)
    private TextView mTimeLength;
    @FindViewById(R.id.activity_rounds_info_confirm_number_medical)
    private TextView mMedicalNumber;
    @FindViewById(R.id.activity_rounds_info_confirm_medical_view)
    private LinearLayout mMedicalView;
    @FindViewById(R.id.activity_rounds_info_confirm_time_view)
    private LinearLayout mTimeView;
    @FindViewById(R.id.activity_rounds_info_confirm_doctor_info_tv)
    private TextView mDoctorInfoTv;
    @FindViewById(R.id.activity_rounds_info_confirm_doctor_name_tv)
    private TextView activityRoundsInfoConfirmDoctorNameTv;
    @FindViewById(R.id.activity_rounds_info_confirm_diagnosis_tip)
    private TextView mDiagnosisTip;
    @FindViewById(R.id.activity_rounds_info_confirm_remarks_view)
    private LinearLayout mDiagnosisTipView;
    @FindViewById(R.id.act_rounds_confirm_department_ll)
    private LinearLayout mDepartmentLl;
    @FindViewById(R.id.act_rounds_info_confirm_department_view)
    private LinearLayout mDepartmentView;

    private RoundsAppointmentInfo mRoundsAppointmentInfo;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initDatas() {
        mRoundsAppointmentInfo = (RoundsAppointmentInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_APPOINTMENT_INFO);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_info_confirm;
    }

    @Override
    protected void initViews() {
        if (mRoundsAppointmentInfo.getDoctorId() <= 0) {
            activityRoundsInfoConfirmDoctorNameTv.setText("待定");
            mDepartmentLl.setVisibility(View.VISIBLE);
            mDoctorInfoTv.setVisibility(View.GONE);
        } else {
            mDoctorInfoTv.setVisibility(View.VISIBLE);
            activityRoundsInfoConfirmDoctorNameTv.setText(mRoundsAppointmentInfo.getDoctorName());
            mDepartmentLl.setVisibility(View.GONE);
        }
        if (StringUtils.isEmpty(mRoundsAppointmentInfo.getRemarks())) {
            mDiagnosisTipView.setVisibility(View.GONE);
        } else {
            mDiagnosisTipView.setVisibility(View.VISIBLE);
            mDiagnosisTip.setText(mRoundsAppointmentInfo.getRemarks());
        }
        setThemeTv(mRoundsAppointmentInfo);
        mDoctorInfoTv.setText(mRoundsAppointmentInfo.getDoctorHospital() + " " + mRoundsAppointmentInfo.getDoctorDepartment() + " " + mRoundsAppointmentInfo.getDoctorLevel());
        mAppeal.setText(mRoundsAppointmentInfo.getAppeal());
        mNeedPPT.setText(mRoundsAppointmentInfo.isNeedPPT() ? getString(R.string.yes) : getString(R.string.no));
        mTimeLength.setText(getString(R.string.rounds_medical_record_time, mRoundsAppointmentInfo.getLengthTime()));
        mMedicalNumber.setText(getString(R.string.rounds_add_medical_record_number_medical_records, LibCollections.size(mRoundsAppointmentInfo.getMedicalInfoList()) + ""));
        addTimeView();
        addDepartmentView();
        addMedicalView();
    }

    private void addTimeView() {
        if (mRoundsAppointmentInfo.getIntentionTimeList() != null) {
            for (int i = 0; i < mRoundsAppointmentInfo.getIntentionTimeList().size(); i++) {
                View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.view_rounds_mould_time, null, false);
                TextView timeTv = view.findViewById(R.id.view_rounds_mould_time_tv);
                timeTv.setText(mRoundsAppointmentInfo.getIntentionTimeList().get(i));
                mTimeView.addView(view);
            }
        }
    }

    private void addDepartmentView() {
        if (mRoundsAppointmentInfo.getIntentionDepartmentList() != null) {
            for (DepartmentListInfo info : mRoundsAppointmentInfo.getIntentionDepartmentList()) {
                View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.view_rounds_mould_department, null, false);
                TextView timeTv = view.findViewById(R.id.view_rounds_mould_department_tv);
                timeTv.setText(info.getDepartmentName());
                mDepartmentView.addView(view);
            }
        }
    }

    private void addMedicalView() {
        if (mRoundsAppointmentInfo.getMedicalInfoList() != null) {
            for (int i = 0; i < mRoundsAppointmentInfo.getMedicalInfoList().size(); i++) {
                RoundsMedicalDetailsInfo info = mRoundsAppointmentInfo.getMedicalInfoList().get(i);
                View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.view_rounds_confirm_medica, null, false);
                TextView medicalNum = view.findViewById(R.id.view_rounds_confirm_medical_num);
                TextView patientName = view.findViewById(R.id.view_rounds_confirm_medical_name);
                TextView patientSex = view.findViewById(R.id.view_rounds_confirm_medical_sex);
                TextView patientAge = view.findViewById(R.id.view_rounds_confirm_medical_age);
                TextView importView = view.findViewById(R.id.view_rounds_confirm_medical_import_view);
                medicalNum.setText(getString(R.string.rounds_medical_record_title, (i + 1) + ""));
                patientName.setText(info.getPatientName());
                patientSex.setText(info.getGender() == 1 ? "男" : "女");
                patientAge.setText(info.getAge() + "");
                if (info.getImportant() == 1) {
                    importView.setVisibility(View.VISIBLE);
                } else {
                    importView.setVisibility(View.GONE);
                }
                mMedicalView.addView(view);
            }
        }
    }

    private void setThemeTv(RoundsAppointmentInfo info) {
        if (info.isNeedPPT()) {
            if (LibCollections.isEmpty(info.getMedicalInfoList())) {
                mTopics.setText(StringUtils.isEmpty(info.getLectureTopics()) ? "" : getString(R.string.rounds_teaching, info.getLectureTopics()));
            } else {
                mTopics.setText(getString(R.string.rounds_theme_add_num, StringUtils.isEmpty(info.getLectureTopics()) ? "" : info.getLectureTopics(), info.getMedicalInfoList().size() + ""));
            }
        } else {
            if (LibCollections.isEmpty(info.getMedicalInfoList())) {
                mTopics.setText("");
            } else {
                mTopics.setText(getString(R.string.rounds_theme_add_num_t, info.getMedicalInfoList().size() + ""));
            }
        }
    }

    @OnClick({R.id.activity_rounds_info_confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_rounds_info_confirm_btn:
                submissionOrder();
                break;
            default:
                break;
        }
    }

    private void submissionOrder() {
        ProgressDialog progressDialog = createProgressDialog("正在提交...");
        progressDialog.show();
        SubmissionOrderRequester requester = new SubmissionOrderRequester(new DefaultResultCallback<OrderInfo>() {
            @Override
            public void onSuccess(OrderInfo orderInfo, BaseResult baseResult) {
                Logger.logD(Logger.APPOINTMENT, "RoundsMouldInfoActivity->baseResult:" + baseResult + " ,integers:" + orderInfo);
                Intent intent = new Intent(RoundsInfoConfirmActivity.this, RoundsAppointmentSuccessActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderInfo.getOrderId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_APPOINTMENT_INFOS, mRoundsAppointmentInfo);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TOPICS, mTopics.getText().toString());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
        requester.doctorId = mRoundsAppointmentInfo.getDoctorId();
        requester.orderTitle = mRoundsAppointmentInfo.getLectureTopics();
        requester.intentionDuration = mRoundsAppointmentInfo.getLengthTime();
        requester.isPpt = mRoundsAppointmentInfo.isNeedPPT() ? 1 : 0;
        requester.orderType = 14;
        requester.triageRequire = mRoundsAppointmentInfo.getRemarks();
        requester.cureDtList = intentionTimeJSONArray(mRoundsAppointmentInfo.getIntentionTimeList());
        requester.visitAppeal = mRoundsAppointmentInfo.getAppeal();
        //requester.patientList = createPatientList(mRoundsAppointmentInfo.getMedicalInfoList());
        requester.medicalList = createPatientList(mRoundsAppointmentInfo.getMedicalInfoList());
        if (LibCollections.isNotEmpty(mRoundsAppointmentInfo.getDepartmentIdList())) {
            requester.departmentList = departmentListJsonArray(mRoundsAppointmentInfo.getDepartmentIdList());
        }
        requester.doPost();
    }

    private JSONArray createPatientList(List<RoundsMedicalDetailsInfo> medicalInfoList) {
        if (null == medicalInfoList) {
            return new JSONArray();
        }
        JSONArray array = new JSONArray();
        for (RoundsMedicalDetailsInfo info : medicalInfoList) {
            array.put(info.getMedicalId());
        }
        return array;
    }

    /**
     * 时间格式化
     *
     * @param addIntentionList list格式为 周三 06月26日 7：00
     * @return
     */
    public JSONArray intentionTimeJSONArray(List<String> addIntentionList) {
        JSONArray array = new JSONArray();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (String info : addIntentionList) {
            JSONObject json = new JSONObject();
            String time = TimeUtils.string2String(year + info, new SimpleDateFormat("yyyyEEMM月dd日 HH:mm", Locale.getDefault()));

//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(year);
//            stringBuilder.append("-");
//            stringBuilder.append(StringUtils.substringBegin(info, 3, 5));
//            stringBuilder.append("-");
//            stringBuilder.append(StringUtils.substringBegin(info, 6, 8));
//
//            stringBuilder.append(StringUtils.substringAfter(info, "日"));
//            String time = stringBuilder.toString();
            try {
                json.put("cure_dt", time);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(json);
        }
        return array;
    }

    private JSONArray medicalListJsonArray(List<Integer> medicalList) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < medicalList.size(); i++) {
            array.put(medicalList.get(i));
        }
        Logger.logD(Logger.APPOINTMENT, "RoundsMouldInfoActivity->medicalListJsonArray:" + array);
        return array;
    }

    private JSONArray departmentListJsonArray(List<Integer> departmentIdList) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < departmentIdList.size(); i++) {
            array.put(departmentIdList.get(i));
        }
        Logger.logD(Logger.APPOINTMENT, "RoundsMouldInfoActivity->departmentListJsonArray:" + array);
        return array;
    }
}
