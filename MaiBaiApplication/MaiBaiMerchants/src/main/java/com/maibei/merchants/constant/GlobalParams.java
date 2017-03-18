package com.maibei.merchants.constant;

public class GlobalParams {

	public static final String IS_CAMERA_INITED = "is_camera_inited"; // 相机是否初始化了
	public static String getSlot() {
		return "secret@9maibei.com";
	}

	public static final String JPUSH_ID_KEY="jpush_id_key";//jpushid的key
	public static final String LOG_FILE = "waterDriver/log.txt";//log的路径
	public static final String withDrawalResultFromWithDrawal="withdrawls_result_from_withdrawal";
	public static final String withDrawalResultFromComm="withdrawals_result_from_comm";//佣金提现结果
	public static final String DB_EDITION="1.0";//数据库版本号
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_NEED_CROP = 100; // 照相的tag值
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_WITHHOUT_CROP=200;//
	public static final int GET_PICTURE_FROM_XIANGCE_CODE_NEED_CROP = 101; // 从相册取相片的tag值
	public static final int GET_PICTURE_FROM_XIANGCE_CODE_WITHOUT_CROP=201;
	public static final int GET_CUT_PICTURE_CODE = 102; // 取得裁剪后的照片

	public static final String LOG_STATUS_NEED_UPLOAD="log_status_need_upload";//需要上传log
	public static final String LOG_STATUS_IS_UPLOADING="log_status_is_uploading";//正在上传log
	public static final String LOG_STATUS_IS_UPLOAD_fail="log_status_upload_fail";//上传失败
	public static final String LOG_STATUS_NOT_NEED_UPLOAD="log_status_not_need_upload";//不需要上传log

	public static final int BUSINESS_LICENCE = 100;         // 营业执照
	public static final int OWNER_ID_CARD = 101;         // 业主身份证
	public static final int SHOP_ID_PRCTURE = 102;		//门店图片
	public static final int INDOOR_PICTURE1=103;//店内环境一
	public static final int INDOOR_PICTURE2=104;//店内环境二
	public static final int CASHIER_PICTURE=105;//收银台
	public static final int COUNTER_PICTURE=106;//商品柜台
	public static final int PERIPHERY_PICTURE=107;//门店周边

	public static final int UPDATE_LOG_STATUS=3;//上传log
	public static final int REQUEST_EXTERNAL_STORAGE = 1;
	public static final int CAMERA_JAVA_REQUEST_CODE = 2;
	public static final int CONTACTS_CODE = 3;
	public static final int LOCATION_CODE=4;

	public static final String DB_NAME="maibeimerchant.db";
	public static final String TABLE_NAME="province";
	public static final String DB_PROVINCE_ID="province_id";
	public static final String DB_PROVINCE_NAME="province_name";

	public static final int RESULT_SELECT_ADDRESS_OK=1;

	public static final String PROVINCE_KEY="province_key";
	public static final String CITY_KEY="city_key";
	public static final String CONTRY_KEY="contry_key";
}
