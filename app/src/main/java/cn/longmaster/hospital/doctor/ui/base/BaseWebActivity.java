package cn.longmaster.hospital.doctor.ui.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.manager.common.FileDownLoadManager;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.ui.account.MyAccountActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.util.BitmapUtils;
import cn.longmaster.hospital.doctor.view.MyStatusBar;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/6/17 13:34
 * @description: WebView基类
 */
public abstract class BaseWebActivity extends BaseToolbarActivity implements DownloadListener {
    @FindViewById(R.id.action_bar_status_bar)
    private MyStatusBar actionBarStatusBar;
    @FindViewById(R.id.activity_base_wv)
    private WebView activityBaseWv;
    @FindViewById(R.id.activity_browser_top_loading_pb)
    private ProgressBar activityBrowserTopLoadingPb;
    private final int FILE_CHOOSER_RESULT_CODE = 10000;
    private final int SYSTEM_API = 21;//系统API为21,即Android5.0系统
    private String mLoadUrl;
    private String mShareUrl;
    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private ShareDialog mShareFileDialog;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    private ProgressDialog mProgressDialog;
    private FileDownLoadManager.FileDownLoadListener mFileDownLoadListener;
    @AppApplication.Manager
    private FileDownLoadManager mFileDownLoadManager;
    private ShareManager mShareManager;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void initDatas() {
        mLoadUrl = getLoadUrl();
        mShareManager = new ShareManager(this);
    }

    @Override
    protected void initViews() {
        initWebSetting(activityBaseWv);
        activityBaseWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                return null;
            }
        });
        activityBaseWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (100 == newProgress) {

                }
                super.onProgressChanged(view, newProgress);
            }
        });
        WebSettings webSettings = activityBaseWv.getSettings();


        if (StringUtils.isTrimEmpty(mLoadUrl) && mLoadUrl.contains("http")) {
            activityBaseWv.loadUrl(mLoadUrl);
        } else {
            Logger.logE(Logger.USER, "Web url is null or not url");
        }
    }

    /**
     * 获取weburl加载地址
     *
     * @return
     */
    public abstract String getLoadUrl();


    protected void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebSetting(WebView webView) {
        activityBaseWv.setDownloadListener(this);
        activityBaseWv.addJavascriptInterface(this, "native");
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setPluginState(WebSettings.PluginState.ON);
        // add java script interface
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        settings.setJavaScriptEnabled(true);
        //intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis());
        //activityBaseWv.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, intent), "sonic");
        // init webview settings
        settings.setAllowContentAccess(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
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

    private void initShareFileDialog() {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        mShareFileDialog = new ShareDialog(this, shareItemList);
        mShareFileDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                if (FileUtil.getFileSize(mShareEntity.getPath()) > 1024 * 1024 * 10) {
                    ToastUtils.showShort(R.string.not_support_share);
                } else {
                    mShareManager.shareFileToWeiXin(mShareEntity);
                }
            }

            @Override
            public void onFriendCircleClick() {
            }

            @Override
            public void onQqClick() {
                mShareManager.shareFileToQq(getThisActivity(), mShareEntity);
            }

            @Override
            public void onMyConsultClick() {
            }

            @Override
            public void onCopyLinkClick() {
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {
            }
        });
        mFileDownLoadListener = new FileDownLoadManager.FileDownLoadListener() {
            @Override
            public void onProgressChanged(String fileUrl, int percent) {
                if (mProgressDialog != null && mProgressDialog.isShowing() && fileUrl.equals(mShareEntity.getUrl())) {
                    mProgressDialog.setProgress(percent);
                }
            }

            @Override
            public void onFileDownLoadFinished(final String fileUrl, final String downLoadUrl) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                AppHandlerProxy.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fileUrl.equals(mShareEntity.getUrl())) {
                            if (mShareFileDialog != null) {
                                mShareFileDialog.show();
                            }
                        }
                    }
                });
            }
        };
    }

    private void initShareDialog(boolean saveImg) {
        List<ShareItem> shareItemList = new ArrayList<>();
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN, R.drawable.ic_share_wei_chat, getString(R.string.share_wei_chat)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_WEXIN_CIRCLE, R.drawable.ic_share_friend_circle, getString(R.string.share_friend_circle)));
        shareItemList.add(new ShareItem(ShareManager.ITEM_QQ, R.drawable.ic_share_qq, getString(R.string.share_qq)));
        if (saveImg) {
            shareItemList.add(new ShareItem(ShareManager.ITEM_SAVE_IMG, R.drawable.ic_share_save_img, getString(R.string.share_save_img)));
        }
        shareItemList.add(new ShareItem(ShareManager.ITEM_COPY_LINK, R.drawable.ic_share_copy_link, getString(R.string.share_copy_link)));
        mShareDialog = new ShareDialog(this, shareItemList);
        mShareDialog.setOnShareClickListener(new ShareDialog.OnShareClickListener() {
            @Override
            public void onWeiChatClick() {
                mShareManager.shareToWeiChat(getThisActivity(), mShareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                mShareManager.shareToWeiCircle(getThisActivity(), mShareEntity);
            }

            @Override
            public void onQqClick() {
                mShareManager.shareToQq(getThisActivity(), mShareEntity);
                Logger.logD(Logger.COMMON, TAG + "->onQqClick()->mShareEntity:" + mShareEntity);
            }

            @Override
            public void onMyConsultClick() {
            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Label", StringUtils.isEmpty(mShareUrl) ? getLoadUrl() : mShareUrl));
                ToastUtils.showShort(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {
                BitmapUtils.savePictureToLocal(viewShot(activityBaseWv));
            }
        });
    }

    @JavascriptInterface
    public void startFunction(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                doFunction(data);
            }
        });
    }

    private void doFunction(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }

        try {
            final JSONObject jsonObject = new JSONObject(data);
            Logger.logD(Logger.COMMON, TAG + "->onQqClick()->jsonObject:" + jsonObject);
            mShareUrl = jsonObject.optString("shareUrl");
            Intent intent = new Intent();
            switch (jsonObject.optInt("messageType")) {
                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_FINISH:
                    setResult(RESULT_OK);
                    finish();
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_SHARE_DOCTOR:
                    if (mShareDialog == null) {
                        initShareDialog(true);
                    }
                    mShareEntity = new ShareEntity();
                    mShareEntity.setTitle(jsonObject.optString("shareTitle"));
                    mShareEntity.setContent(jsonObject.optString("shareDesc"));
                    mShareEntity.setUrl(jsonObject.optString("shareUrl"));
                    mShareEntity.setImgUrl(!jsonObject.optString("shareImage").startsWith("http") ? "" : jsonObject.optString("shareImage"));
                    mShareDialog.show();
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_DATA_MANAGE:
                    intent.setClass(getThisActivity(), ConsultDataManageActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_APPOINTMENT_ID, jsonObject.optInt("appointmentId"));
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_CLINIC, true);
                    startActivity(intent);
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_SHARE_URL:
                    if (mShareDialog == null) {
                        initShareDialog(false);
                    }
                    mShareEntity = new ShareEntity();
                    mShareEntity.setTitle(jsonObject.optString("shareTitle"));
                    mShareEntity.setContent(jsonObject.optString("shareDesc"));
                    mShareEntity.setUrl(jsonObject.optString("shareUrl"));
                    mShareEntity.setImgUrl(jsonObject.optString("shareImage"));
                    mShareDialog.show();
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_SHARE_FILE:
                    if (mShareFileDialog == null) {
                        initShareFileDialog();
                    }
                    mShareEntity = new ShareEntity();
                    mShareEntity.setTitle(jsonObject.optString("shareTitle"));
                    mShareEntity.setContent("");
                    mShareEntity.setUrl(jsonObject.optString("shareUrl"));
                    mShareEntity.setImgUrl("");
                    mShareEntity.setPath(mFileDownLoadManager.getFilePath(mShareEntity.getUrl()));
                    if (!mFileDownLoadManager.downloadingFile(mShareEntity.getUrl()) && FileUtil.isFileExist(mShareEntity.getPath())) {
                        mShareFileDialog.show();
                    } else {
                        if (mProgressDialog == null) {
                            mProgressDialog = createProgressDialog(getString(R.string.downloading));
                        } else {
                            mProgressDialog.show();
                        }
                        mFileDownLoadManager.startDownload(getThisActivity(), mShareEntity.getUrl(), mShareEntity.getTitle(), mFileDownLoadListener);
                    }
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_ACCOUNT:
                    intent.setClass(getThisActivity(), MyAccountActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, jsonObject.optInt("user_id"));
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_MY_DOCTOR, true);
                    startActivity(intent);
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_ROUNDS_SHARE:
                    Logger.logD(Logger.COMMON, TAG + "查房管理分享->jsonObject:" + jsonObject + ",imageUrl:" + jsonObject.optString("shareImage"));
//                    mRightBtn.setVisibility(View.VISIBLE);
//                    mIsRoundsShare = true;
                    if (mShareDialog == null) {
                        initShareDialog(false);
                    }
                    mShareEntity = new ShareEntity();
                    mShareEntity.setTitle(jsonObject.optString("shareTitle"));
                    mShareEntity.setContent(jsonObject.optString("shareDesc"));
                    mShareEntity.setUrl(jsonObject.optString("shareUrl"));
                    mShareEntity.setImgUrl(jsonObject.optString("shareImage"));
                    //mShareDialog.show();
                    break;
                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_IMG_DOWNLOAD:
                    mFileDownLoadManager.startDownload(getThisActivity(), jsonObject.optString("photoUrl"), "正在下载", new FileDownLoadManager.FileDownLoadListener() {
                        @Override
                        public void onProgressChanged(String fileUrl, int percent) {

                        }

                        @Override
                        public void onFileDownLoadFinished(String fileUrl, String downLoadPath) {
                            runOnUiThread(() -> ToastUtils.showShort("图片已保存到:" + downLoadPath));
                        }
                    });
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存到本地相册
     *
     * @param bitmap 图片bitmap
     * @return 图片路径
     */
    public String savePictureToLocal(Bitmap bitmap) {
        String bitPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", "");
        if (TextUtils.isEmpty(bitPath)) {
            return "";
        }
        Uri uuUri = Uri.parse(bitPath);
        String path = FileUtil.getRealPathFromURI(this, uuUri);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(path)));
        sendBroadcast(intent);
        return path;
    }
}
