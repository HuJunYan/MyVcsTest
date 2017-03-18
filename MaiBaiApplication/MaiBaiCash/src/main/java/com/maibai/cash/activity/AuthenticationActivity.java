package com.maibai.cash.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.ViewUtil;
import com.maibai.cash.view.TitleBar;

public class AuthenticationActivity extends BaseActivity implements TitleBar.TitleBarListener2 {

    private WebView wv_content;
    private Bundle bundle;
    private TitleBar tb_titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        initView();
    }

    public void initView() {
        switch (bundle.getString("id")) {
            case GlobalParams.AUTHENTICATION_CHINA_MOBILE:
                tb_titleBar.setTitle("运营商认证");
                break;
            case GlobalParams.AUTHENTICATION_TAOBAO:
                tb_titleBar.setTitle("淘宝认证");
                break;
            case GlobalParams.AUTHENTICATION_JINGDONG:
                tb_titleBar.setTitle("京东认证");
                break;
            case GlobalParams.AUTHENTICATION_ZHIFUBAO:
                tb_titleBar.setTitle("支付宝认证");
                break;
            case GlobalParams.AUTHENTICATION_ZHIMA:
                tb_titleBar.setTitle("芝麻分认证");
                break;
        }
        //启用支持javascript
        WebSettings settings = wv_content.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        wv_content.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void resultFunction(){
                backActivity();
               /* Toast.makeText(mContext, "JS 调用无参方法",
                        Toast.LENGTH_SHORT).show();*/
            }
            @JavascriptInterface
            public void resultFunction(String result) {
                ToastUtil.showToast(mContext,result);
            }
            @JavascriptInterface
            public void showDialog(String loadText){
                ViewUtil.createLoadingDialog(AuthenticationActivity.this, loadText, false);
            }
            @JavascriptInterface
            public void dismissDialog(){
                ViewUtil.cancelLoadingDialog();
            }
            @JavascriptInterface
            public void showDialog(){
                ViewUtil.createLoadingDialog(AuthenticationActivity.this,null,false);
            }
            @JavascriptInterface
            public void showDialog(String loadText,boolean isCanCancel){
                ViewUtil.createLoadingDialog(AuthenticationActivity.this,loadText,isCanCancel);
            }

        },"returnResult");
        //WebView加载web资源
        wv_content.loadUrl(bundle.getString("url"));
//        wv_content.loadUrl("http:192.168.1.110:8080/JavascriptTest/");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

           /* @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(wv_content.canGoBack()){
                    tb_titleBar.setVisibility(View.GONE);
                }else{
                    tb_titleBar.setVisibility(View.VISIBLE);
                }
            }*/
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void findViews() {
        wv_content = (WebView) findViewById(R.id.wv_content);
        tb_titleBar = (TitleBar) findViewById(R.id.tb_titleBar);
    }

    @Override
    protected void setListensers() {
        tb_titleBar.setListener(this);
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wv_content.canGoBack()) {
                wv_content.goBack();//返回上一页面
                return true;
            } else {
                backActivity();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onLeftClick(View view) {
//        backActivity();
        if (wv_content.canGoBack()) {
            wv_content.goBack();//返回上一页面
        } else {
            backActivity();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onRightClick(View view) {
        backActivity();
    }
}
