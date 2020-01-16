package cn.longmaster.hospital.doctor.ui.account;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by W·H·K on 2019/1/21.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class AccountCashAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();
    private OnItemClickListener mOnItemClickListener;
    private RecyclerView.Adapter mInnerAdapter;
    private WithdrawCashAdapter adapter;

    public AccountCashAdapter(RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        adapter = (WithdrawCashAdapter) mInnerAdapter;
        adapter.setOnItemClickListener(new WithdrawCashAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterViews.get(viewType) != null) {
            return new MyViewHolder(mFooterViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return getFootersCount() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterViewPos(position)) {
            return mFooterViews.keyAt(position - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position);
    }

    /**
     * 获取正常数据的size
     *
     * @return
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * 判断是否是Footer
     *
     * @param position
     * @return
     */
    private boolean isFooterViewPos(int position) {
        return position >= getRealItemCount();
    }

    public void addFooterView(View view) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public int getFootersCount() {
        return mFooterViews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}