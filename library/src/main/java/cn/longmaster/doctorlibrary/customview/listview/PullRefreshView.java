package cn.longmaster.doctorlibrary.customview.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 下拉刷新控件，提供了下拉刷新和加载更多两个功能：在下拉刷新中不会产生加载更多的回调，同理：加载更多时也不能下拉刷新。
 * <strong>注意：默认关闭了下拉刷新和加载更多的功能</strong><br />
 * {@link #setOnPullRefreshListener(OnPullRefreshListener)} 设置下拉刷新监听， 设置后，默认开启下拉刷新功能<br />
 * {@link #setOnLoadMoreListener(OnLoadMoreListener)} 设置加载更多监听<br />
 * <p>
 * 当产生下拉刷新回调{@link OnPullRefreshListener#onPullDownRefresh(PullRefreshView)}
 * 后：必须手动停止下拉刷新： {@link #stopPullRefresh()}。
 * </p>
 * <p>
 * 当产生加载更多回调{@link OnLoadMoreListener#onLoadMore(PullRefreshView)} 后：必须手动停止加载更多：
 * {@link #stopLoadMore()}。
 * </p>
 * <p>
 * 当数据没有更多的时候（数据全部获取完），应当关闭下拉刷新功能： {@link #setLoadMoreEnable(boolean)}设置
 * <code>false</code>， 用户重新下拉则需要重新设置下拉功能：数据有更多则开启下拉功能
 * {@link #setLoadMoreEnable(boolean)}设置 <code>true</code> ，数据没有则需要关闭下拉功能:
 * {@link #setLoadMoreEnable(boolean)}设置 <code>false</code>
 * </p>
 *
 * @author yangyong
 */
public class PullRefreshView extends ListView {
    /**
     * 动画时间
     */
    public final static int SCROLL_DURATION_DOWN = 600;
    public final static int SCROLL_DURATION_UP = 400;

    /**
     * 下拉刷新距离移动倍数
     */
    private final static float OFFSET_RADIO = 0.4f;
    private final boolean DEBUG = true;
    private PullRefreshHeader mHeaderView;
    private PullRefreshFooter mFooterView;

    private int mHeaderViewHeight;
    private Scroller mScroller;

    private OnPullRefreshListener onPullRefreshListener;
    private boolean mEnablePullRefresh = false;
    private boolean isRefreshing = false;

    /**
     * 加载更多监听器
     */
    private OnLoadMoreListener mOnLoadMoreListener;

    /**
     * 是否开启加载更多功能
     */
    private boolean mEnableLoadMore = false;

    /**
     * 是否正在加载更多
     */
    private boolean isLoadingMore = false;

    private float mLastY = -1;

    private OnScrollListener mOnScrollListener;
    private OnScrollListener mMyOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

            if (scrollState == SCROLL_STATE_IDLE) {
                // 没有在下拉刷新中，没有在加载更多中,且加载更多功能开启，滑动到最后一项
                if (!isRefreshing && !isLoadingMore && mEnableLoadMore
                        && PullRefreshView.super.getLastVisiblePosition() == PullRefreshView.super.getCount() - 1) {
                    if (mOnLoadMoreListener != null) {
                        // 加载更多
                        isLoadingMore = true;
                        mOnLoadMoreListener.onLoadMore(PullRefreshView.this);
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mOnScrollListener != null) {
                int visibleCount = visibleItemCount;
                if (PullRefreshView.super.getLastVisiblePosition() == totalItemCount - 1) {
                    visibleCount = visibleItemCount - 1;
                }
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleCount, totalItemCount - 1);
            }
        }
    };

    public PullRefreshView(Context context) {
        this(context, null);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    /**
     * 请使用 SimpleBaseAdapter.setOnAdapterItemClickListener方法代替
     * 本listView由于增加了header和footer  会造成回调的position计算障碍
     */
    @Deprecated
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 请使用adapter的getCount方法
     */
    @Override
    @Deprecated
    public int getCount() {
        return super.getCount() - 1;
    }

    @Override
    public int getLastVisiblePosition() {
        if (super.getLastVisiblePosition() == super.getCount() - 1) {
            return super.getLastVisiblePosition() - 1;
        } else {
            return super.getLastVisiblePosition();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mFooterView = new PullRefreshFooter(getContext());
        addFooterView(mFooterView);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * 设置是否开启加载更多功能。
     *
     * @param enable true：开启加载更多功能，false：关闭加载更多功能。
     */
    public void setLoadMoreEnable(boolean enable) {
        mEnableLoadMore = enable;
        if (!isRefreshing) {
            mFooterView.setEnabledLoadMore(enable);
        }
    }

    /**
     * 设置加载更多的监听器。
     * <p>
     * 加载更多回调产生，完成功能后需要调用 {@link #stopLoadMore()}方法停止加载更多，否则不会再产生下拉刷新和加载更多的回调。
     * </p>
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(@NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 设置下拉刷新监听器。
     * <p>
     * 下拉刷新回调产生，完成功能后需要调用 {@link #stopPullRefresh()}
     * 方法停止下拉刷新，否则不会再产生下拉刷新和加载更多的回调。
     * </p>
     *
     * @param onPullRefreshListener 下拉监听接口
     */
    public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
        this.onPullRefreshListener = onPullRefreshListener;
        setPullRefreshEnable(true);
    }

    /**
     * 设置是否开启下拉刷新功能
     *
     * @param enable true：开启下拉功能，false：关闭下拉功能
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) {
            mHeaderView.getContainer().setVisibility(View.INVISIBLE);
        } else {
            mHeaderView.getContainer().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 是否正在下拉刷新中
     * <p>
     * <strong>注意：当返回false的时候，有可能正在加载更多</strong>
     * </p>
     *
     * @return true：正在下拉刷新，false：没有下拉刷新
     */
    public boolean isRefreshing() {
        return isRefreshing;
    }

    /**
     * 是否正在加载更多
     * <p>
     * <strong>注意：返回false时有可能正在下拉刷新</strong>
     * </p>
     *
     * @return true：正在加载更多，false：没有加载更多。
     */
    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    /**
     * 停止加载更多
     * <p>
     * <strong>应当先设置Adapter数据后再调用该函数，否则界面会滚动</strong>
     * </p>
     */
    public void stopLoadMore() {
        if (isLoadingMore) {
            isLoadingMore = false;

            if (super.getLastVisiblePosition() == super.getCount() - 1) {
                View view = getFooterView();
                if (view != null) {
                    int top = view.getTop();
                    int bottom = getHeight() - getPaddingBottom();
                    int dy = bottom - top;
                    smoothScrollBy(-dy, SCROLL_DURATION_DOWN);
                }
            }
        } else {
            if (DEBUG) {
                new Exception("错误：当前的状态没有加载更多，不需要调用停止加载更多，请控制好数据状态！！").printStackTrace();
            }
        }
    }

    /**
     * 下拉完成,默认动画时间500毫秒
     */
    public void stopPullRefresh() {
        stopPullRefresh(SCROLL_DURATION_UP);
    }

    /**
     * 下拉完成
     *
     * @param duration 动画持续时间
     */
    public void stopPullRefresh(int duration) {
        if (isRefreshing) {
            isRefreshing = false;
            resetHeaderHeight(duration);
            mHeaderView.setState(PullRefreshHeader.STATE_INIT);

            // 回复加载更多功能
            if (mEnableLoadMore) {
                mFooterView.setEnabledLoadMore(true);
            } else if (mFooterView.isEnabledLoadMore()) {
                mFooterView.setEnabledLoadMore(false);
            }
        } else {
            if (DEBUG) {
                new Exception("错误：当前的状态不在下拉刷新中，不需要调用停止下拉刷新，请控制好数据状态！！").printStackTrace();
            }
        }
    }

    /**
     * 开始下拉
     */
    public void startPullRefresh() {
        startPullRefresh(SCROLL_DURATION_DOWN);
    }

    /**
     * 开始下拉
     *
     * @param duration 动画持续时间
     */
    public void startPullRefresh(int duration) {
        if (!mEnablePullRefresh) {
            throw new IllegalStateException("该控件已经关闭下拉刷新功能，请开启！");
        }

        if (isLoadingMore) {
            stopLoadMore();

            // 打印当前的方法调用关系
            if (DEBUG) {
                new Exception("错误：当前控件状态为正在加载更多，应停止加载更多后再调用 startPullRefresh()下拉刷新，请控制好状态").printStackTrace();
            }
        }

        if (!isRefreshing) {
            setSelection(0);
            isRefreshing = true;
            mHeaderView.setState(PullRefreshHeader.STATE_REFRESHING);
            if (mEnableLoadMore) {
                // 下拉刷新不能加载更多
                mFooterView.setEnabledLoadMore(false);
            }
            resetHeaderHeight(duration);
        } else {
            if (DEBUG) {
                new Exception("错误：当前的状态为正在下拉刷新中，不需要再下拉刷新，请控制好状态或者调用stopPullRefresh()再调用，否则不会产生下拉刷新的回调")
                        .printStackTrace();
            }
        }
    }

    private void resetHeaderHeight(int duration) {
        int finalHeight = 0;
        if (isRefreshing) {
            finalHeight = mHeaderViewHeight;
        }
        if (duration != 0) {
            startScroller(finalHeight, duration);
        } else {
            mHeaderView.setVisibleHeight(finalHeight);
        }
    }

    private void startScroller(int finalHeight, int duration) {
        int height = mHeaderView.getVisibleHeight();
        mScroller.startScroll(0, height, 0, finalHeight - height, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (isInEditMode()) {
            return;
        }

        if (mScroller.computeScrollOffset()) {
            mHeaderView.setVisibleHeight(mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @SuppressLint("NewApi")
    public void initView() {
        if (isInEditMode()) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            this.setOverScrollMode(OVER_SCROLL_NEVER);
        }
        setHeaderDividersEnabled(false);
        setFooterDividersEnabled(false);

        mScroller = new Scroller(getContext(), new LinearInterpolator());
        mHeaderView = new PullRefreshHeader(getContext());
        mHeaderViewHeight = mHeaderView.getContentHeight();
        addHeaderView(mHeaderView);
        super.setOnScrollListener(mMyOnScrollListener);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    private void updateHeaderHeight(float delta) {
        int finalHeight = (int) delta + mHeaderView.getVisibleHeight();
        mHeaderView.setVisibleHeight(finalHeight);
        setSelection(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mEnablePullRefresh && mScroller.isFinished() && !isRefreshing && !isLoadingMore) {
            if (mLastY == -1) {
                mLastY = ev.getRawY();
            }

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float deltaY = ev.getRawY() - mLastY;
                    mLastY = ev.getRawY();
                    if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                        mHeaderView.setState(PullRefreshHeader.STATE_RELEASE_REFRESH);
                        updateHeaderHeight(deltaY * OFFSET_RADIO);
                    }
                    break;
                default:
                    mLastY = -1;
                    if (getFirstVisiblePosition() == 0) {
                        if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
                            if (onPullRefreshListener != null) {
                                isRefreshing = true;
                                mHeaderView.setState(PullRefreshHeader.STATE_REFRESHING);
                                onPullRefreshListener.onPullDownRefresh(this);
                                if (mEnableLoadMore) {
                                    // 下拉刷新不能加载更多
                                    mFooterView.setEnabledLoadMore(false);
                                }
                            }
                        } else {
                            mHeaderView.setState(PullRefreshHeader.STATE_INIT);
                        }
                        resetHeaderHeight(300);
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }
}
