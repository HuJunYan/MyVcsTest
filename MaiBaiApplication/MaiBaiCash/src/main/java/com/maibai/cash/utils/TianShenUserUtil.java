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
     * 得到当前登录用户的对象
     */
    public static User getUser(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) { //当前没有用户登录
            return null;
        } else {
            return user.get(0);
        }
    }

    /**
     * 保存登录的用户
     */
    public static void saveUser(Context context, User user) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        liteOrm.save(user);
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

    /**
     * 得到当前登录用户的id
     */
    public static long getUserId(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) { //当前没有用户登录
            return 0;
        } else {
            return user.get(0).getId();
        }
    }

    /**
     * 得到当前登录用户极光推送的ID
     */
    public static String getUserJPushId(Context context) {
        LiteOrm liteOrm = DBManager.getInstance(context.getApplicationContext()).getLiteOrm();
        ArrayList<User> user = liteOrm.query(User.class);
        if (user == null || user.size() == 0) { //当前没有用户登录
            return "";
        } else {
            return user.get(0).getJpush_id();
        }
    }

}
