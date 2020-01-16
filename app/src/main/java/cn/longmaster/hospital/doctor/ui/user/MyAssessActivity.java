package cn.longmaster.hospital.doctor.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BaseMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.AssessInfo;
import cn.longmaster.hospital.doctor.core.manager.message.MessageManager;
import cn.longmaster.hospital.doctor.core.manager.message.MessageProtocol;
import cn.longmaster.hospital.doctor.core.manager.message.MessageStateChangeListener;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.user.adapter.MyAssessAdapter;
import cn.longmaster.hospital.doctor.view.IconView;
import cn.longmaster.utils.SPUtils;

/**
 * 我的评估
 * Created by yangyong on 2019-07-16.
 */
public class MyAssessActivity extends BaseActivity implements MessageStateChangeListener {
    @FindViewById(R.id.activity_my_assess_srl)
    private SmartRefreshLayout mRefreshLayout;
    @FindViewById(R.id.activity_my_assess_list)
    private RecyclerView mAssessRecyclerView;
    @FindViewById(R.id.activity_my_assess_empty_view)
    private IconView mEmptyView;

    @AppApplication.Manager
    private MessageManager mMessageManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    private MyAssessAdapter mAdapter;
    private List<BaseMessageInfo> messageInfos = new ArrayList<>();
    private List<Long> mMsgIds = new ArrayList<>();
    private int mPageIndex = 0;
    private int mPageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_assess);
        ViewInjecter.inject(this);
        initView();
        initListener();
        getAllMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SPUtils.getInstance().put(AppPreference.KEY_NEW_ASSESS + mUserInfoManager.getCurrentUserInfo().getUserId(), false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessageManager.unRegMsgStateChangeListener(this);
        mMessageManager.setIsInAssess(false);
    }

    private void initView() {
        mMessageManager.setIsInAssess(true);
        mAssessRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyAssessAdapter(R.layout.item_my_assess, new ArrayList<>());
        mAssessRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
    }

    private void initListener() {
        mMessageManager.regMsgStateChangeListener(this);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BaseMessageInfo messageInfo = (BaseMessageInfo) adapter.getItem(position);
                AssessInfo assessInfo = mAdapter.getAssessInfo(messageInfo);
                if (assessInfo != null) {
                    mMessageManager.updateMessageToRead(mUserInfoManager.getCurrentUserInfo().getUserId(), messageInfo.getMsgId());
                    messageInfo.setMsgState(MessageProtocol.MSG_STATE_READED);
                    mAdapter.notifyItemChanged(position);
                    Intent intent = new Intent(MyAssessActivity.this, BrowserActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getAdwsUrl() + "ac/appraise?appointment_id=" + assessInfo.getOrderId() + "&doctor_id=" + assessInfo.getAttDocId());
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, "评估详情");
                    MyAssessActivity.this.startActivity(intent);
                }
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showList();
                    }
                }, 500);
            }
        });
    }

    private void getAllMessage() {
        mMessageManager.getAllMessage(mUserInfoManager.getCurrentUserInfo().getUserId(), false, new MessageManager.GetMessageStateChangeListener() {
            @Override
            public void getMessageStateChanged(List<BaseMessageInfo> baseMessageInfos) {
                for (BaseMessageInfo messageInfo : baseMessageInfos) {
                    if (mMsgIds.contains(messageInfo.getMsgId())) {
                        continue;
                    }
                    initMessageAssessUid(messageInfo);
                    messageInfos.add(messageInfo);
                    mMsgIds.add(messageInfo.getMsgId());
                }

                mRefreshLayout.setEnableLoadMore(messageInfos.size() > mPageSize);
                showList();
            }
        });
    }

    @Override
    public void onNewMessage(BaseMessageInfo baseMessageInfo) {
        if (baseMessageInfo.getMsgType() != MessageProtocol.SMS_TYPE_DOCTOR_ASSESS || mMsgIds.contains(baseMessageInfo.getMsgId())) {
            return;
        }
        if (mEmptyView.getVisibility() == View.VISIBLE) {
            mRefreshLayout.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
        initMessageAssessUid(baseMessageInfo);
        mAdapter.addData(0, baseMessageInfo);
    }

    private void showList() {
        if (messageInfos.size() == 0) {
            mRefreshLayout.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            int currentTotalSize = mPageIndex * mPageSize + mPageSize;
            mAdapter.addData(messageInfos.subList(mPageIndex * mPageSize, currentTotalSize > messageInfos.size() ? messageInfos.size() : currentTotalSize));
            mRefreshLayout.setEnableLoadMore(currentTotalSize < messageInfos.size());
            if (currentTotalSize < messageInfos.size()) {
                mPageIndex++;
            }
            mRefreshLayout.finishLoadMore();
        }
    }

    private void initMessageAssessUid(BaseMessageInfo messageInfo) {
        int attDocId = 0;
        try {
            JSONObject jsonObject = new JSONObject(messageInfo.getMsgContent());
            attDocId = jsonObject.optInt("uid", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageInfo.setAssessUid(attDocId);
    }
}
