package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.entity.rounds.BasicMedicalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.RoundsMedicalDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.rounds.GetRoundsAssociatedMedicalRequester;
import cn.longmaster.hospital.doctor.core.requests.rounds.RoundsMedicalDetailsRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.RecordMedicalFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.RoundsBasicMedicalFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.RoundsReturnVisitFragment;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;

/**
 * 患者信息Activity
 * Mod by Biao on 2017/7/3
 */
public class RoundsPatientInfoActivity extends NewBaseActivity {
    public final static String TAG = RoundsPatientInfoActivity.class.getSimpleName();
    private final int REQUEST_CODE_MODIFY_INFORMATION = 200; //页面请求码:修改信息
    @FindViewById(R.id.activity_rounds_patient_info_radio_group)
    private RadioGroup mRadioGroup;
    @FindViewById(R.id.activity_rounds_patient_info_tab_medical_rb)
    private RadioButton mMedicalRecordRadioBtn;
    @FindViewById(R.id.activity_rounds_patient_record_rb)
    private RadioButton mRecordRadioBtn;
    @FindViewById(R.id.activity_rounds_patient_return_visit_rb)
    private RadioButton mReturnVisitRadioBtn;
    @FindViewById(R.id.activity_rounds_patient_info_enter_room_ll)
    private LinearLayout activityRoundsPatientInfoEnterRoomLl;
    @FindViewById(R.id.activity_rounds_patient_info_modification_info_iv)
    private ImageView activityRoundsPatientInfoModificationInfoIv;
    @FindViewById(R.id.activity_rounds_patient_info_data_manage_iv)
    private ImageView activityRoundsPatientInfoDataManageIv;
    @FindViewById(R.id.activity_rounds_patient_info_join_room_iv)
    private ImageView activityRoundsPatientInfoJoinRoomIv;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;

    private int mCurrentTab = 0;

    private RoundsMedicalDetailsInfo mRoundsMedicalDetailsInfo;
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private BasicMedicalInfo basicMedicalInfo;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        basicMedicalInfo = (BasicMedicalInfo) intent.getSerializableExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_BASIC_MEDICAL_INFO);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_patient_info;
    }

    @Override
    protected void initViews() {
        if (basicMedicalInfo.isRoom() || basicMedicalInfo.isRelateRecord()) {
            activityRoundsPatientInfoEnterRoomLl.setVisibility(View.GONE);
        } else {
            activityRoundsPatientInfoEnterRoomLl.setVisibility(View.VISIBLE);
        }
        if (basicMedicalInfo.getOrderState() == AppConstant.AppointmentState.WAIT_ASSISTANT_CALL
                || basicMedicalInfo.getOrderState() == AppConstant.AppointmentState.DATA_CHECK_FAIL
                || basicMedicalInfo.getOrderState() == 0) {
            activityRoundsPatientInfoJoinRoomIv.setVisibility(View.GONE);
        } else {
            activityRoundsPatientInfoJoinRoomIv.setVisibility(View.VISIBLE);
        }
        if (basicMedicalInfo.isExperts() && !basicMedicalInfo.isHaveAuthority()) {
            activityRoundsPatientInfoDataManageIv.setVisibility(View.GONE);
        } else {
            activityRoundsPatientInfoDataManageIv.setVisibility(View.VISIBLE);
        }
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.activity_rounds_patient_info_tab_medical_rb:
                    mMedicalRecordRadioBtn.setTextSize(18);
                    mRecordRadioBtn.setTextSize(16);
                    mReturnVisitRadioBtn.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.activity_rounds_patient_record_rb:
                    mMedicalRecordRadioBtn.setTextSize(16);
                    mRecordRadioBtn.setTextSize(18);
                    mReturnVisitRadioBtn.setTextSize(16);
                    mCurrentTab = 1;
                    break;
                case R.id.activity_rounds_patient_return_visit_rb:
                    mMedicalRecordRadioBtn.setTextSize(16);
                    mRecordRadioBtn.setTextSize(16);
                    mReturnVisitRadioBtn.setTextSize(18);
                    mCurrentTab = 2;
                    break;
                default:
                    break;
            }
            if (null != radioTabFragmentHelper) {
                radioTabFragmentHelper.setFragment(mCurrentTab);
            }
        });
        initListener();
        getRoundsAssociatedMedical(basicMedicalInfo.getMedicalId());
        getMedicalDetails(basicMedicalInfo.getMedicalId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.APPOINTMENT, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MODIFY_INFORMATION:
                    if (null != data && data.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATIENT_DELETE, false)) {
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    } else {
                        getRoundsAssociatedMedical(basicMedicalInfo.getMedicalId());
                        getMedicalDetails(basicMedicalInfo.getMedicalId());
                    }
                    break;
                default:
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initListener() {
        ivToolBarBack.setOnClickListener(v -> {
            onBackPressed();
        });
        tvToolBarTitle.setText("患者信息");
        ivToolBarSub.setImageResource(R.mipmap.ic_patient_info_share);
        ivToolBarSub.setVisibility(View.VISIBLE);
        ivToolBarSub.setOnClickListener(v -> {
            if (null != mRoundsMedicalDetailsInfo && null != getShareContent(mRoundsMedicalDetailsInfo, mCurrentTab)) {
                showShareDialog(getShareContent(mRoundsMedicalDetailsInfo, mCurrentTab), mRoundsMedicalDetailsInfo);
            }
        });
        activityRoundsPatientInfoModificationInfoIv.setOnClickListener(v -> {
            getDisplay().startRoundsPatientAddActivity(basicMedicalInfo.getMedicalId(), 0, false, REQUEST_CODE_MODIFY_INFORMATION);
        });
        activityRoundsPatientInfoDataManageIv.setOnClickListener(v -> {
            getDisplay().startRoundsDataManagerActivity(basicMedicalInfo.getMedicalId(), false, false, 0);
        });
        activityRoundsPatientInfoJoinRoomIv.setOnClickListener(v -> {
            Intent intent = new Intent(getThisActivity(), RoundsConsultRoomActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, basicMedicalInfo.getOrderId());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_COURSE_USER_TYPE, basicMedicalInfo.getUserType());
            startActivity(intent);
        });
    }

    /**
     * 拉取查房病历关联病历
     */
    public void getRoundsAssociatedMedical(int medicalId) {
        GetRoundsAssociatedMedicalRequester requester = new GetRoundsAssociatedMedicalRequester((baseResult, roundsAssociatedMedicalInfos) -> {
            Logger.logI(Logger.APPOINTMENT, "RoundsPatientInfoActivity->getRoundsAssociatedMedical:-->roundsAssociatedMedicalInfos:" + roundsAssociatedMedicalInfos);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (LibCollections.isEmpty(roundsAssociatedMedicalInfos)) {
                    mRecordRadioBtn.setVisibility(View.GONE);
//                    mMedicalRecordRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
//                    mReturnVisitRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
                } else {
                    mRecordRadioBtn.setVisibility(View.VISIBLE);
                }
                if (mRoundsMedicalDetailsInfo == null || StringUtils.isEmpty(mRoundsMedicalDetailsInfo.getVisitUrl())) {
                    mReturnVisitRadioBtn.setVisibility(View.GONE);
//                    mMedicalRecordRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
//                    mRecordRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
                } else {
                    mReturnVisitRadioBtn.setVisibility(View.VISIBLE);
                }
                if (LibCollections.isEmpty(roundsAssociatedMedicalInfos) && StringUtils.isEmpty(basicMedicalInfo.getVisitUrl())) {
                    mRadioGroup.setVisibility(View.GONE);
                } else {
                    mRadioGroup.setVisibility(View.VISIBLE);
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.medicalId = medicalId;
        requester.doPost();
    }

    /**
     * 此处拉取病历详情只为存储数据
     */

    private void getMedicalDetails(int medicalId) {
        RoundsMedicalDetailsRequester requester = new RoundsMedicalDetailsRequester(new DefaultResultCallback<RoundsMedicalDetailsInfo>() {
            @Override
            public void onSuccess(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, BaseResult baseResult) {
                mRoundsMedicalDetailsInfo = roundsMedicalDetailsInfo;
                if (StringUtils.isEmpty(roundsMedicalDetailsInfo.getVisitUrl())) {
                    mReturnVisitRadioBtn.setVisibility(View.GONE);
//                    mMedicalRecordRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
//                    mRecordRadioBtn.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
                } else {
                    mReturnVisitRadioBtn.setVisibility(View.VISIBLE);
                }
                if (mRecordRadioBtn.getVisibility() == View.GONE && mReturnVisitRadioBtn.getVisibility() == View.GONE) {
                    mRadioGroup.setVisibility(View.GONE);
                } else {
                    mRadioGroup.setVisibility(View.VISIBLE);
                }
                List<Fragment> fragments = new ArrayList<>(3);
                fragments.add(RoundsBasicMedicalFragment.getInstance(roundsMedicalDetailsInfo, basicMedicalInfo.isRoom()));
                fragments.add(RecordMedicalFragment.getInstance(medicalId));
                fragments.add(RoundsReturnVisitFragment.getInstance(basicMedicalInfo.isRoom(), roundsMedicalDetailsInfo.getVisitUrl()));
                radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                        .setContainerViewId(R.id.activity_rounds_patient_info_fragment_layout_fl)
                        .setFragmentList(fragments)
                        .setFragmentManager(getSupportFragmentManager())
                        .setCurrentTab(mCurrentTab)
                        .build();
                if (!Utils.activityIsDestroyed(RoundsPatientInfoActivity.this)) {
                    radioTabFragmentHelper.initFragment();
                }
            }
        });
        requester.setMedicalId(medicalId);
        requester.doPost();
    }


    private void showShareDialog(ShareEntity shareEntity, RoundsMedicalDetailsInfo roundsMedicalDetailsInfo) {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        ShareDialog shareDialog = new ShareDialog(this, shareItemList);
        shareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                getShareManager().shareToWeiChat(RoundsPatientInfoActivity.this, shareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(RoundsPatientInfoActivity.this, shareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(RoundsPatientInfoActivity.this, shareEntity);
            }

            @Override
            public void onMyConsultClick() {

            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (roundsMedicalDetailsInfo == null || null == cm) {
                    ToastUtils.showShort("复制失败，请重试！");
                    return;
                }
                if (mCurrentTab == 0) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", roundsMedicalDetailsInfo.getMedicalShareUrl()));
                } else if (mCurrentTab == 1) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", roundsMedicalDetailsInfo.getRelationShareUrl()));
                } else if (mCurrentTab == 2) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", roundsMedicalDetailsInfo.getVisitShareUrl()));
                }
                ToastUtils.showShort(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {

            }
        });
        shareDialog.show();
    }

    private ShareEntity getShareContent(RoundsMedicalDetailsInfo roundsMedicalDetailsInfo, int currentTab) {
        ShareEntity shareEntity = new ShareEntity();
        if (currentTab == 0) {
            shareEntity.setUrl(roundsMedicalDetailsInfo.getMedicalShareUrl());
        } else if (currentTab == 1) {
            shareEntity.setUrl(roundsMedicalDetailsInfo.getRelationShareUrl());
        } else if (currentTab == 2) {
            shareEntity.setUrl(roundsMedicalDetailsInfo.getVisitShareUrl());
        }
        shareEntity.setTitle("39互联网医院病历信息");
        shareEntity.setImgUrl("");
        shareEntity.setContent(roundsMedicalDetailsInfo.getPatientName() + " " + (roundsMedicalDetailsInfo.getGender() == 1 ? "男" : "女") + " " + roundsMedicalDetailsInfo.getAge() + " " + roundsMedicalDetailsInfo.getAttendingDisease());
        return shareEntity;
    }
}
