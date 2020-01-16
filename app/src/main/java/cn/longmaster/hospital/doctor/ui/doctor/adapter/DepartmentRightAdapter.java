package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;


/**
 * @author lm_Abiao
 * @date 2019/5/28 13:38
 * @description: 二级科室适配器
 */
public class DepartmentRightAdapter extends BaseQuickAdapter<DepartmentInfo, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public DepartmentRightAdapter(int layoutResId, @Nullable List<DepartmentInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartmentInfo item) {
        helper.setText(R.id.tv_branch_small_name, item.getDepartmentName());
        if (null != array && array.get(helper.getLayoutPosition())) {
            helper.setTextColor(R.id.tv_branch_small_name, ContextCompat.getColor(mContext, R.color.color_333333));
        } else {
            helper.setTextColor(R.id.tv_branch_small_name, ContextCompat.getColor(mContext, R.color.color_666666));
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
