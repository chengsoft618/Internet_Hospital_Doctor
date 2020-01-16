package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.DischargedSummaryInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/9/20 15:46
 * @description:
 */
public class DischargedSummaryAdapter extends BaseQuickAdapter<DischargedSummaryInfo, BaseViewHolder> {
    public DischargedSummaryAdapter(int layoutResId, @Nullable List<DischargedSummaryInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DischargedSummaryInfo item) {
        ImageView imageView = helper.getView(R.id.item_patient_data_summary_manager_pics_iv);
        GlideUtils.showImage(imageView, mContext, AppConfig.getDfsUrl() + "3037/0/" + item.getFileName());
    }
}
