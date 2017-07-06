package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.WithdrawalsBillItemBean;
import com.tianshen.cash.model.WithdrawalsBillListBean;
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
 * Created by m on 16-10-14.
 */
public class GetWithdrawalsBill extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetWithdrawalsBill(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getGetWithdrawalsBillURL();
    }

    public void getWithdrawalsBill(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsBillListBean> mWithdrawalsBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getWithdrawalsBill(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsBillListBean> mWithdrawalsBillListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsBillListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsBillListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsBillListBean> mWithdrawalsBillListCallBack) {
        try {
            if (isRelease) {
                WithdrawalsBillListBean mWithdrawalsBillListBean = (WithdrawalsBillListBean) GsonUtil.json2bean(result, WithdrawalsBillListBean.class);
                mWithdrawalsBillListCallBack.onSuccess(mWithdrawalsBillListBean);
            } else {
                mWithdrawalsBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsBillListBean> mWithdrawalsBillListCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsBillListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsBillListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsBillListBean test() {
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

        String offset = "";
        try {
            offset = mJSONObject.getString("offset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (offset == null || "".equals(offset)) {
            throw new RuntimeException("offset is null");
        }

        String length = "";
        try {
            length = mJSONObject.getString("length");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (length == null || "".equals(length)) {
            throw new RuntimeException("length is null");
        }


        WithdrawalsBillListBean mWithdrawalsBillListBean = new WithdrawalsBillListBean();
        mWithdrawalsBillListBean.setCode(0);
        mWithdrawalsBillListBean.setMsg("GetWithdrawalsBill in success");

        int max = 0;
        if (Integer.parseInt(offset, 10) + Integer.parseInt(length, 10) > 200) {
            max = 200;
        } else {
            max = Integer.parseInt(offset, 10) + Integer.parseInt(length, 10);
        }
        mWithdrawalsBillListBean.setOffset(offset);
        mWithdrawalsBillListBean.setLength(length);
        for (int i = Integer.parseInt(offset); i < max; i++) {
            WithdrawalsBillItemBean withdrawalsBillItemBean = new WithdrawalsBillItemBean();
            withdrawalsBillItemBean.setBill_id((1000 - i) + "");
            int total = ((int) (Math.random() * 30))*10000 + 100000;
            withdrawalsBillItemBean.setTotal_amount(total + "");
            int total_times = ((int) (Math.random() * 10)) + 3;
            withdrawalsBillItemBean.setTotal_times(total_times+"");
            int current_times = ((int) (Math.random() * (total_times-2))) + 1;
            withdrawalsBillItemBean.setCurrent_times(current_times+"");
            double repay_money = (total/total_times)*1.25;
            withdrawalsBillItemBean.setRepay_money((int)repay_money+"");
            int cousumeId = i%2==0?100:200;
            withdrawalsBillItemBean.setConsume_id(cousumeId+"");
            int isOverdue = ((int) (Math.random() * 2));
            withdrawalsBillItemBean.setIs_overdue(isOverdue+"");
            int remainderDays = ((int) (Math.random() * 20)) + 1;
            withdrawalsBillItemBean.setRemainder_repay_days(remainderDays+"");

            mWithdrawalsBillListBean.getData().add(withdrawalsBillItemBean);
        }


        return mWithdrawalsBillListBean;
    }
}
