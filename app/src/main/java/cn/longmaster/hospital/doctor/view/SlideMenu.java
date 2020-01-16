package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 上下滑动菜单
 * Created by Tengshuxiang on 2016-06-07.
 */
public class SlideMenu extends LinearLayout {

    private boolean mIsShown = true;
    private boolean mIsProcessing = false;
    private Scroller mScroller;
    private int mScrollLength;
    private float mStartY;
    private OnSlidePercentListener mOnSlidePercentListener;
    private int mSlideDirection = VERTICAL;

    public SlideMenu(Context context) {
        super(context);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext(), new AccelerateInterpolator());
        mIsShown = true;
        mStartY = getY();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            super.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            mIsProcessing = false;
            if (mOnSlidePercentListener != null) {
                mOnSlidePercentListener.onSlideFinish(mIsShown);
            }
        }
    }

    public void setScrollLength(int length) {
        mScrollLength = length;
    }

    @Override
    public boolean isShown() {
        return mIsShown;
    }

    public boolean isProcessing() {
        return mIsProcessing;
    }

    public void setOnSlidePercentListener(OnSlidePercentListener listener) {
        mOnSlidePercentListener = listener;
    }

    /**
     * 设置滑动方向
     *
     * @param direction 取值{@link LinearLayout#HORIZONTAL#VERTICAL}
     */
    public void setDirection(int direction) {
        mSlideDirection = direction;
    }

    public void toggle() {
        if (!mIsProcessing) {
            mIsProcessing = true;
            slide();
        }
    }

    private void slide() {
        if (mIsShown) {
            if (mSlideDirection == VERTICAL) {
                mScroller.startScroll(getScrollX(), getScrollY(), 0, -mScrollLength, 400);
            } else if (mSlideDirection == HORIZONTAL) {
                mScroller.startScroll(getScrollX(), getScrollY(), mScrollLength, 0, 400);
            }
            mIsShown = false;
            invalidate();
        } else {
            if (mSlideDirection == VERTICAL) {
                mScroller.startScroll(getScrollX(), getScrollY(), 0, mScrollLength, 400);
            } else if (mSlideDirection == HORIZONTAL) {
                mScroller.startScroll(getScrollX(), getScrollY(), -mScrollLength, 0, 400);
            }
            mIsShown = true;
            invalidate();
        }
    }

    public interface OnSlidePercentListener {
        void onSlideFinish(boolean isShow);
    }

}
