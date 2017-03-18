package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.SignUpBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.maibai.user.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by m on 16-8-31.
 */
public class SignUp extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SignUp(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSignUpUrl();
    }

    public void signUp(JSONObject jsonObject, final BaseNetCallBack<SignUpBean> mSignUpCallBack) {
        try {
            encryptPassowrd(jsonObject);
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSignUpCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSignUpCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void signUp(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<SignUpBean> mSignUpCallBack) {
        try {
            encryptPassowrd(jsonObject);
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSignUpCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSignUpCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<SignUpBean> mSignUpCallBack) {
        try {
            SignUpBean mSignUpBean;
            if (isRelease) {
                mSignUpBean = (SignUpBean) GsonUtil.json2bean(result, SignUpBean.class);
            } else {
                mSignUpBean = test();
            }
            mSignUpCallBack.onSuccess(mSignUpBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<SignUpBean> mSignUpCallBack) {
        try {
            if (isRelease) {
                mSignUpCallBack.onFailure(result, errorType, errorCode);
            } else {
                SignUpBean mSignUpBean = test();
                mSignUpCallBack.onSuccess(mSignUpBean);
            }
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

    private SignUpBean test() {
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

        String verify_code = "";
        try {
            verify_code = mJSONObject.getString("verify_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (verify_code == null || "".equals(verify_code)) {
            throw new RuntimeException("verify_code is null");
        }

        SignUpBean mSignUpBean = new SignUpBean();
        mSignUpBean.setCode(0);
        mSignUpBean.setMsg("sign up success");

        int id = ((int) (Math.random() * 500));
        mSignUpBean.getData().setCustomer_id(id + "");
        return mSignUpBean;
    }
}
