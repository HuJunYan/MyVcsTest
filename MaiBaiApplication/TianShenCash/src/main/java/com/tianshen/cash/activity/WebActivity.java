package com.tianshen.cash.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.WechatShareEvent;
import com.tianshen.cash.model.TurnplateBean;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.QRCodeUtils;
import com.tianshen.cash.utils.TianShenShareUtils;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.InviteBottomDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

import static android.R.id.message;

/**
 * 展示H5
 */

public class WebActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_web_back)
    TextView tv_web_back;

    @BindView(R.id.tv_web_exit)
    TextView tv_web_exit;

    @BindView(R.id.wv_web)
    WebView wv_web;

    private String mUrl;
    private String mFrom;
    private InviteBottomDialog inviteBottomDialog;
    private WbShareHandler wbShareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.WEB_URL_KEY);
        mFrom = getIntent().getExtras().getString(GlobalParams.WEB_FROM);
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
        tv_web_exit.setOnClickListener(this);
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
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {

        LogUtil.d("abc", "mUrl--->" + mUrl);
        if (GlobalParams.FROM_HOME.equals(mFrom)) {
            wv_web.addJavascriptInterface(new IJavaScriptInterface(), "turnplate");
        }
        wv_web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                LogUtil.d("murl = ", "error = " + title);
            }
        });
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LogUtil.d("murl2 =", "error" + error.toString());
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    view.loadUrl(url);
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

    public class IJavaScriptInterface {
        @JavascriptInterface
        public void showDialog(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(message)) {
                        showDataErrorTip();
                        return;
                    }
                    try {
                        final TurnplateBean turnplateBean = GsonUtil.json2bean(message, TurnplateBean.class);
                        if (turnplateBean != null) {
                            initShareDialogData(turnplateBean);
                        } else {
                            showDataErrorTip();
                        }

                    } catch (JsonSyntaxException e) {
                        showDataErrorTip();
                        MobclickAgent.reportError(WebActivity.this, e.getMessage());
                    }
                }


            });
        }
    }

    private void initShareDialogData(final TurnplateBean turnplateBean) {
        if (TextUtils.isEmpty(turnplateBean.invite_url)) {
            showDataErrorTip();
            return;
        }
        Bitmap qrCode = QRCodeUtils.createQRCode(turnplateBean.invite_url, (int) (getResources().getDisplayMetrics().density * 140));
        //分享dialog 创建
        inviteBottomDialog = new InviteBottomDialog(WebActivity.this, listener, turnplateBean.share_title, turnplateBean.share_description)
                .setQRCodeBitmap(qrCode).setShareIconResAndName(R.drawable.inviteicon, "share_icon").setShareUrl(turnplateBean.invite_url).setWeiBoListener(new InviteBottomDialog.ShareWeiboListener() {
                    @Override
                    public void shareToWeibo() {
                        shareWeibo(turnplateBean);
                    }
                });
        inviteBottomDialog.show();
    }

    private void shareWeibo(TurnplateBean turnplateBean) {
        WbSdk.install(this, new AuthInfo(this.getApplicationContext(), GlobalParams.APP_WEIBO_KEY, GlobalParams.WEIBO_OAUTH_ADDRESS, GlobalParams.WEIBO_SCOPE));
        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = TianShenShareUtils.getTextObj(turnplateBean.share_title + turnplateBean.invite_url);
        weiboMultiMessage.imageObject = TianShenShareUtils.getImageObj(this, R.drawable.inviteicon);
//        weiboMultiMessage.mediaObject = TianShenShareUtils.getWebpageObj(this, mShareUrl, getResources().getString(R.string.invite_share_text_title), getResources().getString(R.string.invite_share_text_description));
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }


    IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            ToastUtil.showToast(mContext, "分享成功");
            if (inviteBottomDialog != null) {
                inviteBottomDialog.cancel();
            }
        }

        @Override
        public void onError(UiError uiError) {
//            ToastUtil.showToast(mContext, "分享失败");
        }

        @Override
        public void onCancel() {
//            ToastUtil.showToast(mContext, "分享取消");
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (wbShareHandler != null) {
            wbShareHandler.doResultIntent(intent, wbShareCallback);
        }
    }

    private WbShareCallback wbShareCallback = new WbShareCallback() {

        @Override
        public void onWbShareSuccess() {
            ToastUtil.showToast(getApplicationContext(), "分享成功");
            if (inviteBottomDialog != null) {
                inviteBottomDialog.cancel();
            }
        }

        @Override
        public void onWbShareCancel() {
//            ToastUtil.showToast(getApplicationContext(), "分享取消");
        }

        @Override
        public void onWbShareFail() {
//            ToastUtil.showToast(getApplicationContext(), "分享失败");
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.handleResultData(data, listener);
        }

    }

    @Subscribe
    public void onWeChatShareEvent(WechatShareEvent event) {
        if (inviteBottomDialog != null) {
            inviteBottomDialog.cancel();
        }
    }

    private void showDataErrorTip() {
        ToastUtil.showToast(this, "数据错误");
    }

}
