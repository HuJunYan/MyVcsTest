package com.tianshen.cash.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast = null;

	public static final int SHOW = 0;
	public static final int LONG = 1;

	public static void showToast(Context context, String msg) {
		if (msg == null || "".equals(msg)) {
			return;
		}
		LogUtil.d("thread","main thread name = " + Looper.getMainLooper().getThread().getName());
		LogUtil.d("thread","thread name = "+Thread.currentThread().getName());
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
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
        LogUtil.d("thread","main thread name = " + Looper.getMainLooper().getThread().getName());
        LogUtil.d("thread","thread name = "+Thread.currentThread().getName());
		if (toast == null) {
			toast = Toast.makeText(context, msg, time);
		} else {
			toast.setText(msg);
			toast.setDuration(time);
		}
		toast.show();
	}
	public static void showToast(Context context, int resid) {
        LogUtil.d("thread","main thread name = " + Looper.getMainLooper().getThread().getName());
        LogUtil.d("thread","thread name = "+Thread.currentThread().getName());
		if (toast == null) {
			toast = Toast.makeText(context, resid, Toast.LENGTH_LONG);
		} else {
			toast.setText(context.getResources().getText(resid));
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}
}
