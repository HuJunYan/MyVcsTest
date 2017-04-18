package com.tianshen.cash.utils;

import android.content.Context;
import android.text.TextUtils;

import com.litesuits.orm.LiteOrm;
import com.tianshen.cash.manager.DBManager;
import com.tianshen.cash.model.User;

import java.util.ArrayList;

public class TianShenUserUtil {

    /**
     * 判断用户是否已经登录
     */
    public static boolean isLogin(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) { //当前没有用户登录
            return false;
        } else {
            User u = user.get(0);
            String token = u.getToken();
            if (TextUtils.isEmpty(token)) {
                return false;
            }
            return true;
        }
    }

    /**
     * 得到当前登录用户的对象
     */
    public static synchronized User getUser(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) {
            return null;
        }
        return user.get(0);
    }

    /**
     * 保存登录的用户
     */
    public static void saveUser(Context context, User user) {
        if (user == null) {
            return;
        }
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        liteOrm.save(user);
    }

    /**
     * 得到用户登录的Token
     */
    public static String getUserToken(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getToken();
        }
        return "";
    }

    /**
     * 得到当前登录用户的id
     */
    public static String getUserId(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getCustomer_id() + "";
        }
        return "";
    }

    /**
     * 得到当前登录用户极光推送的ID
     */
    public static String getUserJPushId(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getJpush_id();
        }
        return "";
    }

    /**
     * 得到当前登录的手机号
     */
    public static String getUserPhoneNum(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getPhone();
        }
        return "";
    }

    /**
     * 得到当前用户借款的金额
     */
    public static String getUserConsumeAmount(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getConsume_amount();
        }
        return "";
    }

    /**
     * 判断当前用户申请产品的类型（自营产品或者第三方产品(掌众)）
     * 自营产品返回true --第三方产品返回false
     */
    public static boolean isPayWayBySelf(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        boolean mIsLogin = isLogin(context);
        if (!mIsLogin) {
            return true;
        }
        String is_payway = user.get(0).getIs_payway();
        if (TextUtils.isEmpty(is_payway)) {
            return true;
        }
        if ("0".equals(is_payway)) { //"0:自营业务，1:掌众"
            return true;
        } else if ("1".equals(is_payway)) {
            return false;
        }
        return true;
    }


    /**
     * 得到当前用户借款选择产品的ID(如果是自己的产品就返回selWithdrawals里面的id,如果第三方产品返回0)
     */
    public static String getUserRepayId(Context context) {
        String repayId = "0";
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        boolean mIsLogin = isLogin(context);
        if (!mIsLogin) {
            return repayId;
        }
        boolean isPayWayBySelf = isPayWayBySelf(context);
        if (isPayWayBySelf) {
            repayId = user.get(0).getRepay_id();
        } else {
            repayId = "0";
        }
        return repayId;
    }

    /**
     * 判断用户是否点过主页面的确认借款按钮
     */
    public static boolean isClickedHomeGetMoneyButton(Context context) {
        boolean mIsLogin = isLogin(context);
        if (!mIsLogin) {
            return false;
        }
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        return user.get(0).isClickedHomeGetMoneyButton();
    }

    /**
     * 判断用户是否点过主页面的还款按钮
     */
    public static boolean isClickedHomeRePayMoneyButton(Context context) {
        boolean mIsLogin = isLogin(context);
        if (!mIsLogin) {
            return false;
        }
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        return user.get(0).isClickedHomeRePayMoneyButton();
    }

    /**
     * 得到服务电话
     */
    public static String getServiceTelephone(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getService_telephone();
        }
        return "";
    }

    /**
     * 得到微信号
     */
    public static String getWeiXin(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        boolean mIsLogin = isLogin(context);
        if (mIsLogin) {
            ArrayList<User> user = liteOrm.query(User.class);
            return user.get(0).getWechat_id();
        }
        return "";
    }

    /**
     * 清除之前借款存储的一些信息
     */
    public static void clearMoneyStatus(Context context) {
        boolean mIsLogin = isLogin(context);
        if (!mIsLogin) {
            return;
        }
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> users = liteOrm.query(User.class);
        User user = users.get(0);
        user.setConsume_amount("0");
        user.setIs_payway("0");
        user.setRepay_id("0");
        user.setClickedHomeGetMoneyButton(false);
        user.setClickedHomeRePayMoneyButton(false);
        liteOrm.save(user);
    }

}
