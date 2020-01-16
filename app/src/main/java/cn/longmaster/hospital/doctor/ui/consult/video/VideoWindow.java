package cn.longmaster.hospital.doctor.ui.consult.video;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.utils.DisplayUtil;

/**
 * 视频弹出菜单
 * <p/>
 * Created by Tengshuxiang on 2016-06-02.
 */
public class VideoWindow {
    private final String TAG = VideoWindow.class.getSimpleName();
    private Context mContext;
    private PopupWindow mPopupWindow;
    private LinearLayout mContent;

    private int mContentWidth;
    private int mContentHeight;
    private int mArrowPosition = AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_CENTER;

    public VideoWindow(Context context) {
        this(context, AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_CENTER);
    }

    public VideoWindow(Context context, int arrowPosition) {
        mContext = context;

        mArrowPosition = arrowPosition;
        LinearLayout mainRl = new LinearLayout(context);
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainRl.setOrientation(LinearLayout.VERTICAL);
        mainRl.setLayoutParams(mainParams);

        ImageView arrowIv = new ImageView(context);
        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (arrowPosition) {
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_LEFT:
                arrowParams.setMargins(DisplayUtil.dip2px(13), 0, 0, 0);
                arrowIv.setBackgroundResource(R.drawable.bg_video_room_menu_window_arrow);
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_CENTER:
                arrowParams.gravity = Gravity.CENTER_HORIZONTAL;
                arrowIv.setBackgroundResource(R.drawable.bg_video_room_menu_window_arrow);
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_RIGHT:
                arrowParams.gravity = Gravity.END;
                arrowIv.setBackgroundResource(R.drawable.bg_video_room_menu_window_arrow);
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_LEFT:
                arrowParams.setMargins(DisplayUtil.dip2px(13), 0, 0, 0);
                arrowIv.setBackgroundResource(R.drawable.bg_video_room_menu_window_arrow_reverse);
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_CENTER:
                //待扩展
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_RIGHT:
                //待扩展
                break;
        }

        mContent = new LinearLayout(context);
        mContent.setBackgroundResource(R.drawable.bg_video_room_menu_window);
        mContent.setPadding(DisplayUtil.dip2px(24f), DisplayUtil.dip2px(10f), 0, DisplayUtil.dip2px(10f));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContent.setLayoutParams(params);
        mContent.setGravity(Gravity.CENTER_VERTICAL);

        if (arrowPosition >= AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_LEFT
                && arrowPosition <= AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_RIGHT) {
            mainRl.addView(arrowIv, arrowParams);
            mainRl.addView(mContent);
        } else {
            mainRl.addView(mContent);
            mainRl.addView(arrowIv, arrowParams);
        }

        mContentHeight = ContextCompat.getDrawable(mContext, R.drawable.bg_video_room_menu_window_arrow).getIntrinsicHeight();

        mPopupWindow = new PopupWindow(mainRl, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.color_transport)));
        mPopupWindow.setOutsideTouchable(true);
    }

    public void addView(View.OnClickListener listener, int... drawableIds) {
        int id = 0;
        mContentWidth += DisplayUtil.dip2px(24f);
        mContentHeight += DisplayUtil.dip2px(10f) * 2 + ContextCompat.getDrawable(mContext, drawableIds[0]).getIntrinsicHeight();
        for (int drawableId : drawableIds) {
            ImageView view = new ImageView(mContext);
            view.setBackgroundResource(drawableId);
            mContentWidth += ContextCompat.getDrawable(mContext, drawableId).getIntrinsicWidth();
            view.setId(id);
            Log.d(TAG, "view.width: " + mContentWidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, DisplayUtil.dip2px(24f), 0);
            mContentWidth += DisplayUtil.dip2px(24f);
            mContent.addView(view, params);
            view.setOnClickListener(listener);
            id++;
        }

        if (mContentWidth < ContextCompat.getDrawable(mContext, R.drawable.bg_video_room_menu_window).getIntrinsicWidth()) {
            mContentWidth = ContextCompat.getDrawable(mContext, R.drawable.bg_video_room_menu_window).getIntrinsicWidth();
        }
    }

    public void addView(View.OnClickListener listener, List<String> titles, int... drawableIds) {
        int id = 0;
        mContentWidth += DisplayUtil.dip2px(24f);
        mContentHeight += DisplayUtil.dip2px(10f) * 2 + ContextCompat.getDrawable(mContext, drawableIds[0]).getIntrinsicHeight() + DisplayUtil.dip2px(12f);
        for (int drawableId : drawableIds) {
            TextView view = new TextView(mContext);
            view.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
            view.setText(titles.get(id));
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.font_size_10));
            view.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_white_5999c3));
            mContentWidth += ContextCompat.getDrawable(mContext, drawableId).getIntrinsicWidth();
            view.setId(id);
            Log.d(TAG, "view.width: " + mContentWidth);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (id == 1) {
                params.setMargins(0, 0, DisplayUtil.dip2px(18f), 0);
            } else {
                params.setMargins(0, 0, DisplayUtil.dip2px(24f), 0);
            }
            mContentWidth += DisplayUtil.dip2px(24f);
            mContent.addView(view, params);
            view.setOnClickListener(listener);
            id++;
        }

        if (mContentWidth < ContextCompat.getDrawable(mContext, R.drawable.bg_video_room_menu_window).getIntrinsicWidth()) {
            mContentWidth = ContextCompat.getDrawable(mContext, R.drawable.bg_video_room_menu_window).getIntrinsicWidth();
        }
    }

    public void show(View anchor) {
        int[] anchorLocation = new int[2];
        anchor.getLocationInWindow(anchorLocation);
        Log.d(TAG, "anchor x:" + anchorLocation[0] + ",y:" + anchorLocation[1]);
        switch (mArrowPosition) {
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_LEFT:
                mPopupWindow.showAsDropDown(anchor, 0, DisplayUtil.dip2px(4));
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_CENTER:
                mPopupWindow.showAtLocation(anchor, Gravity.TOP | Gravity.START, anchorLocation[0] - (mContentWidth / 2 - anchor.getWidth() / 2), anchorLocation[1] + anchor.getHeight() + DisplayUtil.dip2px(4));
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_TOP_RIGHT:
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_LEFT:
                mPopupWindow.showAtLocation(anchor, Gravity.TOP | Gravity.START, anchorLocation[0], anchorLocation[1] - mContentHeight - DisplayUtil.dip2px(4));
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_CENTER:
                break;
            case AppConstant.VideoWindowPosition.ARROW_POSITION_BOTTOM_RIGHT:
                break;
        }

    }

    /**
     * 替换窗口元素背景图
     *
     * @param index      元素索引（从0开始）
     * @param drawableId
     */
    public void replaceBackbackground(int index, int drawableId) {
        View view = mContent.getChildAt(index);
        if (view == null) {
            return;
        }
        view.setBackgroundResource(drawableId);
    }

    /**
     * 设置元素不可点击
     *
     * @param index
     */
    public void setUnClickable(int index) {
        View view = mContent.getChildAt(index);
        view.setEnabled(false);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
