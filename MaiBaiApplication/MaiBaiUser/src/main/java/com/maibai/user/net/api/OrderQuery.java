package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.OrderRealPayBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-9-18.
 */
public class OrderQuery extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public OrderQuery(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getOrderRealPayURL();
    }

    public void orderQuery(JSONObject jsonObject, final BaseNetCallBack<OrderRealPayBean> mOrderRealPayCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderRealPayCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderRealPayCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void orderQuery(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<OrderRealPayBean> mOrderRealPayCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mOrderRealPayCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mOrderRealPayCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
    private void successHandle(String result, String url, BaseNetCallBack<OrderRealPayBean> mOrderRealPayCallBack) {
        try {
            if (isRelease) {
                OrderRealPayBean mOrderRealPayBean = (OrderRealPayBean) GsonUtil.json2bean(result, OrderRealPayBean.class);
                mOrderRealPayCallBack.onSuccess(mOrderRealPayBean);
            } else {
                mOrderRealPayCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<OrderRealPayBean> mOrderRealPayCallBack) {
        try {
            if (isRelease) {
                mOrderRealPayCallBack.onFailure(result, errorType, errorCode);
            } else {
                mOrderRealPayCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private OrderRealPayBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String consume_id = "";
        try {
            consume_id = mJSONObject.getString("consume_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (consume_id == null || "".equals(consume_id)) {
            throw new RuntimeException("consume_id is null");
        }

        String down_payment = "";
        try {
            down_payment = mJSONObject.getString("down_payment");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (down_payment == null || "".equals(down_payment)) {
            throw new RuntimeException("down_payment is null");
        }

        String repay_type = "";
        try {
            repay_type = mJSONObject.getString("repay_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (repay_type == null || "".equals(repay_type)) {
            throw new RuntimeException("repay_type is null");
        }

        OrderRealPayBean mOrderRealPayBean = new OrderRealPayBean();
        mOrderRealPayBean.setCode(0);
        mOrderRealPayBean.setMsg("OrderQuery in success");

        return mOrderRealPayBean;
    }
}
