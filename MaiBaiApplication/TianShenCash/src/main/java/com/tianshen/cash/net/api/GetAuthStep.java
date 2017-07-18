package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.AuthStepBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 得到首页活动
 */

public class GetAuthStep extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetAuthStep(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getAuthStepUrl();
    }

    public void authStep(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<AuthStepBean> callBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<AuthStepBean> callBack) {
        AuthStepBean bean = GsonUtil.json2bean(result, AuthStepBean.class);
        callBack.onSuccess(bean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<AuthStepBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
