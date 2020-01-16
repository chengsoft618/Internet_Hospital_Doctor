package cn.longmaster.hospital.doctor.ui.hospital.adaprer;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;

/**
 * @author lm_Abiao
 * @date 2019/5/30 13:45
 * @description: 根据省市筛选出的医院
 */
public class HospitalFilterAdapter extends BaseQuickAdapter<HospitalInfo, BaseViewHolder> {

    public HospitalFilterAdapter(int layoutResId, @Nullable List<HospitalInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalInfo item) {
        helper.setText(R.id.item_hospital_filter_list_tv, item.getHospitalName());
    }
}
