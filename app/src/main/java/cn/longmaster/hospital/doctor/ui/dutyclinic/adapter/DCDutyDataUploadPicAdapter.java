package cn.longmaster.hospital.doctor.ui.dutyclinic.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPrognoteDataInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/20 9:52
 * @description: 添加患者病程图片适配器
 */
public class DCDutyDataUploadPicAdapter extends BaseQuickAdapter<DCDutyPrognoteDataInfo, BaseViewHolder> {
    private boolean mod;

    public DCDutyDataUploadPicAdapter(int layoutResId, @Nullable List<DCDutyPrognoteDataInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DCDutyPrognoteDataInfo item) {
        ImageView itemDcDutyDataUpLoadPicIv = helper.getView(R.id.item_dc_duty_data_up_load_pic_iv);
        GlideUtils.showImage(itemDcDutyDataUpLoadPicIv, mContext,  AppConfig.getFirstJourneyUrl() + item.getMaterialPic());
        //helper.setGone(R.id.item_dc_duty_data_up_load_pic_del_iv, !mod);
        helper.addOnClickListener(R.id.item_dc_duty_data_up_load_pic_del_iv);
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }
}
