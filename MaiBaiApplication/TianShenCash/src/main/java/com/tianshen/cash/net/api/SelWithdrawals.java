package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.SelWithdrawalsBean;
import com.tianshen.cash.model.WithdrawalsItemBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-10-17.
 */
public class SelWithdrawals extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SelWithdrawals(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSelWithdrawalsURL();
    }

    public void selWithdrawals(JSONObject jsonObject, final BaseNetCallBack<SelWithdrawalsBean> mSelWithdrawalsCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSelWithdrawalsCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSelWithdrawalsCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void selWithdrawals(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<SelWithdrawalsBean> mSelWithdrawalsCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSelWithdrawalsCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSelWithdrawalsCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<SelWithdrawalsBean> mSelWithdrawalsCallBack) {
        try {
            if (isRelease) {
                SelWithdrawalsBean mSelWithdrawalsBean = (SelWithdrawalsBean) GsonUtil.json2bean(result, SelWithdrawalsBean.class);
                mSelWithdrawalsCallBack.onSuccess(mSelWithdrawalsBean);
            } else {
                mSelWithdrawalsCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<SelWithdrawalsBean> mSelWithdrawalsCallBack) {
        try {
            if (isRelease) {
                mSelWithdrawalsCallBack.onFailure(result, errorType, errorCode);
            } else {
                mSelWithdrawalsCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private SelWithdrawalsBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString(GlobalParams.USER_CUSTOMER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        SelWithdrawalsBean mSelWithdrawalsBean = new SelWithdrawalsBean();
        mSelWithdrawalsBean.setCode(0);
        mSelWithdrawalsBean.setMsg("SelWithdrawals in success");
        int max_cash = 300000;
        mSelWithdrawalsBean.setMax_cash(max_cash+"");
        mSelWithdrawalsBean.setDef_cash("152000");
        mSelWithdrawalsBean.setUnit(((int) (Math.random() * 2)) == 0 ? "10000" : "100000");
        int[] times = {3,6,9};
        float[] interestRate = {1.35f, 1.30f,1.25f};

        for(int i =0; i < times.length; i++) {
            WithdrawalsItemBean withdrawalsItemBean = new WithdrawalsItemBean();
            withdrawalsItemBean.setRepay_times(times[i] + "");
            withdrawalsItemBean.setId(100+i+"");
            for (int j = 0; j < 30; j++) {
                CashSubItemBean cashSubItemBean = new CashSubItemBean();
                cashSubItemBean.setWithdrawal_amount((j*10000)+"");
                float repay_tatal = (j*10000/times[i])*interestRate[i];
                cashSubItemBean.setRepay_total(repay_tatal+"");
                withdrawalsItemBean.getCash_data().add(cashSubItemBean);
            }
            mSelWithdrawalsBean.getData().add(withdrawalsItemBean);
        }

        return mSelWithdrawalsBean;
    }
}
