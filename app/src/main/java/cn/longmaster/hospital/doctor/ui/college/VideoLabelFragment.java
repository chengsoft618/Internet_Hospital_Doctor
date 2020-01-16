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
 * Created by W·H·K on 2018/3/30.
 */

public class VideoLabelFragment extends NewBaseFragment {
    public final static String CLASS_CONFIG_INFO = "class_config_info";
    @FindViewById(R.id.fg_video_label_rv)
    private RecyclerView fgVideoLabelRv;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;

    private ClassConfigInfo mFlassConfigInfo;
    private int mPgeIndex = MIN_PAGE_INDEX_1;
    private List<CourseListInfo> mCourseListInfos = new ArrayList<>();
    private VideoListAdapter mAdapter;
    private int mIsFinish;
    private boolean mFragmentStop = false;

    public static VideoLabelFragment newInstance(ClassConfigInfo info) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(CLASS_CONFIG_INFO, info);
        VideoLabelFragment blankFragment = new VideoLabelFragment();
        blankFragment.setArguments(bundle);
        return blankFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_video_label;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mFlassConfigInfo = (ClassConfigInfo) getArguments().getSerializable(CLASS_CONFIG_INFO);
        getCourseList(AppConstant.CollegeCourseScreenType.COLLEGE_COURSE_SCREEN_TYPE_ALL);
    }

    private void initView() {
        GridLayoutManager mgr = new GridLayoutManager(getActivity(), 2);
        fgVideoLabelRv.setLayoutManager(mgr);
        mAdapter = new VideoListAdapter(R.layout.item_video_list, mCourseListInfos);
        fgVideoLabelRv.setAdapter(mAdapter);
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
    }

    private void getCourseList(int type) {
        CourseListRequester requester = new CourseListRequester((baseResult, courseListInfos) -> {
            Logger.logD(Logger.COMMON, "VideoListFragment->getClassifyConfig()->baseResult:" + baseResult + ", courseListInfos:" + courseListInfos);
            if (mFragmentStop) {
                return;
            }
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mIsFinish = baseResult.getIsFinish();
                Logger.logD(Logger.COMMON, "VideoListFragment->getClassifyConfig()->mIsFinish:" + mIsFinish);
                mCourseListInfos = courseListInfos;
                mAdapter.setNewData(courseListInfos);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.labelId = mFlassConfigInfo.getLabelId();
        requester.moduleType = mFlassConfigInfo.getModuleType();
        requester.orderType = type;
        requester.pageIndex = mPgeIndex;
        requester.pageSize = PAGE_SIZE;
        requester.doPost();
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

    @Override
    public void onStop() {
        super.onStop();
        mFragmentStop = true;
    }
}
