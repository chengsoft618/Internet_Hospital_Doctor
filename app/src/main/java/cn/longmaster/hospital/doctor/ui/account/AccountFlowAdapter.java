package cn.longmaster.hospital.doctor.ui.account;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.account.AccountFlowInfo;
import cn.longmaster.hospital.doctor.util.CommonlyUtil;
import cn.longmaster.utils.StringUtils;

/**
 * Created by W·H·K on 2018/12/18.
 */

public class AccountFlowAdapter extends BaseAdapter {
    private Context mContext;
    private List<AccountFlowInfo> mList;
    private OnItemClickListener mOnItemClickListener;

    public AccountFlowAdapter(Context context, List<AccountFlowInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setData(List<AccountFlowInfo> appointBriefs) {
        this.mList = appointBriefs;
        notifyDataSetChanged();
    }

    public void addData(List<AccountFlowInfo> appointBriefs) {
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
        convertView = layoutInflater.inflate(R.layout.item_account_flow, null);
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

    private void displayView(ViewHolder viewHolder, final int position) {
        if (mList.get(position).getOrderValue() >= 0) {
            viewHolder.mMoneyTv.setText(String.format(mContext.getString(R.string.money_order_value), mList.get(position).getOrderValue()));
            viewHolder.mMoneyTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_0096ff));
        } else {
            viewHolder.mMoneyTv.setText(String.format(mContext.getString(R.string.money_order_value_minus), Math.abs(mList.get(position).getOrderValue())));
            viewHolder.mMoneyTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff4737));
        }
        viewHolder.mTimeTv.setText(CommonlyUtil.conversionTime2(mList.get(position).getInsertDt()));
        if (mList.get(position).getIsMerge() == 1) {
            viewHolder.mDetailView.setVisibility(View.GONE);
            viewHolder.mMergerCashDetailTv.setVisibility(View.VISIBLE);
            viewHolder.mTitleTv.setText(mContext.getString(R.string.account_merge_cash_withdrawal));
            viewHolder.mMergerCashDetailTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FlowDetailActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERIAL_ID, mList.get(position).getSerialId());
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_VALUE, mList.get(position).getOrderValue());
                    mContext.startActivity(intent);
                }
            });
        } else {
            viewHolder.mMergerCashDetailTv.setVisibility(View.GONE);
            viewHolder.mTitleTv.setText(mList.get(position).getReasonDesc());
            setTitleTv(viewHolder, position);
            if (mList.get(position).getReasonDesc().equals(mContext.getString(R.string.bill_reason_settlement_arrears))) {
                if (StringUtils.isEmpty(mList.get(position).getRemark())) {
                    viewHolder.mHospitalTv.setVisibility(View.GONE);
                } else {
                    viewHolder.mHospitalTv.setText(mList.get(position).getRemark());
                }
                viewHolder.mBusinessTimeTv.setText(mContext.getString(R.string.account_arrears_time, CommonlyUtil.conversionTime(mList.get(position).getCureDt())));
            } else {
                if (StringUtils.isEmpty(mList.get(position).getLaunchHospitalName()) && StringUtils.isEmpty(mList.get(position).getCureName())) {
                    viewHolder.mHospitalTv.setVisibility(View.GONE);
                } else {
                    if (mList.get(position).getRole() == 1) {
                        viewHolder.mHospitalTv.setText(StringUtils.isEmpty(mList.get(position).getLaunchHospitalName()) ? mList.get(position).getCureName() : mList.get(position).getLaunchHospitalName() + " " + mList.get(position).getCureName());
                    } else {
                        viewHolder.mHospitalTv.setText(StringUtils.isEmpty(mList.get(position).getDoctorName())
                                ? mList.get(position).getCureName() : mList.get(position).getDoctorName() + " " + mList.get(position).getCureName());
                    }
                }
                viewHolder.mBusinessTimeTv.setText(mContext.getString(R.string.account_business_time, CommonlyUtil.conversionTime(mList.get(position).getCureDt())));
            }
            if (mList.get(position).getAppointmentId() == 0) {
                viewHolder.mNumberTv.setVisibility(View.GONE);
            } else {
                viewHolder.mNumberTv.setText(mContext.getString(R.string.account_numb, mList.get(position).getAppointmentId() + ""));
            }
            if (StringUtils.isEmpty(mList.get(position).getCureDt())) {
                viewHolder.mBusinessTimeTv.setVisibility(View.GONE);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //劳务费View
        LinearLayout mServiceFeeView;
        LinearLayout mDetailView;
        TextView mTitleTv;
        TextView mTimeTv;
        TextView mMoneyTv;
        TextView mHospitalTv;
        TextView mNumberTv;
        TextView mBusinessTimeTv;
        TextView mMergerCashDetailTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mServiceFeeView = (LinearLayout) itemView.findViewById(R.id.item_account_flow_service_fee_view);
            mDetailView = (LinearLayout) itemView.findViewById(R.id.item_account_flow_detail_view);
            mTitleTv = (TextView) itemView.findViewById(R.id.item_account_flow_title_tv);
            mTimeTv = (TextView) itemView.findViewById(R.id.item_account_flow_time_tv);
            mMoneyTv = (TextView) itemView.findViewById(R.id.item_account_flow_money_tv);
            mHospitalTv = (TextView) itemView.findViewById(R.id.item_account_flow_hospital_tv);
            mNumberTv = (TextView) itemView.findViewById(R.id.item_account_flow_number_tv);
            mBusinessTimeTv = (TextView) itemView.findViewById(R.id.item_account_flow_business_time_tv);
            mMergerCashDetailTv = (TextView) itemView.findViewById(R.id.item_account_flow_merger_cash_detail_tv);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private void setTitleTv(ViewHolder viewHolder, int position) {
        switch (mList.get(position).getReason()) {
            case AppConstant.BillReason.BILL_REASON_PAY_FOR:
            case AppConstant.BillReason.BILL_REASON_WITHDRAW:
            case AppConstant.BillReason.BILL_REASON_COMMISSION:
            case AppConstant.BillReason.BILL_REASON_COMPENSATION:
            case AppConstant.BillReason.BILL_REASON_CHANGE_DOCTOR:
            case AppConstant.BillReason.BILL_REASON_SHARE_SUBSIDY:
            case AppConstant.BillReason.BILL_REASON_PROPAGANDA_SUBSIDY:
            case AppConstant.BillReason.BILL_REASON_UPLOAD_SUBSIDY:
            case AppConstant.BillReason.BILL_REASON_PATIENT_PAYMENT:
            case AppConstant.BillReason.BILL_REASON_PATIENT_CASH:
            case AppConstant.BillReason.BILL_REASON_PUNISH:
                viewHolder.mDetailView.setVisibility(View.VISIBLE);
                break;
            case AppConstant.BillReason.BILL_REASON_CONSUMPTION:
            case AppConstant.BillReason.BILL_REASON_PAYMENT:
            case AppConstant.BillReason.BILL_REASON_TOP_UP:
            case AppConstant.BillReason.BILL_REASON_HEALTH_INSURANCE:
            case AppConstant.BillReason.BILL_REASON_SIXTH_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_BEI_LIU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HU_ZHU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_MI_YANG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_JUN_KANG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_SCANNING_CODE_CASH:
            case AppConstant.BillReason.BILL_REASON_GUANG_YUAN_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HUI_HANG_303_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HUAI_BEI_KUANG_GONG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_PU_YANG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_WEI_SHAN_HU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HUAI_BEI_CHAO_YANG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_XIN_WEN_CENTER_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_JIANG_HAN_YOU_TIAN_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_FENG_ZHEN_ZHONG_MENG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_XI_SHUI_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_39_INTERNET_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_OUTPATIENT_TEST_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_TIE_LI_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_TIE_LING_CENTER_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_WU_WEI_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_MI_YANG_ZHONG_JING_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_NEI_JIANG_SIXTH_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_XING_BING_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_WN_SI_XIAN_FENG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HAI_DONG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_LAI_BING_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_WU_LA_TE_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_XI_XIAN_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_CANG_XI_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HE_SHAN_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_GUI_YANG_FOURTH_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_YI_FANG_SCIENCE_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_JIAN_GE_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HUI_RUI_HELP_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HEI_LONG_JIANG_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HUI_RUI_HUI_MIN_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_NAN_NING_SECOND_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HAI_NAN_ZANG_ZU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_HE_BI_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_VIDEO_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_XIN_ZHOU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_BANK_TRANSFER:
            case AppConstant.BillReason.BILL_REASON_GUANG_YUAN_LI_ZHOU_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_BEI_LIU_WEI_JI_WEI:
            case AppConstant.BillReason.BILL_REASON_GUANG_YUAN_CENTER_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_SOUTH_HOSPITAL:
            case AppConstant.BillReason.BILL_REASON_CONSUMPTION_FAIL:
            case AppConstant.BillReason.BILL_REASON_REFUND:
            case AppConstant.BillReason.BILL_REASON_AWARD:
                viewHolder.mDetailView.setVisibility(View.GONE);
                break;
        }
    }
}
