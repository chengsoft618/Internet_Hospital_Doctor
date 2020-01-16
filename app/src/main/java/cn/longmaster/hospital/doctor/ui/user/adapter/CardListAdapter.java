package cn.longmaster.hospital.doctor.ui.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.user.BankCardInfo;

/**
 * 银行卡列表适配器
 * Created by Yang² on 2016/10/25.
 */
public class CardListAdapter extends SimpleBaseAdapter<BankCardInfo, CardListAdapter.ViewHolder> {
    private Context mContext;
    private int mSelectPosition = 0;
    private OnCardListClickListener mOnCardListClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public CardListAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_card;
    }

    public void setOnCardListClickListener(OnCardListClickListener onCardListClickListener) {
        this.mOnCardListClickListener = onCardListClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    protected void bindView(ViewHolder viewHolder, final BankCardInfo bankCardInfo, final int position) {
        if (bankCardInfo.getIsDefault() == AppConstant.AccountState.USING_STATE) {
            viewHolder.mSelectIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mSelectIv.setVisibility(View.GONE);
        }
        viewHolder.disPlayBankCard(bankCardInfo);

        viewHolder.mLayoutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCardListClickListener != null) {
                    mOnCardListClickListener.onCardSelect(mSelectPosition == position, position);
                }

            }
        });
        viewHolder.mLayoutLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    mOnItemLongClickListener.onItemLongClickListener(position, bankCardInfo.getIsDefault() != 1);
                }
                return false;
            }
        });
    }

    @NonNull
    @Override
    protected ViewHolder onNewViewHolder() {
        return new ViewHolder();
    }

    private String cutCardNum(String str) {
        if (str.length() > 4) {
            return str.substring(str.length() - 4);
        }
        return str;
    }

    public class ViewHolder {
        @FindViewById(R.id.item_card_layout_ll)
        private LinearLayout mLayoutLl;
        @FindViewById(R.id.item_card_icon_iv)
        private ImageView mIconIv;
        @FindViewById(R.id.item_card_name_tv)
        private TextView mNameTv;
        @FindViewById(R.id.item_card_info_tv)
        private TextView mInfoTv;
        @FindViewById(R.id.item_card_select_iv)
        private ImageView mSelectIv;
        @FindViewById(R.id.item_cardholder_tv)
        private TextView mCardholderTv;

        private void disPlayBankCard(BankCardInfo bankCardInfo) {
            switch (bankCardInfo.getPayType()) {
                case AppConstant.PayType.PAY_TYPE_BANK_CARD:
                    mNameTv.setText(R.string.bank_card);
                    mInfoTv.setText(mContext.getString(R.string.bank_card_num_tip, cutCardNum(bankCardInfo.getCardNum())));
                    mIconIv.setImageResource(R.drawable.ic_logo_union_pay);
                    mCardholderTv.setText(mContext.getString(R.string.account_withdraw_cardholder, bankCardInfo.getRealName()));
                    break;

                case AppConstant.PayType.PAY_TYPE_ALI_PAY:
                    mNameTv.setText(R.string.ali_pay);
                    mInfoTv.setText(mContext.getString(R.string.account_withdraw_account_number_format, bankCardInfo.getCardNum()));
                    mCardholderTv.setText(mContext.getString(R.string.account_withdraw_account_name, bankCardInfo.getRealName()));
                    mIconIv.setImageResource(R.drawable.ic_logo_ali_pay);
                    break;

                case AppConstant.PayType.PAY_TYPE_WEI_CHAT:
                    mNameTv.setText(R.string.wei_chat);
                    mCardholderTv.setText(mContext.getString(R.string.account_withdraw_weixin_nickname, bankCardInfo.getRealName()));
                    mInfoTv.setText(mContext.getString(R.string.account_withdraw_account_number_format, bankCardInfo.getCardNum()));
                    mIconIv.setImageResource(R.drawable.ic_logo_wei_chat);
                    break;
            }
        }
    }

    public interface OnCardListClickListener {
        void onCardSelect(boolean isSelect, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(int position, boolean click);
    }
}
