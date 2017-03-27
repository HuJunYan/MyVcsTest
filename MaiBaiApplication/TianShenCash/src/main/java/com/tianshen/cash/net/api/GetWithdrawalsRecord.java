package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.WithdrawalsRecordBean;
import com.tianshen.cash.model.WithdrawalsRecordItemBean;
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
 * Created by m on 16-10-19.
 */
public class GetWithdrawalsRecord extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetWithdrawalsRecord(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsRecordURL();
    }

    public void getWithdrawalsBill(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsRecordCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsRecordCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getWithdrawalsBill(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsRecordCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsRecordCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            if (isRelease) {
                WithdrawalsRecordBean mWithdrawalsRecordBean = (WithdrawalsRecordBean) GsonUtil.json2bean(result, WithdrawalsRecordBean.class);
                mWithdrawalsRecordCallBack.onSuccess(mWithdrawalsRecordBean);
            } else {
                mWithdrawalsRecordCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsRecordCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsRecordCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsRecordBean test() {
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


        WithdrawalsRecordBean mWithdrawalsRecordBean = new WithdrawalsRecordBean();
        mWithdrawalsRecordBean.setCode(0);
        mWithdrawalsRecordBean.setMsg("GetWithdrawalsRecord in success");
        int max = 0;
        if (Integer.parseInt(offset, 10) + Integer.parseInt(length, 10) > 200) {
            max = 200;
        } else {
            max = Integer.parseInt(offset, 10) + Integer.parseInt(length, 10);
        }

        mWithdrawalsRecordBean.setOffset(offset);
        mWithdrawalsRecordBean.setLength(length);
        for (int i = Integer.parseInt(offset); i < max; i++) {
            WithdrawalsRecordItemBean withdrawalsRecordItemBean = new WithdrawalsRecordItemBean();
            withdrawalsRecordItemBean.setConsume_id((1000 - i) + "");
            int amount = ((int) (Math.random() * 30))*10000 + 100000;
            withdrawalsRecordItemBean.setAmount(amount+"");
            withdrawalsRecordItemBean.setConsume_time("2016-07-10 08:08:08");
            int repay_times = ((int) (Math.random() * 10)) + 3;
            withdrawalsRecordItemBean.setRepay_times(repay_times+"");
            mWithdrawalsRecordBean.getData().add(withdrawalsRecordItemBean);
        }

        return mWithdrawalsRecordBean;
    }
}
