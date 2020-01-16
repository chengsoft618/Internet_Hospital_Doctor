package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyRoomPatientMedical;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/25 15:54
 * @description:
 */
public class DCDutyRoomPatientPicAdapter extends BaseQuickAdapter<DCDutyRoomPatientMedical, BaseViewHolder> {

    public DCDutyRoomPatientPicAdapter(int layoutResId, @Nullable List<DCDutyRoomPatientMedical> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyRoomPatientMedical item) {
        ImageView itemDcDutyRoomPatientPicPicIv = helper.getView(R.id.item_dc_duty_room_patient_pic_pic_iv);
        GlideUtils.showImage(itemDcDutyRoomPatientPicPicIv, mContext, AppConfig.getFirstJourneyUrl() + item.getFileName());
    }
}
