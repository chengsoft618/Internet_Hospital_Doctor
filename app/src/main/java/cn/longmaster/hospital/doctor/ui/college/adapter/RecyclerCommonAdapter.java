package cn.longmaster.hospital.doctor.ui.college.adapter;

/**
 * Created by Administrator on 2017/5/28.
 * RecyclerView通用adapter
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.longmaster.hospital.doctor.ui.college.listener.ItemClickListener;
import cn.longmaster.hospital.doctor.ui.college.view.ViewHolder;

// TODO: 2019/6/13  RecyclerView.Adapter
public abstract class RecyclerCommonAdapter<DATA> extends RecyclerView.Adapter<ViewHolder> {
    //条目布局
    private int mLayoutId;
    private List<DATA> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public RecyclerCommonAdapter(Context context, List<DATA> data, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mData = data;
        this.mLayoutId = layoutId;
    }

    public void setData(List<DATA> data) {
        mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建view
        View view = mInflater.inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //绑定数据
        bindData(holder, mData.get(position), position);
        //条目点击事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(mData.get(position), position);
                }
            });
        }
    }

    /**
     * 将必要参数传递出去
     *
     * @param holder
     * @param data
     * @param position
     */
    protected abstract void bindData(ViewHolder holder, DATA data, int position);

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //使用接口回调点击事件
    private ItemClickListener mItemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
