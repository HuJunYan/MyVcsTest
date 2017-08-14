package com.tianshen.cash.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.tianshen.cash.base.MyApplicationLike;

public class ToastUtil {
    private static Toast toast = null;

    public static final int SHOW = 0;
    public static final int LONG = 1;

    public static void showToast(Context context, String msg) {
        if (msg == null || "".equals(msg)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApplicationLike.getmApplication(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    public static void showToast(Context context, String msg, int time) {
        if (msg == null || "".equals(msg)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(MyApplicationLike.getmApplication(), msg, time);
        } else {
            toast.setText(msg);
            toast.setDuration(time);
        }
        toast.show();
    }

    public static void showToast(Context context, int resid) {
        if (toast == null) {
            toast = Toast.makeText(MyApplicationLike.getmApplication(), resid, Toast.LENGTH_LONG);
        } else {
            toast.setText(MyApplicationLike.getmApplication().getResources().getText(resid));
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.show();
    }

    /**
     * 取消弹出toast
     */
    public static void cancleToast(Context context) {
        if (context != null) {
            if (toast != null) {
                toast.cancel();
            }
        }
    }
}
