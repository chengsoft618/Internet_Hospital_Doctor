package cn.longmaster.hospital.doctor.ui.rounds.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.core.ExtraDataKeyConfig;
import cn.longmaster.hospital.doctor.core.entity.rounds.OrderDetailsInfo;
import cn.longmaster.hospital.doctor.ui.base.NewBaseFragment;
import cn.longmaster.utils.StringUtils;
import cn.longmaster.utils.ToastUtils;

/**
 * Created by W·H·K on 2019/2/13.
 */

public class SummaryFragment extends NewBaseFragment {
    private final int FILE_CHOOSER_RESULT_CODE = 10000;
    @FindViewById(R.id.layout_summary_fragment_browser_browser_wv)
    private WebView mBrowserWv;
    @FindViewById(R.id.layout_summary_fragment_browser_top_loading_pb)
    private ProgressBar mLoadingPb;

    private String mCurrentLoadUrl;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private OrderDetailsInfo mOrderDetailsInfo;

    public static SummaryFragment getInstance(OrderDetailsInfo orderDetailsInfo) {
        SummaryFragment summaryFragment = new SummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO, orderDetailsInfo);
        summaryFragment.setArguments(bundle);
        return summaryFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_summary_fragment;
    }

    @Override
    public void initViews(View rootView) {
        initData();
        initWebView();
        dealWebViewBrowser();
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.logI(Logger.COMMON, "SummaryFragment-->onStart");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !StringUtils.isEmpty(mCurrentLoadUrl)) {
            Logger.logI(Logger.COMMON, "SummaryFragment-->onHiddenChanged");
            dealWebViewBrowser();
        }
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOrderDetailsInfo = (OrderDetailsInfo) bundle.getSerializable(ExtraDataKeyConfig.EXTRA_DATA_KEY_ORDER_INFO);
            Logger.logI(Logger.COMMON, "mOrderDetailsInfo:" + mOrderDetailsInfo);
        }
        if (mOrderDetailsInfo != null) {
            mCurrentLoadUrl = mOrderDetailsInfo.getMedicalSummaryUrl();
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        mBrowserWv.getSettings().setDefaultTextEncodingName("UTF-8");
        mBrowserWv.getSettings().setSupportZoom(true);
        mBrowserWv.getSettings().setBuiltInZoomControls(true);
        mBrowserWv.getSettings().setJavaScriptEnabled(true);
        mBrowserWv.getSettings().setDomStorageEnabled(true);
        mBrowserWv.setDownloadListener(new MyWebViewDownLoadListener());
        mBrowserWv.getSettings().setUseWideViewPort(true);
        mBrowserWv.getSettings().setLoadWithOverviewMode(true);
        mBrowserWv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mBrowserWv.addJavascriptInterface(getActivity(), "native");
        mBrowserWv.getSettings().setPluginState(WebSettings.PluginState.ON);
    }

    private void dealWebViewBrowser() {
        if (mCurrentLoadUrl == null) {
            ToastUtils.showShort("该页面不存在，请检查你的地址");
            return;
        }

        mBrowserWv.loadUrl(mCurrentLoadUrl);
        mBrowserWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //mTitleTv.setText(getString(R.string.loading));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //mTitleTv.setText(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mCurrentLoadUrl = url;
                view.loadUrl(url);
                return true;
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
                Logger.logD(Logger.COMMON, "->onReceivedTitle()->title:" + title);
                ///mTitleTv.setText(title);
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

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /*  @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK && mBrowserWv.canGoBack()) {  //表示按返回键时的操作
              mBrowserWv.goBack();   //后退
              return true;    //已处理
          }
          return super.onKeyDown(keyCode, event);
      }*/
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    public void onDestroy() {
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
}
