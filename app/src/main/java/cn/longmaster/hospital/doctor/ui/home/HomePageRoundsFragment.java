package cn.longmaster.hospital.doctor.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderListItemInfo;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.rounds.RoundsManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.rounds.RoundsDetailActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2018/5/10.
 * Mod by biao on 2019/10/22
 * 首页查房列表
 */

public class HomePageRoundsFragment extends NewBaseFragment implements MessageStateChangeListener {
    @FindViewById(R.id.fg_home_page_rounds_srl)
    private SmartRefreshLayout fgHomePageRoundsSrl;
    @FindViewById(R.id.fg_home_page_rounds_rv)
    private RecyclerView fgHomePageRoundsRv;
    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private RoundsManager roundsManager;

    private RoundsAdapter mRoundsAdapter;
    private int mPageIndex = MIN_PAGE_INDEX_1;
    private boolean isLoadMore = false;

    @Override
    protected void initDatas() {
        super.initDatas();
        mRoundsAdapter = new RoundsAdapter(R.layout.item_home_rounds_layout, new ArrayList<>(0));
        mRoundsAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderListItemInfo orderListItemInfo = (OrderListItemInfo) adapter.getItem(position);
            if (orderListItemInfo != null) {
                Intent intent = new Intent();
                intent.setClass(getBaseActivity(), RoundsDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, orderListItemInfo.getOrderId());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, orderListItemInfo.getAtthosId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_home_page_rounds;
    }

    @Override
    public void initViews(View rootView) {
        fgHomePageRoundsRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        fgHomePageRoundsRv.setAdapter(mRoundsAdapter);
        fgHomePageRoundsSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getOrderList(isLoadMore = true, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRoundsAdapter.setNewData(null);
                mPageIndex = MIN_PAGE_INDEX_1;
                getOrderList(isLoadMore = false, refreshLayout);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoadMore) {
            fgHomePageRoundsSrl.finishLoadMore();
        } else {
            fgHomePageRoundsSrl.finishRefresh();
        }
        fgHomePageRoundsSrl.autoRefresh();
    }

    private void getOrderList(boolean isLoadMore, RefreshLayout refreshLayout) {
        JSONArray array = new JSONArray();
        array.put(1);
        array.put(4);
        array.put(8);
        roundsManager.getOrderList(mPageIndex, PAGE_SIZE, array.toString(), false, null, new DefaultResultCallback<OrderListInfo>() {
            @Override
            public void onSuccess(OrderListInfo orderListInfo, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "GetOrderListRequester-->baseResult:" + baseResult + ",orderListInfo:" + orderListInfo);
                if (baseResult.isFinish()) {
                    fgHomePageRoundsSrl.finishLoadMoreWithNoMoreData();
                } else {
                    mPageIndex++;
                }
                if (null != orderListInfo) {
                    if (mPageIndex == MIN_PAGE_INDEX_1 && LibCollections.isEmpty(orderListInfo.getOrderListItems())) {
                        mRoundsAdapter.setEmptyView(createEmptyListView());
                    }
                    if (mPageIndex == MIN_PAGE_INDEX_1) {
                        mRoundsAdapter.setNewData(orderListInfo.getOrderListItems());
                    } else {
                        mRoundsAdapter.addData(orderListInfo.getOrderListItems());
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
                if (isLoadMore) {
                    refreshLayout.finishLoadMore();
                } else {
                    refreshLayout.finishRefresh();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMessageManager.regMsgStateChangeListener(this);
    }

    @Override
    public void onDestroyView() {

        mMessageManager.unRegMsgStateChangeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        Logger.logD(Logger.COMMON, "onNewMessage()->baseMessageInfo:" + baseMessageInfo.toString());
        switch (baseMessageInfo.getMsgType()) {
            case MessageProtocol.SMS_TYPE_DOCTOR_WAIT_RECEIVE://等待接诊
            case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH://接诊完成：接诊医生
            case MessageProtocol.SMS_TYPE_DOCTOR_RECEIVE_FINISH_LAUNCH://接诊完成：发起医生
                fgHomePageRoundsSrl.autoRefresh();
                break;
            default:
                break;
        }
    }
}
