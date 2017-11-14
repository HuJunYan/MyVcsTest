package com.tianshen.cash.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.RiskPreFinishEvent;
import com.tianshen.cash.event.WechatShareEvent;
import com.tianshen.cash.model.MessageBean;
import com.tianshen.cash.model.TurnplateBean;
import com.tianshen.cash.net.api.UpdateShareCountApi;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.QRCodeUtils;
import com.tianshen.cash.utils.TianShenShareUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.InviteBottomDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

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

    @BindView(R.id.tv_web_title)
    TextView tv_web_title;

    @BindView(R.id.iv_web_share)
    ImageView iv_web_share;
    private String mUrl;
    private String mFrom;
    private String mType;
    private InviteBottomDialog inviteBottomDialog;
    private WbShareHandler wbShareHandler;
    private int type;
    private MessageBean messageBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.WEB_URL_KEY);
        mFrom = getIntent().getExtras().getString(GlobalParams.WEB_FROM);
        mType = getIntent().getExtras().getString(GlobalParams.WEB_TYPE);
        initUISetting();
        initWebView();

    }

    private void initUISetting() {
        //只用从消息中心页面过来的才取MessageBean对象
        if (GlobalParams.FROM_MESSAGE.equals(mFrom)) {
            messageBean = (MessageBean) getIntent().getExtras().getSerializable(GlobalParams.WEB_MSG_DATA_KEY);
            if (messageBean != null)
                tv_web_title.setText(messageBean.getMsg_title());
            mUrl = messageBean.getMsg_url();
            iv_web_share.setVisibility(View.VISIBLE);
        } else if (GlobalParams.FROM_JOKE.equals(mFrom)) {
            tv_web_title.setText("笑话大全");
        }

        if (GlobalParams.TYPE_READ.equals(mType)) {
            tv_web_exit.setVisibility(View.VISIBLE);
        }
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
        iv_web_share.setOnClickListener(this);
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
        if (GlobalParams.FROM_HOME.equals(mFrom)) { //添加接口
            wv_web.addJavascriptInterface(new IJavaScriptInterface(), "turnplate");
        }
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
                shareToOther();
                break;
        }
    }

    private void shareToOther() {
        if (messageBean != null) {
            TurnplateBean turnplateBean = new TurnplateBean();
            turnplateBean.invite_url = messageBean.getMsg_share_url();
            turnplateBean.share_title = messageBean.getMsg_share_title();
            turnplateBean.share_description = messageBean.getMsg_description();
            initShareDialogData(turnplateBean, InviteBottomDialog.TYPE_WEB);
        } else {
            showDataErrorTip();
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
                            initShareDialogData(turnplateBean, InviteBottomDialog.TYPE_NORMAL_SHARE);
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

    private void initShareDialogData(final TurnplateBean turnplateBean, int type) {
        if (TextUtils.isEmpty(turnplateBean.invite_url)) {
            showDataErrorTip();
            return;
        }
        this.type = type;
        Bitmap qrCode = QRCodeUtils.createQRCode(turnplateBean.invite_url, (int) (getResources().getDisplayMetrics().density * 140));
        //分享dialog 创建
        inviteBottomDialog = new InviteBottomDialog(WebActivity.this, TianShenShareUtils.getIUiListenerInstance(), turnplateBean.share_title, turnplateBean.share_description, type)
                .setQRCodeBitmap(qrCode).setShareIconResAndName(R.drawable.inviteicon, "share_icon").setShareUrl(turnplateBean.invite_url).setWeiBoListener(new InviteBottomDialog.ShareWeiboListener() {
                    @Override
                    public void shareToWeibo() {
                        shareWeibo(turnplateBean);
                    }
                });
        if (!isFinishing()) {
            inviteBottomDialog.show();
        }
    }

    private void shareWeibo(TurnplateBean turnplateBean) {
        WbSdk.install(this, new AuthInfo(this.getApplicationContext(), GlobalParams.APP_WEIBO_KEY, GlobalParams.WEIBO_OAUTH_ADDRESS, GlobalParams.WEIBO_SCOPE));
        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();
        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        weiboMultiMessage.textObject = TianShenShareUtils.getTextObj(turnplateBean.share_title + turnplateBean.invite_url);
        weiboMultiMessage.imageObject = TianShenShareUtils.getImageObj(this, R.drawable.inviteicon);
        wbShareHandler.shareMessage(weiboMultiMessage, false);
    }


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
            if (type == InviteBottomDialog.TYPE_WEB) {
                updateShareSuccess();
            }
        }

        @Override
        public void onWbShareCancel() {
        }

        @Override
        public void onWbShareFail() {
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.handleResultData(data, TianShenShareUtils.getIUiListenerInstance());
        }

    }

    @Subscribe
    public void onWeChatShareEvent(WechatShareEvent event) {
        if (inviteBottomDialog != null) {
            inviteBottomDialog.cancel();
        }
        if (type == InviteBottomDialog.TYPE_WEB) {
            updateShareSuccess();
        }
    }

    private void showDataErrorTip() {
        ToastUtil.showToast(this, "数据错误");
    }


    public void updateShareSuccess() {
        if (messageBean == null) {
            return;
        }
        UpdateShareCountApi updateShareCountApi = new UpdateShareCountApi(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(this));
            jsonObject.put("msg_id", messageBean.getMsg_id());
            jsonObject.put("msg_type", messageBean.getMsg_type());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateShareCountApi.updateShareCount(jsonObject, iv_web_share, false);
    }

    @Subscribe
    public void onRiskPreFinishEvent(RiskPreFinishEvent event) {
        //风控评分结束
        ToastUtil.showToast(mContext, "您的额度评测已经结束,请返回查看");
    }
}
