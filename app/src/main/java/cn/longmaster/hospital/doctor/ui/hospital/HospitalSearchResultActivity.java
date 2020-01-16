package cn.longmaster.hospital.doctor.ui.hospital;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.hospital.HospitalSearchRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.HospitalSearchAdapter;
import cn.longmaster.utils.LibCollections;


/**
 * @author Mloong_Abiao
 * @date 2019/6/4 14:31
 * @description: 医院搜索结果界面
 */
public class HospitalSearchResultActivity extends NewBaseActivity {

    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;

    @FindViewById(R.id.activity_hospital_search_result_rv)
    private RecyclerView activityHospitalSearchRv;
    @FindViewById(R.id.activity_hospital_search_result_srl)
    private SmartRefreshLayout activityHospitalSearchResultSrl;
    private HospitalSearchAdapter hospitalSearchAdapter;

    private int pageIndex = 1;
    private int pageSize = 20;

    @Override
    protected void initDatas() {
        hospitalSearchAdapter = new HospitalSearchAdapter(R.layout.item_hospital_list_search_layout, new ArrayList<HospitalInfo>(0));
        hospitalSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            HospitalInfo hospitalInfo = (HospitalInfo) adapter.getItem(position);
            if (null != hospitalInfo) {
                Intent intent = new Intent();
                intent.putExtra("hospital", hospitalInfo.getHospitalName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_hospital_search_result;
    }

    @Override
    public void initViews() {
        ivToolBarBack.setOnClickListener(view -> onBackPressed());
        tvToolBarTitle.setText("请选择医院");

        activityHospitalSearchRv.setLayoutManager(new LinearLayoutManager(this));
        activityHospitalSearchRv.setAdapter(hospitalSearchAdapter);
        activityHospitalSearchResultSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getHospitalList(getSearchFilter(), false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = MIN_PAGE_INDEX_1;
                getHospitalList(getSearchFilter(), true, refreshLayout);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityHospitalSearchResultSrl.autoRefresh();
    }

    private void getHospitalList(String str, boolean isRefresh, RefreshLayout refreshLayout) {
        HospitalSearchRequester requester = new HospitalSearchRequester(new DefaultResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    activityHospitalSearchResultSrl.finishLoadMoreWithNoMoreData();
                }
                if (pageIndex != 1) {
                    if (LibCollections.isNotEmpty(hospitalInfos)) {
                        hospitalSearchAdapter.addData(hospitalInfos);
                    }
                } else {
                    if (LibCollections.isNotEmpty(hospitalInfos)) {
                        hospitalSearchAdapter.setNewData(hospitalInfos);
                    } else {
                        hospitalSearchAdapter.setEmptyView(createEmptyListView("暂无相关数据"));
                        hospitalSearchAdapter.setNewData(null);
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
        requester.setHospitalName(str);
        requester.setPageIndex(pageIndex);
        requester.setPageSize(pageSize);
        requester.start();
    }

    private String getSearchFilter() {
        return getIntent().getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_QUERY_HOSPITAL);
    }
}
