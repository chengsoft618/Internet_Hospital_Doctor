package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 首页图标按钮自定义控件
 * Created by Yang² on 2016/6/22.
 */
public class IconViewHor extends LinearLayout {

    @FindViewById(R.id.layout_icon_view_layout_ll)
    private LinearLayout layoutLl;
    @FindViewById(R.id.layout_icon_view_left_icon_iv)
    private ImageView mLeftIconIv;
    @FindViewById(R.id.layout_icon_view_right_text_tv)
    private TextView mRightTextTv;

    public IconViewHor(Context context) {
        this(context, null, 0);
    }

    public IconViewHor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconViewHor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_icon_view_hor, this, false);
//        rootView = view;
        addView(view);
        ViewInjecter.inject(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconViewHor);
        float density = getResources().getDisplayMetrics().density;
        int gravity = typedArray.getInt(R.styleable.IconViewHor_viewGravity, Gravity.CENTER_VERTICAL);
        layoutLl.setGravity(gravity);
        setLeftIcon(typedArray.getDrawable(R.styleable.IconViewHor_leftIconImg));
        setRightText(typedArray.getString(R.styleable.IconViewHor_viewText));
        int defColor = ContextCompat.getColor(context, R.color.color_white);
        mRightTextTv.setTextColor(typedArray.getColor(R.styleable.IconViewHor_viewTextColor, defColor));
        float defTextSize = getResources().getDimension(R.dimen.font_size_18);
        mRightTextTv.setTextSize(typedArray.getDimension(R.styleable.IconViewHor_viewTextSize, defTextSize) / density);
        mRightTextTv.setPadding((int) (typedArray.getDimension(R.styleable.IconViewHor_textPaddingLeft, 0)), 0, 0, 0);
        typedArray.recycle();

    }

    public void setRightText(String text) {
        mRightTextTv.setText(text);
    }

    public void setLeftIcon(Drawable drawable) {
        mLeftIconIv.setImageDrawable(drawable);
    }

}
