package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.WithdrawalsRefreshBean;
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
public class WithdrawalsRefresh extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public WithdrawalsRefresh(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsRefreshURL();
    }

    public void selWithdrawals(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsRefreshBean> mWithdrawalsRefreshCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsRefreshCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsRefreshCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void selWithdrawals(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsRefreshBean> mWithdrawalsRefreshCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsRefreshCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsRefreshCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsRefreshBean> mWithdrawalsRefreshCallBack) {
        try {
            if (isRelease) {
                WithdrawalsRefreshBean mWithdrawalsRefreshBean = (WithdrawalsRefreshBean) GsonUtil.json2bean(result, WithdrawalsRefreshBean.class);
                mWithdrawalsRefreshCallBack.onSuccess(mWithdrawalsRefreshBean);
            } else {
                mWithdrawalsRefreshCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsRefreshBean> mWithdrawalsRefreshCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsRefreshCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsRefreshCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsRefreshBean test() {
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

        WithdrawalsRefreshBean mWithdrawalsRefreshBean = new WithdrawalsRefreshBean();
        mWithdrawalsRefreshBean.setCode(0);
        mWithdrawalsRefreshBean.setMsg("WithdrawalsRefresh in success");
        String[] statusArray = {"1", "5", "6", "7"};
        String status = ((int) (Math.random() * 4))+"";
        switch (status) {
            case "0":
                mWithdrawalsRefreshBean.getData().setStatus("1");
                mWithdrawalsRefreshBean.getData().setAmount(((((int) (Math.random() * 4))+1)*100000) +"");
                break;
            case "1":
                mWithdrawalsRefreshBean.getData().setStatus("5");
                break;
            case "2":
                mWithdrawalsRefreshBean.getData().setStatus("6");
                mWithdrawalsRefreshBean.getData().setReason("人工审核未通过");
                break;
            case "3":
                mWithdrawalsRefreshBean.getData().setStatus("7");
                mWithdrawalsRefreshBean.getData().setReason("您暂时不符合我们的征信条件");
                break;
        }

        return mWithdrawalsRefreshBean;
    }
}
