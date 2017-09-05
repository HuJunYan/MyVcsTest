package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.DiffRateInfoBean;
import com.tianshen.cash.model.OrderConfirmBean;
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

public class GetDiffRateInfoApi extends NetBase {
    private Context mContext;
    private String mUrl;
    private JSONObject mJSONObject;

    public GetDiffRateInfoApi(Context context) {
        super(context);
        this.mContext = context;
        mUrl = NetConstantValue.getDiffRateInfoURL();
    }


    public void getDiffRateInfo(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<DiffRateInfoBean> callBack) {
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


    private void successHandle(String result, String url, BaseNetCallBack<DiffRateInfoBean> callBack) {
        DiffRateInfoBean diffRateInfoBean = GsonUtil.json2bean(result, DiffRateInfoBean.class);
        callBack.onSuccess(diffRateInfoBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<DiffRateInfoBean> callBack) {
        try {
            callBack.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
