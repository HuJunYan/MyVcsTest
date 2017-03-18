package com.maibai.cash.utils;

import android.content.Context;
import android.util.Log;

import com.maibai.cash.net.base.UserUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhangchi on 2016/6/21.
 */
public class MainUtil {
//    public static boolean isNewUser(Context mContext) {
//
//        if (!"".equals(UserUtil.getRealName(mContext)) && !"".equals(UserUtil.getMobile(mContext)) && !"".equals(UserUtil.getIdNum(mContext))
//                && "1".equals(UserUtil.getIsSetPayPass(mContext))) {
//            return false;
//        }
//        return true;
//    }
    public static boolean isNewUser(Context mContext) {

        String amountStr = UserUtil.getCustomerAmount(mContext);
        int amountFloat = Integer.parseInt(amountStr);
        if (amountFloat > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static int getCashAmount(Context mContext) {
        String amountStr = UserUtil.getCashAmount(mContext);
        int amount = 0;
        try {
            amount = Integer.parseInt(amountStr);
        }catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        return amount;
    }

    public static boolean isLogin(Context mContext) {
        String id=UserUtil.getId(mContext);
        if("".equals(id)||null==id){
            return false;
        }
        else{
            return true;
        }
    }


    public static boolean isAddBankCard(Context context) {
//        return isNewUser(context)&&
        return false;
    }
}
