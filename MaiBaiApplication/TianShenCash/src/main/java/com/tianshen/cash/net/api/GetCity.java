package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.AddressBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 得到省
 */

public class GetCity extends NetBase {
    private int flag = 1;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetCity(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getCityUrl();
    }

    public GetCity(Context context, int flag) {
        this(context);
        this.flag = flag;
    }

    public void getCity(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<AddressBean> callBack) {
        try {
            jsonObject.put("flag", flag); // 正常选择  flag = 1  银行卡 flag = 2
        } catch (JSONException e) {

        }
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
        LogUtil.d("abc", "解析前-----》");
        AddressBean addressBean = GsonUtil.json2bean(result, AddressBean.class);
        callBack.onSuccess(addressBean);
        LogUtil.d("abc", "解析后-----》");
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
