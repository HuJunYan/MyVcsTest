package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class GetVerifySmsForRepayment extends NetBase {

    private String mUrl;
    private JSONObject mJSONObject;
    private Context context;

    public GetVerifySmsForRepayment(Context context) {
        super(context);
        mUrl = NetConstantValue.getVerifySmsForRepaymentUrl();
    }

    public void getVerify(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<PostDataBean> callBack) {
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
        PostDataBean postDataBean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(postDataBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
        callBack.onFailure(result, errorType, errorCode);
        MobclickAgent.reportError(context, result);
    }


}
