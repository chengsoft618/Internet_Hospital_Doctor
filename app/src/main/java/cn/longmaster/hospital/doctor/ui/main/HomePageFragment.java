package cn.longmaster.hospital.doctor.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.common.BannerAndQuickEnterInfo;
import cn.longmaster.hospital.doctor.core.entity.doctor.ServiceAuthorityInfo;
import cn.longmaster.hospital.doctor.core.entity.dutyclinic.DCProjectInfo;
import cn.longmaster.hospital.doctor.core.entity.user.ProjectListInfo;
import cn.longmaster.hospital.doctor.core.manager.common.BaseConfigManager;
import cn.longmaster.hospital.doctor.core.manager.dutyclinic.DCManager;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.DefaultResultCallback;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.config.BannerQuickEnterRequester;
import cn.longmaster.hospital.doctor.core.requests.consult.remote.ServiceAuthorityRequester;
import cn.longmaster.hospital.doctor.core.requests.user.GetProjectListRequester;
import cn.longmaster.hospital.doctor.ui.PDFViewActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.college.CollegeVideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorDetailActivity;
import cn.longmaster.hospital.doctor.ui.home.HomePageConsultFragment;
import cn.longmaster.hospital.doctor.ui.home.HomePageRoundsFragment;
import cn.longmaster.hospital.doctor.ui.home.MyPatientActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.ui.user.CourseTableActivity;
import cn.longmaster.hospital.doctor.util.GlideImageLoader;
import cn.longmaster.hospital.doctor.util.RadioTabFragmentHelper;
import cn.longmaster.hospital.doctor.view.IconView;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.zxing.CaptureActivity;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * 首页fragment
 * Created by Yang² on 2016/5/30.
 * Mod by biao on 2019/10/29
 */
public class HomePageFragment extends NewBaseFragment {
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "result";
    @FindViewById(R.id.include_home_page_title_bg_v)
    private View includeHomePageTitleBgV;
    @FindViewById(R.id.fg_home_page_title_abl)
    private AppBarLayout fgHomePageTitleAbl;
    @FindViewById(R.id.include_home_page_title_open_fl)
    private RelativeLayout includeHomePageTitleOpenFl;
    @FindViewById(R.id.include_home_page_title_open_bg_v)
    private View includeHomePageTitleOpenBgV;
    @FindViewById(R.id.include_home_page_title_open_tv)
    private TextView includeHomePageTitleOpenTv;
    @FindViewById(R.id.include_home_page_title_open_banner)
    private Banner includeHomePageTitleOpenBanner;
    @FindViewById(R.id.include_home_page_title_open_scan_iv)
    private IconView includeHomePageTitleOpenScanIv;
    @FindViewById(R.id.include_home_page_title_open_consult_iv)
    private IconView includeHomePageTitleOpenConsultIv;
    @FindViewById(R.id.include_home_page_title_open_department_construction_iv)
    private IconView includeHomePageTitleOpenDepartmentConstructionIv;
    @FindViewById(R.id.include_home_page_title_open_material_iv)
    private IconView includeHomePageTitleOpenMaterialIv;
    @FindViewById(R.id.include_home_page_title_close_ll)
    private RelativeLayout includeHomePageTitleCloseLl;
    @FindViewById(R.id.include_home_page_title_close_bg_v)
    private View includeHomePageTitleCloseBgV;
    @FindViewById(R.id.include_home_page_title_close_scan_iv)
    private ImageView includeHomePageTitleCloseScanIv;
    @FindViewById(R.id.include_home_page_title_close_consult_iv)
    private ImageView includeHomePageTitleCloseConsultIv;
    @FindViewById(R.id.include_home_page_title_close_department_construction_iv)
    private ImageView includeHomePageTitleCloseDepartmentConstructionIv;
    @FindViewById(R.id.include_home_page_title_close_material_manage_iv)
    private ImageView includeHomePageTitleCloseMaterialManageIv;
    @FindViewById(R.id.fg_home_page_consult_rg)
    private RadioGroup fgHomePageConsultRg;
    @FindViewById(R.id.fg_home_page_rounds_rb)
    private RadioButton fgHomePageRoundsRb;
    @FindViewById(R.id.fg_home_page_consultation_rb)
    private RadioButton fgHomePageConsultationRb;
    @FindViewById(R.id.fg_home_page_consult_list_tv)
    private TextView fgHomePageConsultListTv;
    @FindViewById(R.id.fg_home_page_consult_fl)
    private FrameLayout fgHomePageConsultFl;

    private Dialog mChoiceCureTypeDialog;

    private NoAuthorityFooter mNoAuthorityFooter;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;
    @AppApplication.Manager
    private BaseConfigManager mBaseConfigManager;
    @AppApplication.Manager
    private DCManager dcManager;
    private HomePageConsultFragment mHomePageConsultFragment;
    private HomePageRoundsFragment mHomePageRoundsFragment;
    private ProgressDialog mProgressDialog;
    private List<ProjectListInfo> mProjectListInfos = new ArrayList<>();
    private List<BannerAndQuickEnterInfo> mBannerAndQuickEnterInfos;
    private List<ServiceAuthorityInfo> mServiceAuthorityInfos;
    private List<Fragment> fragments = new ArrayList<>(2);
    private RadioTabFragmentHelper radioTabFragmentHelper;
    private int mCurrentTab = 0;
    private Pattern numberPattern = Pattern.compile("[0-9]*");

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void initViews(View rootView) {
        getBannerList();
        getLaunchAuthority(mUserInfoManager.getCurrentUserInfo().getUserId());
        specialtyConstruction();
        initListener();
        mHomePageRoundsFragment = new HomePageRoundsFragment();
        mHomePageConsultFragment = new HomePageConsultFragment();
        fragments.add(mHomePageRoundsFragment);
        fragments.add(mHomePageConsultFragment);
        radioTabFragmentHelper = new RadioTabFragmentHelper.Builder()
                .setContainerViewId(R.id.fg_home_page_consult_fl)
                .setFragmentList(fragments)
                .setFragmentManager(getFragmentManager())
                .setCurrentTab(mCurrentTab)
                .build();
        radioTabFragmentHelper.initFragment();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getLaunchAuthority(mUserInfoManager.getCurrentUserInfo().getUserId());
            specialtyConstruction();
        }
    }

    /**
     * 服务权限接口
     */
    private void getLaunchAuthority(int doctorId) {
        ServiceAuthorityRequester serviceAuthorityRequester = new ServiceAuthorityRequester((baseResult, serviceAuthorityInfos) -> {
            Logger.logI(Logger.COMMON, "HomePageFragment：getLaunchAuthority：" + serviceAuthorityInfos);
            if (baseResult.getCode() == OnResultListener.RESULT_SUCCESS) {
                mServiceAuthorityInfos = serviceAuthorityInfos;
                initGroupListView(mServiceAuthorityInfos);
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        serviceAuthorityRequester.setDoctorId(doctorId);
        serviceAuthorityRequester.doPost();
    }

    private void getBannerList() {
        BannerQuickEnterRequester requester = new BannerQuickEnterRequester((baseResult, bannerAndQuickEnterInfos) -> {
            mBannerAndQuickEnterInfos = bannerAndQuickEnterInfos;
            initBanner(bannerAndQuickEnterInfos);
        });
        requester.token = "0";
        requester.bannerType = 1;
        requester.doPost();
    }

    private void initBanner(List<BannerAndQuickEnterInfo> bannerAndQuickEnterInfos) {
        List<String> images = new ArrayList<>(LibCollections.size(bannerAndQuickEnterInfos));
        for (int i = 0; i < LibCollections.size(bannerAndQuickEnterInfos); i++) {
            images.add(AppConfig.getBannerDownloadUrl() + "0/" + bannerAndQuickEnterInfos.get(i).getPicturePath());
        }
        //banner设置方法全部调用完毕时最后调用
        //设置banner样式
        includeHomePageTitleOpenBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        includeHomePageTitleOpenBanner.setOffscreenPageLimit(0);
        //设置图片加载器
        includeHomePageTitleOpenBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        includeHomePageTitleOpenBanner.setImages(images);
        includeHomePageTitleOpenBanner.setBannerTitles(images);
        //设置includeHomePageTitleOpenBanner动画效果
        includeHomePageTitleOpenBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当includeHomePageTitleOpenBanner样式有显示title时）
        //设置自动轮播，默认为true
        includeHomePageTitleOpenBanner.isAutoPlay(true);
        //设置轮播时间
        includeHomePageTitleOpenBanner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        includeHomePageTitleOpenBanner.setIndicatorGravity(BannerConfig.CENTER);
        includeHomePageTitleOpenBanner.start();
    }

    /**
     * 初始化待办事件布局
     */
    private void initGroupListView(List<ServiceAuthorityInfo> serviceAuthorityInfos) {
        if (LibCollections.isNotEmpty(serviceAuthorityInfos)) {
            fgHomePageConsultFl.setVisibility(View.VISIBLE);
        } else {
            if (mNoAuthorityFooter != null) {
                return;
            }
            setNoAuthority();
        }
    }

    /**
     * 设置没有权限
     */
    private void setNoAuthority() {
        fgHomePageConsultFl.setVisibility(View.GONE);
        mNoAuthorityFooter = new NoAuthorityFooter(AppApplication.getInstance().getApplicationContext());
        mNoAuthorityFooter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, AppConfig.getNoAuthorityUrl());
            startActivity(intent);
        });
    }

    /**
     * 获取专科建设入口
     */
    private void specialtyConstruction() {
        GetProjectListRequester requester = new GetProjectListRequester((baseResult, projectListInfos) -> {
            Logger.logD(Logger.COMMON, "->specialtyConstruction()->baseResult:" + baseResult.getCode() + ", projectListInfos:" + projectListInfos);
            if (baseResult.getCode() == 0) {
                if (projectListInfos != null && projectListInfos.size() > 0) {
                    mProjectListInfos = projectListInfos;
                    includeHomePageTitleCloseDepartmentConstructionIv.setVisibility(View.VISIBLE);
                    includeHomePageTitleOpenDepartmentConstructionIv.setVisibility(View.VISIBLE);
                } else {
                    includeHomePageTitleCloseDepartmentConstructionIv.setVisibility(View.GONE);
                    includeHomePageTitleOpenDepartmentConstructionIv.setVisibility(View.GONE);
                }
            }
        });
        requester.doPost();
    }

    private void initListener() {
        fgHomePageConsultListTv.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MyPatientActivity.class));
        });
        includeHomePageTitleCloseConsultIv.setOnClickListener(v -> {
            getProjectList();
        });
        includeHomePageTitleOpenConsultIv.setOnClickListener(v -> {
            getProjectList();
        });
        includeHomePageTitleOpenScanIv.setOnClickListener(v -> {
            startCaptureActivity();
        });
        includeHomePageTitleCloseScanIv.setOnClickListener(v -> {
            startCaptureActivity();
        });
        includeHomePageTitleOpenDepartmentConstructionIv.setOnClickListener(v -> {
            startCourseTableActivity();
        });
        includeHomePageTitleCloseDepartmentConstructionIv.setOnClickListener(v -> {
            startCourseTableActivity();
        });
        includeHomePageTitleOpenMaterialIv.setOnClickListener(v -> {
            getDisplay().startPatientMaterialManagerActivity(null);
        });
        includeHomePageTitleCloseMaterialManageIv.setOnClickListener(v -> {
            getDisplay().startPatientMaterialManagerActivity(null);
        });
        includeHomePageTitleOpenBanner.setOnBannerListener(position -> {
            if (LibCollections.size(mBannerAndQuickEnterInfos) > position) {
                BannerAndQuickEnterInfo bannerAndQuickEnterInfo = mBannerAndQuickEnterInfos.get(position);
                startBannerActivity(bannerAndQuickEnterInfo);
            }
        });
        fgHomePageConsultRg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.fg_home_page_rounds_rb:
                    fgHomePageRoundsRb.setTextSize(18);
                    fgHomePageConsultationRb.setTextSize(16);
                    mCurrentTab = 0;
                    break;
                case R.id.fg_home_page_consultation_rb:
                    fgHomePageRoundsRb.setTextSize(16);
                    fgHomePageConsultationRb.setTextSize(18);
                    mCurrentTab = 1;
                    break;
                default:
                    break;
            }
            radioTabFragmentHelper.setFragment(mCurrentTab);
        });
        fgHomePageRoundsRb.setChecked(true);
        fgHomePageTitleAbl.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            //垂直方向偏移量
            int offset = Math.abs(verticalOffset);
            //最大偏移距离
            int scrollRange = appBarLayout.getTotalScrollRange();
            if (offset <= scrollRange / 2) {//当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
                includeHomePageTitleOpenFl.setVisibility(View.VISIBLE);
                includeHomePageTitleCloseLl.setVisibility(View.GONE);
                //根据偏移百分比 计算透明值
                float scale2 = (float) offset / (scrollRange / 2);
                int alpha2 = (int) (255 * scale2);
                includeHomePageTitleOpenBgV.setBackgroundColor(Color.argb(alpha2, 4, 158, 255));
                includeHomePageTitleOpenBanner.startAutoPlay();
            } else {//当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
                includeHomePageTitleOpenFl.setVisibility(View.GONE);
                includeHomePageTitleCloseLl.setVisibility(View.VISIBLE);
                float scale3 = (float) (scrollRange - offset) / (scrollRange / 2);
                int alpha3 = (int) (255 * scale3);
                includeHomePageTitleCloseBgV.setBackgroundColor(Color.argb(alpha3, 4, 158, 255));
                includeHomePageTitleOpenBanner.stopAutoPlay();
            }
            //根据偏移百分比计算扫一扫布局的透明度值
            float scale = (float) offset / scrollRange;
            int alpha = (int) (255 * scale);
            includeHomePageTitleBgV.setBackgroundColor(Color.argb(alpha, 4, 158, 255));
        });
    }

    private void startBannerActivity(BannerAndQuickEnterInfo bannerInfo) {
        if (StringUtils.isEmpty(bannerInfo.getLinkAddress())) {
            return;
        }
        Intent intent = new Intent();
        switch (bannerInfo.getLinkType()) {
            case AppConstant.LinkType.link_type_net:
                intent.setClass(getBaseActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, bannerInfo.getLinkAddress());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, getString(R.string.home_title));
                getBaseActivity().startActivity(intent);
                break;
            case AppConstant.LinkType.link_type_module:
                intent.setClass(getBaseActivity(), BrowserActivity.class);
                try {
                    JSONObject linkJson = new JSONObject(bannerInfo.getLinkAddress());
                    String args = linkJson.optString("args");
                    JSONObject argsJson = new JSONObject(args);
                    int id = argsJson.optInt("user_id", 0);
                    intent.setClass(getBaseActivity(), DoctorDetailActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, id);
                    getBaseActivity().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AppConstant.LinkType.link_type_video:
                String linkAddress = bannerInfo.getLinkAddress();
                if (numberPattern.matcher(linkAddress).matches()) {
                    int courseId = Integer.valueOf(linkAddress).intValue();
                    CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getBaseActivity(), courseId);
                    Logger.logD(Logger.COMMON, "->registerClickListener:courseId:" + courseId);
                }
                break;
            case AppConstant.LinkType.link_type_guide:
                Logger.logD(Logger.COMMON, "->registerClickListener:bannerInfo.getLinkAddress():" + bannerInfo);
                if (bannerInfo.getLinkAddress().endsWith(".pdf")) {
                    PDFViewActivity.startActivity(getBaseActivity(), bannerInfo.getLinkAddress(), bannerInfo.getPictureName());
                } else {
                    intent.setClass(getBaseActivity(), BrowserActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, bannerInfo.getLinkAddress());
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, bannerInfo.getPictureName());
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, true);
                    getBaseActivity().startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void startCourseTableActivity() {
        Intent intent = new Intent(getBaseActivity(), CourseTableActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_LIST, (Serializable) mProjectListInfos);
        startActivity(intent);
    }

    /**
     * 扫一扫
     */
    private void startCaptureActivity() {
        Intent intent = new Intent(getBaseActivity(), CaptureActivity.class);
        intent.putExtra("autoEnlarged", true);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    private void getProjectList() {
        dcManager.getProjectList(0, new DefaultResultCallback<List<DCProjectInfo>>() {
            @Override
            public void onSuccess(List<DCProjectInfo> dcProjectInfos, BaseResult baseResult) {
                if (LibCollections.isNotEmpty(dcProjectInfos)) {
                    showChoiceVisitDialog(true);
                } else {
                    showChoiceVisitDialog(false);
                }
            }
        });
    }

    /**
     * ic_home_page_consult_small
     */
    private void showChoiceVisitDialog(boolean hasDCProject) {
        if (mChoiceCureTypeDialog != null && mChoiceCureTypeDialog.isShowing()) {
            return;
        }
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_choice_visit_dialog, null);
        mChoiceCureTypeDialog = new AlertDialog.Builder(getActivity(), R.style.custom_noActionbar_window_style).create();
        mChoiceCureTypeDialog.show();
        mChoiceCureTypeDialog.setContentView(layout);
        mChoiceCureTypeDialog.setCanceledOnTouchOutside(true);

        Window win = mChoiceCureTypeDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        RelativeLayout choiceVisitDutyClinicRl = layout.findViewById(R.id.layout_choice_visit_dutyclinic_rl);
        if (hasDCProject) {
            choiceVisitDutyClinicRl.setVisibility(View.VISIBLE);
        }
        RelativeLayout choiceVisitRoundsRl = layout.findViewById(R.id.layout_choice_visit_rounds_rl);
        RelativeLayout choiceVisitConsultRl = layout.findViewById(R.id.layout_choice_visit_consult_rl);
        ImageView choiceVisitCancelIv = layout.findViewById(R.id.layout_choice_visit_cancel_iv);
        choiceVisitDutyClinicRl.setOnClickListener(v -> {
            getDisplay().startDCDoctorListActivity();
            mChoiceCureTypeDialog.dismiss();
        });
        choiceVisitRoundsRl.setOnClickListener(v -> {
            getDisplay().startRoundsMouldInfoActivity();
            mChoiceCureTypeDialog.dismiss();
        });
        choiceVisitConsultRl.setOnClickListener(v -> {
            //有首诊权限才可以发起会诊
            if (dealAuthority(AppConstant.ServiceAuthorityType.SERVICE_AUTHORITY_TYPE_ATTENDING)) {
                getDisplay().startConsultAddActivity(0, null, 0);
            }
            mChoiceCureTypeDialog.dismiss();
        });
        choiceVisitCancelIv.setOnClickListener(v -> mChoiceCureTypeDialog.dismiss());
    }

    /**
     * 测试权限
     *
     * @param serviceType 权限种类
     */
    private boolean dealAuthority(int serviceType) {
        if (mServiceAuthorityInfos == null) {
            return false;
        }
        for (ServiceAuthorityInfo serviceAuthorityInfo : mServiceAuthorityInfos) {
            if (serviceAuthorityInfo.getServiceType() == serviceType) {
                return true;
            }
        }
        new CommonDialog.Builder(getActivity())
                .setMessage(R.string.no_launch_authority)
                .setNegativeButton(R.string.confirm, () -> {
                })
                .setCanceledOnTouchOutside(false)
                .setCancelable(false).show();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.logD(Logger.COMMON, "->onActivityResult()->requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    if (data != null) {
                        String content = data.getStringExtra(DECODED_CONTENT_KEY);
                        Logger.logD(Logger.COMMON, "->onActivityResult()->content:" + content);
                        processingQRResult(content);
                    }
                    break;
                default:
                    break;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void processingQRResult(final String content) {
        showProgressDialog();
        GetProjectListRequester requester = new GetProjectListRequester((baseResult, projectListInfos) -> {
            dismissProgressDialog();
            Logger.logD(Logger.COMMON, "->specialtyConstruction()->baseResult:" + baseResult.getCode() + ", projectListInfos:" + projectListInfos);
            if (baseResult.getCode() == 0) {
                Logger.logD(Logger.COMMON, "GetProjectListRequester->" + content);
                if (LibCollections.isNotEmpty(projectListInfos)) {
                    ToastUtils.showShort(content);
                    if (isAddress(content)) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), BrowserActivity.class);
                        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, content);
                        startActivity(intent);
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int codeType = jsonObject.getInt("code_type");
                            Logger.logD(Logger.COMMON, "->onActivityResult()->codeType:" + codeType);
                            if (codeType == 0) {
                                ToastUtils.showShort("当前二维码无法在医生工作站识别");
                            } else if (codeType == 1) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), CourseTableActivity.class);
                                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PROJECT_LIST, (Serializable) mProjectListInfos);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Logger.logD(Logger.COMMON, "->onActivityResult()->e:" + e);
                            ToastUtils.showShort("当前二维码无法在医生工作站识别");
                        }
                    }
                } else {
                    ToastUtils.showShort("您不是该项目组成员，不能预约课程");
                }
            } else {
                ToastUtils.showShort(R.string.no_network_connection);
            }
        });
        requester.doPost();
    }

    /**
     * @param urls
     * @return
     */
    private boolean isAddress(String urls) {
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";//设置正则表达式
        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        return mat.matches();//判断是否匹配
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = createProgressDialog(getString(R.string.loading));
        } else if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
