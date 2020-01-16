package cn.longmaster.hospital.doctor.ui.rounds.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;

/**
 * Created by W·H·K on 2019/2/15.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class RoundsReturnVisitAdapter extends RecyclerView.Adapter<RoundsReturnVisitAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mList;
    private OnItemClickListener mOnItemClickListener;

    public RoundsReturnVisitAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rounds_return_visit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /*holder.numTv.setText(mList.get(position));
        holder.detailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });*/
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
        TextView numTv;
        TextView timeTv;
        TextView expertsTv;
        TextView stateTv;
        TextView detailsTv;

        public ViewHolder(View itemView) {
            super(itemView);
           /* numTv = (TextView) itemView.findViewById(R.id.item_rounds_record_medical_num_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_rounds_record_medical_time_tv);
            expertsTv = (TextView) itemView.findViewById(R.id.item_rounds_record_medical_experts_tv);
            stateTv = (TextView) itemView.findViewById(R.id.item_rounds_record_medical_state_tv);
            detailsTv = (TextView) itemView.findViewById(R.id.item_rounds_record_medical_details_tv);*/
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
