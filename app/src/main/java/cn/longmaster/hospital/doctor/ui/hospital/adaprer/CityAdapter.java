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
public class CityAdapter extends BaseQuickAdapter<ProvinceInfo.CityListBean, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public CityAdapter(int layoutResId, @Nullable List<ProvinceInfo.CityListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProvinceInfo.CityListBean item) {
        if (null != array && array.get(helper.getLayoutPosition())) {
            helper.setVisible(R.id.item_double_click_city_is_selected_iv, true);
            helper.setTextColor(R.id.item_double_click_city_name_tv, ContextCompat.getColor(mContext, R.color.color_049eff));
        } else {
            helper.setGone(R.id.item_double_click_city_is_selected_iv, false);
            helper.setTextColor(R.id.item_double_click_city_name_tv, ContextCompat.getColor(mContext, R.color.color_1a1a1a));
        }
        helper.setText(R.id.item_double_click_city_name_tv, item.getCity());
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
