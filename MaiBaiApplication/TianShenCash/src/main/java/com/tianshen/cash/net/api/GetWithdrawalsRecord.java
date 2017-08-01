package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.WithdrawalsRecordBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by m on 16-10-19.
 */
public class GetWithdrawalsRecord extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetWithdrawalsRecord(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getWithdrawalsRecordURL();
    }

    public void getWithdrawalsBill(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWithdrawalsRecordCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWithdrawalsRecordCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            if (isRelease) {
                WithdrawalsRecordBean mWithdrawalsRecordBean = (WithdrawalsRecordBean) GsonUtil.json2bean(result, WithdrawalsRecordBean.class);
                mWithdrawalsRecordCallBack.onSuccess(mWithdrawalsRecordBean);
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WithdrawalsRecordBean> mWithdrawalsRecordCallBack) {
        try {
            if (isRelease) {
                mWithdrawalsRecordCallBack.onFailure(result, errorType, errorCode);
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

}
