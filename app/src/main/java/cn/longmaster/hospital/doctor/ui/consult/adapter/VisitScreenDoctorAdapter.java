package cn.longmaster.hospital.doctor.ui.consult.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDoctorInfo;

/**
 * 销售顾问的医生筛选Adapter
 * Created by W·H·K on 2017/12/25.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class VisitScreenDoctorAdapter extends BaseQuickAdapter<VisitDoctorInfo, VisitScreenDoctorAdapter.VisitScreenDoctorViewHolder> {
    private int mSelectId = 0;
    private Context mContext;
    private OnScreenItemClickListener mOnScreenItemClickListener;

    public VisitScreenDoctorAdapter(Context context, @LayoutRes int layoutResId) {
        super(layoutResId);
        this.mContext = context;
    }

    @Override
    protected void convert(VisitScreenDoctorViewHolder helper, final VisitDoctorInfo item) {
        if (item.getUserId() == mSelectId) {
            helper.mLayoutLl.setBackgroundResource(R.drawable.bg_solid_8c43adf6_stroke_45aef8_radius_40);
            helper.mScreenTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
        } else {
            helper.mScreenTv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_45aef8_b4defc));
            helper.mLayoutLl.setBackgroundResource(R.drawable.bg_solid_00000000_stroke_45aef8_radius_40);

        }
        helper.mScreenTv.setText(item.getRealName());
        helper.mStarIv.setSelected(!TextUtils.isEmpty(item.getStartDt()));
        helper.mRedPointIv.setVisibility(item.getAppCount() > 0 ? View.VISIBLE : View.GONE);
        helper.mLayoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectId(item.getUserId());
                if (mOnScreenItemClickListener != null) {
                    mOnScreenItemClickListener.onItemClick(item);
                }
            }
        });
        helper.mStarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnScreenItemClickListener != null) {
                    mOnScreenItemClickListener.onStarClick(item, getData().indexOf(item));
                }
            }
        });
    }

    public void setSelectId(int id) {
        this.mSelectId = id;
        notifyDataSetChanged();
    }

    public int getSelectId() {
        return mSelectId;
    }

    public void setOnScreenItemClickListener(OnScreenItemClickListener onScreenItemClickListener) {
        this.mOnScreenItemClickListener = onScreenItemClickListener;
    }

    class VisitScreenDoctorViewHolder extends BaseViewHolder {
        public LinearLayout mLayoutLl;
        public TextView mScreenTv;
        public ImageView mStarIv;
        public ImageView mRedPointIv;

        public VisitScreenDoctorViewHolder(View itemView) {
            super(itemView);
            mLayoutLl = (LinearLayout) itemView.findViewById(R.id.item_visit_screen_doctor_layout_ll);
            mScreenTv = (TextView) itemView.findViewById(R.id.item_visit_screen_doctor_text_tv);
            mStarIv = (ImageView) itemView.findViewById(R.id.item_visit_screen_doctor_star_iv);
            mRedPointIv = (ImageView) itemView.findViewById(R.id.item_visit_screen_doctor_red_point_iv);
        }
    }

    public interface OnScreenItemClickListener {
        void onItemClick(VisitDoctorInfo visitDoctorInfo);

        void onStarClick(VisitDoctorInfo visitDoctorInfo, int position);
    }
}
