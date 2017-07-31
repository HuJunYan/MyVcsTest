package com.tianshen.cash.activity

import android.app.Activity
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

import com.tianshen.cash.R
import com.tianshen.cash.base.BaseActivity
import com.tianshen.cash.utils.RomUtils

class RegisteActivity2 : BaseActivity() {
    override fun setContentView(): Int = R.layout.activity_registe2

    override fun findViews() {
        initRegisterOptions();
    }

    private fun initRegisterOptions() {
        // 更改状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(android.R.color.white)

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        if (RomUtils.isMIUI()) {
            setStatusBarDarkMode(true, this)
        }
    }

    override fun setListensers() {
    }

    //设置小米状态栏为黑色模式
    fun setStatusBarDarkMode(darkmode: Boolean, activity: Activity) {
        val clazz = activity.window.javaClass
        try {
            var darkModeFlag = 0
            val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.invoke(activity.window, if (darkmode) darkModeFlag else 0, darkModeFlag)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
