package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Logout extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public Logout(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getLogoutUrl();
    }

    public void logout(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mResponseCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mResponseCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void logout(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mResponseCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mResponseCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (isRelease) {
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                mResponseCallBack.onSuccess(mResponseBean);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (isRelease) {
                mResponseCallBack.onFailure(result, errorType, errorCode);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ResponseBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString(GlobalParams.USER_CUSTOMER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("logout in success");

        return mResponseBean;
    }
}
