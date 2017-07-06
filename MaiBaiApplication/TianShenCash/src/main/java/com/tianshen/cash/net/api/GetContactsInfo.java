package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ContactsInfoBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-10-10.
 */
public class GetContactsInfo extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetContactsInfo(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getGetContactsInfoURL();
    }

    public void getContactsInfo(JSONObject jsonObject, final BaseNetCallBack<ContactsInfoBean> ContactsInfoCallBack) {
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

    public void getContactsInfo(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ContactsInfoBean> mCustomerAuthCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCustomerAuthCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCustomerAuthCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<ContactsInfoBean> mCustomerAuthCallBack) {
        try {
            ContactsInfoBean mContactsInfoBean;
            if (isRelease) {
                mContactsInfoBean = (ContactsInfoBean) GsonUtil.json2bean(result, ContactsInfoBean.class);
            } else {
                mContactsInfoBean = test();
            }
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
            } else {
                mCustomerAuthCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ContactsInfoBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString(GlobalParams.USER_CUSTOMER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        ContactsInfoBean mContactsInfoBean = new ContactsInfoBean();
        mContactsInfoBean.setCode(0);
        mContactsInfoBean.setMsg("GetContactsInfo success");
//        mContactsInfoBean.getData().setIs_can_change(GlobalParams.IS_CAN_CHANGE);
        mContactsInfoBean.getData().setIs_can_change(GlobalParams.CAN_NOT_CHANGE);
        mContactsInfoBean.getData().setUser_name("李二狗");
        mContactsInfoBean.getData().setUser_mobile("18634364997");
        mContactsInfoBean.getData().setId_card_num("370555555555555555");
        mContactsInfoBean.getData().setWechat("wechat_num");
        mContactsInfoBean.getData().setUser_address("用户地址xxx");
        mContactsInfoBean.getData().setCompany_phone("01012345678");
        mContactsInfoBean.getData().setCompany_name("工作单位xx工作单位xxxx技术股份游戏有些是开发就公司");
        mContactsInfoBean.getData().setParent_name("张三李四");
        mContactsInfoBean.getData().setParent_phone("13112345678");
        mContactsInfoBean.getData().setParent_address("父母地址xx父母地址xxxx父母地址");
        mContactsInfoBean.getData().setFriends_name("王二");
        mContactsInfoBean.getData().setFriends_phone("13212345678");

        return mContactsInfoBean;
    }
}
