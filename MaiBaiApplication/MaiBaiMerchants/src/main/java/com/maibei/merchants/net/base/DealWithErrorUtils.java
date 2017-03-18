package com.maibei.merchants.net.base;

import android.content.Context;

import com.maibei.merchants.R;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.utils.ToastUtil;

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
			case 118:
				break;
			default:
				ToastUtil.showToast(context, err_msg);
				break;
		}
	}
}
