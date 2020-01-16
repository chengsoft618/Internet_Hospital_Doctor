package cn.longmaster.hospital.doctor.ui.rounds;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.entity.department.DepartmentInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.HospitalInfo;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.rounds.OrderDetailsRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.OrderDetailsFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.SummaryFragment;
import cn.longmaster.hospital.doctor.ui.rounds.fragment.VideoPlaybackFragment;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.hospital.doctor.view.AppActionBar;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;
import cn.longmaster.utils.Utils;

/**
 * 查房详情activity
 * Mod by biao on 2019/12/03
 */
public class RoundsDetailActivity extends NewBaseActivity {
    @FindViewById(R.id.act_rounds_detail_aab)
    private AppActionBar actRoundsDetailAab;
    @FindViewById(R.id.act_rounds_detail_rg)
    private RadioGroup actRoundsDetailRg;
    @FindViewById(R.id.act_rounds_detail_medical_detail_rb)
    private RadioButton actRoundsDetailMedicalDetailRb;
    @FindViewById(R.id.act_rounds_detail_summary_rb)
    private RadioButton actRoundsDetailSummaryRb;
    @FindViewById(R.id.act_rounds_detail_video_playback_rb)
    private RadioButton actRoundsDetailVideoPlaybackRb;

    @AppApplication.Manager
    private DoctorManager mDoctorManager;

    private int mOrderId;
    private boolean mIsSuccess;
    private int mAtthosId;
    private boolean mIsRoom;
    private boolean mIsHaveAuthority;
    private OrderDetailsInfo mOrderDetailsInfo;
    private int mCurrentTab;
    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private String mHospitalName = "";
    private String mDepartmentName = "";

    public int getOrderId() {
        return mOrderId;
    }

    public int getAtthosId() {
        return mAtthosId;
    }

    public boolean getIsRoom() {
        return mIsRoom;
    }

    public boolean getIsHaveAuthority() {
        return mIsHaveAuthority;
    }

    private RadioTabFragmentHelper radioTabFragmentHelper;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mOrderId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_ID, 0);
        mIsSuccess = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_SUCCESS, false);
        mAtthosId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ATTHOS_ID, 0);
        mIsRoom = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_ROOM, false);
        mIsHaveAuthority = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_IS_COURSE, false);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_rounds_detail;
    }

    @Override
    protected void initViews() {
        actRoundsDetailAab.setLeftOnClickListener(v -> {
            if (mIsSuccess) {
                Intent intent = new Intent();
                intent.setClass(RoundsDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                finish();
            }
        });
        actRoundsDetailRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.act_rounds_detail_medical_detail_rb:
                    actRoundsDetailMedicalDetailRb.setTextSize(18);
                    actRoundsDetailSummaryRb.setTextSize(16);
                    actRoundsDetailVideoPlaybackRb.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.act_rounds_detail_summary_rb:
                    mCurrentTab = 1;
                    actRoundsDetailMedicalDetailRb.setTextSize(16);
                    actRoundsDetailSummaryRb.setTextSize(18);
                    actRoundsDetailVideoPlaybackRb.setTextSize(16);
                    break;
                case R.id.act_rounds_detail_video_playback_rb:
                    actRoundsDetailMedicalDetailRb.setTextSize(16);
                    actRoundsDetailSummaryRb.setTextSize(16);
                    actRoundsDetailVideoPlaybackRb.setTextSize(18);
                    mCurrentTab = 2;
                    break;
                default:
                    break;
            }
            if (null != radioTabFragmentHelper) {
                radioTabFragmentHelper.setFragment(mCurrentTab);
            }
        });
        initShareDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getOrderDetails(mOrderId);
    }

    private void getOrderDetails(int orderId) {
        ProgressDialog mProgressDialog = createProgressDialog(getString(R.string.loading));
        mProgressDialog.show();
        OrderDetailsRequester requester = new OrderDetailsRequester(new DefaultResultCallback<OrderDetailsInfo>() {
            @Override
            public void onSuccess(OrderDetailsInfo orderDetailsInfo, BaseResult baseResult) {
                Logger.logI(Logger.COMMON, "GetCommonlyDepartmentRequester：baseResult" + baseResult + " , orderDetailsInfo" + orderDetailsInfo);
                if (orderDetailsInfo != null) {
                    displayRoundsDetails(orderDetailsInfo);
                }
            }

            @Override
            public void onFail(BaseResult result) {
                super.onFail(result);
                ToastUtils.showShort(R.string.no_network_connection);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressDialog.dismiss();
            }
        });
        requester.orderId = orderId;
        requester.doPost();
    }

    private void displayRoundsDetails(OrderDetailsInfo orderDetailsInfo) {
        mOrderDetailsInfo = orderDetailsInfo;
        getDoctorInfo(orderDetailsInfo);
        if (StringUtils.isEmpty(orderDetailsInfo.getMedicalSummaryUrl())) {
            actRoundsDetailSummaryRb.setVisibility(View.GONE);
//            actRoundsDetailMedicalDetailRb.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
//            actRoundsDetailVideoPlaybackRb.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
        } else {
            actRoundsDetailSummaryRb.setVisibility(View.VISIBLE);
        }
        if (orderDetailsInfo.getReviewVideoInfo().size() == 0) {
            actRoundsDetailVideoPlaybackRb.setVisibility(View.GONE);
//            actRoundsDetailMedicalDetailRb.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
//            actRoundsDetailSummaryRb.setBackgroundResource(R.drawable.bg_rounds_doctor_tab);
        } else {
            actRoundsDetailVideoPlaybackRb.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(orderDetailsInfo.getMedicalSummaryUrl()) && orderDetailsInfo.getReviewVideoInfo().size() == 0) {
            actRoundsDetailRg.setVisibility(View.GONE);
        } else {
            actRoundsDetailRg.setVisibility(View.VISIBLE);
        }
        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(OrderDetailsFragment.getInstance(orderDetailsInfo));
        fragments.add(SummaryFragment.getInstance(orderDetailsInfo));
        fragments.add(VideoPlaybackFragment.getInstance(orderDetailsInfo));
        if (!Utils.activityIsDestroyed(getThisActivity())) {
            radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                    .setContainerViewId(R.id.act_rounds_detail_fl)
                    .setFragmentList(fragments)
                    .setFragmentManager(getSupportFragmentManager())
                    .setCurrentTab(mCurrentTab)
                    .build();
            radioTabFragmentHelper.initFragment();
        }
    }

    private void getDoctorInfo(OrderDetailsInfo orderDetailsInfo) {
        mDoctorManager.getHospitalInfo(orderDetailsInfo.getLaunchHospital(), true, new DoctorManager.OnHospitalInfoLoadListener() {
            @Override
            public void onSuccess(HospitalInfo hospitalInfo) {
                if (hospitalInfo != null) {
                    mHospitalName = hospitalInfo.getHospitalName();
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
        mDoctorManager.getDepartmentInfo(orderDetailsInfo.getLaunchHosdepId(), true, new DoctorManager.OnDepartmentInfoLoadListener() {
            @Override
            public void onSuccess(DepartmentInfo departmentInfo) {
                if (departmentInfo != null) {
                    mDepartmentName = departmentInfo.getDepartmentName();
                }
            }

            @Override
            public void onFailed(int code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void initShareDialog() {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        mShareDialog = new ShareDialog(this, shareItemList);
        mShareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                getShareManager().shareToWeiChat(RoundsDetailActivity.this, mShareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(RoundsDetailActivity.this, mShareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(RoundsDetailActivity.this, mShareEntity);
            }

            @Override
            public void onMyConsultClick() {

            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (mOrderDetailsInfo == null) {
                    ToastUtils.showShort("复制失败，请重试！");
                    return;
                }
                if (mCurrentTab == 0) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", mOrderDetailsInfo.getOrderShareUrl()));
                } else if (mCurrentTab == 1) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", mOrderDetailsInfo.getSummaryUrl()));
                } else if (mCurrentTab == 2) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", mOrderDetailsInfo.getReviewVideoShareUrl()));
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
    }

    public void rightClick(View view) {
        getShareContent();
        mShareDialog.show();
    }

    private void getShareContent() {
        if (mOrderDetailsInfo == null) {
            return;
        }
        if (mShareEntity == null) {
            mShareEntity = new ShareEntity();
        }
        if (mCurrentTab == 0) {
            mShareEntity.setTitle("39互联网医院查房预约信息");
            mShareEntity.setUrl(mOrderDetailsInfo.getOrderShareUrl());
        } else if (mCurrentTab == 1) {
            mShareEntity.setTitle("39互联网医院查房纪要");
            mShareEntity.setUrl(mOrderDetailsInfo.getSummaryUrl());
        } else if (mCurrentTab == 2) {
            mShareEntity.setTitle("39互联网医院就诊视频");
            mShareEntity.setUrl(mOrderDetailsInfo.getReviewVideoShareUrl());
        }
        mShareEntity.setImgUrl("");
        mShareEntity.setContent((StringUtils.isEmpty(mHospitalName) ? "" : mHospitalName) + "," + (StringUtils.isEmpty(mDepartmentName) ? "" : mDepartmentName) + "," + setThemeTv(mOrderDetailsInfo));
    }

    private String setThemeTv(OrderDetailsInfo orderDetailsInfo) {
        String str = "";
        if (orderDetailsInfo.isNeedPpt()) {
            if (orderDetailsInfo.getRoundsPatientInfos().size() == 0) {
                str = StringUtils.isEmpty(orderDetailsInfo.getOrderTitle()) ? "" : getString(R.string.rounds_teaching, orderDetailsInfo.getOrderTitle());
            } else {
                str = getString(R.string.rounds_theme_add_num, StringUtils.isEmpty(orderDetailsInfo.getOrderTitle()) ? "" : orderDetailsInfo.getOrderTitle(), orderDetailsInfo.getRoundsPatientInfos().size() + "");
            }
        } else {
            if (orderDetailsInfo.getRoundsPatientInfos().size() == 0) {
                str = "";
            } else {
                str = getString(R.string.rounds_theme_add_num_t, orderDetailsInfo.getRoundsPatientInfos().size() + "");
            }
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        if (mIsSuccess) {
            Intent intent = new Intent();
            intent.setClass(getThisActivity(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.finish();
        }
    }
}
