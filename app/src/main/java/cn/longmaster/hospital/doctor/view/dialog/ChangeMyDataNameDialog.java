package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.longmaster.hospital.doctor.R;

/**
 * @author ABiao_Abiao
 * @date 2019/11/15 15:46
 * @description:
 */
public class ChangeMyDataNameDialog extends AppCompatDialogFragment {
    private TextView editTextDialogTitleTv;
    private EditText editTextDialogMessageEt;
    private TextView editTextDialogPositiveTv;
    private View editTextDialogLineView;
    private TextView editTextDialogNegativeTv;
    private OnDialogClickListener onDialogClickListener;
    private static final String KEY_TO_QUERY_NAME = "_KEY_TO_QUERY_NAME_";

    public static ChangeMyDataNameDialog getInstance(String materialName) {
        ChangeMyDataNameDialog dataNameDialog = new ChangeMyDataNameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TO_QUERY_NAME, materialName);
        dataNameDialog.setArguments(bundle);
        return dataNameDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
        builder.setView(rootView);
        editTextDialogTitleTv = rootView.findViewById(R.id.edit_text_dialog_title_tv);
        editTextDialogMessageEt = rootView.findViewById(R.id.edit_text_dialog_message_et);
        editTextDialogPositiveTv = rootView.findViewById(R.id.edit_text_dialog_positive_tv);
        editTextDialogLineView = rootView.findViewById(R.id.edit_text_dialog_line_view);
        editTextDialogNegativeTv = rootView.findViewById(R.id.edit_text_dialog_negative_tv);
        editTextDialogMessageEt.setText(getMaterialName());
        editTextDialogNegativeTv.setOnClickListener(v -> {
            if (null != onDialogClickListener) {
                onDialogClickListener.onConfirm(editTextDialogMessageEt.getText().toString());
            }
            dismiss();
        });
        editTextDialogPositiveTv.setOnClickListener(v -> {
            if (null != onDialogClickListener) {
                onDialogClickListener.onCancel();
            }
            dismiss();
        });
        return builder.create();
    }

    public interface OnDialogClickListener {
        void onCancel();

        void onConfirm(String msg);
    }

    private String getMaterialName() {
        return getArguments().getString(KEY_TO_QUERY_NAME);
    }

    public OnDialogClickListener getOnDialogClickListener() {
        return onDialogClickListener;
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
