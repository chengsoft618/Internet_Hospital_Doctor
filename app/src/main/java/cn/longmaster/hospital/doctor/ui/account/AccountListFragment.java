package cn.longmaster.hospital.doctor.ui.account;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.entity.account.AccountListInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.account.AccountListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.view.SingleFlowPopuwindow;
import cn.longmaster.hospital.doctor.view.pullview.PullListView;
import cn.longmaster.hospital.doctor.view.pullview.PullToRefreshLayout;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2019/1/17.
 */
public class AccountListFragment extends NewBaseFragment {
    @FindViewById(R.id.layout_account_list_pull_refresh_layout)
    private PullToRefreshLayout mPullToRefreshLayout;
    @FindViewById(R.id.layout_account_list_recycler_view)
    private PullListView mPullListView;
    @FindViewById(R.id.layout_account_list_screen_view)
    private View mScreenView;
    @FindViewById(R.id.layout_account_list_im)
    private ImageView mSwitchIm;
    @FindViewById(R.id.layout_account_list_screen_tv)
    private TextView mSwitchTv;
    @FindViewById(R.id.layout_account_list_empty_layout)
    private LinearLayout mEmptyLl;

    private List<AccountListInfo> mAccountListInfos = new ArrayList<>();
    private List<String> mScreenList = new ArrayList<>();
    private String mCurrentScreen = "全部";
    private SingleFlowPopuwindow mPopupWindow;
    private int mAccountId;
    private int mState = 0;
    private int mPageIndex = 1;
    private int mPageSize = 10;
    private int mIsFinish = 0;
    private AccountVisitListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initDatas() {
        super.initDatas();
        mAccountId = ((MyAccountActivity) getActivity()).getAccountId();
        mScreenList.add(getString(R.string.medical_college_all));
        mScreenList.add(getString(R.string.account_no_settlement));
        mScreenList.add(getString(R.string.account_wait_cash));
        mScreenList.add(getString(R.string.account_cashing));
        mScreenList.add(getString(R.string.account_payment));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_account_visit_list_fragment;
    }

    @Override
    public void initViews(View rootView) {
        initView();
        getAccount();
    }

    private void initView() {
        mAdapter = new AccountVisitListAdapter(getActivity(), mAccountListInfos);
        mPullListView.setAdapter(mAdapter);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mPageIndex = 1;
                getAccountList();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (mIsFinish == 1) {
                    mPageIndex++;
                    getAccountList();
                } else {
                    mPullToRefreshLayout.setLoadHintText();
                    mPullToRefreshLayout.postDelayed(() -> mPullToRefreshLayout.loadMoreFinish(true), 1000);
                }
            }
        });
    }

    private void getAccount() {
        showProgressDialog();
        getAccountList();
    }

    @OnClick({R.id.layout_account_list_im})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_account_list_im:
                showTimeDialog();
                break;
        }
    }

    private void showTimeDialog() {
        mSwitchIm.setEnabled(false);
        mSwitchIm.setClickable(false);
        mSwitchIm.setImageResource(R.drawable.ic_account_open);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_account_screen, null);
        final RecyclerView mRecyclerView = contentView.findViewById(R.id.dialog_account_screen_recycler_view);
        mPopupWindow = new SingleFlowPopuwindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.showAsDropDown(mScreenView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPopupWindow.setOnDismissListener(() -> {
            mSwitchIm.setImageResource(R.drawable.ic_account_close);
            mPopupWindow = null;
            new Handler().postDelayed(() -> {
                mSwitchIm.setEnabled(true);
                mSwitchIm.setClickable(true);
            }, 100);
        });
        AccountScreenAdapter accountScreenAdapter = new AccountScreenAdapter(R.layout.item_account_screen, mScreenList, mCurrentScreen);
        mRecyclerView.setAdapter(accountScreenAdapter);
        accountScreenAdapter.setOnItemClickListener((view, adapter, position) -> {
            displayScreenView(position);
            mPopupWindow.dismiss();
        });
    }

    private void displayScreenView(int position) {
        mSwitchTv.setText(mScreenList.get(position));
        mCurrentScreen = mScreenList.get(position);
        getCurrentScreenState();
        getAccount();
    }

    private void getCurrentScreenState() {
        mPageIndex = 1;
        if (mCurrentScreen.equals(getString(R.string.medical_college_all))) {
            mState = 0;
        } else if (mCurrentScreen.equals(getString(R.string.account_no_settlement))) {
            mState = 101;
        } else if (mCurrentScreen.equals(getString(R.string.account_wait_cash))) {
            mState = 102;
        } else if (mCurrentScreen.equals(getString(R.string.account_cashing))) {
            mState = 103;
        } else if (mCurrentScreen.equals(getString(R.string.account_payment))) {
            mState = 104;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }

    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        });
    }

    private void getAccountList() {
        AccountListRequester requester = new AccountListRequester((baseResult, accountListInfo) -> {
            Logger.logD(Logger.COMMON, "AccountListFragment->AccountListRequester-->baseResult：" + baseResult + ",accountListInfo：" + accountListInfo);
            dismissProgressDialog();
            mPullToRefreshLayout.refreshFinish(true);
            mPullToRefreshLayout.loadMoreFinish(true);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && accountListInfo != null) {
                if (mPageIndex == 1 && accountListInfo.size() == 0) {
                    mPullToRefreshLayout.setVisibility(View.GONE);
                    mEmptyLl.setVisibility(View.VISIBLE);
                } else {
                    mEmptyLl.setVisibility(View.GONE);
                    mPullToRefreshLayout.setVisibility(View.VISIBLE);
                }
                mIsFinish = baseResult.getIsFinish();
                if (mPageIndex == 1) {
                    mAdapter.setData(accountListInfo);
                } else {
                    mAdapter.addData(accountListInfo);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.account = mAccountId;
        requester.state = mState;
        requester.pageindex = mPageIndex;
        requester.pageSize = mPageSize;
        requester.doPost();
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
