package com.tianshen.cash.utils;

import android.content.Context;
import android.text.TextUtils;

public class TianShenUserUtil {

    private static String TOKEN_KEY = "token";
    private static String CUSTOMER_ID_KEY = "customer_id";
    private static String NAME_KEY = "name";
    private static String AGE_KEY = "age";
    private static String PHONE_KEY = "phone";
    private static String SEX_KEY = "sex";
    private static String JPUSH_ID_KEY = "jpush_id";
    private static String FACE_THRESHOLD_KEY = "face_threshold"; //face++的阈值
    private static String ID_NUM_KEY = "id_num"; //身份证号
    private static String REPAY_ID_KEY = "repay_id";
    private static String CONSUME_AMOUNT_KEY = "consume_amount";
    private static String IS_PAYWAY_KEY = "is_payway";
    private static String LOCATION_KEY = "location";
    private static String PROVINCE_KEY = "province";
    private static String CITY_KEY = "city";
    private static String COUNTRY_KEY = "country";
    private static String ADDRESS_KEY = "address";
    private static String SERVICE_TELEPHONE_KEY = "service_telephone";
    private static String WECHAT_ID_KEY = "wechat_id";
    private static String IS_SHOW_SERVICE_TELEPHONE_KEY = "is_show_service_telephone";
    private static String IS_CLICKED_HOME_GET_MONEY_BUTTON_KEY = "is_clicked_home_get_money_button";
    private static String IS_CLICKED_HOME_REPAY_MONEY_BUTTON_KEY = "is_clicked_home_repay_money_button";

    private static String SHOW_ACTIVITY_TIME = "show_activity_time";
    private static String SHOW_ACTIVITY_COUNT = "show_activity_count";
    private static String RISK_PRE_SMS_SMSID_key = "risk_pre_smsId_key"; //向上绑卡字段
    private static String RISK_PRE_SMS_USERNo_key = "risk_pre_userno_key";//向上绑卡字段

    /**
     * 判断用户是否已经登录
     */
    public static boolean isLogin(Context context) {
        String customer_id = SharedPreferencesUtil.getInstance(context).getString(CUSTOMER_ID_KEY, "");
        if (TextUtils.isEmpty(customer_id)) {
            return false;
        }
        return true;
    }

    /**
     * 保存用户的Token
     */
    public static void saveUserToken(Context context, String token) {
        SharedPreferencesUtil.getInstance(context).putString(TOKEN_KEY, token);
    }

    /**
     * 得到用户登录的Token
     */
    public static String getUserToken(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(TOKEN_KEY, "");
    }

    /**
     * 保存用户的Name
     */
    public static void saveUserName(Context context, String name) {
        SharedPreferencesUtil.getInstance(context).putString(NAME_KEY, name);
    }

    /**
     * 得到用户登录的Name
     */
    public static String getUserName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(NAME_KEY, "");
    }

    /**
     * 保存用户的ID
     */
    public static void saveUserId(Context context, String id) {
        SharedPreferencesUtil.getInstance(context).putString(CUSTOMER_ID_KEY, id);
    }

    /**
     * 得到当前登录用户的id
     */
    public static String getUserId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(CUSTOMER_ID_KEY, "");
    }

    /**
     * 保存登录用户极光推送的ID
     */
    public static void saveUserJPushId(Context context, String jpush_id) {
        SharedPreferencesUtil.getInstance(context).putString(JPUSH_ID_KEY, jpush_id);
    }

    /**
     * 得到当前登录用户极光推送的ID
     */
    public static String getUserJPushId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(JPUSH_ID_KEY, "");
    }

    /**
     * 保存登录用户手机号
     */
    public static void saveUserPhoneNum(Context context, String phone) {
        SharedPreferencesUtil.getInstance(context).putString(PHONE_KEY, phone);
    }

    /**
     * 得到当前登录的手机号
     */
    public static String getUserPhoneNum(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(PHONE_KEY, "");
    }

    /**
     * 保存登录用户的身份证号
     */
    public static void saveUserIDNum(Context context, String id_num) {
        SharedPreferencesUtil.getInstance(context).putString(ID_NUM_KEY, id_num);
    }

    /**
     * 得到当前登录用户身份证号
     */
    public static String getUserIDNum(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(ID_NUM_KEY, "");
    }

    /**
     * 保存face++的阈值
     */
    public static void saveFaceThreshold(Context context, String face_threshold) {
        SharedPreferencesUtil.getInstance(context).putString(FACE_THRESHOLD_KEY, face_threshold);
    }

    /**
     * 得到face++的阈值
     */
    public static String getFaceThreshold(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(FACE_THRESHOLD_KEY, "");
    }

    /**
     * 保存登录用户借款的金额
     */
    public static void saveUserConsumeAmount(Context context, String consume_amount) {
        SharedPreferencesUtil.getInstance(context).putString(CONSUME_AMOUNT_KEY, consume_amount);
    }

    /**
     * 得到当前用户借款的金额
     */
    public static String getUserConsumeAmount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(CONSUME_AMOUNT_KEY, "");
    }

    /**
     * 保存当前用户借款选择产品的ID返回selWithdrawals里面的id
     */
    public static void saveUserRepayId(Context context, String repay_id_key) {
        SharedPreferencesUtil.getInstance(context).putString(REPAY_ID_KEY, repay_id_key);
    }

    /**
     * 得到当前用户借款选择产品的ID返回selWithdrawals里面的id
     */
    public static String getUserRepayId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(REPAY_ID_KEY, "");
    }

    /**
     * 保存服务电话
     */
    public static void saveServiceTelephone(Context context, String service_telephone) {
        SharedPreferencesUtil.getInstance(context).putString(SERVICE_TELEPHONE_KEY, service_telephone);
    }

    /**
     * 得到服务电话
     */
    public static String getServiceTelephone(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(SERVICE_TELEPHONE_KEY, "");
    }

    /**
     * 保存微信号
     */
    public static void saveWeiXin(Context context, String wechat_id) {
        SharedPreferencesUtil.getInstance(context).putString(WECHAT_ID_KEY, wechat_id);
    }

    /**
     * 得到微信号
     */
    public static String getWeiXin(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(WECHAT_ID_KEY, "");
    }

    public static void saveLocation(Context context, String location) {
        SharedPreferencesUtil.getInstance(context).putString(LOCATION_KEY, location);
    }

    public static String getLocation(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(LOCATION_KEY, "");
    }

    public static void saveProvince(Context context, String province) {
        SharedPreferencesUtil.getInstance(context).putString(PROVINCE_KEY, province);
    }

    public static String getProvince(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(PROVINCE_KEY, "");
    }

    public static void saveCity(Context context, String city) {
        SharedPreferencesUtil.getInstance(context).putString(CITY_KEY, city);
    }

    public static String getCity(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(CITY_KEY, "");
    }

    public static void saveCountry(Context context, String country) {
        SharedPreferencesUtil.getInstance(context).putString(COUNTRY_KEY, country);
    }

    public static String getCountry(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(COUNTRY_KEY, "");
    }

    public static void saveAddress(Context context, String address) {
        SharedPreferencesUtil.getInstance(context).putString(ADDRESS_KEY, address);
    }

    public static String getAddress(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(ADDRESS_KEY, "");
    }

    /**
     * 判断当前用户申请产品的类型（自营产品或者第三方产品(掌众)）
     * 自营产品返回true --第三方产品返回false
     */
    public static void saveIsPayWayBySelf(Context context, String is_payway) {
        SharedPreferencesUtil.getInstance(context).putString(IS_PAYWAY_KEY, is_payway);
    }

    /**
     * 判断当前用户申请产品的类型（自营产品或者第三方产品(掌众)）
     * 自营产品返回true --第三方产品返回false
     */
    public static boolean isPayWayBySelf(Context context) {
        String is_payway = SharedPreferencesUtil.getInstance(context).getString(IS_PAYWAY_KEY, "");
        return "0".equals(is_payway);
    }

    public static void saveIsClickedHomeGetMoneyButton(Context context, boolean is_clicked_home_get_money_button) {
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_CLICKED_HOME_GET_MONEY_BUTTON_KEY, is_clicked_home_get_money_button);
    }

    /**
     * 判断用户是否点过主页面的确认借款按钮
     */
    public static boolean isClickedHomeGetMoneyButton(Context context) {
        return SharedPreferencesUtil.getInstance(context).getBoolean(IS_CLICKED_HOME_GET_MONEY_BUTTON_KEY);
    }

    /**
     * 判断用户是否点过主页面的还款按钮
     */
    public static boolean isClickedHomeRePayMoneyButton(Context context) {
        return SharedPreferencesUtil.getInstance(context).getBoolean(IS_CLICKED_HOME_REPAY_MONEY_BUTTON_KEY);
    }


    public static boolean isShowServiceTelephone(Context context) {
        String is_show_service_telephone = SharedPreferencesUtil.getInstance(context).getString(IS_SHOW_SERVICE_TELEPHONE_KEY);
        return "1".equals(is_show_service_telephone);
    }


    public static void saveIsShowServiceTelephone(Context context, String is_show_service_telephone) {
        SharedPreferencesUtil.getInstance(context).putString(IS_SHOW_SERVICE_TELEPHONE_KEY, is_show_service_telephone);
    }

    /**
     * 清除之前借款存储的一些信息
     */
    public static void clearMoneyStatus(Context context) {
        SharedPreferencesUtil.getInstance(context).putString(CONSUME_AMOUNT_KEY, "0");
        SharedPreferencesUtil.getInstance(context).putString(IS_PAYWAY_KEY, "0");
        SharedPreferencesUtil.getInstance(context).putString(REPAY_ID_KEY, "0");
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_CLICKED_HOME_GET_MONEY_BUTTON_KEY, false);
        SharedPreferencesUtil.getInstance(context).putBoolean(IS_CLICKED_HOME_REPAY_MONEY_BUTTON_KEY, false);
    }

    /**
     * 保存上次活动显示的时间
     */
    public static void saveShowActivityTime(Context context, long show_activity_time) {
        SharedPreferencesUtil.getInstance(context).putLong(SHOW_ACTIVITY_TIME, show_activity_time);
    }

    /**
     * 得到上次活动显示的时间
     */
    public static long getShowActivityTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getLong(SHOW_ACTIVITY_TIME);
    }

    /**
     * 得到上次活动显示的次数
     */
    public static int getShowActivityCount(Context context) {
        return SharedPreferencesUtil.getInstance(context).getInt(SHOW_ACTIVITY_COUNT);
    }

    /**
     * 保存上次活动显示的次数
     */
    public static void saveShowActivityCount(Context context, int show_activity_count) {
        SharedPreferencesUtil.getInstance(context).putInt(SHOW_ACTIVITY_COUNT, show_activity_count);
    }

    /**
     * 得到向上smsId
     *
     * @param context
     */
    public static String getSmsId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(RISK_PRE_SMS_SMSID_key);
    }

    /**
     * 得到向上UserNo
     *
     * @param context
     */
    public static String getUserNo(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString(RISK_PRE_SMS_USERNo_key);
    }

    /**
     * 保存smsId
     *
     * @param context
     * @param smsId
     */
    public static void saveSmsId(Context context, String smsId) {
        SharedPreferencesUtil.getInstance(context).putString(RISK_PRE_SMS_SMSID_key, smsId);
    }

    /**
     * 保存userNo
     *
     * @param context
     * @param userNo
     */
    public static void saveUserNo(Context context, String userNo) {
        SharedPreferencesUtil.getInstance(context).putString(RISK_PRE_SMS_USERNo_key, userNo);
    }

    /**
     * 清除所有的用户信息
     */
    public static void clearUser(Context context) {
        SharedPreferencesUtil.getInstance(context).clearSp();
    }


}
