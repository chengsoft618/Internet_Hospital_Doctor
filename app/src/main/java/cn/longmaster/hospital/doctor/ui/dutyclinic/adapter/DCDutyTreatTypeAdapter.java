package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyTreatType;

/**
 * @author ABiao_Abiao
 * @date 2019/12/28 16:28
 * @description:
 */
public class DCDutyTreatTypeAdapter extends BaseQuickAdapter<DCDutyTreatType, BaseViewHolder> {
    public DCDutyTreatTypeAdapter(int layoutResId, @Nullable List<DCDutyTreatType> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyTreatType item) {
        helper.setText(R.id.item_dc_duty_treat_type_name_tv, item.getName());
    }
}
