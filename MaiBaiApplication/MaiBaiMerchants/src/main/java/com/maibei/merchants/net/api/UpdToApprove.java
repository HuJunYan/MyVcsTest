package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.ResponseBean;
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
 * Created by Administrator on 2016/8/19.
 */
public class UpdToApprove extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public UpdToApprove(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetUpdToApproveUrl();
    }

    public void updToApprove(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    public void updToApprove(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
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

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mSaleListCallBack) {
        ResponseBean mResponseBean;
        if (isRelease) {
            mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
        } else {
            mResponseBean = test();
        }
        mSaleListCallBack.onSuccess(mResponseBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mSaleListCallBack) {
        if (isRelease) {
            mSaleListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mSaleListCallBack.onSuccess(test());
        }
    }

    private ResponseBean test() {
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
        String status = "";
        try {
            status = mJSONObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status == null || "".equals(status)) {
            throw new RuntimeException("status is null");
        }

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("UpdToApprove success");

        return mResponseBean;
    }
}