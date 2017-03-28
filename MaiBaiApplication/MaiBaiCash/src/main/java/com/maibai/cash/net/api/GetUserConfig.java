package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.model.TianShenLoginBean;
import com.maibai.cash.model.UserConfig;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 得到用户配置信息
 */
public class GetUserConfig extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetUserConfig(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUserConfigUrl();
    }

    public void userConfig(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<UserConfig> userConfigCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, userConfigCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, userConfigCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<UserConfig> mUserConfigBack) {
        try {
            UserConfig mUserConfig = GsonUtil.json2bean(result, UserConfig.class);
            mUserConfigBack.onSuccess(mUserConfig);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<UserConfig> mUserConfigCallBack) {
        try {
            mUserConfigCallBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
