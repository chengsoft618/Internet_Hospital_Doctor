package cn.longmaster.hospital.doctor.ui.college.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.college.GuideDataInfo;

/**
 * 指南文库适配器
 * Created by W·H·K on 2018/3/19.
 */
public class GuideLiteratureAdapter extends BaseQuickAdapter<GuideDataInfo, BaseViewHolder> {

    public GuideLiteratureAdapter(int layoutResId, @Nullable List<GuideDataInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuideDataInfo item) {
        helper.setText(R.id.item_guide_literature_tv, item.getMaterialName());
    }
}
