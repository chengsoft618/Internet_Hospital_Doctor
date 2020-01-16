package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListItemInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/10/22 11:36
 * @description:
 */
public class HomeConsultationAdapter extends BaseQuickAdapter<OrderListItemInfo, BaseViewHolder> {
    public HomeConsultationAdapter(int layoutResId, @Nullable List<OrderListItemInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListItemInfo item) {

    }
}
