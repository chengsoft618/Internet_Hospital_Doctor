package cn.longmaster.hospital.doctor.ui.department;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.department.DepartmentSearchRequest;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.department.adpter.DepartmentSearchAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * @author Mloong_Abiao
 * @date 2019/6/4 20:20
 * @description: 可是搜索结果界面
 */
public class DepartmentSearchResultActivity extends NewBaseActivity {
    @FindViewById(R.id.act_department_search_result_aab)
    private AppActionBar actDepartmentSearchResultAab;
    @FindViewById(R.id.activity_department_search_result_rv)
    private RecyclerView activityDepartmentSearchResultRv;
    @FindViewById(R.id.activity_department_search_result_srl)
    private SmartRefreshLayout activityDepartmentSearchResultSrl;
    private DepartmentSearchAdapter departmentSearchAdapter;

    private String mDepartmentFilter;
    private int pageIndex = MIN_PAGE_INDEX_1;

    @Override
    protected void initDatas() {
        departmentSearchAdapter = new DepartmentSearchAdapter(R.layout.item_department_list_search_layout, new ArrayList<>(0));

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_department_search_result;
    }

    @Override
    protected void initViews() {
        actDepartmentSearchResultAab.setLeftOnClickListener(view -> onBackPressed());
        departmentSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            DepartmentInfo departmentInfo = (DepartmentInfo) adapter.getItem(position);
            if (null != departmentInfo) {
                Intent intent = new Intent();
                intent.putExtra("department", departmentInfo.getDepartmentName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        activityDepartmentSearchResultSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getDepartmentList(false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = MIN_PAGE_INDEX_1;
                getDepartmentList(true, refreshLayout);
            }
        });
        activityDepartmentSearchResultRv.setLayoutManager(new LinearLayoutManager(this));
        activityDepartmentSearchResultRv.setAdapter(departmentSearchAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityDepartmentSearchResultSrl.autoRefresh();
    }

    private void getDepartmentList(boolean isRefresh, RefreshLayout refreshLayout) {
        DepartmentSearchRequest request = new DepartmentSearchRequest(new DefaultResultCallback<List<DepartmentInfo>>() {
            @Override
            public void onSuccess(List<DepartmentInfo> departmentInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    activityDepartmentSearchResultSrl.finishLoadMoreWithNoMoreData();
                }
                if (pageIndex != MIN_PAGE_INDEX_1) {
                    if (LibCollections.isNotEmpty(departmentInfos)) {
                        departmentSearchAdapter.addData(departmentInfos);
                    }
                } else {
                    if (LibCollections.isNotEmpty(departmentInfos)) {
                        departmentSearchAdapter.setNewData(departmentInfos);
                    } else {
                        departmentSearchAdapter.setEmptyView(createEmptyListView());
                        departmentSearchAdapter.setNewData(null);
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
        request.setPageIndex(pageIndex);
        request.setPageSize(PAGE_SIZE);
        request.setDepartmentName(getKeyToQueryDepartment());
        request.start();
    }

    private String getKeyToQueryDepartment() {
        if (StringUtils.isTrimEmpty(mDepartmentFilter)) {
            mDepartmentFilter = getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_QUERY_DEPARTMENT);
        }
        return mDepartmentFilter;
    }
}
