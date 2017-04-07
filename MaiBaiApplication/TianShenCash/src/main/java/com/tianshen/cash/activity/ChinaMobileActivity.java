package com.tianshen.cash.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.utils.ToastUtil;

import butterknife.BindView;

/**
 * 运营商认证页面
 */

public class ChinaMobileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_china_mobile_back)
    TextView tvChinaMobileBack;
    @BindView(R.id.wv_china_mobile)
    WebView wvChinaMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_china_mobile;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void setListensers() {
        tvChinaMobileBack.setOnClickListener(this);
    }

    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = wvChinaMobile.getSettings();
        // 打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setAppCacheEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    private void setWebView() {

        String url = "http://118.190.83.21:8081/h5/mobile_auth/mobile_auth.html";
        wvChinaMobile.loadUrl(url);
        wvChinaMobile.addJavascriptInterface(new JSCallback(), "android");
        wvChinaMobile.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    public class JSCallback {

        //将显示Toast和对话框的方法暴露给JS脚本调用
        @JavascriptInterface
        public void showToast(String str) {
            ToastUtil.showToast(mContext, "JS-->" + str);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_china_mobile_back:
                backActivity();
                break;
        }
    }
}
