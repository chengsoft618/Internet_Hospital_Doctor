package cn.longmaster.hospital.doctor.ui;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.DisplayUtil;

/**
 * 相机拍摄预览底部小图Adapter
 * Created by Yang² on 2017/9/22.
 */
public class PreviewBottomAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PreviewBottomAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView photoIv = helper.getView(R.id.item_preview_bottom_photo_aiv);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) photoIv.getLayoutParams();
        if (getData().get(0).equals(item)) {
            lp.setMargins(DisplayUtil.dip2px(5), 0, DisplayUtil.dip2px(5), 0);
        } else {
            lp.setMargins(0, 0, DisplayUtil.dip2px(5), 0);
        }
        photoIv.setLayoutParams(lp);
        GlideUtils.showImage(photoIv, mContext, item);
    }
}
