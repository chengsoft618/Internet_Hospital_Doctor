package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.longmaster.hospital.doctor.R;

/**
 * @author ABiao_Abiao
 * @date 2019/11/8 14:29
 * @description:
 */
public class PhotoChooseDialog extends AppCompatDialogFragment {
    private TextView dialogPhotoChooseCameraTv;
    private TextView dialogPhotoChooseAlbumTv;
    private TextView dialogPhotoChooseCancel;
    private OnClickListener listener;

    public static PhotoChooseDialog getInstance() {
        return new PhotoChooseDialog();
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
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_photo_choose, null);
        builder.setView(rootView);
        dialogPhotoChooseCameraTv = rootView.findViewById(R.id.dialog_photo_choose_camera_tv);
        dialogPhotoChooseAlbumTv = rootView.findViewById(R.id.dialog_photo_choose_album_tv);
        dialogPhotoChooseCancel = rootView.findViewById(R.id.dialog_photo_choose_cancel_tv);
        dialogPhotoChooseAlbumTv.setOnClickListener(v -> {
            if (null != listener) {
                listener.onAlbumClick();
            }
            dismiss();
        });
        dialogPhotoChooseCameraTv.setOnClickListener(v -> {
            if (null != listener) {
                listener.onCameraClick();
            }
            dismiss();
        });
        dialogPhotoChooseCancel.setOnClickListener(v -> {
            if (null != listener) {
                listener.onCancel();
            }
            dismiss();
        });
        AlertDialog dialog = builder.create();
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        return dialog;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onCancel();

        void onCameraClick();

        void onAlbumClick();
    }
}
