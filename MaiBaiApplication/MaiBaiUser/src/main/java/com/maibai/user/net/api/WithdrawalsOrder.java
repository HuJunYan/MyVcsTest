package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.WithdrawalsOrderBean;
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
 * Created by m on 16-10-18.
 */
public class WithdrawalsOrder extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public WithdrawalsOrder(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsOrderURL();
    }

    public void withdrawalsOrder(JSONObject jsonObject, final BaseNetCallBack<WithdrawalsOrderBean> mWithdrawalsOrderCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsOrderCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsOrderCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void withdrawalsOrder(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsOrderBean> mWithdrawalsOrderCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsOrderCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsOrderCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsOrderBean> mWithdrawalsOrderCallBack) {
        try {
            if (isRelease) {
                WithdrawalsOrderBean mWithdrawalsOrderBean = (WithdrawalsOrderBean) GsonUtil.json2bean(result, WithdrawalsOrderBean.class);
                mWithdrawalsOrderCallBack.onSuccess(mWithdrawalsOrderBean);
            } else {
                mWithdrawalsOrderCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsOrderBean> mWithdrawalsOrderCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsOrderCallBack.onFailure(result, errorType, errorCode);
            } else {
                mWithdrawalsOrderCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private WithdrawalsOrderBean test() {
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

        WithdrawalsOrderBean mWithdrawalsOrderBean = new WithdrawalsOrderBean();
        mWithdrawalsOrderBean.setCode(0);
        mWithdrawalsOrderBean.setMsg("WithdrawalsOrder on success");
        int amount = ((int) (Math.random() * 3) + 1)*100000;
        mWithdrawalsOrderBean.getData().setAmount(amount+"");
        int transferAmount = (int)(amount*0.92);
        mWithdrawalsOrderBean.getData().setTransfer_amount(transferAmount+"");
        mWithdrawalsOrderBean.getData().setConsume_time("1970-02-10 08:08:08");
        mWithdrawalsOrderBean.getData().setTransfer_time("24小时左右到帐");
        mWithdrawalsOrderBean.getData().setCard_num("01234567890123456");
        mWithdrawalsOrderBean.getData().setBank_name("招商银行");
        mWithdrawalsOrderBean.getData().setRepay_times("3");
        mWithdrawalsOrderBean.getData().setRepay_amount((amount/3*1.2)+"");

        return mWithdrawalsOrderBean;
    }
}
