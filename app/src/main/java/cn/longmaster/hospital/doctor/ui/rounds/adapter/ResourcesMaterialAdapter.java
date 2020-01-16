package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderFileInfo;

/**
 * Created by W·H·K on 2018/5/17.
 * Mod by biao on 2019/12/03
 */

public class ResourcesMaterialAdapter extends BaseQuickAdapter<OrderFileInfo, BaseViewHolder> {
    public ResourcesMaterialAdapter(int layoutResId, @Nullable List<OrderFileInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OrderFileInfo item) {
        helper.setText(R.id.item_rounds_tech_material_name_tv, item.getRemark());
        if (item.getType() == 1) {
            helper.setImageResource(R.id.item_rounds_tech_material_iv, R.drawable.ic_ppj);
        } else if (item.getType() == 2) {
            helper.setImageResource(R.id.item_rounds_tech_material_iv, R.drawable.ic_mp4);
        }
    }
}
