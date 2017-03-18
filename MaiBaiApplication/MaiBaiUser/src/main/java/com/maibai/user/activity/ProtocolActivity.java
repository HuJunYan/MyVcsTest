package com.maibai.user.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.view.TitleBar;

public class ProtocolActivity extends BaseActivity {

    private int pro_type = 0;
    private WebView wv_content;
    private String protocolURL = "";
    private TitleBar title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pro_type = getIntent().getExtras().getInt("pro_type");
        init();
    }

    public void init() {
        if (pro_type == GlobalParams.PRO_REGISTE) {
            protocolURL = NetConstantValue.getUserLoginPro();//用户注册协议的地址
            title_bar.setTitle("买呗用户服务协议");
        } else if (pro_type == GlobalParams.PRO_SERVER) {
            protocolURL = NetConstantValue.getServerURL();//用户服务协议的地址
            title_bar.setTitle("买呗分期付款服务协议");
        }
        wv_content.loadUrl(protocolURL);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
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
        title_bar = (TitleBar) findViewById(R.id.title_bar);
        wv_content = (WebView) findViewById(R.id.wv_content);
    }

    @Override
    protected void setListensers() {

    }
}
