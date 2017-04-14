package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
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

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.CHINA_MOBILE_URL_KEY);
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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {
        wvChinaMobile.addJavascriptInterface(new JSCallback(), "tianshen");
        String userId = TianShenUserUtil.getUserId(mContext);
        String userToken = TianShenUserUtil.getUserToken(mContext);
        mUrl = mUrl + "?src=android&uid=" + userId + "&token=" + userToken;
        LogUtil.d("abc", "运营商URL--->" + mUrl);
        wvChinaMobile.loadUrl(mUrl);
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

        @JavascriptInterface
        public void authCallBack(boolean result) {
            ToastUtil.showToast(mContext, "运营商认证结果-->" + result);
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
