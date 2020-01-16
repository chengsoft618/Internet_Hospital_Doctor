package cn.longmaster.hospital.doctor.ui.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.FinanceStatisticInfo;
import cn.longmaster.hospital.doctor.core.manager.consult.ConsultManager;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.dialog.InformedConsentDialog;
import cn.longmaster.utils.SPUtils;

/**
 * 我的账户
 * Created by Yang² on 2016/6/2.
 */
public class MyAccountActivity extends BaseActivity {
    private final String TAG = MyAccountActivity.class.getSimpleName();
    private final int REQUEST_CODE_WITHDRAW_CASH = 200;

    @FindViewById(R.id.activity_my_account_balance_tv)
    private TextView mBalanceTv;//账户余额
    @FindViewById(R.id.activity_my_account_radio_group)
    private RadioGroup mRadioGroup;

    @AppApplication.Manager
    private ConsultManager mConsultManager;
    @AppApplication.Manager
    private AccountManager mAccountManager;
    private float mVailavble;

    private AccountFlowFragment mAccountFlowFragment;
    private AccountListFragment mAccountListFragment;
    private Fragment mCurrentFragment;
    private int mAccountId;
    private boolean mFromMyDoctor;

    public int getAccountId() {
        return mAccountId;
    }

    public void setAccountId(int mAccountId) {
        this.mAccountId = mAccountId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ViewInjecter.inject(this);
        initData();
        initView();
        initFragment();
        getAccountData();
    }

    private void initData() {
        mAccountId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, 0);
        //mAccountId = 1004344;
        mFromMyDoctor = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_MY_DOCTOR, false);
        setAccountId(mAccountId);
    }

    private void initView() {
        if (SPUtils.getInstance().getBoolean(AppPreference.KEY_IS_FIRST_MY_ACCOUNT, true)) {
            showAgreementDialog();
        }
    }

    private void initFragment() {
        if (mFromMyDoctor) {
            mRadioGroup.check(R.id.activity_my_account_list_btn);
            initAccountListFragment(R.id.activity_my_account_list_btn);
        } else {
            mRadioGroup.check(R.id.activity_my_account_flow_btn);
            initAccountFlowFragment(R.id.activity_my_account_flow_btn);
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.activity_my_account_flow_btn:
                        initAccountFlowFragment(checkedId);
                        break;
                    case R.id.activity_my_account_list_btn:
                        initAccountListFragment(checkedId);
                        break;
                }
            }
        });
    }

    private void initAccountFlowFragment(int checkedId) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            if (mAccountFlowFragment == null) {
                mAccountFlowFragment = new AccountFlowFragment();
                transaction.add(R.id.activity_my_account_fragment_layout, mAccountFlowFragment, checkedId + "");
            }
            hideFragment(transaction);
            transaction.show(mAccountFlowFragment);
            transaction.commitAllowingStateLoss();
            mCurrentFragment = mAccountFlowFragment;
            mAccountFlowFragment.setOnFinanceStatisticListener(new AccountFlowFragment.OnFinanceStatisticRequesterListener() {
                @Override
                public void onFinanceStatisticRequester(BaseResult baseResult, FinanceStatisticInfo financeStatisticInfo) {
                    if (baseResult.getCode() == 0) {
                        mBalanceTv.setText(String.format(getString(R.string.my_account_balance), financeStatisticInfo.getAvailaValue()));
                        mVailavble = financeStatisticInfo.getAvailaValue();
                    }
                }
            });
        }
    }

    private void initAccountListFragment(int checkedId) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            if (mAccountListFragment == null) {
                mAccountListFragment = new AccountListFragment();
                transaction.add(R.id.activity_my_account_fragment_layout, mAccountListFragment, checkedId + "");
            }
            hideFragment(transaction);
            transaction.show(mAccountListFragment);
            transaction.commitAllowingStateLoss();
            mCurrentFragment = mAccountListFragment;
        }
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (mAccountFlowFragment != null) {
            transaction.hide(mAccountFlowFragment);
        }
        if (mAccountListFragment != null) {
            transaction.hide(mAccountListFragment);
        }
    }

    private void getAccountData() {
        mAccountManager.getFinanceStatistic(mAccountId, new OnResultListener<FinanceStatisticInfo>() {
            @Override
            public void onResult(BaseResult baseResult, FinanceStatisticInfo financeStatisticInfo) {
                if (baseResult.getCode() == RESULT_SUCCESS) {
                    mBalanceTv.setText(String.format(getString(R.string.my_account_balance), financeStatisticInfo.getAvailaValue()));
                    mVailavble = financeStatisticInfo.getAvailaValue();
                } else {
                    showToast(R.string.no_network_connection);
                }
            }
        });
    }

    @OnClick({R.id.activity_my_account_cash_btn})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.activity_my_account_cash_btn:
                intent.setClass(this, WithdrawCashActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_AVAILA_VALUE, mVailavble);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, mAccountId);
                startActivityForResult(intent, REQUEST_CODE_WITHDRAW_CASH);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WITHDRAW_CASH && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    public void rightClick(View view) {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, getResources().getString(R.string.account_service_description));
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getServerDescUrl());
        startActivity(intent);
    }

    /**
     * 显示协议
     */
    private void showAgreementDialog() {
        InformedConsentDialog.Builder builder = new InformedConsentDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_user_informed_consent)
                .setMessage(R.string.my_account_agreement_content)
                .setButton(R.string.confirm, new InformedConsentDialog.OnBtnClickListener() {
                    @Override
                    public void onBtnClicked() {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        SPUtils.getInstance().put(AppPreference.KEY_IS_FIRST_MY_ACCOUNT, false);
                    }
                }).show();
    }
}
