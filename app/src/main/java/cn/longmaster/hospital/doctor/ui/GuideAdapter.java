package cn.longmaster.hospital.doctor.ui;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 引导页适配器
 *
 * @author Tengshuxiang 2016-05-30 15:30
 */
public class GuideAdapter extends PagerAdapter {
    private Activity mContext;
    private List<View> mListPageView;

    public GuideAdapter(Activity context, List<View> pageView) {
        this.mContext = context;
        this.mListPageView = pageView;
    }

    @Override
    public int getCount() {
        return mListPageView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListPageView.get(position));
        return mListPageView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListPageView.get(position));
    }
}
