package cn.longmaster.hospital.doctor.ui.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.account.AccountCashInfo;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;

/**
 * Created by W·H·K on 2018/12/18.
 */
// TODO: 2019/6/13  RecyclerView.Adapter
public class WithdrawCashAdapter extends RecyclerView.Adapter<WithdrawCashAdapter.ViewHolder> {
    private Context mContext;
    private List<AccountCashInfo> mList;
    private OnItemClickListener mOnItemClickListener;

    public WithdrawCashAdapter(Context context, List<AccountCashInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void addData(List<AccountCashInfo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setData(List<AccountCashInfo> list) {
        mList.clear();
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_cash_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        goneView(holder);
        switch (mList.get(position).getCashType()) {
            case 1:
                displayNormalView(holder, position);
                break;

            case 2:
                displayOtherView(holder, position);
                break;

            case 3:
                displayArrearsView(holder, position);
                break;
        }
    }

    private void goneView(ViewHolder holder) {
        holder.mNormalView.setVisibility(View.GONE);
        holder.mOtherView.setVisibility(View.GONE);
        holder.mArrearsView.setVisibility(View.GONE);
    }

    private void displayNormalView(ViewHolder holder, final int position) {
        holder.mNormalView.setVisibility(View.VISIBLE);
        holder.mNormalNumberTv.setText(mList.get(position).getAppointmentId() + "");
        holder.mNormalTimeTv.setText(CommonlyUtil.conversionTime(mList.get(position).getCureDt()));
        if (mList.get(position).getRole() == 1) {
            holder.mHospitalView.setText(mContext.getString(R.string.account_roots_hospital));
            holder.mNormalExpertsTv.setText(mList.get(position).getLaunchHospitalName());
        } else {
            holder.mHospitalView.setText(mContext.getString(R.string.chat_role_superior_doctor) + ":");
            holder.mNormalExpertsTv.setText(mList.get(position).getDoctorName());
        }
        if (mList.get(position).getIncomeValue() == 0) {
            holder.mNormalServiceFeeTv.setText("0元");
        } else {
            holder.mNormalServiceFeeTv.setText(mContext.getString(R.string.my_account_decimal, mList.get(position).getIncomeValue()) + "元");
        }
        if (mList.get(position).isSelected()) {
            holder.mNormalSelectedIv.setImageResource(R.drawable.ic_withdraw_cash_selected);
        } else {
            holder.mNormalSelectedIv.setImageResource(R.drawable.ic_withdraw_cash_not_selected);
        }
        if (position + 1 == mList.size()) {
            holder.mNormalLineView.setBackgroundResource(R.color.white);
        } else {
            holder.mNormalLineView.setBackgroundResource(R.color.color_e5e5e5);
        }
        holder.mNormalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    private void displayOtherView(ViewHolder holder, final int position) {
        holder.mOtherView.setVisibility(View.VISIBLE);
        if (mList.get(position).getIncomeValue() == 0) {
            holder.mOtherServiceFeeTv.setText("0元");
        } else {
            holder.mOtherServiceFeeTv.setText(mContext.getString(R.string.my_account_decimal, mList.get(position).getIncomeValue()) + "元");
        }
        if (mList.get(position).isSelected()) {
            holder.mOtherSelectedIv.setImageResource(R.drawable.ic_withdraw_cash_selected);
        } else {
            holder.mOtherSelectedIv.setImageResource(R.drawable.ic_withdraw_cash_not_selected);
        }
        if (position + 1 == mList.size()) {
            holder.mOtherLineView.setBackgroundResource(R.color.white);
        } else {
            holder.mOtherLineView.setBackgroundResource(R.color.color_e5e5e5);
        }
        holder.mOtherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    private void displayArrearsView(ViewHolder holder, final int position) {
        holder.mArrearsView.setVisibility(View.VISIBLE);
        holder.mArrearsReasonTv.setText(mList.get(position).getRemark());
        holder.mArrearsTimeTv.setText(CommonlyUtil.conversionTime(mList.get(position).getCureDt()));
        if (mList.get(position).getIncomeValue() == 0) {
            holder.mArrearsServiceFeeTv.setText("0元");
        } else {
            holder.mArrearsServiceFeeTv.setText(mContext.getString(R.string.my_account_decimal, mList.get(position).getIncomeValue()) + "元");
        }
        holder.mArrearsSelectedIv.setImageResource(R.drawable.ic_withdraw_cash_selected);
        if (position + 1 == mList.size()) {
            holder.mArrearsLineView.setBackgroundResource(R.color.white);
        } else {
            holder.mArrearsLineView.setBackgroundResource(R.color.color_e5e5e5);
        }
        holder.mArrearsView.setOnClickListener(new View.OnClickListener() {
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
        //正常就诊明细view
        RelativeLayout mNormalView;
        ImageView mNormalSelectedIv;
        TextView mNormalNumberTv;
        TextView mNormalTimeTv;
        TextView mNormalExpertsTv;
        TextView mNormalServiceFeeTv;
        TextView mHospitalView;
        View mNormalLineView;
        //其他 view
        RelativeLayout mOtherView;
        ImageView mOtherSelectedIv;
        TextView mOtherServiceFeeTv;
        View mOtherLineView;
        //欠款
        RelativeLayout mArrearsView;
        ImageView mArrearsSelectedIv;
        TextView mArrearsReasonTv;
        TextView mArrearsTimeTv;
        TextView mArrearsServiceFeeTv;
        View mArrearsLineView;

        public ViewHolder(View itemView) {
            super(itemView);
            mNormalView = (RelativeLayout) itemView.findViewById(R.id.item_withdraw_cash_normal_view);
            mNormalSelectedIv = (ImageView) itemView.findViewById(R.id.item_withdraw_cash_selected_iv);
            mNormalNumberTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_number_tv);
            mNormalTimeTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_time_tv);
            mNormalExpertsTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_experts_tv);
            mNormalServiceFeeTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_normal_service_fee_iv);
            mNormalLineView = itemView.findViewById(R.id.item_withdraw_cash_normal_line_view);
            mHospitalView = (TextView) itemView.findViewById(R.id.item_withdraw_cash_hospital_tv);
            //
            mOtherView = (RelativeLayout) itemView.findViewById(R.id.item_withdraw_cash_other_view);
            mOtherSelectedIv = (ImageView) itemView.findViewById(R.id.item_withdraw_cash_other_selected_iv);
            mOtherServiceFeeTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_other_service_fee_iv);
            mOtherLineView = itemView.findViewById(R.id.item_withdraw_cash_other_line_view);
            //
            mArrearsView = (RelativeLayout) itemView.findViewById(R.id.item_withdraw_cash_arrears_view);
            mArrearsSelectedIv = (ImageView) itemView.findViewById(R.id.item_withdraw_cash_arrears_selected_iv);
            mArrearsReasonTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_arrears_reason_tv);
            mArrearsTimeTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_arrears_time_tv);
            mArrearsServiceFeeTv = (TextView) itemView.findViewById(R.id.item_withdraw_cash_arrears_service_fee_tv);
            mArrearsLineView = itemView.findViewById(R.id.item_withdraw_cash_arrears_line_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
