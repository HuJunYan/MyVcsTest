package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ActivityBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 风控前置借款页面获取验证码
 */

public class GetLoanVerifyCode extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetLoanVerifyCode(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getLoanVerifyCodeURL();
    }

    public void send(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<PostDataBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
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
        PostDataBean bean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(bean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
