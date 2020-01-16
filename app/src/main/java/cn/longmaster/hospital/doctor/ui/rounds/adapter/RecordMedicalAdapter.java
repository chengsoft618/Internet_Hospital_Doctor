package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsAssociatedMedicalInfo;

/**
 * Created by W·H·K on 2019/2/15.
 */
public class RecordMedicalAdapter extends BaseQuickAdapter<RoundsAssociatedMedicalInfo, BaseViewHolder> {

    public RecordMedicalAdapter(int layoutResId, @Nullable List<RoundsAssociatedMedicalInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoundsAssociatedMedicalInfo item) {
        helper.setText(R.id.item_rounds_record_medical_num_tv, item.getMedicalId() + "");
        helper.setText(R.id.item_rounds_record_medical_time_tv, item.getCureDt());
        helper.setText(R.id.item_rounds_record_medical_experts_tv, item.getDoctorName());
        helper.setText(R.id.item_rounds_record_medical_state_tv, item.getAppointmentStatRemark());
        helper.addOnClickListener(R.id.item_rounds_record_medical_details_tv);
    }
}
