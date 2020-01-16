package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantItem;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyVisitPlantListAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 22:19
 * @description: 随访计划模版选择列表
 */
public class DcDutyVisitPlanListActivity extends NewBaseActivity {
    private final int REQUEST_CODE_FOR_CHOOSE_VISIT_PLANT = 200;
    @FindViewById(R.id.act_dc_duty_visit_plan_list_aab)
    private AppActionBar actDcDutyVisitPlanListAab;
    @FindViewById(R.id.act_dc_duty_visit_plan_list_srl)
    private SmartRefreshLayout actDcDutyVisitPlanListSrl;
    @FindViewById(R.id.act_dc_duty_visit_plan_list_rv)
    private RecyclerView actDcDutyVisitPlanListRv;
    private DCDutyVisitPlantListAdapter dcDutyVisitPlantListAdapter;
    @AppApplication.Manager
    DCManager dcManager;
    private int mMedicalId;
    private int mProjectId;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mMedicalId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_MEDICAL_ID, 0);
        mProjectId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PROJECT_ID, 0);
    }

    @Override
    protected void initDatas() {
        dcDutyVisitPlantListAdapter = new DCDutyVisitPlantListAdapter(R.layout.item_dc_duty_visit_plant, new ArrayList<>(0));
        dcDutyVisitPlantListAdapter.setOnItemClickListener((adapter, view, position) -> {
            DCDutyVisitPlantItem item = (DCDutyVisitPlantItem) adapter.getItem(position);
            if (null != item) {
                getDisplay().startDcDutyVisitPlanDetailsActivity(mMedicalId, item.getTempId(), REQUEST_CODE_FOR_CHOOSE_VISIT_PLANT);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_visit_plant_list;
    }

    @Override
    protected void initViews() {
        actDcDutyVisitPlanListSrl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getVisitPlant(refreshLayout, mProjectId);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getVisitPlant(refreshLayout, mProjectId);
            }
        });
        actDcDutyVisitPlanListRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyVisitPlanListRv.setHasFixedSize(true);
        actDcDutyVisitPlanListRv.setAdapter(dcDutyVisitPlantListAdapter);
        actDcDutyVisitPlanListAab.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        actDcDutyVisitPlanListSrl.autoRefresh();
        initListener();
    }

    private void getVisitPlant(RefreshLayout refreshLayout, int mProjectId) {
        dcManager.getVisitPlantListByProject(mProjectId, new DefaultResultCallback<List<DCDutyVisitPlantItem>>() {
            @Override
            public void onSuccess(List<DCDutyVisitPlantItem> dcDutyVisitPlantItems, BaseResult baseResult) {
                if (baseResult.isFinish()) {
                    actDcDutyVisitPlanListSrl.finishLoadMoreWithNoMoreData();
                }
                if (LibCollections.isEmpty(dcDutyVisitPlantItems)) {
                    dcDutyVisitPlantListAdapter.setNewData(null);
                    dcDutyVisitPlantListAdapter.setEmptyView(createEmptyListView());
                } else {
                    dcDutyVisitPlantListAdapter.setNewData(dcDutyVisitPlantItems);
                }
            }

            @Override
            public void onFinish() {
                refreshLayout.finishRefresh();
            }
        });
    }

    private void initListener() {
        actDcDutyVisitPlanListAab.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_CHOOSE_VISIT_PLANT) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
