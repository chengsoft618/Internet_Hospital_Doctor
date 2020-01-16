package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.longmaster.hospital.doctor.R;

/**
 * 自定义布局
 * Created by Yang² on 2016/7/12.
 *
 * @description 格子布局(类似4.0中的gridlayout)
 */
public class MyGridLayout extends ViewGroup {
    private final String TAG = "MyGridLayout";

    int marginHor = 2;// 每个格子的水平和垂直间隔
    int marginVer = 2;// 每个格子的水平和垂直间隔
    int colums = 2;
    int childCount = 0;

    int childWidth = 0;
    int childHeight = 0;
    GridAdapter adapter;

    public MyGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.MyGridLayout);
            colums = a.getInteger(R.styleable.MyGridLayout_numColumns, 2);
            marginHor = (int) a.getDimension(R.styleable.MyGridLayout_itemMarginHor, 2);
            marginVer = (int) a.getDimension(R.styleable.MyGridLayout_itemMarginVer, 2);
        }
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridLayout(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        childWidth = (measureWidth - (colums - 1) * marginHor) / colums;
        childHeight = childWidth;

        childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            int widthSpec = 0;
            int heightSpec = 0;

            LayoutParams lParams = (LayoutParams) child.getLayoutParams();
            lParams.left = (i % colums) * (childWidth + marginHor);
            lParams.top = (i / colums) * (childWidth + marginVer);
            lParams.width = childWidth;
            lParams.height = childHeight;
            widthSpec = MeasureSpec.makeMeasureSpec(lParams.width, MeasureSpec.EXACTLY);
            heightSpec = MeasureSpec.makeMeasureSpec(lParams.height, MeasureSpec.EXACTLY);
            child.measure(widthSpec, heightSpec);

        }

        int vw = measureWidth;
        int vh = measureHeight;
        if (childCount < colums) {
            vw = childCount * (childWidth + marginHor);
        }
        if (childCount % colums == 0) {
            vh = (childCount / colums) * (childWidth + marginVer) - marginVer;
        } else {
            vh = ((childCount + colums) / colums) * (childWidth + marginVer) - marginVer;
        }

        setMeasuredDimension(vw, vh);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams lParams = (LayoutParams) child.getLayoutParams();
            child.layout(lParams.left, lParams.top, lParams.left + childWidth,
                    lParams.top + childHeight);
        }

    }

    public interface GridAdapter {
        View getView(int index);

        int getCount();
    }

    /**
     * 设置适配器
     */
    public void setGridAdapter(GridAdapter adapter) {
        this.adapter = adapter;
        // 动态添加视图
        int size = adapter.getCount();
        for (int i = 0; i < size; i++) {
            addView(adapter.getView(i));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(final OnItemClickListener click) {
        if (this.adapter == null) {
            return;
        }
        for (int i = 0; i < adapter.getCount(); i++) {
            final int index = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    click.onItemClick(v, index);
                }
            });
        }
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public int left = 0;
        public int top = 0;

        public LayoutParams(Context arg0, AttributeSet arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(int arg0, int arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(ViewGroup.LayoutParams arg0) {
            super(arg0);
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new MyGridLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MyGridLayout.LayoutParams;
    }
}