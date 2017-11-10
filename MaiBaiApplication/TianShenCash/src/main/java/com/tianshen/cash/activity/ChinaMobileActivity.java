package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 运营商认证页面
 */

public class ChinaMobileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_china_mobile_back)
    TextView tvChinaMobileBack;
    @BindView(R.id.wv_china_mobile)
    WebView wvChinaMobile;

    @BindView(R.id.tv_china_mobile_exit)
    TextView tv_china_mobile_exit;

    @BindView(R.id.tv_china_mobile_title)
    TextView tv_china_mobile_title;

    private String mUrl;
    private String mTitle;
    private String mstatue="0";//默认的认证状态 为0

    private ArrayList<String> loadHistoryUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* mTitle = getIntent().getExtras().getString(GlobalParams.CHINA_MOBILE_TITLE_KEY);
        mUrl = getIntent().getExtras().getString(GlobalParams.CHINA_MOBILE_URL_KEY); */
        mTitle = getIntent().getStringExtra(GlobalParams.CHINA_MOBILE_TITLE_KEY);
        mUrl = getIntent().getStringExtra(GlobalParams.CHINA_MOBILE_URL_KEY);

        tv_china_mobile_title.setText(mTitle);
        initWebView();
        MaiDianUtil.ding(this,MaiDianUtil.FLAG_20);
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
        tv_china_mobile_exit.setOnClickListener(this);
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
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);//解决HTML显示不全的问题
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {
        wvChinaMobile.addJavascriptInterface(new JSCallback(), "tianshen");
        String userId = TianShenUserUtil.getUserId(mContext);
        String userToken = TianShenUserUtil.getUserToken(mContext);

        String and = "";
        if ("芝麻信用".equals(mTitle)) {
            and = "?";
        } else {
            and = "&";
        }
        mUrl = mUrl + and + "src=android&uid=" + userId + "&token=" + userToken;
        LogUtil.d("abc", "URL--->" + mUrl);
        wvChinaMobile.loadUrl(mUrl);
        wvChinaMobile.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    LogUtil.d("abc", "URL--->" + url);
                    view.loadUrl(url);
                    loadHistoryUrls.add(url);
                }
                return true;
            }
        });
    }

    public class JSCallback {

        @JavascriptInterface
        public void authCallBack(final boolean result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result) {
                        MaiDianUtil.ding(mContext,MaiDianUtil.FLAG_21);
                        ToastUtil.showToast(mContext, "认证成功!", Toast.LENGTH_LONG);
                        mstatue = "1";
                        setResultValue(mstatue);
                    } else {
                        MaiDianUtil.ding(mContext,MaiDianUtil.FLAG_22);
                        ToastUtil.showToast(mContext, "认证失败!", Toast.LENGTH_LONG);
                        mstatue = "0";
                        setResultValue(mstatue);
                    }
                    backActivity();
                }
            });

        }

        @JavascriptInterface
        public void authCallBackStr(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(mContext, result, Toast.LENGTH_LONG);
                    mstatue = result;
                    setResultValue(mstatue);
                    backActivity();
                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_china_mobile_back:
                if (checkIsAuthSuccess()) {
                    setResultValue(mstatue);
                    return;
                }
                boolean canGoBack = wvChinaMobile.canGoBack();
                if (canGoBack) {
                      goBack();
                } else {
                    //直接返回
                  setResultValue(mstatue);
                    backActivity();
                }
                break;
            case R.id.tv_china_mobile_exit:
                setResultValue(mstatue);
                backActivity();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (checkIsAuthSuccess()) {
                return true;
            }
            boolean canGoBack = wvChinaMobile.canGoBack();
            if (canGoBack) {
                goBack();
            } else {
                backActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //检查 当前页面是否是认证成功页面
    private boolean checkIsAuthSuccess() {
        if (loadHistoryUrls.size() > 0 && loadHistoryUrls.get(loadHistoryUrls.size() - 1) != null && loadHistoryUrls.get(loadHistoryUrls.size() - 1).contains("h5/sesame/se_order.html")) {
            setResultValue(mstatue);
            backActivity();
            return true;
        }
        return false;

    }

    //检查上一个页面是否为重定向页面并返回上一页页面
    public void goBack() {
        setResultValue(mstatue);
        if (loadHistoryUrls.size() > 1 && loadHistoryUrls.get(loadHistoryUrls.size() - 2).contains("index.html")) {
            if (loadHistoryUrls.size() == 2) {//如果第一个页面就是重定向 直接back

                backActivity();
            } else {
                //back 2 次
                wvChinaMobile.goBack();
                wvChinaMobile.goBack();
            }
            //因为是重定向 去掉后两个url
            loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
            loadHistoryUrls.remove(loadHistoryUrls.size() - 1);
        } else {
            wvChinaMobile.goBack();
        }
        LogUtil.d("abcd", "size = " + loadHistoryUrls.size());
    }

    public void setResultValue(String value){
        Intent intent = new Intent();
        intent.putExtra("RESULTSTATUE",value);
        ChinaMobileActivity.this.setResult(1,intent);

    }

}
