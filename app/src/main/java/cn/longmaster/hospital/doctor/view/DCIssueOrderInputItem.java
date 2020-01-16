package cn.longmaster.hospital.doctor.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 专科建设-出具医嘱数据栏
 * Created by yangyong on 2019-11-27.
 */
public class DCIssueOrderInputItem extends LinearLayout {
    @FindViewById(R.id.layout_dcissue_order_input_item_required_tv)
    private TextView requiredTv;
    @FindViewById(R.id.layout_dcissue_order_input_item_title_tv)
    private TextView titleTv;
    @FindViewById(R.id.layout_dcissue_order_input_item_input_et)
    private EditText inputEt;

    public DCIssueOrderInputItem(Context context) {
        this(context, null);
    }

    public DCIssueOrderInputItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DCIssueOrderInputItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rootView = layoutInflater.inflate(R.layout.layout_dcissue_order_input_item, this, false);
        ViewInjecter.inject(this, rootView);
        addView(rootView);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DCIssueOrderInputItem);
        boolean isRequired = typedArray.getBoolean(R.styleable.DCIssueOrderInputItem_dcioii_is_required, false);
        requiredTv.setVisibility(isRequired ? VISIBLE : INVISIBLE);
        titleTv.setText(typedArray.getString(R.styleable.DCIssueOrderInputItem_dcioii_title));
        inputEt.setHint(typedArray.getString(R.styleable.DCIssueOrderInputItem_dcioii_input_hint));
        boolean editable = typedArray.getBoolean(R.styleable.DCIssueOrderInputItem_dcioii_editable, true);
        if (!editable) {
            inputEt.setFocusable(false);
            inputEt.setFocusableInTouchMode(false);
        }
        typedArray.recycle();
    }

    public String getInputContent() {
        return inputEt.getText().toString().trim();
    }

    public void setInputContent(String content) {
        inputEt.setText(content);
    }

    public void setInputType(int type){
        inputEt.setInputType(type);
    }
    public void setMaxInput(int max){
        inputEt.setMaxEms(max);
    }
}
