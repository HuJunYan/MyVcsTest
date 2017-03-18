package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.WithdrawalsBillInfoBean;
import com.maibai.cash.model.WithdrawalsBillInfoItenBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-10-19.
 */
public class GetWithdrawalsBillInfo extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetWithdrawalsBillInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsBillInfoURL();
    }

    public void getWithdrawalsBill(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsBillInfoBean> mWithdrawalsBillInfoCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsBillInfoCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsBillInfoCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getWithdrawalsBill(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsBillInfoBean> mWithdrawalsBillInfoCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsBillInfoCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsBillInfoCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsBillInfoBean> mWithdrawalsBillInfoCallBack) {
        try {
            if (isRelease) {
                WithdrawalsBillInfoBean mWithdrawalsBillInfoBean = (WithdrawalsBillInfoBean) GsonUtil.json2bean(result, WithdrawalsBillInfoBean.class);
                mWithdrawalsBillInfoCallBack.onSuccess(mWithdrawalsBillInfoBean);
            } else {
                mWithdrawalsBillInfoCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsBillInfoBean> mWithdrawalsBillInfoCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsBillInfoCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsBillInfoCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsBillInfoBean test() {
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

        String bill_id = "";
        try {
            bill_id = mJSONObject.getString("bill_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bill_id == null || "".equals(bill_id)) {
            throw new RuntimeException("bill_id is null");
        }

        WithdrawalsBillInfoBean mWithdrawalsBillInfoBean = new WithdrawalsBillInfoBean();
        mWithdrawalsBillInfoBean.setCode(0);
        mWithdrawalsBillInfoBean.setMsg("GetWithdrawalsBillInfo in success");
        int amountInt = ((int) (Math.random() * 4) + 1)*100000;
        mWithdrawalsBillInfoBean.getData().setAmount(amountInt+"");
        int isDiscount = ((int) (Math.random() * 2));
        int transferAmountInt = (isDiscount==0?amountInt:(int)((double)amountInt*0.92));
        mWithdrawalsBillInfoBean.getData().setTransfer_amount(transferAmountInt+"");
        mWithdrawalsBillInfoBean.getData().setConsume_time("2016-07-10 08:08:08");


        int repayTimes = ((int) (Math.random() * 5)) + 5;
        mWithdrawalsBillInfoBean.getData().setRepay_times(repayTimes+"");
//        mWithdrawalsBillInfoBean.getData().setRepay_amount((int)((double)(amountInt/repayTimes)*1.27)+"");
        mWithdrawalsBillInfoBean.getData().setHave_repay("10000");
        mWithdrawalsBillInfoBean.getData().setNeed_repay_total((int)((double)(amountInt)*1.5)+"");
        for (int i = 0; i < repayTimes; i++) {
            WithdrawalsBillInfoItenBean item = new WithdrawalsBillInfoItenBean();
            item.setPrincipal((int)((double)(amountInt)*repayTimes)+"");
            if (i==0) {
                item.setLate_fee("");
                item.setState("0");
                item.setTime("08-10已还");
            }else if (i == 1) {
                item.setLate_fee("10000");
                item.setState("0");
                item.setTime("08-20已还");
            }else if (i == 2) {
                item.setLate_fee("5000");
                item.setState("1");
                item.setTime("逾期3天");
            }else {
                item.setLate_fee("0");
                item.setState("2");
                item.setTime("11-10待还");
            }
            mWithdrawalsBillInfoBean.getData().getList().add(item);
        }


        return mWithdrawalsBillInfoBean;
    }
}
