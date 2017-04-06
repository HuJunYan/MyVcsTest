package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.StatisticsRollBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chunpengguo on 2017/1/14.
 */

public class StatisticsRoll extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public StatisticsRoll(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getStatisticsRollUrl();
    }

    public void getStatisticsRoll(JSONObject jsonObject,final BaseNetCallBack<StatisticsRollBean> mResponseCallBack) {
        try {

            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
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

    public void getStatisticsRoll( JSONObject jsonObject,View view, boolean isShowDialog, final BaseNetCallBack<StatisticsRollBean> mResponseCallBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<StatisticsRollBean> mSaleListCallBack) {
        try {
            StatisticsRollBean mResponseBean;
            if (isRelease) {
                mResponseBean = (StatisticsRollBean) GsonUtil.json2bean(result, StatisticsRollBean.class);
            } else {
                mResponseBean = test();
            }
            mSaleListCallBack.onSuccess(mResponseBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<StatisticsRollBean> mSaleListCallBack) {
        try {
            if (isRelease) {
                mSaleListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mSaleListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private StatisticsRollBean test() {
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
        String bank_card = "";
        try {
            bank_card = mJSONObject.getString("bank_card");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bank_card == null || "".equals(bank_card)) {
            throw new RuntimeException("bank_card is null");
        }
        String pay_pass = "";
        try {
            pay_pass = mJSONObject.getString("pay_pass");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pay_pass == null || "".equals(pay_pass)) {
            throw new RuntimeException("pay_pass is null");
        }

        StatisticsRollBean mResponseBean = new StatisticsRollBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("unbindBankCard success");

        return mResponseBean;
    }
}
