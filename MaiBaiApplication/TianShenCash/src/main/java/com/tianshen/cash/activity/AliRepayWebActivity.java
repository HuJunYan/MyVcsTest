package com.tianshen.cash.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * 支付宝还款页面
 */

public class AliRepayWebActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_ali_repay_back)
    TextView tv_ali_repay_back;
    @BindView(R.id.wv_ali_repay)
    WebView wv_ali_repay;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.WEB_URL_KEY);
        initWebView();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_ali_repay;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void setListensers() {
        tv_ali_repay_back.setOnClickListener(this);
    }

    private void initWebView() {
        setWebViewSettings();
        setWebView();
    }

    private void setWebViewSettings() {
        WebSettings webSettings = wv_ali_repay.getSettings();
        // 打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 便页面支持缩放
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//解决HTML显示不全的问题
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {
        wv_ali_repay.addJavascriptInterface(new JSCallback(), "tianshen");

        LogUtil.d("abc", "URL--->" + mUrl);

        wv_ali_repay.loadUrl(mUrl);
        wv_ali_repay.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    LogUtil.d("abc", "URL--->" + url);
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    /**
     * js调用安卓回调方法 window.tianshen.repayBack()
     */
    public class JSCallback {

        @JavascriptInterface
        public void repayBack() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    backHome();
                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ali_repay_back:
                backHome();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void backHome() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        backActivity();
    }


}
