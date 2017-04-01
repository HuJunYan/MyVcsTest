package com.maibai.cash.net.api;

import android.content.Context;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.ExtroContactsBean;
import com.maibai.cash.model.PostDataBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class GetExtroContacts extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetExtroContacts(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getExtroContactsUrl();
    }

    public void getExtroContacts(JSONObject jsonObject, final BaseNetCallBack<ExtroContactsBean> callBack) {
        try {
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
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ExtroContactsBean> callBack) {
        ExtroContactsBean mExtroContactsBean = GsonUtil.json2bean(result, ExtroContactsBean.class);
        callBack.onSuccess(mExtroContactsBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ExtroContactsBean> callBack) {
    }


}
