package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentItemInfo;


/**
 * @author lm_Abiao
 * @date 2019/5/28 13:38
 * @description:
 */
public class DepartmentLeftAdapter extends BaseQuickAdapter<DepartmentItemInfo, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public DepartmentLeftAdapter(int layoutResId, @Nullable List<DepartmentItemInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartmentItemInfo item) {
        helper.setText(R.id.tv_branch_big_name, item.getParentDepartment());
        if (null != array && array.get(helper.getLayoutPosition())) {
            helper.setVisible(R.id.iv_branch_big_select, true);
            helper.setTextColor(R.id.tv_branch_big_name, ContextCompat.getColor(mContext, R.color.color_049eff));
        } else {
            helper.setGone(R.id.iv_branch_big_select, false);
            helper.setTextColor(R.id.tv_branch_big_name, ContextCompat.getColor(mContext, R.color.color_1a1a1a));
        }
    }

    public void setSelected(int position) {
        array = new SparseBooleanArray(getData().size());
        array.put(position, true);
        notifyDataSetChanged();
    }

    public void clearSelected() {
        for (int i = 0; i < array.size(); i++) {
            array.put(i, false);
        }
        notifyDataSetChanged();
    }
}
