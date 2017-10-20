package com.tianshen.cash.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tianshen.cash.R;
import com.tianshen.cash.base.MyApplicationLike;

/**
 * Created by Administrator on 2017/8/1.
 */

public class StatusBarUtil {
    private StatusBarUtil() {
    }

    /**
     * 设置状态栏颜色为白色 4.4以上
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarWhite(Activity activity) {
        Window window = activity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //生成一个和状态栏大小一样的view
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        params.gravity = Gravity.TOP;
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(activity.getResources().getColor(R.color.global_bg_white));
        statusBarView.setVisibility(View.GONE);
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        decorView.addView(statusBarView);
    }


    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);

        }
        return statusBarHeight;
    }

    /**
     * 设置状态栏为白色 或者渐变色  只有本项目可用
     *
     * @param activity
     * @param isWhite
     */
    public static void setStatusBarWhiteOrGradient(Activity activity, boolean isWhite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            View baseView = activity.findViewById(R.id.base_layout);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isWhite) {
                if (baseView != null) {
                    baseView.setBackgroundColor(Color.WHITE);
                }
            } else {
                if (baseView != null) {
                    baseView.setBackgroundResource(R.drawable.shape_navigation);
                }
            }
            if (MyApplicationLike.isMIUI) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
//            if (isEMUI()) {
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            } else {
//                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            }
            window.setStatusBarColor(Color.TRANSPARENT);
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        //适配miui
        if (MyApplicationLike.isMIUI) {
            RomUtils.setStatusBarDarkMode(isWhite, activity);
        } else if (RomUtils.FlymeSetStatusBarLightMode(activity.getWindow(), true)) { //适配Flyme4.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                setStatusBarWhite(activity);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isWhite) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            }
            //设置6.0以上字体颜色为深色
        }
    }


    public static void setStatusBarBgWhite(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.global_bg_white));
            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        //适配miui
        if (MyApplicationLike.isMIUI) {
            RomUtils.setStatusBarDarkMode(true, activity);
        } else if (RomUtils.FlymeSetStatusBarLightMode(activity.getWindow(), true)) { //适配Flyme4.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                StatusBarUtil.setStatusBarWhite(activity);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //设置6.0以上字体颜色为深色
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}
