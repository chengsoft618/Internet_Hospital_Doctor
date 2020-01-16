package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorItemInfo;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.doctor.GetDoctorListRegularlyRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.doctor.adapter.DoctorListNewAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.ToastUtils;

import static android.app.Activity.RESULT_OK;


/**
 * 常用专家fragemnt
 * Created by W·H·K on 2018/5/7.
 */
public class CommonlyExpertFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_commonly_expert_srl)
    private SmartRefreshLayout fragmentCommonlyExpertSrl;
    @FindViewById(R.id.fragment_commonly_expert_rv)
    private RecyclerView fragmentCommonlyExpertRv;
    @FindViewById(R.id.include_new_no_data_ll)
    private LinearLayout includeNewNoDataLl;
    private OnDoctorListLoadListener onDoctorListLoadListener;

    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    private final int REQUEST_CODE_CHOICE_DOCTOR = 100;
    private static final int REQUEST_CODE_SEARCH_DOCTOR = 1024;
    private DoctorListNewAdapter doctorListNewAdapter;
    private int pageIndex = MIN_PAGE_INDEX_1;
    private int mDepartment;

    public static CommonlyExpertFragment getInstance() {
        CommonlyExpertFragment commonlyExpertFragment = new CommonlyExpertFragment();
        return commonlyExpertFragment;
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        doctorListNewAdapter = new DoctorListNewAdapter(R.layout.item_doctor_list_layout_test, new ArrayList<>(0));
        doctorListNewAdapter.setOnItemClickListener((adapter, view, position) -> {
            DoctorItemInfo doctorItemInfo = (DoctorItemInfo) adapter.getItem(position);
            if (null != doctorItemInfo) {
                if (mUserInfoManager.getCurrentUserInfo().getUserId() == doctorItemInfo.getDoctorId()) {
                    ToastUtils.showShort(getString(R.string.rounds_info_not_to_oneself));
                    return;
                }
                if (doctorItemInfo.getDoctorId() > 0) {
                    getDisplay().startSelectionTimeActivity(doctorItemInfo.getDoctorId(), true, null, REQUEST_CODE_CHOICE_DOCTOR);
                } else {
                    finishWithResult(doctorItemInfo.getDoctorId(), null);
                }
            }
        });
        doctorListNewAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DoctorItemInfo doctorItemInfo = (DoctorItemInfo) adapter.getItem(position);
            if (null != doctorItemInfo) {
                getDisplay().startDoctorDetailActivity(doctorItemInfo.getDoctorId(), true, true, REQUEST_CODE_CHOICE_DOCTOR);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_rounds_commonly_expert_fragment;
    }

    @Override
    public void initViews(View rootView) {
        fragmentCommonlyExpertRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentCommonlyExpertRv.setAdapter(doctorListNewAdapter);
        fragmentCommonlyExpertSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                if (0 != mDepartment) {
                    getDoctorList(mDepartment, true, refreshLayout);
                } else {
                    Logger.logE(Logger.DOCTOR, "department id is 0");
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = MIN_PAGE_INDEX_1;
                getDoctorList(mDepartment, false, refreshLayout);
            }
        });
        getMyDepartmentId();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOICE_DOCTOR) {
                finishWithResult(data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0), data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST));
            }
        }
    }

    private void finishWithResult(int doctorId, ArrayList<String> timeList) {
        Intent intent = new Intent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, doctorId);
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, timeList);
        getBaseActivity().setResult(Activity.RESULT_OK, intent);
        getBaseActivity().finish();
    }

    private void getDoctorList(int departmentId, boolean isLoadMore, RefreshLayout refreshLayout) {
        GetDoctorListRegularlyRequester requester = new GetDoctorListRegularlyRequester(new DefaultResultCallback<List<DoctorItemInfo>>() {
            @Override
            public void onSuccess(List<DoctorItemInfo> doctorItemInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    fragmentCommonlyExpertSrl.finishLoadMoreWithNoMoreData();
                }
                if (pageIndex == MIN_PAGE_INDEX_1) {
                    if (null != onDoctorListLoadListener) {
                        onDoctorListLoadListener.onDoctorLoad(LibCollections.isNotEmpty(doctorItemInfos));
                    }
                    doctorListNewAdapter.setNewData(doctorItemInfos);
                    includeNewNoDataLl.setVisibility(LibCollections.isEmpty(doctorItemInfos) ? View.VISIBLE : View.GONE);
                    fragmentCommonlyExpertSrl.setVisibility(LibCollections.isEmpty(doctorItemInfos) ? View.GONE : View.VISIBLE);
                } else {
                    doctorListNewAdapter.addData(doctorItemInfos);
                    includeNewNoDataLl.setVisibility(LibCollections.isNotEmpty(doctorItemInfos) ? View.VISIBLE : View.GONE);
                    fragmentCommonlyExpertSrl.setVisibility(LibCollections.isNotEmpty(doctorItemInfos) ? View.GONE : View.VISIBLE);
                }
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
        requester.setDepartmentId(departmentId);
        requester.setPageIndex(pageIndex);
        requester.setPageSize(PAGE_SIZE);
        requester.start();
    }

    private void getMyDepartmentId() {
        mDoctorManager.getDoctorInfo(mUserInfoManager.getCurrentUserInfo().getUserId(), false, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDepartment = doctorBaseInfo.getDepartmentId();
                fragmentCommonlyExpertSrl.autoRefresh();
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void setOnDoctorListLoadListener(OnDoctorListLoadListener onDoctorListLoadListener) {
        this.onDoctorListLoadListener = onDoctorListLoadListener;
    }

    public interface OnDoctorListLoadListener {
        void onDoctorLoad(boolean hasDoctor);
    }
}
