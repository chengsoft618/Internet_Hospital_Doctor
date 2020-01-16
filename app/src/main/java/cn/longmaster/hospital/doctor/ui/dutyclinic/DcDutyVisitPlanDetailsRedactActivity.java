package cn.longmaster.hospital.doctor.ui.dutyclinic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempDetailItem;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDutyVisitPlantTempItem;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDutyVisitPlanDetailsIsRedactAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.dialog.DCDutyVisitPlanDetailsRemovePointDialog;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.TimePickUtils;
import cn.longmaster.utils.TimeUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;

/**
 * @author: wangyang
 * @description: 随访计划详情编辑页面
 * @date: 2019-12-30 20:42:01
 */
public class DcDutyVisitPlanDetailsRedactActivity extends NewBaseActivity {
    private final int VISIT_PLAN_INFO_TIP_STATE_NOT_REMIND = 0;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_redact_root)
    private LinearLayout actDcDutyVisitPlanDetailsRedactRoot;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_redact_aab)
    private AppActionBar actDcDutyVisitPlantDetailsAab;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_redact_name_ll)
    private LinearLayout actDcDutyVisitPlantDetailsRedactNamell;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_redact_name)
    private TextView actDcDutyVisitPlantDetailsRedactName;
    @FindViewById(R.id.act_dc_duty_visit_plant_details_redact_rv)
    private RecyclerView actDcDutyVisitPlantDetailsRedactRv;
    @AppApplication.Manager
    private DCManager dcManager;
    private int mMedicalId;
    private int mplantId;
    /**
     * 没有删除节点时传空字符串，当删除多个节点时用英文逗号隔开
     */
    private String delListIds = "";
    private String mHospitalName;
    private DCDutyVisitPlanDetailsIsRedactAdapter dcDutyVisitPlanDetailsIsRedactAdapter;
    private List<DCDutyVisitPlantTempItem> notRemindTempItems;
    private List<DCDutyVisitPlantTempItem> updateRemindTempItems;
    /**
     * 标记是否选择了时间
     */
    private boolean isChooseTime;
    /**
     * 同一个节点位置
     */
    private int mPosition;
    private String mChoseTime;
    private String sendMessageContent;
    private LinearLayout messageRemindParentll;
    private String[] tempPlanType = new String[]{"复诊提醒", "用药提醒", "资料上传提醒"};
    private boolean isEmptyList;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mMedicalId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MEDICAL_ID, 0);
        mHospitalName = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DC_DUTY_PATIENT_VISIT_PLAN_REDACT_HOSPITAL_NAME);
    }

    @Override
    protected void initDatas() {
        dcDutyVisitPlanDetailsIsRedactAdapter = new DCDutyVisitPlanDetailsIsRedactAdapter(new ArrayList<>(0));
        dcDutyVisitPlanDetailsIsRedactAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                DCDutyVisitPlantTempItem adapterItem = (DCDutyVisitPlantTempItem) adapter.getItem(position);
                mPosition = position;
                if (adapterItem != null) {
                    switch (view.getId()) {
                        case R.id.item_dc_duty_visit_plan_details_adapter_top_diagnostic_circular:
                            new CommonDialog.Builder(getThisActivity())
                                    .setTitle("确定删除该随访计划节点")
                                    .setCancelable(false)
                                    .setPositiveButton("取消", () -> {
                                    })
                                    .setNegativeButton("确定", () -> {
                                        adapter.remove(position);
                                        StringBuffer param = new StringBuffer();
                                        String delListId = StringUtils.integer2Str(adapterItem.getListId());
                                        if (adapterItem.getListId() != 0) {
                                            if (StringUtils.isEmpty(delListIds)){
                                                delListIds = delListId;
                                            }else {
                                                param.append(delListIds);
                                                param.append(",");
                                                param.append(delListId);
                                                delListIds = param.toString();
                                            }
                                        }
                                    })
                                    .show();
                            break;
                        case R.id.item_dc_duty_visit_plan_details_adpter_date_st:
                            SuperTextView superTextView = (SuperTextView) adapter.getViewByPosition(actDcDutyVisitPlantDetailsRedactRv, position, R.id.item_dc_duty_visit_plan_details_adpter_date_st);
                            showDateChoseDialog(superTextView);
                            break;
                        case R.id.item_dc_duty_visit_plan_details_adapter_add_iv:
                            messageRemindParentll = (LinearLayout) adapter.getViewByPosition(actDcDutyVisitPlantDetailsRedactRv, position, R.id.item_dc_duty_visit_plan_details_adapter_message_reminding_parent);
                            if (isChooseTime) {
                                showChoseSendMessage(adapter, position);
                                if (mPosition == position) {
                                    isChooseTime = true;
                                } else {
                                    isChooseTime = false;
                                }
                            } else {
                                ToastUtils.showShort("请选择提醒时间");
                            }
                            break;
                        case R.id.item_dc_duty_visit_plan_details_adapter_add_redact_layout_addtv://添加随访计划
                            setUpdateVisitPlanPoitnt(adapter, adapterItem.getTempId(), position);
                            Logger.logI(Logger.COMMON, "onItemChildClick: 添加随访计划 position =" + position);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        dcDutyVisitPlanDetailsIsRedactAdapter.setOndeletedChildViewClickListener(new DCDutyVisitPlanDetailsIsRedactAdapter.OndeletedChildViewClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DCDutyVisitPlantTempDetailItem item = (DCDutyVisitPlantTempDetailItem) adapter.getItem(position);
                if (item != null) {
                    switch (view.getId()) {
                        case R.id.item_dc_duty_paient_disease_redact_deleted:
                            adapter.remove(position);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_dc_duty_visit_plan_details_redact;
    }

    @Override
    protected void initViews() {
        actDcDutyVisitPlantDetailsRedactRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(getThisActivity()));
        actDcDutyVisitPlantDetailsRedactRv.setAdapter(dcDutyVisitPlanDetailsIsRedactAdapter);
        getPatientVisitPlanInfo();
        initLisntener();
    }

    private void initLisntener() {
        actDcDutyVisitPlantDetailsAab.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//保存
                updateRemindTempItems = new ArrayList<>();
                if (LibCollections.isNotEmpty(dcDutyVisitPlanDetailsIsRedactAdapter.getData())) {
                    for (DCDutyVisitPlantTempItem tempItem : dcDutyVisitPlanDetailsIsRedactAdapter.getData()) {
                        if (tempItem.getItemType() == DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_ADD_LAYOUT) {
                            dcDutyVisitPlanDetailsIsRedactAdapter.getData().remove(tempItem);
                        }
                    }
                    updateRemindTempItems.addAll(dcDutyVisitPlanDetailsIsRedactAdapter.getData());
                    getUpdateVisitPlan(mMedicalId, mplantId, delListIds, updateRemindTempItems);
                }
            }
        });
    }

    /**
     * 添加随访计划节点
     *
     * @param adapter
     */
    private void setUpdateVisitPlanPoitnt(BaseQuickAdapter adapter, int tempId, int position) {
        List<DCDutyVisitPlantTempItem> newTempItemData = adapter.getData();
        DCDutyVisitPlantTempItem item = new DCDutyVisitPlantTempItem();
        item.setListId(0);
        item.setTempId(tempId);
        item.setListDt(TimeUtils.millis2String(TimeUtils.getNowMills(), "yyyy-MM-dd HH:ss"));
        item.setFollowupTempDetailList(null);
        item.setItemType(DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_LAYOUT);
        newTempItemData.add(position, item);
        adapter.setNewData(newTempItemData);
    }

    /**
     * 添加随访计划节点详情
     *
     * @param adapter
     * @param position
     * @param listDt
     * @param tempMessage
     */
    private void setUpdateVisitPlanDetailsData(BaseQuickAdapter adapter, int position, String listDt, int tempPlanType, String tempMessage) {
        List<DCDutyVisitPlantTempItem> addTempItemData = adapter.getData();
        DCDutyVisitPlantTempItem adapterItem = addTempItemData.get(position);
        adapterItem.setListDt(TimeUtils.string2String(listDt, "yyyy-MM-dd", "yyyy-MM-dd HH:ss"));
        List<DCDutyVisitPlantTempDetailItem> detailItemlList = adapterItem.getFollowupTempDetailList();
        DCDutyVisitPlantTempDetailItem detailItem = new DCDutyVisitPlantTempDetailItem();
        if (LibCollections.isEmpty(detailItemlList)) {
            detailItemlList = new ArrayList<>();
            detailItem.setId(0);
            detailItem.setTempId(adapterItem.getTempId());
            detailItem.setListId(adapterItem.getListId());
            detailItem.setType(tempPlanType);
            detailItem.setContent(tempMessage);
            detailItemlList.add(detailItem);
        } else if (LibCollections.isNotEmpty(detailItemlList) && adapterItem.getListId() == 0) {
            detailItem.setId(0);
            detailItem.setTempId(adapterItem.getTempId());
            detailItem.setListId(adapterItem.getListId());
            detailItem.setType(tempPlanType);
            detailItem.setContent(tempMessage);
            detailItemlList.add(detailItem);
        } else {
            detailItem.setId(LibCollections.size(detailItemlList) - 1);
            detailItem.setTempId(adapterItem.getTempId());
            detailItem.setListId(adapterItem.getListId());
            detailItem.setType(tempPlanType);
            detailItem.setContent(tempMessage);
            detailItemlList.add(detailItem);
        }
        adapterItem.setFollowupTempDetailList(detailItemlList);
        adapter.setNewData(addTempItemData);
    }

    private int getTempPlanType(String sendMessageContent) {
        for (int i = 1; i <= tempPlanType.length; i++) {
            if (StringUtils.equals(sendMessageContent, tempPlanType[i - 1])) {
                return i;
            }
        }
        return 1;
    }

    /**
     * @param superTextView 随访计划时间选择
     */
    private void showDateChoseDialog(SuperTextView superTextView) {
        TimePickUtils.showBottomPickTime(getThisActivity(), new TimePickUtils.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                superTextView.setCenterString(TimeUtils.date2String(date, "yyyy年MM月dd日"));
                mChoseTime = TimeUtils.date2String(date, "yyyy-MM-dd");
                isChooseTime = true;
            }
        });
    }

    /**
     * 当选择了时间后，选择发送的消息内容
     */
    private void showChoseSendMessage(BaseQuickAdapter adapter, int position) {
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_message_reminding_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(this, R.style.custom_noActionbar_window_style).create();
        dialog.show();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        Window win = dialog.getWindow();
        if (null == win) {
            return;
        }
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        TextView mSubvisitMessageTv = layout.findViewById(R.id.layout_subvisit_message_reminding_dialog);
        TextView mMedicationsRemindingTv = layout.findViewById(R.id.layout_medications_reminding_dialog);
        TextView mDataUploadTv = layout.findViewById(R.id.layout_data_uploading_reminding_dialog);
        TextView mCancelTv = layout.findViewById(R.id.layout_cancel_message_reminding_dialog);
        mSubvisitMessageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageContent = mSubvisitMessageTv.getText().toString().trim();
                int tempPlanType = getTempPlanType(sendMessageContent);
                String tempMessage = "请于" + mChoseTime + "到" + mHospitalName + "复诊";
                setUpdateVisitPlanDetailsData(adapter, position, mChoseTime, tempPlanType, tempMessage);
                dialog.dismiss();
            }
        });
        mMedicationsRemindingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageContent = mMedicationsRemindingTv.getText().toString().trim();
                int tempPlanType = getTempPlanType(sendMessageContent);
                String tempMessage = "请遵医嘱按时按量服药";
                setUpdateVisitPlanDetailsData(adapter, position, mChoseTime, tempPlanType, tempMessage);
                dialog.dismiss();
            }
        });
        mDataUploadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageContent = mDataUploadTv.getText().toString().trim();
                int tempPlanType = getTempPlanType(sendMessageContent);
                String tempMessage = "请将病历资料进行上传";
                setUpdateVisitPlanDetailsData(adapter, position, mChoseTime, tempPlanType, tempMessage);
                dialog.dismiss();
            }
        });
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void getPatientVisitPlanInfo() {
        dcManager.getVisitPlantListByMedical(mMedicalId, new DefaultResultCallback<List<DCDutyVisitPlantTempItem>>() {
            @Override
            public void onSuccess(List<DCDutyVisitPlantTempItem> dcDutyVisitPlantTempItems, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(dcDutyVisitPlantTempItems)) {
                    notRemindTempItems = new ArrayList<>();
                    actDcDutyVisitPlantDetailsRedactNamell.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsRedactName.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsRedactRv.setVisibility(View.VISIBLE);
                    actDcDutyVisitPlantDetailsRedactName.setText(dcDutyVisitPlantTempItems.get(0).getTempName());
                    mplantId = dcDutyVisitPlantTempItems.get(0).getTempId();
                    for (DCDutyVisitPlantTempItem item : dcDutyVisitPlantTempItems) {
                        setDcDutyPatientVisitPlanRvData(item);
                    }
                    DCDutyVisitPlantTempItem item = new DCDutyVisitPlantTempItem();
                    item.setItemType(DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_ADD_LAYOUT);
                    notRemindTempItems.add(item);
                    dcDutyVisitPlanDetailsIsRedactAdapter.addData(notRemindTempItems);
                }
            }
        });
    }

    private void setDcDutyPatientVisitPlanRvData(DCDutyVisitPlantTempItem item) {
        if (item.getTipState() == VISIT_PLAN_INFO_TIP_STATE_NOT_REMIND) {
            item.setItemType(DCDutyVisitPlantTempItem.ITEM_DC_DUTY_VISIT_PLAN_DETAILS_ADAPTER_REDACT_LAYOUT);
            notRemindTempItems.add(item);
        }
    }

    /**
     * gen更新随访计划
     *
     * @param mMedicalId
     * @param mPlantId
     * @param followupTempListBeanList
     */
    private void getUpdateVisitPlan(int mMedicalId, int mPlantId, String delListIds, List<DCDutyVisitPlantTempItem> followupTempListBeanList) {
        ProgressDialog mProgressDialog = createProgressDialog(getString(R.string.loading));
        mProgressDialog.show();
        dcManager.updateVisitPlants(mMedicalId, mPlantId, delListIds, followupTempListBeanList, new DefaultResultCallback<Void>() {
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