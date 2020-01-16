package cn.longmaster.hospital.doctor.ui.dutyclinic.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.screen.ScreenUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugUsageDosageInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCInspectInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCReferralDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCSuggestionDetailInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCCustomDrugAdapter;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCDrugUsageListAdapter;
import cn.longmaster.hospital.doctor.util.ListUtils;
import cn.longmaster.hospital.doctor.view.DCIssueOrderInputItem;
import cn.longmaster.hospital.doctor.view.DCIssueOrderTitleAndValueItem;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.PhoneUtil;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 出具医嘱对话框
 * Created by yangyong on 2019-12-01.
 */
public class IssueReferralAndSuggestionDialog extends Dialog {
    public static final int ISSUE_TYPE_REFERRAL = 0;
    public static final int ISSUE_TYPE_SUGGESTION = 1;
    @FindViewById(R.id.layout_dcissue_order_dialog_type_rg)
    private RadioGroup radioGroup;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_ll)
    private LinearLayout referralLL;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_name_item)
    private DCIssueOrderInputItem referralNameItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_phonenum_item)
    private DCIssueOrderInputItem referralPhoneNumItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_cardno_item)
    private DCIssueOrderInputItem referralCardNoItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_curedt_tv)
    private TextView referralCureDtTv;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_sender_item)
    private DCIssueOrderTitleAndValueItem referralSendItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_receiver_item)
    private DCIssueOrderTitleAndValueItem referralReceiverItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_send_reason_et)
    private EditText referralSendReasonEt;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_checks_fl)
    private FlexboxLayout referralChecksFl;
    @FindViewById(R.id.layout_dcissue_order_dialog_referral_check_item)
    private EditText referralCheckInputEt;

    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_ll)
    private LinearLayout suggestionLl;

    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_name_item)
    private DCIssueOrderInputItem suggestionNameItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_phonenum_item)
    private DCIssueOrderInputItem suggestionPhoneNumItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_cardno_item)
    private DCIssueOrderInputItem suggestionCardNoItem;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_curedt_tv)
    private TextView suggestionCureDtTv;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_diagnose_et)
    private EditText suggestionDiagnoseEt;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_remark_et)
    private EditText suggestionRemarkEt;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_check_items_fl)
    private FlexboxLayout suggestionCheckItemsFl;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_add_check_item_et)
    private EditText suggestionCheckItemEt;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_drug_fl)
    private FlexboxLayout drugFl;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_add_drug_et)
    private EditText addDrugEt;
    @FindViewById(R.id.layout_dcissue_order_dialog_suggestion_drug_usage_dosage_rv)
    private RecyclerView drugUsageDosageRv;

    private DCManager dcManager;
    private OnIssueReferralAndSuggestionDialogStateChangeListener listener;
    private Context context;
    private List<DCInspectInfo> defaultDCInspectInfos = new ArrayList<>();
    private List<DCInspectInfo> referralInspectInfos = new ArrayList<>();
    private List<DCInspectInfo> suggestionInspectInfos = new ArrayList<>();
    private List<DCDrugInfo> defaultDrugInfos = new ArrayList<>();
    private List<DCDrugInfo> drugInfos = new ArrayList<>();
    private DCOrderDetailInfo orderDetailInfo;
    private boolean isReferral = true;
    private int issueType;
    private int sendHospitalId, receiveHospitalId;
    private DCReferralDetailInfo referralDetailInfo;
    private DCSuggestionDetailInfo suggestionDetailInfo;
    private DCDrugUsageListAdapter cycleAdapter;
    private DCDrugUsageListAdapter cycleNumAdapter;
    private DCDrugUsageListAdapter doseAdapter;
    private DCDrugUsageListAdapter doseUnAdapter;
    private DCMedicalInfo currentMedicalInfo;

    private DCCustomDrugAdapter drugAdapter;

    private List<DCDrugUsageDosageInfo> cycleDrugUsageDosageInfos = new ArrayList<>();
    private List<DCDrugUsageDosageInfo> cycleNumDrugUsageDosageInfos = new ArrayList<>();
    private List<DCDrugUsageDosageInfo> doseDrugUsageDosageInfos = new ArrayList<>();
    private List<DCDrugUsageDosageInfo> doseUnDrugUsageDosageInfos = new ArrayList<>();

    private List<DCDrugInfo> selectedDrugInfos = new ArrayList<>();

    private List<String> cycleList = Arrays.asList("一天", "二天", "三天", "四天", "五天", "睡前", "饭前", "饭后", "临睡前");
    private List<String> cycleNumList = Arrays.asList("一次", "二次", "三次", "四次", "五次", "六次", "七次", "八次");
    private List<String> doseList = Arrays.asList("0.25", "0.5", "0.75", "1", "2", "3", "4", "5", "6");
    private List<String> doseUnitList = Arrays.asList("g", "mg", "ml");

    // private DCDrugInfo currentSelectedDCDrugInfo;

    public IssueReferralAndSuggestionDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public void display(int type, DCOrderDetailInfo orderDetailInfo, OnIssueReferralAndSuggestionDialogStateChangeListener listener) {
        this.orderDetailInfo = orderDetailInfo;
        this.issueType = type;
        this.listener = listener;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
        setContentView(R.layout.layout_dcissue_order_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        initData();
        initCheckListener();
        initView();
        getDefaultInspects();
    }

    @OnClick({R.id.layout_dcissue_order_dialog_new_tv,
            R.id.layout_dcissue_order_dialog_close_iv,
            R.id.layout_dcissue_order_dialog_referral_add_check_btn,
            R.id.layout_dcissue_order_dialog_referral_submit_btn,
            R.id.layout_dcissue_order_dialog_suggestion_submit_btn,
            R.id.layout_dcissue_order_dialog_suggestion_add_check_item_btn,
            R.id.layout_dcissue_order_dialog_referral_curedt_ll,
            R.id.layout_dcissue_order_dialog_suggestion_curedt_ll,
            R.id.layout_dcissue_order_dialog_referral_change_sr_iv,
            R.id.layout_dcissue_order_dialog_suggestion_add_drug_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dcissue_order_dialog_new_tv:
                clearInputs();
                break;

            case R.id.layout_dcissue_order_dialog_close_iv:
                this.dismiss();
                break;

            case R.id.layout_dcissue_order_dialog_referral_add_check_btn:
                String checkStr = referralCheckInputEt.getText().toString().trim();
                if (StringUtils.isEmpty(checkStr)) {
                    ToastUtils.showShort("检查检验不能为空");
                    return;
                }
                referralCheckInputEt.setText("");
                for (DCInspectInfo info : referralInspectInfos) {
                    if (info.getInspect().equals(checkStr)) {
                        ToastUtils.showShort("该检查检验已经存在，请勿重复添加");
                        return;
                    }
                }
                DCInspectInfo info = new DCInspectInfo();
                info.setInspect(checkStr);
                info.setChecked(true);
                referralInspectInfos.add(info);
                addInspect(info, true);
                break;

            case R.id.layout_dcissue_order_dialog_suggestion_add_check_item_btn:
                String suggestionInspectStr = suggestionCheckItemEt.getText().toString().trim();
                if (StringUtils.isEmpty(suggestionInspectStr)) {
                    ToastUtils.showShort("检查检验不能为空");
                    return;
                }
                suggestionCheckItemEt.setText("");
                for (DCInspectInfo suggestionInspectInfo : suggestionInspectInfos) {
                    if (suggestionInspectInfo.getInspect().equals(suggestionInspectStr)) {
                        ToastUtils.showShort("该检查检验已经存在，请勿重复添加");
                        return;
                    }
                }
                DCInspectInfo suggestionInspectInfo = new DCInspectInfo();
                suggestionInspectInfo.setInspect(suggestionInspectStr);
                suggestionInspectInfo.setChecked(true);
                suggestionInspectInfos.add(suggestionInspectInfo);
                addInspect(suggestionInspectInfo, false);
                break;

            case R.id.layout_dcissue_order_dialog_referral_submit_btn:
                submitReferral();
                break;

            case R.id.layout_dcissue_order_dialog_suggestion_submit_btn:
                submitSuggestion();
                break;

            case R.id.layout_dcissue_order_dialog_referral_curedt_ll:
                showReferralDatePickDialog();
                break;

            case R.id.layout_dcissue_order_dialog_suggestion_curedt_ll:
                showSuggestionDatePickDialog();
                break;

            case R.id.layout_dcissue_order_dialog_referral_change_sr_iv:
                int tempId = sendHospitalId;
                String tempName = referralSendItem.getValue();
                referralSendItem.setValue(referralReceiverItem.getValue());
                sendHospitalId = receiveHospitalId;
                referralReceiverItem.setValue(tempName);
                receiveHospitalId = tempId;
                break;

            case R.id.layout_dcissue_order_dialog_suggestion_add_drug_btn:
                String drugNameStr = addDrugEt.getText().toString().trim();
                if (StringUtils.isEmpty(drugNameStr)) {
                    ToastUtils.showShort("药品名称不能为空");
                    return;
                }
                addDrugEt.setText("");
                for (DCDrugInfo drugInfo : drugInfos) {
                    if (drugInfo.getDrugName().equals(drugNameStr)) {
                        ToastUtils.showShort("该药品已经存在，请勿重复添加");
                        return;
                    }
                }
                DCDrugInfo drugInfo = new DCDrugInfo();
                drugInfo.setDrugName(drugNameStr);
                drugInfo.setCycle("一天");
                drugInfo.setCycleNum("三次");
                drugInfo.setDose("2");
                drugInfo.setDoseUnit("mg");
                drugInfo.setChecked(true);
                drugInfos.add(drugInfo);
                addDrugItem(drugInfo);

                selectedDrugInfos.add(drugInfo);
                drugAdapter.notifyDataSetChanged();
                break;

            default:
                break;
        }
    }

    private void initData() {
        for (int i = 0; i < cycleList.size(); i++) {
            DCDrugUsageDosageInfo info = new DCDrugUsageDosageInfo(cycleList.get(i), false);
            cycleDrugUsageDosageInfos.add(info);
        }
        for (int i = 0; i < cycleNumList.size(); i++) {
            DCDrugUsageDosageInfo info = new DCDrugUsageDosageInfo(cycleNumList.get(i), false);
            cycleNumDrugUsageDosageInfos.add(info);
        }
        for (int i = 0; i < doseList.size(); i++) {
            DCDrugUsageDosageInfo info = new DCDrugUsageDosageInfo(doseList.get(i), false);
            doseDrugUsageDosageInfos.add(info);
        }
        for (int i = 0; i < doseUnitList.size(); i++) {
            DCDrugUsageDosageInfo info = new DCDrugUsageDosageInfo(doseUnitList.get(i), false);
            doseUnDrugUsageDosageInfos.add(info);
        }
    }

    private void initView() {
        sendHospitalId = orderDetailInfo.getLaunchHospitalId();
        receiveHospitalId = orderDetailInfo.getDoctorHospitalId();
        referralPhoneNumItem.setInputType(InputType.TYPE_CLASS_PHONE);
        referralPhoneNumItem.setMaxInput(11);
        suggestionPhoneNumItem.setInputType(InputType.TYPE_CLASS_PHONE);
        suggestionPhoneNumItem.setMaxInput(11);
        referralSendItem.setValue(orderDetailInfo.getLaunchHospitalName());
        referralReceiverItem.setValue(orderDetailInfo.getDoctorHospitalName());
        if (issueType == ISSUE_TYPE_REFERRAL) {
            radioGroup.check(R.id.layout_dcissue_order_dialog_type_referral_rb);
        } else {
            radioGroup.check(R.id.layout_dcissue_order_dialog_type_suggestion_rb);
        }
        suggestionLl.setVisibility(View.GONE);
        referralLL.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        drugUsageDosageRv.setLayoutManager(linearLayoutManager);
        drugUsageDosageRv.setNestedScrollingEnabled(false);
        drugAdapter = new DCCustomDrugAdapter(R.layout.item_dcissue_order_custom_drug, selectedDrugInfos);
        drugAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DCDrugInfo currentSelectedDCDrugInfo = (DCDrugInfo) adapter.getItem(position);
            switch (view.getId()) {
                case R.id.item_dcissue_order_custom_drug_drug_usage_ll:
                    showChoiceUsageWindow(currentSelectedDCDrugInfo);
                    break;
                case R.id.item_dcissue_order_custom_drug_drug_dosage_ll:
                    showChoiceDosageWindow(currentSelectedDCDrugInfo);
                    break;
                default:
                    break;
            }
        });
        drugUsageDosageRv.setAdapter(drugAdapter);
    }

    private void initCheckListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.layout_dcissue_order_dialog_type_referral_rb) {
                    suggestionLl.setVisibility(View.GONE);
                    referralLL.setVisibility(View.VISIBLE);
                    isReferral = true;
                } else {
                    referralLL.setVisibility(View.GONE);
                    suggestionLl.setVisibility(View.VISIBLE);
                    isReferral = false;
                }
            }
        });
    }

    private void getMedicalList(int orderId) {
        dcManager.getDCRoomMedicalList(orderId, new DefaultResultCallback<List<DCMedicalInfo>>() {
            @Override
            public void onSuccess(List<DCMedicalInfo> dcMedicalInfos, BaseResult baseResult) {
                if (dcMedicalInfos != null && dcMedicalInfos.size() > 0) {
                    ListUtils.sort(dcMedicalInfos, false, "insert_dt");
                    getReferral(dcMedicalInfos.get(0).getMedicalId());
                    getSuggestion(dcMedicalInfos.get(0).getMedicalId());
                    currentMedicalInfo = dcMedicalInfos.get(0);
                    if (StringUtils.isEmpty(currentMedicalInfo.getCureDt())) {
                        currentMedicalInfo.setCureDt(orderDetailInfo.getInsertDt());
                    }
                } else {
                    String cureDt = orderDetailInfo.getInsertDt();
                    if (!StringUtils.isEmpty(cureDt) && cureDt.length() > 10) {
                        cureDt = cureDt.substring(0, 10);
                    }
                    referralCureDtTv.setText(cureDt);
                    suggestionCureDtTv.setText(cureDt);
                }
            }
        });
    }

    private void getReferral(int medicalId) {
        if (orderDetailInfo != null) {
            dcManager.getDCReferralDetail(medicalId, new DefaultResultCallback<DCReferralDetailInfo>() {
                @Override
                public void onSuccess(DCReferralDetailInfo info, BaseResult baseResult) {
                    if (info != null) {
                        referralDetailInfo = info;
                        showReferral(info);
                    } else {
                        if (currentMedicalInfo != null) {
                            info = new DCReferralDetailInfo();
                            info.setPatientName(currentMedicalInfo.getPatientName());
                            info.setCardNo(currentMedicalInfo.getCardNo());
                            info.setPhoneNum(currentMedicalInfo.getPhoneNum());
                            info.setCureDt(currentMedicalInfo.getCureDt());
                            info.setReferralName(orderDetailInfo.getLaunchHospitalName());
                            info.setReceiveName(orderDetailInfo.getDoctorHospitalName());
                            showReferral(info);
                        }
                    }
                }

                @Override
                public void onFail(BaseResult baseResult) {
                    super.onFail(baseResult);
                    if (baseResult.getCode() == -102) {
                        if (currentMedicalInfo != null) {
                            DCReferralDetailInfo info = new DCReferralDetailInfo();
                            info.setPatientName(currentMedicalInfo.getPatientName());
                            info.setCardNo(currentMedicalInfo.getCardNo());
                            info.setPhoneNum(currentMedicalInfo.getPhoneNum());
                            info.setCureDt(currentMedicalInfo.getCureDt());
                            info.setReferralName(orderDetailInfo.getLaunchHospitalName());
                            info.setReceiveName(orderDetailInfo.getDoctorHospitalName());
                            showReferral(info);
                        }
                    } else {
                        ToastUtils.showShort(R.string.no_network_connection);
                    }
                }
            });
        }
    }

    private void showReferral(DCReferralDetailInfo info) {
        referralNameItem.setInputContent(info.getPatientName());
        referralPhoneNumItem.setInputContent(info.getPhoneNum());
        referralCardNoItem.setInputContent(info.getCardNo());
        String cureDt = info.getCureDt();
        if (!StringUtils.isEmpty(cureDt) && cureDt.length() > 10) {
            cureDt = cureDt.substring(0, 10);
        }
        referralCureDtTv.setText(cureDt);
        referralSendItem.setValue(info.getReferralName());
        referralReceiverItem.setValue(info.getReceiveName());
        referralSendReasonEt.setText(info.getReferralDesc());

        Logger.logD(Logger.COMMON, "IssueReferralAndSuggestionDialog->showReferral()->inspectStr:" + info.getInspect());
        if (!StringUtils.isEmpty(info.getInspect())) {
            String[] inspectArr = info.getInspect().split(",");
            for (int i = 0; i < inspectArr.length; i++) {
                if (isInspectExist(ISSUE_TYPE_REFERRAL, inspectArr[i])) {
                    continue;
                }
                DCInspectInfo inspectInfo = new DCInspectInfo();
                inspectInfo.setInspect(inspectArr[i]);
                inspectInfo.setChecked(true);
                referralInspectInfos.add(inspectInfo);
            }

            referralChecksFl.removeAllViews();
            for (DCInspectInfo inspectInfo : referralInspectInfos) {
                addInspect(inspectInfo, true);
            }
        }
    }

    private void getSuggestion(int medicalId) {
        if (orderDetailInfo != null) {
            dcManager.getDCSuggestionDetail(medicalId, new DefaultResultCallback<DCSuggestionDetailInfo>() {
                @Override
                public void onSuccess(DCSuggestionDetailInfo info, BaseResult baseResult) {
                    if (info != null) {
                        showSuggestion(info);
                        suggestionDetailInfo = info;
                    } else {
                        if (currentMedicalInfo != null) {
                            info = new DCSuggestionDetailInfo();
                            info.setPatientName(currentMedicalInfo.getPatientName());
                            info.setCardNo(currentMedicalInfo.getCardNo());
                            info.setPhoneNum(currentMedicalInfo.getPhoneNum());
                            info.setCureDt(currentMedicalInfo.getCureDt());
                            showSuggestion(info);
                        }
                    }
                }

                @Override
                public void onFail(BaseResult baseResult) {
                    super.onFail(baseResult);
                    if (baseResult.getCode() == -102) {
                        if (currentMedicalInfo != null) {
                            DCSuggestionDetailInfo info = new DCSuggestionDetailInfo();
                            info.setPatientName(currentMedicalInfo.getPatientName());
                            info.setCardNo(currentMedicalInfo.getCardNo());
                            info.setPhoneNum(currentMedicalInfo.getPhoneNum());
                            info.setCureDt(currentMedicalInfo.getCureDt());
                            showSuggestion(info);
                        }
                    } else {
                        ToastUtils.showShort(R.string.no_network_connection);
                    }
                }
            });
        }
    }

    private void showSuggestion(DCSuggestionDetailInfo info) {
        suggestionNameItem.setInputContent(info.getPatientName());
        suggestionPhoneNumItem.setInputContent(info.getPhoneNum());
        suggestionCardNoItem.setInputContent(info.getCardNo());
        String cureDt = info.getCureDt();
        if (!StringUtils.isEmpty(cureDt) && cureDt.length() > 10) {
            cureDt = cureDt.substring(0, 10);
        }
        suggestionCureDtTv.setText(cureDt);
        suggestionDiagnoseEt.setText(info.getDiseaseName());
        suggestionRemarkEt.setText(info.getDiseaseDesc());
        Logger.logD(Logger.COMMON, "IssueReferralAndSuggestionDialog->showSuggestion()->inspectStr:" + info.getInspect());
        if (!StringUtils.isEmpty(info.getInspect())) {
            String[] inspectArr = info.getInspect().split(",");
            for (int i = 0; i < inspectArr.length; i++) {
                if (isInspectExist(ISSUE_TYPE_SUGGESTION, inspectArr[i])) {
                    continue;
                }
                DCInspectInfo inspectInfo = new DCInspectInfo();
                inspectInfo.setInspect(inspectArr[i]);
                inspectInfo.setChecked(true);
                suggestionInspectInfos.add(inspectInfo);
            }
            suggestionCheckItemsFl.removeAllViews();
            for (DCInspectInfo inspectInfo : suggestionInspectInfos) {
                addInspect(inspectInfo, false);
            }
        }
        if (info.getDrugInfos() != null && info.getDrugInfos().size() > 0) {
            for (DCDrugInfo drugInfo : info.getDrugInfos()) {
                if (isDrugExist(drugInfo)) {
                    continue;
                }
                drugInfo.setChecked(true);
                drugInfos.add(drugInfo);
            }
            drugFl.removeAllViews();
            for (DCDrugInfo drugInfo : drugInfos) {
                addDrugItem(drugInfo);
                if (drugInfo.isChecked()) {
                    selectedDrugInfos.add(drugInfo);
                }
            }
            drugAdapter.notifyDataSetChanged();
        }
    }

    private void getDefaultInspects() {
        dcManager.getDCDefaultInspecList(new DefaultResultCallback<List<DCInspectInfo>>() {
            @Override
            public void onSuccess(List<DCInspectInfo> dcInspectInfos, BaseResult baseResult) {
                defaultDCInspectInfos.addAll(dcInspectInfos);
                makeInspectData();
                for (DCInspectInfo info : referralInspectInfos) {
                    addInspect(info, true);
                }
                for (DCInspectInfo info : suggestionInspectInfos) {
                    addInspect(info, false);
                }
            }

            @Override
            public void onFinish() {
                getDefaultDrug();
            }
        });
    }

    private void makeInspectData() {
        referralInspectInfos.clear();
        suggestionInspectInfos.clear();
        drugInfos.clear();
        for (DCInspectInfo info : defaultDCInspectInfos) {
            DCInspectInfo referralInspectInfo = new DCInspectInfo();
            referralInspectInfo.setInspect(info.getInspect());
            referralInspectInfo.setChecked(false);
            referralInspectInfos.add(referralInspectInfo);
            DCInspectInfo suggestionInspectInfo = new DCInspectInfo();
            suggestionInspectInfo.setInspect(info.getInspect());
            suggestionInspectInfo.setChecked(false);
            suggestionInspectInfos.add(suggestionInspectInfo);
        }
    }

    private void makeDrugData() {
        for (DCDrugInfo info : defaultDrugInfos) {
            DCDrugInfo tempDrugInfo = new DCDrugInfo();
            tempDrugInfo.setId(info.getId());
            tempDrugInfo.setDrugName(info.getDrugName());
            tempDrugInfo.setCycle(info.getCycle());
            tempDrugInfo.setCycleNum(info.getCycleNum());
            tempDrugInfo.setDose(info.getDose());
            tempDrugInfo.setDoseUnit(info.getDoseUnit());
            tempDrugInfo.setRemark(info.getRemark());
            tempDrugInfo.setInsertDt(info.getInsertDt());
            tempDrugInfo.setChecked(false);
            drugInfos.add(tempDrugInfo);
        }
    }

    private void addInspect(DCInspectInfo info, boolean isReferral) {
        Logger.logD(Logger.COMMON, "IssueReferralAndSuggestionDialog->addInspect()->info:" + info + ", isReferral:" + isReferral);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dccheck_item, null);
        TextView titleTv = view.findViewById(R.id.layout_dccheck_item_title);
        titleTv.setText(info.getInspect());
        titleTv.setBackgroundResource(info.isChecked() ? R.drawable.bg_solid_049eff_radius_45 : R.drawable.bg_solid_ffffff_stroke_666666_radius_45);
        titleTv.setTextColor(info.isChecked() ? context.getResources().getColor(R.color.color_white) : context.getResources().getColor(R.color.color_666666));
        if (isReferral) {
            referralChecksFl.addView(view);
        } else {
            suggestionCheckItemsFl.addView(view);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setChecked(!info.isChecked());
                changeInspectCheckState(info, isReferral);
            }
        });
    }

    private void changeInspectCheckState(DCInspectInfo info, boolean isReferral) {
        if (isReferral) {
            for (int i = 0; i < referralInspectInfos.size(); i++) {
                DCInspectInfo inspectInfo = referralInspectInfos.get(i);
                if (inspectInfo.getInspect().equals(info.getInspect())) {
                    inspectInfo.setChecked(info.isChecked());
                    TextView titleTv = referralChecksFl.getChildAt(i).findViewById(R.id.layout_dccheck_item_title);
                    titleTv.setBackgroundResource(info.isChecked() ? R.drawable.bg_solid_049eff_radius_45 : R.drawable.bg_solid_ffffff_stroke_666666_radius_45);
                    titleTv.setTextColor(info.isChecked() ? context.getResources().getColor(R.color.color_white) : context.getResources().getColor(R.color.color_666666));
                    break;
                }
            }
        } else {
            for (int i = 0; i < suggestionInspectInfos.size(); i++) {
                DCInspectInfo inspectInfo = suggestionInspectInfos.get(i);
                if (inspectInfo.getInspect().equals(info.getInspect())) {
                    inspectInfo.setChecked(info.isChecked());
                    TextView titleTv = suggestionCheckItemsFl.getChildAt(i).findViewById(R.id.layout_dccheck_item_title);
                    titleTv.setBackgroundResource(info.isChecked() ? R.drawable.bg_solid_049eff_radius_45 : R.drawable.bg_solid_ffffff_stroke_666666_radius_45);
                    titleTv.setTextColor(info.isChecked() ? context.getResources().getColor(R.color.color_white) : context.getResources().getColor(R.color.color_666666));
                    break;
                }
            }
        }
    }

    private void changeDrugCheckState(DCDrugInfo info) {
        for (int i = 0; i < drugInfos.size(); i++) {
            DCDrugInfo drugInfo = drugInfos.get(i);
            if (drugInfo.getDrugName().equals(info.getDrugName())) {
                drugInfo.setChecked(info.isChecked());
                TextView titleTv = drugFl.getChildAt(i).findViewById(R.id.layout_dccheck_item_title);
                titleTv.setBackgroundResource(info.isChecked() ? R.drawable.bg_solid_049eff_radius_45 : R.drawable.bg_solid_ffffff_stroke_666666_radius_45);
                titleTv.setTextColor(info.isChecked() ? context.getResources().getColor(R.color.color_white) : context.getResources().getColor(R.color.color_666666));
                break;
            }
        }
        if (info.isChecked()) {
            selectedDrugInfos.add(info);
        } else {
            selectedDrugInfos.remove(info);
        }
        drugAdapter.notifyDataSetChanged();
    }

    private void submitReferral() {
        String patientName = referralNameItem.getInputContent();
        if (StringUtils.isEmpty(patientName)) {
            ToastUtils.showShort("请填写患者姓名");
            return;
        }
        String phoneNum = referralPhoneNumItem.getInputContent();
        if (StringUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("请填写患者电话");
            return;
        }
        if (!PhoneUtil.isPhoneNumber(phoneNum)) {
            ToastUtils.showShort("请填写正确的患者电话");
            return;
        }
        String referralReason = referralSendReasonEt.getText().toString().trim();
        if (StringUtils.isEmpty(referralReason)) {
            ToastUtils.showShort("请填写转诊原因");
            return;
        }
        String cureDt = referralCureDtTv.getText().toString().trim();
        if (StringUtils.isEmpty(cureDt)) {
            ToastUtils.showShort("请选择就诊时间");
            return;
        }
        dcManager.submitDCReferral(orderDetailInfo.getOrderId(), currentMedicalInfo == null ? 0 : currentMedicalInfo.getMedicalId(),
                patientName, phoneNum, referralCardNoItem.getInputContent(), cureDt, sendHospitalId, receiveHospitalId
                , referralReason, referralInspectInfos, new DefaultResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, BaseResult baseResult) {
                        dismiss();
                        listener.onSubmitReferralSuccess();
                    }

                    @Override
                    public void onFail(BaseResult result) {
                        super.onFail(result);
                        if (result.getCode() == 120) {
                            ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                        } else if (result.getCode() == 102) {
                            ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                        } else {
                            ToastUtils.showShort(R.string.no_network_connection);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void submitSuggestion() {
        String patientName = suggestionNameItem.getInputContent();
        if (StringUtils.isEmpty(patientName)) {
            ToastUtils.showShort("请填写患者姓名");
            return;
        }
        String cureDt = suggestionCureDtTv.getText().toString().trim();
        if (StringUtils.isEmpty(cureDt)) {
            ToastUtils.showShort("请选择就诊时间");
            return;
        }
        String phoneNum = suggestionPhoneNumItem.getInputContent();
        if (StringUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("请填写患者电话");
            return;
        }
        if (!PhoneUtil.isPhoneNumber(phoneNum)) {
            ToastUtils.showShort("请填写正确的患者电话");
            return;
        }
        dcManager.submitDCSuggestion(orderDetailInfo.getOrderId(), currentMedicalInfo == null ? 0 : currentMedicalInfo.getMedicalId(), patientName, suggestionPhoneNumItem.getInputContent()
                , suggestionCardNoItem.getInputContent(), cureDt, suggestionDiagnoseEt.getText().toString().trim(), suggestionRemarkEt.getText().toString().trim()
                , drugInfos, suggestionInspectInfos, new DefaultResultCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid, BaseResult baseResult) {
                        dismiss();
                        listener.onSubmitSuggestionSuccess();
                    }

                    @Override
                    public void onFail(BaseResult result) {
                        super.onFail(result);
                        if (result.getCode() == 120) {
                            ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                        } else if (result.getCode() == 102) {
                            ToastUtils.showShort("患者联系电话不能填写医生或销售代表电话");
                        } else {
                            ToastUtils.showShort(R.string.no_network_connection);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private void clearInputs() {
        referralDetailInfo = null;
        suggestionDetailInfo = null;
        currentMedicalInfo = null;

        radioGroup.check(R.id.layout_dcissue_order_dialog_type_referral_rb);

        referralNameItem.setInputContent("");
        referralPhoneNumItem.setInputContent("");
        referralCardNoItem.setInputContent("");
        referralSendReasonEt.setText("");
        referralChecksFl.removeAllViews();

        suggestionNameItem.setInputContent("");
        suggestionPhoneNumItem.setInputContent("");
        suggestionCardNoItem.setInputContent("");
        suggestionDiagnoseEt.setText("");
        suggestionRemarkEt.setText("");
        suggestionCheckItemsFl.removeAllViews();
        drugFl.removeAllViews();

        makeInspectData();
        for (DCInspectInfo info : referralInspectInfos) {
            addInspect(info, true);
        }
        for (DCInspectInfo info : suggestionInspectInfos) {
            addInspect(info, false);
        }
        makeDrugData();
        for (DCDrugInfo info : drugInfos) {
            addDrugItem(info);
        }
        selectedDrugInfos.clear();
        drugAdapter.notifyDataSetChanged();
    }

    private boolean isInspectExist(int issueType, String inspect) {
        if (issueType == ISSUE_TYPE_REFERRAL) {
            for (DCInspectInfo info : referralInspectInfos) {
                if (inspect.equals(info.getInspect())) {
                    info.setChecked(true);
                    return true;
                }
            }
            return false;
        } else {
            for (DCInspectInfo info : suggestionInspectInfos) {
                if (inspect.equals(info.getInspect())) {
                    info.setChecked(true);
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isDrugExist(DCDrugInfo info) {
        for (DCDrugInfo drugInfo : drugInfos) {
            if (info.getDrugName().equals(drugInfo.getDrugName())) {
                drugInfo.setChecked(true);
                return true;
            }
        }
        return false;
    }

    private void getDefaultDrug() {
        dcManager.getDCDefaultDrugList(new DefaultResultCallback<List<DCDrugInfo>>() {
            @Override
            public void onSuccess(List<DCDrugInfo> infos, BaseResult baseResult) {
                if (infos == null) {
                    return;
                }
                defaultDrugInfos = infos;
                makeDrugData();
                for (DCDrugInfo drugInfo : drugInfos) {
                    addDrugItem(drugInfo);
                }
            }

            @Override
            public void onFinish() {
                getMedicalList(orderDetailInfo.getOrderId());
            }
        });
    }

    private void addDrugItem(DCDrugInfo info) {
        Logger.logD(Logger.COMMON, "IssueReferralAndSuggestionDialog->addInspect()->info:" + info + ", isReferral:" + isReferral);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dccheck_item, null);
        TextView titleTv = view.findViewById(R.id.layout_dccheck_item_title);
        titleTv.setText(info.getDrugName());
        titleTv.setBackgroundResource(info.isChecked() ? R.drawable.bg_solid_049eff_radius_45 : R.drawable.bg_solid_ffffff_stroke_666666_radius_45);
        titleTv.setTextColor(info.isChecked() ? context.getResources().getColor(R.color.color_white) : context.getResources().getColor(R.color.color_666666));
        drugFl.addView(view);
        view.setOnClickListener(v -> {
            info.setChecked(!info.isChecked());
            changeDrugCheckState(info);
        });
    }

    private void showChoiceUsageWindow(DCDrugInfo dcDrugInfo) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_dcdrug_usage_window, null);
        Dialog choiceUsageWindow = new Dialog(context, R.style.custom_noActionbar_window_style);
        choiceUsageWindow.setContentView(contentView);
        choiceUsageWindow.setCanceledOnTouchOutside(false);
        choiceUsageWindow.setCancelable(true);
        Window dialogWindow = choiceUsageWindow.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        contentView.findViewById(R.id.layout_dcdrug_usage_window_add_dosage_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomCycleAndCycleNumWindow(dcDrugInfo);
                choiceUsageWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.layout_dcdrug_usage_window_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceUsageWindow.dismiss();
            }
        });
        RecyclerView cycleRv = contentView.findViewById(R.id.layout_dcdrug_usage_window_cycle_rv);
        RecyclerView cycleNumRv = contentView.findViewById(R.id.layout_dcdrug_usage_window_cyclenum_rv);
        LinearLayoutManager cycleLayoutManager = new LinearLayoutManager(context);
        cycleLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cycleRv.setLayoutManager(cycleLayoutManager);
        LinearLayoutManager cycleNumLayoutManager = new LinearLayoutManager(context);
        cycleNumLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cycleNumRv.setLayoutManager(cycleNumLayoutManager);

        cycleAdapter = new DCDrugUsageListAdapter(R.layout.item_dcdrug_usage, cycleDrugUsageDosageInfos);
        cycleRv.setAdapter(cycleAdapter);
        cycleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DCDrugUsageDosageInfo info = (DCDrugUsageDosageInfo) adapter.getData().get(position);
                for (DCDrugUsageDosageInfo tempInfo : cycleDrugUsageDosageInfos) {
                    if (info.getTitle().equals(tempInfo.getTitle())) {
                        tempInfo.setSelected(true);
                    } else {
                        tempInfo.setSelected(false);
                    }
                }
                cycleAdapter.notifyDataSetChanged();

                for (DCDrugUsageDosageInfo tempInfo : cycleNumDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        choiceUsageWindow.dismiss();
                        break;
                    }
                }
            }
        });

        cycleNumAdapter = new DCDrugUsageListAdapter(R.layout.item_dcdrug_usage, cycleNumDrugUsageDosageInfos);
        cycleNumRv.setAdapter(cycleNumAdapter);
        cycleNumAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DCDrugUsageDosageInfo info = (DCDrugUsageDosageInfo) adapter.getData().get(position);
                for (DCDrugUsageDosageInfo tempInfo : cycleNumDrugUsageDosageInfos) {
                    if (info.getTitle().equals(tempInfo.getTitle())) {
                        tempInfo.setSelected(true);
                    } else {
                        tempInfo.setSelected(false);
                    }
                }
                cycleNumAdapter.notifyDataSetChanged();

                for (DCDrugUsageDosageInfo tempInfo : cycleDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        choiceUsageWindow.dismiss();
                        break;
                    }
                }
            }
        });
        choiceUsageWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dcDrugInfo == null) {
                    return;
                }
                String cycleStr = "";
                String cycleNumStr = "";
                for (DCDrugUsageDosageInfo tempInfo : cycleDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        cycleStr = tempInfo.getTitle();
                        break;
                    }
                }
                for (DCDrugUsageDosageInfo tempInfo : cycleNumDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        cycleNumStr = tempInfo.getTitle();
                        break;
                    }
                }
                if (!StringUtils.isEmpty(cycleStr) && !StringUtils.isEmpty(cycleNumStr)) {
                    dcDrugInfo.setCycle(cycleStr);
                    dcDrugInfo.setCycleNum(cycleNumStr);
                }

                for (DCDrugUsageDosageInfo tempInfo : cycleDrugUsageDosageInfos) {
                    tempInfo.setSelected(false);
                }
                for (DCDrugUsageDosageInfo tempInfo : cycleNumDrugUsageDosageInfos) {
                    tempInfo.setSelected(false);
                }
                cycleAdapter.notifyDataSetChanged();
                cycleNumAdapter.notifyDataSetChanged();
                drugAdapter.notifyDataSetChanged();
            }
        });
        choiceUsageWindow.show();
    }

    private void showChoiceDosageWindow(DCDrugInfo dcDrugInfo) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_dcdrug_usage_window, null);
        Dialog choiceDosageWindow = new Dialog(context, R.style.custom_noActionbar_window_style);
        choiceDosageWindow.setContentView(contentView);
        choiceDosageWindow.setCanceledOnTouchOutside(false);
        choiceDosageWindow.setCancelable(true);
        Window dialogWindow = choiceDosageWindow.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        contentView.findViewById(R.id.layout_dcdrug_usage_window_add_dosage_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDoseAndeDoseUnWindow(dcDrugInfo);
                choiceDosageWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.layout_dcdrug_usage_window_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceDosageWindow.dismiss();
            }
        });
        RecyclerView doseRv = contentView.findViewById(R.id.layout_dcdrug_usage_window_cycle_rv);
        RecyclerView doseUnitRv = contentView.findViewById(R.id.layout_dcdrug_usage_window_cyclenum_rv);
        LinearLayoutManager doseLayoutManager = new LinearLayoutManager(context);
        doseLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        doseRv.setLayoutManager(doseLayoutManager);
        LinearLayoutManager doseUnitLayoutManager = new LinearLayoutManager(context);
        doseUnitLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        doseUnitRv.setLayoutManager(doseUnitLayoutManager);

        doseAdapter = new DCDrugUsageListAdapter(R.layout.item_dcdrug_usage, doseDrugUsageDosageInfos);
        doseRv.setAdapter(doseAdapter);
        doseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DCDrugUsageDosageInfo info = (DCDrugUsageDosageInfo) adapter.getData().get(position);
                for (DCDrugUsageDosageInfo tempInfo : doseDrugUsageDosageInfos) {
                    if (info.getTitle().equals(tempInfo.getTitle())) {
                        tempInfo.setSelected(true);
                    } else {
                        tempInfo.setSelected(false);
                    }
                }
                doseAdapter.notifyDataSetChanged();

                for (DCDrugUsageDosageInfo tempInfo : doseUnDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        choiceDosageWindow.dismiss();
                        break;
                    }
                }
            }
        });

        doseUnAdapter = new DCDrugUsageListAdapter(R.layout.item_dcdrug_usage, doseUnDrugUsageDosageInfos);
        doseUnitRv.setAdapter(doseUnAdapter);
        doseUnAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DCDrugUsageDosageInfo info = (DCDrugUsageDosageInfo) adapter.getData().get(position);
                for (DCDrugUsageDosageInfo tempInfo : doseUnDrugUsageDosageInfos) {
                    if (info.getTitle().equals(tempInfo.getTitle())) {
                        tempInfo.setSelected(true);
                    } else {
                        tempInfo.setSelected(false);
                    }
                }
                doseUnAdapter.notifyDataSetChanged();

                for (DCDrugUsageDosageInfo tempInfo : doseDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        choiceDosageWindow.dismiss();
                        break;
                    }
                }
            }
        });
        choiceDosageWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dcDrugInfo == null) {
                    return;
                }
                String doseStr = "";
                String doseUnStr = "";
                for (DCDrugUsageDosageInfo tempInfo : doseDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        doseStr = tempInfo.getTitle();
                        break;
                    }
                }
                for (DCDrugUsageDosageInfo tempInfo : doseUnDrugUsageDosageInfos) {
                    if (tempInfo.isSelected()) {
                        doseUnStr = tempInfo.getTitle();
                        break;
                    }
                }
                if (!StringUtils.isEmpty(doseStr) && !StringUtils.isEmpty(doseUnStr)) {
                    dcDrugInfo.setDose(doseStr);
                    dcDrugInfo.setDoseUnit(doseUnStr);
                }

                for (DCDrugUsageDosageInfo tempInfo : doseDrugUsageDosageInfos) {
                    tempInfo.setSelected(false);
                }
                for (DCDrugUsageDosageInfo tempInfo : doseUnDrugUsageDosageInfos) {
                    tempInfo.setSelected(false);
                }
                doseAdapter.notifyDataSetChanged();
                doseUnAdapter.notifyDataSetChanged();
                drugAdapter.notifyDataSetChanged();
            }
        });
        choiceDosageWindow.show();
    }

    private void showCustomCycleAndCycleNumWindow(DCDrugInfo dcDrugInfo) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_dcdrug_usage_custom_window, null);
        Dialog cycleAndCycleNumCustomWindow = new Dialog(context, R.style.custom_noActionbar_window_style);
        cycleAndCycleNumCustomWindow.setContentView(contentView);
        cycleAndCycleNumCustomWindow.setCanceledOnTouchOutside(false);
        cycleAndCycleNumCustomWindow.setCancelable(true);
        Window dialogWindow = cycleAndCycleNumCustomWindow.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        EditText cycleEt = contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_cycle_et);
        EditText cycleNumEt = contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_cyclenum_et);
        contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cycleStr = cycleEt.getText().toString().trim();
                String cycleNumStr = cycleNumEt.getText().toString().trim();
                if (StringUtils.isEmpty(cycleStr)) {
                    ToastUtils.showShort("请输入天数");
                    return;
                }
                if (StringUtils.isEmpty(cycleNumStr)) {
                    ToastUtils.showShort("请输入次数");
                    return;
                }
                if (dcDrugInfo != null) {
                    dcDrugInfo.setCycle(cycleStr);
                    dcDrugInfo.setCycleNum(cycleNumStr);
                }
                cycleEt.setText("");
                cycleNumEt.setText("");
                cycleAndCycleNumCustomWindow.dismiss();
                drugAdapter.notifyDataSetChanged();
            }
        });
        contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleAndCycleNumCustomWindow.dismiss();
            }
        });
        cycleAndCycleNumCustomWindow.show();
    }

    private void showCustomDoseAndeDoseUnWindow(DCDrugInfo dcDrugInfo) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_dcdrug_usage_custom_window, null);
        Dialog doseAndDoseUnCustomWindow = new Dialog(context, R.style.custom_noActionbar_window_style);
        doseAndDoseUnCustomWindow.setContentView(contentView);
        doseAndDoseUnCustomWindow.setCanceledOnTouchOutside(false);
        doseAndDoseUnCustomWindow.setCancelable(true);
        Window dialogWindow = doseAndDoseUnCustomWindow.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ScreenUtil.getScreenWidth() - DisplayUtil.dp2px(30);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        EditText doseEt = contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_cycle_et);
        doseEt.setHint("请输入剂量");
        EditText doseUnNumEt = contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_cyclenum_et);
        doseUnNumEt.setHint("请输入单位");
        contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String doseStr = doseEt.getText().toString().trim();
                String doseUnNumStr = doseUnNumEt.getText().toString().trim();
                if (StringUtils.isEmpty(doseStr)) {
                    ToastUtils.showShort("请输入剂量");
                    return;
                }
                if (StringUtils.isEmpty(doseUnNumStr)) {
                    ToastUtils.showShort("请输入单位");
                    return;
                }
                if (dcDrugInfo != null) {
                    dcDrugInfo.setDose(doseStr);
                    dcDrugInfo.setDoseUnit(doseUnNumStr);
                }
                doseEt.setText("");
                doseUnNumEt.setText("");
                doseAndDoseUnCustomWindow.dismiss();
                drugAdapter.notifyDataSetChanged();
            }
        });
        contentView.findViewById(R.id.layotu_dcdrug_usage_custom_window_close_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doseAndDoseUnCustomWindow.dismiss();
            }
        });
        doseAndDoseUnCustomWindow.show();
    }

    private void showReferralDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                referralCureDtTv.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showSuggestionDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                suggestionCureDtTv.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public interface OnIssueReferralAndSuggestionDialogStateChangeListener {
        void onSubmitReferralSuccess();

        void onSubmitSuggestionSuccess();
    }
}
