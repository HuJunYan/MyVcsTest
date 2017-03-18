package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.MineBean;
import com.maibai.cash.model.MineCardInfoBean;
import com.maibai.cash.model.RecordPopBean;
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
 * Created by chunpengguo on 2017/1/17.
 */

public class RecordPopup  extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public RecordPopup(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getRecordPopupTimeUrl();
    }

    public void recordPopup(JSONObject jsonObject, final BaseNetCallBack<RecordPopBean> mMineCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMineCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMineCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void recordPopup(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<RecordPopBean> mMineCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMineCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMineCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<RecordPopBean> mMineCallBack) {
        try {
            if (isRelease) {
                RecordPopBean mMineBean = (RecordPopBean) GsonUtil.json2bean(result, RecordPopBean.class);
                mMineCallBack.onSuccess(mMineBean);
            } else {
                mMineCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<RecordPopBean> mMineCallBack) {
        try {
            if (isRelease) {
                mMineCallBack.onFailure(result, errorType, errorCode);
            } else {
                mMineCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private RecordPopBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        RecordPopBean mMineBean = new RecordPopBean();
        mMineBean.setCode(0);
        mMineBean.setMsg("Mine in success");
        int random = ((int) (Math.random() * 5));
        for (int i = 0; i < random; i++) {
            MineCardInfoBean mineCardInfoBean = new MineCardInfoBean();
            mineCardInfoBean.setCard_user_name("张三");
            mineCardInfoBean.setCard_num("123456789012" + (1 + i) * 13);
            mineCardInfoBean.setReserved_mobile("133123456" + (i + 1) * 12);

        }

        return mMineBean;
    }
}
