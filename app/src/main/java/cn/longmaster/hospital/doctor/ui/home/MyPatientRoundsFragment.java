package cn.longmaster.hospital.doctor.ui.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListItemInfo;
import cn.longmaster.hospital.doctor.core.manager.rounds.RoundsManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDetailActivity;
import cn.longmaster.utils.KeyboardUtils;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2018/5/10.
 * Mod by biao on 2019/10/29
 * 预约列表查房
 */

public class MyPatientRoundsFragment extends NewBaseFragment {
    @FindViewById(R.id.fg_my_patient_rounds_search_et)
    private EditText fgMyPatientRoundsSearchEt;
    @FindViewById(R.id.fg_my_patient_rounds_search_clear_iv)
    private ImageView fgMyPatientRoundsSearchClearIv;
    @FindViewById(R.id.fg_my_patient_rounds_search_tv)
    private TextView fgMyPatientRoundsSearchTv;
    @FindViewById(R.id.fg_my_patient_rounds_srl)
    private SmartRefreshLayout fgMyPatientRoundsSrl;
    @FindViewById(R.id.fg_my_patient_rounds_rv)
    private RecyclerView fgMyPatientRoundsRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    @FindViewById(R.id.include_new_no_data_tv)
    private TextView includeNewNoDataTv;
    private RoundsAdapter mRoundsAdapter;
    private int mPageIndex = MIN_PAGE_INDEX_1;
    private boolean mSameDep = true;
    @AppApplication.Manager
    private RoundsManager roundsManager;

    @Override
    protected void initDatas() {
        super.initDatas();
        mRoundsAdapter = new RoundsAdapter(R.layout.item_home_rounds_layout, new ArrayList<>(0));
        mRoundsAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderListItemInfo orderListItemInfo = (OrderListItemInfo) adapter.getItem(position);
            if (null == orderListItemInfo) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(getBaseActivity(), RoundsDetailActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderListItemInfo.getOrderId());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, orderListItemInfo.getAtthosId());
            startActivity(intent);
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_my_patient_rounds;
    }

    @Override
    public void initViews(View rootView) {
        includeNewNoDataTv.setText("暂无相关数据");
        fgMyPatientRoundsRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        fgMyPatientRoundsRv.setAdapter(mRoundsAdapter);
        fgMyPatientRoundsSrl.autoRefresh();
        initListener();
    }

    private void initListener() {
        fgMyPatientRoundsSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                getOrderList(false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRoundsAdapter.setNewData(null);
                mPageIndex = MIN_PAGE_INDEX_1;
                getOrderList(true, refreshLayout);
            }
        });
        fgMyPatientRoundsSearchTv.setOnClickListener(v -> {
            fgMyPatientRoundsSrl.autoRefresh();
        });
        fgMyPatientRoundsSearchClearIv.setOnClickListener(v -> {
            fgMyPatientRoundsSearchEt.setText(null);
            fgMyPatientRoundsSrl.autoRefresh();
        });
        fgMyPatientRoundsSearchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                fgMyPatientRoundsSrl.autoRefresh();
                KeyboardUtils.hideSoftInput(v);
                return true;
            }
            return false;
        });
        fgMyPatientRoundsSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    fgMyPatientRoundsSearchClearIv.setVisibility(View.VISIBLE);
                } else {
                    fgMyPatientRoundsSearchClearIv.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setSameDep(boolean isSameDep) {
        mSameDep = isSameDep;
        fgMyPatientRoundsSrl.autoRefresh();
    }

    private void getOrderList(boolean isRefresh, RefreshLayout refreshLayout) {
        roundsManager.getOrderList(mPageIndex, PAGE_SIZE, null, mSameDep, getString(fgMyPatientRoundsSearchEt), new DefaultResultCallback<OrderListInfo>() {
            @Override
            public void onSuccess(OrderListInfo orderListInfo, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "GetOrderListRequester-->baseResult:" + baseResult + ",orderListInfo:" + orderListInfo);
                if (baseResult.isFinish()) {
                    fgMyPatientRoundsSrl.finishLoadMoreWithNoMoreData();
                }
                if (mPageIndex == MIN_PAGE_INDEX_1) {
                    if (LibCollections.isEmpty(orderListInfo.getOrderListItems())) {
                        includeNewNoDataLl.setVisibility(View.VISIBLE);
                        fgMyPatientRoundsSrl.setVisibility(View.GONE);
                    } else {
                        includeNewNoDataLl.setVisibility(View.GONE);
                        fgMyPatientRoundsSrl.setVisibility(View.VISIBLE);
                    }
                    mRoundsAdapter.setNewData(orderListInfo.getOrderListItems());
                } else {
                    mRoundsAdapter.addData(orderListInfo.getOrderListItems());
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }
            }
        });
    }
}
