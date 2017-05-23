package com.tianshen.cash.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.liulishuo.filedownloader.FileDownloader;
import com.meituan.android.walle.WalleChannelReader;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tianshen.cash.adapter.AndroidLogAdapter;
import com.tianshen.cash.constant.NetConstantValue;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;

import cn.fraudmetrix.sdk.FMAgent;
import cn.jpush.android.api.JPushInterface;

@DefaultLifeCycle(application = "com.tianshen.cash.base.MyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class MyApplicationLike extends DefaultApplicationLike {

    private static MyApplicationLike mMyApplicationLike;
    private static Application mApplication;
    private volatile ArrayList<Activity> mTempActivity = new ArrayList<>();

    public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        MultiDex.install(base);
        TinkerInstaller.install(this);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        clearTempActivityInBackStack();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplicationLike = this;
        mApplication = getApplication();
        SDKInitializer.initialize(mApplication);
        FMAgent.init(mApplication, NetConstantValue.checkIsReleaseService());
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(mApplication); // 初始化 JPush
        initLogger();
        initUMeng();
        FileDownloader.init(mApplication);
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
        String channel = WalleChannelReader.getChannel(mApplication);
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(mApplication, appKey, channel);
        MobclickAgent.startWithConfigure(config);
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

    public static MyApplicationLike getMyApplicationLike() {
        return mMyApplicationLike;
    }

    public ArrayList<Activity> getAllActivities() {
        return mTempActivity;
    }
}
