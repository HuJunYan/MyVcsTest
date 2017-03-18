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
 * Created by Administrator on 2016/8/2.
 */
public class SaveMerchantInfo extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SaveMerchantInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSaveInfoUrl();
    }

    public void saveMerchantInfo(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
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

    public void saveMerchantInfo(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
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
        String merchant_name = "";
        try {
            merchant_name = mJSONObject.getString("merchant_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (merchant_name == null || "".equals(merchant_name)) {
            throw new RuntimeException("merchant_name is null");
        }
        String owner_name = "";
        try {
            owner_name = mJSONObject.getString("owner_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (owner_name == null || "".equals(owner_name)) {
            throw new RuntimeException("owner_name is null");
        }
        String operate_year = "";
        try {
            operate_year = mJSONObject.getString("operate_year");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (operate_year == null || "".equals(operate_year)) {
            throw new RuntimeException("verify_code is null");
        }

        String sales_per_month = "";
        try {
            sales_per_month = mJSONObject.getString("sales_per_month");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sales_per_month == null || "".equals(sales_per_month)) {
            throw new RuntimeException("sales_per_month is null");
        }

        String address = "";
        try {
            address = mJSONObject.getString("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (address == null || "".equals(address)) {
            throw new RuntimeException("address is null");
        }

        String id_num = "";
        try {
            id_num = mJSONObject.getString("id_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (id_num == null || "".equals(id_num)) {
            throw new RuntimeException("id_num is null");
        }

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("saveMerchantInfo success");

        return mResponseBean;
    }
}