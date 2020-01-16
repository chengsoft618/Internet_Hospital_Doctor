package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.consult.AppointmentInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.AppointmentItemForRelateInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.rounds.PatientManager;

/**
 * @author W·H·K
 * @date 2019/2/18
 * Mod by biao on 2019/11/08
 */
public class ConsultationRecordMedicalAdapter extends BaseQuickAdapter<AppointmentItemForRelateInfo, BaseViewHolder> {
    private SparseArray<AppointmentInfo> mAppointmentInfoMap = new SparseArray<>();
    private SparseArray<PatientInfo> mPatientMap = new SparseArray<>();

    public ConsultationRecordMedicalAdapter(int layoutResId, @Nullable List<AppointmentItemForRelateInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppointmentItemForRelateInfo item) {
        helper.addOnClickListener(R.id.item_consultation_disease_record_tv);
        int appointmentId = item.getRelationId();
        AppointmentInfo appointmentInfo = mAppointmentInfoMap.get(appointmentId);
        if (appointmentInfo != null) {
            helper.setText(R.id.item_consul_record_disease_time_desc_tv, getTimeDes(appointmentInfo));
        } else {
            AppApplication.getInstance().getManager(ConsultManager.class).getAppointmentInfo(appointmentId, new ConsultManager.OnAppointmentInfoLoadListener() {
                @Override
                public void onSuccess(AppointmentInfo appointmentInfo) {
                    mAppointmentInfoMap.put(appointmentId, appointmentInfo);
                    helper.setText(R.id.item_consul_record_disease_time_desc_tv, getTimeDes(appointmentInfo));
                }

                @Override
                public void onFailed(int code, String msg) {
                    helper.setText(R.id.item_consul_record_disease_time_desc_tv, "");
                }

                @Override
                public void onFinish() {

                }
            });
        }
        PatientInfo patientInfo = mPatientMap.get(appointmentId);
        if (null != patientInfo && null != patientInfo.getPatientBaseInfo()) {
            helper.setText(R.id.item_consul_record_disease_name_desc_tv, patientInfo.getPatientBaseInfo().getFirstCureResult());
        } else {
            AppApplication.getInstance().getManager(PatientManager.class).getPatientInfo(appointmentId, new PatientManager.OnPatientInfoLoadListener() {
                @Override
                public void onSuccess(PatientInfo patientInfo) {
                    if (null != patientInfo && null != patientInfo.getPatientBaseInfo()) {
                        mPatientMap.put(appointmentId, patientInfo);
                        helper.setText(R.id.item_consul_record_disease_name_desc_tv, patientInfo.getPatientBaseInfo().getFirstCureResult());
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    helper.setText(R.id.item_consul_record_disease_name_desc_tv, "");
                }

                @Override
                public void onFinish() {

                }
            });
        }
    }

    /**
     * 获取时间描述
     *
     * @param appointmentInfo
     * @return
     */
    private String getTimeDes(AppointmentInfo appointmentInfo) {
        String timeTitle = "";
        if (appointmentInfo != null && appointmentInfo.getBaseInfo() != null) {
            if (appointmentInfo.getBaseInfo().getPredictCureDt() == null || appointmentInfo.getBaseInfo().getPredictCureDt().contains("2099")) {
                timeTitle = "时间待定";
            } else {
                timeTitle = appointmentInfo.getBaseInfo().getPredictCureDt();
            }
        }
        return timeTitle;
    }
}
