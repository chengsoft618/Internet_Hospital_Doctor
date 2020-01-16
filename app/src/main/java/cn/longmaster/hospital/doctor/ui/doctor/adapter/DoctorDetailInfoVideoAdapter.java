package cn.longmaster.hospital.doctor.ui.doctor.adapter;

import android.support.annotation.Nullable;

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
public class DoctorDetailInfoVideoAdapter extends BaseQuickAdapter<DoctorStyleInfo.VideoDataBean, BaseViewHolder> {
    public DoctorDetailInfoVideoAdapter(int layoutResId, @Nullable List<DoctorStyleInfo.VideoDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorStyleInfo.VideoDataBean item) {
        //helper.setText(R.id.item_doctor_detail_doctor_video_iv,item.getUrlLink());
        helper.setText(R.id.item_doctor_detail_doctor_video_tv, item.getContent());
    }
}
