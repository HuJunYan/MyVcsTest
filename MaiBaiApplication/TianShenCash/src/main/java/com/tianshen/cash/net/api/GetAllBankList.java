package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.BankListBean;
import com.tianshen.cash.model.BankListItemBean;
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
 * Created by Administrator on 2016/8/23.
 */
public class GetAllBankList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetAllBankList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetAllBankListUrl();
    }

    public void getAllBankList(JSONObject jsonObject, final BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBankListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBankListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getAllBankList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBankListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBankListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            BankListBean mBankListBean;
            if (isRelease) {
                mBankListBean = (BankListBean) GsonUtil.json2bean(result, BankListBean.class);
            } else {
                mBankListBean = test();
            }
            mBankListCallBack.onSuccess(mBankListBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            if (isRelease) {
                mBankListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mBankListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private BankListBean test() {
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
        String bank_card = "";
        try {
            bank_card = mJSONObject.getString("bank_card");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bank_card == null || "".equals(bank_card)) {
            throw new RuntimeException("bank_card is null");
        }

        BankListBean mBankListBean = new BankListBean();
        mBankListBean.setCode(0);
        mBankListBean.setMsg("getBandList success");
//        int max = 0;
//        if (Integer.parseInt(offset,10) + Integer.parseInt(length, 10) >100) {
//            max = 100;
//        } else {
//            max = Integer.parseInt(offset,10) + Integer.parseInt(length, 10);
//        }
        for (int i = 0; i < 20; i++) {
            BankListItemBean mBankListItemBean = new BankListItemBean();
            mBankListItemBean.setBank_id("10" + i);
            mBankListItemBean.setBank_name("银行 " + i);
            mBankListBean.getData().add(mBankListItemBean);
        }

        return mBankListBean;
    }
}
