package cn.longmaster.doctorlibrary.customview.listview;


/**
 * 下拉的监听接口
 *
 * @author yangyong
 */
public interface OnPullRefreshListener {
    /**
     * 下拉刷新时调用
     *
     * @param pullRefreshView
     */
    public void onPullDownRefresh(PullRefreshView pullRefreshView);
}
