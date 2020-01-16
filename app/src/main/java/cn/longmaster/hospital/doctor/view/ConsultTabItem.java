package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 视频就诊tabitem
 * Created by yangyong on 16/6/03.
 */
public class ConsultTabItem extends FrameLayout {
    @FindViewById(R.id.layout_consult_tabitem_title)
    private TextView mTitleTv;

    @FindViewById(R.id.layout_consult_tabitem_value)
    private TextView mValueTv;

    public ConsultTabItem(Context context) {
        this(context, null);
    }

    public ConsultTabItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConsultTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_consult_tabitem, this, false);
        addView(rootView);
        ViewInjecter.inject(this, rootView);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ConsultTabItem);
        setTitleText(typedArray.getString(R.styleable.ConsultTabItem_consultTabItemTitleText));
        setValeText(typedArray.getString(R.styleable.ConsultTabItem_consultTabItemValueText));
        typedArray.recycle();
    }

    public void setTitleText(String text) {
        mTitleTv.setText(text);
    }

    public void setValeText(String text) {
        mValueTv.setText(text);
    }
}
