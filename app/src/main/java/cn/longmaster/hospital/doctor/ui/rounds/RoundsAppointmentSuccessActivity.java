package cn.longmaster.hospital.doctor.ui.rounds;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAppointmentInfo;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.view.MyStatusBar;

public class RoundsAppointmentSuccessActivity extends NewBaseActivity {
    @FindViewById(R.id.act_rounds_appointment_success)
    private LinearLayout actRoundsAppointmentSuccess;
    @FindViewById(R.id.action_bar_status_bar)
    private MyStatusBar actionBarStatusBar;
    @FindViewById(R.id.act_rounds_choice_doctor_title_bar)
    private RelativeLayout actRoundsChoiceDoctorTitleBar;
    @FindViewById(R.id.act_rounds_action_bar_title)
    private TextView actRoundsActionBarTitle;
    @FindViewById(R.id.act_rounds_appointment_success_tv)
    private TextView actRoundsAppointmentSuccessTv;
    @FindViewById(R.id.act_rounds_appointment_success_appointment_num_tv)
    private TextView actRoundsAppointmentSuccessAppointmentNumTv;
    @FindViewById(R.id.act_rounds_appointment_success_appointment_num_desc_tv)
    private TextView actRoundsAppointmentSuccessAppointmentNumDescTv;
    @FindViewById(R.id.act_rounds_appointment_success_doctor_tv)
    private TextView actRoundsAppointmentSuccessDoctorTv;
    @FindViewById(R.id.act_rounds_appointment_success_doctor_name_tv)
    private TextView actRoundsAppointmentSuccessDoctorNameTv;
    @FindViewById(R.id.act_rounds_appointment_success_lecture_topics_tv)
    private TextView actRoundsAppointmentSuccessLectureTopicsTv;
    @FindViewById(R.id.act_rounds_appointment_success_lecture_topics_desc_tv)
    private TextView actRoundsAppointmentSuccessLectureTopicsDescTv;
    @FindViewById(R.id.act_rounds_appointment_success_time_tv)
    private TextView actRoundsAppointmentSuccessTimeTv;
    @FindViewById(R.id.act_rounds_appointment_success_time_desc_ll)
    private LinearLayout actRoundsAppointmentSuccessTimeDescLl;
    @FindViewById(R.id.act_rounds_appointment_success_note_tv)
    private TextView actRoundsAppointmentSuccessNoteTv;
    @FindViewById(R.id.act_rounds_appointment_success_see)
    private TextView actRoundsAppointmentSuccessSee;
    @FindViewById(R.id.act_rounds_appointment_success_home)
    private TextView actRoundsAppointmentSuccessHome;

    private RoundsAppointmentInfo mRoundsAppointmentInfo;
    private int mOrderId;
    private String mTopics;

    @Override
    protected void initDatas() {
        mOrderId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        mRoundsAppointmentInfo = (RoundsAppointmentInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_APPOINTMENT_INFOS);
        mTopics = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TOPICS);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_appointment_success;
    }

    @Override
    protected void initViews() {
        actRoundsAppointmentSuccessAppointmentNumDescTv.setText(mOrderId + "");
        actRoundsAppointmentSuccessLectureTopicsDescTv.setText(mTopics);
        actRoundsAppointmentSuccessHome.setOnClickListener(v -> {
            goMainActivity();
        });
        actRoundsAppointmentSuccessSee.setOnClickListener(v -> {
            Intent intent = new Intent(RoundsAppointmentSuccessActivity.this, RoundsDetailActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, mOrderId);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_SUCCESS, true);
            startActivity(intent);
            finish();
        });
        if (mRoundsAppointmentInfo.getDoctorId() <= 0) {
            actRoundsAppointmentSuccessDoctorNameTv.setText(R.string.rounds_undetermined);
        } else {
            actRoundsAppointmentSuccessDoctorNameTv.setText(mRoundsAppointmentInfo.getDoctorName());
        }
        if (mRoundsAppointmentInfo.getIntentionTimeList() != null) {
            for (int i = 0; i < mRoundsAppointmentInfo.getIntentionTimeList().size(); i++) {
                View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.view_reservations_time, null, false);
                TextView timeTv = view.findViewById(R.id.time_tv);
                timeTv.setText(mRoundsAppointmentInfo.getIntentionTimeList().get(i));
                actRoundsAppointmentSuccessTimeDescLl.addView(view);
            }
        }
    }

    @Override
    public void onBackPressed() {
        goMainActivity();
    }

    private void goMainActivity() {
        Intent intent = new Intent(RoundsAppointmentSuccessActivity.this, MainActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACTION, AppConstant.ACTION_CHANGE_TAB);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getThisActivity(), MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
