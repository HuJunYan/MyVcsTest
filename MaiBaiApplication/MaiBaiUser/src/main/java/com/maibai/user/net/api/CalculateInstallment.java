package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.CalculateInstallmentBean;
import com.maibai.user.model.CalculateInstallmentItemBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbyh on 16/7/5.
 */

public class CalculateInstallment extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public CalculateInstallment(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetCalculateInstallmentUrl();
    }

    public void calculateInstallment(JSONObject jsonObject, final BaseNetCallBack<CalculateInstallmentBean> mCalculateInstallmentCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCalculateInstallmentCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCalculateInstallmentCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void calculateInstallment(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CalculateInstallmentBean> mCalculateInstallmentCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCalculateInstallmentCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCalculateInstallmentCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<CalculateInstallmentBean> mCalculateInstallmentCallBack) {
        try {
            if (isRelease) {
                CalculateInstallmentBean mCalculateInstallmentBean = (CalculateInstallmentBean) GsonUtil.json2bean(result, CalculateInstallmentBean.class);
                mCalculateInstallmentCallBack.onSuccess(mCalculateInstallmentBean);
            } else {
                mCalculateInstallmentCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CalculateInstallmentBean> mCalculateInstallmentCallBack) {
        try {
            if (isRelease) {
                mCalculateInstallmentCallBack.onFailure(result, errorType, errorCode);
            } else {
                mCalculateInstallmentCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private CalculateInstallmentBean test() {
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

        String consume_id = "";
        try {
            consume_id = mJSONObject.getString("consume_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (consume_id == null || "".equals(consume_id)) {
            throw new RuntimeException("consume_id is null");
        }

        CalculateInstallmentBean mCalculateInstallmentBean = new CalculateInstallmentBean();
        mCalculateInstallmentBean.setCode(0);

        mCalculateInstallmentBean.setMsg("CalculateInstallment in success");
        int amount = ((int) (Math.random() * 50000)) + 10000;
        float total = (float) amount / (float) 5.5;
        int principal = amount / 6;
        float interest = total - principal;
        List<CalculateInstallmentItemBean> datas = new ArrayList<CalculateInstallmentItemBean>();
        for (int i = 0; i < 10; i++) {
            CalculateInstallmentItemBean data = new CalculateInstallmentItemBean();
            data.setRepay_times(10 + "");
            data.setRepay_date(10 + "");
            data.setRepay_total(20000 + "");
            data.setPay_interest(100 + "");
            data.setPay_principal(300 + "");
            datas.add(data);
        }

        mCalculateInstallmentBean.setData(datas);
        mCalculateInstallmentBean.setConsume_amount(amount + "");
       /* mCalculateInstallmentBean.getData().setConsume_amount(amount + "");
        mCalculateInstallmentBean.getData().setRepay_times("6");
        long repayDate = System.currentTimeMillis() / 1000 - 24 * 60 * 60L;
        mCalculateInstallmentBean.getData().setRepay_date(repayDate + "");
        mCalculateInstallmentBean.getData().setReplay_total(total + "");
        mCalculateInstallmentBean.getData().setPay_principal(principal + "");
        mCalculateInstallmentBean.getData().setPay_interest(interest + "");*/

        return mCalculateInstallmentBean;
    }
}
