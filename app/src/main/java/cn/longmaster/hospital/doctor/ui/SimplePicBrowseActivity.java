package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.view.AppActionBar;

/**
 * 大图预览
 * Created by Yang² on 2018/1/2.
 */

public class SimplePicBrowseActivity extends BaseActivity {
    private final String TAG = SimplePicBrowseActivity.class.getSimpleName();

    @FindViewById(R.id.activity_simple_pic_browse_viewpager)
    private HackyViewPager mHackyViewPager;
    @FindViewById(R.id.activity_simple_pic_browse_title_bar)
    private AppActionBar mAppActionBar;

    private PicBrowseAdapter mPicBrowseAdapter;
    private ArrayList<String> mLocalFilePaths;
    private ArrayList<String> mServerUrls;
    private ArrayList<String> mDeletePicList = new ArrayList<>();
    private int mIndex;
    private boolean mShowDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pic_browse);
        ViewInjecter.inject(this);
        initData();
        initView();
        setAdapter();
        addListener();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResultData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        mLocalFilePaths = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOCALPATHS);
        mServerUrls = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SERVERURLS);
        mIndex = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_INDEX, 0);
        mShowDelete = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_SHOW_DELETE, false);
        Logger.logI(Logger.COMMON, TAG + "-->mLocalFilePaths-->" + mLocalFilePaths.size());
        Logger.logI(Logger.COMMON, TAG + "-->mServerUrls-->" + mServerUrls.size());
    }

    private void initView() {
        if (!mShowDelete) {
            mAppActionBar.removeFunction(AppActionBar.FUNCTION_RIGHT_TEXT);
        }
    }

    private void setAdapter() {
        if (mLocalFilePaths == null) {
            Logger.logI(Logger.COMMON, "setAdapter()本地图片路径为空！！！");
            return;
        }
        mPicBrowseAdapter = new PicBrowseAdapter(this, mLocalFilePaths, mServerUrls);
        mPicBrowseAdapter.setOnPhotoTapListener((view, x, y) -> toggleFullScreen());
        mHackyViewPager.setAdapter(mPicBrowseAdapter);
        mHackyViewPager.setCurrentItem(mIndex);
    }

    private void addListener() {
        mHackyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onLeftClick(View view) {
        setResultData();
    }

    public void onDeleteClick(View view) {
        mDeletePicList.add(mServerUrls.get(mIndex));
        mLocalFilePaths.remove(mIndex);
        mServerUrls.remove(mIndex);
        if (mLocalFilePaths.size() == 0) {
            setResultData();
        } else {
            mPicBrowseAdapter.notifyDataSetChanged();
        }
    }

    private void setResultData() {
        if (mDeletePicList.size() > 0) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_DELETE_PIC, mDeletePicList);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 开关全屏
     */
    private void toggleFullScreen() {
        mAppActionBar.setVisibility(mAppActionBar.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

}
