package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.ActiveQuotaBean;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chunpengguo on 2016/12/30.
 */

public class AgainVerify extends NetBase{
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public AgainVerify(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getAgainUrl();
    }

    public void againVerify(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> againVerifyCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, againVerifyCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, againVerifyCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void againVerify(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> againVerifyCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, againVerifyCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, againVerifyCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> againVerifyCallBack) {
        try {
            if (isRelease) {
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                againVerifyCallBack.onSuccess(mResponseBean);
            } else {
                againVerifyCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> againVerifyCallBack) {
        try {
            if (isRelease) {
                againVerifyCallBack.onFailure(result, errorType, errorCode);
            } else {
                againVerifyCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ResponseBean test() {
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

        String card_user_name = "";
        try {
            card_user_name = mJSONObject.getString("card_user_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (card_user_name == null || "".equals(card_user_name)) {
            throw new RuntimeException("card_user_name is null");
        }

        String card_num = "";
        try {
            card_num = mJSONObject.getString("card_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (card_num == null || "".equals(card_num)) {
            throw new RuntimeException("card_num is null");
        }

        String reserved_mobile = "";
        try {
            reserved_mobile = mJSONObject.getString("reserved_mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (reserved_mobile == null || "".equals(reserved_mobile)) {
            throw new RuntimeException("reserved_mobile is null");
        }

        String verify_code = "";
        try {
            verify_code = mJSONObject.getString("verify_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (verify_code == null || "".equals(verify_code)) {
            throw new RuntimeException("verify_code is null");
        }

        String bind_no = "";
        try {
            bind_no = mJSONObject.getString("bind_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bind_no == null || "".equals(bind_no)) {
            throw new RuntimeException("bind_no is null");
        }

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("bindBankCard in success");

        return mResponseBean;
    }
}
