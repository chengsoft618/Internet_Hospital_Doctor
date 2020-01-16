package cn.longmaster.hospital.doctor.ui.account;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.account.AccountFlowInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.FinanceStatisticInfo;
import cn.longmaster.hospital.doctor.core.manager.user.AccountManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.account.AccountFlowRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.view.pullview.PullListView;
import cn.longmaster.hospital.doctor.view.pullview.PullToRefreshLayout;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2019/1/17.
 */

public class AccountFlowFragment extends NewBaseFragment {
    @FindViewById(R.id.layout_account_flow_pull_refresh_layout)
    private PullToRefreshLayout mPullToRefreshLayout;
    @FindViewById(R.id.layout_account_flow_recycler_view)
    private PullListView mPullListView;
    @FindViewById(R.id.layout_account_flow_empty_layout)
    private LinearLayout mEmptyLl;

    @AppApplication.Manager
    private AccountManager mAccountManager;
    private List<AccountFlowInfo> mUserBillInfos = new ArrayList<>();
    private AccountFlowAdapter mAdapter;
    private int mAccountId;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private int mIsFinish = 0;
    private OnFinanceStatisticRequesterListener mOnFinanceStatisticListener;
    private ProgressDialog mProgressDialog;


    @Override
    protected void initDatas() {
        super.initDatas();
        mAccountId = ((MyAccountActivity) getActivity()).getAccountId();
        Logger.logD(Logger.COMMON, "AccountFlowFragment->mAccountId:" + mAccountId);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_account_flow_fragment;
    }

    @Override
    public void initViews(View rootView) {
        initView();
        getAccountBills();
    }

    private void initView() {
        mAdapter = new AccountFlowAdapter(getActivity(), mUserBillInfos);
        mPullListView.setAdapter(mAdapter);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = 1;
                getBills();
                getAccountData();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (mIsFinish == 1) {
                    mPageIndex++;
                    getBills();
                } else {
                    mPullToRefreshLayout.setLoadHintText();
                    mPullToRefreshLayout.postDelayed(() -> mPullToRefreshLayout.loadMoreFinish(true), 1000);
                }
            }
        });
    }

    private void getAccountBills() {
        showProgressDialog();
        getBills();
    }

    /**
     * 根据用户id拉取账户流水明细
     */
    private void getBills() {
        AccountFlowRequester requester = new AccountFlowRequester(new DefaultResultCallback<List<AccountFlowInfo>>() {
            @Override
            public void onSuccess(List<AccountFlowInfo> accountFlowInfos, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "AccountFlowFragment->AccountFlowRequester-->baseResult：" + baseResult + ",accountFlowInfos：" + accountFlowInfos);
                if (accountFlowInfos != null) {
                    if (mPageIndex == 1 && accountFlowInfos.size() == 0) {
                        mPullToRefreshLayout.setVisibility(View.GONE);
                        mEmptyLl.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyLl.setVisibility(View.GONE);
                        mPullToRefreshLayout.setVisibility(View.VISIBLE);
                    }
                    mIsFinish = baseResult.getIsFinish();
                    if (mPageIndex == 1) {
                        mAdapter.setData(accountFlowInfos);
                    } else {
                        mAdapter.addData(accountFlowInfos);
                    }
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismissProgressDialog();
                mPullToRefreshLayout.refreshFinish(true);
                mPullToRefreshLayout.loadMoreFinish(true);
            }
        });
        requester.account = mAccountId;
        requester.pageindex = mPageIndex;
        requester.pageSize = mPageSize;
        requester.doPost();
    }

    private void getAccountData() {
        mAccountManager.getFinanceStatistic(mAccountId, (baseResult, financeStatisticInfo) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (mOnFinanceStatisticListener != null) {
                    mOnFinanceStatisticListener.onFinanceStatisticRequester(baseResult, financeStatisticInfo);
                }
            }
        });
    }

    public void setOnFinanceStatisticListener(OnFinanceStatisticRequesterListener listener) {
        mOnFinanceStatisticListener = listener;
    }

    public interface OnFinanceStatisticRequesterListener {
        void onFinanceStatisticRequester(BaseResult baseResult, FinanceStatisticInfo financeStatisticInfo);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}


