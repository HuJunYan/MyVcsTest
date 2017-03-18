package com.maibai.user.net.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.utils.MemoryAddressUtils;
import com.maibai.user.utils.Utils;

public class GsonUtil {
	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	public static <T> T json2bean(String result, Class<T> clazz) {
		Gson gson = new Gson(mContext);
		T t = gson.fromJson(result, clazz);
		return t;
	}

	public static String bean2json(Object object) {
		String s = "";
		try {
			Gson gson = new Gson(mContext);
			s = gson.toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

//	public static JSONObject getJSONObjectForUSer(Context context, JSONObject request) {
//		JSONObject json = new JSONObject();
//		String mUid = UserUtil.getUserUid(context);
//		String mToken = UserUtil.getUserToken(context);
//		try {
//			json.put("uid", mUid);
//			json.put("token", mToken);
////			json.put("ver", GlobalParams.VER);
//			String time = Long.valueOf(System.currentTimeMillis() / 1000).toString();
//			json.put("time", time);
//			json.put("sign", getMd5AndReverse(time));
////			json.put("device", GlobalParams.DEVICE);
//			json.put("request", request);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return json;
//	}

	/**
	 * 判断操作是否执行成功
	 *
	 * 
	 * @param result
	 *            操作以后服务器下发的json数据
	 * @return 操作是否执行成功，成功为true,否则为false
	 */
	public static boolean isSuccess(String result) {
		ResponseBean mResponseBean = GsonUtil.json2bean(result, ResponseBean.class);
		return (ResponseBean.SUCCESS == mResponseBean.getCode());
	}

	/**
	 * 获取错误码
	 * 
	 * @param result服务器下发的json数据
	 * @return
	 */
	public static int getErrorCode(String result) {
		int code = -2;
		try {
			ResponseBean response = GsonUtil.json2bean(result, ResponseBean.class);
			if (response != null) {
				code = response.getCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	public static void gotoActivity(Context mContext, Class<?> toActivityClass, Bundle bundle) {
		Intent intent = new Intent(mContext, toActivityClass);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		mContext.startActivity(intent);
		((Activity) mContext).overridePendingTransition(MemoryAddressUtils.push_right_in(), MemoryAddressUtils.not_exit_push_left_out());
	}

	/**
	 * 退出到某个Activity
	 */
	protected void backActivity(Context mContext) {
		((Activity) mContext).finish();
		((Activity) mContext).overridePendingTransition(MemoryAddressUtils.not_exit_push_left_in(), MemoryAddressUtils.push_right_out());
	}

	/**
	 * 签名算法 key字符串反转，md5一次，生成的md5 hash字符串在反转一次
	 * 
	 * @param key
	 * @return
	 */
	public static String getMd5AndReverse(String key) {
		return reverse(Utils.md5(reverse(key)));
	}

	/**
	 * 反转字符串
	 * 
	 * @param s
	 * @return
	 */
	private static String reverse(String s) {
		return new StringBuffer(s).reverse().toString();
	}

	/**
	 * 读取读取assets资源下的数据
	 * 
	 * @param context
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String getInfo(Context context, String fileName) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream in = context.getAssets().open(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
}
