package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.google.gson.JsonSyntaxException;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.XiangShangSubmitInfoBean;
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

public class GetSubmitXiangShangBindApi extends NetBase {

    private Context mContext;
    private final String mUrl;
    private JSONObject mJSONObject;

    public GetSubmitXiangShangBindApi(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSubmitXiangShangBindInfoURL();
    }

    public void getSubmitXiangShangBindInfo(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<XiangShangSubmitInfoBean> callBack) {
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

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<XiangShangSubmitInfoBean> callBack) {
        callBack.onFailure(result, errorType, errorCode);
    }

    private void successHandle(String result, String url, BaseNetCallBack<XiangShangSubmitInfoBean> callBack) {
        try {
            XiangShangSubmitInfoBean data = GsonUtil.json2bean(result, XiangShangSubmitInfoBean.class);
            callBack.onSuccess(data);
        } catch (JsonSyntaxException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

}
