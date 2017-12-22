package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.AuthCreditBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;


public class GetCreditConf extends NetBase {

    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetCreditConf(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getCreditConfURL();
    }

    public void getData(JSONObject jsonObject, final BaseNetCallBack<AuthCreditBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
            @Override
            public void onSuccess(String result, String url) {
                successHandle(result, url, callBack);
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                failureHandle(result, errorType, errorCode, callBack);
            }
        });
    }

    private void successHandle(String result, String url, BaseNetCallBack<AuthCreditBean> callBack) {
        AuthCreditBean bean = GsonUtil.json2bean(result, AuthCreditBean.class);
        callBack.onSuccess(bean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<AuthCreditBean> callBack) {
        callBack.onFailure(mUrl, errorType, errorCode);
        MobclickAgent.reportError(mContext, result);
    }
}
