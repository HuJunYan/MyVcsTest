package com.maibai.cash.net.api;

import android.content.Context;
import android.view.View;

import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.WeChatOrder;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.SignUtils;
import com.maibai.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/5.
 */
public class GetWeChatOrder extends NetBase {
    private Context mContext;
    private JSONObject mJSONObject;
    private String mUrl;

    public GetWeChatOrder(Context context) {
        super(context);
        this.mContext = context;
        mUrl = NetConstantValue.getWeChatOrderURl();
    }

//    public void getWeChatOrder(JSONObject jsonObject, final BaseNetCallBack<WeChatOrder> mWechatOrder) {
//        try {
//            mJSONObject = SignUtils.signJsonContainTwoLevelList(jsonObject, "consume_data", "installment_history");
//            if (mJSONObject == null) {
//                return;
//            }
//            getDataFromServerByPost(mUrl, mJSONObject, null, true, new CallBack() {
//                @Override
//                public void onSuccess(String result, String url) {
//                    successHandle(result, url, mWechatOrder);
//                }
//
//                @Override
//                public void onFailure(String result, int errorType, int errorCode) {
//                    failureHandle(result, errorType, errorCode, mWechatOrder);
//                }
//            });
//        } catch (Exception e) {
//            MobclickAgent.reportError(mContext, LogUtil.getException(e));
//            e.printStackTrace();
//        }
//    }

    /**
     *
     * @param jsonObject
     * @param view
     * @param isShowDialog
     * @param type 0:首付， 1：消费次月还款， 2：消费分期还款， 5：现金贷分期还款
     * @param mWechatOrder
     */
    public void getWeChatOrder(JSONObject jsonObject, View view, boolean isShowDialog, int type, final BaseNetCallBack<WeChatOrder> mWechatOrder) {
        try {
            if (jsonObject == null) {
                return;
            }
            switch (type) {
                case 0:
                    mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
                    break;
                case 1:
                    mJSONObject = SignUtils.signJsonContainList(jsonObject, "consume_data");
                    break;
                case 2:
                case 5:
                    mJSONObject = SignUtils.signJsonContainTwoLevelList(jsonObject, "consume_data", "installment_history");
                    break;
            }
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    successHandle(result, url, mWechatOrder);
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    failureHandle(result, errorType, errorCode, mWechatOrder);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void successHandle(String result, String url, BaseNetCallBack<WeChatOrder> mWechatOrder) {
//        if (isRelease) {
        try {
            WeChatOrder mBillListBean = (WeChatOrder) GsonUtil.json2bean(result, WeChatOrder.class);
            mWechatOrder.onSuccess(mBillListBean);
       /* } else {
            mBillListCallBack.onSuccess(test());
        }*/
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }

    private void failureHandle(String result, int errorType, int errorCode, BaseNetCallBack<WeChatOrder> mWechatOrder) {
//        if (isRelease) {
        try {
            mWechatOrder.onFailure(result, errorType, errorCode);
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
      /*  } else {
            mBillListCallBack.onSuccess(test());
        }*/
    }
}
