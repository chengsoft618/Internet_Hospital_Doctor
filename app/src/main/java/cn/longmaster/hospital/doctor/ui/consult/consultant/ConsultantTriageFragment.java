package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.doctorlibrary.customview.listview.PullRefreshView;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.entity.consultant.SearchScheduingInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitScreenInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetVisitListRequester;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatMemberListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.im.ChatActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 分诊会诊fragment
 * Created by W·H·K on 2017/12/22.
 */

public class ConsultantTriageFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_prv)
    private PullRefreshView mFragmentPrv;
    @FindViewById(R.id.fragment_tips_tv)
    private TextView mTipsTv;
    @FindViewById(R.id.fragment_empty_layout)
    private LinearLayout mEmptyLayout;

    private int mPageindex = 1;
    private int mPageSize = 10;
    private List<VisitDetailsInfo> mVisitDetailsInfos;
    private VisitListAdapter mAdapter;
    private SearchScheduingInfo mSearchScheduingInfo;
    private VisitScreenInfo mVisitScreenInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_all_layout;
    }

    @Override
    public void initViews(View rootView) {
        initPullRefreshView();
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        displaySearchData();
    }

    private void displaySearchData() {
        mSearchScheduingInfo = ((SchedulingActivity) getActivity()).getSearchScheduingInfo();
        mVisitScreenInfo = ((SchedulingActivity) getActivity()).getmVisitScreenInfo();
        Logger.logI(Logger.COMMON, "ConsultantTriageFragment->onStart()-->searchScheduingInfo:" + mSearchScheduingInfo + ",visitScreenInfo:" + mVisitScreenInfo);
        displayData();
    }

    private void displayData() {
        if (mSearchScheduingInfo == null && mVisitScreenInfo == null) {
            getVisitList("", "", 0, "", "");
        } else {
            mPageindex = 1;
            getVisitList(mVisitScreenInfo == null ? "" : mVisitScreenInfo.getBeginDt(),
                    mVisitScreenInfo == null ? "" : mVisitScreenInfo.getEndDt(),
                    mVisitScreenInfo == null ? 0 : mVisitScreenInfo.getDoctorId(),
                    mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getKeyWord(),
                    mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getSearchType());
        }
    }

    private void initView() {
        mTipsTv.setVisibility(View.GONE);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new VisitListAdapter(getActivity(), true, false);
        mFragmentPrv.setAdapter(mAdapter);
        mAdapter.setOnDoctorAssistantClickListener((view, assistantDoctorInfo, appointmentInfoId) -> displayHeadPortraitDialog(assistantDoctorInfo, appointmentInfoId));
        mAdapter.setOnCheckClickListener((view, position) -> {
            if (mVisitDetailsInfos.get(position).getSelected()) {
                mVisitDetailsInfos.get(position).setSelected(false);
            } else {
                mVisitDetailsInfos.get(position).setSelected(true);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void displayHeadPortraitDialog(AssistantDoctorInfo assistantDoctorInfo, int appointmentInfoId) {
        View heardView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_visit_doctor_assistant_info_dialog, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(heardView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        displayHeadPortraitView(heardView, assistantDoctorInfo, appointmentInfoId);
    }

    private void displayHeadPortraitView(View heardView, final AssistantDoctorInfo assistantDoctorInfo, int appointmentInfoId) {
        final CircleImageView heardPortraitDialogImg = heardView.findViewById(R.id.layout_dialog_doctor_name_img);
        final TextView doctorName = heardView.findViewById(R.id.layout_dialog_doctor_name_tv);
        final TextView doctorPhone = heardView.findViewById(R.id.layout_dialog_patient_phone);
        final TextView sendMassageTv = heardView.findViewById(R.id.layout_dialog_doctor_send_massage_tv);
        displayAdminInfo(heardPortraitDialogImg, doctorName, doctorPhone, assistantDoctorInfo);
        getGroupId(appointmentInfoId, sendMassageTv);
    }

    private void getGroupId(final int appointmentInfoId, final TextView sendMassageTv) {
        GetChatMemberListRequester requester = new GetChatMemberListRequester(new DefaultResultCallback<List<MemberListInfo>>() {
            @Override
            public void onSuccess(List<MemberListInfo> memberListInfos, BaseResult baseResult) {
                Logger.logD(Logger.COMMON, "ConsultantTriageFragment->getGroupId()->baseResult:" + baseResult);
                sendMassageTv.setOnClickListener(v -> ChatActivity.startChatActivity(getActivity(), appointmentInfoId, memberListInfos.get(0).getGroupId()));
            }
        });
        requester.appointmentId = appointmentInfoId;
        requester.doPost();
    }

    private void displayAdminInfo(CircleImageView heardPortraitDialogImg, TextView doctorName, final TextView doctorPhone, AssistantDoctorInfo assistantDoctorInfo) {
        GlideUtils.showDoctorOnLineAvatar(heardPortraitDialogImg, getBaseActivity(), AvatarUtils.getAvatar(false, assistantDoctorInfo.getUserId(), "0"));
        doctorName.setText(assistantDoctorInfo.getUserName());
        doctorPhone.setText(assistantDoctorInfo.getPhoneNum() + "");
        doctorPhone.setOnClickListener(v -> call(doctorPhone.getText().toString()));
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getVisitList(String beginDt, String endDt, int doctorId, String keyWord, String searchType) {
        GetVisitListRequester requester = new GetVisitListRequester((baseResult, visitInfo) -> {
            Logger.logI(Logger.COMMON, "ConsultantTriageFragment-->GetVisitListRequester-->visitInfo:" + visitInfo + ",baseResult:" + baseResult);
            if (mFragmentPrv.isRefreshing()) {
                mFragmentPrv.stopPullRefresh();
            }
            if (mFragmentPrv.isLoadingMore()) {
                mFragmentPrv.stopLoadMore();
            }
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (visitInfo.getVisitDetailsInfos() != null) {
                    mVisitDetailsInfos = visitInfo.getVisitDetailsInfos();
                    for (VisitDetailsInfo info : mVisitDetailsInfos) {
                        info.setSelected(false);
                    }
                    if (mPageindex == 1 && mVisitDetailsInfos.size() == 0) {
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    } else {
                        mEmptyLayout.setVisibility(View.GONE);
                    }
                }
                if (mPageindex == 1) {
                    mAdapter.setData(mVisitDetailsInfos);
                } else {
                    mAdapter.addData(mVisitDetailsInfos);
                }
                mFragmentPrv.setLoadMoreEnable(visitInfo.getIsFinish() == 1);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.statNum = AppConstant.ConsultantStatusType.APPOINTMENT_STATUS_TYPE_TRIAGE;
        requester.scheduingId = -1;
        requester.pageindex = mPageindex;
        requester.beginDt = beginDt;
        requester.endDt = endDt;
        requester.doctorId = doctorId;
        requester.pagesize = mPageSize;
        requester.searchType = searchType + "";
        requester.searchWord = keyWord;
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
                getVisitList("", "", 0, "", "");
            } else {
                getVisitList(mVisitScreenInfo == null ? "" : mVisitScreenInfo.getBeginDt(),
                        mVisitScreenInfo == null ? "" : mVisitScreenInfo.getEndDt(),
                        mVisitScreenInfo == null ? 0 : mVisitScreenInfo.getDoctorId(),
                        mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getKeyWord(),
                        mSearchScheduingInfo == null ? "" : mSearchScheduingInfo.getSearchType());
            }
        });
        mFragmentPrv.startPullRefresh();
    }
}
