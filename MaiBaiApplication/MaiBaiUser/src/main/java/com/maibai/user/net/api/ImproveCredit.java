package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ImproveQuotaBean;
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
 * Created by Administrator on 2016/8/2.
 */
public class ImproveCredit extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public ImproveCredit(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getImproveCreditUrl();
    }

    public void improveCredit(JSONObject jsonObject, final BaseNetCallBack<ImproveQuotaBean> mResponseCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
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

    public void improveCredit(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ImproveQuotaBean> mResponseCallBack) {
        try {
            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
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

    private void successHandle(String result, String url, BaseNetCallBack<ImproveQuotaBean> mResponseCallBack) {
        try {
            if (isRelease) {
                ImproveQuotaBean mResponseBean = (ImproveQuotaBean) GsonUtil.json2bean(result, ImproveQuotaBean.class);
                mResponseCallBack.onSuccess(mResponseBean);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ImproveQuotaBean> mResponseCallBack) {
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

    private ImproveQuotaBean test() {
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

        String qq = "";
        try {
            qq = mJSONObject.getString("qq");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (qq == null || "".equals(qq)) {
            throw new RuntimeException("qq is null");
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

        ImproveQuotaBean mResponseBean = new ImproveQuotaBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("resetPassword in success");

        return mResponseBean;
    }
}
