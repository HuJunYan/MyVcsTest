package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.google.gson.JsonSyntaxException;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.XiangShangVerifyCodeBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by wang on 2017/10/13.
 */

public class GetBindXiangShangVerifyCodeApi extends NetBase {

    private Context mContext;
    private final String mUrl;
    private JSONObject mJSONObject;

    public GetBindXiangShangVerifyCodeApi(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getBindXiangShangVerifyCodeURL();
    }

    public void getBindXiangShangVerifyCode(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<XiangShangVerifyCodeBean> callBack) {
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

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<XiangShangVerifyCodeBean> callBack) {
        callBack.onFailure(result, errorType, errorCode);
    }

    private void successHandle(String result, String url, BaseNetCallBack<XiangShangVerifyCodeBean> callBack) {
        try {
            XiangShangVerifyCodeBean data = GsonUtil.json2bean(result, XiangShangVerifyCodeBean.class);
            callBack.onSuccess(data);
        } catch (JsonSyntaxException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

}
