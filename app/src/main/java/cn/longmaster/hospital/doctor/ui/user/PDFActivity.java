package cn.longmaster.hospital.doctor.ui.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.ToastUtils;

import static cn.longmaster.hospital.doctor.util.BitmapUtils.savePictureToLocal;

public class PDFActivity extends NewBaseActivity {
    @FindViewById(R.id.activity_pdf_view_web)
    private WebView mWebView;
    @FindViewById(R.id.activity_pdf_action_title)
    private TextView mTitleTv;
    private String mPdfUrl;
    private String mTitle;
    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private int mMaterialId;
    private String mShareUrl;

    @Override
    protected void initDatas() {
        mPdfUrl = getIntent().getStringExtra("pdf_url");
        mTitle = getIntent().getStringExtra("title");
        mMaterialId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, 0);
        Logger.logD(Logger.COMMON, "PDFViewActivity()->mPdfUrl:" + mPdfUrl + ", mMaterialId:" + mMaterialId);
        mShareUrl = mPdfUrl;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_pdf;
    }

    @Override
    protected void initViews() {
        mTitleTv.setText(mTitle);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                Logger.logD(Logger.COMMON, "PDFViewActivity()->initView--> url:" + url);
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        if (!"".equals(mPdfUrl)) {
            byte[] bytes = null;
            try {// 获取以字符编码为utf-8的字符
                bytes = mPdfUrl.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (bytes != null) {
                mPdfUrl = new BASE64Encoder().encode(bytes);// BASE64转码
            }
        }
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.M) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("系统版本过低，暂不支持在线预览");
                }
            }, 200);
            return;
        }
        mWebView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + mPdfUrl);
    }


    @OnClick({R.id.activity_pdf_action_left_btn,
            R.id.activity_pdf_action_right_btn,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_pdf_action_left_btn:
                finish();
                break;

            case R.id.activity_pdf_action_right_btn:
                if (mShareDialog == null) {
                    initShareDialog(true);
                }
                mShareEntity = new ShareEntity();
                mShareEntity.setTitle(mTitle);
                mShareEntity.setContent("");
                mShareEntity.setUrl(mShareUrl);
                mShareDialog.show();
                break;
            default:
                break;
        }
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
                getShareManager().shareToWeiChat(PDFActivity.this, mShareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(PDFActivity.this, mShareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(PDFActivity.this, mShareEntity);
            }

            @Override
            public void onMyConsultClick() {
            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Label", mShareUrl));
                ToastUtils.showShort(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {
                savePictureToLocal(viewShot(mWebView));
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
        ToastUtils.showShort(R.string.already_share_save_img);
        return bm;
    }
}