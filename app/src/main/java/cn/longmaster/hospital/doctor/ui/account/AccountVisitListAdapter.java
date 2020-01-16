package cn.longmaster.hospital.doctor.ui.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;

/**
 * Created by W·H·K on 2018/12/18.
 */

public class AccountVisitListAdapter extends BaseAdapter {
    private Context mContext;
    private List<AccountListInfo> mList;

    public AccountVisitListAdapter(Context context, List<AccountListInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setData(List<AccountListInfo> appointBriefs) {
        this.mList = appointBriefs;
        notifyDataSetChanged();
    }

    public void addData(List<AccountListInfo> appointBriefs) {
        this.mList.addAll(appointBriefs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_account_visit_list, null);
        final ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        displayView(viewHolder, position);
        return convertView;
    }

    private void displayView(ViewHolder viewHolder, int position) {
        viewHolder.mNumberTv.setText(mList.get(position).getAppointmentId() + "");
        viewHolder.mVideoTimeTv.setText(CommonlyUtil.conversionTime(mList.get(position).getCureDt()));
        viewHolder.mOperationTimeTv.setText(CommonlyUtil.conversionTime3(mList.get(position).getUpdateDt()));
        if (mList.get(position).getRole() == 1) {
            viewHolder.mHospitalTv.setText(mContext.getString(R.string.account_roots_hospital));
            viewHolder.mExpertsTv.setText(mList.get(position).getLaunchHospitalName());
        } else {
            viewHolder.mHospitalTv.setText(mContext.getString(R.string.chat_role_superior_doctor) + ": ");
            viewHolder.mExpertsTv.setText(mList.get(position).getDoctorName());
        }
        viewHolder.mCashTv.setText(" " + mContext.getString(R.string.my_account_decimal, mList.get(position).getIncomeValue()) + "元");
        displayState(viewHolder, position);
    }

    private void displayState(ViewHolder viewHolder, int position) {
        viewHolder.mStateTv.setVisibility(View.GONE);
        viewHolder.mOperationTimeTv.setVisibility(View.GONE);
        viewHolder.mStateView.setVisibility(View.GONE);
        switch (mList.get(position).getState()) {
            case AppConstant.AccountListState.ACCOUNT_LIST_PENDING_SETTLEMENT:
                viewHolder.mStateView.setVisibility(View.VISIBLE);
                viewHolder.mStateView.setText(mContext.getString(R.string.account_no_settlement));
                break;

            case AppConstant.AccountListState.ACCOUNT_LIST_PAID:
                viewHolder.mStateTv.setVisibility(View.VISIBLE);
                viewHolder.mOperationTimeTv.setVisibility(View.VISIBLE);
                viewHolder.mStateTv.setText(mContext.getString(R.string.account_wait_cash));
                viewHolder.mStateTv.setBackgroundResource(R.drawable.bg_solid_ffa61b_radius_3);
                break;

            case AppConstant.AccountListState.ACCOUNT_LIST_CASHING:
                viewHolder.mStateTv.setVisibility(View.VISIBLE);
                viewHolder.mOperationTimeTv.setVisibility(View.VISIBLE);
                viewHolder.mStateTv.setText(mContext.getString(R.string.account_cashing));
                viewHolder.mStateTv.setBackgroundResource(R.drawable.bg_solid_0bd014_radius_3);
                break;

            case AppConstant.AccountListState.ACCOUNT_LIST_WAIT_CASH:
                viewHolder.mStateTv.setVisibility(View.VISIBLE);
                viewHolder.mOperationTimeTv.setVisibility(View.VISIBLE);
                viewHolder.mStateTv.setText(mContext.getString(R.string.account_payment));
                viewHolder.mStateTv.setBackgroundResource(R.drawable.bg_solid_0096ff_radius_3);
                break;

            case AppConstant.AccountListState.ACCOUNT_LIST_WAIT_UNTREATED:
            case AppConstant.AccountListState.ACCOUNT_LIST_UNTREATEDING:
            case AppConstant.AccountListState.ACCOUNT_LIST_UNTREATED_FINISH:
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNumberTv;
        TextView mVideoTimeTv;
        TextView mExpertsTv;
        TextView mCashTv;
        TextView mStateTv;
        TextView mOperationTimeTv;
        TextView mStateView;
        TextView mHospitalTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mNumberTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_number_tv);
            mVideoTimeTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_video_time_tv);
            mExpertsTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_experts_tv);
            mCashTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_cash_tv);
            mStateTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_state_tv);
            mOperationTimeTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_time_tv);
            mStateView = (TextView) itemView.findViewById(R.id.item_account_visit_list_state_view);
            mHospitalTv = (TextView) itemView.findViewById(R.id.item_account_visit_list_hospital_tv);
        }
    }
}
