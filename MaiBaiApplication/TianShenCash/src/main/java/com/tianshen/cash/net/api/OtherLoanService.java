package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PostDataBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * 提交其他借款方式
 */
public class OtherLoanService extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public OtherLoanService(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.otherLoanURL();
    }

    public void postData(JSONObject jsonObject, final BaseNetCallBack<PostDataBean> callBack) {
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

    private void successHandle(String result, String url, BaseNetCallBack<PostDataBean> callBack) {
        try {
            if (isRelease) {
                PostDataBean bean = GsonUtil.json2bean(result, PostDataBean.class);
                callBack.onSuccess(bean);
            } else {
                callBack.onSuccess(test());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PostDataBean> callBack) {
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

    private PostDataBean test() {
        PostDataBean postDataBean = new PostDataBean();
        postDataBean.setCode(0);
        postDataBean.setMsg("success");
        return postDataBean;
    }
}