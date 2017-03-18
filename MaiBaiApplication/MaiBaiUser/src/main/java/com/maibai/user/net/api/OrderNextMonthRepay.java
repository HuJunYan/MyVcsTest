package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.OrderBean;
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

/**
 * Created by sbyh on 16/7/4.
 */

public class OrderNextMonthRepay extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public OrderNextMonthRepay(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getOrderNextMonthRepayUrl();
    }

    public void orderNextMonthRepay(JSONObject jsonObject, final BaseNetCallBack<OrderBean> mOrderCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void orderNextMonthRepay(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<OrderBean> mOrderCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<OrderBean> mOrderCallBack) {
        try {
            if (isRelease) {
                OrderBean mOrderBean = (OrderBean) GsonUtil.json2bean(result, OrderBean.class);
                mOrderCallBack.onSuccess(mOrderBean);
            } else {
                mOrderCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<OrderBean> mOrderCallBack) {
        try {
            if (isRelease) {
                mOrderCallBack.onFailure(result, errorType, errorCode);
            } else {
                mOrderCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private OrderBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
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

        String amount = "";
        try {
            amount = mJSONObject.getString("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (amount == null || "".equals(amount)) {
            throw new RuntimeException("amount is null");
        }

        String pay_pass = "";
        try {
            pay_pass = mJSONObject.getString("pay_pass");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pay_pass == null || "".equals(pay_pass)) {
            throw new RuntimeException("OrderNextMonthRepay is null");
        }

        OrderBean mOrderBean = new OrderBean();
        mOrderBean.setCode(0);
        mOrderBean.setMsg("OrderNextMonthRepay in success");
        mOrderBean.getData().setAmount("35000");
        mOrderBean.getData().setRepay_type("1");
        mOrderBean.getData().setRepay_date("1900-1-1");
        mOrderBean.getData().setRepay_total("6933");
        mOrderBean.getData().setDiscount("100");

        return mOrderBean;
    }
}
