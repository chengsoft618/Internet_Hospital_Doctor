package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DepartmentListInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/10/21 13:52
 * @description:
 */
public class AddDepartmentAdapter extends BaseQuickAdapter<DepartmentListInfo, BaseViewHolder> {
    public AddDepartmentAdapter(int layoutResId, @Nullable List<DepartmentListInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartmentListInfo item) {
        helper.setText(R.id.item_add_intention_department_tv, item.getDepartmentName());
        helper.addOnClickListener(R.id.item_add_intention_department_del_iv);
    }
}
