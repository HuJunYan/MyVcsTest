package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.GetBankListBean;
import com.maibai.user.model.GetBankListItemBean;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.CallBack;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.NetBase;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sbyh on 16/7/4.
 */

public class GetBindBankList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetBindBankList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getBankListUrl();
    }

    public void getBindBankList(JSONObject jsonObject, final BaseNetCallBack<GetBankListBean> mGetBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mGetBankListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mGetBankListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getBindBankList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<GetBankListBean> mGetBankListCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mGetBankListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mGetBankListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<GetBankListBean> mGetBankListCallBack) {
        try {
            if (isRelease) {
                GetBankListBean mGetBankListBean = (GetBankListBean) GsonUtil.json2bean(result, GetBankListBean.class);
                mGetBankListCallBack.onSuccess(mGetBankListBean);
            } else {
                mGetBankListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<GetBankListBean> mGetBankListCallBack) {
        try {
            if (isRelease) {
                mGetBankListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mGetBankListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private GetBankListBean test() {
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

        GetBankListBean mGetBankListBean = new GetBankListBean();
        mGetBankListBean.setCode(0);
        mGetBankListBean.setMsg("GetBankList in success");
        int random = ((int) (Math.random() * 5));
        for (int i = 0; i < random; i++) {
            GetBankListItemBean getBankListItemBean = new GetBankListItemBean();
            getBankListItemBean.setCard_user_name("张三");
            getBankListItemBean.setCard_num("12345678" + i * 12);
            getBankListItemBean.setReserved_mobile("13212345678");
            mGetBankListBean.getData().add(getBankListItemBean);
        }

        return mGetBankListBean;
    }
}
