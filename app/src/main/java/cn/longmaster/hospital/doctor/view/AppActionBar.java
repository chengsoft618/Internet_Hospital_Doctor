package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.InjectOnClickListener;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 程序actionbar
 * Created by yangyong on 2016/4/20.
 */
public class AppActionBar extends RelativeLayout {
    public static final int FUNCTION_LEFT_BTN = 1;
    public static final int FUNCTION_TITLE = 2;
    public static final int FUNCTION_RIGHT_TEXT = 4;
    public static final int FUNCTION_RIGHT_SECOND_TEXT = 32;
    public static final int FUNCTION_LEFT_TEXT = 16;
    public static final int FUNCTION_RIGHT_BTN = 8;

    private int function;

    private View rootView;

    @FindViewById(R.id.action_bar_status_bar)
    private View statusBar;

    @FindViewById(R.id.action_bar_left_btn)
    private ImageView leftBtn;

    @FindViewById(R.id.action_bar_right_btn)
    private ImageView rightBtn;

    @FindViewById(R.id.action_bar_title)
    private TextView titleView;

    @FindViewById(R.id.action_bar_left_text)
    private TextView leftText;
    @FindViewById(R.id.action_bar_right_text)
    private TextView rightText;
    @FindViewById(R.id.action_bar_right_second_text)
    private TextView rightSecondText;
    @FindViewById(R.id.action_bar_progress_bar)
    private ProgressBar progressBar;

    public AppActionBar(Context context) {
        this(context, null, 0);
    }

    public AppActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_action_bar, this, false);
        rootView = view;
        addView(view);
        ViewInjecter.inject(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppActionBar);
        int defColor;
        if (isInEditMode()) {
            defColor = ContextCompat.getColor(context, R.color.color_app_main_color);
        } else {
            defColor = ContextCompat.getColor(context, R.color.color_app_main_color);
        }

        setActionBarBackground(typedArray.getColor(R.styleable.AppActionBar_barBackground, defColor));
        setTitle(typedArray.getString(R.styleable.AppActionBar_titleText), typedArray.getInt(R.styleable.AppActionBar_titileTextLimit, 0));
        int defTitleColor = ContextCompat.getColor(context, R.color.white);
        setTitleColor(typedArray.getColor(R.styleable.AppActionBar_titleColor, defTitleColor));

        setLeftText(typedArray.getString(R.styleable.AppActionBar_leftText));
        setRightText(typedArray.getString(R.styleable.AppActionBar_rightText));
        setRightSecondText(typedArray.getString(R.styleable.AppActionBar_rightSecondText));
        if (typedArray.getColorStateList(R.styleable.AppActionBar_leftTextColor) != null) {
            setLeftTextColor(typedArray.getColorStateList(R.styleable.AppActionBar_leftTextColor));
        } else {
            setLeftTextColor(typedArray.getColor(R.styleable.AppActionBar_leftTextColor, defTitleColor));
        }

        if (typedArray.getColorStateList(R.styleable.AppActionBar_rightTextColor) != null) {
            setRightTextColor(typedArray.getColorStateList(R.styleable.AppActionBar_rightTextColor));
        } else {
            setRightTextColor(typedArray.getColor(R.styleable.AppActionBar_rightTextColor, defTitleColor));
        }

        if (typedArray.getColorStateList(R.styleable.AppActionBar_rightSecondTextColor) != null) {
            setRightSecondTextColor(typedArray.getColorStateList(R.styleable.AppActionBar_rightSecondTextColor));
        } else {
            setRightSecondTextColor(typedArray.getColor(R.styleable.AppActionBar_rightSecondTextColor, defTitleColor));
        }
        setRightBtnImageDrawable(typedArray.getDrawable(R.styleable.AppActionBar_rightBtnImage));

        setStatusBarColor(typedArray.getColor(R.styleable.AppActionBar_statusBarColor, Color.TRANSPARENT));

        Drawable leftBtnImageDrawable = typedArray.getDrawable(R.styleable.AppActionBar_leftBtnImage);
        if (leftBtnImageDrawable == null) {
            leftBtnImageDrawable = ContextCompat.getDrawable(context, R.mipmap.ic_actionbar_back_white);
        }
        leftBtn.setImageDrawable(leftBtnImageDrawable);

        showStatusBar(typedArray.getBoolean(R.styleable.AppActionBar_showStatusBar, true));

        int function = typedArray.getInt(R.styleable.AppActionBar_function, FUNCTION_LEFT_BTN | FUNCTION_TITLE);
        setFunction(function);

        String backClick = typedArray.getString(R.styleable.AppActionBar_leftClick);
        if (TextUtils.isEmpty(backClick)) {
            backClick = "onBackClick";
        }
        setLeftOnClickListener(new InjectOnClickListener(backClick, context));

        String titleClick = typedArray.getString(R.styleable.AppActionBar_titleClick);
        if (!TextUtils.isEmpty(titleClick)) {
            setTitleOnClickListener(new InjectOnClickListener(titleClick, context));
        }

        String leftClick = typedArray.getString(R.styleable.AppActionBar_leftTextClick);
        if (!TextUtils.isEmpty(leftClick)) {
            setLeftTextOnClickListener(new InjectOnClickListener(leftClick, context));
        }
        String rightClick = typedArray.getString(R.styleable.AppActionBar_rightTextClick);
        if (!TextUtils.isEmpty(rightClick)) {
            setRightTextOnClickListener(new InjectOnClickListener(rightClick, context));
        }
        String rightSecondClick = typedArray.getString(R.styleable.AppActionBar_rightSecondTextClick);
        if (!TextUtils.isEmpty(rightSecondClick)) {
            setRightSecondTextOnClickListener(new InjectOnClickListener(rightSecondClick, context));
        }

        String rightBtnClick = typedArray.getString(R.styleable.AppActionBar_rightBtnClick);
        if (!TextUtils.isEmpty(rightBtnClick)) {
            setRightBtnOnClickListener(new InjectOnClickListener(rightBtnClick, context));
        }

        typedArray.recycle();
    }

    public void setStatusBarColor(@ColorInt int color) {
        statusBar.setBackgroundColor(color);
    }

    public void setActionBarBackground(@ColorInt int color) {
        rootView.setBackgroundColor(color);
    }

    public void setTitle(String title) {
        setTitle(title, 0);
    }

    public void setTitle(String title, int textLimit) {
        if (textLimit > 0) {
            if (null != title && title.length() > textLimit) {
                title = title.substring(0, textLimit);
                title += "...";
            }
        }
        titleView.setText(title);
    }

    public void setTitleSize(int unit, float size) {
        titleView.setTextSize(unit, size);
    }

    public void setTitleColor(@ColorInt int color) {
        titleView.setTextColor(color);
    }

    public void setRightTextColor(@ColorInt int color) {
        rightText.setTextColor(color);
    }

    public void setRightTextColor(ColorStateList colorStateList) {
        rightText.setTextColor(colorStateList);
    }

    public void setRightSecondTextColor(@ColorInt int color) {
        rightSecondText.setTextColor(color);
    }

    public void setRightSecondTextColor(ColorStateList colorStateList) {
        rightSecondText.setTextColor(colorStateList);
    }

    public void setLeftTextColor(@ColorInt int color) {
        leftText.setTextColor(color);
    }

    public void setLeftTextColor(ColorStateList colorStateList) {
        leftText.setTextColor(colorStateList);
    }

    public void showStatusBar(boolean enable) {
        statusBar.setVisibility(enable ? VISIBLE : GONE);
    }

    public void setFunction(int function) {
        this.function = function;
        leftBtn.setVisibility((function & FUNCTION_LEFT_BTN) == FUNCTION_LEFT_BTN ? VISIBLE : GONE);
        titleView.setVisibility((function & FUNCTION_TITLE) == FUNCTION_TITLE ? VISIBLE : GONE);
        leftText.setVisibility((function & FUNCTION_LEFT_TEXT) == FUNCTION_LEFT_TEXT ? VISIBLE : GONE);
        rightText.setVisibility((function & FUNCTION_RIGHT_TEXT) == FUNCTION_RIGHT_TEXT ? VISIBLE : GONE);
        rightSecondText.setVisibility((function & FUNCTION_RIGHT_SECOND_TEXT) == FUNCTION_RIGHT_SECOND_TEXT ? VISIBLE : GONE);
        rightBtn.setVisibility((function & FUNCTION_RIGHT_BTN) == FUNCTION_RIGHT_BTN ? VISIBLE : GONE);
        progressBar.setVisibility(GONE);
    }

    public void addFunction(int function) {
        setFunction(this.function | function);
    }

    public void removeFunction(int function) {
        setFunction(this.function & ~function);
    }

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        leftBtn.setOnClickListener(onClickListener);
    }

    public void setRightBtnOnClickListener(OnClickListener onClickListener) {
        rightBtn.setOnClickListener(onClickListener);
    }

    public void setLeftTextOnClickListener(OnClickListener onClickListener) {
        leftText.setOnClickListener(onClickListener);
    }

    public void setRightTextOnClickListener(OnClickListener onClickListener) {
        rightText.setOnClickListener(onClickListener);
    }

    public void setRightSecondTextOnClickListener(OnClickListener onClickListener) {
        rightSecondText.setOnClickListener(onClickListener);
    }

    public void setRightBtnImageDrawable(Drawable drawable) {
        rightBtn.setImageDrawable(drawable);
    }

    public void setLeftText(String text) {
        leftText.setText(text);
    }

    public void setRightText(String text) {
        rightText.setText(text);
    }

    public void setRightSecondText(String text) {
        rightSecondText.setText(text);
    }

    public void setProgressBarVisibility(boolean visibility) {
        progressBar.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setTitleOnClickListener(OnClickListener onClickListener) {
        titleView.setOnClickListener(onClickListener);
    }
}
