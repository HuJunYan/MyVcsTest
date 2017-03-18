package com.maibai.cash.net.base;

import android.content.Context;

import com.maibai.cash.R;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.utils.ToastUtil;

public class DealWithErrorUtils {

	public static void dealWithErrorCode(Context context, String result) {
		ResponseBean mResponseBean = null;
		String errorMsg = "";
		try {
			mResponseBean = GsonUtil.json2bean(result, ResponseBean.class);
			errorMsg = mResponseBean.getMsg();
		} catch (Exception e) {
			errorMsg = context.getResources().getString(R.string.ServiceFaile);
		}
		if (mResponseBean != null) {
			showErrorToast(context, mResponseBean.getCode(), errorMsg);
		}
	}

	private static void showErrorToast(Context context, int err_code, String err_msg) {
		switch (err_code) {
			case 10000:
			case 118: // 无升级
				break;

			case 101: // 下单失败
			case 204: // 坐标信息不正确
			case 211:
			case 501: // 服务器开小车了，请稍后重试
				ToastUtil.showToast(context, "网络不给力：" + err_code);
				break;
			default:
				ToastUtil.showToast(context, err_msg);
				break;
		}
	}
}
