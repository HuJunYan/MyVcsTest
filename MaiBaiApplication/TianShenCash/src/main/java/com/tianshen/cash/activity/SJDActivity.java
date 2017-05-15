package com.tianshen.cash.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.net.api.IKnow;
import com.tianshen.cash.net.api.SJDLoanBack;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 手机贷H5页面
 */

public class SJDActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_web_back)
    TextView tv_web_back;
    @BindView(R.id.wv_web)
    WebView wv_web;
    private String mUrl;

    private boolean mIsSJDLoanBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getIntent().getExtras().getString(GlobalParams.WEB_URL_KEY);
        initWebView();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_sjd;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void setListensers() {
        tv_web_back.setOnClickListener(this);
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
        webSettings.setSupportZoom(true); //支持缩放
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void setWebView() {

        LogUtil.d("abc", "mUrl--->" + mUrl);

        wv_web.loadUrl(mUrl);
        wv_web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_web_back:
                delaySJDLoanBack();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            delaySJDLoanBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    * simple example using timer to do something after 5 second
    */
    private void delaySJDLoanBack() {

        if (mIsSJDLoanBack == true) {
            return;
        }

        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<? extends Long> getObservable() {
        return Observable.timer(5, TimeUnit.SECONDS);
    }

    private Observer<Long> getObserver() {
        return new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {
                mIsSJDLoanBack = true;
                String loadText = mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
                ViewUtil.createLoadingDialog((Activity) mContext, loadText, false);
            }

            @Override
            public void onNext(Long value) {
            }

            @Override
            public void onError(Throwable e) {
                ViewUtil.cancelLoadingDialog();
            }

            @Override
            public void onComplete() {
                sjdLoanBack();
            }
        };
    }


    /**
     * 通知服务器手机贷返回
     */
    private void sjdLoanBack() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final SJDLoanBack loanBack = new SJDLoanBack(mContext);
        loanBack.sjdLoanBack(jsonObject, new BaseNetCallBack<PostDataBean>() {
            @Override
            public void onSuccess(PostDataBean paramT) {
                ViewUtil.cancelLoadingDialog();
                gotoMainActivity();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                ViewUtil.cancelLoadingDialog();
                gotoMainActivity();
            }
        });
    }

    /**
     * 回到首页
     */
    private void gotoMainActivity() {
        EventBus.getDefault().post(new UserConfigChangedEvent());
        gotoActivity(mContext, MainActivity.class, null);
        finish();
    }


}
