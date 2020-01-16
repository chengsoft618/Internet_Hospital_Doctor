package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.doctor.RecmdInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/6/12 14:53
 * @description:
 */
public class DoctorRecmdReasenAdapter extends BaseQuickAdapter<RecmdInfo, BaseViewHolder> {
    public DoctorRecmdReasenAdapter(int layoutResId, @Nullable List<RecmdInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecmdInfo item) {

    }
}
