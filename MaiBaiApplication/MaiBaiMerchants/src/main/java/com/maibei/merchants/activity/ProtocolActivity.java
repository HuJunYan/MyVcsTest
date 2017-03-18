package com.maibei.merchants.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.NetConstantValue;


public class ProtocolActivity extends BaseActivity {

    WebView wv_content;
    String protocolURL="http://www.baidu.com";//http://serverName/Home/System/installmentProtocol

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wv_content.loadUrl(protocolURL);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(NetConstantValue.merchantSignUp());
                return true;
            }
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void findViews() {
        wv_content=(WebView)findViewById(R.id.wv_content);

    }

    @Override
    protected void setListensers() {

    }


}
