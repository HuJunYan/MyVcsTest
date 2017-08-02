package com.tianshen.cash.net.base;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;

public class XUtilsManager {
    private static XUtilsManager mXUtilsManager;
    private HttpUtils mHttpUtils;

    private XUtilsManager(Context e) {
        mHttpUtils = new HttpUtils();
    }

    public static XUtilsManager getInstance(Context f) {
        if (null == mXUtilsManager) {
            synchronized (XUtilsManager.class) {
                if (null == mXUtilsManager) {
                    mXUtilsManager = new XUtilsManager(f);
                }
            }
        }
        return mXUtilsManager;
    }

    public HttpUtils getHttpUtils() {
        return mHttpUtils;
    }
}
