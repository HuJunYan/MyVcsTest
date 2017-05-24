package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.model.SuperMarkerBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 得到省
 */

public class GetSuperMarkerList extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetSuperMarkerList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getFlowSupermarketListURL();
    }

    public void getSuperMarkerList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<SuperMarkerBean> callBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<SuperMarkerBean> callBack) {
        SuperMarkerBean superMarkerBean = GsonUtil.json2bean(result, SuperMarkerBean.class);
        callBack.onSuccess(superMarkerBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<SuperMarkerBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }


}
