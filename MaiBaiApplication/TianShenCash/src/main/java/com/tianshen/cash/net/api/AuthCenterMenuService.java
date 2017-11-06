package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.AuthCenterMenuBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by sbyh on 16/7/4.
 */

public class AuthCenterMenuService extends NetBase {
    private boolean isRelease = false;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public AuthCenterMenuService(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getAuthCenterMenuURL();
    }

    public void getData(JSONObject jsonObject, final BaseNetCallBack<AuthCenterMenuBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
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

    private void successHandle(String result, String url, BaseNetCallBack<AuthCenterMenuBean> callBack) {
        try {
            if (isRelease) {
                AuthCenterMenuBean bean = GsonUtil.json2bean(result, AuthCenterMenuBean.class);
                callBack.onSuccess(bean);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<AuthCenterMenuBean> callBack) {
        try {
            if (isRelease) {
                callBack.onFailure(result, errorType, errorCode);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AuthCenterMenuBean test() {
        LogUtil.d("abc", "test");
        AuthCenterMenuBean authCenterMenuBean = new AuthCenterMenuBean();
       // AuthCenterMenuBean.Data data = new AuthCenterMenuBean().new Data();
        AuthCenterMenuBean.Data data = authCenterMenuBean.new Data();
        data.setAuth_id_num("1");
        data.setAuth_person_info("0");
        data.setAuth_credit("0");
        data.setCash_amount("0");
        authCenterMenuBean.setCode(0);
        authCenterMenuBean.setMsg("success1111");
        authCenterMenuBean.setData(data);
        return authCenterMenuBean;
    }
}
