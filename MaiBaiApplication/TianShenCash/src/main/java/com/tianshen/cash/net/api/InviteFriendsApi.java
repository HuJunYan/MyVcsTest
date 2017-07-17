package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.InviteFriendsBean;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.SignUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/17.
 */

public class InviteFriendsApi extends NetBase {
    private Context mContext;
    private String mUrl;
    private JSONObject mJSONObject;

    public InviteFriendsApi(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getInviteFriendsUrl();
    }

    public void getInviteData(JSONObject jsonObject, final BaseNetCallBack<InviteFriendsBean> callBack) {
        mJSONObject = SignUtils.signJsonContainList(jsonObject);
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
    }

    private void successHandle(String result, String url, BaseNetCallBack<InviteFriendsBean> callBack) {
        InviteFriendsBean inviteFriendsBean = GsonUtil.json2bean(result, InviteFriendsBean.class);
        callBack.onSuccess(inviteFriendsBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<InviteFriendsBean> callBack) {
        callBack.onFailure(result, errorType, errorCode);
    }

}
