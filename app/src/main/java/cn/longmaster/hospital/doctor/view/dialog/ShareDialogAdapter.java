package cn.longmaster.hospital.doctor.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;

/**
 * 分享Adapter
 * Created by Yang² on 2017/11/14.
 */

public class ShareDialogAdapter extends SimpleBaseAdapter<ShareItem, ShareDialogAdapter.ViewHolder> {
    public ShareDialogAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_share_dialog;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, ShareItem shareItem, int position) {
        viewHolder.imgIv.setImageResource(shareItem.resId);
        viewHolder.titleTv.setText(shareItem.title);
        if (getCount() <= 4) {
            viewHolder.lineView.setVisibility(View.GONE);
        } else {
            if (position / 4 < (getCount() - 1) / 4) {
                viewHolder.lineView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.lineView.setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_share_dialog_img_iv)
        private ImageView imgIv;
        @FindViewById(R.id.item_share_dialog_title_tv)
        private TextView titleTv;
        @FindViewById(R.id.item_share_dialog_line_view)
        private View lineView;
    }
}
