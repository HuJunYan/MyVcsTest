package com.maibei.merchants.net.api;

import android.content.Context;
import android.view.View;

import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.model.CommisionBean;
import com.maibei.merchants.model.CommisionListBean;
import com.maibei.merchants.model.CommisionStagesItemBean;
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
 * Created by m on 16-11-18.
 */
public class GetMerchantCommision extends NetBase {
    private boolean isRelease = true;
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public GetMerchantCommision(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.GetMerchantCommisionUrl();
    }

    public void getMerchantCommision(JSONObject jsonObject, final BaseNetCallBack<CommisionBean> mCommisionCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCommisionCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCommisionCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    public void getMerchantCommision(JSONObject jsonObject, View view, boolean isShowDialog, final BaseNetCallBack<CommisionBean> mCommisionCallBack) {
        try {
            mJSONObject = SignUtils.signJSONbject(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mCommisionCallBack);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mCommisionCallBack);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<CommisionBean> mCommisionCallBack) {
        CommisionBean mCommisionBean;
        if (isRelease) {
            mCommisionBean = (CommisionBean) GsonUtil.json2bean(result, CommisionBean.class);
        } else {
            mCommisionBean = test();
        }
        mCommisionCallBack.onSuccess(mCommisionBean);
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<CommisionBean> mCommisionCallBack) {
        if (isRelease) {
            mCommisionCallBack.onFailure(result, errorType, errorCode);
        } else {
            mCommisionCallBack.onSuccess(test());
        }
    }

    private CommisionBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }

        String merchant_id = "";
        try {
            merchant_id = mJSONObject.getString("merchant_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (merchant_id == null || "".equals(merchant_id)) {
            throw new RuntimeException("merchant_id is null");
        }
        String offset = "";
        try {
            offset = mJSONObject.getString("offset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (offset == null || "".equals(offset)) {
            throw new RuntimeException("offset is null");
        }

        String length = "";
        try {
            length = mJSONObject.getString("length");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (length == null || "".equals(length)) {
            throw new RuntimeException("length is null");
        }

        CommisionBean mCommisionBean = new CommisionBean();
        mCommisionBean.setCode(0);
        mCommisionBean.setMsg("GetMerchantCommision success");
        mCommisionBean.setTotal("200");
        mCommisionBean.setLength(length);
        mCommisionBean.setOffset(offset);
        int commisionAll = ((int) (Math.random() * 2000) + 1000) * 1000;
        mCommisionBean.getData().setCommision_all(commisionAll + "");
        int max = 0;
        if (Integer.parseInt(offset, 10) + Integer.parseInt(length, 10) > 100) {
            max = 100;
        } else {
            max = Integer.parseInt(offset, 10) + Integer.parseInt(length, 10);
        }
        String[] dateArray = {"总佣金", "2016-10-11", "2016-11-11", "2016-12-11", "2017-01-11", "2017-02-11"};
        for (int i = Integer.parseInt(offset); i < max; i++) {
            CommisionListBean commisionListBean = new CommisionListBean();
            commisionListBean.setCustomer_name("姓名 " + i);
            commisionListBean.setConsume_date("2016-11-15 15:32:56");
            commisionListBean.setCommission_arrive("3300");
            int money = ((int) (Math.random() * 100) + 1) * 10000;
            int count = ((int) (Math.random() * 4) + 2);
            for (int j = 0; j < count; j++) {
                CommisionStagesItemBean commisionStagesItemBean = new CommisionStagesItemBean();
                commisionStagesItemBean.setStatus(j + "");
                commisionStagesItemBean.setTime("0000-00-00");
                //commisionStagesItemBean.setName(dateArray[j]);
                if (j == 0) {
                    commisionStagesItemBean.setMoney(money + "");
                } else {
                    commisionStagesItemBean.setMoney((money / (count - 1)) + "");
                }
                commisionListBean.getCommision_stages().add(commisionStagesItemBean);
            }
            mCommisionBean.getData().getCommision_list().add(commisionListBean);
        }
        return mCommisionBean;
    }
}
