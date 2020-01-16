package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientItem;
import cn.longmaster.utils.LibCollections;

/**
 * @author ABiao_Abiao
 * @date 2019/12/25 14:09
 * @description:
 */
public class DCDutyRoomPatientAdapter extends BaseQuickAdapter<DCDutyRoomPatientItem, BaseViewHolder> {
    public DCDutyRoomPatientAdapter(int layoutResId, @Nullable List<DCDutyRoomPatientItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyRoomPatientItem item) {
        helper.setText(R.id.item_patient_name_tv, item.getPatientName());
        helper.setText(R.id.item_patient_pic_num_tv, "共" + LibCollections.size(item.getCheckList()) + "张资料图片");
    }
}
