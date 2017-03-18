package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.VerifyCodeBean;
import com.maibai.user.model.VerifyCodeDataBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sbyh on 16/7/4.
 */

public class GetVerifyCode extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetVerifyCode(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getVerifyCodeUrl();
    }

    public void getVerifyCode(JSONObject jsonObject, final BaseNetCallBack<VerifyCodeBean> mVerifyCodeCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mVerifyCodeCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mVerifyCodeCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getVerifyCode(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<VerifyCodeBean> mVerifyCodeCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mVerifyCodeCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mVerifyCodeCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<VerifyCodeBean> mVerifyCodeCallBack) {
        try {
            if (isRelease) {
                VerifyCodeBean mVerifyCodeBean = (VerifyCodeBean) GsonUtil.json2bean(result, VerifyCodeBean.class);
                mVerifyCodeCallBack.onSuccess(mVerifyCodeBean);
            } else {
                mVerifyCodeCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<VerifyCodeBean> mVerifyCodeCallBack) {
        try {
            if (isRelease) {
                mVerifyCodeCallBack.onFailure(result, errorType, errorCode);
            } else {
                mVerifyCodeCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private VerifyCodeBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String mobile = "";
        try {
            mobile = mJSONObject.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mobile == null || "".equals(mobile)) {
            throw new RuntimeException("mobile is null");
        }
        String type = "";
        try {
            mobile = mJSONObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mobile == null || "".equals(mobile)) {
            throw new RuntimeException("mobile is null");
        }

        VerifyCodeBean mVerifyCodeBean = new VerifyCodeBean();
        mVerifyCodeBean.setCode(0);
        mVerifyCodeBean.setMsg("getVerifyCode in success");
        VerifyCodeDataBean mVerifyCodeDataBean = new VerifyCodeDataBean();
        int code = ((int) (Math.random() * 50000));
        mVerifyCodeDataBean.setVerify_code(120000 + code);
        long phoneNum = 13212345678L + ((long) (Math.random() * 50000));
        mVerifyCodeDataBean.setMobile(phoneNum + "");
        mVerifyCodeBean.setData(mVerifyCodeDataBean);

        return mVerifyCodeBean;
    }
}
