package cn.longmaster.hospital.doctor.ui.dutyclinic.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCOrderDetailInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCReferralDetailInfo;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.util.ListUtils;
import cn.longmaster.hospital.doctor.view.DCIssueOrderTitleAndValueItem;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 值班门诊-转诊单确认框
 * Created by yangyong on 2019-12-02.
 */
public class ReferralMakeSureDialog extends Dialog {
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_name_item)
    private DCIssueOrderTitleAndValueItem nameItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_phonenum_item)
    private DCIssueOrderTitleAndValueItem phoneNumItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_cardno_item)
    private DCIssueOrderTitleAndValueItem cardNoItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_curedt_item)
    private DCIssueOrderTitleAndValueItem cureDtItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_sender_item)
    private DCIssueOrderTitleAndValueItem senderItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_receiver_item)
    private DCIssueOrderTitleAndValueItem receiverItem;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_referral_reason_item)
    private TextView referralReasonTv;
    @FindViewById(R.id.layout_dcreferral_make_sure_dialog_inspect_fl)
    private FlexboxLayout inspectFl;

    private Context context;
    private DCOrderDetailInfo orderDetailInfo;
    private ReferralMakeSureDialogStateChangeListener listener;
    private UserInfoManager userInfoManager;
    private DCManager dcManager;
    private int mMedicalId;

    public void setOrderDetailInfo(DCOrderDetailInfo orderDetailInfo) {
        this.orderDetailInfo = orderDetailInfo;
        getMedicalList();
    }

    public void setReferralMakeSureDialogStateChangeListener(ReferralMakeSureDialogStateChangeListener listener) {
        this.listener = listener;
    }

    public ReferralMakeSureDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dcreferral_make_sure_dialog);
        ViewInjecter.inject(this);
        userInfoManager = AppApplication.getInstance().getManager(UserInfoManager.class);
        dcManager = AppApplication.getInstance().getManager(DCManager.class);
    }

    @OnClick({R.id.layout_dcreferral_make_sure_dialog_modify_btn,
            R.id.layout_dcreferral_make_sure_dialog_sure_btn,
            R.id.layout_dcissue_order_dialog_close_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dcreferral_make_sure_dialog_modify_btn:
                listener.onModifyClicked();
                dismiss();
                break;

            case R.id.layout_dcreferral_make_sure_dialog_sure_btn:
                if (null != orderDetailInfo && userInfoManager.getCurrentUserInfo().getUserId() == orderDetailInfo.getLaunchDoctorId()) {
                    dcManager.sendMsgForIssueAndSuggest(mMedicalId, 0, new DefaultResultCallback<Void>() {
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
            case R.id.layout_dcissue_order_dialog_close_iv:
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
                if (dcMedicalInfos != null && dcMedicalInfos.size() > 0) {
                    ListUtils.sort(dcMedicalInfos, false, "insert_dt");
                    mMedicalId = dcMedicalInfos.get(0).getMedicalId();
                    getReferral(mMedicalId);
                }
            }
        });
    }

    private void getReferral(int medicalId) {
        if (orderDetailInfo == null) {
            return;
        }
        dcManager.getDCReferralDetail(medicalId, new DefaultResultCallback<DCReferralDetailInfo>() {
            @Override
            public void onSuccess(DCReferralDetailInfo dcReferralDetailInfo, BaseResult baseResult) {
                if (dcReferralDetailInfo != null) {
                    showReferral(dcReferralDetailInfo);
                }
            }
        });
    }

    private void showReferral(DCReferralDetailInfo info) {
        nameItem.setValue(info.getPatientName());
        phoneNumItem.setValue(info.getPhoneNum());
        cardNoItem.setValue(info.getCardNo());
        String cureDt = info.getCureDt();
        if (!StringUtils.isEmpty(cureDt) && cureDt.length() > 10) {
            cureDt = cureDt.substring(0, 10);
        }
        cureDtItem.setValue(cureDt);
        senderItem.setValue(info.getReferralName());
        receiverItem.setValue(info.getReceiveName());
        referralReasonTv.setText(info.getReferralDesc());
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
    }

    public interface ReferralMakeSureDialogStateChangeListener {
        void onModifyClicked();
    }
}
