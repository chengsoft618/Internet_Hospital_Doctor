package cn.longmaster.hospital.doctor.ui.consult;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.FormForConsult;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/10/25 14:40
 * @description:
 */
public class ConsultAddConfirmDialog extends DialogFragment {
    private static final String KEY_TO_QUERY_CONSULT_INFO = "_KEY_TO_QUERY_CONSULT_INFO_";
    private OnConfirmDialogClickListener listener;
    private TextView layoutConsultConfirmInfoPatientNameTv;
    private TextView layoutConsultConfirmInfoPatientNameDescTv;
    private TextView layoutConsultConfirmInfoPhoneNumTv;
    private TextView layoutConsultConfirmInfoPhoneNumDescTv;
    private TextView layoutConsultConfirmInfoDoctorNameTv;
    private TextView layoutConsultConfirmInfoDoctorNameDescTv;
    private TextView layoutConsultConfirmInfoConsultDateTv;
    private TextView layoutConsultConfirmInfoConsultDateDescTv;
    private TextView layoutConsultConfirmInfoCostTv;
    private TextView layoutConsultConfirmInfoCostDescTv;
    private TextView layoutConsultConfirmInfoCancelTv;
    private TextView layoutConsultConfirmInfoConfirmTv;
    private ImageView layoutConsultConfirmIv;

    private final int TYPE_NO_EXPERT = 1;
    private final int TYPE_NO_SCHEDULE = 2;
    private final int TYPE_ALL = 3;

    public static ConsultAddConfirmDialog getInstance(FormForConsult formForConsult) {
        ConsultAddConfirmDialog dialog = new ConsultAddConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TO_QUERY_CONSULT_INFO, formForConsult);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FormForConsult formForConsult = getFormForConsult();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_consult_confirm_info_dialog, null);
        builder.setView(rootView);
        layoutConsultConfirmInfoPatientNameTv = rootView.findViewById(R.id.layout_consult_confirm_info_patient_name_tv);
        layoutConsultConfirmInfoPatientNameDescTv = rootView.findViewById(R.id.layout_consult_confirm_info_patient_name_desc_tv);
        layoutConsultConfirmInfoPhoneNumTv = rootView.findViewById(R.id.layout_consult_confirm_info_phone_num_tv);
        layoutConsultConfirmInfoPhoneNumDescTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_phone_num_desc_tv);
        layoutConsultConfirmInfoDoctorNameTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_doctor_name_tv);
        layoutConsultConfirmInfoDoctorNameDescTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_doctor_name_desc_tv);
        layoutConsultConfirmInfoConsultDateTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_consult_date_tv);
        layoutConsultConfirmInfoConsultDateDescTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_consult_date_desc_tv);
        layoutConsultConfirmInfoCostTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_cost_tv);
        layoutConsultConfirmInfoCostDescTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_cost_desc_tv);
        layoutConsultConfirmInfoCancelTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_cancel_tv);
        layoutConsultConfirmInfoConfirmTv = (TextView) rootView.findViewById(R.id.layout_consult_confirm_info_confirm_tv);
        layoutConsultConfirmIv = (ImageView) rootView.findViewById(R.id.layout_consult_confirm_iv);
        layoutConsultConfirmInfoPatientNameDescTv.setText(formForConsult.getRealName());
        layoutConsultConfirmInfoPhoneNumDescTv.setText(formForConsult.getPhoneNum());
        DoctorBaseInfo doctorBaseInfo = formForConsult.getDoctorBaseInfo();
        if (doctorBaseInfo != null) {
            StringBuilder doctorInfo = new StringBuilder();
            doctorInfo.append(doctorBaseInfo.getHospitalName());
            doctorInfo.append("、");
            doctorInfo.append(doctorBaseInfo.getDepartmentName());
            doctorInfo.append("、");
            doctorInfo.append(doctorBaseInfo.getDoctorLevel());
            doctorInfo.append("、");
            doctorInfo.append(doctorBaseInfo.getRealName());
            layoutConsultConfirmInfoDoctorNameDescTv.setText(doctorInfo);
            ScheduleOrImageInfo scheduleOrImageInfo = formForConsult.getScheduleOrImageInfo();
            if (scheduleOrImageInfo != null) {
                if (!StringUtils.isEmpty(scheduleOrImageInfo.getBeginDt()) && scheduleOrImageInfo.getBeginDt().contains("2099")) {
                    layoutConsultConfirmInfoConsultDateDescTv.setText("时间待定");
                } else {
                    StringBuilder time = new StringBuilder();
                    time.append(TimeUtils.string2String(scheduleOrImageInfo.getBeginDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("yyyy年 MM月dd日 E HH:mm", Locale.getDefault())));
                    time.append("至");
                    time.append(TimeUtils.string2String(scheduleOrImageInfo.getEndDt(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()), new SimpleDateFormat("HH:mm", Locale.getDefault())));
                    layoutConsultConfirmInfoConsultDateDescTv.setText(time);
                }

                layoutConsultConfirmInfoCostDescTv.setText("￥" + scheduleOrImageInfo.getAdmissionPrice(formForConsult.getServiceType()));
            } else {
                initViews(TYPE_NO_SCHEDULE);
            }
        } else {
            initViews(TYPE_NO_EXPERT);
        }
        layoutConsultConfirmInfoCancelTv.setOnClickListener(v -> {
            listener.onCancel();
            dismiss();
        });
        layoutConsultConfirmInfoConfirmTv.setOnClickListener(v -> {
            listener.onConfirm();
            dismiss();
        });
        return builder.create();
    }

    private void initViews(int type) {
        if (TYPE_NO_EXPERT == type) {
            layoutConsultConfirmInfoDoctorNameTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoDoctorNameDescTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoConsultDateTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoConsultDateDescTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoCostTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoCostDescTv.setVisibility(View.GONE);
        } else if (TYPE_NO_SCHEDULE == type) {
            layoutConsultConfirmInfoConsultDateTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoConsultDateDescTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoCostTv.setVisibility(View.GONE);
            layoutConsultConfirmInfoCostDescTv.setVisibility(View.GONE);
        }
    }

    private FormForConsult getFormForConsult() {
        return (FormForConsult) getArguments().getSerializable(KEY_TO_QUERY_CONSULT_INFO);
    }

    public OnConfirmDialogClickListener getListener() {
        return listener;
    }

    public void setListener(OnConfirmDialogClickListener listener) {
        this.listener = listener;
    }

    public interface OnConfirmDialogClickListener {
        void onCancel();

        void onConfirm();
    }
}
