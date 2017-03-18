package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.NearByMerchantItemBean;
import com.maibai.user.model.NearByMerchantListBean;
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
 * Created by sbyh on 16/7/4.
 */

public class GetNearByList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetNearByList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getNearByListUrl();
    }

    public void getNearByList(JSONObject jsonObject, final BaseNetCallBack<NearByMerchantListBean> mNearByMerchantListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mNearByMerchantListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mNearByMerchantListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getNearByList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<NearByMerchantListBean> mNearByMerchantListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mNearByMerchantListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mNearByMerchantListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<NearByMerchantListBean> mNearByMerchantListCallBack) {
        try {
            if (isRelease) {
                NearByMerchantListBean mVerifyCodeBean = (NearByMerchantListBean) GsonUtil.json2bean(result, NearByMerchantListBean.class);
                mNearByMerchantListCallBack.onSuccess(mVerifyCodeBean);
            } else {
                mNearByMerchantListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<NearByMerchantListBean> mNearByMerchantListCallBack) {
        try {
            if (isRelease) {
                mNearByMerchantListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mNearByMerchantListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private NearByMerchantListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String category = "";
        try {
            category = mJSONObject.getString("category");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (category == null || "".equals(category)) {
            throw new RuntimeException("mobile is null");
        }

        String location = "";
        try {
            location = mJSONObject.getString("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (location == null || "".equals(location)) {
            throw new RuntimeException("location is null");
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

        NearByMerchantListBean mNearByMerchantListBean = new NearByMerchantListBean();
        mNearByMerchantListBean.setCode(0);
        mNearByMerchantListBean.setMsg("GetNearByList in success");
        mNearByMerchantListBean.setOffset(offset);
        mNearByMerchantListBean.setLength(length);

        mNearByMerchantListBean.setTotal("200");
        int max = 0;
        if (Integer.parseInt(offset, 10) + Integer.parseInt(length, 10) > 200) {
            max = 200;
        } else {
            max = Integer.parseInt(offset, 10) + Integer.parseInt(length, 10);
        }
        for (int i = Integer.parseInt(offset); i < max; i++) {
            NearByMerchantItemBean nearByMerchantItemBean = new NearByMerchantItemBean();
            nearByMerchantItemBean.setId(i + "");
            nearByMerchantItemBean.setName("商户名称" + i);
            nearByMerchantItemBean.setCategory(category);
            nearByMerchantItemBean.setProvince("");
            nearByMerchantItemBean.setCity("北京市");
            nearByMerchantItemBean.setCountry("海淀区");
            nearByMerchantItemBean.setAddress("幸福路1号");
            nearByMerchantItemBean.setCoordinate("123.123213,453.1231231");
            nearByMerchantItemBean.setDistance("1234");
            nearByMerchantItemBean.setLogo("http://image.tianjimedia.com/uploadImages/2014/345/11/9U0WG45JO443.jpg");
            nearByMerchantItemBean.getPromotion().setIcon("http://photo.jschina.com.cn/p/201105/W020110509545464495279.jpg");
            nearByMerchantItemBean.getPromotion().setTitle("新用户首单直减50元");
            int random = ((int) (Math.random() * 10));
            long repayDate = System.currentTimeMillis() / 1000 - 24 * 60 * 60L * random;
            nearByMerchantItemBean.setCreate_time(repayDate + "");
            mNearByMerchantListBean.getData().add(nearByMerchantItemBean);
        }

        return mNearByMerchantListBean;
    }
}
