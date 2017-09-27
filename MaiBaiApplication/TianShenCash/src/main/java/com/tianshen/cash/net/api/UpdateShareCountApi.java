package com.tianshen.cash.net.api;

import android.content.Context;
import android.view.View;

import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.SignUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by wang on 2017/9/27.
 */

public class UpdateShareCountApi extends NetBase {
    private String mUrl;
    private Context mContext;
    private JSONObject mJSONObject;

    public UpdateShareCountApi(Context context) {
        super(context);
        mContext = context;
        mUrl = NetConstantValue.getUpdateShareCountURL();
    }

    public void updateShareCount(JSONObject jsonObject, View view, boolean isShowDialog) {
        try {
            mJSONObject = SignUtils.signJsonNotContainList(jsonObject);
            if (mJSONObject == null) {
                return;
            }
            getDataFromServerByPost(mUrl, mJSONObject, view, isShowDialog, new CallBack() {
                @Override
                public void onSuccess(String result, String url) {
                    //do nothing
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    //do nothing
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
