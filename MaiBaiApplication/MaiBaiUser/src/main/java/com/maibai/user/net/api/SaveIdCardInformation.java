package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.ResponseBean;
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

public class SaveIdCardInformation extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SaveIdCardInformation(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSaveIdCardInformationUrl();
    }

    public void saveIdCardInformation(JSONObject jsonObject, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
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

    public void saveIdCardInformation(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
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

    private void successHandle(String result, String url, BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (isRelease) {
                ResponseBean mResponseBean = (ResponseBean) GsonUtil.json2bean(result, ResponseBean.class);
                mResponseCallBack.onSuccess(mResponseBean);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<ResponseBean> mResponseCallBack) {
        try {
            if (isRelease) {
                mResponseCallBack.onFailure(result, errorType, errorCode);
            } else {
                mResponseCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private ResponseBean test() {
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

        String real_name = "";
        try {
            real_name = mJSONObject.getString("real_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (real_name == null || "".equals(real_name)) {
            throw new RuntimeException("real_name is null");
        }

        String gender = "";
        try {
            gender = mJSONObject.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (gender == null || "".equals(gender)) {
            throw new RuntimeException("gender is null");
        }

        String nation = "";
        try {
            nation = mJSONObject.getString("nation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (nation == null || "".equals(nation)) {
            throw new RuntimeException("nation is null");
        }

        String birthday = "";
        try {
            birthday = mJSONObject.getString("birthday");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (birthday == null || "".equals(birthday)) {
            throw new RuntimeException("birthday is null");
        }

        String birthplace = "";
        try {
            birthplace = mJSONObject.getString("birthplace");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (birthplace == null || "".equals(birthplace)) {
            throw new RuntimeException("birthplace is null");
        }

        String id_num = "";
        try {
            id_num = mJSONObject.getString("id_num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (id_num == null || "".equals(id_num)) {
            throw new RuntimeException("id_num is null");
        }

        String sign_organ = "";
        try {
            sign_organ = mJSONObject.getString("sign_organ");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sign_organ == null || "".equals(sign_organ)) {
            throw new RuntimeException("sign_organ is null");
        }

        String valid_period = "";
        try {
            valid_period = mJSONObject.getString("valid_period");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (valid_period == null || "".equals(valid_period)) {
            throw new RuntimeException("valid_period is null");
        }

        ResponseBean mResponseBean = new ResponseBean();
        mResponseBean.setCode(0);
        mResponseBean.setMsg("saveIdCardInformation in success");

        return mResponseBean;
    }
}
