package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.AdvanceInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.HospitalRequester;

/**
 * Created by W·H·K on 2018/1/4.
 */

public class AdvanceView extends FrameLayout {
    private Context mContext;
    private TextView mHospitalNameTv;
    private LinearLayout mTtemView;
    private LinearLayout mCountView;
    private TextView mColumnTv;
    private AdvanceInfo mData;
    private Map<Integer, PatientInfo> mPatientInfo = new HashMap<>();

    public AdvanceView(Context context) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_advance_list, this, false);
        mContext = context;
        initView(rootView);
        addView(rootView);
    }

    private void initView(View rootView) {
        mHospitalNameTv = (TextView) rootView.findViewById(R.id.view_advance_hospital_tv);
        mTtemView = (LinearLayout) rootView.findViewById(R.id.view_advance_item_view);
        mCountView = (LinearLayout) rootView.findViewById(R.id.view_advance_count_view);
        mColumnTv = (TextView) rootView.findViewById(R.id.view_advance_column);
    }

    public void setData(AdvanceInfo data) {
        this.mData = data;
        getHospitalInfo(data.getHospitalId());
        double payValue = 0.0;
        for (int i = 0; i < mData.getAppointmentInfos().size(); i++) {
            View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_view_advance, this, false);
            TextView appointmentIdTv = (TextView) rootView.findViewById(R.id.item_view_advance_child_appointment_id_tv);
            TextView nameTv = (TextView) rootView.findViewById(R.id.item_view_advance_child_name_tv);
            TextView moneyTv = (TextView) rootView.findViewById(R.id.item_view_advance_child_money_tv);
            appointmentIdTv.setText(mData.getAppointmentInfos().get(i).getAppointmentId() + "");
            getPatientInfo(mData.getAppointmentInfos().get(i).getAppointmentId(), nameTv);
            moneyTv.setText(mContext.getString(R.string.advance_money, mData.getAppointmentInfos().get(i).getPayValue() + ""));
            mTtemView.addView(rootView);
            payValue += mData.getAppointmentInfos().get(i).getPayValue();
        }
        mColumnTv.setText(mContext.getString(R.string.advance_common_visit, mData.getAppointmentInfos().size(), (float) payValue + ""));
    }

    public void getPatientInfo(final int appointmentId, final TextView nameTv) {
        if (mPatientInfo.containsKey(appointmentId)) {
            nameTv.setText(mPatientInfo.get(appointmentId).getPatientBaseInfo().getRealName());
        } else {
            AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                @Override
                public void onSuccess(PatientInfo patientInfo) {
                    if (patientInfo != null) {
                        mPatientInfo.put(appointmentId, patientInfo);
                        nameTv.setText(patientInfo.getPatientBaseInfo().getRealName());
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
    }

    private void getHospitalInfo(int hospitalId) {
        HospitalRequester requester = new HospitalRequester(new OnResultListener<HospitalInfo>() {
            @Override
            public void onResult(BaseResult baseResult, HospitalInfo hospitalInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mHospitalNameTv.setText(hospitalInfo.getHospitalName());
                }
            }
        });
        requester.hospitalId = hospitalId;
        requester.token = "0";
        requester.doPost();
    }
}
