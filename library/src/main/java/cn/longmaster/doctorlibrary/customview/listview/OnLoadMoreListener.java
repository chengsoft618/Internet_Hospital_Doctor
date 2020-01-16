package cn.longmaster.doctorlibrary.customview.listview;

/**
 * 加载更多监听器
 *
 * @author yangyong
 */
public interface OnLoadMoreListener {
    /**
     * 产生加载更多事件的回调
     *
     * @param pullRefreshView
     */
    public void onLoadMore(PullRefreshView pullRefreshView);
}
