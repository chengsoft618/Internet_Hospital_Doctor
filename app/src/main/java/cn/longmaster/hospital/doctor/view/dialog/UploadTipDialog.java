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
import android.widget.Button;

import cn.longmaster.hospital.doctor.R;

/**
 * 上传须知dialog
 * Created by Tengshuxiang on 2016-08-18.
 */
public class UploadTipDialog extends AppCompatDialogFragment {
    private Button mCloseTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_upload_tip, null);
        builder.setView(rootView);
        mCloseTv = rootView.findViewById(R.id.layout_dialog_upload_bottom_btn);
        mCloseTv.setOnClickListener(v -> dismiss());
        return builder.create();
    }
}
