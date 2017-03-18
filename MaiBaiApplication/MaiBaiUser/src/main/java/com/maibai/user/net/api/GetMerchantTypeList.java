package com.maibai.user.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.user.constant.NetConstantValue;
import com.maibai.user.model.MerchantTypeItemBean;
import com.maibai.user.model.MerchantTypeListBean;
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
 * Created by m on 16-9-14.
 */
public class GetMerchantTypeList extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetMerchantTypeList(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getMerchantTypeListURL();
    }

    public void getMerchantTypeList(JSONObject jsonObject, final BaseNetCallBack<MerchantTypeListBean> mMerchantTypeListCallBack) {
        try {
//            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMerchantTypeListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMerchantTypeListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getMerchantTypeList(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<MerchantTypeListBean> mMerchantTypeListCallBack) {
        try {
//            jsonObject.put("pay_pass", Utils.MD5SHA1AndReverse(jsonObject.getString("pay_pass")));
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mMerchantTypeListCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mMerchantTypeListCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<MerchantTypeListBean> mMerchantTypeListCallBack) {
        try {
            if (isRelease) {
                MerchantTypeListBean mMerchantTypeListBean = (MerchantTypeListBean) GsonUtil.json2bean(result, MerchantTypeListBean.class);
                for (int i = 0; i < mMerchantTypeListBean.getData().size(); i++) {
                    mMerchantTypeListBean.getData().get(i).setType_img(mMerchantTypeListBean.getImg_url() + mMerchantTypeListBean.getData().get(i).getType_img());
                }
                mMerchantTypeListCallBack.onSuccess(mMerchantTypeListBean);
            } else {
                mMerchantTypeListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<MerchantTypeListBean> mMerchantTypeListCallBack) {
        try {
            if (isRelease) {
                mMerchantTypeListCallBack.onFailure(result, errorType, errorCode);
            } else {
                mMerchantTypeListCallBack.onSuccess(test());
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private MerchantTypeListBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String province_name = "";
        try {
            province_name = mJSONObject.getString("province_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (province_name == null || "".equals(province_name)) {
            throw new RuntimeException("province_name is null");
        }

        String city_name = "";
        try {
            city_name = mJSONObject.getString("city_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (city_name == null || "".equals(city_name)) {
            throw new RuntimeException("city_name is null");
        }

        String county_name = "";
        try {
            county_name = mJSONObject.getString("county_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (county_name == null || "".equals(county_name)) {
            throw new RuntimeException("county_name is null");
        }

        MerchantTypeListBean mMerchantTypeListBean = new MerchantTypeListBean();
        mMerchantTypeListBean.setCode(0);
        mMerchantTypeListBean.setMsg("GetMerchantTypeList in success");
        String[] typeName = {"饭店", "超市", "电动车", "数码", "家居建材", "教育培训", "租房", "其他"};

        for (int i = 0; i < 8; i++) {
            MerchantTypeItemBean merchantTypeItemBean = new MerchantTypeItemBean();
            merchantTypeItemBean.setType_id(i + "");
            merchantTypeItemBean.setType_name(typeName[i]);
            merchantTypeItemBean.setType_img("http://pic.58pic.com/58pic/15/28/08/76X58PIC2UP_1024.jpg");
            mMerchantTypeListBean.getData().add(merchantTypeItemBean);
        }

        return mMerchantTypeListBean;
    }
}
