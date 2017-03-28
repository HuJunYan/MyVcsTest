package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.SignInBean;
import com.maibai.cash.model.TianShenLoginBean;
import com.maibai.cash.model.User;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.TianShenUserUtil;
import com.maibai.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sbyh on 16/6/28.
 */

public class SignIn extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SignIn(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSignInUrl();
    }

    public void signIn(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<TianShenLoginBean> mSignInCallBack) {
        try {
            encryptPassowrd(jsonObject);
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSignInCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSignInCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<TianShenLoginBean> mSignInCallBack) {
        try {
            TianShenLoginBean mSignInBean = GsonUtil.json2bean(result, TianShenLoginBean.class);
            mSignInCallBack.onSuccess(mSignInBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<TianShenLoginBean> mSignInCallBack) {
        try {
            mSignInCallBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void encryptPassowrd(JSONObject jsonObject) throws JSONException {
        List<String> keyList = SignUtils.copyIterator(jsonObject.keys());
        if (keyList.contains("password")) {
            jsonObject.put("password", Utils.MD5SHA1AndReverse(jsonObject.getString("password")));
        }
    }

    private SignInBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String device_id = "";
        try {
            device_id = mJSONObject.getString("device_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_id == null || "".equals(device_id)) {
            throw new RuntimeException("device_id is null");
        }

        SignInBean mSignInBean = new SignInBean();
        mSignInBean.setCode(0);
        mSignInBean.setMsg("sign in success");

        int id = ((int) (Math.random() * 500));
//        mSignInBean.getData().setId(id + "");
//        mSignInBean.getData().setDevice_id(1000 + id + "");
//        int payPass = ((int) (Math.random() * 2));
//        if (payPass == 0) {
//            mSignInBean.getData().setReal_name("");
//            mSignInBean.getData().setMobile("");
//            mSignInBean.getData().setId_num("");
//            mSignInBean.getData().setIsset_pay_pass("0");
//            mSignInBean.getData().setAmount("0");
//            mSignInBean.getData().setBalance_amount("0");
//            mSignInBean.getData().setMax_amount("0");
//            mSignInBean.getData().setCreate_time("");
//            mSignInBean.getData().setUpdate_time("");
//            mSignInBean.getData().setSignin_type("0");
//        } else {
//            mSignInBean.getData().setReal_name("名字" + id);
//            mSignInBean.getData().setMobile("13512345" + id);
//            mSignInBean.getData().setId_num("10000" + id);
//            mSignInBean.getData().setIsset_pay_pass("1");
//            mSignInBean.getData().setAmount("1000000");
//            mSignInBean.getData().setBalance_amount("300000");
//            mSignInBean.getData().setMax_amount("100000000");
//            long createTime = System.currentTimeMillis() / 1000 - 24 * 60 * 60L;
//            mSignInBean.getData().setCreate_time(createTime + "");
//            long updateTime = System.currentTimeMillis() / 1000;
//            mSignInBean.getData().setUpdate_time(updateTime + "");
//            mSignInBean.getData().setSignin_type("1");
//        }

        return mSignInBean;
    }
}
