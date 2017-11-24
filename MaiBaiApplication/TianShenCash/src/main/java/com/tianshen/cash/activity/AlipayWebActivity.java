package com.tianshen.cash.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.LogUtil;

import butterknife.BindView;

/**
 * 支付宝还款页面
 */

public class AlipayWebActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_web_back)
    TextView tv_web_back;

    @BindView(R.id.tv_web_exit)
    TextView tv_web_exit;

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
        return R.layout.activity_alipay_web;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void setListensers() {
        tv_web_back.setOnClickListener(this);
        tv_web_exit.setOnClickListener(this);
    }

    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_web != null) {
            ViewGroup parent = (ViewGroup) wv_web.getParent();
            if (parent != null) {
                parent.removeView(wv_web);
            }
            wv_web.removeAllViews();
            wv_web.destroy();
            wv_web = null;
        }
    }

    private void setWebViewSettings() {
        WebSettings webSettings = wv_web.getSettings();
        // 打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {

        LogUtil.d("abc", "mUrl--->" + mUrl);

        wv_web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //使用系统自带的浏览器下载
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {

                }
            }
        });
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("abc", "mUrl--->" + url);
                if (url != null) {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    } else {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (Exception e) {
                        }
                    }
                }
                return true;
            }
        });
        wv_web.loadUrl(mUrl);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_web_back:
                boolean canGoBack = wv_web.canGoBack();
                if (canGoBack) {
                    wv_web.goBack();
                } else {
                    backActivity();
                }
                break;
            case R.id.tv_web_exit:
                backActivity();
                break;
            case R.id.iv_web_share:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            boolean canGoBack = wv_web.canGoBack();
            if (canGoBack) {
                wv_web.goBack();
            } else {
                backActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
