package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.CountyListBean;
import com.maibei.merchants.model.CountyListItemBean;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.CallBack;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.NetBase;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m on 16-11-2.
 */
public class GetCounty extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public GetCounty(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetCountyUrl();
    }

    public void getCounty(JSONObject jsonObject, final BaseNetCallBack<CountyListBean> mCountyListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCountyListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCountyListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getCounty(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CountyListBean> mCountyListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCountyListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCountyListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<CountyListBean> mCountyListCallBack){
        CountyListBean mCountyListBean;
        if (isRelease) {
            mCountyListBean = (CountyListBean) GsonUtil.json2bean(result, CountyListBean.class);
        } else {
            mCountyListBean = test();
        }
        mCountyListCallBack.onSuccess(mCountyListBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CountyListBean> mCountyListCallBack){
        if (isRelease) {
            mCountyListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mCountyListCallBack.onSuccess(test());
        }
    }

    private CountyListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String city_id = "";
        try {
            city_id = mJSONObject.getString("city_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (city_id == null || "".equals(city_id)) {
            throw new RuntimeException("city_id is null");
        }

        CountyListBean mCountyListBean = new CountyListBean();
        mCountyListBean.setCode(0);
        mCountyListBean.setMsg("getCounty success");
        for (int i = 0; i < 20; i++) {
            CountyListItemBean mCountyListItemBean = new CountyListItemBean();
            mCountyListItemBean.setCounty_id("10" + i);
            mCountyListItemBean.setCounty_name("州县 " + i);
            mCountyListBean.getData().add(mCountyListItemBean);
        }

        return mCountyListBean;
    }
}
