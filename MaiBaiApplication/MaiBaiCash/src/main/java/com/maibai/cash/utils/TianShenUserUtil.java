package com.maibai.cash.utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.maibai.cash.manager.DBManager;
import com.maibai.cash.model.User;

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
            return true;
        }
    }

    /**
     * 得到用户登录的Token
     */
    public static String getUserToken(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) { //当前没有用
            return "";
        } else {
            return user.get(0).getToken();
        }
    }

}
