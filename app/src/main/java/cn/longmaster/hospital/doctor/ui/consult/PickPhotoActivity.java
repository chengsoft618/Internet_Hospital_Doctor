package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.photo.MediaStoreHelper;
import cn.longmaster.doctorlibrary.util.photo.Photo;
import cn.longmaster.doctorlibrary.util.photo.PhotoDirectory;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.Display;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialFileFlagInfo;
import cn.longmaster.hospital.doctor.core.upload.newupload.MaterialTaskManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.ui.consult.adapter.PickPhotoAdapter;
import cn.longmaster.utils.DisplayUtil;
import cn.longmaster.utils.ToastUtils;

public class PickPhotoActivity extends NewBaseActivity {
    private final String TAG = PickPhotoActivity.class.getSimpleName();
    //默认可以选择图片的最大数量
    private final int DEFAULT_MAX_COUNT = 99;
    private final int DEFAULT_MAX_COUNT_TWO = 9;
    private final int CODE_PHOTO_PREVIEW = 200;
    private final int NO_UPLOAD_RECORD = 100;//无上传记录状态标识

    @FindViewById(R.id.activity_photo_picker_photos_rv)
    private RecyclerView mPhotosRv;
    @FindViewById(R.id.activity_photo_picker_confirm_btn)
    private Button mConfirmBtn;
    @FindViewById(R.id.activity_photo_picker_bottom_rl)
    private RelativeLayout mBottomRl;

    @AppApplication.Manager
    private MaterialTaskManager mMaterialTaskManager;

    //可以选择相片最大数量
    private int mMaxCount;
    //图片目录集合
    private List<PhotoDirectory> mDirectories;
    //所有图片list
    private List<Photo> mPhotoList;
    //选择图片list
    private List<Photo> mSelectPhotoList;
    private int mAppointmentId;
    private boolean mIsUploadFirstJoureny;

    private PickPhotoAdapter mPickPhotoAdapter;
    private boolean isLoadedImages;

    @Override
    protected void handleIntent(Intent intent, Display display) {
        super.handleIntent(intent, display);
        mIsUploadFirstJoureny = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_UPLOAD_FIRST_JOURNEY, false);
        int picCount = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PIC_COUNT, 0);
        if (mIsUploadFirstJoureny) {
            mMaxCount = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, DEFAULT_MAX_COUNT_TWO);
            mMaxCount = DEFAULT_MAX_COUNT_TWO - picCount;
        } else {
            mMaxCount = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, DEFAULT_MAX_COUNT);
        }
        mAppointmentId = intent.getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, 0);
    }

    @Override
    protected void initDatas() {
        mDirectories = new ArrayList<>();
        mPhotoList = new ArrayList<>();
        mSelectPhotoList = new ArrayList<>();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_picker_upload_photo;
    }

    @Override
    protected void initViews() {
        mPhotosRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int space = DisplayUtil.dip2px(5);
                outRect.top = space;
                outRect.right = space;
            }
        });
        if (mMaxCount == 1) {
            mBottomRl.setVisibility(View.GONE);
        } else {
            mBottomRl.setVisibility(View.VISIBLE);
        }
        mPickPhotoAdapter = new PickPhotoAdapter(getThisActivity(), mMaxCount);
        //设置RecyclerView
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mPhotosRv.setLayoutManager(layoutManager);
        mPhotosRv.setItemAnimator(null);
        mPhotosRv.setAdapter(mPickPhotoAdapter);
        mMaterialTaskManager.getMaterialFileFlags(mAppointmentId, new MaterialTaskManager.GetMaterialFileFlagCallback() {
            @Override
            public void onGetMaterialFileFlagStateChanged(List<MaterialFileFlagInfo> materialFileFlagInfos) {
                Logger.logI(Logger.COMMON, TAG + "getUploadedStateFiles-->getUploadedMaterialFiles-->materialFileFlagInfos:" + materialFileFlagInfos);
                getDirectories(materialFileFlagInfos);
            }
        });
        setListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_PHOTO_PREVIEW:
                    mPhotoList = (ArrayList<Photo>) WeakDataHolder.getInstance().getData(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PHOTOS);
                    mSelectPhotoList = (ArrayList<Photo>) WeakDataHolder.getInstance().getData(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PHOTOS);
                    if (mMaxCount == 1) {
                        if (data != null) {
                            ArrayList<String> paths = data.getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS);
                            setSinglePhoto(paths);
                        }
                    } else {
                        setConfirm(mSelectPhotoList);
                    }
                    break;
                default:
                    break;
            }
        } else {

            mPickPhotoAdapter.setData(mPhotoList, mSelectPhotoList.size());
            mPhotosRv.setVisibility(View.VISIBLE);
            //mPhotosRv.setAdapter(mPickPhotoAdapter);
            refreshPreviewTextView(mSelectPhotoList.size());
        }
    }

    /**
     * 获取所有本地图片目录
     */
    private void getDirectories(final List<MaterialFileFlagInfo> mUploadFiles) {
        MediaStoreHelper.getPhotoDirs(getThisActivity(), directories -> {
            if (isLoadedImages) {
                return;
            }
            Logger.logI(Logger.COMMON, "PickPhotoActivity-->getDirectories()" + directories);
            mDirectories.clear();
            mDirectories.addAll(directories);
            mPhotoList.clear();
            mPhotoList.addAll(mDirectories.get(MediaStoreHelper.INDEX_ALL_PHOTOS).getPhotos());

            for (int i = 0; i < mPhotoList.size(); i++) {
                mPhotoList.get(i).setUploadState(NO_UPLOAD_RECORD);
            }

            if (mUploadFiles.size() > 0) {
                for (int i = 0; i < mUploadFiles.size(); i++) {
                    for (int j = 0; j < mPhotoList.size(); j++) {
                        if (mPhotoList.get(j).getPath().equals(mUploadFiles.get(i).getLocalFilePath())) {
                            mPhotoList.get(j).setUploadState(mUploadFiles.get(i).getUploadState());
                        }
                    }
                }
            }
            List<Photo> photos = new ArrayList<>();
            for (Photo photo : mPhotoList) {
                File file = new File(photo.getPath());
                if (!(file.exists())) {
                    photos.add(photo);
                }
            }
            if (photos.size() > 0) {
                mPhotoList.removeAll(photos);
            }
            Logger.logI(Logger.COMMON, "PickPhotoActivity-->getDirectories()-->mPhotoList.size()-->" + mPhotoList.size() + ",  mUploadFiles.size():" + mUploadFiles.size() + ", mPhotoList:" + mPhotoList);
            isLoadedImages = true;
            mPickPhotoAdapter.setData(mPhotoList);
        });
    }

    /**
     * 设置监听
     */
    private void setListener() {
        //监听图片选中状态
        mPickPhotoAdapter.setOnItemCheckListener((position, photo, isCheck) -> {
            if (isCheck) {
                mSelectPhotoList.remove(photo);
            } else {
                if (mSelectPhotoList.size() >= mMaxCount) {
                    if (mIsUploadFirstJoureny) {
                        ToastUtils.showShort(getString(R.string.photo_picker_max_tips, mMaxCount));
                    }
                    return true;
                } else {
                    mSelectPhotoList.add(photo);
                }
            }
            refreshPreviewTextView(mSelectPhotoList.size());
            return false;
        });
        mPickPhotoAdapter.setOnItemClickListener((position, photo) -> {
            Intent intent = new Intent(getThisActivity(), PhotoPreviewActivity.class);
            WeakDataHolder.getInstance().saveData(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PHOTOS, mPhotoList);
            WeakDataHolder.getInstance().saveData(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PHOTOS, mSelectPhotoList);
            WeakDataHolder.getInstance().saveData(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, position);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_UPLOAD_FIRST_JOURNEY, mIsUploadFirstJoureny);
            intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, mMaxCount);
            startActivityForResult(intent, CODE_PHOTO_PREVIEW);
        });

        mConfirmBtn.setOnClickListener(v -> {
            if (mSelectPhotoList.isEmpty()) {
                return;
            }
            setConfirm(mSelectPhotoList);
        });
    }

    /**
     * 刷新按钮显示文案
     */
    private void refreshPreviewTextView(int total) {
        if (total <= 0) {
            mConfirmBtn.setText(R.string.photo_picker_confirm);
            mConfirmBtn.setTextColor(getCompatColor(R.color.color_999999));
            mConfirmBtn.setBackgroundResource(R.drawable.bg_btn_photo_gray_clickable_false);
        } else {
            mConfirmBtn.setText(getString(R.string.photo_picker_confirm_count, total));
            mConfirmBtn.setTextColor(getCompatColor(R.color.color_white));
            mConfirmBtn.setBackgroundResource(R.drawable.bg_btn_photo_blue);
        }
    }

    private void setSinglePhoto(ArrayList<String> paths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 确定按钮操作
     */
    private void setConfirm(List<Photo> selectPhotoList) {
        ArrayList<String> selectedPhotos = new ArrayList<>();
        for (Photo photo : selectPhotoList) {
            selectedPhotos.add(photo.getPath());
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS, selectedPhotos);
        setResult(RESULT_OK, intent);
        finish();
    }
}
