package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyPatientItemInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyPatientAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/16 11:30
 * @description: 门诊患者列表界面
 */
public class DCDutyPatientListActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dc_duty_patient_list_aab)
    private AppActionBar actDcDutyPatientListAab;
    @FindViewById(R.id.act_dc_duty_patient_list_srl)
    private SmartRefreshLayout actDcDutyPatientListSrl;
    @FindViewById(R.id.act_dc_duty_patient_list_rv)
    private RecyclerView actDcDutyPatientListRv;

    @AppApplication.Manager
    private DCManager dcManager;
    private int projectId;
    private DCDutyPatientAdapter dcDutyPatientAdapter;

    @Override
    protected void initDatas() {
        projectId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, 0);
        dcDutyPatientAdapter = new DCDutyPatientAdapter(R.layout.item_dc_duty_patient, new ArrayList<>(0));
        dcDutyPatientAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyPatientItemInfo info = (DCDutyPatientItemInfo) adapter.getItem(position);
            if (null != info) {
                getDisplay().startDCDutyPatientDetailActivity(info.getUserId(), projectId, 0);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dcduty_patient_list;
    }

    @Override
    protected void initViews() {
        actDcDutyPatientListSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX++;
                getPatientList(projectId, refreshLayout);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                PAGE_INDEX = MIN_PAGE_INDEX_1;
                getPatientList(projectId, refreshLayout);
            }
        });
        actDcDutyPatientListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyPatientListRv.setAdapter(dcDutyPatientAdapter);
        actDcDutyPatientListAab.setLeftOnClickListener(v -> {
            onBackPressed();
        });
        actDcDutyPatientListSrl.autoRefresh();
    }

    private void getPatientList(int projectId, RefreshLayout refreshLayout) {
        boolean isRefresh = PAGE_INDEX == MIN_PAGE_INDEX_1;
        dcManager.getPatientList(projectId, 0, PAGE_INDEX, PAGE_SIZE, new DefaultResultCallback<List<DCDutyPatientItemInfo>>() {
            @Override
            public void onSuccess(List<DCDutyPatientItemInfo> dcDutyPatientItemInfos, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    actDcDutyPatientListSrl.finishLoadMoreWithNoMoreData();
                }
                if (isRefresh) {
                    if (LibCollections.isEmpty(dcDutyPatientItemInfos)) {
                        dcDutyPatientAdapter.setNewData(null);
                        dcDutyPatientAdapter.setEmptyView(createEmptyListView());
                    } else {
                        dcDutyPatientAdapter.setNewData(dcDutyPatientItemInfos);
                    }
                } else {
                    dcDutyPatientAdapter.addData(dcDutyPatientItemInfos);
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
    }
}
