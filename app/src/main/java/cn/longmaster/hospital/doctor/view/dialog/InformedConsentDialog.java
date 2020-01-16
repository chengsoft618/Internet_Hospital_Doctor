package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 用户知情同意通用dialog
 * Created by Yang² on 2016/7/28.
 */
public class InformedConsentDialog extends Dialog {

    @FindViewById(R.id.layout_informed_consent_title_tv)
    private TextView mTitleTv;
    @FindViewById(R.id.layout_informed_consent_layout_ll)
    private LinearLayout mLayoutLl;
    @FindViewById(R.id.layout_informed_consent_confirm_btn)
    private Button mConfirmBtn;

    private static DialogParams mDialogParams;

    protected InformedConsentDialog(Context context) {
        this(context, R.style.custom_noActionbar_window_style);
    }

    protected InformedConsentDialog(Context context, int theme) {
        super(context, theme);
    }

    protected InformedConsentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_informed_consent_dialog);
        ViewInjecter.inject(this);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
        setContent();
    }

    private void setContent() {
        if (!TextUtils.isEmpty(mDialogParams.title)) {
            mTitleTv.setText(mDialogParams.title);
        }
        if (!TextUtils.isEmpty(mDialogParams.btnText)) {
            mConfirmBtn.setText(mDialogParams.btnText);
        }
        mTitleTv.setTextColor(ContextCompat.getColor(getContext(), mDialogParams.titleTextColor));
        mConfirmBtn.setTextColor(ContextCompat.getColor(getContext(), mDialogParams.btnTextColor));
        if (null != mDialogParams.messageArray) {
            for (String message : mDialogParams.messageArray) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_message, null);
                TextView textView = (TextView) view.findViewById(R.id.item_dialog_message_tv);
                textView.setText(message);
                mLayoutLl.addView(view);
            }

        } else if (null != mDialogParams.messageIdArray) {
            for (int messageId : mDialogParams.messageIdArray) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_message, null);
                TextView textView = (TextView) view.findViewById(R.id.item_dialog_message_tv);
                textView.setText(messageId);
                mLayoutLl.addView(view);
            }
        }
    }

    @OnClick({R.id.layout_informed_consent_confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_informed_consent_confirm_btn:
                mDialogParams.btnClickListener.onBtnClicked();
                dismiss();
                break;
        }
    }

    public static class DialogParams {

        private String title;
        private String btnText;
        private String[] messageArray;
        private int[] messageIdArray;
        private int titleTextColor = R.color.color_app_main_color;
        private int btnTextColor = R.color.color_white;
        private OnBtnClickListener btnClickListener;
        private OnDismissListener onDismissListener;
        private boolean canceledOnTouchOutside = false;
        private boolean cancelable = false;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBtnText(String btnText) {
            this.btnText = btnText;
        }

        public void setMessageArray(String[] messageArray) {
            this.messageArray = messageArray;
        }

        public void setMessageIdArray(int[] messageIdArray) {
            this.messageIdArray = messageIdArray;
        }

        public void setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
        }

        public void setBtnTextColor(int btnTextColor) {
            this.btnTextColor = btnTextColor;
        }

        public void setBtnClickListener(OnBtnClickListener btnClickListener) {
            this.btnClickListener = btnClickListener;
        }

        public void setOnDismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
        }

        public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
            mDialogParams = new DialogParams();
        }

        public Builder setTitle(int titleId) {
            mDialogParams.setTitle(context.getString(titleId));
            return this;
        }

        public Builder setTitle(String title) {
            mDialogParams.setTitle(title);
            return this;
        }

        public Builder setMessage(int... messageArray) {
            mDialogParams.setMessageIdArray(messageArray);
            return this;
        }

        public Builder setMessage(String... messageArray) {
            mDialogParams.setMessageArray(messageArray);
            return this;
        }

        public Builder setButton(int textId, OnBtnClickListener listener) {
            mDialogParams.setBtnText(context.getString(textId));
            mDialogParams.setBtnClickListener(listener);
            return this;
        }

        public Builder setTitleTextColor(int colorId) {
            mDialogParams.setTitleTextColor(colorId);
            return this;
        }

        public Builder setBtnTextColor(int colorId) {
            mDialogParams.setBtnTextColor(colorId);
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean b) {
            mDialogParams.setCanceledOnTouchOutside(b);
            return this;
        }

        public Builder setCancelable(boolean b) {
            mDialogParams.setCancelable(b);
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener listener) {
            mDialogParams.setOnDismissListener(listener);
            return this;
        }

        private InformedConsentDialog create() {
            InformedConsentDialog dialog = new InformedConsentDialog(context);
            return dialog;
        }

        public InformedConsentDialog show() {
            final InformedConsentDialog dialog = create();
            dialog.show();
            dialog.setOnDismissListener(mDialogParams.onDismissListener);
            dialog.setCanceledOnTouchOutside(mDialogParams.canceledOnTouchOutside);
            dialog.setCancelable(mDialogParams.cancelable);
            return dialog;
        }
    }

    public interface OnBtnClickListener {
        void onBtnClicked();
    }

}
