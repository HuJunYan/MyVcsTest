package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.CashSubItemBean;
import com.tianshen.cash.model.ConstantBean;
import com.tianshen.cash.model.OtherLoanBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 得到其他借款方式
 */
public class GetOtherLoanService extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetOtherLoanService(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getOtherLoanURL();
    }

    public void getData(JSONObject jsonObject, final BaseNetCallBack<OtherLoanBean> callBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, callBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, callBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<OtherLoanBean> callBack) {
        try {
            if (isRelease) {
                OtherLoanBean bean = GsonUtil.json2bean(result, OtherLoanBean.class);
                callBack.onSuccess(bean);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<OtherLoanBean> callBack) {
        try {
            if (isRelease) {
                callBack.onFailure(result, errorType, errorCode);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OtherLoanBean test() {
        OtherLoanBean otherLoanBean = new OtherLoanBean();
        OtherLoanBean.Data data = otherLoanBean.new Data();
        otherLoanBean.setCode(0);
        data.setMin_cash("60000");
        data.setMax_cash("100000");
        data.setUnit("10000");

        ArrayList<CashSubItemBean> cashSubItemBeans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            // private String withdrawal_amount; // 提现金额单位分
//            private String repay_total; // 每期还款数，单位分
//            private String transfer_amount;//到账金额,单位分
            CashSubItemBean cashSubItemBean = new CashSubItemBean();
            cashSubItemBean.setRepay_total("");
            cashSubItemBean.setTransfer_amount("");
            int Withdrawal_amount = 60000 + i * 10000;
            int repay_total = 80000 + i * 10000;

            cashSubItemBean.setWithdrawal_amount("" + Withdrawal_amount);
            cashSubItemBean.setRepay_total("" + repay_total);
            cashSubItemBean.setTransfer_amount("" + Withdrawal_amount);
            cashSubItemBeans.add(cashSubItemBean);
        }
        data.setCash_data(cashSubItemBeans);
        otherLoanBean.setData(data);
        return otherLoanBean;
    }
}