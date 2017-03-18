package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.CityListBean;
import com.maibei.merchants.model.CityListItemBean;
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
public class GetCity extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;
    public GetCity(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetCityUrl();
    }

    public void getCity(JSONObject jsonObject, final BaseNetCallBack<CityListBean> mCityListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCityListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCityListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getCity(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CityListBean> mCityListCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCityListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCityListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<CityListBean> mCityListCallBack){
        CityListBean mCityListBean;
        if (isRelease) {
            mCityListBean = (CityListBean) GsonUtil.json2bean(result, CityListBean.class);
        } else {
            mCityListBean = test();
        }
        mCityListCallBack.onSuccess(mCityListBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CityListBean> mCityListCallBack){
        if (isRelease) {
            mCityListCallBack.onFailure(result, errorType, errorCode);
        } else {
            mCityListCallBack.onSuccess(test());
        }
    }

    private CityListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String province_id = "";
        try {
            province_id = mJSONObject.getString("province_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (province_id == null || "".equals(province_id)) {
            throw new RuntimeException("province_id is null");
        }

        CityListBean mCityListBean = new CityListBean();
        mCityListBean.setCode(0);
        mCityListBean.setMsg("getCity success");
        for (int i = 0; i < 20; i++) {
            CityListItemBean mCityListItemBean = new CityListItemBean();
            mCityListItemBean.setCity_id("10" + i);
            mCityListItemBean.setCity_name("城市 " + i);
            mCityListBean.getData().add(mCityListItemBean);
        }

        return mCityListBean;
    }
}
