package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.IdNumInfoBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 获取用户身份认证信息
 */

public class GetIdNumInfo extends NetBase {

    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetIdNumInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUserIdNumInfoUrl();
    }

    public void getIdNumInfo(JSONObject jsonObject, boolean isShowDialog, final BaseNetCallBack<IdNumInfoBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, isShowDialog, new CallBack() {
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

    private void successHandle(String result, String url, BaseNetCallBack<IdNumInfoBean> callBack) {
        IdNumInfoBean bean = GsonUtil.json2bean(result, IdNumInfoBean.class);
        callBack.onSuccess(bean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<IdNumInfoBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
