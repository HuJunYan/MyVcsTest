package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.model.UserRepayDetailBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.SignUtils;

import org.json.JSONObject;


/**
 * Created by Administrator on 2017/8/24.
 */

public class UserRepayDetailApi extends NetBase {
    private Context mContext;
    private String mUrl;
    private JSONObject mJSONObject;

    public UserRepayDetailApi(Context context) {
        super(context);
        this.mContext = context;
        mUrl = NetConstantValue.getRepayDetailURL();
    }


    public void getUserRepayDetail(JSONObject jsonObject, final BaseNetCallBack<UserRepayDetailBean> callBack) {
        mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
        if (mJSONObject == null) {
            return;
        }
        getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
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

    private void successHandle(String result, String url, BaseNetCallBack<UserRepayDetailBean> callBack) {
        callBack.onSuccess(GsonUtil.json2bean(result,UserRepayDetailBean.class));
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<UserRepayDetailBean> callBack) {
    }
}
