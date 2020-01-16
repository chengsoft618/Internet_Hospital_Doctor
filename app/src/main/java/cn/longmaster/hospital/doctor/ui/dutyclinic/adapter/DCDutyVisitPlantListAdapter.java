package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantItem;

/**
 * @author ABiao_Abiao
 * @date 2019/12/18 15:51
 * @description:
 */
public class DCDutyVisitPlantListAdapter extends BaseQuickAdapter<DCDutyVisitPlantItem, BaseViewHolder> {

    public DCDutyVisitPlantListAdapter(int layoutResId, @Nullable List<DCDutyVisitPlantItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyVisitPlantItem item) {
        helper.setText(R.id.item_du_duty_plant_name, item.getTempName());
    }
}
