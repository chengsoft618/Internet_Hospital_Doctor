package cn.longmaster.hospital.doctor.ui.hospital;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import cn.longmaster.hospital.doctor.core.requests.hospital.HospitalSearchRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.hospital.adaprer.HospitalFilterAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/7/3 17:53
 * @description: 选择医院
 */
public class HospitalChooseActivity extends NewBaseActivity {

    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.include_search_title_et)
    private EditText includeSearchTitleEt;
    @FindViewById(R.id.include_search_clear_iv)
    private ImageView includeSearchClearIv;
    @FindViewById(R.id.include_search_operation_tv)
    private TextView includeSearchOperationTv;
    @FindViewById(R.id.activity_hospital_search_result_srl)
    private SmartRefreshLayout activityHospitalSearchResultSrl;
    @FindViewById(R.id.activity_hospital_search_result_rv)
    private RecyclerView activityHospitalSearchResultRv;

    private HospitalFilterAdapter hospitalFilterAdapter;
    private int mPageIndex = MIN_PAGE_INDEX_1;

    @Override
    protected void initDatas() {
        hospitalFilterAdapter = new HospitalFilterAdapter(R.layout.item_hospital_list_filter_layout, new ArrayList<>(0));
        hospitalFilterAdapter.setOnItemClickListener((adapter, view, position) -> {
            HospitalInfo hospitalInfo = (HospitalInfo) adapter.getItem(position);
            if (null != hospitalInfo) {
                Intent intent = new Intent();
                intent.putExtra("hospital", hospitalInfo);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_hospital_choose;
    }

    @Override
    protected void initViews() {
        ivToolBarBack.setOnClickListener(v -> onBackPressed());
        tvToolBarTitle.setText("选择医院");
        includeSearchOperationTv.setText("查找");
        includeSearchTitleEt.setHint("输入医院名称");
        includeSearchClearIv.setOnClickListener(v -> {
            includeSearchTitleEt.setText(null);
            activityHospitalSearchResultSrl.autoRefresh();
        });
        includeSearchOperationTv.setOnClickListener(v -> {
            activityHospitalSearchResultSrl.autoRefresh();
        });
        includeSearchTitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isTrimEmpty(s)) {
                    includeSearchClearIv.setVisibility(View.GONE);
                } else {
                    includeSearchClearIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        includeSearchTitleEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                activityHospitalSearchResultSrl.autoRefresh();
                return true;
            }
            return false;
        });
        activityHospitalSearchResultRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        activityHospitalSearchResultRv.setAdapter(hospitalFilterAdapter);
        activityHospitalSearchResultSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                getHospitalList(getString(includeSearchTitleEt), false, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = MIN_PAGE_INDEX_1;
                getHospitalList(getString(includeSearchTitleEt), true, refreshLayout);
            }
        });
        activityHospitalSearchResultSrl.autoRefresh();
    }

    private void getHospitalList(String str, boolean isRefresh, RefreshLayout refreshLayout) {
        HospitalSearchRequester requester = new HospitalSearchRequester(new DefaultResultCallback<List<HospitalInfo>>() {
            @Override
            public void onSuccess(List<HospitalInfo> hospitalInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    activityHospitalSearchResultSrl.finishLoadMoreWithNoMoreData();
                }
                if (mPageIndex != MIN_PAGE_INDEX_1) {
                    if (LibCollections.isNotEmpty(hospitalInfos)) {
                        hospitalFilterAdapter.addData(hospitalInfos);
                    }
                } else {
                    if (LibCollections.isNotEmpty(hospitalInfos)) {
                        hospitalFilterAdapter.setNewData(hospitalInfos);
                    } else {
                        hospitalFilterAdapter.setNewData(null);
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
        requester.setPageIndex(mPageIndex);
        requester.setPageSize(PAGE_SIZE);
        requester.start();
    }
}
