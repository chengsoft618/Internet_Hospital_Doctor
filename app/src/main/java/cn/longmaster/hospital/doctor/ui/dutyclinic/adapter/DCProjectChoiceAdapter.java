package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;

/**
 * 值班门诊-项目选择弹窗适配器
 * Created by yangyong on 2019-11-29.
 */
public class DCProjectChoiceAdapter extends BaseQuickAdapter<DCProjectInfo, BaseViewHolder> {
    public DCProjectChoiceAdapter(int layoutResId, @Nullable List<DCProjectInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DCProjectInfo item) {
        helper.setText(R.id.item_dcproject_choice_window_title_tv, item.getItemName());
    }
}
