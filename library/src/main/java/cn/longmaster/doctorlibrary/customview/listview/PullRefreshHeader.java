package cn.longmaster.doctorlibrary.customview.listview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import cn.longmaster.doctorlibrary.R;

/**
 * 下拉的顶部显示视图
 *
 * @author yangyong
 */
public class PullRefreshHeader extends LinearLayout {
    /**
     * 正常状态
     */
    public static final int STATE_INIT = 0;

    /**
     * 即将下拉刷新
     */
    public static final int STATE_RELEASE_REFRESH = 1;

    /**
     * 正在刷新
     */
    public static final int STATE_REFRESHING = 2;

    private View mContainer;

    private View refreshIcon;

    private int mState = STATE_INIT;

    public PullRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public View getContainer() {
        return mContainer;
    }

    @SuppressLint("InflateParams")
    private void initView() {
        mContainer = LayoutInflater.from(getContext()).inflate(R.layout.refresh_listview_header, null);
        refreshIcon = mContainer.findViewById(R.id.pull_refresh_icon);
        setGravity(Gravity.BOTTOM);

        this.addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
    }

    public void setState(int state) {
        mState = state;
        switch (state) {
            case STATE_INIT:
                break;
            case STATE_RELEASE_REFRESH:
                float currentRotation = (((float) (getVisibleHeight() % getContentHeight())) / getContentHeight());
                refreshIcon.setRotation(360 * currentRotation);
                break;
            case STATE_REFRESHING:
                startAnimator(refreshIcon.getRotation());
                break;
            default:
        }

        mState = state;
    }

    private void startAnimator(float start) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, 360);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                refreshIcon.setRotation((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mState != STATE_REFRESHING) {
                    return;
                }
                mContainer.post(new Runnable() {
                    @Override
                    public void run() {
                        startAnimator(0);
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration((int) (900 * ((float) (360 - start) / 360)));
        valueAnimator.start();
    }

    public int getContentHeight() {
        return dipToPx(60) + 1;
    }

    /**
     * DIP 转 PX
     *
     * @param dip dip单位
     * @return 转换后的像素
     */
    public int dipToPx(float dip) {
        return (int) (getResources().getDisplayMetrics().density * dip + 0.5f);
    }

    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }
}
