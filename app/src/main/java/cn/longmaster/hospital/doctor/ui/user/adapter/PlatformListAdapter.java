package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.PlatformListItem;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/10 16:58
 * @description:
 */
public class PlatformListAdapter extends BaseQuickAdapter<PlatformListItem, BaseViewHolder> {
    public PlatformListAdapter(int layoutResId, @Nullable List<PlatformListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlatformListItem item) {
        if (item.getCheckState() == 1) {
            //通过
            helper.setBackgroundRes(R.id.item_platform_list_approval_type_v, R.drawable.bg_solid_45aef8_radius_10);
            helper.setText(R.id.item_platform_list_approval_type_tv, "审核通过");
            helper.setTextColor(R.id.item_platform_list_approval_type_tv, ContextCompat.getColor(mContext, R.color.color_45aef8));
        } else if (item.getCheckState() == 2) {
            //不通过
            helper.setBackgroundRes(R.id.item_platform_list_approval_type_v, R.drawable.bg_solid_ff6666_radius_10);
            helper.setText(R.id.item_platform_list_approval_type_tv, "审核不通过");
            helper.setTextColor(R.id.item_platform_list_approval_type_tv, ContextCompat.getColor(mContext, R.color.color_ff6666));
        } else {
            //等待审核
            helper.setBackgroundRes(R.id.item_platform_list_approval_type_v, R.drawable.bg_solid_00db86_radius_10);
            helper.setText(R.id.item_platform_list_approval_type_tv, "等待审核");
            helper.setTextColor(R.id.item_platform_list_approval_type_tv, ContextCompat.getColor(mContext, R.color.color_00db86));
        }
        helper.setText(R.id.item_platform_list_date_desc_tv, TimeUtils.string2String(item.getAmtDt(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()), new SimpleDateFormat("yyyy年MM月", Locale.getDefault())));
        helper.setText(R.id.item_platform_list_from_hospital_name_tv, item.getHospitalName());
        helper.setText(R.id.item_platform_list_from_department_name_tv, item.getDepartmentName());

        helper.setText(R.id.item_platform_list_type_desc_tv, "平台服务费");

        helper.setText(R.id.item_platform_list_pay_money_desc_tv, item.getAdvanceValue());
    }
}
