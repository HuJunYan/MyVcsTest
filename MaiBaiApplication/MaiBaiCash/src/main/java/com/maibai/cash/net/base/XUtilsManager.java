package com.maibai.cash.net.base;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.maibai.cash.utils.Utils;

public class XUtilsManager {
	private static XUtilsManager mXUtilsManager;
	private BitmapUtils mBitmapUtils;
	private HttpUtils mHttpUtils;

	private XUtilsManager(Context e) {
		mHttpUtils = new HttpUtils();
		mBitmapUtils = BitmapUtils.create(e);
		mBitmapUtils.configDiskCachePath(Utils.getCachePath(e));
	}

	public static XUtilsManager getInstance(Context f) {
		if (null == mXUtilsManager) {
			synchronized (XUtilsManager.class) {
				if (null == mXUtilsManager) {
					mXUtilsManager = new XUtilsManager(f);
				}
			}
		}
		return mXUtilsManager;
	}

	public BitmapUtils getBitmapUtils() {
		return mBitmapUtils;
	}

	public HttpUtils getHttpUtils() {
		return mHttpUtils;
	}
}
