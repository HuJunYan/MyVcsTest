package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ConstantBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;


public class CustomerInfoService extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public CustomerInfoService(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getCustomerInfoUrl();
    }

    public void getData(JSONObject jsonObject, final BaseNetCallBack<ConstantBean> callBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<ConstantBean> callBack) {
        try {
            if (isRelease) {
                ConstantBean bean = GsonUtil.json2bean(result, ConstantBean.class);
                callBack.onSuccess(bean);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ConstantBean> callBack) {
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

    private ConstantBean test() {
        ConstantBean constantBean = new ConstantBean();
        ConstantBean.Data data = constantBean.new Data();
        constantBean.setCode(0);
        data.setCompany_name("天神贷公司");
        data.setUser_address_county("");
        data.setUser_address_detail("");
        data.setUser_address_provice("");
        constantBean.setData(data);
        return constantBean;
    }
}