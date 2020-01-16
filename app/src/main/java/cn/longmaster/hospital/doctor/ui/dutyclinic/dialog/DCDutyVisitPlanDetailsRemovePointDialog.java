package cn.longmaster.hospital.doctor.ui.dutyclinic.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * @author: wangyang
 * @date: 2020-01-04 14:43
 * @description:
 */
public class DCDutyVisitPlanDetailsRemovePointDialog extends Dialog {
    @FindViewById(R.id.layout_visit_plan_details_redact_remove_point_title_tv)
    private TextView mTitleTv;
    @FindViewById(R.id.layout_visit_plan_details_redact_remove_point_positive_tv)
    private TextView mPositiveTv;
    @FindViewById(R.id.layout_visit_plan_details_redact_remove_point_negative_tv)
    private TextView mNegativeTv;
    private String mTitleTvContent;
    private OnPositiveBtnClickListener positiveBtnClickListener;
    private OnNegativeBtnClickListener negativeBtnClickListener;

    public void setPositiveBtnClickListener(OnPositiveBtnClickListener positiveBtnClickListener) {
        this.positiveBtnClickListener = positiveBtnClickListener;
    }

    public void setNegativeBtnClickListener(OnNegativeBtnClickListener negativeBtnClickListener) {
        this.negativeBtnClickListener = negativeBtnClickListener;
    }

    public DCDutyVisitPlanDetailsRemovePointDialog(@NonNull Context context) {
        this(context, R.style.custom_noActionbar_window_style);
    }

    public DCDutyVisitPlanDetailsRemovePointDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_visit_paln_details_redact_remove_point_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        mTitleTv.setText(mTitleTvContent);
    }

    @OnClick({R.id.layout_visit_plan_details_redact_remove_point_positive_tv, R.id.layout_visit_plan_details_redact_remove_point_negative_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_visit_plan_details_redact_remove_point_positive_tv: {
                dismiss();
            }
            break;

            case R.id.layout_visit_plan_details_redact_remove_point_negative_tv: {
                negativeBtnClickListener.onNegativeBtnClicked();
                dismiss();
            }
            break;
            default:
                break;
        }
    }

    public void setmTitleTvContent(String titleContent) {
        this.mTitleTvContent = titleContent;
    }

    public interface OnPositiveBtnClickListener {
        void onPositiveBtnClicked();
    }

    public interface OnNegativeBtnClickListener {
        void onNegativeBtnClicked();
    }
}
