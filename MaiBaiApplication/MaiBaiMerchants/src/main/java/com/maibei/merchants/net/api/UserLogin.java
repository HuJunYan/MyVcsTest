package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.UserLoginBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.maibei.merchants.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sbyh on 16/6/27.
 */

public class UserLogin extends NetBase{
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public UserLogin(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUserLoginUrl();
    }

    public void userLogin(JSONObject jsonObject, final BaseNetCallBack<UserLoginBean> mUserLoginCallBack) {
        try {
            jsonObject.put("password", Utils.MD5SHA1AndReverse(jsonObject.getString("password")));
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mUserLoginCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mUserLoginCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void userLogin(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<UserLoginBean> mUserLoginCallBack) {
        try {
            jsonObject.put("password", Utils.MD5SHA1AndReverse(jsonObject.getString("password")));
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mUserLoginCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mUserLoginCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<UserLoginBean> mUserLoginCallBack){
        UserLoginBean mUserLoginBean;
        if (isRelease) {
            mUserLoginBean = (UserLoginBean) GsonUtil.json2bean(result, UserLoginBean.class);
        } else {
            mUserLoginBean = test();
        }
        UserUtil.setUser(mContext, mUserLoginBean);
        mUserLoginCallBack.onSuccess(mUserLoginBean);

    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<UserLoginBean> mUserLoginCallBack){
        if (isRelease) {
            mUserLoginCallBack.onFailure(result, errorType, errorCode);
        } else {
            mUserLoginCallBack.onSuccess(test());
        }
    }

    private UserLoginBean test() {
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

        String password = "";
        try {
            password = mJSONObject.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (password == null || "".equals(password)) {
            throw new RuntimeException("password is null");
        }

        UserLoginBean mUserLoginBean = new UserLoginBean();
        mUserLoginBean.setCode(0);
        mUserLoginBean.setMsg("login success");
        int id = ((int) (Math.random() * 100)) + 100;
        mUserLoginBean.data.setId(id + "");
        mUserLoginBean.data.setMobile(mobile);
        int balance_head = ((int) (Math.random() * 100)) + 2000;
        int balance_tail = ((int) (Math.random() * 100));
        mUserLoginBean.data.setBalance(balance_head+"."+balance_tail);

        return mUserLoginBean;
    }
}
