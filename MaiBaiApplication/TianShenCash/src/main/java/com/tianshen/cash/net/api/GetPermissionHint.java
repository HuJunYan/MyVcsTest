package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.PermissionHintBean;
import com.tianshen.cash.model.PermissionHintItemBean;
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
 * Created by Administrator on 2016/8/2.
 */
public class GetPermissionHint extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetPermissionHint(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getOpenPermissionHintUrl();
    }

    public void getPermissionHint(JSONObject jsonObject, final BaseNetCallBack<PermissionHintBean> mPermissionHintCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mPermissionHintCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mPermissionHintCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getPermissionHint(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<PermissionHintBean> mPermissionHintCallBack) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mPermissionHintCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mPermissionHintCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<PermissionHintBean> mPermissionHintCallBack) {
        try {
            if (isRelease) {
                PermissionHintBean mPermissionHintBean = (PermissionHintBean) GsonUtil.json2bean(result, PermissionHintBean.class);
                mPermissionHintCallBack.onSuccess(mPermissionHintBean);
            } else {
                mPermissionHintCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<PermissionHintBean> mPermissionHintCallBack) {
        try {
            if (isRelease) {
                mPermissionHintCallBack.onFailure(result, errorType, errorCode);
            } else {
                mPermissionHintCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private PermissionHintBean test() {
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

        String permission_type = "";
        try {
            permission_type = mJSONObject.getString("permission_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (permission_type == null || "".equals(permission_type)) {
            throw new RuntimeException("permission_type is null");
        }

        String device_id = "";
        try {
            device_id = mJSONObject.getString("device_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_id == null || "".equals(device_id)) {
            throw new RuntimeException("device_id is null");
        }

        String device_os = "";
        try {
            device_os = mJSONObject.getString("device_os");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_os == null || "".equals(device_os)) {
            throw new RuntimeException("device_os is null");
        }

        String device_os_version = "";
        try {
            device_os_version = mJSONObject.getString("device_os_version");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_os_version == null || "".equals(device_os_version)) {
            throw new RuntimeException("device_os_version is null");
        }

        String device_mode = "";
        try {
            device_mode = mJSONObject.getString("device_mode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_mode == null || "".equals(device_mode)) {
            throw new RuntimeException("device_mode is null");
        }

        String device_brand = "";
        try {
            device_brand = mJSONObject.getString("device_brand");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (device_brand == null || "".equals(device_brand)) {
            throw new RuntimeException("device_brand is null");
        }

        PermissionHintBean mPermissionHintBean = new PermissionHintBean();
        mPermissionHintBean.setCode(0);
        mPermissionHintBean.setMsg("GetPermissionHint in success");
        mPermissionHintBean.getData().setTitle(permission_type + "类型的权限开启步骤");
        int random = ((int) (Math.random() * 3)) + 3;
        for (int i = 0; i < random; i++) {
            PermissionHintItemBean mPermissionHintItemBean = new PermissionHintItemBean();
            mPermissionHintItemBean.setStep(i + "");
            mPermissionHintItemBean.setHint("开启权限提示，第" + i + "步");
            mPermissionHintBean.getData().getOptions().add(mPermissionHintItemBean);
        }

        return mPermissionHintBean;
    }
}
