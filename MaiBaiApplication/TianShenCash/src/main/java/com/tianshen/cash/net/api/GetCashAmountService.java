package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.CashAmountBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;


public class GetCashAmountService extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetCashAmountService(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getCashAmountURL();
    }

    public void getData(JSONObject jsonObject, final BaseNetCallBack<CashAmountBean> callBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<CashAmountBean> callBack) {
        try {
            if (isRelease) {
                CashAmountBean bean = GsonUtil.json2bean(result, CashAmountBean.class);
                callBack.onSuccess(bean);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CashAmountBean> callBack) {
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

    private CashAmountBean test() {
        CashAmountBean cashAmountBean = new CashAmountBean();
        CashAmountBean.Data data = cashAmountBean.new Data();
        cashAmountBean.setCode(0);
        data.setCash_amount("0");
        data.setCash_amount_status("0");
        data.setJoke_url("https://www.baidu.com");
        cashAmountBean.setData(data);
        return cashAmountBean;
    }
}