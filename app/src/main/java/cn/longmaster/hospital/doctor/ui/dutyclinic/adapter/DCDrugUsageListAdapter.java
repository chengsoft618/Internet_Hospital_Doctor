package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugUsageDosageInfo;

/**
 * 值班门诊-处方用法用量列表适配器
 * Created by yangyong on 2019-12-04.
 */
public class DCDrugUsageListAdapter extends BaseQuickAdapter<DCDrugUsageDosageInfo, BaseViewHolder> {
    public DCDrugUsageListAdapter(int layoutResId, @Nullable List<DCDrugUsageDosageInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDrugUsageDosageInfo item) {
        if (item.isSelected()) {
            helper.setBackgroundColor(R.id.item_dcdrug_usage_item_ll, AppApplication.getInstance().getResources().getColor(R.color.color_f2f2f2));
        } else {
            helper.setBackgroundColor(R.id.item_dcdrug_usage_item_ll, AppApplication.getInstance().getResources().getColor(R.color.color_white));
        }
        helper.setText(R.id.item_dcdrug_usage_title_tv, item.getTitle());
    }
}
