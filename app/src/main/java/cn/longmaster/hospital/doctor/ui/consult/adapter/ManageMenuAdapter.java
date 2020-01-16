package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consult.ManageMenuInfo;

/**
 * 管理菜单适配器
 * Created by JinKe on 2016-09-07.
 */
public class ManageMenuAdapter extends SimpleBaseAdapter<ManageMenuInfo, ManageMenuAdapter.ViewHolder> {
    private OnMenuItemClickListener mMenuItemClickListener;

    public ManageMenuAdapter(Context context, OnMenuItemClickListener listener) {
        super(context);
        mMenuItemClickListener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_manage_menu;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, final ManageMenuInfo manageMenuInfo, int position) {
        if (getCount() > 0) {
            if (position == 0) {
                viewHolder.mItemName.setBackgroundResource(R.drawable.bg_btn_manage_menu_top);
            } else if (position == getCount() - 1) {
                viewHolder.mItemName.setBackgroundResource(R.drawable.bg_btn_manage_menu_bottom);
            } else {
                viewHolder.mItemName.setBackgroundResource(R.drawable.bg_btn_manage_menu_middle);
            }
        } else {
            viewHolder.mItemName.setBackgroundResource(R.drawable.bg_btn_manage_menu_single);
        }
        viewHolder.mItemName.setText(manageMenuInfo.getItemName());
        viewHolder.mItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuItemClickListener.onMenuItemClick(manageMenuInfo.getItemId(), manageMenuInfo.isClickable());
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_manage_menu_title)
        private TextView mItemName;
    }

    public interface OnMenuItemClickListener {
        public void onMenuItemClick(int itemId, boolean isClickable);
    }
}
