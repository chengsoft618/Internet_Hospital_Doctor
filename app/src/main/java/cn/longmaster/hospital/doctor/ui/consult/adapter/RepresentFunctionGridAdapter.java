package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consultant.RepresentFunctionInfo;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * 销售代表功能Adapter
 * Created by Yang² on 2017/11/10.
 */

public class RepresentFunctionGridAdapter extends SimpleBaseAdapter<RepresentFunctionInfo, RepresentFunctionGridAdapter.ViewHolder> {
    private Context mContext;

    public RepresentFunctionGridAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_represent_function;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, RepresentFunctionInfo info, int position) {
        if (position == 0) {
            viewHolder.functionAsyncImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_consult_manage));
            viewHolder.functionTextView.setText(R.string.represent_function_consult_manage);
        } else {
            GlideUtils.showImage(viewHolder.functionAsyncImageView, mContext, info.getIcon());
            viewHolder.functionTextView.setText(info.getName());
        }
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    public class ViewHolder {
        @FindViewById(R.id.item_represent_function_img_aiv)
        private ImageView functionAsyncImageView;
        @FindViewById(R.id.item_represent_function_text_tv)
        private TextView functionTextView;
    }

}
