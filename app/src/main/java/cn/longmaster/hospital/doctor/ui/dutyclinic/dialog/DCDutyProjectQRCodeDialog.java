package cn.longmaster.hospital.doctor.ui.dutyclinic.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.util.GlideUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/12/19 21:02
 * @description:
 */
public class DCDutyProjectQRCodeDialog extends DialogFragment {
    private static final String KEY_TO_QUERY_PROJECT_NAME = "_KEY_TO_QUERY_PROJECT_NAME_";
    private static final String KEY_TO_QUERY_PROJECT_QR_CODE = "_KEY_TO_QUERY_PROJECT_QR_CODE_";
    private OnDialogClickListener onDialogClickListener;

    public static DCDutyProjectQRCodeDialog getInstance(String projectName, String projectQRCode) {
        DCDutyProjectQRCodeDialog dialog = new DCDutyProjectQRCodeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TO_QUERY_PROJECT_NAME, projectName);
        bundle.putString(KEY_TO_QUERY_PROJECT_QR_CODE, projectQRCode);
        dialog.setArguments(bundle);
        return dialog;
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
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_dc_duty_project_qr_code, null);
        RelativeLayout rootViewRl = (RelativeLayout) rootView.findViewById(R.id.dailog_dc_duty_project_qr_code_rl);
        ImageView titleIv = rootView.findViewById(R.id.title_iv);
        ImageView closeIv = rootView.findViewById(R.id.close_iv);
        TextView projectNameTv = rootView.findViewById(R.id.project_name_tv);
        ImageView qrCodeIv = rootView.findViewById(R.id.qr_code_iv);
        TextView saveQrCodeTv = rootView.findViewById(R.id.save_qr_code_tv);
        qrCodeIv.setOnLongClickListener(v -> {
            if (null != onDialogClickListener) {
                closeIv.setVisibility(View.GONE);
                onDialogClickListener.onQRCodeSave(rootViewRl);
                closeIv.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });
        closeIv.setOnClickListener(v -> {
            dismiss();
        });
        projectNameTv.setText("让患者微信扫一扫加入\n" + getProjectName() + "项目");
        GlideUtils.showImage(qrCodeIv, getContext(), getQRCodeUtl());
        builder.setView(rootView);
        return builder.create();
    }

    public interface OnDialogClickListener {
        void onQRCodeSave(View rootViewRl);
    }

    private String getProjectName() {
        return getArguments() == null ? null : getArguments().getString(KEY_TO_QUERY_PROJECT_NAME);
    }

    private String getQRCodeUtl() {
        return getArguments() == null ? null : getArguments().getString(KEY_TO_QUERY_PROJECT_QR_CODE);
    }

    public OnDialogClickListener getOnDialogClickListener() {
        return onDialogClickListener;
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }
}
