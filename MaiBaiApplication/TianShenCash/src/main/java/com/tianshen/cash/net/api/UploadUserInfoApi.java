package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.SignUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/7/4.
 */

public class UploadUserInfoApi extends NetBase {
    private final Context mContext;
    private String mUrl;
    private JSONObject mJSONObject;

    public UploadUserInfoApi(Context context) {
        super(context);
        this.mContext = context;
        mUrl = NetConstantValue.getUploadUserInfoURL();
    }

    public void uploadUserInfo(JSONObject jsonObject, final BaseNetCallBack<PostDataBean> callBack) {
        mJSONObject = SignUtils.signJsonContainList(jsonObject, GlobalParams.USER_INFO_APP_LIST, GlobalParams.USER_INFO_CALL_LIST, GlobalParams.USER_INFO_MESSAGE_LIST);
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

    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        PostDataBean mPostDataBean = GsonUtil.json2bean(result, PostDataBean.class);
        callBack.onSuccess(mPostDataBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
        callBack.onFailure(result, errorType, errorCode);
    }

}
