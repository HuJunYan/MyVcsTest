package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/5.
 */

public class GetDiffOrderPayApi extends NetBase {
    private final String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetDiffOrderPayApi(Context context) {
        super(context);
        this.mContext = context;
        mUrl = NetConstantValue.getDiffOrderPayURL();
    }

    public void getDiffOrderPay(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<PostDataBean> callBack) {
        try {
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
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }


    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        PostDataBean diffRateInfoBean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(diffRateInfoBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
