package com.tianshen.cash.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.liulishuo.filedownloader.FileDownloader;
import com.meituan.android.walle.WalleChannelReader;
import com.moxie.client.manager.MoxieSDK;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import com.tianshen.cash.adapter.AndroidLogAdapter;
import com.tianshen.cash.constant.NetConstantValue;

import com.tianshen.cash.utils.RomUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import cn.fraudmetrix.sdk.FMAgent;
import cn.jpush.android.api.JPushInterface;

public class MyApplicationLike extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    private static MyApplicationLike sApplication;
    private volatile ArrayList<Activity> mTempActivity = new ArrayList<>();
    public static boolean isMIUI;
    public static boolean isFlyMe;
    public static boolean isOnResume; //判断应用是否在前台
    private int resumeCount;

    @Override
    public void onTerminate() {
        super.onTerminate();
        clearTempActivityInBackStack();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        SDKInitializer.initialize(sApplication);
        FMAgent.init(sApplication, NetConstantValue.checkIsReleaseService());
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(sApplication); // 初始化 JPush
        initLogger();
        initUMeng();
        FileDownloader.init(sApplication);
        //初始化bugly
        CrashReport.initCrashReport(sApplication, "64c5b81f2f", false);
        isMIUI = RomUtils.isMIUI();
        isFlyMe = RomUtils.FlymeSetStatusBarLightMode();
        //魔蝎淘宝认证
        MoxieSDK.init(this);
        registerActivityLifecycleCallbacks(this);
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

    private void initUMeng() {
        String appKey = "58d8d84f04e2055475001ba2";
        String channel = WalleChannelReader.getChannel(sApplication);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(sApplication, appKey, channel);
        MobclickAgent.startWithConfigure(config);
    }

    public synchronized void addTempActivityInBackStack(Activity activity) {
        mTempActivity.add(activity);
    }

    public synchronized void removeTempActivityInBackStack(Activity activity) {
        mTempActivity.remove(activity);
    }

    public static Application getsApplication() {
        return sApplication;
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

    public static MyApplicationLike getMyApplicationLike() {
        return sApplication;
    }

    public ArrayList<Activity> getAllActivities() {
        return mTempActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        isOnResume = true;
        resumeCount++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        resumeCount--;
        if (resumeCount == 0) {
            isOnResume = false;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
