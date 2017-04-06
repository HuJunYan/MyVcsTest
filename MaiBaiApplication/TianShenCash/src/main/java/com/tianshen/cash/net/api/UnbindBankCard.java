package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ResponseBean;
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
 * Created by Administrator on 2016/8/19.
 */
public class UnbindBankCard extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public UnbindBankCard(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetUnbindBankCardUrl();
    }

    public void unbindBankCard(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
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

    public void unbindBankCard(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
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

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mSaleListCallBack) {
        try {
            ResponseBean mResponseBean;
            if (isRelease) {
                mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
            } else {
                mResponseBean = test();
            }
            mSaleListCallBack.onSuccess(mResponseBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mSaleListCallBack) {
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

    private ResponseBean test() {
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

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("unbindBankCard success");

        return mResponseBean;
    }
}
