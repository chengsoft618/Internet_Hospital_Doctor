package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.customview.listview.PullRefreshView;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consultant.ScheduingListInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.SearchScheduingInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitScreenInfo;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.ConsultantSchedulingRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetVisitListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.consult.adapter.ConsultationManageAdapter;
import cn.longmaster.utils.ToastUtils;

/**
 * 全部会诊fragment
 * Created by W·H·K on 2017/12/22.
 * Mod by biao on 2019/8/28
 */
public class ConsultantAllFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_prv)
    private PullRefreshView mFragmentPrv;
    @FindViewById(R.id.fragment_tips_tv)
    private TextView mTipsTv;
    @FindViewById(R.id.fragment_empty_layout)
    private LinearLayout mEmptyLayout;

    private int mPageSize = 10;
    private ConsultationManageAdapter mAdapter;
    private int mPageindex = 1;
    private OnTipsTvClickListener mOnTipsTvClickListener;
    private OnSwitchClickListener mOnSwitchClickListener;
    private SearchScheduingInfo mSearchScheduingInfo;
    private VisitScreenInfo mVisitScreenInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_all_layout;
    }

    @Override
    public void initViews(View rootView) {
        getTriageList();
        initPullRefreshView();
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        displaySearchData();
    }

    private void displaySearchData() {
        mSearchScheduingInfo = ((SchedulingActivity) getActivity()).getSearchScheduingInfo();
        mVisitScreenInfo = ((SchedulingActivity) getActivity()).getmVisitScreenInfo();
        Logger.logI(Logger.COMMON, "ConsultantAllFragment->onStart()-->searchScheduingInfo:" + mSearchScheduingInfo + ",visitScreenInfo:" + mVisitScreenInfo);
        displayData();
    }

    private void displayData() {
        if ((mSearchScheduingInfo == null && mVisitScreenInfo == null)) {
            getConsultantScheduling("", "", 0, "", "");
        } else {
            mPageindex = 1;
            getConsultantScheduling(mVisitScreenInfo == null ? "" : mVisitScreenInfo.getBeginDt(),
                    mVisitScreenInfo == null ? "" : mVisitScreenInfo.getEndDt(),
                    mVisitScreenInfo == null ? 0 : mVisitScreenInfo.getDoctorId(),
                    mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getKeyWord(),
                    mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getSearchType());
        }
    }

    public void setOnTipsTvClickListener(OnTipsTvClickListener listener) {
        this.mOnTipsTvClickListener = listener;
    }

    public void setOnSwitchClickListener(OnSwitchClickListener listener) {
        this.mOnSwitchClickListener = listener;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ConsultationManageAdapter(getActivity());
        mFragmentPrv.setAdapter(mAdapter);
        mAdapter.setOnAdapterItemClickListener((position, scheduingListInfo) -> {
            if (mOnSwitchClickListener != null) {
                mOnSwitchClickListener.onTSwitchClicked(scheduingListInfo);
            }
        });
    }

    private void initListener() {
        mTipsTv.setOnClickListener(v -> {
            if (mOnTipsTvClickListener != null) {
                mOnTipsTvClickListener.onTipsTvClicked(v);
            }
        });
    }

    private void getConsultantScheduling(String beginDt, String endDt, int doctorId, String keyWord, String searchType) {
        ConsultantSchedulingRequester schedulingRequester = new ConsultantSchedulingRequester((baseResult, SshedulingListInfos) -> {
            Logger.logI(Logger.COMMON, "ConsultantAllFragment-->getConsultantScheduling-->SshedulingListInfos:" + SshedulingListInfos + ",mPageindex:" + mPageindex);
            if (mFragmentPrv.isRefreshing()) {
                mFragmentPrv.stopPullRefresh();
            }
            if (mFragmentPrv.isLoadingMore()) {
                mFragmentPrv.stopLoadMore();
            }
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (mPageindex == 1 && SshedulingListInfos.getScheduingList().size() == 0) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                }
                if (mPageindex == 1) {
                    mAdapter.setData(SshedulingListInfos.getScheduingList());
                } else {
                    mAdapter.addData(SshedulingListInfos.getScheduingList());
                }
                mFragmentPrv.setLoadMoreEnable(SshedulingListInfos.getIsFinish() == 1);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        schedulingRequester.statNum = AppConstant.ConsultantStatusType.APPOINTMENT_STATUS_TYPE_ALL;
        schedulingRequester.pageindex = mPageindex;
        schedulingRequester.pagesize = mPageSize;
        schedulingRequester.beginDt = beginDt;
        schedulingRequester.endDt = endDt;
        schedulingRequester.doctorId = doctorId;
        schedulingRequester.searchWord = keyWord;
        schedulingRequester.searchType = searchType;
        schedulingRequester.doPost();
    }

    private void getTriageList() {
        GetVisitListRequester requester = new GetVisitListRequester((baseResult, visitInfo) -> {
            Logger.logI(Logger.COMMON, "ConsultantAllFragment-->getTriageList-->visitInfo:" + visitInfo);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (visitInfo.getVisitDetailsInfos().size() > 0) {
                    mTipsTv.setVisibility(View.VISIBLE);
                } else {
                    mTipsTv.setVisibility(View.GONE);
                }
            }
        });
        requester.statNum = AppConstant.ConsultantStatusType.APPOINTMENT_STATUS_TYPE_TRIAGE;
        requester.scheduingId = -1;
        requester.pageindex = mPageindex;
        requester.pagesize = mPageSize;
        requester.doPost();
    }

    public void initPullRefreshView() {
        mFragmentPrv.setOnPullRefreshListener(pullRefreshView -> {
            mPageindex = 1;
            displayData();
        });
        mFragmentPrv.setOnLoadMoreListener(pullRefreshView -> {
            mPageindex++;
            if (mSearchScheduingInfo == null && mVisitScreenInfo == null) {
                getConsultantScheduling("", "", 0, "", "");
            } else {
                getConsultantScheduling(mVisitScreenInfo == null ? "" : mVisitScreenInfo.getBeginDt(),
                        mVisitScreenInfo == null ? "" : mVisitScreenInfo.getEndDt(),
                        mVisitScreenInfo == null ? 0 : mVisitScreenInfo.getDoctorId(),
                        mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getKeyWord(),
                        mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getSearchType());
            }
        });
        mFragmentPrv.startPullRefresh();
    }

    interface OnTipsTvClickListener {
        void onTipsTvClicked(View view);
    }

    interface OnSwitchClickListener {
        void onTSwitchClicked(ScheduingListInfo ScheduingListInfo);
    }
}
