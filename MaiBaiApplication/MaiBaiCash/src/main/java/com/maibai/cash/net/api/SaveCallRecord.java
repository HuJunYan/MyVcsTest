package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.SaveCallRecordBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chenrongshang on 16/7/5.
 */
public class SaveCallRecord extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public SaveCallRecord(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getSaveCallRecordUrl();
    }

    public void saveCallRecord(JSONObject jsonObject, final BaseNetCallBack<SaveCallRecordBean> mSaveCallRecordCallBack) {
        try {
            mJSONObject = SignUtils.signJsonContainList(jsonObject, "record_list");
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSaveCallRecordCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSaveCallRecordCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void saveCallRecord(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<SaveCallRecordBean> mSaveCallRecordCallBack) {
        try {
            mJSONObject = SignUtils.signJsonContainList(jsonObject, "record_list");
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mSaveCallRecordCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mSaveCallRecordCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<SaveCallRecordBean> mSaveCallRecordCallBack) {
        try {
            if (isRelease) {
                SaveCallRecordBean mSaveCallRecord = (SaveCallRecordBean) GsonUtil.json2bean(result, SaveCallRecordBean.class);
                mSaveCallRecordCallBack.onSuccess(mSaveCallRecord);
            } else {
                mSaveCallRecordCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<SaveCallRecordBean> mSaveCallRecordCallBack) {
        try {
            if (isRelease) {
                mSaveCallRecordCallBack.onFailure(result, errorType, errorCode);
            } else {
                mSaveCallRecordCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private SaveCallRecordBean test() {
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

        String record_list = "";
        try {
            record_list = mJSONObject.getString("record_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (record_list == null || "".equals(record_list)) {
            throw new RuntimeException("record_list is null");
        }

        SaveCallRecordBean mSaveCallRecordBean = new SaveCallRecordBean();
        mSaveCallRecordBean.setCode(0);
        mSaveCallRecordBean.setMsg("SaveCallRecord in success");

        return mSaveCallRecordBean;
    }

    private String signJsonBea() {
        return null;
    }

    private String signSubJsonObject(JSONObject jsonObject) {
        List<String> keyList = SignUtils.getSortedKeyList(jsonObject);
        String secretKey = "";
        String paramString = "";
        try {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                paramString += key + "=" + jsonObject.getString(key);
                if (i != keyList.size() - 1) {
                    paramString += "&";
                }
            }
            secretKey = Utils.md5(paramString + GlobalParams.getSlot());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

}
