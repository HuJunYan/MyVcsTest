package com.tianshen.cash.net.api;


import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class BaseLoanInfoApply extends NetBase {

    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public BaseLoanInfoApply(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getBaseLoanInfoApply();
    }

    public void baseLoanInfoApply(JSONObject jsonObject, View view, final BaseNetCallBack<PostDataBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, view, true, new CallBack() {
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

    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        PostDataBean mPostDataBean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(mPostDataBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
        callBack.onFailure(mUrl, errorType, errorCode);
        MobclickAgent.reportError(mContext, result);
    }

}
