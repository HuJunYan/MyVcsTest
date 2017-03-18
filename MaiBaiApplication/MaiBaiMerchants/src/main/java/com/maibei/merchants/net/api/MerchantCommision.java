package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.MerchantCommisionBean;
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
 * Created by m on 16-11-21.
 */
public class MerchantCommision extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public MerchantCommision(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.merchantCommisionUrl();
    }

    public void merchantCommision(JSONObject jsonObject, final BaseNetCallBack<MerchantCommisionBean> mMerchantCommisionCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMerchantCommisionCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMerchantCommisionCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void merchantCommision(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<MerchantCommisionBean> mMerchantCommisionCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMerchantCommisionCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMerchantCommisionCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<MerchantCommisionBean> mSaleListCallBack) {
        MerchantCommisionBean mMerchantCommisionBean;
        if (isRelease) {
            mMerchantCommisionBean = (MerchantCommisionBean) GsonUtil.json2bean(result, MerchantCommisionBean.class);
        } else {
            mMerchantCommisionBean = test();
        }
        mSaleListCallBack.onSuccess(mMerchantCommisionBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<MerchantCommisionBean> mSaleListCallBack) {
        if (isRelease) {
            mSaleListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mSaleListCallBack.onSuccess(test());
        }
    }

    private MerchantCommisionBean test() {
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

        MerchantCommisionBean mMerchantCommisionBean = new MerchantCommisionBean();
        mMerchantCommisionBean.setCode(0);
        mMerchantCommisionBean.setMsg("merchantCommision success");
        int balancemoney = ((int)(Math.random() * 100) + 1) * 1000;
        mMerchantCommisionBean.getData().setCommision_balance(balancemoney+"");

        return mMerchantCommisionBean;
    }
}
