package com.tianshen.cash.utils;

import android.os.Environment;

public class Config {

	/*程序SD卡存储根目录*/
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory() + "/";

	/*图片缓存目录*/
	public static final String IMAGE_CACHE_DIR = SD_PATH + "tianshen/image/";

	/*补丁包缓存目录*/
	public static final String TINKER_CACHE_DIR = SD_PATH + "tianshen/tinker/";
}
