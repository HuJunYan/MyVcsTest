package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.LastSaveCallRecordTimeBean;
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
 * Created by chenrongshang on 16/7/5.
 */
public class GetLastSaveCallRecordTime extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetLastSaveCallRecordTime(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getLastSaveCallRecordTimeUrl();
    }

    public void getLastSaveCallRecordTime(JSONObject jsonObject, final BaseNetCallBack<LastSaveCallRecordTimeBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mResponseCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mResponseCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getLastSaveCallRecordTime(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<LastSaveCallRecordTimeBean> mLastSaveCallRecordTimeCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mLastSaveCallRecordTimeCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mLastSaveCallRecordTimeCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<LastSaveCallRecordTimeBean> mLastSaveCallRecordTimeCallBack) {
        try {
            if (isRelease) {
                LastSaveCallRecordTimeBean mLastSaveCallRecordTimeBean = (LastSaveCallRecordTimeBean) GsonUtil.json2bean(result, LastSaveCallRecordTimeBean.class);
                mLastSaveCallRecordTimeCallBack.onSuccess(mLastSaveCallRecordTimeBean);
            } else {
                mLastSaveCallRecordTimeCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<LastSaveCallRecordTimeBean> mLastSaveCallRecordTimeCallBack) {
        try {
            if (isRelease) {
                mLastSaveCallRecordTimeCallBack.onFailure(result, errorType, errorCode);
            } else {
                mLastSaveCallRecordTimeCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private LastSaveCallRecordTimeBean test() {
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

        LastSaveCallRecordTimeBean mLastSaveCallRecordTimeBean = new LastSaveCallRecordTimeBean();
        mLastSaveCallRecordTimeBean.setCode(0);
        mLastSaveCallRecordTimeBean.setMsg("GetLastSaveCallRecordTime in success");
        mLastSaveCallRecordTimeBean.getData().setCustomer_id(customer_id);
        int random = ((int) (Math.random() * 10));
        long lastTime = System.currentTimeMillis() / 1000 - 24 * 60 * 60L * random;
        mLastSaveCallRecordTimeBean.getData().setLast_save_time(lastTime + "");

        return mLastSaveCallRecordTimeBean;
    }
}
