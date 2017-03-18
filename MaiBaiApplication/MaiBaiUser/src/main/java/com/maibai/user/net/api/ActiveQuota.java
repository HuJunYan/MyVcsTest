package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ActiveQuotaBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.maibai.user.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 14658 on 2016/7/12.
 */
public class ActiveQuota extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public ActiveQuota(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getActiveQuota();
    }

    public void activeQuota(JSONObject jsonObject, final BaseNetCallBack<ActiveQuotaBean> mActiveQuotaCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mActiveQuotaCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mActiveQuotaCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void activeQuota(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ActiveQuotaBean> mActiveQuotaCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mActiveQuotaCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mActiveQuotaCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ActiveQuotaBean> mActiveQuotaCallBack) {
        try {
            if (isRelease) {
                ActiveQuotaBean mResponseBean = (ActiveQuotaBean) GsonUtil.json2bean(result, ActiveQuotaBean.class);
                mActiveQuotaCallBack.onSuccess(mResponseBean);
            } else {
                mActiveQuotaCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ActiveQuotaBean> mResponseCallBack) {
        try {
            if (isRelease) {
                mResponseCallBack.onFailure(result, errorType, errorCode);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ActiveQuotaBean test() {
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
            throw new RuntimeException("id is null");
        }

        String pay_pass = "";
        try {
            pay_pass = mJSONObject.getString("pay_pass");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (pay_pass == null || "".equals(pay_pass)) {
            throw new RuntimeException("pay_pass is null");
        }

        ActiveQuotaBean mActiveQuotaBean = new ActiveQuotaBean();
        mActiveQuotaBean.setCode(0);
        mActiveQuotaBean.setMsg("ActiveQuota in success");
        mActiveQuotaBean.getData().setAmount("100");

        return mActiveQuotaBean;
    }
}
