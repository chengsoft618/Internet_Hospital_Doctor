package cn.longmaster.hospital.doctor.ui.consult.video;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.longmaster.doctorlibrary.util.log.Logger;
import cn.longmaster.doctorlibrary.viewinject.FindViewById;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.utils.ToastUtils;

/**
 * @author ABiao_Abiao
 * @date 2019/11/12 10:13
 * @description: 帮助中心
 */
public class HelpCoreActivity extends NewBaseActivity {
    @FindViewById(R.id.iv_tool_bar_back)
    private ImageView ivToolBarBack;
    @FindViewById(R.id.tv_tool_bar_title)
    private TextView tvToolBarTitle;
    @FindViewById(R.id.tv_tool_bar_sub)
    private TextView tvToolBarSub;
    @FindViewById(R.id.iv_tool_bar_sub)
    private ImageView ivToolBarSub;
    @FindViewById(R.id.activity_help_core_top_loading_pb)
    private ProgressBar activityHelpCoreTopLoadingPb;
    @FindViewById(R.id.activity_help_core_web_view)
    private WebView activityHelpCoreWebView;
    private String mCurrentLoadUrl = "http://adws.39hospital.com/qa/index";

    public static void start(Context context) {
        Intent intent = new Intent(context, HelpCoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_help_core;
    }

    @Override
    protected void initViews() {
        tvToolBarTitle.setText(R.string.consult_help_center);
        ivToolBarBack.setOnClickListener(v -> {
            finish();
        });
        initWebView();
        dealWebViewBrowser();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        activityHelpCoreWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        activityHelpCoreWebView.getSettings().setSupportZoom(true);
        activityHelpCoreWebView.getSettings().setBuiltInZoomControls(true);
        activityHelpCoreWebView.getSettings().setJavaScriptEnabled(true);
        activityHelpCoreWebView.getSettings().setDomStorageEnabled(true);
        activityHelpCoreWebView.setDownloadListener(new HelpCoreActivity.MyWebViewDownLoadListener());
        activityHelpCoreWebView.getSettings().setUseWideViewPort(true);
        activityHelpCoreWebView.getSettings().setLoadWithOverviewMode(true);
        activityHelpCoreWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        activityHelpCoreWebView.addJavascriptInterface(HelpCoreActivity.this, "native");
        activityHelpCoreWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void dealWebViewBrowser() {
        if (mCurrentLoadUrl == null) {
            ToastUtils.showShort("该页面不存在，请检查你的地址");
            finish();
            return;
        }

        activityHelpCoreWebView.loadUrl(mCurrentLoadUrl);
        activityHelpCoreWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mCurrentLoadUrl = url;
                view.loadUrl(url);
                return true;
            }
        });
        activityHelpCoreWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                activityHelpCoreTopLoadingPb.setProgress(progress);
                if (progress == 100) {
                    activityHelpCoreTopLoadingPb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Logger.logD(Logger.COMMON, "HelpCoreActivity->onReceivedTitle()->title:" + title);

            }

            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                return true;
            }
        });
    }
}

