package cn.longmaster.hospital.doctor.ui.hospital.adaprer;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.ProvinceInfo;

/**
 * @author lm_Abiao
 * @date 2019/5/28 13:38
 * @description:
 */
public class ProvinceAdapter extends BaseQuickAdapter<ProvinceInfo, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public ProvinceAdapter(int layoutResId, @Nullable List<ProvinceInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProvinceInfo item) {
        helper.setText(R.id.tv_province_name, item.getProvince());
        if (null != array && array.get(helper.getLayoutPosition())) {
            helper.setVisible(R.id.iv_province_select, true);
            helper.setTextColor(R.id.tv_province_name, ContextCompat.getColor(mContext, R.color.color_45aef8));
        } else {
            helper.setGone(R.id.iv_province_select, false);
            helper.setTextColor(R.id.tv_province_name, ContextCompat.getColor(mContext, R.color.color_333333));
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
