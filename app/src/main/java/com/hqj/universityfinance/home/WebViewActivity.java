package com.hqj.universityfinance.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hqj.universityfinance.R;


/**
 * Created by wang on 17-9-13.
 */

public class WebViewActivity extends Activity {

    private WebView mWebView;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        init();
        mWebView.loadUrl(getIntent().getData().toString());
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.news_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    closeDialog();
                } else {
                    openLoadingDialog(newProgress);
                }
            }
        });

    }

    private void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    private void openLoadingDialog(int progress) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setTitle(R.string.loading_text);
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setProgress(progress);
            mDialog.show();
        } else {
            mDialog.setProgress(progress);
        }
    }

}
