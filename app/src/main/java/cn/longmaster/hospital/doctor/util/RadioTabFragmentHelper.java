package cn.longmaster.hospital.doctor.util;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ABiao_Abiao
 * @date 2019/7/1 9:48
 * @description: 通过RadioButton实现Fragment的Tab效果帮助类
 */
public class RadioTabFragmentHelper {
    private List<Fragment> fragmentList;
    @IdRes
    private int containerViewId;
    private FragmentManager fragmentManager;
    private int mCurrentTab;

    private RadioTabFragmentHelper(Builder builder) {
        this.fragmentManager = checkNotNull(builder.fragmentManager, "fragmentManager can not be null");
        this.fragmentList = checkNotNull(builder.fragmentList, "fragmentList can not be null");
        this.containerViewId = builder.containerViewId;
        this.mCurrentTab = builder.mCurrentTab;
    }

    public void initFragment() {
        for (Fragment fragment : fragmentList) {
            if (null != fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName())) {
                fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName())).commitAllowingStateLoss();
            }
        }
        for (Fragment fragment : fragmentList) {
            fragmentManager.beginTransaction().add(containerViewId, fragment, fragment.getClass().getSimpleName()).hide(fragment).commitAllowingStateLoss();
        }
        setFragment(mCurrentTab);
    }

    public void setFragment(int currentTab) {
        for (int i = 0; i < fragmentList.size(); i++) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            Fragment fragment = fragmentList.get(i);
            if (i == currentTab) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentTab = currentTab;
    }

    public void clear() {

    }

    public static class Builder {
        private List<Fragment> fragmentList;
        @IdRes
        private int containerViewId;
        private FragmentManager fragmentManager;
        private int mCurrentTab;

        public Builder() {

        }

        public Builder setCurrentTab(int currentTab) {
            this.mCurrentTab = currentTab;
            return this;
        }

        public Builder setFragmentList(List<Fragment> fragmentList) {
            this.fragmentList = fragmentList;
            return this;
        }

        public Builder setContainerViewId(int containerViewId) {
            this.containerViewId = containerViewId;
            return this;
        }

        public Builder setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            return this;
        }

        public RadioTabFragmentHelper build() {
            return new RadioTabFragmentHelper(this);
        }
    }
}
