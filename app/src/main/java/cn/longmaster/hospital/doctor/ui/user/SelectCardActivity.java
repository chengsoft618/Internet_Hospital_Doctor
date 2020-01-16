package cn.longmaster.hospital.doctor.ui.user;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.user.BankCardInfo;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.account.WithdrawCashActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.CardListAdapter;

/**
 * 选择账户
 * Created by Yang² on 2016/10/24.
 */
public class SelectCardActivity extends BaseActivity {
    private final int REQUEST_CODE_ADD_ACCOUNT = 200;
    @FindViewById(R.id.activity_select_card_list_lv)
    private ListView mCardListLv;
    @AppApplication.Manager
    private AccountManager mAccountManager;
    private CardListAdapter mCardListAdapter;
    private List<BankCardInfo> mBankcardInfoList = new ArrayList<>();
    private int mAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        ViewInjecter.inject(this);
        initData();
        getBankCards();
        initAdapter();
        iniItemOnclick();
        iniItemLongClick();
    }

    private void initData() {
        mAccountId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, 0);
    }

    private void showDeleteDialog(final int position) {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_delete_message_dialog, null);
        TextView mDeleteText = (TextView) layout.findViewById(R.id.layout_delete_message_text_tv);
        mDeleteText.setText(getString(R.string.account_withdraw_delete_account));
        final Dialog dialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        TextView mDelete = (TextView) layout.findViewById(R.id.layout_delete_message_text_tv);
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = createProgressDialog(getString(R.string.data_committing));
                mAccountManager.deleteBankAccount(mBankcardInfoList.get(position).getCardNum(), mAccountId, new OnResultListener<Void>() {
                    @Override
                    public void onResult(BaseResult baseResult, Void aVoid) {
                        progressDialog.cancel();
                        if (baseResult.getCode() == RESULT_SUCCESS) {
                            mBankcardInfoList.remove(position);
                            mCardListAdapter.setData(mBankcardInfoList);
                            showToast(R.string.data_delete_success);
                        } else {
                            showToast(R.string.no_network_connection);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 获取银行卡信息
     */
    private void getBankCards() {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        mAccountManager.getBankCards(mAccountId, new OnResultListener<List<BankCardInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<BankCardInfo> bankCardInfos) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (bankCardInfos.size() > 0) {
                        iniSort(bankCardInfos);
                        mBankcardInfoList.addAll(bankCardInfos);
                        mCardListAdapter.setData(bankCardInfos);
                    }
                } else {
                    showToast(R.string.no_network_connection);
                }
                if (progressDialog.isShowing()) {
                    progressDialog.cancel();
                }
            }
        });
    }

    private void initAdapter() {
        mCardListAdapter = new CardListAdapter(this);
        mCardListAdapter.setData(mBankcardInfoList);
        mCardListLv.setAdapter(mCardListAdapter);
    }

    private void iniItemOnclick() {
        mCardListAdapter.setOnCardListClickListener(new CardListAdapter.OnCardListClickListener() {
            @Override
            public void onCardSelect(boolean isSelect, int position) {
                Intent intent = new Intent();
                intent.setClass(SelectCardActivity.this, WithdrawCashActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BANK_CARD_INFO, mBankcardInfoList.get(position));
                setResult(RESULT_OK, intent);
                mAccountManager.setDefaultAccount(mBankcardInfoList.get(position).getCardNum(), mAccountId, new OnResultListener<Void>() {
                    @Override
                    public void onResult(BaseResult baseResult, Void aVoid) {
                        if (baseResult.getCode() == RESULT_SUCCESS) {
                            SelectCardActivity.this.finish();
                        } else {
                            showToast(R.string.no_network_connection);
                        }
                    }
                });
            }
        });
    }

    private void iniItemLongClick() {
        mCardListAdapter.setOnItemLongClickListener(new CardListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(int position, boolean click) {
                if (click) {
                    showDeleteDialog(position);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ACCOUNT && resultCode == RESULT_OK) {
            mBankcardInfoList.clear();
            getBankCards();
        }
    }

    public void rightBtnClick(View view) {
        Intent intent = new Intent(SelectCardActivity.this, AddAccountActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, mAccountId);
        startActivityForResult(intent, REQUEST_CODE_ADD_ACCOUNT);
    }

    public void iniSort(List<BankCardInfo> bankCardInfo) {
        if (bankCardInfo.size() > 0) {
            Collections.sort(bankCardInfo, new Comparator<BankCardInfo>() {
                @Override
                public int compare(BankCardInfo lhs, BankCardInfo rhs) {
                    int res = 0;
                    long l;
                    long r;
                    l = DateUtil.dateToMillisecond(lhs.getInsertDt());
                    r = DateUtil.dateToMillisecond(rhs.getInsertDt());
                    if (l != r) {
                        res = l < r ? 1 : -1;
                    }
                    return res;
                }
            });
        }
    }
}

