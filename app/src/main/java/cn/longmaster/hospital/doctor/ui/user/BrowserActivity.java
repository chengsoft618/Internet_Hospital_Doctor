package cn.longmaster.hospital.doctor.ui.user;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.longmaster.doctorlibrary.util.common.FileUtil;
import cn.longmaster.doctorlibrary.util.handler.AppHandlerProxy;
import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.doctorlibrary.viewinject.OnClick;
import cn.longmaster.doctorlibrary.viewinject.ViewInjecter;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.AppApplication;
import cn.longmaster.hospital.doctor.core.AppConstant;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.ShareEntity;
import cn.longmaster.hospital.doctor.core.entity.common.ShareItem;
import cn.longmaster.hospital.doctor.core.manager.common.FileDownLoadManager;
import cn.longmaster.hospital.doctor.core.manager.common.ShareManager;
import cn.longmaster.hospital.doctor.core.manager.storage.SdManager;
import cn.longmaster.hospital.doctor.ui.BaseActivity;
import cn.longmaster.hospital.doctor.ui.account.MyAccountActivity;
import cn.longmaster.hospital.doctor.ui.consult.ConsultDataManageActivity;
import cn.longmaster.hospital.doctor.util.WebUrlConstant;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.dialog.ShareDialog;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

import static android.webkit.WebView.enableSlowWholeDocumentDraw;


/**
 * 内置浏览器界面
 * Created by Yang² on 2016/6/3.
 */
@SuppressWarnings("deprecation")
public class BrowserActivity extends BaseActivity {
    private final String TAG = BrowserActivity.class.getSimpleName();
    @FindViewById(R.id.activity_browser_browser_wv)
    private WebView mBrowserWv;
    @FindViewById(R.id.activity_browser_top_loading_pb)
    private ProgressBar mLoadingPb;
    @FindViewById(R.id.action_right_btn)
    private ImageView mRightBtn;
    @FindViewById(R.id.action_title)
    private TextView mTitleTv;
    private String mTitle;
    private String mCurrentLoadUrl;

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private final int FILE_CHOOSER_RESULT_CODE = 10000;
    private final int SYSTEM_API = 21;//系统API为21,即Android5.0系统

    private ShareDialog mShareDialog;
    private ShareEntity mShareEntity;
    private boolean mIsUseTitle;
    private boolean mIsMyData;
    private int mMaterialId;
    private ShareDialog mShareFileDialog;
    private FileDownLoadManager.FileDownLoadListener mFileDownLoadListener;
    private ProgressDialog mProgressDialog;
    private String mShareUrl;
    @AppApplication.Manager
    private FileDownLoadManager mFileDownLoadManager;
    private boolean mIsRoundsShare = false;//是否是查房分享

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= SYSTEM_API) {
            enableSlowWholeDocumentDraw();
        }
        setContentView(R.layout.activity_browser);
        ViewInjecter.inject(this);
        initData();
        initWebView();
        dealWebViewBrowser();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mBrowserWv.canGoBack()) {  //表示按返回键时的操作
            if (canGoBack(mBrowserWv.getUrl())) {
                finish();
            } else {
                mBrowserWv.goBack();   //后退
                return true;    //已处理
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        Intent intent = getIntent();
        mCurrentLoadUrl = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_LOADING_URL);
        mTitle = intent.getStringExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE);
        mIsUseTitle = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_TITLE_NAME, false);
        mIsMyData = intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_MY_DATA, false);
        mMaterialId = getIntent().getIntExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_MATERIAL_ID, 0);
        Logger.logD(Logger.COMMON, TAG + "->initData()->url:" + mCurrentLoadUrl + ", title:" + mTitle);
        if (!StringUtils.isEmpty(mTitle)) {
            // setTitle(mTitle);
            mTitleTv.setText(mTitle);
        }

        if (intent.getBooleanExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_IS_SETTLED, false)) {//医生入驻显示入驻知情框
            showSettledDialog();
        }
        if (mIsUseTitle) {
            mRightBtn.setVisibility(View.VISIBLE);
        }
        mRightBtn.setVisibility(mIsMyData ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.action_left_btn,
            R.id.action_right_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_left_btn:
                if (canGoBack(mBrowserWv.getUrl())) {
                    finish();
                } else {
                    if (mBrowserWv.canGoBack()) {
                        mBrowserWv.goBack();
                    } else {
                        super.onBackPressed();
                    }
                }
                break;
            case R.id.action_right_btn:
                if (mIsRoundsShare) {
                    if (mShareDialog == null) {
                        initShareDialog(false);
                    }
                    mShareDialog.show();
                } else {
                    if (mShareDialog == null) {
                        initShareDialog(false);
                    }
                    mShareEntity = new ShareEntity();
                    mShareEntity.setTitle(mTitle);
                    mShareEntity.setContent("");
                    mShareEntity.setUrl(mCurrentLoadUrl);
                    mShareDialog.show();
                }
                break;
            default:
                break;
        }
    }

    private boolean canGoBack(String url) {
        if (StringUtils.isTrimEmpty(url)) {
            return false;
        }
        for (String homeUrl : WebUrlConstant.getInstance().getHomePages()) {
            if (url.contains(homeUrl)) {
                return true;
            }
        }
        return false;
    }

    private void wvGoback(String url) {

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        mBrowserWv.getSettings().setDefaultTextEncodingName("UTF-8");
        mBrowserWv.getSettings().setSupportZoom(true);
        mBrowserWv.getSettings().setBuiltInZoomControls(true);
        mBrowserWv.getSettings().setJavaScriptEnabled(true);
        mBrowserWv.getSettings().setDomStorageEnabled(true);
        mBrowserWv.getSettings().setUseWideViewPort(true);
        mBrowserWv.getSettings().setLoadWithOverviewMode(true);
        mBrowserWv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBrowserWv.getSettings().setPluginState(WebSettings.PluginState.ON);
        mBrowserWv.setDownloadListener(new MyWebViewDownLoadListener());
        mBrowserWv.addJavascriptInterface(BrowserActivity.this, "native");
    }

    private void dealWebViewBrowser() {
        if (mCurrentLoadUrl == null) {
            showToast("该页面不存在，请检查你的地址");
            finish();
            return;
        }
        mBrowserWv.loadUrl(mCurrentLoadUrl);
        mBrowserWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Logger.logD(Logger.COMMON, TAG + "->onPageStarted");
                if (!mIsUseTitle) {
                    mRightBtn.setVisibility(View.GONE);
                }
                if (!mIsUseTitle) {
                    mTitleTv.setText(getString(R.string.loading));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Logger.logD(Logger.COMMON, TAG + "->onPageFinished:url:" + url);
                if (!mIsUseTitle) {
                    mTitleTv.setText(view.getTitle());
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mCurrentLoadUrl = url;
//               view.loadUrl(url);
//                return true;
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();//接受证书，解决webview部分机型https无法打开问题。
            }
        });

        mBrowserWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                mLoadingPb.setProgress(progress);
                if (progress == 100) {
                    mLoadingPb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Logger.logD(Logger.COMMON, TAG + "->onReceivedTitle()->title:" + title);
                if (!mIsUseTitle) {
                    mTitleTv.setText(title);
                }
            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadMessage = valueCallback;
                openImageChooserActivity();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
                return true;
            }
        });
    }

    private void setTitle(String title) {
//        if (title.length() > 10)
//            title = title.substring(0, 10) + "...";
        mTitleTv.setText(title);
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) {
                return;
            }
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //清空所有Cookie
        CookieSyncManager.createInstance(getActivity());  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        mBrowserWv.setWebChromeClient(null);
        mBrowserWv.setWebViewClient(null);
        mBrowserWv.getSettings().setJavaScriptEnabled(false);
        mBrowserWv.clearCache(true);
    }

    @Override
    protected void onPause() {
        callHiddenWebViewMethod("onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        callHiddenWebViewMethod("onResume");
        super.onResume();
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
                    intent.setClass(getActivity(), ConsultDataManageActivity.class);
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
                        mFileDownLoadManager.startDownload(getActivity(), mShareEntity.getUrl(), mShareEntity.getTitle(), mFileDownLoadListener);
                    }
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_ACCOUNT:
                    intent.setClass(getActivity(), MyAccountActivity.class);
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_ACCOUNT_ID, jsonObject.optInt("user_id"));
                    intent.putExtra(ExtraDataKeyConfig.EXTRA_DATA_KEY_FROM_MY_DOCTOR, true);
                    startActivity(intent);
                    break;

                case AppConstant.H5MessageType.H5_MESSAGE_TYPE_ROUNDS_SHARE:
                    Logger.logD(Logger.COMMON, TAG + "查房管理分享->jsonObject:" + jsonObject + ",imageUrl:" + jsonObject.optString("shareImage"));
                    mRightBtn.setVisibility(View.VISIBLE);
                    mIsRoundsShare = true;
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
                    if (mProgressDialog == null) {
                        mProgressDialog = createProgressDialog(getString(R.string.downloading));
                    } else {
                        mProgressDialog.show();
                    }
                    mFileDownLoadManager.startDownload(getActivity(), jsonObject.optString("photoUrl"), "正在下载", SdManager.getInstance().getAlbumPath(), new FileDownLoadManager.FileDownLoadListener() {
                        @Override
                        public void onProgressChanged(String fileUrl, int percent) {
                            if (mProgressDialog != null && mProgressDialog.isShowing() && fileUrl.equals(mShareEntity.getUrl())) {
                                mProgressDialog.setProgress(percent);
                            }
                        }

                        @Override
                        public void onFileDownLoadFinished(String fileUrl, String downLoadPath) {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            runOnUiThread(() -> {
                                if (StringUtils.isTrimEmpty(downLoadPath)) {
                                    ToastUtils.showShort("下载失败");
                                } else {
                                    ToastUtils.showShort("图片已保存到:" + downLoadPath);
                                    //通知相册刷新
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(new File(downLoadPath)));
                                    sendBroadcast(intent);
                                }
                            });
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
                getShareManager().shareToWeiChat(BrowserActivity.this, mShareEntity);
            }

            @Override
            public void onFriendCircleClick() {
                getShareManager().shareToWeiCircle(BrowserActivity.this, mShareEntity);
            }

            @Override
            public void onQqClick() {
                getShareManager().shareToQq(BrowserActivity.this, mShareEntity);
                Logger.logD(Logger.COMMON, TAG + "->onQqClick()->mShareEntity:" + mShareEntity);
            }

            @Override
            public void onMyConsultClick() {
            }

            @Override
            public void onCopyLinkClick() {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Label", StringUtils.isEmpty(mShareUrl) ? mCurrentLoadUrl : mShareUrl));
                showToast(R.string.share_link_copied);
            }

            @Override
            public void onQrCodeClick() {
            }

            @Override
            public void onSaveImgClick() {
                savePictureToLocal(viewShot(mBrowserWv));
            }
        });
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
                    showToast(R.string.not_support_share);
                } else {
                    getShareManager().shareFileToWeiXin(mShareEntity);
                }
            }

            @Override
            public void onFriendCircleClick() {
            }

            @Override
            public void onQqClick() {
                getShareManager().shareFileToQq(getActivity(), mShareEntity);
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
            public void onFileDownLoadFinished(String fileUrl, String downLoadPath) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                AppHandlerProxy.runOnUIThread(() -> {
                    if (fileUrl.equals(mShareEntity.getUrl())) {
                        if (mShareFileDialog != null) {
                            mShareFileDialog.show();
                        }
                    }
                });
            }
        };
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

    private void callHiddenWebViewMethod(String name) {
        if (mBrowserWv != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(mBrowserWv);
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    private void showSettledDialog() {
        CommonDialog.Builder builder = new CommonDialog.Builder(getActivity());
        builder.setMessage(R.string.user_settled_tip)
                .setNegativeButton(R.string.confirm, new CommonDialog.OnNegativeBtnClickListener() {
                    @Override
                    public void onNegativeBtnClicked() {

                    }
                }).show();
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /*public void leftClick(View view) {
        if (mBrowserWv.canGoBack()) {
            mBrowserWv.goBack();
            return;
        }
        finish();
    }*/

    /**
     * 清除WebView缓存
     */
    private void clearWebViewCache() {
        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    private void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    @Override
    public ProgressDialog createProgressDialog(String msg) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(true);// 设置在点击Dialog外是否取消Dialog进度条
        progressDialog.setTitle(msg);
        progressDialog.setMax(100);
        progressDialog.show();
        return progressDialog;
    }
}
