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
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.hospital.GetHospitalByCityRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.HospitalFilterAdapter;
import cn.longmaster.utils.LibCollections;

/**
 * @author Mloong_Abiao
 * @date 2019/6/4 14:31
 * @description: 医生根据城市筛选界面
 */
public class HospitalFilterActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView includeTitleBackIv;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView includeToolBarTitle;
    @FindViewById(R.id.activity_hospital_province_name_tv)
    private TextView activityHospitalProvinceNameTv;
    @FindViewById(R.id.activity_hospital_city_name_tv)
    private TextView activityHospitalCityNameTv;
    @FindViewById(R.id.activity_hospital_filter_rv)
    private RecyclerView activityHospitalFilterRv;
    @FindViewById(R.id.activity_hospital_filter_srl)
    private SmartRefreshLayout activityHospitalFilterSrl;
    private HospitalFilterAdapter hospitalFilterAdapter;
    public static final String KEY_TO_QUERY_PROVINCE = "_KEY_TO_QUERY_PROVINCE_";
    public static final String KEY_TO_QUERY_CITY = "_KEY_TO_QUERY_CITY_";
    private int pageIndex = 1;
    private final int pageSize = 10;

    @Override
    protected void initDatas() {
        hospitalFilterAdapter = new HospitalFilterAdapter(R.layout.item_hospital_list_filter_layout, new ArrayList<>(0));
        hospitalFilterAdapter.setOnItemClickListener((adapter, view, position) -> {
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
    protected int getContentViewId() {
        return R.layout.activity_hospital_filter;
    }

    @Override
    protected void initViews() {
        includeToolBarTitle.setText("选择医院");
        activityHospitalFilterRv.setLayoutManager(new LinearLayoutManager(this));
        activityHospitalFilterRv.setAdapter(hospitalFilterAdapter);
        includeTitleBackIv.setOnClickListener(view -> onBackPressed());
        activityHospitalProvinceNameTv.setText(getIntent().getStringExtra(KEY_TO_QUERY_PROVINCE));
        activityHospitalCityNameTv.setText(getIntent().getStringExtra(KEY_TO_QUERY_CITY));
        activityHospitalFilterSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                getHospitalList(false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = MIN_PAGE_INDEX_1;
                getHospitalList(true, refreshLayout);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityHospitalFilterSrl.autoRefresh();
    }

    private void getHospitalList(boolean isRefresh, RefreshLayout refreshLayout) {
        GetHospitalByCityRequester requester = new GetHospitalByCityRequester(new DefaultResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos, BaseResult baseResult) {
                if (pageIndex == MIN_PAGE_INDEX_1) {
                    hospitalFilterAdapter.setNewData(hospitalInfos);
                } else if (LibCollections.isNotEmpty(hospitalInfos)) {
                    hospitalFilterAdapter.addData(hospitalInfos);
                }
                if (baseResult.isFinish()) {
                    activityHospitalFilterSrl.finishLoadMoreWithNoMoreData();
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
        requester.setProvince(getString(activityHospitalProvinceNameTv));
        requester.setCity(getString(activityHospitalCityNameTv));
        requester.setPageIndex(pageIndex);
        requester.setPageSize(pageSize);
        requester.start();
    }
}
