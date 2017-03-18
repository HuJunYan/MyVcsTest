package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.BindVerifySmsBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/5.
 */
public class GetBindVerifySms extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetBindVerifySms(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getBindVerifySmsUrl();
    }

    public void getBindVerifySms(JSONObject jsonObject, final BaseNetCallBack<BindVerifySmsBean> mBindVerifySmsCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBindVerifySmsCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBindVerifySmsCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getBindVerifySms(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<BindVerifySmsBean> mBindVerifySmsCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mBindVerifySmsCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mBindVerifySmsCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<BindVerifySmsBean> mBindVerifySmsCallBack) {
        try {
            if (isRelease) {
                BindVerifySmsBean mBindVerifySmsBean = (BindVerifySmsBean) GsonUtil.json2bean(result, BindVerifySmsBean.class);
                mBindVerifySmsCallBack.onSuccess(mBindVerifySmsBean);
            } else {
                mBindVerifySmsCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<BindVerifySmsBean> mBindVerifySmsCallBack) {
        try {
            if (isRelease) {
                mBindVerifySmsCallBack.onFailure(result, errorType, errorCode);
            } else {
                mBindVerifySmsCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private BindVerifySmsBean test() {
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

        BindVerifySmsBean mBindVerifySmsBean = new BindVerifySmsBean();
        mBindVerifySmsBean.setCode(0);
        mBindVerifySmsBean.setMsg("getBindVerifySms in success");
        mBindVerifySmsBean.getData().setBind_no("123123");

        return mBindVerifySmsBean;
    }
}
