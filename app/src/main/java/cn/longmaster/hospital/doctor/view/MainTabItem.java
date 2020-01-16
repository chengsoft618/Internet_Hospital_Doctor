package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * tab 底部栏
 * Created by yangyong on 16/5/30.
 */
public class MainTabItem extends FrameLayout {

    @FindViewById(R.id.main_tab_image)
    private ImageView imageView;

    @FindViewById(R.id.main_tab_text)
    private TextView textView;

    @FindViewById(R.id.main_tab_red_point)
    private ImageView readPoint;

    private Drawable normalDrawable;
    private Drawable selectedDrawable;
    private boolean tabIsSelected = false;

    public MainTabItem(Context context) {
        this(context, null);
    }

    public MainTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View rootView = LayoutInflater.from(context).inflate(R.layout.view_main_tab_item, this, false);
        ViewInjecter.inject(this, rootView);
        addView(rootView);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainTabItem);

        setIsSelected(typedArray.getBoolean(R.styleable.MainTabItem_isSelected, false));
        setText(typedArray.getString(R.styleable.MainTabItem_tabText));
        setSelectedDrawable(typedArray.getDrawable(R.styleable.MainTabItem_selectedImage));
        setNormalDrawable(typedArray.getDrawable(R.styleable.MainTabItem_normalImage));
        setIsShowReadPoint(typedArray.getBoolean(R.styleable.MainTabItem_showRedPoint, false));
        typedArray.recycle();
    }

    public boolean isTabSelected() {
        return tabIsSelected;
    }

    public void setNormalDrawable(Drawable normalDrawable) {
        this.normalDrawable = normalDrawable;
        checkState();
    }

    public void setSelectedDrawable(Drawable selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
        checkState();
    }

    /**
     * 设置是否选中
     */
    public void setIsSelected(boolean isSelected) {
        this.tabIsSelected = isSelected;
        checkState();
    }

    private void checkState() {
        imageView.setImageDrawable(tabIsSelected ? selectedDrawable : normalDrawable);
    }

    /**
     * 设置显示文本
     */
    public void setText(String text) {
        textView.setText(text);
    }

    public void setIsShowReadPoint(boolean isShowReadPoint) {
        if (isShowReadPoint) {
            readPoint.setVisibility(VISIBLE);
        } else {
            readPoint.setVisibility(GONE);
        }
    }
}
