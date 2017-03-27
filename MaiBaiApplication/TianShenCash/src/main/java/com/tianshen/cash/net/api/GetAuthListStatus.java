package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.net.base.AuthenticationStatus;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetAuthListStatus extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetAuthListStatus(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getAuthListURL();
    }

    public void getAuthList(JSONObject jsonObject, final BaseNetCallBack<AuthenticationStatus> authenticationStatusCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, authenticationStatusCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, authenticationStatusCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getAuthList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<AuthenticationStatus> authenticationStatusCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, authenticationStatusCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, authenticationStatusCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<AuthenticationStatus> authenticationStatusCallBack) {
        try {
            if (isRelease) {
                AuthenticationStatus authenticationBean = (AuthenticationStatus) GsonUtil.json2bean(result, AuthenticationStatus.class);
                authenticationStatusCallBack.onSuccess(authenticationBean);
            } else {
//            authenticationStatusCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<AuthenticationStatus> authenticationStatusCallBack) {
        try {
            if (isRelease) {
                authenticationStatusCallBack.onFailure(result, errorType, errorCode);
            } else {
//            authenticationStatusCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
