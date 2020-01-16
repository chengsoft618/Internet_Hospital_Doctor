package cn.longmaster.hospital.doctor.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.account.AccountCashInfo;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.entity.account.CashListInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.FinanceStatisticInfo;
import cn.longmaster.hospital.doctor.core.entity.user.BankCardInfo;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.account.CashListRequester;
import cn.longmaster.hospital.doctor.core.requests.account.CashRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.user.AddAccountActivity;
import cn.longmaster.hospital.doctor.ui.user.SelectCardActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.StringUtils;

/**
 * 余额提现
 * Created by Yang² on 2016/6/2.
 */
public class WithdrawCashActivity extends BaseActivity {
    private final int REQUEST_CODE_SELECT_CARD = 200;
    private final int ADDCCOUNT_CODE_SELECT_CARD = 300;

    @FindViewById(R.id.activity_withdraw_cash_layout_ll)
    private LinearLayout mCashLy;
    @FindViewById(R.id.activity_withdraw_add_account_layout_ll)
    private LinearLayout mAddAccountLy;
    @FindViewById(R.id.activity_withdraw_cash_icon_iv)
    private ImageView mIconIv;
    @FindViewById(R.id.activity_withdraw_cash_name_tv)
    private TextView mNameTv;
    @FindViewById(R.id.activity_withdraw_cash_card_number_tv)
    private TextView mCardNumberTv;
    @FindViewById(R.id.activity_withdraw_account_name_tv)
    private TextView mAccountNameTv;
    @FindViewById(R.id.activity_withdraw_recycler_view)
    private RecyclerView mRecyclerView;

    @AppApplication.Manager
    private AccountManager mAccountManager;
    public String mBankNo;//提现银行卡号
    public int mPayType;//银行账户类型 1：银行卡; 2：支付宝; 3：微信
    public int mSettlementType;//申请人身份类型 1：后台管理员; 2：医生; 3：患者
    private List<AccountCashInfo> mWithdrawCashList = new ArrayList<>();
    private List<Integer> mIncomeIds = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private WithdrawCashAdapter mWithdrawCashAdapter;
    private float mTotalAmount = 0;
    private int mAccountId;
    private TextView mTotalAmountTv1;
    private RelativeLayout mCommitBtn;
    private AccountCashAdapter mFooterAdapter;
    private List<Integer> mOtherIncomeIdList = new ArrayList<>();
    private float mAvailaValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cash);
        ViewInjecter.inject(this);
        initData();
        initView();
        getBankCards();
        getCashList();
    }

    private void initData() {
        //mAvailaValue = getIntent().getFloatExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_AVAILA_VALUE, 0);
        mAccountId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, 0);
        mSettlementType = AppConstant.IdentityType.IDENTITY_TYPE_DOCTOR;
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 获取银行卡信息
     */
    private void getBankCards() {
        mAccountManager.getBankCards(mAccountId, new OnResultListener<List<BankCardInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<BankCardInfo> bankCardInfos) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    if (bankCardInfos.size() > 0) {
                        mCashLy.setVisibility(View.VISIBLE);
                        mAddAccountLy.setVisibility(View.GONE);
                        fillData(bankCardInfos);
                    } else {
                        mCashLy.setVisibility(View.GONE);
                        mAddAccountLy.setVisibility(View.VISIBLE);
                    }
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
    }

    private void getCashList() {
        final ProgressDialog progressDialog = createProgressDialog(getString(R.string.loading_data));
        CashListRequester requester = new CashListRequester(new OnResultListener<CashListInfo>() {
            @Override
            public void onResult(BaseResult baseResult, CashListInfo cashListInfo) {
                Logger.logD(Logger.COMMON, "WithdrawCashActivity->CashListRequester-->baseResult：" + baseResult + ",accountListInfo：" + cashListInfo);
                progressDialog.cancel();
                if (baseResult.getCode() == RESULT_SUCCESS && cashListInfo != null) {
                    initCashInfo(cashListInfo);
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.account = mAccountId;
        requester.doPost();
    }

    private void initCashInfo(CashListInfo cashListInfo) {
        List<AccountListInfo> incomeList = cashListInfo.getIncomeList();
        List<AccountListInfo> otherList = cashListInfo.getOtherList();
        List<AccountListInfo> arrearList = cashListInfo.getArrearList();
        if (incomeList.size() > 0) {
            for (int i = 0; i < incomeList.size(); i++) {
                AccountCashInfo info = new AccountCashInfo();
                info.setIncomeId(incomeList.get(i).getIncomeId());
                info.setUserId(incomeList.get(i).getUserId());
                info.setType(incomeList.get(i).getType());
                info.setRole(incomeList.get(i).getRole());
                info.setAppointmentId(incomeList.get(i).getAppointmentId());
                info.setIncomeValue(incomeList.get(i).getIncomeValue());
                info.setRemark(incomeList.get(i).getRemark());
                info.setInsertDt(incomeList.get(i).getInsertDt());
                info.setDoctorName(incomeList.get(i).getDoctorName());
                info.setLaunchHospitalName(incomeList.get(i).getLaunchHospitalName());
                info.setCureDt(incomeList.get(i).getCureDt());
                info.setCashType(1);
                info.setSelected(true);
                mWithdrawCashList.add(info);
            }
        }
        if (otherList.size() > 0) {
            float incomeValue = 0;
            for (int i = 0; i < otherList.size(); i++) {
                mOtherIncomeIdList.add(otherList.get(i).getIncomeId());
                incomeValue += otherList.get(i).getIncomeValue();
            }
            AccountCashInfo otherInfo = new AccountCashInfo();
            otherInfo.setIncomeValue(incomeValue);
            otherInfo.setCashType(2);
            otherInfo.setSelected(true);
            mWithdrawCashList.add(otherInfo);
        }
        if (arrearList.size() > 0) {
            for (int i = 0; i < arrearList.size(); i++) {
                AccountCashInfo info = new AccountCashInfo();
                info.setIncomeId(arrearList.get(i).getIncomeId());
                info.setIncomeValue(arrearList.get(i).getIncomeValue());
                info.setRemark(arrearList.get(i).getRemark());
                info.setCureDt(arrearList.get(i).getCureDt());
                info.setInsertDt(arrearList.get(i).getInsertDt());
                info.setCashType(3);
                info.setSelected(true);
                mWithdrawCashList.add(info);
            }
        }
        for (int i = 0; i < mWithdrawCashList.size(); i++) {
            if (mWithdrawCashList.get(i).isSelected()) {
                mTotalAmount += mWithdrawCashList.get(i).getIncomeValue();
            }
        }
        setWithdrawCashAdapter();
    }

    private void setWithdrawCashAdapter() {
        mWithdrawCashAdapter = new WithdrawCashAdapter(this, mWithdrawCashList);
        mFooterAdapter = new AccountCashAdapter(mWithdrawCashAdapter);
        View footerView = LayoutInflater.from(this).inflate(R.layout.view_account_cash, null, false);
        displayFooterView(footerView);
        mFooterAdapter.addFooterView(footerView);
        mRecyclerView.setAdapter(mFooterAdapter);
        mFooterAdapter.setOnItemClickListener(new AccountCashAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mWithdrawCashList.get(position).getCashType() == 3) {
                    return;
                }
                if (mWithdrawCashList.get(position).isSelected()) {
                    mWithdrawCashList.get(position).setSelected(false);
                    mTotalAmount -= mWithdrawCashList.get(position).getIncomeValue();
                } else {
                    mWithdrawCashList.get(position).setSelected(true);
                    mTotalAmount += mWithdrawCashList.get(position).getIncomeValue();
                }
                if (mTotalAmount == 0) {
                    mTotalAmountTv1.setText((int) mTotalAmount + "");
                } else {
                    mTotalAmountTv1.setText(getString(R.string.my_account_decimal, mTotalAmount));
                }
                mFooterAdapter.notifyDataSetChanged();
            }
        });
    }

    private void displayFooterView(View footerView) {
        mTotalAmountTv1 = (TextView) footerView.findViewById(R.id.view_total_amount_tv);
        mCommitBtn = (RelativeLayout) footerView.findViewById(R.id.view_commit_btn);
        LinearLayout topView = (LinearLayout) footerView.findViewById(R.id.view_top_view);
        if (mWithdrawCashList.size() == 0) {
            topView.setVisibility(View.GONE);
        }
        if (mTotalAmount == 0) {
            mTotalAmountTv1.setText((int) mTotalAmount + "");
        } else {
            mTotalAmountTv1.setText(getString(R.string.my_account_decimal, mTotalAmount));
        }
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(mBankNo)) {
                    showToast("请先添加银行卡账户");
                    return;
                }
                if (mTotalAmount < 0) {
                    showAgreementDialog();
                } else if (mTotalAmount == 0) {
                    showToast("提现金额不能为0");
                } else {
                    getAccountAvailable();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_CARD && resultCode == RESULT_OK) {
            BankCardInfo bankCardInfo = (BankCardInfo) data.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BANK_CARD_INFO);
            disPlayBankCard(bankCardInfo);
        } else if (requestCode == ADDCCOUNT_CODE_SELECT_CARD && resultCode == RESULT_OK) {
            getBankCards();
        }
    }

    @OnClick({R.id.activity_withdraw_cash_layout_ll,
            R.id.activity_withdraw_add_account_layout_ll})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.activity_withdraw_cash_layout_ll:
                intent.setClass(this, SelectCardActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, mAccountId);
                startActivityForResult(intent, REQUEST_CODE_SELECT_CARD);
                break;

            case R.id.activity_withdraw_add_account_layout_ll:
                intent.setClass(this, AddAccountActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, mAccountId);
                startActivityForResult(intent, ADDCCOUNT_CODE_SELECT_CARD);
                break;
        }
    }

    /**
     * 如果有默认银行卡，则选默认的银行卡
     * 如果默认银行卡多余1个，则选择一个默认的银行卡
     * 如果没有默认的银行卡，则选择一个银行的银行卡
     * 如果没有银行的银行卡，则选择第一个银行卡
     *
     * @param bankCardInfoList
     */
    private void fillData(List<BankCardInfo> bankCardInfoList) {
        for (BankCardInfo bankCardInfo : bankCardInfoList) {
            if (bankCardInfo.getIsDefault() == AppConstant.AccountState.USING_STATE) {
                disPlayBankCard(bankCardInfo);
                return;
            }
        }
        for (BankCardInfo bankCardInfo : bankCardInfoList) {
            if (bankCardInfo.getPayType() == AppConstant.PayType.PAY_TYPE_BANK_CARD) {
                disPlayBankCard(bankCardInfo);
                return;
            }
        }
        disPlayBankCard(bankCardInfoList.get(0));
    }

    private void disPlayBankCard(BankCardInfo bankCardInfo) {
        mPayType = bankCardInfo.getPayType();
        switch (mPayType) {
            case AppConstant.PayType.PAY_TYPE_BANK_CARD:
                mNameTv.setText(R.string.bank_card);
                mCardNumberTv.setText(getString(R.string.bank_card_num_tip, cutCardNum(bankCardInfo.getCardNum())));
                mIconIv.setImageResource(R.drawable.ic_logo_union_pay);
                mAccountNameTv.setText(getString(R.string.account_withdraw_cardholder, bankCardInfo.getRealName()));
                break;

            case AppConstant.PayType.PAY_TYPE_ALI_PAY:
                mNameTv.setText(R.string.ali_pay);
                mCardNumberTv.setText(bankCardInfo.getCardNum());
                mIconIv.setImageResource(R.drawable.ic_logo_ali_pay);
                break;

            case AppConstant.PayType.PAY_TYPE_WEI_CHAT:
                mNameTv.setText(R.string.wei_chat);
                mCardNumberTv.setText(bankCardInfo.getCardNum());
                mIconIv.setImageResource(R.drawable.ic_logo_wei_chat);
                break;
        }
        mBankNo = bankCardInfo.getCardNum();
    }

    private String cutCardNum(String str) {
        if (str.length() > 4) {
            return str.substring(str.length() - 4);
        }
        return str;
    }

    private void showDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getActivity());
        builder.setMessage(R.string.withdraw_cash_success_dialog)
                .setNegativeButton(R.string.sure, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    private void balanceWithdrawal() {
        mIncomeIds.clear();
        for (int i = 0; i < mWithdrawCashList.size(); i++) {
            if (mWithdrawCashList.get(i).isSelected()) {
                if (mWithdrawCashList.get(i).getCashType() == 2) {
                    mIncomeIds.addAll(mOtherIncomeIdList);
                } else {
                    mIncomeIds.add(mWithdrawCashList.get(i).getIncomeId());
                }
            }
        }
        Logger.logD(Logger.COMMON, "WithdrawCashActivity->mIncomeIds:" + mIncomeIds);
        CashRequester requester = new CashRequester(new OnResultListener<Void>() {
            @Override
            public void onResult(BaseResult baseResult, Void aVoid) {
                Logger.logD(Logger.COMMON, "WithdrawCashActivity->baseResult:" + baseResult);
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
                if (baseResult != null) {
                    if (baseResult.getCode() == RESULT_SUCCESS) {
                        showDialog();
                    } else {
                        showToast(baseResult.getError());
                    }
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
        requester.bankNo = mBankNo;
        requester.opValue = mTotalAmount;
        requester.payType = mPayType;
        requester.settlementType = mSettlementType;
        requester.account = mAccountId;
        requester.incomeIds = incomeIdJsonArray(mIncomeIds);
        requester.doPost();
    }

    private String incomeIdJsonArray(List<Integer> incomeIds) {
        String str = "";
        for (int i = 0; i < incomeIds.size(); i++) {
            str += incomeIds.get(i) + ",";
        }
        Logger.logD(Logger.APPOINTMENT, "WithdrawCashActivity->incomeIdJsonArray:" + str);
        return str.substring(0, str.length() - 1);
    }

    private void showAgreementDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getActivity());
        builder.setMessage(R.string.account_cash_error)
                .setNegativeButton(R.string.close, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .show();
    }

    private void getAccountAvailable() {
        mProgressDialog = createProgressDialog(getString(R.string.loading_data));
        mAccountManager.getFinanceStatistic(mAccountId, new OnResultListener<FinanceStatisticInfo>() {
            @Override
            public void onResult(BaseResult baseResult, FinanceStatisticInfo financeStatisticInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mAvailaValue = financeStatisticInfo.getAvailaValue();
                    if (mTotalAmount > mAvailaValue) {
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }
                        showToast("您所选择的提现项目有误，请重新选择或联系客服为您处理。400-860-3939");
                    } else {
                        balanceWithdrawal();
                    }
                } else {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                    showToast(R.string.no_network_connection);
                }
            }
        });
    }
}
