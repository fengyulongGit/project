package com.creditpomelo.accounts.main.web.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.base.AppCompatActivity;

import butterknife.BindView;

public class WebActivity extends AppCompatActivity {
    @BindView(R.id.webview)
    public WebView webview;

    private String url = "";
    private String title;

    private boolean isSupportZoom = false;

    @Override
    protected int layoutContentResID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initViewData() {

        try {
            CookieSyncManager.createInstance(this);
            CookieSyncManager.getInstance().startSync();
            CookieManager.getInstance().removeSessionCookie();
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.clearCache(true);
        webview.clearHistory();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            title = bundle.getString("title");

            isSupportZoom = bundle.getBoolean("is_support_zoom", false);
        }
        tv_title.setText(title);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("GBK");
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (isSupportZoom) {
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true);

            webSettings.setDisplayZoomControls(false);
        }

        webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_title.setText(title);
            }

        });
        if (Build.VERSION.SDK_INT >= 17) {
            webview.addJavascriptInterface(new WebAppInterface(this), "accounts");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webview.removeJavascriptInterface("accessibility");
                webview.removeJavascriptInterface("accessibilityTraversal");
                webview.removeJavascriptInterface("searchBoxJavaBridge_");
            }
        }
        webview.loadUrl(url);
    }

    @Override
    protected void requestData() {
    }

    @Override
    protected void onDestroy() {
        webview.clearCache(true);
        webview.clearHistory();

        super.onDestroy();
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候
            showProgressDialog("");
            if (tv_right != null) {
                tv_right.setVisibility(View.INVISIBLE);
            }
            webview.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
            // super.onPageFinished(view, url);
            try {
                tv_title.setText(webview.getTitle());
                closeProgressDialog();
                webview.setEnabled(true);

//                webview.loadUrl("javascript:showShareBtn()");
//                webview.loadUrl("javascript:showTipsBtn()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 网页加载时的连接的网址
            tv_right.setVisibility(View.INVISIBLE);
//            view.loadUrl(url);
            return false;
        }
    }

    @Override
    public void left() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (webview.canGoBack()) {
            webview.goBack();
            tv_right.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * 返回上一页面
         */
        @JavascriptInterface
        public void toBack() {
            finish();
        }
    }
}

