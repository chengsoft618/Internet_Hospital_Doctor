package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 首页图标按钮自定义控件
 * Created by Yang² on 2016/6/22.
 */
public class TextViewLine extends LinearLayout {

    @FindViewById(R.id.layout_icon_view_layout_ll)
    private LinearLayout layoutLl;
    @FindViewById(R.id.layout_textView_left_tv)
    private TextView mTextLeftIv;
    @FindViewById(R.id.layout_textView_right_tv)
    private TextView mTextRightTv;

    public TextViewLine(Context context) {
        this(context, null, 0);
    }

    public TextViewLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_text_view_line, this, false);
//        rootView = view;
        addView(view);
        ViewInjecter.inject(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewLine);
        int defColor = ContextCompat.getColor(context, R.color.color_white);
        float defTextSize = getResources().getDimension(R.dimen.font_size_16);
        float density = getResources().getDisplayMetrics().density;
        setLeftText(typedArray.getString(R.styleable.TextViewLine_textViewLeftText));
        setLeftTextColor(typedArray.getColor(R.styleable.TextViewLine_textViewLeftTextColor, defColor));
        mTextLeftIv.setTextSize(typedArray.getDimension(R.styleable.TextViewLine_textViewLeftTextSize, defTextSize) / density);
        setRightText(typedArray.getString(R.styleable.TextViewLine_textViewRightText));
        setRightTextColor(typedArray.getColor(R.styleable.TextViewLine_textViewRightTextColor, defColor));
        mTextRightTv.setTextSize(typedArray.getDimension(R.styleable.TextViewLine_textViewRightTextSize, defTextSize) / density);
        typedArray.recycle();

    }

    public void setLeftText(String text) {
        mTextLeftIv.setText(text);
    }

    public void setRightText(String text) {
        mTextRightTv.setText(text);
    }

    public void setLeftTextColor(int color) {
        mTextLeftIv.setTextColor(color);
    }

    public void setRightTextColor(int color) {
        mTextRightTv.setTextColor(color);
    }

}
