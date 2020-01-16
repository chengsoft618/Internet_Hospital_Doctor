package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.ConfirmListItem;

/**
 * @author ABiao_Abiao
 * @date 2019/7/4 17:32
 * @description: 确认单
 */
public class ConfirmListAdapter extends BaseQuickAdapter<ConfirmListItem, BaseViewHolder> {
    public ConfirmListAdapter(int layoutResId, @Nullable List<ConfirmListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConfirmListItem item) {
        helper.setText(R.id.item_confirm_list_from_hospital_name_tv, item.getAttHospitalName());
        helper.setText(R.id.item_confirm_list_from_department_name_tv, item.getAttDepartmentName());

        helper.setText(R.id.item_confirm_list_to_hospital_name_tv, item.getHospitalName());
        helper.setText(R.id.item_confirm_list_to_department_name_tv, item.getDepartmentName());
        helper.setText(R.id.item_confirm_list_to_doctor_name_tv, item.getDoctorName());
        if (item.getType() == 1) {
            helper.setText(R.id.item_confirm_list_type_desc_tv, "会诊");
        } else if (item.getType() == 2) {
            helper.setText(R.id.item_confirm_list_type_desc_tv, "查房");
        } else {
            helper.setText(R.id.item_confirm_list_type_desc_tv, "未知");
        }

        helper.setText(R.id.item_confirm_list_order_num_desc_tv, item.getAppointmentId());
        helper.setText(R.id.item_confirm_list_pay_money_desc_tv, item.getPayValue());

    }

    public void changeItemName(int position, String newName) {
        getData().get(position).setHospitalName(newName);
    }
}
