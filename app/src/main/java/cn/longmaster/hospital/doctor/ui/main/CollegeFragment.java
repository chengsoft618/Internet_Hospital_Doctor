package cn.longmaster.hospital.doctor.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.college.ClassConfigInfo;
import cn.longmaster.hospital.doctor.core.entity.common.BannerAndQuickEnterInfo;
import cn.longmaster.hospital.doctor.core.manager.common.BaseConfigManager;
import cn.longmaster.hospital.doctor.core.requests.BaseResult;
import cn.longmaster.hospital.doctor.core.requests.OnResultListener;
import cn.longmaster.hospital.doctor.core.requests.OpTypeConfig;
import cn.longmaster.hospital.doctor.core.requests.college.GetClassifyConfigRequester;
import cn.longmaster.hospital.doctor.core.requests.config.RequestParams;
import cn.longmaster.hospital.doctor.ui.PDFViewActivity;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.hospital.doctor.ui.college.CollegeVideoPlayerActivity;
import cn.longmaster.hospital.doctor.ui.college.GuideLiteratureFragment;
import cn.longmaster.hospital.doctor.ui.college.VideoLabelFragment;
import cn.longmaster.hospital.doctor.ui.college.VideoListFragment;
import cn.longmaster.hospital.doctor.ui.college.adapter.MyPagerAdapter;
import cn.longmaster.hospital.doctor.ui.doctor.DoctorDetailActivity;
import cn.longmaster.hospital.doctor.ui.user.BrowserActivity;
import cn.longmaster.hospital.doctor.util.GlideImageLoader;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.StringUtils;

/**
 * 医学院fragment
 * <p>
 * Created by W·H·K on 2018/3/23.
 */

public class CollegeFragment extends NewBaseFragment {
    @FindViewById(R.id.fragment_college_view_pager)
    private ViewPager mViewPager;
    @FindViewById(R.id.fragment_college_tab)
    private TabLayout mTabLayout;
    @FindViewById(R.id.fragment_college_banner_avp)
    private Banner fragmentCollegeBannerAvp;
    private Pattern pattern = Pattern.compile("[0-9]*");
    private ProgressDialog mProgressDialog;
    private List<BannerAndQuickEnterInfo> mBanners = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_college;
    }

    @Override
    public void initViews(View rootView) {
        initListener();
        getBannerFromDB();
        getClassifyConfig();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getBannerFromDB();
        getClassifyConfig();
    }

    private void initListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                Logger.logD(Logger.COMMON, "->initLinster:state:" + state);
            }
        });
        fragmentCollegeBannerAvp.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (LibCollections.size(mBanners) > position) {
                    bannerItemClick(mBanners.get(position));
                }
            }
        });
    }

    /**
     * 从数据库获取banner数据
     */
    public void getBannerFromDB() {
        if (mBanners.size() == 0) {
            showProgressDialog();
            requesterBanner("0");
        }
    }

    /**
     * 更新banner数据
     *
     * @param banners banner列表
     */
    private void initBanner(List<BannerAndQuickEnterInfo> banners) {
        if (!LibCollections.isEmpty(banners)) {
            List<String> images = new ArrayList<>(LibCollections.size(banners));
            for (int i = 0; i < LibCollections.size(banners); i++) {
                images.add(AppConfig.getBannerDownloadUrl() + "0/" + banners.get(i).getPicturePath());
            }
            //banner设置方法全部调用完毕时最后调用
            //设置banner样式
            fragmentCollegeBannerAvp.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            fragmentCollegeBannerAvp.setImageLoader(new GlideImageLoader());
            //设置图片集合
            fragmentCollegeBannerAvp.setImages(images);
            fragmentCollegeBannerAvp.setBannerTitles(images);
            //设置fragmentCollegeBannerAvp动画效果
            fragmentCollegeBannerAvp.setBannerAnimation(Transformer.Default);
            //设置标题集合（当fragmentCollegeBannerAvp样式有显示title时）
            //设置自动轮播，默认为true
            fragmentCollegeBannerAvp.isAutoPlay(true);
            //设置轮播时间
            fragmentCollegeBannerAvp.setDelayTime(2500);
            //设置指示器位置（当banner模式中有指示器时）
            fragmentCollegeBannerAvp.setIndicatorGravity(BannerConfig.CENTER);
            fragmentCollegeBannerAvp.start();
        }
    }

    private void bannerItemClick(BannerAndQuickEnterInfo bannerInfo) {
        if (StringUtils.isEmpty(bannerInfo.getLinkAddress())) {
            return;
        }
        Intent intent = new Intent();
        int linkType = bannerInfo.getLinkType();
        if (linkType == AppConstant.LinkType.link_type_net) {
            intent.setClass(getActivity(), BrowserActivity.class);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, bannerInfo.getLinkAddress());
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, getActivity().getString(R.string.home_title));
            startActivity(intent);
        } else if (linkType == AppConstant.LinkType.link_type_module) {
            intent.setClass(getActivity(), BrowserActivity.class);
            try {
                JSONObject linkJson = new JSONObject(bannerInfo.getLinkAddress());
                String args = linkJson.optString("args");
                JSONObject argsJson = new JSONObject(args);
                int id = argsJson.optInt("user_id", 0);
                intent.setClass(getActivity(), DoctorDetailActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DOCTOR_ID, id);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (linkType == AppConstant.LinkType.link_type_video) {
            String linkAddress = bannerInfo.getLinkAddress();
            if (pattern.matcher(linkAddress).matches()) {
                int courseId = Integer.valueOf(linkAddress).intValue();
                CollegeVideoPlayerActivity.startCollegeVideoPlayerActivity(getActivity(), courseId);
                Logger.logD(Logger.COMMON, "->registerClickListener:courseId:" + courseId);
            }
        } else if (linkType == AppConstant.LinkType.link_type_guide) {
            Logger.logD(Logger.COMMON, "->registerClickListener:bannerInfo.getLinkAddress():" + bannerInfo);
            if (bannerInfo.getLinkAddress().endsWith(".pdf")) {
                PDFViewActivity.startActivity(getActivity(), bannerInfo.getLinkAddress(), bannerInfo.getPictureName());
            } else {
                intent.setClass(getActivity(), BrowserActivity.class);
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL, bannerInfo.getLinkAddress());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE, bannerInfo.getPictureName());
                intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, true);
                startActivity(intent);
            }
        }
    }

    /**
     * 服务器请求banner数据
     */
    private void requesterBanner(String token) {
        RequestParams params = new RequestParams();
        params.setBannerType(4);
        params.setType(OpTypeConfig.CLIENTAPI_OPTYE_BANNER_AND_QUICK_ENTRY);
        params.setToken(token);
        AppApplication.getInstance().getManager(BaseConfigManager.class).getBaseConfigFromNet(params, new OnResultListener() {
            @Override
            public void onResult(BaseResult baseResult, Object o) {
                dismissProgressDialog();
                Logger.logD(Logger.COMMON, "->requesterBanner:Object:" + o);
                if (baseResult.getCode() == RESULT_SUCCESS && o != null) {
                    mBanners = (ArrayList<BannerAndQuickEnterInfo>) o;
                    initBanner(mBanners);
                }
            }
        });
    }

    private void getClassifyConfig() {
        GetClassifyConfigRequester requester = new GetClassifyConfigRequester(new OnResultListener<List<ClassConfigInfo>>() {
            @Override
            public void onResult(BaseResult baseResult, List<ClassConfigInfo> classConfigInfos) {
                Logger.logD(Logger.COMMON, "CollegeFragment->getClassifyConfig()->baseResult:" + baseResult + ", classConfigInfos:" + classConfigInfos);
                if (baseResult.getCode() == RESULT_SUCCESS && classConfigInfos.size() > 0) {
                    displayTabLayout(classConfigInfos);
                }
            }
        });
        requester.doPost();
    }

    private void displayTabLayout(final List<ClassConfigInfo> classConfigInfos) {
        List<String> tabList = new ArrayList<>();
        for (ClassConfigInfo classConfigInfo : classConfigInfos) {
            tabList.add(classConfigInfo.getModuleTitle());
        }
        mViewPager.removeAllViewsInLayout();
        final List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < classConfigInfos.size(); i++) {
            ClassConfigInfo classConfigInfo = classConfigInfos.get(i);
            if (null == classConfigInfo) {
                continue;
            }
            if (classConfigInfo.getModuleType() == AppConstant.CollegeModularType.COLLEGE_MODULAR_TYPE_GUIDE) {
                mFragments.add(GuideLiteratureFragment.newInstance(classConfigInfo.getModuleTitle()));
            } else if (classConfigInfo.getModuleType() == AppConstant.CollegeModularType.COLLEGE_MODULAR_TYPE_VIDEO) {
                mFragments.add(VideoListFragment.newInstance(classConfigInfo));
            } else {
                mFragments.add(VideoLabelFragment.newInstance(classConfigInfo));
            }
        }
        MyPagerAdapter mMyPagerAdapter = new MyPagerAdapter(getFragmentManager(), tabList, mFragments);
        mViewPager.setAdapter(mMyPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 设置tablayout的下划线宽度跟tab内容一致
     *
     * @param tabLayout
     */
    public void reflex(final TabLayout tabLayout) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                    int dp10 = DisplayUtil.dip2px(10);
                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void showProgressDialog() {
        if (isAdded()) {
            if (mProgressDialog == null) {
                mProgressDialog = createProgressDialog(getString(R.string.loading));
            } else if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    private void dismissProgressDialog() {
        if (isAdded()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}
