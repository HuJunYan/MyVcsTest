package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.ContactsInfoBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-10-10.
 */
public class GetUserInfo extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetUserInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getGetContactsInfoURL();
    }

    public void getUserInfo(JSONObject jsonObject, final BaseNetCallBack<ContactsInfoBean> ContactsInfoCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, ContactsInfoCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, ContactsInfoCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ContactsInfoBean> mCustomerAuthCallBack) {
        try {
            ContactsInfoBean mContactsInfoBean = (ContactsInfoBean) GsonUtil.json2bean(result, ContactsInfoBean.class);
            mCustomerAuthCallBack.onSuccess(mContactsInfoBean);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ContactsInfoBean> mCustomerAuthCallBack) {
        try {
            if (isRelease) {
                mCustomerAuthCallBack.onFailure(result, errorType, errorCode);
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
