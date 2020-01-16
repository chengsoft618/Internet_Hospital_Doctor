package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.user.RelationMedicalInfo;

/**
 * Created by W·H·K on 2018/7/27.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class AlreadyRelationAdapter extends RecyclerView.Adapter<AlreadyRelationAdapter.ViewHolder> {
    private Context mContext;
    private List<RelationMedicalInfo> mList = new ArrayList<>();
    private OnCancelRelationClickListener mOnCancelRelationClickListener;

    public AlreadyRelationAdapter(Context context, List<RelationMedicalInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnCancelRelationClickListener(OnCancelRelationClickListener listener) {
        this.mOnCancelRelationClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_already_relation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.num.setText(mContext.getString(R.string.rounds_appointment_num, mList.get(position).getAppointmentId() + ""));
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCancelRelationClickListener != null) {
                    mOnCancelRelationClickListener.onItemCancelClick(v, position);
                }
            }
        });
    }

    public void addData(List<RelationMedicalInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItemData(RelationMedicalInfo info) {
        mList.add(0, info);
        notifyDataSetChanged();
    }

    public void setData(List<RelationMedicalInfo> list) {
        mList.clear();
        mList = list;
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
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
        TextView cancel;
        TextView num;

        public ViewHolder(View itemView) {
            super(itemView);
            cancel = (TextView) itemView.findViewById(R.id.item_already_relation_cancel);
            num = (TextView) itemView.findViewById(R.id.item_already_relation_num);
        }
    }

    public interface OnCancelRelationClickListener {
        void onItemCancelClick(View view, int position);
    }
}
