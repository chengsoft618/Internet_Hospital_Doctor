package cn.longmaster.hospital.doctor.ui.doctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.TabEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.entity.doctor.DoctorBaseInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.ServiceAuthorityInfo;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.core.manager.user.AuthenticationManager;
import cn.longmaster.hospital.doctor.core.manager.user.DoctorManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.consult.remote.ServiceAuthorityRequester;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.HistoryConsultActivity;
import cn.longmaster.hospital.doctor.util.AvatarUtils;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.util.TabLayoutManager;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.SPUtils;
import cn.longmaster.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 医生详情
 * Created by Yang² on 2016/6/12.
 */
public class DoctorDetailActivity extends NewBaseActivity {
    private final int REQUEST_CODE_FOR_CHOOSE_ROUNDS_DOCTOR = 100;
    private final int REQUEST_CODE_FOR_START_ROUNDS = 200;
    @FindViewById(R.id.activity_doctor_detail_content_abl)
    private AppBarLayout activityDoctorDetailContentAbl;
    @FindViewById(R.id.activity_doctor_detail_tab_fl)
    private FrameLayout activityDoctorDetailTabFl;
    @FindViewById(R.id.activity_doctor_detail_title_rl)
    private RelativeLayout activityDoctorDetailTitleRl;
    @FindViewById(R.id.activity_doctor_detail_content_rl)
    private RelativeLayout activityDoctorDetailContentRl;
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.activity_doctor_detail_avatar_civ)
    private CircleImageView activityDoctorDetailAvatarCiv;
    @FindViewById(R.id.activity_doctor_detail_name_tv)
    private TextView activityDoctorDetailNameTv;
    @FindViewById(R.id.activity_doctor_hospital_name_tv)
    private TextView activityDoctorHospitalNameTv;
    @FindViewById(R.id.activity_doctor_department_name_tv)
    private TextView activityDoctorDepartmentNameTv;
    @FindViewById(R.id.activity_doctor_level_name_tv)
    private TextView activityDoctorLevelNameTv;
    @FindViewById(R.id.activity_doctor_detail_is_check_in_tv)
    private TextView activityDoctorDetailIsCheckInTv;
    @FindViewById(R.id.activity_doctor_detail_is_recommend_iv)
    private ImageView activityDoctorDetailIsRecommendIv;
    @FindViewById(R.id.activity_doctor_modify_iv)
    private ImageView activityDoctorModifyIv;
    @FindViewById(R.id.activity_doctor_share_iv)
    private ImageView activityDoctorShareIv;
    @FindViewById(R.id.activity_doctor_detail_ctl)
    private CommonTabLayout activityDoctorDetailCtl;
    @FindViewById(R.id.activity_doctor_detail_fl)
    private FrameLayout activityDoctorDetailFl;
    @FindViewById(R.id.activity_doctor_detail_launch_consult_btn)
    private Button activityDoctorDetailLaunchConsultBtn;
    private int mDoctorId;
    private DoctorBaseInfo mDoctorBaseInfo;//医生基本信息，用于传入升窗，减少升窗逻辑

    private boolean mIsHaveAuthority = false;//医生是否有权限
    private boolean isChooseDoctor = false;
    //是否发起查房选医生否则是会诊
    private boolean isForRounds = false;
    @AppApplication.Manager
    private DoctorManager mDoctorManager;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private AuthenticationManager mAuthenticationManager;
    private int mCurrentTab = 0;
    private DoctorDetailInfoFragment doctorDetailInfoFragment;
    private DoctorExpertInfoFragment doctorExpertInfoFragment;
    private TabLayoutManager tabLayoutManager;
    //医生标题是否展开
    private boolean isTitleExpand;

    @Override
    protected void initDatas() {
        isChooseDoctor = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TO_CHECK_IS_CHOOSE_DOCTOR, false);
        isForRounds = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_IS_ROUNDS, false);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_doctor_detail;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText("医生详情");
        if (!isEntering()) {
            activityDoctorDetailIsCheckInTv.setVisibility(View.GONE);
            activityDoctorDetailLaunchConsultBtn.setVisibility(View.GONE);
        } else {
            activityDoctorDetailIsCheckInTv.setVisibility(View.VISIBLE);
            activityDoctorDetailLaunchConsultBtn.setVisibility(View.VISIBLE);
        }
        if (isChooseDoctor) {
            activityDoctorDetailLaunchConsultBtn.setText("选择医生");
        } else {
            activityDoctorDetailLaunchConsultBtn.setText("立即预约");
        }
        initListener();
        if (mUserInfoManager.getCurrentUserInfo().getUserId() == getDoctorId()) {
            activityDoctorModifyIv.setVisibility(View.VISIBLE);
        } else {
            activityDoctorModifyIv.setVisibility(View.GONE);
        }
        getDoctorInfo(getDoctorId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (null == data) {
                return;
            }
            int doctorId = data.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, 0);
            ArrayList<String> intentionTime = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST);
            switch (requestCode) {
                case REQUEST_CODE_FOR_CHOOSE_ROUNDS_DOCTOR:
                    finishWithResult(doctorId, intentionTime);
                    break;
                case REQUEST_CODE_FOR_START_ROUNDS:
                    getDisplay().startRoundsMouldInfoActivity(doctorId, intentionTime, null, null, false, null, 0);
                    break;
                default:
                    break;
            }
        }
    }

    private OnTabSelectListener getOnTabSelectListener() {
        return new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        };
    }

    private void initFragments(DoctorBaseInfo doctorBaseInfo) {
        doctorDetailInfoFragment = DoctorDetailInfoFragment.getInstance(doctorBaseInfo);
        doctorExpertInfoFragment = DoctorExpertInfoFragment.getInstance(doctorBaseInfo);
        tabLayoutManager = new TabLayoutManager.Builder()
                .init(getThisActivity(), activityDoctorDetailCtl, R.id.activity_doctor_detail_fl)
                .addTab(new TabEntity("基本信息"), doctorDetailInfoFragment)
                .addTab(new TabEntity("专家风采"), doctorExpertInfoFragment)
                .setOnTabSelectListener(getOnTabSelectListener())
                .build();
        tabLayoutManager.start();
        activityDoctorShareIv.setVisibility(View.VISIBLE);
    }

    private void initListener() {
        activityDoctorDetailContentAbl.addOnOffsetChangedListener((appBarLayout, i) -> {
            isTitleExpand = i == 0;
        });
        activityDoctorDetailLaunchConsultBtn.setOnClickListener(view -> {
            if (isChooseDoctor) {
                if (mUserInfoManager.getCurrentUserInfo().getUserId() != getDoctorId()) {
                    if (isForRounds) {
                        getDisplay().startSelectionTimeActivity(getDoctorId(), false, null, REQUEST_CODE_FOR_CHOOSE_ROUNDS_DOCTOR);
                    } else {
                        getServiceAuthority(getDoctorId());
                    }
                } else {
                    ToastUtils.showShort(R.string.rounds_info_not_to_oneself);
                }
            } else {
                showChoiceVisitDialog();
            }
        });
        activityDoctorShareIv.setOnClickListener(view -> {
            if (null != mDoctorBaseInfo) {
                createShareDialog(createShareContent(mDoctorBaseInfo)).show();
            }
        });
        activityDoctorModifyIv.setOnClickListener(view -> {
            if (mDoctorBaseInfo != null) {
                createModDoctorUrl(getDoctorId());
            }
        });

        ivToolBarBack.setOnClickListener(view -> onBackPressed());
    }

    /**
     * ic_home_page_consult_small
     */
    private void showChoiceVisitDialog() {
        View layout = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_choice_visit_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(getThisActivity(), R.style.custom_noActionbar_window_style).create();
        dialog.show();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);

        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        RelativeLayout choiceVisitRoundsRl = layout.findViewById(R.id.layout_choice_visit_rounds_rl);
        RelativeLayout layoutChoiceVisitDutyclinicRl = layout.findViewById(R.id.layout_choice_visit_dutyclinic_rl);
        layoutChoiceVisitDutyclinicRl.setVisibility(View.GONE);
        RelativeLayout choiceVisitConsultRl = layout.findViewById(R.id.layout_choice_visit_consult_rl);
        ImageView choiceVisitCancelIv = layout.findViewById(R.id.layout_choice_visit_cancel_iv);
        choiceVisitRoundsRl.setOnClickListener(v -> {
            if (mUserInfoManager.getCurrentUserInfo().getUserId() == getDoctorId()) {
                ToastUtils.showShort(R.string.rounds_info_not_to_oneself);
            } else {
                if (getDoctorId() > 0) {
                    getDisplay().startSelectionTimeActivity(getDoctorId(), false, null, REQUEST_CODE_FOR_START_ROUNDS);
                } else {
                    finishWithResult(getDoctorId(), null);
                }
            }
            dialog.dismiss();
        });
        choiceVisitConsultRl.setOnClickListener(v -> {
            if (mUserInfoManager.getCurrentUserInfo().getUserId() == getDoctorId()) {
                ToastUtils.showShort(R.string.doctor_detail_not_to_self_initiate_consultation);
            } else {
                getServiceAuthority(getDoctorId());
            }
            dialog.dismiss();
        });
        choiceVisitCancelIv.setOnClickListener(v -> dialog.dismiss());
    }

    /**
     * 获取医生服务权限
     */

    private void finishWithResult(int doctorId, ArrayList<String> timeList) {
        Intent intent = new Intent();
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_DOCTOR_ID, doctorId);
        if (LibCollections.isNotEmpty(timeList)) {
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ROUNDS_INTENTION_TIME_LIST, timeList);
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private ShareDialog createShareDialog(ShareEntity shareEntity) {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_MY_CONSULT, R.drawable.ic_share_my_consult, getString(R.string.share_my_consult)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QR_CODE, R.drawable.ic_share_qr_code, getString(R.string.share_qr_code)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_SAVE_IMG, R.drawable.ic_share_save_img, getString(R.string.share_save_img)));
        ShareDialog shareDialog = new ShareDialog(this, shareItemList);
        shareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                shareEntity.setImgUrl(SdManager.getInstance().getAppointAvatarFilePath(mDoctorBaseInfo.getUserId() + ""));
                getShareManager().shareToWeiChat(DoctorDetailActivity.this, shareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                shareEntity.setImgUrl(SdManager.getInstance().getAppointAvatarFilePath(mDoctorBaseInfo.getUserId() + ""));
                getShareManager().shareToWeiCircle(DoctorDetailActivity.this, shareEntity);
            }

            @Override
            public void onQqClick() {
                shareEntity.setImgUrl(AvatarUtils.getAvatar(false, mDoctorBaseInfo.getUserId(), mDoctorBaseInfo.getAvaterToken()));
                getShareManager().shareToQq(DoctorDetailActivity.this, shareEntity);
            }

            @Override
            public void onMyConsultClick() {
                Intent intent = new Intent(DoctorDetailActivity.this, HistoryConsultActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SHARE, true);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_BASE_INFO, mDoctorBaseInfo);
                startActivity(intent);
            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (null != cm) {
                    cm.setPrimaryClip(ClipData.newPlainText("Label", mDoctorBaseInfo.getWebDoctorUrl()));
                    ToastUtils.showShort(R.string.share_link_copied);
                }
            }

            @Override
            public void onQrCodeClick() {
                DoctorQRCodeActivity.startDoctorQRCodeActivity(DoctorDetailActivity.this, mDoctorBaseInfo);
            }

            @Override
            public void onSaveImgClick() {
                Bitmap bitmapTitle = BitmapUtils.shotView(activityDoctorDetailTitleRl);
                if (isTitleExpand) {
                    Bitmap content = BitmapUtils.shotView(activityDoctorDetailContentAbl);
                    bitmapTitle = BitmapUtils.mergeBitmap_TB(bitmapTitle, content, false);
                }
                Bitmap contentTab = BitmapUtils.shotView(activityDoctorDetailTabFl);
                bitmapTitle = BitmapUtils.mergeBitmap_TB(bitmapTitle, contentTab, false);
                final Bitmap bitmapTop = bitmapTitle;

                if (mCurrentTab == 0) {
                    if (null != doctorDetailInfoFragment) {
                        doctorDetailInfoFragment.startShotView(bitmap -> {
                            String path = savePictureToLocal(BitmapUtils.mergeBitmap_TB(bitmapTop, bitmap, false));
                            ToastUtils.showShort("图片已经保存到：" + path);
                        });
                    }
                } else {
                    if (null != doctorExpertInfoFragment) {
                        doctorExpertInfoFragment.startShotView(bitmap -> {
                            String path = savePictureToLocal(BitmapUtils.mergeBitmap_TB(bitmapTop, bitmap, false));
                            ToastUtils.showShort("图片已经保存到：" + path);
                        });
                    }
                }
            }
        });
        return shareDialog;
    }

    private ShareEntity createShareContent(DoctorBaseInfo doctorBaseInfo) {
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.setTitle((TextUtils.isEmpty(doctorBaseInfo.getRealName()) ? "" : doctorBaseInfo.getRealName())
                + "-" + (TextUtils.isEmpty(doctorBaseInfo.getDoctorLevel()) ? "" : doctorBaseInfo.getDoctorLevel()));
        shareEntity.setContent((TextUtils.isEmpty(doctorBaseInfo.getHospitalName()) ? "" : doctorBaseInfo.getHospitalName())
                + "\t" + (TextUtils.isEmpty(doctorBaseInfo.getDepartmentName()) ? "" : doctorBaseInfo.getDepartmentName()));
        shareEntity.setUrl(doctorBaseInfo.getWebDoctorUrl());
        return shareEntity;
    }

    /**
     * 拉取医生信息
     */
    private void getDoctorInfo(int doctorId) {
        mDoctorManager.getDoctorInfo(doctorId, new DoctorManager.OnDoctorInfoLoadListener() {
            @Override
            public void onSuccess(DoctorBaseInfo doctorBaseInfo) {
                mDoctorBaseInfo = doctorBaseInfo;
                initFragments(doctorBaseInfo);
                if (isEntering()) {
                    populateDoctorInfo(doctorBaseInfo);
                } else {
                    populateNoEnteringDoctorInfo(doctorBaseInfo);
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

    private void populateDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        GlideUtils.showDoctorAvatar(activityDoctorDetailAvatarCiv, this, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
        activityDoctorDetailNameTv.setText(doctorBaseInfo.getRealName());
        activityDoctorLevelNameTv.setText(doctorBaseInfo.getDoctorLevel());
        activityDoctorDepartmentNameTv.setText(doctorBaseInfo.getDepartmentName());
        activityDoctorHospitalNameTv.setText(doctorBaseInfo.getHospitalName());
        if (doctorBaseInfo.isMonthRecommend()) {
            activityDoctorDetailIsRecommendIv.setVisibility(View.VISIBLE);
        } else {
            activityDoctorDetailIsRecommendIv.setVisibility(View.GONE);
        }
    }

    private void populateNoEnteringDoctorInfo(DoctorBaseInfo doctorBaseInfo) {
        GlideUtils.showDoctorAvatar(activityDoctorDetailAvatarCiv, this, AvatarUtils.getAvatar(false, doctorBaseInfo.getUserId(), doctorBaseInfo.getAvaterToken()));
        activityDoctorDetailNameTv.setText(doctorBaseInfo.getRealName());
        activityDoctorLevelNameTv.setText(doctorBaseInfo.getDoctorLevel());
        activityDoctorDepartmentNameTv.setText(doctorBaseInfo.getDepartmentName());
        activityDoctorHospitalNameTv.setText(doctorBaseInfo.getHospitalName());
        if (doctorBaseInfo.isMonthRecommend()) {
            activityDoctorDetailIsRecommendIv.setVisibility(View.VISIBLE);
        } else {
            activityDoctorDetailIsRecommendIv.setVisibility(View.GONE);
        }
    }

    private void showAuthorityDialog() {
        new CommonDialog.Builder(DoctorDetailActivity.this)
                .setMessage(R.string.doctor_authority_dialog_message)
                .setPositiveButton(R.string.doctor_authority_dialog_ok, () -> {

                })
                .show();
    }

    /**
     * 获取医生服务权限
     */
    private void getServiceAuthority(int doctorID) {
        ServiceAuthorityRequester requester = new ServiceAuthorityRequester((baseResult, serviceAuthorityInfos) -> {
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                if (null == serviceAuthorityInfos) {
                    showAuthorityDialog();
                    return;
                }
                for (ServiceAuthorityInfo serviceAuthorityInfo : serviceAuthorityInfos) {
                    if (serviceAuthorityInfo.getServiceType() == AppConstant.SchedulingType.SCHEDULING_TYPE_REMOTE_CONSULT) {
                        mIsHaveAuthority = true;
                        break;
                    }
                }
                if (mIsHaveAuthority) {
                    getDisplay().startConsultAddActivity(getDoctorId(), null, 0);
                } else {
                    showAuthorityDialog();
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.setDoctorId(doctorID);
        requester.doPost();
    }

    /**
     * 获取医生ID
     */
    private int getDoctorId() {
        if (0 == mDoctorId) {
            mDoctorId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, 0);
        }
        return mDoctorId;
    }

    /**
     * 医生是否入住
     */
    private boolean isEntering() {
        return getDoctorId() >= 0;
    }

    /**
     * 创建修改医生链接地址
     *
     * @param doctorId 医生ID
     * @return
     */
    private void createModDoctorUrl(int doctorId) {
        getAuth(new AuthenticationManager.GetAuthenticationListener() {
            @Override
            public void onGetAuthentication() {
                StringBuilder urlStringBuilder = new StringBuilder(AppConfig.getAdwsUrl());
                urlStringBuilder.append("Doctor/edit_info/?doc_id=");
                urlStringBuilder.append(doctorId);
                urlStringBuilder.append("&user_id=");
                urlStringBuilder.append(mUserInfoManager.getCurrentUserInfo().getUserId());
                urlStringBuilder.append("&c_auth=");
                urlStringBuilder.append(SPUtils.getInstance().getString(AppPreference.KEY_AUTHENTICATION_AUTH, ""));
                getDisplay().startBrowserActivity(urlStringBuilder.toString(), mDoctorBaseInfo.getRealName(), true, false, 0, 0);
            }

            @Override
            public void onTimeOut() {

            }
        });
    }

    /**
     * 获取鉴权值
     *
     * @param getAuthenticationListener
     */
    private void getAuth(AuthenticationManager.GetAuthenticationListener getAuthenticationListener) {
        mAuthenticationManager.getAuthenticationInfo(new AuthenticationManager.GetAuthenticationListener() {
            @Override
            public void onGetAuthentication() {
                getAuthenticationListener.onGetAuthentication();
            }

            @Override
            public void onTimeOut() {
                getAuthenticationListener.onTimeOut();
            }
        });
    }

    public String savePictureToLocal(Bitmap bitmap) {
        String bitPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
        if (TextUtils.isEmpty(bitPath)) {
            return "";
        }
        Uri uuUri = Uri.parse(bitPath);
        String path = FileUtil.getRealPathFromURI(this, uuUri);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        sendBroadcast(intent);
        return path;
    }
}
