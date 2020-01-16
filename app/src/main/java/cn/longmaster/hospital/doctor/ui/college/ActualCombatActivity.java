package cn.longmaster.hospital.doctor.ui.college;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.ClassConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.college.CourseListInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.college.CourseListRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.college.adapter.VideoListAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 名医实战讲堂activity
 * Created by W·H·K on 2018/3/23.
 */
public class ActualCombatActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_actual_combat_bar)
    private AppActionBar mBar;
    @FindViewById(R.id.activity_actual_combat_rv)
    private RecyclerView activityActualCombatRv;
    @FindViewById(R.id.activity_actual_combat_srl)
    private SmartRefreshLayout activityActualCombatSrl;

    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    private ClassConfigInfo mFlassConfigInfo;
    private VideoListAdapter mAdapter;
    private int mCurrentScreenType = AppConstant.CollegeCourseScreenType.COLLEGE_COURSE_SCREEN_TYPE_NEW;
    private String mTitle;
    private int pageIndex = MIN_PAGE_INDEX_1;

    @Override
    protected void initDatas() {
        mFlassConfigInfo = (ClassConfigInfo) getIntent().getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FLASS_CONFIG_INFO);
        mTitle = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_actual_combat;
    }

    @Override
    protected void initViews() {
        activityActualCombatRv.setLayoutManager(RecyclerViewUtils.getGridLayoutManager(getThisActivity(), 2));
        mAdapter = new VideoListAdapter(R.layout.item_video_list, new ArrayList<>(0));
        activityActualCombatRv.setAdapter(mAdapter);
        mBar.setTitle(mTitle);
        activityActualCombatSrl.autoRefresh();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CourseListInfo info = (CourseListInfo) adapter.getItem(position);
            if (null == info) {
                return;
            }
            int medicalAuth = info.getMedicalAuth();
            if (medicalAuth == 1) {
                final ProgressDialog progressDialog = createProgressDialog("权限校验中", false);
                mDoctorManager.getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
                    @Override
                    public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                        if (doctorBaseInfo.getViewingAuth() == 1) {
                            CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getThisActivity(), info.getCourseId());
                        } else {
                            showNoAuthorityDialog();
                        }
                    }

                    @Override
                    public void onFailed(int code, String msg) {
                        ToastUtils.showShort("权限校验失败");
                    }

                    @Override
                    public void onFinish() {
                        progressDialog.dismiss();
                    }
                });
            } else {
                CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getThisActivity(), info.getCourseId());
            }
        });
        activityActualCombatSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getCourseList(mCurrentScreenType, true, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = MIN_PAGE_INDEX_1;
                getCourseList(mCurrentScreenType, false, refreshLayout);
            }
        });
    }

    private void showNoAuthorityDialog() {
        View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.dialog_course_no_authority, null);
        RelativeLayout btnView = view.findViewById(R.id.dialog_no_authority_btn);
        TextView otherView = view.findViewById(R.id.dialog_no_authority_other_view);

        final Dialog dialog = new Dialog(getThisActivity(), R.style.custom_noActionbar_window_style);
        dialog.show();
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        setDialogViewListener(btnView, otherView, dialog);
    }

    private void setDialogViewListener(RelativeLayout btnView, TextView otherView, final Dialog dialog) {
        btnView.setOnClickListener(v -> {
            dialog.dismiss();
            StringBuilder urlStringBuilder = new StringBuilder(AppConfig.getQualificationUrl());
            urlStringBuilder.append("?user_id=");
            urlStringBuilder.append(mUserInfoManager.getCurrentUserInfo().getUserId());
            urlStringBuilder.append("&c_auth=");
            urlStringBuilder.append(SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH, ""));
            Intent intent = new Intent();
            intent.setClass(getThisActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, urlStringBuilder.toString());
            startActivity(intent);
        });
        otherView.setOnClickListener(v -> dialog.dismiss());
    }

    private void getCourseList(int type, final boolean isLoadMore, RefreshLayout refreshLayout) {
        CourseListRequester requester = new CourseListRequester((baseResult, courseListInfos) -> {
            if (isLoadMore) {
                refreshLayout.finishLoadMore();
            } else {
                refreshLayout.finishRefresh();
            }
            if (baseResult.isFinish()) {
                activityActualCombatSrl.finishLoadMoreWithNoMoreData();
            }
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (isLoadMore) {
                    mAdapter.addData(courseListInfos);
                } else {
                    mAdapter.setNewData(courseListInfos);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.labelId = mFlassConfigInfo.getLabelId();
        requester.moduleType = mFlassConfigInfo.getModuleType();
        requester.orderType = type;
        requester.pageIndex = pageIndex;
        requester.pageSize = PAGE_SIZE;
        requester.doPost();
    }

    public void rightClick(View view) {
        TextView text = (TextView) view;
        String string = text.getText().toString();
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.dialog_actual_combat, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final TextView selectedTv = contentView.findViewById(R.id.dialog_newest_tv);
        final TextView uncheckedTv = contentView.findViewById(R.id.dialog_hottest_tv);
        if (string.equals(getString(R.string.medical_college_newest))) {
            selectedTv.setText(R.string.medical_college_newest);
            uncheckedTv.setText(R.string.medical_college_hottest);
        } else {
            selectedTv.setText(R.string.medical_college_hottest);
            uncheckedTv.setText(R.string.medical_college_newest);
        }
        setListener(selectedTv, uncheckedTv, popupWindow);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(view, DisplayUtil.dip2px(-18), DisplayUtil.dip2px(-40));
    }

    private void setListener(final TextView selectedTv, final TextView uncheckedTv, final PopupWindow popupWindow) {
        selectedTv.setOnClickListener(v -> refreshView(popupWindow, selectedTv));
        uncheckedTv.setOnClickListener(v -> refreshView(popupWindow, uncheckedTv));
    }

    public void refreshView(PopupWindow popupWindow, TextView textView) {
        mBar.setRightText(textView.getText().toString());
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        pageIndex = MIN_PAGE_INDEX_1;
        if ((textView.getText().toString()).equals(getString(R.string.medical_college_newest))) {
            mCurrentScreenType = AppConstant.CollegeCourseScreenType.COLLEGE_COURSE_SCREEN_TYPE_NEW;
        } else {
            mCurrentScreenType = AppConstant.CollegeCourseScreenType.COLLEGE_COURSE_SCREEN_TYPE_HOT;
        }
        activityActualCombatSrl.autoRefresh();
    }
}
