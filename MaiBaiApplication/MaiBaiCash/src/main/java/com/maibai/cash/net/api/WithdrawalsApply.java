package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.WithdrawalsApplyBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-10-18.
 */
public class WithdrawalsApply extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public WithdrawalsApply(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsApplyURL();
    }

    public void withdrawalsApply(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsApplyBean> mWithdrawalsApplyCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsApplyCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsApplyCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void withdrawalsApply(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsApplyBean> mWithdrawalsApplyCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsApplyCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsApplyCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsApplyBean> mWithdrawalsApplyCallBack) {
        try {
            if (isRelease) {
                WithdrawalsApplyBean mWithdrawalsApplyBean = (WithdrawalsApplyBean) GsonUtil.json2bean(result, WithdrawalsApplyBean.class);
                mWithdrawalsApplyCallBack.onSuccess(mWithdrawalsApplyBean);
            } else {
                mWithdrawalsApplyCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsApplyBean> mWithdrawalsApplyCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsApplyCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsApplyCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsApplyBean test() {
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

        WithdrawalsApplyBean mWithdrawalsApplyBean = new WithdrawalsApplyBean();
        mWithdrawalsApplyBean.setCode(0);
        mWithdrawalsApplyBean.setMsg("WithdrawalsApply in success");
        String[] statusArray = {"1", "5", "6", "7"};
        int is_need_verify = ((int) (Math.random() * 2));
        if (is_need_verify == 0) {
            int amount = ((int) (Math.random() * 20) + 1)*100000;
            mWithdrawalsApplyBean.getData().setAmount(amount+"");
        } else {
            mWithdrawalsApplyBean.getData().setConsume_id("1000");
        }
        mWithdrawalsApplyBean.getData().setIs_need_verify(is_need_verify+"");

        return mWithdrawalsApplyBean;
    }
}
