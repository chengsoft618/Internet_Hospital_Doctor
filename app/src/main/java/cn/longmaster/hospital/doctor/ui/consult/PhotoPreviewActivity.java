package cn.longmaster.hospital.doctor.ui.consult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.util.photo.Photo;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.HackyViewPager;
import cn.longmaster.hospital.doctor.ui.consult.adapter.PhotoPreviewAdapter;
import cn.longmaster.hospital.doctor.view.AppActionBar;

/**
 * 图片多选预览
 * Created by Yang² on 2016/11/18.
 */

public class PhotoPreviewActivity extends BaseActivity {
    //默认可以选择图片的最大数量
    private final int DEFAULT_MAX_COUNT = 99;
    private final int DEFAULT_MAX_COUNT_TWO = 9;//上传病历首程图片最大数量

    @FindViewById(R.id.activity_photo_preview_title_bar)
    private AppActionBar mActionBar;
    @FindViewById(R.id.activity_photo_preview_viewpager)
    private HackyViewPager mViewPager;
    @FindViewById(R.id.activity_photo_preview_confirm_btn)
    private Button mConfirmBtn;

    private PhotoPreviewAdapter mPhotoPreviewAdapter;
    private List<Photo> mPhoneList;
    private List<Photo> mSelectPhotoList;
    private int mIndex;
    private int mMaxCount;
    private boolean mIsUploadFirstJoureny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        ViewInjecter.inject(this);
        initData();
        initView();
        setAdapter();
        setListener();
    }

    @OnClick(R.id.activity_photo_preview_confirm_btn)
    public void onClick(View v) {
        if (mMaxCount == 1) {
            setSinglePhoto();
        } else {
            if (mSelectPhotoList.isEmpty()) {
                return;
            }
            setConfirm(RESULT_OK);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mMaxCount > 1) {
            setConfirm(RESULT_CANCELED);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mIsUploadFirstJoureny = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_UPLOAD_FIRST_JOURNEY, false);
        if (mIsUploadFirstJoureny) {
            mMaxCount = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, DEFAULT_MAX_COUNT_TWO);
        } else {
            mMaxCount = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MAX_COUNT, DEFAULT_MAX_COUNT);
        }
        WeakDataHolder instance = WeakDataHolder.getInstance();
        if (instance != null) {
            mPhoneList = (ArrayList<Photo>) instance.getData(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PHOTOS);
            mSelectPhotoList = (ArrayList<Photo>) instance.getData(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PHOTOS);
            mIndex = (int) instance.getData(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX);
        }
        Logger.logD(Logger.COMMON, "initData->mPhoneList:" + mPhoneList + ", mSelectPhotoList:" + mSelectPhotoList);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mActionBar.setLeftOnClickListener(v -> {
            if (mMaxCount > 1) {
                setConfirm(RESULT_CANCELED);
          /*  setResult(RESULT_CANCELED);
            finish();*/
            } else {
                onBackPressed();
            }
        });
        setFunction();
        refreshActionBar();
        initPreviewTextView();
    }

    /**
     * 设置适配器
     */
    private void setAdapter() {
        mPhotoPreviewAdapter = new PhotoPreviewAdapter(this, mPhoneList);
        mViewPager.setAdapter(mPhotoPreviewAdapter);
        mViewPager.setCurrentItem(mIndex);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                refreshActionBar();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void rightClick(View view) {
        Photo photo = mPhoneList.get(mViewPager.getCurrentItem());
        if (photo.isSelect()) {
            photo.setSelect(false);
            mSelectPhotoList.remove(photo);
            mActionBar.setRightBtnImageDrawable(getCompatDrawable(R.drawable.bg_btn_preview_blue_normal));
        } else {
            if (mSelectPhotoList.size() < mMaxCount) {
                photo.setSelect(true);
                mSelectPhotoList.add(photo);
                mActionBar.setRightBtnImageDrawable(getCompatDrawable(R.drawable.bg_btn_preview_blue_checked));
            } else {
                showToast(getString(R.string.photo_picker_max_tips, mMaxCount));
            }
        }
        refreshPreviewTextView(mSelectPhotoList.size());
    }

    private void setFunction() {
        if (mMaxCount == 1) {
            mActionBar.removeFunction(AppActionBar.FUNCTION_RIGHT_BTN);
        } else {
            mActionBar.addFunction(AppActionBar.FUNCTION_RIGHT_BTN);
        }
    }

    private void initPreviewTextView() {
        if (mMaxCount == 1) {
            mConfirmBtn.setText(R.string.photo_picker_confirm);
            mConfirmBtn.setTextColor(getCompatColor(R.color.color_white));
            mConfirmBtn.setBackgroundResource(R.drawable.bg_btn_photo_blue);
        } else {
            refreshPreviewTextView(mSelectPhotoList.size());
        }
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

    private void refreshActionBar() {
        if (mMaxCount > 1) {
            mActionBar.setRightBtnImageDrawable(getCompatDrawable(mPhoneList.get(mIndex).isSelect()
                    ? R.drawable.bg_btn_preview_blue_checked : R.drawable.bg_btn_preview_blue_normal));
        }
    }

    private void setSinglePhoto() {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(mPhoneList.get(mIndex).getPath());
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PATHS, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setConfirm(int code) {
        Intent intent = new Intent();
        WeakDataHolder.getInstance().saveData(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCAL_PHOTOS, mPhoneList);
        WeakDataHolder.getInstance().saveData(ExtraDataKeyConfig.EXTRA_DATA_KEY_SELECT_PHOTOS, mSelectPhotoList);
        setResult(code, intent);
        finish();
    }
}
