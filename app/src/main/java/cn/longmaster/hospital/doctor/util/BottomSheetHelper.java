package cn.longmaster.hospital.doctor.util;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.LibCollections;
import cn.longmaster.utils.RecyclerViewUtils;
import cn.longmaster.utils.StringUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ABiao_Abiao
 * @date 2019/7/11 20:43
 * @description:
 */
public class BottomSheetHelper {
    public static class BottomSheetListBuilder {
        private int themeResId;
        private Activity activity;
        private List<String> items;
        @DrawableRes
        private int background;
        private String title;
        @DrawableRes
        private int titleBackground;
        @ColorInt
        private int titleTextColor;
        private float titleTextSize;

        private OnItemClickListener onItemClickListener;
        @DrawableRes
        private int cancelBackground;
        @ColorInt
        private int cancelTextColor;
        private float cancelTextSize;
        @DrawableRes
        private int itemBackground;
        @ColorInt
        private int itemTextColor;
        private float itemTextSize;
        private boolean hasCancelButton;
        private @DrawableRes
        int firstItemBackground;
        private @DrawableRes
        int lastItemBackground;

        private int marginLeft;
        private int marginRight;
        private int marginTop;
        private int marginBottom;
        private int cancelMarginLeft;
        private int cancelMarginTop;
        private int cancelMarginRight;
        private int cancelMarginBottom;
        private View parent;
        private View locationView;
        private int height;

        public BottomSheetListBuilder setThemeResId(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public BottomSheetListBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public BottomSheetListBuilder addItem(String item) {
            if (null == items) {
                items = new ArrayList<>();
            }
            items.add(item);
            return this;
        }

        public BottomSheetListBuilder setItems(List<String> items) {
            this.items = items;
            return this;
        }

        public BottomSheetListBuilder setBackground(@DrawableRes int background) {
            this.background = background;
            return this;
        }

        public BottomSheetListBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BottomSheetListBuilder setTitleBackground(int titleBackground) {
            this.titleBackground = titleBackground;
            return this;
        }

        public BottomSheetListBuilder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public BottomSheetListBuilder setTitleTextSize(float titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public BottomSheetListBuilder setItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public BottomSheetListBuilder setCancelBackground(@DrawableRes int cancelBackground) {
            this.cancelBackground = cancelBackground;
            return this;
        }

        public BottomSheetListBuilder setCancelTextColor(@ColorInt int cancelTextColor) {
            this.cancelTextColor = cancelTextColor;
            return this;
        }

        public BottomSheetListBuilder setCancelTextSize(float cancelTextSize) {
            this.cancelTextSize = cancelTextSize;
            return this;
        }

        public BottomSheetListBuilder setItemBackground(@DrawableRes int itemBackground) {
            this.itemBackground = itemBackground;
            return this;
        }

        public BottomSheetListBuilder setItemTextColor(@ColorInt int itemTextColor) {
            this.itemTextColor = itemTextColor;
            return this;
        }

        public BottomSheetListBuilder setItemTextSize(float itemTextSize) {
            this.itemTextSize = itemTextSize;
            return this;
        }

        public BottomSheetListBuilder setHasCancelButton(boolean hasCancelButton) {
            this.hasCancelButton = hasCancelButton;
            return this;
        }

        public BottomSheetListBuilder setFirstItemBackground(@DrawableRes int firstItemBackground) {
            this.firstItemBackground = firstItemBackground;
            return this;
        }

        public BottomSheetListBuilder setLastItemBackground(@DrawableRes int lastItemBackground) {
            this.lastItemBackground = lastItemBackground;
            return this;
        }


        public BottomSheetListBuilder setMarginLeft(int marginLeft) {
            this.marginLeft = marginLeft;
            return this;
        }

        public BottomSheetListBuilder setMarginRight(int marginRight) {
            this.marginRight = marginRight;
            return this;
        }

        public BottomSheetListBuilder setMarginTop(int marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        public BottomSheetListBuilder setMarginBottom(int marginBottom) {
            this.marginBottom = marginBottom;
            return this;
        }

        public BottomSheetListBuilder setCancelMarginLeft(int cancelMarginLeft) {
            this.cancelMarginLeft = cancelMarginLeft;
            return this;
        }

        public BottomSheetListBuilder setCancelMarginTop(int cancelMarginTop) {
            this.cancelMarginTop = DisplayUtil.dp2px(cancelMarginTop);
            return this;
        }

        public BottomSheetListBuilder setCancelMarginRight(int cancelMarginRight) {
            this.cancelMarginRight = cancelMarginRight;
            return this;
        }

        public BottomSheetListBuilder setCancelMarginBottom(int cancelMarginBottom) {
            this.cancelMarginBottom = cancelMarginBottom;
            return this;
        }

        public BottomSheetListBuilder setParent(View parent) {
            this.parent = parent;
            return this;
        }

        public BottomSheetListBuilder setLocationView(View locationView) {
            this.locationView = locationView;
            return this;
        }

        public BottomSheetListBuilder setHeight(int height) {
            this.height = height;
            return this;
        }

        public PopupWindow build() {
            checkNotNull(activity, "Activity can not be null");
            checkNotNull(items, "item can not be null");
            checkNotNull(parent, "parent can not be null");
            View layout = LayoutInflater.from(activity).inflate(R.layout.layout_bottom_sheet_dialog, null);
            final PopupWindow mPopWindow = new PopupWindow(layout);
            mPopWindow.setFocusable(true);
            mPopWindow.setOutsideTouchable(true);
            mPopWindow.update();

            mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

            activity.getWindow().getAttributes().alpha = 0.5F;
            if (0 != height) {
                mPopWindow.setHeight(height);
            } else {
                mPopWindow.setHeight(parent.getHeight());
            }
            if (null == locationView) {
                mPopWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            } else {
                int width = parent.getMeasuredWidth();
                int height = parent.getMeasuredHeight();
                int[] location = new int[2];
                locationView.getLocationOnScreen(location);
                mPopWindow.showAtLocation(parent, Gravity.NO_GRAVITY, (location[0] + width / 2) - width / 2, location[1] - height);
            }

//            if (null != win) {
//                WindowManager.LayoutParams lp = win.getAttributes();
//                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                lp.gravity = Gravity.BOTTOM;
//                win.setAttributes(lp);
//            }
            BottomSheetAdapter bottomSheetAdapter = new BottomSheetAdapter(R.layout.item_bottom_sheet, items);
            if (0 != firstItemBackground) {
                bottomSheetAdapter.setFirstItemBackground(firstItemBackground);
            }
            if (0 != lastItemBackground) {
                bottomSheetAdapter.setLastItemBackground(lastItemBackground);
            }
            if (0 != itemBackground) {
                bottomSheetAdapter.setItemBackground(itemBackground);
            }
            if (0 != itemTextColor) {
                bottomSheetAdapter.setItemColor(itemTextColor);
            }
            if (0 != itemTextSize) {
                bottomSheetAdapter.setItemSize(itemTextSize);
            }
            RecyclerView layoutBottomSheetItemRv = (RecyclerView) layout.findViewById(R.id.layout_bottom_sheet_item_rv);
            TextView layoutBottomSheetCancelTv = (TextView) layout.findViewById(R.id.layout_bottom_sheet_cancel_tv);
            TextView layoutBottomSheetTitleTv = (TextView) layout.findViewById(R.id.layout_bottom_sheet_title_tv);
            LinearLayout layoutBottomSheetItemLl = (LinearLayout) layout.findViewById(R.id.layout_bottom_sheet_item_ll);
            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    layoutBottomSheetItemLl.setVisibility(View.GONE);
                }
            });
            layoutBottomSheetItemLl.setVisibility(View.VISIBLE);
            layoutBottomSheetItemLl.setOnClickListener(v -> {
                mPopWindow.dismiss();
            });
            if (0 != background) {
                layoutBottomSheetItemLl.setBackgroundResource(background);
            }
            if (!StringUtils.isTrimEmpty(title)) {
                if (0 != titleTextColor) {
                    layoutBottomSheetTitleTv.setTextColor(titleTextColor);
                }
                if (0 != titleTextSize) {
                    layoutBottomSheetTitleTv.setTextSize(titleTextSize);
                }
                if (0 != titleBackground) {
                    layoutBottomSheetTitleTv.setBackgroundResource(titleBackground);
                }
                layoutBottomSheetTitleTv.setVisibility(View.VISIBLE);
                layoutBottomSheetTitleTv.setText(title);
            } else {
                layoutBottomSheetTitleTv.setVisibility(View.GONE);
            }
            if (hasCancelButton) {
                if (0 != cancelTextColor) {
                    layoutBottomSheetCancelTv.setTextColor(cancelTextColor);
                }
                if (0 != cancelTextSize) {
                    layoutBottomSheetCancelTv.setTextSize(cancelTextSize);
                }
                if (0 != cancelBackground) {
                    layoutBottomSheetCancelTv.setBackgroundResource(cancelBackground);
                }
                layoutBottomSheetCancelTv.setVisibility(View.VISIBLE);
            } else {
                layoutBottomSheetCancelTv.setVisibility(View.GONE);
            }

            layoutBottomSheetItemRv.setLayoutManager(RecyclerViewUtils.getVerLinearLayoutManager(activity));
            layoutBottomSheetItemRv.setAdapter(bottomSheetAdapter);

            //layoutBottomSheetItemRv.addItemDecoration(RecyclerViewUtils.getRecyclerViewDiv());
            //
            layoutBottomSheetItemRv.getLayoutParams().height = DisplayUtil.dp2px(4 * 56);
            //
            LinearLayout.LayoutParams cancelLayoutParams = (LinearLayout.LayoutParams) layoutBottomSheetCancelTv.getLayoutParams();
            cancelLayoutParams.setMargins(cancelMarginLeft, cancelMarginTop, cancelMarginRight, cancelMarginBottom);
            layoutBottomSheetCancelTv.setLayoutParams(cancelLayoutParams);

//
            FrameLayout.LayoutParams llLayoutParams = (FrameLayout.LayoutParams) layoutBottomSheetItemLl.getLayoutParams();
            llLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
            layoutBottomSheetItemLl.setLayoutParams(llLayoutParams);

            bottomSheetAdapter.setOnItemClickListener((adapter, view, position) -> onItemClickListener.onItemClick(mPopWindow, adapter, view, position));
            layoutBottomSheetCancelTv.setOnClickListener(v -> mPopWindow.dismiss());

            return mPopWindow;
        }
    }

    private static class BottomSheetAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private @ColorInt
        int itemColor;
        private float itemSize;
        private @DrawableRes
        int itemBackground;
        @DrawableRes
        private int firstItemBackground;
        @DrawableRes
        private int lastItemBackground;

        public BottomSheetAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        public int getItemColor() {
            return itemColor;
        }

        public void setItemColor(int itemColor) {
            this.itemColor = itemColor;
        }

        public float getItemSize() {
            return itemSize;
        }

        public void setItemSize(float itemSize) {
            this.itemSize = itemSize;
        }

        public int getItemBackground() {
            return itemBackground;
        }

        public void setItemBackground(int itemBackground) {
            this.itemBackground = itemBackground;
        }

        public int getFirstItemBackground() {
            return firstItemBackground;
        }

        public void setFirstItemBackground(int firstItemBackground) {
            this.firstItemBackground = firstItemBackground;
        }

        public int getLastItemBackground() {
            return lastItemBackground;
        }

        public void setLastItemBackground(int lastItemBackground) {
            this.lastItemBackground = lastItemBackground;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int size = LibCollections.size(getData());
            TextView itemTv = helper.getView(R.id.item_bottom_sheet);
            if (helper.getLayoutPosition() == 0) {
                if (0 != firstItemBackground) {
                    helper.setBackgroundRes(R.id.item_bottom_sheet, firstItemBackground);
                }
            } else if (helper.getLayoutPosition() == size - 1) {
                if (0 != lastItemBackground) {
                    helper.setBackgroundRes(R.id.item_bottom_sheet, lastItemBackground);
                }
            } else {
                if (0 != itemBackground) {
                    itemTv.setBackgroundResource(itemBackground);
                }
            }
            if (0 != itemColor) {
                helper.setTextColor(R.id.item_bottom_sheet, itemColor);
            }
            if (0 != itemSize) {
                itemTv.setTextSize(itemSize);
            }

            helper.setText(R.id.item_bottom_sheet, item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PopupWindow popupWindow, BaseQuickAdapter adapter, View view, int position);
    }
}
