package cn.longmaster.hospital.doctor.ui.im;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.longmaster.hospital.doctor.R;

/**
 * Created by YY on 17/8/15.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class BottomFunctionAdapter extends BaseQuickAdapter<BottomFunctionInfo, BottomFunctionAdapter.BottomFunctionViewHolder> {
    private Context mContext;

    public BottomFunctionAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<BottomFunctionInfo> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BottomFunctionViewHolder helper, BottomFunctionInfo item) {
        Drawable drawable = ResourcesCompat.getDrawable(mContext.getResources(), item.getIcon(), null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        helper.functionView.setCompoundDrawables(null, drawable, null, null);
        helper.functionView.setText(item.getTitle());
    }

    class BottomFunctionViewHolder extends BaseViewHolder {
        public TextView functionView;

        public BottomFunctionViewHolder(View view) {
            super(view);
            functionView = (TextView) view.findViewById(R.id.chat_bottom_function_textview);
        }
    }
}
