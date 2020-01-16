package cn.longmaster.hospital.doctor.ui.dutyclinic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCDrugInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCSuggestionDetailInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.ui.dutyclinic.adapter.DCSuggestionMakeSureDrugListAdapter;
import cn.longmaster.hospital.doctor.util.ListUtils;
import cn.longmaster.hospital.doctor.view.DCIssueOrderTitleAndValueItem;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 值班门诊-诊疗意见确认框
 * Created by yangyong on 2019-12-02.
 */
public class SuggestionMakeSureDialog extends Dialog {
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_name_item)
    private DCIssueOrderTitleAndValueItem nameItem;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_phonenum_item)
    private DCIssueOrderTitleAndValueItem phoneNumItem;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_cardno_item)
    private DCIssueOrderTitleAndValueItem cardNoItem;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_curedt_item)
    private DCIssueOrderTitleAndValueItem cureDtItem;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_diagnosis_tv)
    private TextView diagnosisTv;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_remark_tv)
    private TextView remarkTv;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_inspect_fl)
    private FlexboxLayout inspectFl;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_drug_fl)
    private FlexboxLayout drugFl;
    @FindViewById(R.id.layout_dcsuggestion_make_sure_dialog_drug_rv)
    private RecyclerView drugUsageAndDosageRv;

    private SuggestionMakeSureDialogStateChangeListener listener;
    private Context context;
    private DCOrderDetailInfo orderDetailInfo;
    private UserInfoManager userInfoManager;
    private DCManager dcManager;
    private int mMedicalId;
    public void setOrderDetailInfo(DCOrderDetailInfo orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
        getMedicalList();
    }

    public void setSuggestionMakeSureDialogStateChangeListener(SuggestionMakeSureDialogStateChangeListener listener) {
        this.listener = listener;
    }

    public SuggestionMakeSureDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfoManager = AppApplication.getInstance().getManager(UserInfoManager.class);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
        setContentView(R.layout.layout_dcsuggestion_make_sure_dialog);
        ViewInjecter.inject(this);
    }

    @OnClick({R.id.layout_dcsuggestion_make_sure_dialog_close_iv,
            R.id.layout_dcsuggestion_make_sure_dialog_modify_btn,
            R.id.layout_dcsuggestion_make_sure_dialog_sure_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dcsuggestion_make_sure_dialog_close_iv:
            case R.id.layout_dcsuggestion_make_sure_dialog_sure_btn:
                if (null != orderDetailInfo && userInfoManager.getCurrentUserInfo().getUserId() == orderDetailInfo.getLaunchDoctorId()) {
                    dcManager.sendMsgForIssueAndSuggest(mMedicalId, 1, new DefaultResultCallback<Void>() {
                        @Override
                        public void onSuccess(Void aVoid, BaseResult baseResult) {
                            ToastUtils.showShort("发送短信成功");
                        }

                        @Override
                        public void onFinish() {
                            dismiss();
                        }
                    });
                } else {
                    dismiss();
                }
                break;
            case R.id.layout_dcsuggestion_make_sure_dialog_modify_btn:
                listener.onModifyClicked();
                dismiss();
                break;

            default:
                break;
        }
    }

    private void getMedicalList() {
        dcManager.getDCRoomMedicalList(orderDetailInfo.getOrderId(), new DefaultResultCallback<List<DCMedicalInfo>>() {
            @Override
            public void onSuccess(List<DCMedicalInfo> dcMedicalInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(dcMedicalInfos)) {
                    ListUtils.sort(dcMedicalInfos, false, "insert_dt");
                    mMedicalId = dcMedicalInfos.get(0).getMedicalId();
                    getSuggestion(mMedicalId);
                }
            }
        });
    }

    private void getSuggestion(int medicalId) {
        if (orderDetailInfo == null) {
            return;
        }
        dcManager.getDCSuggestionDetail(medicalId, new DefaultResultCallback<DCSuggestionDetailInfo>() {
            @Override
            public void onSuccess(DCSuggestionDetailInfo dcSuggestionDetailInfo, BaseResult baseResult) {
                if (dcSuggestionDetailInfo != null) {
                    showSuggestion(dcSuggestionDetailInfo);
                }
            }
        });
    }

    private void showSuggestion(DCSuggestionDetailInfo info) {
        nameItem.setValue(info.getPatientName());
        phoneNumItem.setValue(info.getPhoneNum());
        cardNoItem.setValue(info.getCardNo());
        String cureDt = info.getCureDt();
        if (!StringUtils.isEmpty(cureDt) && cureDt.length() > 10) {
            cureDt = cureDt.substring(0, 10);
        }
        cureDtItem.setValue(cureDt);
        diagnosisTv.setText(info.getDiseaseName());
        remarkTv.setText(info.getDiseaseDesc());
        if (!StringUtils.isEmpty(info.getInspect())) {
            String[] inspectArr = info.getInspect().split(",");
            for (int i = 0; i < inspectArr.length; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.layout_dccheck_item, null);
                TextView titleTv = view.findViewById(R.id.layout_dccheck_item_title);
                titleTv.setText(inspectArr[i]);
                titleTv.setBackgroundResource(R.drawable.bg_solid_049eff_radius_45);
                titleTv.setTextColor(context.getResources().getColor(R.color.color_white));
                inspectFl.addView(view);
            }
        }
        if (info.getDrugInfos() != null && info.getDrugInfos().size() > 0) {
            for (DCDrugInfo drugInfo : info.getDrugInfos()) {
                View view = LayoutInflater.from(context).inflate(R.layout.layout_dccheck_item, null);
                TextView titleTv = view.findViewById(R.id.layout_dccheck_item_title);
                titleTv.setText(drugInfo.getDrugName());
                titleTv.setBackgroundResource(R.drawable.bg_solid_049eff_radius_45);
                titleTv.setTextColor(context.getResources().getColor(R.color.color_white));
                drugFl.addView(view);
            }

            DCSuggestionMakeSureDrugListAdapter drugListAdapter = new DCSuggestionMakeSureDrugListAdapter(R.layout.item_dcsuggestion_makesure_drug, info.getDrugInfos());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            drugUsageAndDosageRv.setLayoutManager(linearLayoutManager);
            drugUsageAndDosageRv.setNestedScrollingEnabled(false);
            drugUsageAndDosageRv.setAdapter(drugListAdapter);
        }
    }

    public interface SuggestionMakeSureDialogStateChangeListener {
        void onModifyClicked();
    }
}
