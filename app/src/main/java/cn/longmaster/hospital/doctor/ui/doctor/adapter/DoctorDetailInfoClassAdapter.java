package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorStyleInfo;


/**
 * @author Mloong_Abiao
 * @date 2019/6/6 13:41
 * @description:
 */
public class DoctorDetailInfoClassAdapter extends BaseQuickAdapter<DoctorStyleInfo.MaterialDataBean, BaseViewHolder> {
    private SparseBooleanArray array = new SparseBooleanArray();

    public DoctorDetailInfoClassAdapter(int layoutResId, @Nullable List<DoctorStyleInfo.MaterialDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorStyleInfo.MaterialDataBean item) {
        //helper.setText(R.id.item_doctor_detail_doctor_class_iv, item.getPicUrl());
        helper.setText(R.id.item_doctor_detail_doctor_class_tv, item.getMaterialName());
        if (null != array && array.get(helper.getLayoutPosition())) {
            helper.setTextColor(R.id.item_doctor_detail_doctor_class_tv, ContextCompat.getColor(mContext, R.color.color_45aef8));
        } else {
            helper.setTextColor(R.id.item_doctor_detail_doctor_class_tv, ContextCompat.getColor(mContext, R.color.color_333333));
        }
    }

    public void setSelected(int position) {
        array = new SparseBooleanArray(getData().size());
        array.put(position, true);
        notifyDataSetChanged();
    }
}
