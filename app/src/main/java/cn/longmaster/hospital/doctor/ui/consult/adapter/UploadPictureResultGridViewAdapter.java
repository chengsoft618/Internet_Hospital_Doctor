package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskResultInfo;

/**
 * Created by W·H·K on 2018/5/9.
 * Mod by biao on 2019/11/30
 */

public class UploadPictureResultGridViewAdapter extends BaseQuickAdapter<MaterialTaskResultInfo, BaseViewHolder> {
    private SparseArray<PatientInfo> mPatientInfos = new SparseArray<>();

    public UploadPictureResultGridViewAdapter(int layoutResId, @Nullable List<MaterialTaskResultInfo> data) {
        super(layoutResId, data);
    }

    @Override
    public void addData(@NonNull MaterialTaskResultInfo data) {
        getData().remove(data);
        getData().add(data);
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MaterialTaskResultInfo item) {
        helper.addOnClickListener(R.id.item_upload_result_see_tv);
        displayStateView(helper, item);
        displayAppointmentInfo(helper, item);
    }

    private void displayStateView(BaseViewHolder helper, final MaterialTaskResultInfo taskResultInfo) {
        if (taskResultInfo.getFailedCount() == 0) {
            helper.setText(R.id.item_upload_result_state_tv, R.string.data_queue_upload_result_all_success);
            helper.setTextColor(R.id.item_upload_result_state_tv, ContextCompat.getColor(mContext, R.color.color_39d210));
            helper.setText(R.id.item_upload_result_see_tv, R.string.data_queue_upload_result_see);
        } else {
            helper.setText(R.id.item_upload_result_state_tv, mContext.getString(R.string.data_queue_upload_result_fail, taskResultInfo.getFailedCount()));
            helper.setTextColor(R.id.item_upload_result_state_tv, ContextCompat.getColor(mContext, R.color.color_ff3a3a));
            helper.setText(R.id.item_upload_result_see_tv, R.string.data_queue_upload_result_again);
        }
    }

    private void displayAppointmentInfo(BaseViewHolder helper, final MaterialTaskResultInfo taskResultInfo) {
        helper.setText(R.id.item_upload_result_appointment_id_tv, taskResultInfo.getAppointmentId() + "");
        if (taskResultInfo.getAppointmentId() >= 500000) {
            RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
                @Override
                public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                    Logger.logI(Logger.APPOINTMENT, "UploadPictureResultAdapter-->baseResult:" + baseResult + ",roundsMedicalDetailsInfo：" + roundsMedicalDetailsInfo);
                    helper.setText(R.id.item_upload_result_name_tv, roundsMedicalDetailsInfo.getPatientName());
                }
            });
            requester.setMedicalId(taskResultInfo.getAppointmentId());
            requester.doPost();
        } else {
            if (null != mPatientInfos.get(taskResultInfo.getAppointmentId())) {
                helper.setText(R.id.item_upload_result_name_tv, mPatientInfos.get(taskResultInfo.getAppointmentId()).getPatientBaseInfo().getRealName());
            } else {
                AppApplication.getInstance().getManager(ConsultManager.class).getPatientInfo(taskResultInfo.getAppointmentId(), new ConsultManager.OnPatientInfoLoadListener() {
                    @Override
                    public void onSuccess(PatientInfo patientInfo) {
                        if (patientInfo != null) {
                            helper.setText(R.id.item_upload_result_name_tv, patientInfo.getPatientBaseInfo().getRealName());
                            mPatientInfos.put(taskResultInfo.getAppointmentId(), patientInfo);
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
    }
}


