package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.WithDrawBean;
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
 * Created by chenrongshang on 16/7/4.
 */
public class WithDraw extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public WithDraw(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawUrl();
    }

    public void withDraw(JSONObject jsonObject, final BaseNetCallBack<WithDrawBean> mWithDrawCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithDrawCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithDrawCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void withDraw(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithDrawBean> mWithDrawCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithDrawCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithDrawCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithDrawBean> mSaleListCallBack) {
        WithDrawBean mWithDrawBean;
        if (isRelease) {
            mWithDrawBean = (WithDrawBean) GsonUtil.json2bean(result, WithDrawBean.class);
        } else {
            mWithDrawBean = test();
        }
        mSaleListCallBack.onSuccess(mWithDrawBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithDrawBean> mSaleListCallBack) {
        if (isRelease) {
            mSaleListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mSaleListCallBack.onSuccess(test());
        }
    }

    private WithDrawBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String merchant_id = "";
        try {
            merchant_id = mJSONObject.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (merchant_id == null || "".equals(merchant_id)) {
            throw new RuntimeException("merchant_id is null");
        }
        String money = "";
        try {
            money = mJSONObject.getString("money");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (money == null || "".equals(money)) {
            throw new RuntimeException("money is null");
        }

        WithDrawBean mWithDrawBean = new WithDrawBean();
        mWithDrawBean.setCode(0);
        mWithDrawBean.setMsg("withDraw success");

        return mWithDrawBean;
    }
}