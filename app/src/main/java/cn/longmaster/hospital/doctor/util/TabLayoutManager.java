package cn.longmaster.hospital.doctor.util;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Abiao
 * @version 0.01, 2018/9/13 09:55
 * @description: 底部菜单栏工具
 * @since Version0.01
 */
public class TabLayoutManager {
    private final String TAG = "TAG_" + TabLayoutManager.class.getSimpleName();
    private FragmentActivity mActivity;
    private CommonTabLayout mCommonTabLayout;
    private ArrayList<CustomTabEntity> mCustomTabEntities;
    private ArrayList<Fragment> mFragmentList;
    private List<String> mNavigateTitles;
    private int mCurrentPage;
    private int mViewContentId;

    private OnTabSelectListener mTabSelectListener;

    private TabLayoutManager(Builder builder) {
        mActivity = checkNotNull(builder.mActivity, "mActivity can not be null");
        mCommonTabLayout = checkNotNull(builder.mCommonTabLayout, "mCommonTabLayout can not be null");
        mCustomTabEntities = checkNotNull(builder.mCustomTabEntities, "mCustomTabEntities can not be null");
        mFragmentList = checkNotNull(builder.mFragmentList, "mFragmentList can not be null");
        mNavigateTitles = builder.mNavigateTitles;
        mTabSelectListener = builder.mTabSelectListener;
        mCurrentPage = builder.mCurrentPage;
        mViewContentId = builder.mViewContentId;
    }

    public void start() {
        //setTabData使用的是show and hide所以所有的fragment对于用户都是可见的
        mCommonTabLayout.setTabData(mCustomTabEntities, mActivity, mViewContentId, mFragmentList);
        mCommonTabLayout.setOnTabSelectListener(mTabSelectListener);
        mCommonTabLayout.setCurrentTab(mCurrentPage);
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
        mCommonTabLayout.setCurrentTab(currentPage);
    }

    public void show(int position, int msgNum) {
        mCommonTabLayout.showMsg(position, msgNum);
    }

    public void showDot(int position) {
        mCommonTabLayout.showDot(position);
    }

    public void hideMsg(int position) {
        mCommonTabLayout.hideMsg(position);
    }

    public static class Builder {
        FragmentActivity mActivity;
        CommonTabLayout mCommonTabLayout;
        ArrayList<CustomTabEntity> mCustomTabEntities = new ArrayList<>();
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        List<String> mNavigateTitles;
        int mCurrentPage = 0;
        int mViewContentId;
        private OnTabSelectListener mTabSelectListener;

        public Builder init(FragmentActivity activity, CommonTabLayout commonTabLayout, @IdRes int viewContentId) {
            mActivity = activity;
            mCommonTabLayout = commonTabLayout;
            mViewContentId = viewContentId;
            return this;
        }

        public Builder addTab(CustomTabEntity customTabEntity, Fragment fragment) {
            mCustomTabEntities.add(checkNotNull(customTabEntity, "customTabEntity can not null"));
            mFragmentList.add(checkNotNull(fragment, "fragment can not null"));
            return this;
        }

        public Builder addNavigateTitles(List<String> navigateTitles) {
            mNavigateTitles = navigateTitles;
            return this;
        }

        public Builder setCurrentPage(int defaultPage) {
            mCurrentPage = defaultPage;
            return this;
        }

        public Builder setOnTabSelectListener(OnTabSelectListener tabSelectListener) {
            this.mTabSelectListener = tabSelectListener;
            return this;
        }

        public TabLayoutManager build() {
            return new TabLayoutManager(this);
        }
    }
}
