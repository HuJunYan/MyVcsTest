package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.IknowBean;
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
 * Created by m on 16-10-10.
 */
public class UnBindMyBankCard extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public UnBindMyBankCard(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUnBindBankCardUrl();
    }

    public void unBind(JSONObject jsonObject, final BaseNetCallBack<PostDataBean> callBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, callBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, callBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        try {
            PostDataBean postDataBean = GsonUtil.json2bean(result, PostDataBean.class);
            callBack.onSuccess(postDataBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> mCustomerAuthCallBack) {
        try {
            if (isRelease) {
                mCustomerAuthCallBack.onFailure(result, errorType, errorCode);
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
