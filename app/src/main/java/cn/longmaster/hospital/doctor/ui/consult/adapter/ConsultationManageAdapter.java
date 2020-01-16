package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.remote.ScheduleOrImageInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.ScheduingListInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.SchedulePaymentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleServiceRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetSchedulePaymentRequester;
import cn.longmaster.hospital.doctor.ui.consult.consultant.PaymentConfirmActivity;
import cn.longmaster.utils.StringUtils;

/**
 * 会诊管理适配
 * Created by W·H·K on 2017/12/20.
 */
public class ConsultationManageAdapter extends SimpleBaseAdapter<ScheduingListInfo, ConsultationManageAdapter.ViewHolder> {
    private Context mContext;
    private Map<Integer, ScheduleOrImageInfo> mScheduleOrImageInfoMap = new HashMap<>();
    private Map<Integer, DoctorBaseInfo> mDoctorInfos = new HashMap<>();

    public ConsultationManageAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_consultation_manage;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ScheduingListInfo scheduingListInfo, int position) {
        viewHolder.setViewTag();
        viewHolder.displayView(scheduingListInfo);
        viewHolder.onClickListener(scheduingListInfo);
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_consultation_appointment_id)
        private TextView mAappointmentId;
        @FindViewById(R.id.item_consultation_month)
        private TextView mMonth;
        @FindViewById(R.id.item_consultation_pay_ic)
        private TextView mPayIc;
        @FindViewById(R.id.item_consultation_confirmation_sheet)
        private ImageView mConfirmationSheet;
        @FindViewById(R.id.item_consultation_attending_doctor)
        private TextView mAttendingDoctor;
        @FindViewById(R.id.item_consultation_superior_expert)
        private TextView mSuperiorExpert;

        private void setViewTag() {
            mAappointmentId.setTag("");
            mMonth.setTag("");
            mPayIc.setTag("");
            mAttendingDoctor.setTag("");
            mSuperiorExpert.setTag("");
            mConfirmationSheet.setTag("");
        }

        private void displayView(ScheduingListInfo listInfo) {
            mAappointmentId.setText(mContext.getString(R.string.consult_payment_confirm_schedule_id, listInfo.getScheduingId() + ""));
            String str = mContext.getString(R.string.advance_d);
            int total = 0;
            int size = listInfo.getAttDoctorList().size();
            if (size > 4) {
                total = 4;
            } else if (size > 0 && size <= 4) {
                total = listInfo.getAttDoctorList().size();
            }
            if (size > 0) {
                for (int i = 0; i < total; i++) {
                    if (i == listInfo.getAttDoctorList().size() - 1) {
                        str += listInfo.getAttDoctorList().get(i).getAttDoctorName();
                    } else {
                        str += listInfo.getAttDoctorList().get(i).getAttDoctorName() + ",";
                    }
                }
                mAttendingDoctor.setText(str);
            } else {
                mAttendingDoctor.setText(str);
            }
            getScheduleTime(listInfo);
            getSchedulePayment(listInfo);
        }

        private void getScheduleTime(final ScheduingListInfo listInfo) {
            if (mScheduleOrImageInfoMap.containsKey(listInfo.getScheduingId())) {
                displayScheduingListInfo(mScheduleOrImageInfoMap.get(listInfo.getScheduingId()));
            } else {
                ScheduleServiceRequester scheduleServiceRequester = new ScheduleServiceRequester(new OnResultListener<ScheduleOrImageInfo>() {
                    @Override
                    public void onResult(BaseResult baseResult, ScheduleOrImageInfo scheduleOrImageInfo) {
                        Logger.logI(Logger.COMMON, "ConsultationManageAdapter-->scheduleOrImageInfo:" + scheduleOrImageInfo);
                        if (baseResult.getCode() == RESULT_SUCCESS && scheduleOrImageInfo != null) {
                            mScheduleOrImageInfoMap.put(listInfo.getScheduingId(), scheduleOrImageInfo);
                            displayScheduingListInfo(scheduleOrImageInfo);
                        }
                    }
                });
                scheduleServiceRequester.scheduingId = listInfo.getScheduingId();
                scheduleServiceRequester.doPost();
            }
        }

        private void displayScheduingListInfo(ScheduleOrImageInfo scheduleOrImageInfo) {
            if (StringUtils.isEmpty(scheduleOrImageInfo.getBeginDt()) || scheduleOrImageInfo.getBeginDt().contains("2099")) {
                mMonth.setText(R.string.time_to_be_determined);
            } else {
                mMonth.setText(DateUtil.getTime(scheduleOrImageInfo.getBeginDt(), "MM月dd日 HH:mm")
                        + "-" + DateUtil.getTime(scheduleOrImageInfo.getEndDt(), "HH:mm"));
            }
            getDoctorInfo(scheduleOrImageInfo.getDoctorId());
        }

        private void getSchedulePayment(ScheduingListInfo listInfo) {
            GetSchedulePaymentRequester requester = new GetSchedulePaymentRequester(new OnResultListener<SchedulePaymentInfo>() {
                @Override
                public void onResult(BaseResult baseResult, SchedulePaymentInfo schedulePaymentInfo) {
                    if (baseResult.getCode() != RESULT_SUCCESS) {
                        return;
                    }
                    if (schedulePaymentInfo != null) {
                        if (schedulePaymentInfo.getPaymentInfo() == null) {
                            mConfirmationSheet.setImageResource(R.drawable.ic_calendar_dark);
                        } else {
                            mConfirmationSheet.setImageResource(R.drawable.ic_calendar);
                        }
                    }
                }
            });
            requester.scheduingId = listInfo.getScheduingId();
            requester.doPost();
        }

        private void getDoctorInfo(final int doctorId) {
            if (mDoctorInfos.containsKey(doctorId)) {
                mSuperiorExpert.setText(mContext.getString(R.string.consult_payment_confirm_doctor, mDoctorInfos.get(doctorId).getRealName()));
            } else {
                AppApplication.getInstance().getManager(DoctorManager.class).getDoctorInfo(doctorId, false, new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        mDoctorInfos.put(doctorId, doctorBaseInfo);
                        mSuperiorExpert.setText(mContext.getString(R.string.consult_payment_confirm_doctor, doctorBaseInfo.getRealName()));
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

        private void onClickListener(final ScheduingListInfo scheduingListInfo) {
            mConfirmationSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PaymentConfirmActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_ID, scheduingListInfo.getScheduingId());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
