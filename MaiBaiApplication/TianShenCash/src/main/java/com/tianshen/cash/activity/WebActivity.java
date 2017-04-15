package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;

import butterknife.BindView;

/**
 * 展示H5
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_web_back)
    TextView tv_web_back;
    @BindView(R.id.wv_web)
    WebView wv_web;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.WEB_URL_KEY);
        initWebView();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_web;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void setListensers() {
        tv_web_back.setOnClickListener(this);
    }

    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = wv_web.getSettings();
        // 打开页面时， 自适应屏幕
//        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {
        wv_web.loadUrl(mUrl);
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_web_back:
                backActivity();
                break;
        }
    }
}
