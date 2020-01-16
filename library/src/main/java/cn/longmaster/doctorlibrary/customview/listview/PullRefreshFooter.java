package cn.longmaster.doctorlibrary.customview.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import cn.longmaster.doctorlibrary.R;

/**
 * 加载更多 视图
 *
 * @author yangyong
 */
public class PullRefreshFooter extends FrameLayout {
    private boolean isEnable = false;
    private View mFooter;

    @SuppressLint("InflateParams")
    public PullRefreshFooter(Context context) {
        super(context);
        mFooter = LayoutInflater.from(context).inflate(R.layout.refresh_listview_footer, null);
        addView(mFooter);
        setEnabledLoadMore(isEnable);
    }

    public boolean isEnabledLoadMore() {
        return isEnable;
    }

    public void setEnabledLoadMore(boolean isEnable) {
        this.isEnable = isEnable;
        mFooter.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }
}
