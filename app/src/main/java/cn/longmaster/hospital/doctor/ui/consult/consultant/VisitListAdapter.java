package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.appointment.AppointmentByIdRequester;
import cn.longmaster.hospital.doctor.core.requests.doctor.AssistantDoctorRequester;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.ui.consult.record.PatientInformationActivity;

/**
 * 就诊列表适配器
 * <p>
 * Created by W·H·K on 2017/12/22.
 */

public class VisitListAdapter extends SimpleBaseAdapter<VisitDetailsInfo, VisitListAdapter.ViewHolder> {
    private Context mContext;
    private OnDoctorAssistantClickListener mOnDoctorAssistantClickListener;
    private OnCheckClickListener mOnCheckClickListener;
    private boolean mIsTriage = false;
    private boolean mIsFinish = false;
    private Map<Integer, AssistantDoctorInfo> mAssistantDoctorInfo = new HashMap<>();
    private Map<Integer, AppointmentInfo> mAppointmentInfos = new HashMap<>();
    private Map<Integer, PatientInfo> mPatientInfo = new HashMap<>();

    public VisitListAdapter(Context context, boolean isTriage, boolean isFinish) {
        super(context);
        this.mContext = context;
        this.mIsTriage = isTriage;
        this.mIsFinish = isFinish;
    }

    public void setOnDoctorAssistantClickListener(OnDoctorAssistantClickListener listener) {
        this.mOnDoctorAssistantClickListener = listener;
    }

    public void setOnCheckClickListener(OnCheckClickListener listener) {
        this.mOnCheckClickListener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_visit_layout;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, VisitDetailsInfo visitDetailsInfo, int position) {
        viewHolder.setViewTag(visitDetailsInfo.getAppointmentId());
        viewHolder.displayView(visitDetailsInfo);
        viewHolder.displayVisitType(visitDetailsInfo);
        viewHolder.setViewClickListener(visitDetailsInfo, position);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_visit_rounds_identification)
        private TextView mIdentification;
        @FindViewById(R.id.item_visit_num_tv)
        private TextView mNumTv;
        @FindViewById(R.id.item_visit_patient_name)
        private TextView mPatientNameTv;
        @FindViewById(R.id.item_visit_money)
        private TextView mMonetTv;
        @FindViewById(R.id.item_visit_pay)
        private TextView mPayTv;
        @FindViewById(R.id.item_visit_check)
        private ImageView mCheckIv;
        @FindViewById(R.id.item_visit_assistant_name)
        private TextView mAssistantNameTv;
        @FindViewById(R.id.item_visit_assistant_name_medical_record)
        private TextView mMedicalRecordTv;
        @FindViewById(R.id.item_visit_upload)
        private TextView mUploadTv;
        @FindViewById(R.id.item_visit_above_view)
        private RelativeLayout mAboveView;
        @FindViewById(R.id.item_visit_below_view)
        private RelativeLayout mBelowView;

        private void setViewTag(int appointmentId) {
            mIdentification.setTag(appointmentId);
            mNumTv.setTag(appointmentId);
            mMonetTv.setTag(appointmentId);
            mPayTv.setTag(appointmentId);
            mCheckIv.setTag("");
            mAssistantNameTv.setTag(appointmentId);
            mMedicalRecordTv.setTag(appointmentId);
            mUploadTv.setTag(appointmentId);
            mPatientNameTv.setTag(appointmentId);
            mAboveView.setTag("");
            mBelowView.setTag("");
        }

        private void displayView(VisitDetailsInfo visitDetailsInfo) {
            mNumTv.setText(visitDetailsInfo.getAppointmentId() + "");
            mMonetTv.setText(mContext.getString(R.string.advance_money, visitDetailsInfo.getPayValue() + ""));
            if (mIsTriage || mIsFinish) {
                mCheckIv.setVisibility(View.INVISIBLE);
            } else {
                mCheckIv.setVisibility(View.VISIBLE);
            }
            if (visitDetailsInfo.getIsPay() == 0) {
                mPayTv.setText(mContext.getString(R.string.visit_unpaid));
            } else {
                mPayTv.setText(mContext.getString(R.string.visit_already_paid));
                mCheckIv.setVisibility(View.INVISIBLE);
            }

            if (visitDetailsInfo.getSelected()) {
                mCheckIv.setImageResource(R.drawable.ic_visit_selected);
            } else {
                mCheckIv.setImageResource(R.drawable.ic_visit_unselected);
            }
            getPatientInfo(visitDetailsInfo.getAppointmentId());
            getDoctorAssistantInfo(visitDetailsInfo);
        }

        public void getPatientInfo(final int appointmentId) {
            if (mPatientInfo.containsKey(appointmentId)) {
                mPatientNameTv.setText(mPatientInfo.get(appointmentId).getPatientBaseInfo().getRealName());
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(appointmentId, new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            mPatientInfo.put(appointmentId, patientInfo);
                            mPatientNameTv.setText(patientInfo.getPatientBaseInfo().getRealName());
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

        private void getDoctorAssistantInfo(final VisitDetailsInfo visitDetailsInfo) {
            AppointmentByIdRequester requester = new AppointmentByIdRequester(new DefaultResultCallback<AppointmentInfo>() {
                @Override
                public void onSuccess(AppointmentInfo appointmentInfo, BaseResult baseResult) {
                    mAppointmentInfos.put(visitDetailsInfo.getAppointmentId(), appointmentInfo);
                    getAssistDoctor(appointmentInfo.getBaseInfo().getAdminId());
                }
            });
            requester.appointmentId = visitDetailsInfo.getAppointmentId();
            requester.doPost();
        }

        /**
         * 获取医生助理信息
         *
         * @param assistId
         */
        private void getAssistDoctor(final int assistId) {
            if (mAssistantDoctorInfo.containsKey(assistId)) {
                mAssistantNameTv.setText(mAssistantDoctorInfo.get(assistId).getUserName());
            } else {
                AssistantDoctorRequester requester = new AssistantDoctorRequester(new DefaultResultCallback<AssistantDoctorInfo>() {
                    @Override
                    public void onSuccess(AssistantDoctorInfo assistantDoctorInfo, BaseResult baseResult) {
                        mAssistantDoctorInfo.put(assistId, assistantDoctorInfo);
                        if (("").equals(assistantDoctorInfo.getUserName())) {
                            mAssistantNameTv.setText("");
                        } else {
                            mAssistantNameTv.setText(assistantDoctorInfo.getUserName());
                        }
                    }
                });
                requester.assistantId = assistId;
                requester.doPost();
            }
        }

        public void displayVisitType(VisitDetailsInfo visitDetailsInfo) {
            Logger.logI(Logger.COMMON, "VisitListAdapter-->visitDetailsInfo.getCureType:" + visitDetailsInfo.getCureType());
            switch (visitDetailsInfo.getCureType()) {
                case AppConstant.VisitType.VISIT_TYPE_VIDEO_CONSULTATION:
                    mIdentification.setText(mContext.getString(R.string.visit_consultation));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_VIDEO_CONSULT:
                    mIdentification.setText(mContext.getString(R.string.visit_consult));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_IMAGE_CONSULTATION:
                case AppConstant.VisitType.VISIT_TYPE_IMAGE_CONSULTATION_BATCH:
                    mIdentification.setText(mContext.getString(R.string.visit_img));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_PATHOLOGY_CONSULTATION:
                    mIdentification.setText(mContext.getString(R.string.visit_illness));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_TURN_CONSULTATION:
                    mIdentification.setText(mContext.getString(R.string.visit_turn));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_VIDEO_CONSULTATION_FURTHER:
                case AppConstant.VisitType.VISIT_TYPE_VIDEO_CONSULT_FURTHER:
                case AppConstant.VisitType.VISIT_TYPE_OBSERVATION_FACE_FURTHER:
                case AppConstant.VisitType.VISIT_TYPE_IMAGE_CONSULTATION_FURTHER:
                    mIdentification.setText(mContext.getString(R.string.visit_complex));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_OBSERVATION_FACE:
                    mIdentification.setText(mContext.getString(R.string.visit_surface));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_MULTIDISCIPLINARY_CONSULTATION:
                    mIdentification.setText(mContext.getString(R.string.visit_many));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_EXPATRIATE_CONSULTATION:
                    mIdentification.setText(mContext.getString(R.string.visit_abroad));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_OUTPATIENT_SERVICE:
                    mIdentification.setText(mContext.getString(R.string.visit_door));
                    break;
                case AppConstant.VisitType.VISIT_TYPE_ROUNDS:
                    mIdentification.setText(mContext.getString(R.string.visit_check));
                    break;
                default:
                    break;
            }
        }

        private void setViewClickListener(final VisitDetailsInfo visitDetailsInfo, final int position) {
            mAboveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBelowView.getVisibility() == View.GONE) {
                        mBelowView.setVisibility(View.VISIBLE);
                        mAboveView.setBackgroundResource(R.drawable.bg_visit_list_above);
                    } else {
                        mBelowView.setVisibility(View.GONE);
                        mAboveView.setBackgroundResource(R.drawable.bg_solid_ffffff_stroke_c8c7cc_radius_5);
                    }
                }
            });
            mAssistantNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnDoctorAssistantClickListener != null) {
                        mOnDoctorAssistantClickListener.onDoctorAssistantClicked(v, mAssistantDoctorInfo.get(mAppointmentInfos.get(visitDetailsInfo.getAppointmentId()).getBaseInfo().getAdminId()), visitDetailsInfo.getAppointmentId());
                    }
                }
            });

            mMedicalRecordTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PatientInformationActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, visitDetailsInfo.getAppointmentId());
                    mContext.startActivity(intent);
                }
            });
            mUploadTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAppointmentInfos.get(visitDetailsInfo.getAppointmentId()).getBaseInfo().getAppointmentStat() == 15) {
                        Toast.makeText(mContext, mContext.getString(R.string.appointment_complete), Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(mContext, ConsultDataManageActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, visitDetailsInfo.getAppointmentId());
                        mContext.startActivity(intent);
                    }
                }
            });
            mCheckIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCheckClickListener != null) {
                        mOnCheckClickListener.onCheckClicked(v, position);
                    }
                }
            });
        }
    }

    interface OnDoctorAssistantClickListener {
        void onDoctorAssistantClicked(View view, AssistantDoctorInfo assistantDoctorInfo, int appointmentInfoId);
    }

    interface OnCheckClickListener {
        void onCheckClicked(View view, int position);
    }
}
