package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;

/**
 * 分享医生名片
 * Created by Yang² on 2017/9/5.
 */

public class ShareDoctorCardDialog extends Dialog {
    @FindViewById(R.id.share_doctor_card_dialog_consult_num_tv)
    private TextView mConsultNumTv;
    @FindViewById(R.id.share_doctor_card_dialog_patient_name_tv)
    private TextView mPatientNameTv;
    @FindViewById(R.id.share_doctor_card_dialog_doctor_name_tv)
    private TextView mDoctorNameTv;
    @FindViewById(R.id.share_doctor_card_dialog_hospital_tv)
    private TextView mHospitalTv;
    @FindViewById(R.id.share_doctor_card_dialog_suggest_et)
    private EditText mSuggestEt;
    private OnDoctorCardClickListener mOnDoctorCardClickListener;

    public ShareDoctorCardDialog(Context context) {
        super(context, R.style.custom_noActionbar_window_style);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share_doctor_card_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    @OnClick({R.id.share_doctor_card_dialog_cancel_tv,
            R.id.share_doctor_card_dialog_send_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_doctor_card_dialog_cancel_tv:
                dismiss();
                break;

            case R.id.share_doctor_card_dialog_send_tv:
                if (mOnDoctorCardClickListener != null) {
                    mOnDoctorCardClickListener.onSendClick(mSuggestEt.getText().toString().trim());
                }
                dismiss();
                break;
        }
    }

    public void displayDialog(@NonNull PatientBaseInfo patientBaseInfo, @NonNull String doctorName, @NonNull String hospitalName) {
        if (patientBaseInfo != null) {
            mConsultNumTv.setText(String.valueOf(patientBaseInfo.getAppointmentId()));
            mPatientNameTv.setText(patientBaseInfo.getRealName());
        }
        mDoctorNameTv.setText(doctorName);
        mHospitalTv.setText(hospitalName);
    }

    public void setOnDoctorCardClickListener(OnDoctorCardClickListener onDoctorCardClickListener) {
        this.mOnDoctorCardClickListener = onDoctorCardClickListener;
    }

    public interface OnDoctorCardClickListener {
        void onSendClick(String suggestText);
    }
}
