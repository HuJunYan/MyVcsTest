package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.AddressBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 得到省
 */

public class GetCity extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetCity(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getCityUrl();
    }

    public void getCity(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<AddressBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
            @Override
            public void onSuccess(String result, String url) {
                successHandle(result, url, callBack);
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                failureHandle(result, errorType, errorCode, callBack);
            }
        });
    }

    private void successHandle(String result, String url, BaseNetCallBack<AddressBean> callBack) {
        AddressBean addressBean = GsonUtil.json2bean(result, AddressBean.class);
        callBack.onSuccess(addressBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<AddressBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }


}
