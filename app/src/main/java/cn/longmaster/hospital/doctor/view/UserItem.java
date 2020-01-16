package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.InjectOnClickListener;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 个人中心自定义控件
 * Created by Yang² on 2016/5/31.
 */
public class UserItem extends LinearLayout {

    public static final int FUNCTION_LEFT_ICON = 1;
    public static final int FUNCTION_TITLE = 2;
    public static final int FUNCTION_INFO_TEXT = 4;
    public static final int FUNCTION_RIGHT_ARROW = 8;

    private int titleBarFunction;

    private View rootView;

    @FindViewById(R.id.title_bar_left_icon)
    private ImageView leftIcon;
    @FindViewById(R.id.title_bar_title)
    private TextView itemTitle;
    @FindViewById(R.id.title_bar_info_text)
    private TextView infoText;
    @FindViewById(R.id.title_bar_right_arrow)
    private ImageView rightArrow;
    @FindViewById(R.id.title_bar_red_point)
    private View redPoint;


    public UserItem(Context context) {
        this(context, null, 0);
    }

    public UserItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_user_item, this, false);
        rootView = view;
        addView(view);
        ViewInjecter.inject(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UserItem);

        setTitle(typedArray.getString(R.styleable.UserItem_itemText));
        int defTitleColor = ContextCompat.getColor(context, R.color.black);
        setTitleColor(typedArray.getColor(R.styleable.UserItem_itemColor, defTitleColor));

        int defRightTextColor = ContextCompat.getColor(context, R.color.black);
        setRightText(typedArray.getString(R.styleable.UserItem_infoText));
        setRightTextColor(typedArray.getColor(R.styleable.UserItem_infoTextColor, defRightTextColor));

        setLeftIconImageDrawable(typedArray.getDrawable(R.styleable.UserItem_leftIconImage));
        titleBarFunction = typedArray.getInt(R.styleable.UserItem_title_function, FUNCTION_LEFT_ICON | FUNCTION_TITLE | FUNCTION_INFO_TEXT | FUNCTION_RIGHT_ARROW);
        setUserItemFunction(titleBarFunction);
        setShowRedPoint(false);

        String titleClick = typedArray.getString(R.styleable.UserItem_itemClick);
        if (!TextUtils.isEmpty(titleClick)) {
            try {
                Method method = context.getClass().getMethod(titleClick, View.class);
                setTitleBarOnClickListener(new InjectOnClickListener(method, context));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        typedArray.recycle();

    }

    public void setTitle(String title) {
        itemTitle.setText(title);
    }

    public void setTitleColor(@ColorInt int color) {
        itemTitle.setTextColor(color);
    }

    public void setRightText(String text) {
        infoText.setText(text);
    }

    public void setRightTextColor(@ColorInt int color) {
        infoText.setTextColor(color);
    }

    public void setLeftIconImageDrawable(Drawable drawable) {
        leftIcon.setImageDrawable(drawable);
    }

    public void setUserItemFunction(int function) {
        this.titleBarFunction = function;

        leftIcon.setVisibility((function & FUNCTION_LEFT_ICON) == FUNCTION_LEFT_ICON ? VISIBLE : GONE);
        itemTitle.setVisibility((function & FUNCTION_TITLE) == FUNCTION_TITLE ? VISIBLE : GONE);
        infoText.setVisibility((function & FUNCTION_INFO_TEXT) == FUNCTION_INFO_TEXT ? VISIBLE : GONE);
        rightArrow.setVisibility((function & FUNCTION_RIGHT_ARROW) == FUNCTION_RIGHT_ARROW ? VISIBLE : GONE);
    }

    public void setTitleBarOnClickListener(OnClickListener onClickListener) {
        rootView.setOnClickListener(onClickListener);
    }

    public void setShowRedPoint(boolean isShowReadPoint) {
        if (isShowReadPoint) {
            redPoint.setVisibility(VISIBLE);
        } else {
            redPoint.setVisibility(GONE);
        }
    }

}
