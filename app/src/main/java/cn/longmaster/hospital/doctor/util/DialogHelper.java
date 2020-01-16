package cn.longmaster.hospital.doctor.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.consult.PickPhotoActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;

/**
 * @author ABiao_Abiao
 * @date 2019/7/5 9:53
 * @description: 弹窗辅助类
 */
public class DialogHelper {

    public interface OnItemClickListener {
        void onItemClick(Dialog dialog, BaseQuickAdapter adapter, View view, int position);
    }

    public static class ChoosePicsBuilder {
        private FragmentActivity activity;
        private Fragment fragment;
        private int countPics;
        private int maxCount = 9;
        private int requestCodeCamera;
        private int requestCodeAlbum;
        private String uploadPhotoName;
        private boolean isUploadFirstJoureny;
        private Disposable disposable;
        private View.OnClickListener photographClickListener;
        private View.OnClickListener albumClickListener;

        public ChoosePicsBuilder setActivity(FragmentActivity activity) {
            this.activity = activity;
            return this;
        }

        public ChoosePicsBuilder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public ChoosePicsBuilder setCountPics(int countPics) {
            this.countPics = countPics;
            return this;
        }

        public ChoosePicsBuilder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public ChoosePicsBuilder setRequestCodeCamera(int requestCodeCamera) {
            this.requestCodeCamera = requestCodeCamera;
            return this;
        }

        public ChoosePicsBuilder setRequestCodeAlbum(int requestCodeAlbum) {
            this.requestCodeAlbum = requestCodeAlbum;
            return this;
        }

        public ChoosePicsBuilder setUploadPhotoName(String uploadPhotoName) {
            this.uploadPhotoName = uploadPhotoName;
            return this;
        }

        public ChoosePicsBuilder setUploadFirstJoureny(boolean isUploadFirstJoureny) {
            this.isUploadFirstJoureny = isUploadFirstJoureny;
            return this;
        }

        public ChoosePicsBuilder setPhotographClickListener(View.OnClickListener photographClickListener) {
            this.photographClickListener = photographClickListener;
            return this;
        }

        public ChoosePicsBuilder setAlbumClickListener(View.OnClickListener albumClickListener) {
            this.albumClickListener = albumClickListener;
            return this;
        }

        public void show() {
            View layout = LayoutInflater.from(activity).inflate(R.layout.layout_photograph_mode_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(activity, R.style.custom_noActionbar_window_style).create();
            dialog.show();
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);

            Window win = dialog.getWindow();
            if (null == win) {
                return;
            }
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            win.setAttributes(lp);

            TextView photograph = layout.findViewById(R.id.layout_photograph_photograph);
            TextView album = layout.findViewById(R.id.layout_photograph_album);
            TextView mCancelTv = layout.findViewById(R.id.layout_photograph_cancel);
            photograph.setOnClickListener(v -> {
                if (null != photographClickListener) {
                    photographClickListener.onClick(v);
                } else {
                    disposable = PermissionHelper.init(activity).addPermissionGroups(PermissionConstants.CAMERA)
                            .requestEachCombined()
                            .subscribe(permission -> {
                                if (permission.granted) {
                                    startCameraActivity(activity, fragment, uploadPhotoName, requestCodeCamera);
                                    // `permission.name` is granted !
                                } else {
                                    new CommonDialog.Builder(activity)
                                            .setTitle("权限授予")
                                            .setMessage("在设置-应用管理-权限中开启相机权限，才能正常使用相机")
                                            .setPositiveButton("取消", dialog::dismiss)
                                            .setCancelable(false)
                                            .setNegativeButton("确定", Utils::gotoAppDetailSetting)
                                            .show();
                                }
                            });
                }

                dialog.dismiss();
            });
            album.setOnClickListener(v -> {
                if (null != albumClickListener) {
                    albumClickListener.onClick(v);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(activity, PickPhotoActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_UPLOAD_FIRST_JOURNEY, isUploadFirstJoureny);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_COUNT, countPics);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, maxCount);
                    if (null != fragment) {
                        fragment.startActivityForResult(intent, requestCodeAlbum);
                    } else {
                        activity.startActivityForResult(intent, requestCodeAlbum);
                    }
                }
                dialog.dismiss();
            });
            mCancelTv.setOnClickListener(v -> dialog.dismiss());
            dialog.setOnDismissListener(dialog1 -> {
                if (null != disposable && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            });
        }

        private void startCameraActivity(FragmentActivity activity, Fragment fragment, String fileName, int requestCode) {
            Intent intentCamera = new Intent();
            intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.addCategory(Intent.CATEGORY_DEFAULT);
            //mUploadPhotoName = SdManager.getInstance().getTempPath() + TimeUtils.getNowMills() + ".jpg";
            File file = new File(fileName);
            if (file.exists()) {
                FileUtil.deleteFile(fileName);
            }
            Uri uri = getUri(activity, file);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (fragment != null) {
                fragment.startActivityForResult(intentCamera, requestCode);
            } else {
                activity.startActivityForResult(intentCamera, requestCode);
            }
        }

        private Uri getUri(FragmentActivity activity, File path) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return FileProvider4Camera.getUriForFile(activity, activity.getPackageName() + ".FileProvider4Camera", path);
            } else {
                return Uri.fromFile(path);
            }
        }
    }

    public static class SingleChooseBuilder {
        private AppCompatActivity activity;
        private Fragment fragment;

    }
}
