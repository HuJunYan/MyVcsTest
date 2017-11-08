package com.tianshen.cash.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.UserAuthCenterBean;
import com.tianshen.cash.net.api.GetUserAuthCenter;
import com.tianshen.cash.net.base.BaseNetCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 完成认证&提升额度的dialog工具类
 */

public class CashAmountDialogUtils {

    public static void show(Context context) {
        String userId = TianShenUserUtil.getUserId(context);
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, userId);
            GetUserAuthCenter getUserAuthCenter = new GetUserAuthCenter(context);
            getUserAuthCenter.userAuthCenter(jsonObject, null, true, new BaseNetCallBack<UserAuthCenterBean>() {
                @Override
                public void onSuccess(UserAuthCenterBean paramT) {
                    String taobaoPass = paramT.getData().getTaobao_pass();
                    LogUtil.d("abc", "taobaoPass--->" + taobaoPass);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
