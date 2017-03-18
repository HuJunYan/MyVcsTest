package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
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
 * Created by sbyh on 16/7/5.
 */

public class Repayment extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public Repayment(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getRepaymentUrl();
    }

//    public void repayment(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
//        try {
//            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
//            mJSONObject = SignUtils.signJsonContainTwoLevelList(jsonObject, "consume_data", "installment_history");
//            if (mJSONObject == null) {
//                return;
//            }
//            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
//                @Override
//                public void onSuccess(String result, String url) {
//                    successHandle(result, url, mResponseCallBack);
//                }
//
//                @Override
//                public void onFailure(String result, int errorType, int errorCode) {
//                    failureHandle(result, errorType, errorCode, mResponseCallBack);
//                }
//            });
//        } catch (Exception e) {
//            MobclickAgent.reportError(mContext, LogUtil.getException(e));
//            e.printStackTrace();
//        }
//    }

    /**
     *
     * @param jsonObject
     * @param view
     * @param isShowDialog
     * @param type 0:首付， 1：消费次月还款， 2：消费分期还款， 5：现金贷分期还款
     * @param mResponseCallBack
     */
    public void repayment(JSONObject jsonObject, View view, boolean isShowDialog, int type, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (jsonObject == null) {
                return;
            }
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            switch (type) {
                case 0:
                    mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
                    break;
                case 1:
                    mJSONObject = SignUtils.signJsonContainList(jsonObject, "consume_data");
                    break;
                case 2:
                case 5:
                    mJSONObject = SignUtils.signJsonContainTwoLevelList(jsonObject, "consume_data", "installment_history");
                    break;
            }
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

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (isRelease) {
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                mResponseCallBack.onSuccess(mResponseBean);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mResponseCallBack) {
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
            throw new RuntimeException("id is null");
        }

        String amount = "";
        try {
            amount = mJSONObject.getString("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (amount == null || "".equals(amount)) {
            throw new RuntimeException("amount is null");
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

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("Repayment in success");

        return mResponseBean;
    }
}
