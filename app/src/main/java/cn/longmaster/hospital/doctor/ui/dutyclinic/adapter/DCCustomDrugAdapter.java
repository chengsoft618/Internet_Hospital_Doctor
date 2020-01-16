package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugInfo;

/**
 * 值班门诊-自定义药品用法用量适配器
 * Created by yangyong on 2019-12-06.
 */
public class DCCustomDrugAdapter extends BaseQuickAdapter<DCDrugInfo, BaseViewHolder> {

    public DCCustomDrugAdapter(int layoutResId, @Nullable List<DCDrugInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDrugInfo item) {
        helper.setText(R.id.item_dcissue_order_custom_drug_drug_name_tv, item.getDrugName());
        helper.setText(R.id.item_dcissue_order_custom_drug_drug_usage_tv, "用法：" + item.getCycle() + item.getCycleNum());
        helper.setText(R.id.item_dcissue_order_custom_drug_drug_dosage_tv, "用量：每次" + item.getDose() + item.getDoseUnit());
        helper.addOnClickListener(R.id.item_dcissue_order_custom_drug_drug_usage_ll);
        helper.addOnClickListener(R.id.item_dcissue_order_custom_drug_drug_dosage_ll);
    }

    public void replace(int position, DCDrugInfo info) {
        if (position < getItemCount()) {
            getData().set(position, info);
        }
        notifyItemChanged(position);
    }
}
