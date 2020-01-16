package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 专科建设-出具医嘱数据栏
 * Created by yangyong on 2019-11-27.
 */
public class DCIssueOrderTitleAndValueItem extends LinearLayout {
    @FindViewById(R.id.layout_dcissue_order_title_and_value_title_tv)
    private TextView titleTv;
    @FindViewById(R.id.layout_dcissue_order_title_and_value_value_tv)
    private TextView valueTv;
    @FindViewById(R.id.layout_dcissue_order_title_and_value_divider_view)
    private View dividerView;

    public DCIssueOrderTitleAndValueItem(Context context) {
        this(context, null);
    }

    public DCIssueOrderTitleAndValueItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DCIssueOrderTitleAndValueItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.layout_dcissue_order_title_and_value_item, this, false);
        ViewInjecter.inject(this, rootView);
        addView(rootView);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DCIssueOrderTitleAndValueItem);
        titleTv.setText(typedArray.getString(R.styleable.DCIssueOrderTitleAndValueItem_dciotv_title));
        valueTv.setText(typedArray.getString(R.styleable.DCIssueOrderTitleAndValueItem_dciotv_value));
        boolean hasDivider = typedArray.getBoolean(R.styleable.DCIssueOrderTitleAndValueItem_dciotv_divider, true);
        if (!hasDivider) {
            dividerView.setVisibility(GONE);
        }
        typedArray.recycle();
    }

    public void setValue(String value) {
        valueTv.setText(value);
    }

    public String getValue() {
        return valueTv.getText().toString().trim();
    }
}
