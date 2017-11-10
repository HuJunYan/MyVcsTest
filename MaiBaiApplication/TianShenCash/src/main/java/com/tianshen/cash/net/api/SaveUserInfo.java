package com.tianshen.cash.net.api;

import android.content.Context;

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
 * 保存用户个人信息
 */
public class SaveUserInfo extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SaveUserInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSaveCustomerInfoUrl();
    }

    public void saveCustomerInfoUrl(JSONObject jsonObject, final BaseNetCallBack<PostDataBean> callBack) {
        try {
            mJSONObject = SignUtils.signJsonContainList(jsonObject, "extroContacts");
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
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        PostDataBean mPostDataBean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(mPostDataBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
    }


}
