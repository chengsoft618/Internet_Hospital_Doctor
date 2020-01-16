package cn.longmaster.hospital.doctor.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;

/**
 * 图片选择方式dialog
 * Created by Tengshuxiang on 2016-08-15.
 */
public class PhotoWayDialog extends Dialog {
    private OnWayClickListener mOnWayClickListener;

    public PhotoWayDialog(Context context) {
        this(context, R.style.customAnimation_noActionbar_window_style);
    }

    public PhotoWayDialog(Context context, int themeResId) {
        super(context, themeResId);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_photo_way);
        ViewInjecter.inject(this);
    }

    @OnClick({R.id.layout_dialog_photo_way_album_btn,
            R.id.layout_dialog_photo_way_camera_btn,
            R.id.layout_dialog_photo_way_close_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_dialog_photo_way_album_btn:
                mOnWayClickListener.onAlbumClick();
                dismiss();
                break;

            case R.id.layout_dialog_photo_way_camera_btn:
                mOnWayClickListener.onCameraClick();
                dismiss();
                break;

            case R.id.layout_dialog_photo_way_close_btn:
                dismiss();
                break;
        }
    }

    public void setOnWayClickListener(OnWayClickListener onWayClickListener) {
        this.mOnWayClickListener = onWayClickListener;
    }

    public interface OnWayClickListener {
        void onAlbumClick();

        void onCameraClick();
    }

}
