package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
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

    public void getVerifyCode(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    public void getVerifyCode(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mResponseCallBack){
        if (isRelease) {
            ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
            mResponseCallBack.onSuccess(mResponseBean);
        } else {
            mResponseCallBack.onSuccess(test());
        }
    }
    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mResponseCallBack){
        if (isRelease) {
            mResponseCallBack.onFailure(result, errorType, errorCode);
        } else {
            mResponseCallBack.onSuccess(test());
        }
    }

    private ResponseBean test() {
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

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("getVerifyCode in success");

        return mResponseBean;
    }
}
