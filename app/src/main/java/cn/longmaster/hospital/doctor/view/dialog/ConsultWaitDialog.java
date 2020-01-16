package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleServiceRequester;

/**
 * 进入诊室等待dialog
 * Created by Yang² on 2017/1/5.
 */

public class ConsultWaitDialog extends Dialog {

    @FindViewById(R.id.layout_consult_wait_title_tv)
    private TextView mTitleTv;
    @FindViewById(R.id.layout_consult_wait_date_tv)
    private TextView mDateTv;
    @FindViewById(R.id.layout_consult_wait_time_tv)
    private TextView mTimeTv;
    @FindViewById(R.id.layout_consult_wait_sequence_num_tv)
    private TextView mSequenceNumTv;
    @FindViewById(R.id.layout_consult_wait_predict_time_tv)
    private TextView mPredictTimeTv;
    @FindViewById(R.id.layout_consult_wait_superior_doctor_tv)
    private TextView mSuperiorDoctorTv;
    @FindViewById(R.id.layout_consult_wait_attending_doctor_tv)
    private TextView mAttendingDoctorTv;
    @FindViewById(R.id.layout_consult_wait_patient_tv)
    private TextView mPatientTv;

    private OnWaitClickListener mOnWaitClickListener;

    public ConsultWaitDialog(Context context) {
        super(context, R.style.custom_noActionbar_window_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_consult_wait_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setDefault();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.layout_consult_wait_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_consult_wait_confirm_tv:
                if (mOnWaitClickListener != null) {
                    mOnWaitClickListener.onConfirmClick();
                }
                dismiss();
                break;
        }

    }

    private void setDefault() {
        mSequenceNumTv.setText(getContext().getString(R.string.consult_wait_sequence_num, ""));
        mPredictTimeTv.setText(getContext().getString(R.string.consult_wait_predict_time, ""));
        mSuperiorDoctorTv.setText(getContext().getString(R.string.consult_wait_consultation_doctor, ""));
        mAttendingDoctorTv.setText(getContext().getString(R.string.consult_wait_attending_doctor, ""));
        mPatientTv.setText(getContext().getString(R.string.consult_wait_patient, ""));
        mTitleTv.setText(R.string.consult_wait_title_wait);
    }

    /**
     * 设置就诊信息
     *
     * @param appointmentInfo
     */
    public void setConsultInfo(AppointmentInfo appointmentInfo) {
        if (appointmentInfo == null) {
            return;
        }
        ScheduleServiceRequester requester = new ScheduleServiceRequester(new OnResultListener<ScheduleOrImageInfo>() {
            @Override
            public void onResult(BaseResult baseResult, ScheduleOrImageInfo scheduleOrImageInfo) {
                if (baseResult.getCode() != RESULT_SUCCESS) {
                    return;
                }
                if (!TextUtils.isEmpty(scheduleOrImageInfo.getBeginDt()) && !TextUtils.isEmpty(scheduleOrImageInfo.getEndDt())) {
                    if (scheduleOrImageInfo.getBeginDt().contains("2099")) {
                        mDateTv.setText(R.string.time_to_be_determined);
                        mTimeTv.setVisibility(View.GONE);
                    } else {
                        mDateTv.setText(DateUtil.standardDateToChinaDate(getContext(), scheduleOrImageInfo.getBeginDt()).split(" ")[0]);
                        mTimeTv.setVisibility(View.VISIBLE);
                        mTimeTv.setText(scheduleOrImageInfo.getBeginDt().split(" ")[1].substring(0, 5) + "-" + scheduleOrImageInfo.getEndDt().split(" ")[1].substring(0, 5));
                    }
                }
            }
        });
        requester.scheduingId = appointmentInfo.getBaseInfo().getScheduingId();
        requester.doPost();
        if (appointmentInfo.getBaseInfo() != null) {
            mSequenceNumTv.setText(getContext().getString(R.string.consult_wait_sequence_num, appointmentInfo.getBaseInfo().getSerialNumber() + ""));
            if (!TextUtils.isEmpty(appointmentInfo.getBaseInfo().getPredictCureDt())) {
                if (appointmentInfo.getBaseInfo().getPredictCureDt().contains("2099")) {
                    mPredictTimeTv.setText(getContext().getString(R.string.consult_wait_predict_time, getContext().getString(R.string.time_to_be_determined)));

                } else {
                    mPredictTimeTv.setText(getContext().getString(R.string.consult_wait_predict_time, appointmentInfo.getBaseInfo().getPredictCureDt().split(" ")[1].substring(0, 5)));
                }
            }
        }
        //设置医生
        AppApplication.getInstance().getManager(DoctorManager.class).getDoctor(appointmentInfo.getBaseInfo().getDoctorUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo != null && !TextUtils.isEmpty(doctorBaseInfo.getRealName())) {
                    mSuperiorDoctorTv.setText(getContext().getString(R.string.consult_wait_consultation_doctor, doctorBaseInfo.getRealName()));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
        AppApplication.getInstance().getManager(DoctorManager.class).getDoctor(appointmentInfo.getBaseInfo().getAttendingDoctorUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo != null && !TextUtils.isEmpty(doctorBaseInfo.getRealName())) {
                    mAttendingDoctorTv.setText(getContext().getString(R.string.consult_wait_attending_doctor, doctorBaseInfo.getRealName()));
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });

        //设置患者
        AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentInfo.getBaseInfo().getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
            @Override
            public void onSuccess(PatientInfo patientInfo) {
                if (patientInfo != null) {
                    mPatientTv.setText(getContext().getString(R.string.consult_wait_patient, patientInfo.getPatientBaseInfo().getRealName()));
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

    public void setOnWaitClickListener(OnWaitClickListener onWaitClickListener) {
        this.mOnWaitClickListener = onWaitClickListener;
    }

    public interface OnWaitClickListener {
        void onConfirmClick();
    }
}
