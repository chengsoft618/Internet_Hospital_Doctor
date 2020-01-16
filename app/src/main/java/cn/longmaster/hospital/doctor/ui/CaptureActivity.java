package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.longmaster.cameralibrary.MyJCameraView;
import cn.longmaster.cameralibrary.listener.ErrorListener;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.util.GlideUtils;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;

import static cn.longmaster.hospital.doctor.util.BitmapUtils.savePictureToLocal;


/**
 * 自定义摄像头
 * Created by Yang² on 2017/9/20.
 */

public class CaptureActivity extends NewBaseActivity {

    private final int CAPTURE_PHOTO_DEFAULT_MAX_NUM = 99;
    private final int REQUEST_CODE_CAPTURE_PREVIEW = 200;

    @FindViewById(R.id.activity_capture_camera_jcv)
    private MyJCameraView mJCameraView;
    @FindViewById(R.id.activity_capture_cancel_tv)
    private TextView mCancelTv;
    @FindViewById(R.id.activity_capture_upload_tv)
    private TextView mUploadTv;
    @FindViewById(R.id.activity_capture_capture_ib)
    private ImageButton mCaptureIb;
    @FindViewById(R.id.activity_capture_switch_ib)
    private ImageButton mSwitchIb;
    @FindViewById(R.id.activity_capture_photo_iv)
    private ImageView mPhotoIv;
    @FindViewById(R.id.activity_capture_photo_delete_ib)
    private ImageButton mDeleteIb;

    private ArrayList<String> mPhotoList = new ArrayList<>();
    private boolean mIsReupload;
    private boolean mFromPayment;
    private int mMaxNum;

    public static void startActivity(FragmentActivity activity, boolean isReUpload, int requestCode) {
        startActivity(activity, isReUpload, false, 99, requestCode);
    }

    public static void startActivity(FragmentActivity activity, boolean isReUpload, boolean isPayment, int maxNum, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, CaptureActivity.class);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_REUPLOAD, isReUpload);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAYMENT, isPayment);
        intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_NUM, maxNum);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mJCameraView.onResume();
    }

    @Override
    protected void initDatas() {
        mIsReupload = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_REUPLOAD, false);
        mFromPayment = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PAYMENT, false);
        mMaxNum = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_NUM, CAPTURE_PHOTO_DEFAULT_MAX_NUM);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_capture;
    }

    @Override
    protected void initViews() {
        addListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mJCameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_PREVIEW:
                if (resultCode == RESULT_CANCELED) {
                    mPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    setPhoto();
                } else if (resultCode == RESULT_OK) {
                    mPhotoList = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
                    setResultData(RESULT_OK);
                }
                break;
            default:
                break;
        }
    }

    private void addListener() {
        mJCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showPermissionDialog();
                    }
                });
            }

            @Override
            public void AudioPermissionError() {
            }
        });
        //JCameraView监听
        mJCameraView.setTakePictureListener(bitmap -> {
            mJCameraView.onPause();
            AppHandlerProxy.runOnUIThread(() -> {
                mPhotoIv.setVisibility(View.VISIBLE);
                mDeleteIb.setVisibility(View.VISIBLE);
                //存入本地相册
                String path = savePictureToLocal(bitmap);
                addToPicList(path);
                GlideUtils.showImage(mPhotoIv, getThisActivity(), path);
            });
            AppHandlerProxy.postDelayed(() -> mJCameraView.onResume(), 1000);
        });

    }

    @OnClick({R.id.activity_capture_cancel_tv,
            R.id.activity_capture_upload_tv,
            R.id.activity_capture_capture_ib,
            R.id.activity_capture_switch_ib,
            R.id.activity_capture_photo_iv,
            R.id.activity_capture_photo_delete_ib})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_capture_cancel_tv:
//                stopCamera();
                finish();
                break;

            case R.id.activity_capture_upload_tv:
                if (mPhotoList.size() > 0) {
                    setResultData(RESULT_OK);
                }
                break;

            case R.id.activity_capture_capture_ib:
                if (mIsReupload) {//重新上传只保留一张照片
                    mPhotoList.clear();
                    setPhoto();
                }
                if (checkPhotoNum()) {
                    Logger.logI(Logger.CAMERA, "mJCameraView.takePictures()-->");
                    mJCameraView.takePictures();
                }
                break;

            case R.id.activity_capture_switch_ib:
                mJCameraView.switchCamera();
                break;

            case R.id.activity_capture_photo_iv:
                Intent intent = new Intent(getThisActivity(), CapturePreviewActivity.class);
                intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, mPhotoList);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_PREVIEW);
                break;

            case R.id.activity_capture_photo_delete_ib:
                deletePhoto();
                break;

        }

    }

    private void deletePhoto() {
        if (mPhotoList.size() > 0) {
            mPhotoList.remove(0);
            setPhoto();
        }
    }

    /**
     * 添加到图片列表
     *
     * @param path
     */
    private void addToPicList(String path) {
        mPhotoList.add(0, path);
    }

    private void setPhoto() {
        if (mPhotoList.size() > 0) {
            mPhotoIv.setVisibility(View.VISIBLE);
            mDeleteIb.setVisibility(View.VISIBLE);
            GlideUtils.showImage(mPhotoIv, getThisActivity(), mPhotoList.get(0));
        } else {
            mPhotoIv.setImageDrawable(null);
            mPhotoIv.setVisibility(View.GONE);
            mDeleteIb.setVisibility(View.GONE);
        }
    }

    private boolean checkPhotoNum() {
        if (mPhotoList.size() >= mMaxNum) {
            showOutNumDialog();
            return false;
        }
        return true;
    }

    private void showOutNumDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage(mFromPayment ? getString(R.string.upload_capture_max_tips, 2) : getString(R.string.upload_capture_photo_max_num_tips, mMaxNum))
                .setNegativeButton(R.string.user_upload_data, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
//                        if (checkNetwork()) {
                        setResultData(RESULT_OK);
//                        }
                    }
                }).show();
    }

    private boolean checkNetwork() {
        if (NetStateReceiver.getCurrentNetType(getThisActivity()) == NetStateReceiver.NETWORK_TYPE_NONE) {
            showNoNetworkDialog();
            return false;
        }
        if (NetStateReceiver.getCurrentNetType(getThisActivity()) == NetStateReceiver.NETWOKR_TYPE_MOBILE) {
            showNoWifiDialog();
            return false;
        }
        return true;
    }

    private void showPermissionDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setTitle(R.string.title_dialog_tip)
                .setMessage(R.string.camera_permission_not_granted)
                .setNegativeButton(R.string.sure, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        finish();
                    }
                }).show();

    }

    private void showNoNetworkDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage(R.string.upload_no_network)
                .setNegativeButton(R.string.sure, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                    }
                }).show();

    }

    private void showNoWifiDialog() {
        new CommonDialog.Builder(getThisActivity())
                .setMessage(R.string.upload_no_wifi)
                .setPositiveButton(R.string.upload_not_now, new CommonDialog.OnPositiveBtnClickListener() {
                    @Override
                    public void onPositiveBtnClicked() {

                    }
                })
                .setNegativeButton(R.string.upload_still, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {
                        setResultData(RESULT_OK);
                    }
                }).show();
    }

    private void setResultData(int resultCode) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, mPhotoList);
        setResult(resultCode, intent);
        finish();
    }
}
