package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.longmaster.hospital.doctor.R;

/**
 * 版本更新
 * update on2017.8.25
 * Created by yangyong on 2015/12/23.
 */
public class UpdateDialog extends Dialog {

    private static Builder sBuilder;
    private static UpdateDialog sUpdateDialog;

    public UpdateDialog(Context context) {
        super(context);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
    }

    protected UpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        setTextAfterColon(findViewById(R.id.dialog_update_info_tv), getContext().getString(R.string.version_update_info, sBuilder.version));
        findViewById(R.id.dialog_update_close_iv).setVisibility(sBuilder.closeable ? View.VISIBLE : View.GONE);
        findViewById(R.id.dialog_update_update_tv).setOnClickListener(v -> {
            dismiss();
            if (sBuilder.onUpdateClickListener != null) {
                sBuilder.onUpdateClickListener.onClick(v);
            }
        });
        findViewById(R.id.dialog_update_close_iv).setOnClickListener(v -> {
            dismiss();
            if (sBuilder.onCloseClickListener != null) {
                sBuilder.onCloseClickListener.onClick(v);
            }
        });
        setCancelable(sBuilder.cancelable);
    }

    private void setTextAfterColon(TextView tv, String text) {
        if (text == null || text.length() == 0) {
            text = "";
        }
        tv.setText(tv.getText().toString().substring(0, tv.getText().toString().indexOf(":") + 1) + "%s");
        tv.setText(String.format(tv.getText().toString(), text));
    }

    private static UpdateDialog showDialog(Context context, Builder builder) {
        sBuilder = builder;
        if (sUpdateDialog == null || !sUpdateDialog.getContext().getClass().getCanonicalName().equals(context.getClass().getCanonicalName())) {
            sUpdateDialog = new UpdateDialog(context, R.style.updateDialogStyle);
        }
        sUpdateDialog.show();
        return sUpdateDialog;
    }

    public static class Builder {
        private Context context;
        private String version;
        private View.OnClickListener onUpdateClickListener;
        private View.OnClickListener onCloseClickListener;
        private boolean cancelable;
        private boolean closeable;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setOnUpdateClickListener(View.OnClickListener onUpdateClickListener) {
            this.onUpdateClickListener = onUpdateClickListener;
            return this;
        }

        public Builder setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
            this.onCloseClickListener = onCloseClickListener;
            return this;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        public void setCloseable(boolean closeable) {
            this.closeable = closeable;
        }

        public UpdateDialog show() {
            return showDialog(context, this);
        }
    }
}
