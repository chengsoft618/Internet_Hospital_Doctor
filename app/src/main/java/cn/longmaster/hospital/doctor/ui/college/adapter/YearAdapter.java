package cn.longmaster.hospital.doctor.ui.college.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.college.YearInfo;

/**
 * 文献筛选年份适配器
 * <p>
 * Created by W·H·K on 2018/3/20.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    private Context mContext;
    private List<YearInfo> mList;
    private OnItemClickListener mOnItemClickListener;

    public YearAdapter(Context context, List<YearInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_guide_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Logger.logI(Logger.COMMON, "onBindViewHolder:" + mList.get(position).isSelected());
        holder.textView.setText(mList.get(position).getMatYear());
        if (mList.get(position).isSelected()) {
            holder.screenView.setBackgroundResource(R.color.color_ebeff0);
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_45aef8));
        } else {
            holder.screenView.setBackgroundResource(R.color.white);
            holder.textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666));
        }
        holder.itemV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        LinearLayout itemV;
        LinearLayout screenView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_guide_screen_tv);
            itemV = (LinearLayout) itemView.findViewById(R.id.item_guide_screen);
            screenView = (LinearLayout) itemView.findViewById(R.id.item_guide_screen_view);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
