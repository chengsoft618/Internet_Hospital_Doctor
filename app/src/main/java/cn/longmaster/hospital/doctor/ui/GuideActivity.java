package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.anim.DepthPageTransformer;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConfig;
import cn.longmaster.hospital.doctor.core.AppPreference;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfo;
import cn.longmaster.hospital.doctor.core.manager.user.UserInfoManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.main.MainActivity;
import cn.longmaster.utils.SPUtils;

/**
 * 引导页
 */
public class GuideActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_guide_vp)
    private ViewPager mViewPager;
    @FindViewById(R.id.activity_guide_index_ll)
    private LinearLayout mIndexLl;
    @AppApplication.Manager
    private UserInfoManager mUserInfoManager;

    //存储引导页的所有View
    private List<View> mGuidePages = new ArrayList<>();
    //引导页所有View的ID
    private int[] mGuideView = new int[]{R.layout.guide_view_page1,
            R.layout.guide_view_page2,
            R.layout.guide_view_page3,
            R.layout.guide_view_page4};
    //上一次位置
    private int mLastPosition;
    private UserInfo mUserInfo;

    @Override
    protected void initDatas() {
        mUserInfo = mUserInfoManager.getCurrentUserInfo();
        SPUtils.getInstance().put(AppPreference.KEY_GUIDE_PAGE_CURRENT_VERSION, AppConfig.CLIENT_VERSION);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initViews() {
        for (int resId : mGuideView) {
            mGuidePages.add(getLayoutInflater().inflate(resId, null));
        }
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(new GuideAdapter(this, mGuidePages));
        regListener();
    }

    @OnClick({R.id.activity_guide_skip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_guide_skip:
                if (mUserInfo.getUserId() == 0) {
                    startActivity(new Intent(getThisActivity(), LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getThisActivity(), MainActivity.class));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 注册监听
     */
    private void regListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexLl.getChildAt(position).setBackgroundResource(R.drawable.bg_o_solid_45aef8_7x7);
                if (position != mLastPosition) {
                    mIndexLl.getChildAt(mLastPosition).setBackgroundResource(R.drawable.bg_o_solid_cccccc_7x7);
                    mLastPosition = position;
                }
                if (position == mGuideView.length - 1) {
                    mIndexLl.setVisibility(View.GONE);
                } else {
                    mIndexLl.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mUserInfo.getUserId() == 0) {
            mGuidePages.get(mGuidePages.size() - 1).findViewById(R.id.guide_view_page4_enter).setOnClickListener(v -> {
                startActivity(new Intent(getThisActivity(), LoginActivity.class));
                finish();
            });
        } else {
            mGuidePages.get(mGuidePages.size() - 1).findViewById(R.id.guide_view_page4_enter).setOnClickListener(v -> startActivity(new Intent(getThisActivity(), MainActivity.class)));
        }
    }
}
