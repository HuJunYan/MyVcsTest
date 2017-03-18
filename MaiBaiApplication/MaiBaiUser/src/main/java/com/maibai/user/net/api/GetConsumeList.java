package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ConsumeItemBean;
import com.maibai.user.model.ConsumeListBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-9-14.
 */
public class GetConsumeList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetConsumeList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getConsumeListURL();
    }

    public void getConsumeList(JSONObject jsonObject, final BaseNetCallBack<ConsumeListBean> mConsumeListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getConsumeList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ConsumeListBean> mConsumeListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mConsumeListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mConsumeListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ConsumeListBean> mConsumeListCallBack) {
        try {
            if (isRelease) {
                ConsumeListBean mConsumeListBean = (ConsumeListBean) GsonUtil.json2bean(result, ConsumeListBean.class);
                for (int i = 0; i < mConsumeListBean.getData().size(); i++) {
                    mConsumeListBean.getData().get(i).setMerchant_logo(mConsumeListBean.getImg_url() + mConsumeListBean.getData().get(i).getMerchant_logo());
                }
                mConsumeListCallBack.onSuccess(mConsumeListBean);
            } else {
                mConsumeListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ConsumeListBean> mConsumeListCallBack) {
        try {
            if (isRelease) {
                mConsumeListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mConsumeListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ConsumeListBean test() {
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

        ConsumeListBean mConsumeListBean = new ConsumeListBean();
        mConsumeListBean.setCode(0);
        mConsumeListBean.setMsg("GetBankList in success");
        int random = ((int) (Math.random() * 5)) + 3;
        for (int i = 0; i < random; i++) {
            ConsumeItemBean consumeItemBean = new ConsumeItemBean();
            consumeItemBean.setConsume_id("100" + i);
            consumeItemBean.setMerchant_name("商户名称" + i);
            consumeItemBean.setMerchant_logo("http://pic.58pic.com/58pic/15/28/08/76X58PIC2UP_1024.jpg");
            int repay_type = (i % 2) + 1;
            consumeItemBean.setRepay_type(repay_type + "");
            consumeItemBean.setConsume_date("2016-08-01 18:22");
            consumeItemBean.setReal_pay("100+i*2");
            mConsumeListBean.getData().add(consumeItemBean);
        }

        return mConsumeListBean;
    }
}
