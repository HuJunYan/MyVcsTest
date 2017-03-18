package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.CertUploadStatusBean;
import com.maibai.user.model.CertUploadStatusItemBean;
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
 * Created by sbyh on 16/7/5.
 */

public class GetCertUploadStatus extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetCertUploadStatus(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetCertUploadStatusUrl();
    }

    public void getCertUploadStatus(JSONObject jsonObject, final BaseNetCallBack<CertUploadStatusBean> mCertUploadStatusCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCertUploadStatusCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCertUploadStatusCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getCertUploadStatus(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CertUploadStatusBean> mCertUploadStatusCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCertUploadStatusCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCertUploadStatusCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<CertUploadStatusBean> mCertUploadStatusCallBack) {
        try {
            if (isRelease) {
                CertUploadStatusBean mCertUploadStatusBean = (CertUploadStatusBean) GsonUtil.json2bean(result, CertUploadStatusBean.class);
                mCertUploadStatusCallBack.onSuccess(mCertUploadStatusBean);
            } else {
                mCertUploadStatusCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CertUploadStatusBean> mCertUploadStatusCallBack) {
        try {
            if (isRelease) {
                mCertUploadStatusCallBack.onFailure(result, errorType, errorCode);
            } else {
                mCertUploadStatusCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private CertUploadStatusBean test() {
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

        CertUploadStatusBean mCertUploadStatusBean = new CertUploadStatusBean();
        mCertUploadStatusBean.setCode(0);
        mCertUploadStatusBean.setMsg("GetCertUploadStatus in success");
        for (int i = 2; i < 8; i++) {
            CertUploadStatusItemBean certUploadStatusItemBean = new CertUploadStatusItemBean();
            certUploadStatusItemBean.setType(i + "");
            certUploadStatusItemBean.setPhoto_url("http://d.hiphotos.baidu.com/image/pic/item/562c11dfa9ec8a13f075f10cf303918fa1ecc0eb.jpg");
            int type = ((int) (Math.random() * 4));
            certUploadStatusItemBean.setStatus(type + "");
            mCertUploadStatusBean.getData().add(certUploadStatusItemBean);
        }

        return mCertUploadStatusBean;
    }
}
