package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.BankListBean;
import com.maibei.merchants.model.BankListItemBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/6.
 */
public class GetBankList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public GetBankList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetBankListUrl();
    }

    public void getBankList(JSONObject jsonObject, final BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    public void getBankList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<BankListBean> mBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    private void successHandle(String result, String url, BaseNetCallBack<BankListBean> mBankListCallBack){
        BankListBean mBankListBean;
        if (isRelease) {
            mBankListBean = (BankListBean) GsonUtil.json2bean(result, BankListBean.class);
        } else {
            mBankListBean = test();
        }
        mBankListCallBack.onSuccess(mBankListBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<BankListBean> mBankListCallBack){
        if (isRelease) {
            mBankListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mBankListCallBack.onSuccess(test());
        }
    }

    private BankListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String merchant_id = "";
        try {
            merchant_id = mJSONObject.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (merchant_id == null || "".equals(merchant_id)) {
            throw new RuntimeException("merchant_id is null");
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
