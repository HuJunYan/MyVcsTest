package com.tianshen.cash.manager;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;

public class DBManager {

    private static DBManager mDBManager;
    private DataBaseConfig mConfig;

    private DBManager(Context context) {
        mConfig = new DataBaseConfig(context);
        mConfig.debugged = true; // open the log
        mConfig.dbVersion = 1; // set database version
        mConfig.onUpdateListener = null; // set database update listener

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
        return LiteOrm.newSingleInstance(mConfig);
    }

}
