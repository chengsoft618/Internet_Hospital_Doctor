package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempDetailItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyVisitPlanDetailsNotRedactAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.TimeUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/18 15:49
 * @description: 随访计划详情
 */
public class DcDutyVisitPlanDetailsActivity extends NewBaseActivity {
    @FindViewById(R.id.act_dc_duty_visit_plan_details_root)
    private LinearLayout actDcDutyVisitPlanDetailsRoot;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_aab)
    private AppActionBar actDcDutyVisitPlantDetailsAab;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_name_ll)
    private LinearLayout actDcDutyVisitPlantDetailsNamell;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_name)
    private TextView actDcDutyVisitPlantDetailsName;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_rv)
    private RecyclerView actDcDutyVisitPlantDetailsRv;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_selected)
    private TextView actDcDutyVisitPlantDetailsSelected;

    private int mMedicalId;
    /**
     * 所选随访计划ID
     */
    private int mPlantId;
    private String delListIds = "";
    @AppApplication.Manager
    DCManager dcManager;
    private DCDutyVisitPlanDetailsNotRedactAdapter dcDutyVisitPlanDetailsNotRedactAdapter;
    private List<DCDutyVisitPlantTempItem> followupTempListBeanList = new ArrayList<>();
    private List<DCDutyVisitPlantTempItem> updateFollowupTempList = new ArrayList<>();

    @Override
    protected void initDatas() {
        mMedicalId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
        mPlantId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_VISIT_PLANT_ID, 0);
        dcDutyVisitPlanDetailsNotRedactAdapter = new DCDutyVisitPlanDetailsNotRedactAdapter(R.layout.item_dc_duty_visit_plan_details_adapter_layout, new ArrayList<>(0));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_visit_plant_details;
    }

    @Override
    protected void initViews() {
        actDcDutyVisitPlantDetailsRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyVisitPlantDetailsRv.setHasFixedSize(true);
        actDcDutyVisitPlantDetailsRv.setAdapter(dcDutyVisitPlanDetailsNotRedactAdapter);
        getVisitPlantDetails(mPlantId);
        initListener();
    }

    private void initListener() {
        actDcDutyVisitPlantDetailsAab.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        actDcDutyVisitPlantDetailsSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LibCollections.isNotEmpty(followupTempListBeanList)) {
                    for (DCDutyVisitPlantTempItem item : followupTempListBeanList) {
                        item.setListId(0);
                        item.setListDt(TimeUtils.millis2String(TimeUtils.getNowMills(), "yyyy-MM-dd HH:ss"));
                        List<DCDutyVisitPlantTempDetailItem> followupTempDetailList = item.getFollowupTempDetailList();
                        for (DCDutyVisitPlantTempDetailItem detailItem : followupTempDetailList) {
                            detailItem.setId(0);
                            detailItem.setListId(0);
                        }
                    }
                    getUpdateVisitPlan(mMedicalId, mPlantId,delListIds, followupTempListBeanList);
                }
            }
        });
    }

    private void getVisitPlantDetails(int mPlantId) {
        followupTempListBeanList.clear();
        dcManager.getVisitPlantInfo(mPlantId, new DefaultResultCallback<DCDutyVisitPlantItem>() {
            @Override
            public void onSuccess(DCDutyVisitPlantItem dcDutyVisitPlantItem, BaseResult baseResult) {
                if (dcDutyVisitPlantItem != null) {
                    actDcDutyVisitPlantDetailsNamell.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsName.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsRv.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsName.setText(dcDutyVisitPlantItem.getTempName());
                    followupTempListBeanList = dcDutyVisitPlantItem.getFollowupTempList();
                    dcDutyVisitPlanDetailsNotRedactAdapter.setNewData(followupTempListBeanList);
                }
            }
        });
    }

    private void getUpdateVisitPlan(int mMedicalId, int mPlantId, String delListIds, List<DCDutyVisitPlantTempItem> followupTempListBeanList) {
        ProgressDialog mProgressDialog = createProgressDialog(getString(R.string.loading));
        mProgressDialog.show();
        dcManager.updateVisitPlants(mMedicalId, mPlantId, delListIds,followupTempListBeanList, new DefaultResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, BaseResult baseResult) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
    }

}
