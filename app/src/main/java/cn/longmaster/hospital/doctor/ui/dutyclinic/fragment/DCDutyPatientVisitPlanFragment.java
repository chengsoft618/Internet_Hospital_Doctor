package cn.longmaster.hospital.doctor.ui.dutyclinic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlanIsRemindDetailsItemData;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempDetailItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyVisitPlanIsRemindAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyVisitPlanNotRemindAdapter;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/17 20:18
 * @description: 患者随访计划
 */
public class DCDutyPatientVisitPlanFragment extends NewBaseFragment {
    private final int VISIT_PLAN_INFO_TIP_STATE_NOT_REMIND = 0;
    private final int VISIT_PLAN_INFO_TIP_STATE_IS_REMIND = 1;
    private final int REQUEST_CODE_FOR_VISIT_PLANT_DETAILS_LIST = 101;
    private final int REQUEST_CODE_FOR_VISIT_PLANT_DETAILS_REDACT = 103;

    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_add)
    private SuperTextView fgDcDutyPpatientVisitPlanAdd;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_name_ll)
    private LinearLayout fgDcDutyPpatientvisitPlanNamell;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_name_tv)
    private TextView fgDcDutyPpatientvisitPlanNameTv;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_is_remind_ll)
    private LinearLayout fgDcDutyDutyPatientVisitPlanIsRemindll;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_is_remind_tv)
    private TextView fgDcDutyPpatientvisitPlanIsRemindTv;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_is_remind_rv)
    private RecyclerView fgDcDutyPpatientvisitPlanIsRemindRv;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_divide_view)
    private View fgDcDutyPpatientVisitPlanDivideView;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_not_remind_ll)
    private LinearLayout fgDcDutyDutyPatientVisitPlanNotRemindll;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_not_remind_tv)
    private TextView fgDcDutyPpatientvisitPlanNotRemindTv;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_not_remind_rv)
    private RecyclerView fgDcDutyPpatientvisitPlanNotRemindRv;
    @FindViewById(R.id.fg_dc_duty_patient_visit_plan_redact)
    private TextView fgDcDutyPpatientvisitPlanNotRedact;
    private View isRemindRv;
    @AppApplication.Manager
    private DCManager dcManager;
    /**
     * 病例ID
     */
    private int mMedicalId;
    /**
     * 项目ID
     */
    private int mProjectId;
    /**
     * 患者所属社区卫生医院
     */
    private String mHospitalName;
    private DCDutyVisitPlanNotRemindAdapter dcDutyVisitPlanNotRemindAdapter;
    private DCDutyVisitPlanIsRemindAdapter dcDutyVisitPlanIsRemindAdapter;

    @Override
    protected void initDatas() {
        super.initDatas();
        dcDutyVisitPlanIsRemindAdapter = new DCDutyVisitPlanIsRemindAdapter(R.layout.item_dc_duty_visit_plan_is_redmin_adapter_layout, new ArrayList<>(0));
        dcDutyVisitPlanNotRemindAdapter = new DCDutyVisitPlanNotRemindAdapter(R.layout.item_dc_duty_visit_plan_not_redmin_adapter_layout, new ArrayList<>(0));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fg_dc_duty_patient_visit_plan;
    }

    @Override
    public void initViews(View rootView) {
        fgDcDutyPpatientvisitPlanIsRemindRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getActivity()));
        fgDcDutyPpatientvisitPlanIsRemindRv.setAdapter(dcDutyVisitPlanIsRemindAdapter);
        fgDcDutyPpatientvisitPlanNotRemindRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getActivity()));
        fgDcDutyPpatientvisitPlanNotRemindRv.setAdapter(dcDutyVisitPlanNotRemindAdapter);
//        getPatientVisitPlanInfo(mMedicalId);
        initListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            refresh(mMedicalId,mProjectId,mHospitalName);
        }
    }

    private void initListener() {
        fgDcDutyPpatientVisitPlanAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//添加随访计划患者
                getDisplay().startDcDutyVisitPlanListActivity(mProjectId, mMedicalId, REQUEST_CODE_FOR_VISIT_PLANT_DETAILS_LIST);
            }
        });
        fgDcDutyPpatientvisitPlanNotRedact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//编辑随访计划
                getDisplay().startDcDutyVisitPlanDetailsRedactActivity(mMedicalId, mHospitalName, REQUEST_CODE_FOR_VISIT_PLANT_DETAILS_REDACT);
            }
        });
    }

    /**
     * @param medicalId 获取患者随访计划详情
     */
    private void getPatientVisitPlanInfo(int medicalId) {
        if (null != dcManager && medicalId != 0) {
            dcManager.getVisitPlantListByMedical(medicalId, new DefaultResultCallback<List<DCDutyVisitPlantTempItem>>() {
                @Override
                public void onSuccess(List<DCDutyVisitPlantTempItem> dcDutyVisitPlantTempItems, BaseResult baseResult) {
                    if (LibCollections.isNotEmpty(dcDutyVisitPlantTempItems)) {
                        fgDcDutyPpatientVisitPlanAdd.setVisibility(View.GONE);
                        fgDcDutyPpatientvisitPlanNamell.setVisibility(View.VISIBLE);
                        fgDcDutyPpatientvisitPlanNameTv.setText(dcDutyVisitPlantTempItems.get(0).getTempName());
                        setDcDutyPatientVisitPlanRvData(dcDutyVisitPlantTempItems);
                    } else {
                        fgDcDutyPpatientVisitPlanAdd.setVisibility(View.VISIBLE);
                        fgDcDutyPpatientvisitPlanNamell.setVisibility(View.GONE);
                        fgDcDutyDutyPatientVisitPlanIsRemindll.setVisibility(View.GONE);
                        fgDcDutyPpatientVisitPlanDivideView.setVisibility(View.GONE);
                        fgDcDutyDutyPatientVisitPlanNotRemindll.setVisibility(View.GONE);
                        dcDutyVisitPlanIsRemindAdapter.setNewData(null);
                    }
                }
            });
        }
    }

    /**
     * @param itemList 随访计划：已提醒：1 未提醒 ：0
     */
    private void setDcDutyPatientVisitPlanRvData(List<DCDutyVisitPlantTempItem> itemList) {
        List<DCDutyVisitPlantTempItem> notRemindTempItems = new ArrayList<>();
        List<DCDutyVisitPlanIsRemindDetailsItemData> dataDetailsItem = new ArrayList<>();
        for (DCDutyVisitPlantTempItem item : itemList) {
            if (item.getTipState() == VISIT_PLAN_INFO_TIP_STATE_NOT_REMIND) {
                fgDcDutyDutyPatientVisitPlanNotRemindll.setVisibility(View.VISIBLE);
                fgDcDutyPpatientvisitPlanNotRedact.setVisibility(View.VISIBLE);
                notRemindTempItems.add(item);
            } else {
                fgDcDutyPpatientvisitPlanNotRedact.setVisibility(View.VISIBLE);
                if (LibCollections.isNotEmpty(item.getFollowupTempDetailList())) {
                    fgDcDutyDutyPatientVisitPlanIsRemindll.setVisibility(View.VISIBLE);
                    for (DCDutyVisitPlantTempDetailItem tempDetailItem : item.getFollowupTempDetailList()) {
                        dataDetailsItem.add(new DCDutyVisitPlanIsRemindDetailsItemData(tempDetailItem.getListId(), tempDetailItem.getInsertDt(), tempDetailItem.getContent()));
                    }
                }
            }
        }
        if (LibCollections.isNotEmpty(notRemindTempItems) || LibCollections.isNotEmpty(dataDetailsItem)) {
            dcDutyVisitPlanNotRemindAdapter.setNewData(notRemindTempItems);
            dcDutyVisitPlanIsRemindAdapter.setNewData(dataDetailsItem);
        }
        if (LibCollections.isNotEmpty(notRemindTempItems) && LibCollections.isNotEmpty(dataDetailsItem)) {
            fgDcDutyPpatientVisitPlanDivideView.setVisibility(View.VISIBLE);
        }
    }

    public void refresh(int medicalId, int projectId,String hospitalName) {
        mMedicalId = medicalId;
        mProjectId = projectId;
        mHospitalName = hospitalName;
        getPatientVisitPlanInfo(mMedicalId);
    }
}
