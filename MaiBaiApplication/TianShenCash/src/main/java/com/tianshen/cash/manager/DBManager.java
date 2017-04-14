package com.tianshen.cash.manager;

import android.content.Context;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.tianshen.cash.utils.LogUtil;

import java.io.File;

public class DBManager {

    private static DBManager mDBManager;
    private DataBaseConfig mConfig;
    private LiteOrm mLiteOrm;


    private DBManager(Context context) {
        File cacheDir = context.getCacheDir();
        String daPath = cacheDir.getAbsolutePath() + "/db/tianshen.db";
        mConfig = new DataBaseConfig(context, daPath);
        mConfig.debugged = true; // open the log
        mConfig.dbVersion = 1; // set database version
        mConfig.onUpdateListener = null; // set database update listener
        mLiteOrm = LiteOrm.newSingleInstance(mConfig);
    }

    public static DBManager getInstance(Context context) {
        if (mDBManager == null) {
            synchronized (DBManager.class) {
                if (mDBManager == null) {
                    mDBManager = new DBManager(context);
                }
            }
        }
        return mDBManager;
    }

    public LiteOrm getLiteOrm() {
        return mLiteOrm;
    }

}
