package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.CheckUpgradeBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sbyh on 16/7/5.
 */

public class CheckUpgrade extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public CheckUpgrade(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetCheckUpgradeUrl();
    }

    public void checkUpgrade(JSONObject jsonObject, final BaseNetCallBack<CheckUpgradeBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
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

    public void checkUpgrade(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CheckUpgradeBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
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

    private void successHandle(String result, String url, BaseNetCallBack<CheckUpgradeBean> mResponseCallBack){
        if (isRelease) {
            CheckUpgradeBean mCheckUpgradeBean = (CheckUpgradeBean) GsonUtil.json2bean(result, CheckUpgradeBean.class);
            mResponseCallBack.onSuccess(mCheckUpgradeBean);
        } else {
            mResponseCallBack.onSuccess(test());
        }
    }
    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CheckUpgradeBean> mResponseCallBack){
        if (isRelease) {
            mResponseCallBack.onFailure(result, errorType, errorCode);
        } else {
            mResponseCallBack.onSuccess(test());
        }
    }

    private CheckUpgradeBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String id = "";
        try {
            id = mJSONObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (id == null || "".equals(id)) {
            throw new RuntimeException("id is null");
        }

        String current_version = "";
        try {
            current_version = mJSONObject.getString("current_version");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (current_version == null || "".equals(current_version)) {
            throw new RuntimeException("current_version is null");
        }

        String app_type = "";
        try {
            app_type = mJSONObject.getString("app_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (app_type == null || "".equals(app_type)) {
            throw new RuntimeException("app_type is null");
        }
        CheckUpgradeBean mCheckUpgradeBean = new CheckUpgradeBean();
        int upgrade_or_not = ((int) (Math.random() * 3));
        if (upgrade_or_not != 0) {
            mCheckUpgradeBean.setCode(0);
            mCheckUpgradeBean.setMsg("CheckUpgrade in success");
            mCheckUpgradeBean.getData().setApp_type("1");
            mCheckUpgradeBean.getData().setDownload_url("http://serverName/download/app/android-1.2.apk");
            mCheckUpgradeBean.getData().setIntroduction("1.测试升级\n2.修改bug");
            int total = ((int) (Math.random() * 2));
            mCheckUpgradeBean.getData().setForce_upgrade(total + "");
        } else {
            mCheckUpgradeBean.setCode(1000);
            mCheckUpgradeBean.setMsg("not need to upgrade");
        }

        return mCheckUpgradeBean;
    }
}
