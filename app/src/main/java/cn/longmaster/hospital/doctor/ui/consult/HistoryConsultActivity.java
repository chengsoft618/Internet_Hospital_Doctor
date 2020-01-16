package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import cn.longmaster.doctorlibrary.customview.listview.OnLoadMoreListener;
import cn.longmaster.doctorlibrary.customview.listview.OnPullRefreshListener;
import cn.longmaster.doctorlibrary.customview.listview.PullRefreshView;
import cn.longmaster.doctorlibrary.customview.listview.SimpleBaseAdapter;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consult.PatientBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.BaseGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupInfo;
import cn.longmaster.hospital.doctor.core.entity.im.ChatGroupList;
import cn.longmaster.hospital.doctor.core.entity.im.MemberExitGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberJoinGroupMessageInfo;
import cn.longmaster.hospital.doctor.core.manager.im.GroupMessageManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatGroupListRequester;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.IMConsultListAdapter;
import cn.longmaster.hospital.doctor.ui.doctor.SearchActivity;
import cn.longmaster.hospital.doctor.ui.im.ChatActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.ShareDoctorCardDialog;

/**
 * 历史就诊
 * Created by Yang² on 2017/8/17.
 */

public class HistoryConsultActivity extends BaseActivity implements GroupMessageManager.GroupMessageStateChangeListener,
        GroupMessageManager.GroupMessageUnreadCountStateChangeListener, GroupMessageManager.GroupStateChangeListener {
    private final String TAG = HistoryConsultActivity.class.getSimpleName();
    private final int REQUEST_CODE_SEARCH = 100;
    @FindViewById(R.id.activity_history_consult_appactionbar)
    private AppActionBar mAppActionBar;
    @FindViewById(R.id.activity_history_consult_list_prv)
    private PullRefreshView mListPrv;
    @FindViewById(R.id.activity_history_consult_empty_layout)
    private LinearLayout mEmptyLl;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private GroupMessageManager mMessageManager;

    private IMConsultListAdapter mIMConsultListAdapter;
    private boolean mIsShare = false;
    private DoctorBaseInfo mDoctorBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_consult);
        ViewInjecter.inject(this);
        initData();
        initView();
        initPullRefreshView();
        initAdapter();
        addListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessageManager.registerGroupMessageStateChangeListener(this, false);
        mMessageManager.registerUnreadCountStateChangeListener(this, false);
        mMessageManager.registerGroupStateChangeListener(this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
            String keyWords = data.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SEARCH_CONTENT);
            getGroupList("", 0, keyWords);
        }
    }

    private void initData() {
        mIsShare = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, false);
        if (mIsShare) {
            mDoctorBaseInfo = (DoctorBaseInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_BASE_INFO);
        }
    }

    private void initView() {
        mAppActionBar.setTitle(mIsShare ? getString(R.string.home_consult) : getString(R.string.home_consult_history));
    }

    private void initPullRefreshView() {
        mListPrv.setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onPullDownRefresh(PullRefreshView pullRefreshView) {
                getGroupList("", 0, "");
                mEmptyLl.setVisibility(View.GONE);
            }
        });
        mListPrv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(PullRefreshView pullRefreshView) {
                if (mIMConsultListAdapter.getData().size() > 0) {
                    getGroupList(mIMConsultListAdapter.getLastItem().getUpdDt(), mIMConsultListAdapter.getLastItem().getGroupId(), "");
                }
            }
        });
        mListPrv.startPullRefresh();
        getGroupList("", 0, "");
    }

    private void initAdapter() {
        mIMConsultListAdapter = new IMConsultListAdapter(getActivity());
        mListPrv.setAdapter(mIMConsultListAdapter);
    }

    private void addListener() {
        mIMConsultListAdapter.setOnAdapterItemClickListener(new SimpleBaseAdapter.OnAdapterItemClickListener<ChatGroupInfo>() {
            @Override
            public void onAdapterItemClick(int position, ChatGroupInfo chatGroupInfo) {
                if (mIsShare) {
                    showDoctorCardDialog(mIMConsultListAdapter.getPatientBaseInfo(chatGroupInfo.getAppointmentId()), chatGroupInfo);
                } else {
                    ChatActivity.startChatActivity(getActivity(), chatGroupInfo.getAppointmentId(), chatGroupInfo.getGroupId());
                }
            }
        });
        mMessageManager.registerGroupMessageStateChangeListener(this, true);
        mMessageManager.registerUnreadCountStateChangeListener(this, true);
        mMessageManager.registerGroupStateChangeListener(this, true);
    }

    private void showDoctorCardDialog(final PatientBaseInfo patientBaseInfo, final ChatGroupInfo chatGroupInfo) {
        ShareDoctorCardDialog shareDoctorCardDialog = new ShareDoctorCardDialog(getActivity());
        shareDoctorCardDialog.setOnDoctorCardClickListener(new ShareDoctorCardDialog.OnDoctorCardClickListener() {
            @Override
            public void onSendClick(String suggestText) {
                //发送分享名片
                setCardGroupMessage(chatGroupInfo);
            }
        });
        shareDoctorCardDialog.show();
        shareDoctorCardDialog.displayDialog(patientBaseInfo, mDoctorBaseInfo.getRealName(), mDoctorBaseInfo.getHospitalName());
    }

    /**
     * 拉取群组列表
     */
    private void getGroupList(final String groupDt, final int groupId, String keyWords) {
        GetChatGroupListRequester groupListRequester = new GetChatGroupListRequester(new DefaultResultCallback<ChatGroupList>() {
            @Override
            public void onSuccess(ChatGroupList chatGroupList, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "getGroupList-->" + chatGroupList);
                if (mListPrv.isRefreshing()) {
                    mListPrv.stopPullRefresh();
                } else if (mListPrv.isLoadingMore()) {
                    mListPrv.stopLoadMore();
                }
                if (chatGroupList == null) {
                    return;
                }
                if (TextUtils.isEmpty(groupDt) && groupId == 0) {
                    mIMConsultListAdapter.setGroupList(chatGroupList);
                    mEmptyLl.setVisibility(chatGroupList.getChatGroupList().size() == 0 ? View.VISIBLE : View.GONE);
                } else {
                    mIMConsultListAdapter.addGroupList(chatGroupList);
                }
                mListPrv.setLoadMoreEnable(baseResult.isFinish());
            }
        });
        groupListRequester.status = mIsShare ? "0,1,4" : "3";
//        groupListRequester.status = "0,1,4" ;
        groupListRequester.mode = 2;
        groupListRequester.pageSize = 20;
        groupListRequester.orderType = 0;
        groupListRequester.groupDt = groupDt;
        groupListRequester.groupMarkId = groupId;
        groupListRequester.keyWords = keyWords;
        groupListRequester.doPost();
    }

    @OnClick({R.id.activity_history_consult_search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_history_consult_search_tv:
                SearchActivity.startSearchActivityForResult(getActivity(), AppConstant.SEARCH_TYPE_APPOINTMENT, getString(R.string.search_patient_hint), REQUEST_CODE_SEARCH);
                break;

        }
    }

    private void setCardGroupMessage(ChatGroupInfo chatGroupInfo) {
        mMessageManager.sendCardGroupMessage(chatGroupInfo.getAppointmentId(), chatGroupInfo.getGroupId(), mDoctorBaseInfo.getUserId(), chatGroupInfo.getRole(), new GroupMessageManager.GroupMessageSendStateChangeListener() {
            @Override
            public void onSaveDBStateChanged(int code, BaseGroupMessageInfo baseGroupMessageInfo) {
                Logger.logD(Logger.IM, TAG + "->setCardGroupMessage()->onSaveDBStateChanged()->code:" + code + ", BaseGroupMessageInfo:" + baseGroupMessageInfo);
            }

            @Override
            public void onFileUploadStateChanged(long seqId, int code, String fileName) {
            }

            @Override
            public void onSendStateChanged(final int result, final long seqId) {
                Logger.logD(Logger.IM, TAG + "->setCardGroupMessage()->onSendStateChanged()->result:" + result + ", seqId:" + seqId);
                showToast(result == 0 ? R.string.share_success : R.string.share_failed);
                finish();
            }
        });
    }

    @Override
    public void onSendGroupMsgStateChanged(int result, BaseGroupMessageInfo baseGroupMessageInfo) {

    }

    @Override
    public void onReceiveNewGroupMessageStateChanged(final BaseGroupMessageInfo baseGroupMessageInfo) {
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mIMConsultListAdapter == null || baseGroupMessageInfo == null) {
                    return;
                }
                if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_JOIN_MSG) {
                    //自己加入
                    MemberJoinGroupMessageInfo memberJoinGroupMessageInfo = (MemberJoinGroupMessageInfo) baseGroupMessageInfo;
                    if (memberJoinGroupMessageInfo.getUserId() == mUserInfoManager.getCurrentUserInfo().getUserId()) {
                        getGroupList("", 0, "");
                    }
                } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.MEMBER_EXIT_MSG) {
                    //自己退出
                    MemberExitGroupMessageInfo memberExitGroupMessageInfo = (MemberExitGroupMessageInfo) baseGroupMessageInfo;
                    if (memberExitGroupMessageInfo.getUserId() == mUserInfoManager.getCurrentUserInfo().getUserId()) {
                        getGroupList("", 0, "");
                    }
                } else if (baseGroupMessageInfo.getMsgType() == BaseGroupMessageInfo.STATE_CHANGE_MSG) {
                    //改变状态
                    getGroupList("", 0, "");
                }
            }
        });
    }

    @Override
    public void onGroupMessageUnreadCountStateChanged(int groupId, int count) {
        Logger.logI(Logger.APPOINTMENT, "onGroupMessageUnreadCountStateChanged-->groupId：" + groupId);
        if (mIMConsultListAdapter == null) {
            return;
        }
        mIMConsultListAdapter.setRedPointMap(groupId, false);
        mIMConsultListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetGroupStatusStateChanged(int result, int groupId, int status) {

    }

    @Override
    public void onUpdGroupStatusStateChanged(int result, int groupId, int status) {
        Logger.logI(Logger.APPOINTMENT, "onUpdGroupStatusStateChanged-->status：" + status);
        AppHandlerProxy.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mIMConsultListAdapter == null) {
                    return;
                }
                getGroupList("", 0, "");
            }
        });
    }

    @Override
    public void onGetGroupListStateChanged(int result, int opType, int count, List<Integer> groupList) {

    }
}
