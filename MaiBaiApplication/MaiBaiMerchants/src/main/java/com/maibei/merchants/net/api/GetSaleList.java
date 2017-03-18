package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.SaleListBean;
import com.maibei.merchants.model.SaleListItemBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class GetSaleList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public GetSaleList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSaleListUrl();
    }

    public void getSaleList(JSONObject jsonObject, final BaseNetCallBack<SaleListBean> mSaleListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSaleListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSaleListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getSaleList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<SaleListBean> mSaleListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSaleListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSaleListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<SaleListBean> mSaleListCallBack){
        SaleListBean mSaleListBean;
        if (isRelease) {
            mSaleListBean = (SaleListBean) GsonUtil.json2bean(result, SaleListBean.class);
        } else {
            mSaleListBean = test();
        }
        mSaleListCallBack.onSuccess(mSaleListBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<SaleListBean> mSaleListCallBack){
        if (isRelease) {
            mSaleListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mSaleListCallBack.onSuccess(test());
        }
    }

    private SaleListBean test() {
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

        SaleListBean mSaleListBean = new SaleListBean();
        mSaleListBean.setCode(0);
        mSaleListBean.setMsg("getSaleList success");
        mSaleListBean.setTotal(100+"");
        mSaleListBean.setOffset(offset);
        mSaleListBean.setLength(length);
        int commision_status = (int)(Math.random() * 2);
        mSaleListBean.setCommision_status(commision_status + "");
        mSaleListBean.data = new ArrayList<SaleListItemBean>();
        int max = 0;
        if (Integer.parseInt(offset,10) + Integer.parseInt(length, 10) >100) {
            max = 100;
        } else {
            max = Integer.parseInt(offset,10) + Integer.parseInt(length, 10);
        }
        for (int i = Integer.parseInt(offset); i < max; i++) {
            SaleListItemBean mSaleListItemBean = new SaleListItemBean();
            mSaleListItemBean.setCustomer_name("姓名" + i);
            long consumeTime = System.currentTimeMillis() / 1000 - 100 * i;
            mSaleListItemBean.setConsume_time(consumeTime+"");
            float consumeAmount = ((float) (Math.random() * 2000))/((float)13);
            mSaleListItemBean.setConsume_amount(consumeAmount+"");
            mSaleListBean.getData().add(mSaleListItemBean);
        }

        return mSaleListBean;
    }
}
