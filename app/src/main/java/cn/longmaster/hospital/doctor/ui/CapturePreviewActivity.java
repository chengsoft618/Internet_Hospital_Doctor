package cn.longmaster.hospital.doctor.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.receiver.NetStateReceiver;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.utils.DisplayUtil;

/**
 * 相机拍摄预览
 * Created by Yang² on 2017/9/21.
 */

public class CapturePreviewActivity extends NewBaseActivity {
    //private final String TAG = CapturePreviewActivity.class.getSimpleName();

    @FindViewById(R.id.activity_capture_preview_cancel_tv)
    private TextView mCancelTv;
    @FindViewById(R.id.activity_capture_preview_delete_tv)
    private TextView mDeleteTv;
    @FindViewById(R.id.activity_capture_preview_viewpager_hvp)
    private HackyViewPager mViewPagerHvp;
    @FindViewById(R.id.activity_capture_preview_upload_tv)
    private TextView mUploadTv;
    @FindViewById(R.id.activity_capture_preview_pic_list_rv)
    private RecyclerView mPicListRv;

    private CapturePreviewAdapter mCapturePreviewAdapter;
    private PreviewBottomAdapter mPreviewBottomAdapter;
    private ArrayList<String> mPhotoList = new ArrayList<>();
    private int mIndex;

    @Override
    protected void initDatas() {
        mPhotoList = getIntent().getStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_capture_preview;
    }

    @Override
    protected void initViews() {
        setAdapter();
        setListener();
    }

    private void setAdapter() {
        mCapturePreviewAdapter = new CapturePreviewAdapter(this, mPhotoList);
        mViewPagerHvp.setAdapter(mCapturePreviewAdapter);
        mPicListRv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        mPreviewBottomAdapter = new PreviewBottomAdapter(R.layout.item_preview_bottom, mPhotoList);
        mPicListRv.setAdapter(mPreviewBottomAdapter);
    }

    /**
     * 设置监听器
     */
    private void setListener() {
        mViewPagerHvp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
                Logger.logI(Logger.COMMON, "mIndex:" + mIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPreviewBottomAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mViewPagerHvp.setCurrentItem(position);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_PATHS, mPhotoList);
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.activity_capture_preview_cancel_tv,
            R.id.activity_capture_preview_delete_tv,
            R.id.activity_capture_preview_upload_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.activity_capture_preview_cancel_tv:
                setResultData(RESULT_CANCELED);
                break;

            case R.id.activity_capture_preview_delete_tv:
                if (mPhotoList.size() > 0) {
                    showDeletePopupWindow(view);
                }
                break;

            case R.id.activity_capture_preview_upload_tv:
                if (mPhotoList.size() > 0) {
                    setResultData(RESULT_OK);
                }
                break;
            default:
                break;
        }
    }

    private void showDeletePopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getThisActivity()).inflate(R.layout.layout_delete_confirm, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        LinearLayout layout = contentView.findViewById(R.id.layout_delete_confirm_layout_ll);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoList.remove(mIndex);
                if (mPhotoList.size() == 0) {
                    popupWindow.dismiss();
                    setResultData(RESULT_CANCELED);
                    return;
                }
                mCapturePreviewAdapter.notifyDataSetChanged();
                mPreviewBottomAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, DisplayUtil.dip2px(-10));
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
