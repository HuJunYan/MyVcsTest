package com.tianshen.cash.base;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.baidu.mapapi.SDKInitializer;
import com.tianshen.cash.constant.NetConstantValue;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tianshen.cash.adapter.AndroidLogAdapter;
import cn.fraudmetrix.sdk.FMAgent;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {

    private volatile ArrayList<Activity> mTempActivity = new ArrayList<Activity>();

    public static Typeface typeFace;
    private Context mResumeContext;

    public Context getResumeContext() {
        return mResumeContext;
    }

    public void setResumeContext(Context mResumeContext) {
        this.mResumeContext = mResumeContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        FMAgent.init(getApplication(), NetConstantValue.checkIsReleaseService());
//		if (Utils.isApplicationRepeat(this)) {
//			return;
//		}
		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
//		LogUtil.autoSetDebugOrReleaseMode(MyApplication.this);
//		GsonUtil.init(MyApplication.this);

        initLogger();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        clearTempActivityInBackStack();
    }

    public synchronized void addTempActivityInBackStack(Activity activity) {
        mTempActivity.add(activity);
    }

    public synchronized void clearTempActivityInBackStack() {
        Iterator<Activity> iterator = mTempActivity.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
        }
        mTempActivity.clear();
    }

    public synchronized void clearTempActivityInBackStack(Class<?> className) {
        if (className != null) {
            Iterator<Activity> iterator = mTempActivity.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (!activity.getClass().getName().equals(className.getName())) {

                    activity.finish();
                }
            }
            Activity saveActivity = null;
            for (Activity activity : mTempActivity) {
                if (activity.getClass().getName().equals(className.getName())) {
                    saveActivity = activity;
                }
            }
            mTempActivity.clear();
            addTempActivityInBackStack(saveActivity);
        }
    }

    public synchronized void clearTempActivityInBackStack2() {
        for (int x = 0; x < mTempActivity.size() - 1; x++) {
            Activity activity = mTempActivity.get(x);
            activity.finish();
        }
    }

    public MyApplication getApplication() {
        // TODO Auto-generated method stub
        return this;
    }

    public ArrayList<Activity> getAllActivities() {
        return mTempActivity;
    }

    private void initLogger() {
        Logger
                .init("abc")                 // default PRETTYLOGGER or use just init()
                .methodCount(0)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                .methodOffset(0)                // default 0
                .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }
}
