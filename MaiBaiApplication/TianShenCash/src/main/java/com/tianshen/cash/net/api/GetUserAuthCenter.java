package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 得到用户认证中心
 */
public class GetUserAuthCenter extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetUserAuthCenter(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUserAuthCenterUrl();
    }

    public void userAuthCenter(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<UserAuthCenterBean> userAuthCenterCallBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
            @Override
            public void onSuccess(String result, String url) {
                successHandle(result, url, userAuthCenterCallBack);
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                failureHandle(result, errorType, errorCode, userAuthCenterCallBack);
            }
        });
    }

    private void successHandle(String result, String url, BaseNetCallBack<UserAuthCenterBean> userAuthCenterCallBack) {
        UserAuthCenterBean userAuthCenterBean = GsonUtil.json2bean(result, UserAuthCenterBean.class);
        userAuthCenterCallBack.onSuccess(userAuthCenterBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<UserAuthCenterBean> userAuthCenterCallBack) {
        try {
            userAuthCenterCallBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
