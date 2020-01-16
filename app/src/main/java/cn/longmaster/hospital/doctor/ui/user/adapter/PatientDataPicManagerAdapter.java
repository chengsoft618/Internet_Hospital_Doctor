package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/4 11:39
 * @description: 患者材料-图片管理
 */
public class PatientDataPicManagerAdapter extends BaseQuickAdapter<AuxiliaryMaterialInfo, BaseViewHolder> {
    public PatientDataPicManagerAdapter(int layoutResId, @Nullable List<AuxiliaryMaterialInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuxiliaryMaterialInfo item) {
        ImageView ivMeteraial = helper.getView(R.id.item_patient_data_manager_pics_iv);
        GlideUtils.showImage(ivMeteraial, mContext, AppConfig.getMaterialDownloadUrl() + item.getMaterialPic());
        helper.setText(R.id.item_patient_data_manager_pics_tv, StringUtils.isEmpty(item.getMaterialName()) ? "未命名-" + helper.getLayoutPosition() : item.getMaterialName());
    }
}
