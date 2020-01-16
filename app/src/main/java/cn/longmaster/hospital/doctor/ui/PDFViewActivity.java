package cn.longmaster.hospital.doctor.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.hospital.doctor.view.pdfviewpager.library.RemotePDFViewPager;
import cn.longmaster.hospital.doctor.view.pdfviewpager.library.adapter.PDFPagerAdapter;
import cn.longmaster.hospital.doctor.view.pdfviewpager.library.remote.DownloadFile;

/**
 * Created by YY on 18/3/31.
 */

public class PDFViewActivity extends BaseActivity implements DownloadFile.Listener {
    @FindViewById(R.id.action_title)
    private TextView mTitleTv;
    @FindViewById(R.id.remote_pdf_root)
    private LinearLayout mRootLl;
    @FindViewById(R.id.activity_pdf_view_top_loading_pb)
    private ProgressBar mProgressBar;

    private RemotePDFViewPager mRemotePDFViewPager;
    private PDFPagerAdapter mAdapter;
    private String mPdfUrl;
    private String mTitle;
    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private boolean mIsMyData;
    private int mMaterialId;

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, PDFViewActivity.class);
        intent.putExtra("pdf_url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        ViewInjecter.inject(this);
        initData();
        mTitleTv.setText(mTitle);
        setDownloadButtonListener();
    }

    private void initData() {
        mPdfUrl = getIntent().getStringExtra("pdf_url");
        mTitle = getIntent().getStringExtra("title");
        mMaterialId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, 0);
        mIsMyData = getIntent().getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MY_DATA, false);
        Logger.logD(Logger.COMMON, "PDFViewActivity()->mPdfUrl:" + mPdfUrl + ", mMaterialId:" + mMaterialId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAdapter != null) {
            mAdapter.close();
        }
    }

    @OnClick({R.id.action_left_btn,
            R.id.action_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_left_btn:
                finish();
                break;
            case R.id.action_right_btn:
                if (mShareDialog == null) {
                    initShareDialog(true);
                }
                mShareEntity = new ShareEntity();
                mShareEntity.setTitle(mTitle);
                mShareEntity.setContent("");
                mShareEntity.setUrl(mPdfUrl);
                mShareDialog.show();
                break;
            default:
                break;
        }
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        mRemotePDFViewPager = new RemotePDFViewPager(ctx, mPdfUrl, listener);
    }

    public void updateLayout() {
        mRootLl.removeAllViewsInLayout();
        mRootLl.addView(mRemotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initShareDialog(boolean mIsUsetitle) {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        if (!mIsUsetitle) {
            shareItemList.add(new ShareItem(ShareManager.ITEM_SAVE_IMG, R.drawable.ic_share_save_img, getString(R.string.share_save_img)));
        }
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        mShareDialog = new ShareDialog(this, shareItemList);
        mShareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                getShareManager().shareToWeiChat(PDFViewActivity.this, mShareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(PDFViewActivity.this, mShareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(PDFViewActivity.this, mShareEntity);
            }

            @Override
            public void onMyConsultClick() {
            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Label", mPdfUrl));
                showToast(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {
                savePictureToLocal(viewShot(mRootLl));
            }
        });
    }

    public Bitmap viewShot(final View view) {
        if (view == null) {
            return null;
        }
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);
        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
            return null;
        }
        Bitmap bm;
        try {
            bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_4444);
            } catch (OutOfMemoryError ee) {
                return null;
            }
        }
        Canvas bigCanvas = new Canvas(bm);
        Paint paint = new Paint();
        int iHeight = bm.getHeight();
        bigCanvas.drawBitmap(bm, 0, iHeight, paint);
        view.draw(bigCanvas);
        showToast(getString(R.string.already_share_save_img));
        return bm;
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        mProgressBar.setVisibility(View.GONE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.M) {
            showToast("系统版本过低，暂不支持该功能");
            return;
        }

        mAdapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        mRemotePDFViewPager.setAdapter(mAdapter);
        updateLayout();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        mProgressBar.setProgress((int) ((progress / (total * 1.0)) * 100));
    }
}
