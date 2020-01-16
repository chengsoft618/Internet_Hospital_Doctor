package cn.longmaster.hospital.doctor.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupList;
import cn.longmaster.hospital.doctor.core.entity.im.MemberJoinGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatGroupListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.adapter.IMConsultListNewAdapter;
import cn.longmaster.hospital.doctor.ui.im.ChatActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * Created by W·H·K on 2018/5/10.
 * Mod by biao on2019/10/29
 * 首页会诊
 */

public class HomePageConsultFragment extends NewBaseFragment implements GroupMessageManager.GroupMessageStateChangeListener, GroupMessageManager.GroupMessageUnreadCountStateChangeListener,
        GroupMessageManager.GroupStateChangeListener {
    @FindViewById(R.id.fg_home_page_consult_srl)
    private SmartRefreshLayout fgHomePageConsultSrl;
    @FindViewById(R.id.fg_home_page_consult_rv)
    private RecyclerView fgHomePageConsultRv;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private GroupMessageManager mGroupMessageManager;
    private IMConsultListNewAdapter mIMConsultListAdapter;
    private boolean isRefresh = true;

    @Override
    protected void initDatas() {
        super.initDatas();
        mIMConsultListAdapter = new IMConsultListNewAdapter(R.layout.item_im_consult_new, new ArrayList<>());
        mIMConsultListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatGroupInfo chatGroupInfo = (ChatGroupInfo) adapter.getItem(position);
            if (null != chatGroupInfo) {
                ChatActivity.startChatActivity(getActivity(), chatGroupInfo.getAppointmentId(), chatGroupInfo.getGroupId());
            }
        });
        mGroupMessageManager.registerGroupMessageStateChangeListener(this, true);
        mGroupMessageManager.registerUnreadCountStateChangeListener(this, true);
        mGroupMessageManager.registerGroupStateChangeListener(this, true);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_home_page_consultation;
    }

    @Override
    public void initViews(View rootView) {
        fgHomePageConsultRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getBaseActivity()));
        fgHomePageConsultRv.setAdapter(mIMConsultListAdapter);
        fgHomePageConsultSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mIMConsultListAdapter.getItemCount() > 0) {
                    ChatGroupInfo lastItem = mIMConsultListAdapter.getData().get(mIMConsultListAdapter.getItemCount());
                    if (null != lastItem) {
                        getGroupList(refreshLayout, isRefresh = false, lastItem.getUpdDt(), lastItem.getGroupId());
                    }
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIMConsultListAdapter.setNewData(null);
                getGroupList(refreshLayout, isRefresh = true, "", 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            fgHomePageConsultSrl.finishRefresh();
        } else {
            fgHomePageConsultSrl.finishLoadMore();
        }
        fgHomePageConsultSrl.autoRefresh();
    }

    /**
     * 拉取群组列表
     */
    private void getGroupList(RefreshLayout refreshLayout, boolean isRefresh, final String groupDt, final int groupId) {
        GetChatGroupListRequester groupListRequester = new GetChatGroupListRequester(new DefaultResultCallback<ChatGroupList>() {
            @Override
            public void onSuccess(ChatGroupList chatGroupList, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "getGroupList-->" + chatGroupList);
                if (baseResult.isFinish()) {
                    fgHomePageConsultSrl.finishLoadMoreWithNoMoreData();
                }
                if (null == chatGroupList || LibCollections.isEmpty(chatGroupList.getChatGroupList())) {
                    mIMConsultListAdapter.setEmptyView(createEmptyListView());
                } else {
                    if (TextUtils.isEmpty(groupDt) && groupId == 0) {
                        mIMConsultListAdapter.setNewData(chatGroupList.getChatGroupList());
                    } else {
                        mIMConsultListAdapter.addData(chatGroupList.getChatGroupList());
                    }
                }
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
        groupListRequester.status = "0,1,2,4";
        groupListRequester.mode = 2;
        groupListRequester.pageSize = 20;
        groupListRequester.orderType = 0;
        groupListRequester.groupDt = groupDt;
        groupListRequester.groupMarkId = groupId;
        groupListRequester.doPost();
    }

    @Override
    public void onDestroy() {
        mGroupMessageManager.registerGroupMessageStateChangeListener(this, false);
        mGroupMessageManager.registerUnreadCountStateChangeListener(this, false);
        mGroupMessageManager.registerGroupStateChangeListener(this, false);
        super.onDestroy();
    }

    @Override
    public void onSendGroupMsgStateChanged(int result, BaseGroupMessageInfo baseGroupMessageInfo) {
        Logger.logI(Logger.APPOINTMENT, "onSendGroupMsgStateChanged-->baseGroupMessageInfo：" + baseGroupMessageInfo);
        if (mIMConsultListAdapter == null || baseGroupMessageInfo == null) {
            return;
        }
        AppHandlerProxy.runOnUIThread(() -> mIMConsultListAdapter.updateGroupMessage(baseGroupMessageInfo, true));
    }

    @Override
    public void onReceiveNewGroupMessageStateChanged(final BaseGroupMessageInfo baseGroupMessageInfo) {
        AppHandlerProxy.runOnUIThread(() -> {
            if (mIMConsultListAdapter == null || baseGroupMessageInfo == null) {
                return;
            }
            if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_JOIN_MSG) {
                //自己加入
                MemberJoinGroupMessageInfo memberJoinGroupMessageInfo = (MemberJoinGroupMessageInfo) baseGroupMessageInfo;
                if (memberJoinGroupMessageInfo.getUserId() == mUserInfoManager.getCurrentUserInfo().getUserId()) {
                    fgHomePageConsultSrl.autoRefresh();
                    return;
                }
            } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.GUIDE_SET_DATE_MSG) {
                //设置排班时间
                if (mGroupMessageManager.getCurentImId() != baseGroupMessageInfo.getGroupId()) {
                    mIMConsultListAdapter.setRedPointMap(baseGroupMessageInfo.getGroupId(), true);
                }
                fgHomePageConsultSrl.autoRefresh();
                return;
            } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.STATE_CHANGE_MSG) {
                //改变状态，比如完成
                fgHomePageConsultSrl.autoRefresh();
                return;
            }
            mIMConsultListAdapter.updateGroupMessage(baseGroupMessageInfo, false);
        });
    }

    @Override
    public void onGroupMessageUnreadCountStateChanged(int groupId, int count) {
        Logger.logI(Logger.APPOINTMENT, "onGroupMessageUnreadCountStateChanged-->groupId：" + groupId);
        if (mIMConsultListAdapter == null) {
            return;
        }
        AppHandlerProxy.runOnUIThread(() -> mIMConsultListAdapter.setRedPointMap(groupId, false));
    }

    @Override
    public void onGetGroupStatusStateChanged(int result, int groupId, int status) {

    }

    @Override
    public void onUpdGroupStatusStateChanged(int result, int groupId, int status) {
        Logger.logI(Logger.APPOINTMENT, "onUpdGroupStatusStateChanged-->status：" + status);
        AppHandlerProxy.runOnUIThread(() -> {
            if (mIMConsultListAdapter == null) {
                return;
            }
            fgHomePageConsultSrl.autoRefresh();
        });
    }

    @Override
    public void onGetGroupListStateChanged(int result, int opType, int count, List<Integer> groupList) {

    }
}
