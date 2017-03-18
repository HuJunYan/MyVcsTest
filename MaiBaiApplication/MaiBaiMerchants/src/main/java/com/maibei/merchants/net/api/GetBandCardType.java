package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.BandCardTypeBean;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.maibei.merchants.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/2.
 */
public class GetBandCardType extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetBandCardType(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getBandCardTypeUrl();
    }

    public void getBandCardType(JSONObject jsonObject, final BaseNetCallBack<BandCardTypeBean> mBandCardTypeCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBandCardTypeCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBandCardTypeCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getBandCardType(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<BandCardTypeBean> mBandCardTypeCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBandCardTypeCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBandCardTypeCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<BandCardTypeBean> mBandCardTypeCallBack) {
        BandCardTypeBean mBandCardTypeBean;
        if (isRelease) {
            mBandCardTypeBean = (BandCardTypeBean) GsonUtil.json2bean(result, BandCardTypeBean.class);
        } else {
            mBandCardTypeBean = test();
        }
        mBandCardTypeCallBack.onSuccess(mBandCardTypeBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<BandCardTypeBean> mBandCardTypeCallBack) {
        if (isRelease) {
            mBandCardTypeCallBack.onFailure(result, errorType, errorCode);
        } else {
            mBandCardTypeCallBack.onSuccess(test());
        }
    }

    private BandCardTypeBean test() {
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
            throw new RuntimeException("merchant_name is null");
        }
        String bank_card = "";
        try {
            bank_card = mJSONObject.getString("bank_card");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bank_card == null || "".equals(bank_card)) {
            throw new RuntimeException("bank_card is null");
        }

        BandCardTypeBean mBandCardTypeBean = new BandCardTypeBean();
        mBandCardTypeBean.setCode(0);
        mBandCardTypeBean.setMsg("getBandCardType success");
        mBandCardTypeBean.getData().setBank_card_type("招商银行");

        return mBandCardTypeBean;
    }
}