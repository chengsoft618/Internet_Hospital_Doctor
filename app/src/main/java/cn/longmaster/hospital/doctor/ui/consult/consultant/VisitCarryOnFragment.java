package cn.longmaster.hospital.doctor.ui.consult.consultant;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.customview.listview.PullRefreshView;
import cn.longmaster.doctorlibrary.util.common.DateUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.consultant.ScheduingListInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.SearchScheduingInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitDetailsInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitInfo;
import cn.longmaster.hospital.doctor.core.entity.consultant.VisitScreenInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.AssistantDoctorInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.im.MemberListInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.appointment.ScheduleServiceRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetSchedulePaymentRequester;
import cn.longmaster.hospital.doctor.core.requests.consultant.GetVisitListRequester;
import cn.longmaster.hospital.doctor.core.requests.im.GetChatMemberListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.im.ChatActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>就诊列表进行预约
 * Created by W·H·K on 2017/12/25.
 */

public class VisitCarryOnFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_visit_all_appointment_id)
    private TextView mAppointmentIdTv;
    @FindViewById(R.id.fragment_visit_all_time)
    private TextView mTimeTv;
    @FindViewById(R.id.fragment_visit_all_check)
    private ImageView mCheckIv;
    @FindViewById(R.id.fragment_visit_all_attending_doctor)
    private TextView mAttendingDoctor;
    @FindViewById(R.id.fragment_visit_all_superior_expert)
    private TextView mSuperiorExpert;
    @FindViewById(R.id.fragment_visit_all_prv)
    private PullRefreshView mVisitPrv;
    @FindViewById(R.id.fragment_visit_empty_layout)
    private LinearLayout mEmptyLayout;
    @FindViewById(R.id.fragment_visit_advance_view)
    private LinearLayout mAdvanceView;
    @FindViewById(R.id.fragment_visit_all_confirmation_sheet)
    private ImageView mConfirmationSheet;

    private int mPageindex = 1;
    private ScheduingListInfo mScheduingListInfo;
    private int mPageSize = 10;
    private VisitListAdapter mAdapter;
    private List<VisitDetailsInfo> mVisitDetailsInfos;
    private boolean mIsCheck = false;
    private List<VisitDetailsInfo> mAdvances = new ArrayList<>();
    private SearchScheduingInfo mSearchScheduingInfo;
    private VisitScreenInfo mVisitScreenInfo;

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_visit_all_layout;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        initView();
        initPullRefreshView();
        getScheduleService();
    }

    private void initData() {
        mScheduingListInfo = (ScheduingListInfo) getArguments().getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_LIST);
    }

    @Override
    public void onStart() {
        super.onStart();
        displaySearchData();
    }

    private void displaySearchData() {
        mAdapter.clear();
        mAdvances.clear();
        mIsCheck = false;
        mCheckIv.setImageResource(R.drawable.ic_visit_unselected);
        mSearchScheduingInfo = ((SchedulingActivity) getActivity()).getSearchScheduingInfo();
        mVisitScreenInfo = ((SchedulingActivity) getActivity()).getmVisitScreenInfo();
        Logger.logI(Logger.COMMON, "VisitCarryOnFragment->onStart()-->searchScheduingInfo:" + mSearchScheduingInfo + ",visitScreenInfo:" + mVisitScreenInfo);
        displayData();
    }

    private void displayData() {
        mVisitPrv.startPullRefresh();
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
        getSchedulePayment(mScheduingListInfo);
        mAppointmentIdTv.setText(getString(R.string.consult_payment_confirm_schedule_id, mScheduingListInfo.getScheduingId() + ""));
        String str = getString(R.string.advance_d);
        for (int i = 0; i < mScheduingListInfo.getAttDoctorList().size(); i++) {
            if (i == mScheduingListInfo.getAttDoctorList().size() - 1 || i == 3) {
                str += mScheduingListInfo.getAttDoctorList().get(i).getAttDoctorName();
                break;
            } else {
                str += mScheduingListInfo.getAttDoctorList().get(i).getAttDoctorName() + ",";
            }
        }
        mAttendingDoctor.setText(str);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new VisitListAdapter(getActivity(), false, false);
        mVisitPrv.setAdapter(mAdapter);
        mAdapter.setOnDoctorAssistantClickListener((view, assistantDoctorInfo, appointmentInfoId) -> displayHeadPortraitDialog(assistantDoctorInfo, appointmentInfoId));
        mAdapter.setOnCheckClickListener((view, position) -> {
            if (mVisitDetailsInfos.get(position).getSelected()) {
                mVisitDetailsInfos.get(position).setSelected(false);
                mAdvances.remove(mVisitDetailsInfos.get(position));
                if (mAdvances.size() == 0) {
                    mAdvanceView.setVisibility(View.GONE);
                }
            } else {
                mVisitDetailsInfos.get(position).setSelected(true);
                mAdvances.add(mVisitDetailsInfos.get(position));
                mAdvanceView.setVisibility(View.VISIBLE);
            }
            mAdapter.notifyDataSetChanged();
        });
    }

    private void getSchedulePayment(ScheduingListInfo listInfo) {
        GetSchedulePaymentRequester requester = new GetSchedulePaymentRequester((baseResult, schedulePaymentInfo) -> {
            if (baseResult.getCode() != OnResultListener.RESULT_SUCCESS) {
                return;
            }
            if (schedulePaymentInfo != null) {
                if (schedulePaymentInfo.getPaymentInfo() == null) {
                    mConfirmationSheet.setImageResource(R.drawable.ic_calendar_dark);
                } else {
                    mConfirmationSheet.setImageResource(R.drawable.ic_calendar);
                }
            }
        });
        requester.scheduingId = listInfo.getScheduingId();
        requester.doPost();
    }

    private void getVisitList(String beginDt, String endDt, int doctorId, String keyWord, String searchType) {
        GetVisitListRequester requester = new GetVisitListRequester((baseResult, visitInfo) -> {
            Logger.logI(Logger.COMMON, "VisitCarryOnFragment-->GetVisitListRequester-->visitInfo:" + visitInfo);
            setVisitInfo(visitInfo, baseResult);
        });
        requester.statNum = AppConstant.ConsultantStatusType.APPOINTMENT_STATUS_TYPE_CARRY_ON;
        requester.scheduingId = mScheduingListInfo.getScheduingId();
        requester.pageindex = mPageindex;
        requester.beginDt = beginDt;
        requester.endDt = endDt;
        requester.doctorId = doctorId;
        requester.pagesize = mPageSize;
        requester.searchType = searchType;
        requester.searchWord = keyWord;
        requester.doPost();
    }

    private void getScheduleService() {
        ScheduleServiceRequester serviceRequester = new ScheduleServiceRequester((baseResult, scheduleOrImageInfo) -> {
            Logger.logI(Logger.COMMON, "VisitCarryOnFragment-->etScheduleService-->scheduleOrImageInfo:" + scheduleOrImageInfo);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS && scheduleOrImageInfo != null) {
                if (StringUtils.isEmpty(scheduleOrImageInfo.getBeginDt()) || scheduleOrImageInfo.getBeginDt().contains("2099")) {
                    mTimeTv.setText(R.string.time_to_be_determined);
                } else {
                    mTimeTv.setText(DateUtil.getTime(scheduleOrImageInfo.getBeginDt(), "MM月dd日 HH:mm")
                            + "-" + DateUtil.getTime(scheduleOrImageInfo.getEndDt(), "HH:mm"));
                }
                getDoctorInfo(scheduleOrImageInfo.getDoctorId());
            }
        });
        serviceRequester.scheduingId = mScheduingListInfo.getScheduingId();
        serviceRequester.doPost();
    }

    private void setVisitInfo(VisitInfo visitInfo, BaseResult baseResult) {
        if (mVisitPrv.isRefreshing()) {
            mVisitPrv.stopPullRefresh();
        }
        if (mVisitPrv.isLoadingMore()) {
            mVisitPrv.stopLoadMore();
        }
        if (baseResult.getCode() == 0) {
            if (visitInfo.getVisitDetailsInfos() != null) {
                mVisitDetailsInfos = visitInfo.getVisitDetailsInfos();
                if (mPageindex == 1 && mVisitDetailsInfos.size() == 0) {
                    mEmptyLayout.setVisibility(View.VISIBLE);
                } else {
                    mEmptyLayout.setVisibility(View.GONE);
                }
                for (VisitDetailsInfo info : mVisitDetailsInfos) {
                    info.setSelected(false);
                }
                if (mPageindex == 1) {
                    mAdapter.setData(mVisitDetailsInfos);
                } else {
                    mAdapter.addData(mVisitDetailsInfos);
                }

                mVisitPrv.setLoadMoreEnable(visitInfo.getIsFinish() == 1);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        }
    }

    public void initPullRefreshView() {
        mVisitPrv.setOnPullRefreshListener(pullRefreshView -> {
            mPageindex = 1;
            displayData();
        });
        mVisitPrv.setOnLoadMoreListener(pullRefreshView -> {
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
        mVisitPrv.startPullRefresh();
    }

    private void getDoctorInfo(final int doctorId) {
        AppApplication.getInstance().getManager(DoctorManager.class).getDoctor(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (null == doctorBaseInfo || getActivity() == null) {
                    return;
                }
                mSuperiorExpert.setText(getActivity().getString(R.string.consult_payment_confirm_doctor, doctorBaseInfo.getRealName()));
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
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
                Logger.logD(Logger.COMMON, "VisitCarryOnFragment->getMemberList()->baseResult:" + baseResult);
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

    @OnClick({R.id.fragment_visit_all_advance_tv,
            R.id.fragment_visit_all_check,
            R.id.fragment_visit_all_confirmation_sheet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_visit_all_advance_tv:
                if (mAdvances.size() > 0) {
                    AdvanceActivity.startAdvanceActivity(getActivity(), mAdvances);
                } else {
                    ToastUtils.showShort(getString(R.string.no_advance_appointment));
                }
                break;
            case R.id.fragment_visit_all_check:
                displayCheckView();
                break;
            case R.id.fragment_visit_all_confirmation_sheet:
                Intent intent = new Intent(getActivity(), PaymentConfirmActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SCHEDULE_ID, mScheduingListInfo.getScheduingId());
                startActivity(intent);
                break;
        }
    }

    private void displayCheckView() {
        if (mIsCheck) {
            mIsCheck = false;
            mCheckIv.setImageResource(R.drawable.ic_visit_unselected);
            for (VisitDetailsInfo info : mVisitDetailsInfos) {
                if (info.getSelected()) {
                    info.setSelected(false);
                }
            }
            mAdvances.clear();
            mAdvanceView.setVisibility(View.GONE);
        } else {
            mIsCheck = true;
            mCheckIv.setImageResource(R.drawable.ic_visit_selected);
            for (VisitDetailsInfo info : mVisitDetailsInfos) {
                if (!info.getSelected() && info.getIsPay() == 0) {
                    info.setSelected(true);
                    mAdvances.add(info);
                }
            }
            if (mAdvances.size() > 0) {
                mAdvanceView.setVisibility(View.VISIBLE);
            } else {
                mAdvanceView.setVisibility(View.GONE);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
