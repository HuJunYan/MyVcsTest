package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.model.WithdrawHistoryBean;
import com.maibei.merchants.model.WithdrawHistoryItemBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.maibei.merchants.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GetWithdrawHistory extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetWithdrawHistory(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawHistoryUrl();
    }

    public void getWithdrawHistory(JSONObject jsonObject, final BaseNetCallBack<WithdrawHistoryBean> mWithdrawHistoryCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawHistoryCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawHistoryCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getWithdrawHistory(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawHistoryBean> mWithdrawHistoryCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawHistoryCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawHistoryCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawHistoryBean> mWithdrawHistoryCallBack) {
        WithdrawHistoryBean mWithdrawHistoryBean;
        if (isRelease) {
            mWithdrawHistoryBean = (WithdrawHistoryBean) GsonUtil.json2bean(result, WithdrawHistoryBean.class);
        } else {
            mWithdrawHistoryBean = test();
        }
        mWithdrawHistoryCallBack.onSuccess(mWithdrawHistoryBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawHistoryBean> mWithdrawHistoryCallBack) {
        if (isRelease) {
            mWithdrawHistoryCallBack.onFailure(result, errorType, errorCode);
        } else {
            mWithdrawHistoryCallBack.onSuccess(test());
        }
    }

    private WithdrawHistoryBean test() {
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

        WithdrawHistoryBean mWithdrawHistoryBean = new WithdrawHistoryBean();
        mWithdrawHistoryBean.setCode(0);
        mWithdrawHistoryBean.setMsg("getWithdrawHistory success");
        mWithdrawHistoryBean.setTotal("200");
        mWithdrawHistoryBean.setLength(length);
        mWithdrawHistoryBean.setOffset(offset);
        int max = 0;
        if (Integer.parseInt(offset,10) + Integer.parseInt(length, 10) >100) {
            max = 100;
        } else {
            max = Integer.parseInt(offset,10) + Integer.parseInt(length, 10);
        }

        for (int i = Integer.parseInt(offset); i < max; i++) {
            WithdrawHistoryItemBean mWithdrawHistoryItemBean = new WithdrawHistoryItemBean();
            mWithdrawHistoryItemBean.setId(i+"");
            float amount = ((float) (Math.random() * 2000))/((float)13) + 1;
            mWithdrawHistoryItemBean.setAmount(amount + "");
            mWithdrawHistoryItemBean.setWithdraw_time("08-03 15:55");
            mWithdrawHistoryBean.getData().add(mWithdrawHistoryItemBean);
        }
        return mWithdrawHistoryBean;
    }
}