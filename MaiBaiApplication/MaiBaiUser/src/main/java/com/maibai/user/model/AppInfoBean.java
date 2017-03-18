package com.maibai.user.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/8/12.
 * 手机内已安装的所有app信息
 */
public class AppInfoBean {
    private  String appName;//app名称
    private String pakegeName;//app包名
    private String versionCode;//app版本号
    private String versionName;//app版本名
    private Drawable icon;//app图标

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPakegeName() {
        return pakegeName;
    }

    public void setPakegeName(String pakegeName) {
        this.pakegeName = pakegeName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
