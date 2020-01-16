package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;

/**
 * @author lm_Abiao
 * @date 2019/5/31 11:38
 * @description:
 */
public class DoctorVisitFilterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public DoctorVisitFilterAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_doctor_list_filter_time_tv, item);
        helper.addOnClickListener(R.id.item_doctor_list_filter_time_iv);
    }
}
