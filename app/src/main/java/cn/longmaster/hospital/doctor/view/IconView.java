package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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
public class IconView extends LinearLayout {

    @FindViewById(R.id.layout_icon_view_top_icon_iv)
    private ImageView mTopIconIv;

    @FindViewById(R.id.layout_icon_view_bottom_text_tv)
    private TextView mBottomTextTv;

    public IconView(Context context) {
        this(context, null, 0);
    }

    public IconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_icon_view, this, false);
        addView(view);
        ViewInjecter.inject(this, view);
        float density = getResources().getDisplayMetrics().density;


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconView);
        mTopIconIv.setImageDrawable(typedArray.getDrawable(R.styleable.IconView_topIconImage));
        mBottomTextTv.setText(typedArray.getString(R.styleable.IconView_bottomText));
        int defColor = ContextCompat.getColor(context, R.color.color_white);
        mBottomTextTv.setTextColor(typedArray.getColor(R.styleable.IconView_bottomTextColor, defColor));
        float defTextSize = getResources().getDimension(R.dimen.font_size_14);
        mBottomTextTv.setTextSize(typedArray.getDimension(R.styleable.IconView_bottomTextSize, defTextSize) / density);
        mBottomTextTv.setPadding(0, (int) (typedArray.getDimension(R.styleable.IconView_textPaddingTop, 0)), 0, 0);
        typedArray.recycle();

    }

    public ImageView getTopIconIv() {
        return mTopIconIv;
    }

    public TextView getBottomTextTv() {
        return mBottomTextTv;
    }
}
