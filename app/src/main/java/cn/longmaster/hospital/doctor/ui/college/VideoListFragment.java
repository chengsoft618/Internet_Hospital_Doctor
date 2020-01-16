package cn.longmaster.hospital.doctor.ui.college;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
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
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.college.adapter.VideoListAdapter;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 视频列表fragment
 * Created by W·H·K on 2018/3/19.
 */
public class VideoListFragment extends NewBaseFragment {
    private final static String CLASS_CONFIG_INFO = "class_config_info";
    @FindViewById(R.id.fragment_video_list_rv)
    private RecyclerView mRecyclerView;
    @FindViewById(R.id.fragment_video_list_empty_view)
    private LinearLayout mEmptyView;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;

    private VideoListAdapter mAdapter;
    private ClassConfigInfo mFlassConfigInfo;
    private List<CourseListInfo> mCourseListInfos = new ArrayList<>();
    private int mOrderType = 0;
    private int mPageIndex = 1;
    private OnScrollListener mOnScrollListener;
    private boolean mFragmentStop = false;

    public static VideoListFragment newInstance(ClassConfigInfo info) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CLASS_CONFIG_INFO, info);
        VideoListFragment blankFragment = new VideoListFragment();
        blankFragment.setArguments(bundle);
        return blankFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_video_list;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mFlassConfigInfo = (ClassConfigInfo) getArguments().getSerializable(CLASS_CONFIG_INFO);
        if (mFlassConfigInfo.getModuleType() == AppConstant.CollegeModularType.COLLEGE_MODULAR_TYPE_VIDEO) {
            getCourseList(1, mPageIndex);
        } else {
            getCourseList(0, mPageIndex);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    private void getCourseList(int orderType, int pageIndex) {
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mgr);
        CourseListRequester requester = new CourseListRequester((baseResult, courseListInfos) -> {
            Logger.logD(Logger.COMMON, "VideoListFragment->getClassifyConfig()->baseResult:" + baseResult + ", courseListInfos:" + courseListInfos);
            if (mFragmentStop) {
                return;
            }
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mCourseListInfos = courseListInfos;
                displayCourseList(mCourseListInfos);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.labelId = mFlassConfigInfo.getLabelId();
        requester.moduleType = mFlassConfigInfo.getModuleType();
        requester.orderType = orderType;
        requester.pageIndex = pageIndex;
        requester.pageSize = 8;
        requester.doPost();
    }

    private void displayCourseList(final List<CourseListInfo> courseListInfos) {
        if (courseListInfos.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setNewData(courseListInfos);
            mEmptyView.setVisibility(View.GONE);
            if (mFlassConfigInfo.getModuleType() == AppConstant.CollegeModularType.COLLEGE_MODULAR_TYPE_VIDEO) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_college_foot, mRecyclerView, false);
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ActualCombatActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FLASS_CONFIG_INFO, mFlassConfigInfo);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, mFlassConfigInfo.getModuleTitle());
                    startActivity(intent);
                });
                mAdapter.addFooterView(view);
            }
        }
    }

    private void initView() {
        mAdapter = new VideoListAdapter(R.layout.item_video_list, mCourseListInfos);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CourseListInfo info = (CourseListInfo) adapter.getItem(position);
            if (null == info) {
                return;
            }
            int medicalAuth = info.getMedicalAuth();
            Logger.logD(Logger.COMMON, "VideoListFragment->initListener()->medicalAuth:" + medicalAuth);
            if (medicalAuth == 1) {
                getDoctorInfo(position);
            } else {
                CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getActivity(), info.getCourseId());
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollListener(recyclerView, dx, dy);
                }

            }
        });
    }

    private void getDoctorInfo(final int position) {
        final ProgressDialog progressDialog = createProgressDialog("权限校验中", false);
        mDoctorManager.getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId(), new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                if (doctorBaseInfo.getViewingAuth() == 1) {
                    CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getActivity(), mCourseListInfos.get(position).getCourseId());
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
    }

    private void showNoAuthorityDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_course_no_authority, null);
        RelativeLayout btnView = view.findViewById(R.id.dialog_no_authority_btn);
        TextView otherView = view.findViewById(R.id.dialog_no_authority_other_view);

        final Dialog dialog = new Dialog(getActivity(), R.style.custom_noActionbar_window_style);
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
            intent.setClass(getActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, urlStringBuilder.toString());
            Logger.logD(Logger.APPOINTMENT, "UsercenterFragment->url:" + urlStringBuilder.toString());
            startActivity(intent);
        });
        otherView.setOnClickListener(v -> dialog.dismiss());
    }

    public interface OnScrollListener {
        void onScrollListener(RecyclerView view, int dx, int dy);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentStop = true;
        Logger.logD(Logger.APPOINTMENT, "VideoListFragment->测试：onStop");
    }
}
