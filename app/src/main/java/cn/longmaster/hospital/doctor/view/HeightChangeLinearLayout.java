package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 监听大小改变的Layout
 * Created by YY on 2017/08/16.
 */
public class HeightChangeLinearLayout extends LinearLayout {
    private OnHeightChangeListener onHeightChangeListener;

    public HeightChangeLinearLayout(Context context) {
        this(context, null);
    }

    public HeightChangeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeightChangeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (onHeightChangeListener != null) {
                    onHeightChangeListener.onHeightChange(bottom - top, oldBottom - oldTop);
                }
            }
        });
    }

    public void setOnHeightChangeListener(OnHeightChangeListener onHeightChangeListener) {
        this.onHeightChangeListener = onHeightChangeListener;
    }

    public interface OnHeightChangeListener {
        void onHeightChange(int newHeight, int oldHeight);
    }
}
