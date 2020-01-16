package cn.longmaster.hospital.doctor.ui.department.adpter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;

/**
 * @author lm_Abiao
 * @date 2019/5/30 13:45
 * @description: 根据省市筛选出的医院
 */
public class DepartmentSearchAdapter extends BaseQuickAdapter<DepartmentInfo, BaseViewHolder> {

    public DepartmentSearchAdapter(int layoutResId, @Nullable List<DepartmentInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartmentInfo item) {
        helper.setText(R.id.item_department_search_list_tv, item.getDepartmentName());
    }
}
