package com.maibei.merchants.net.base;

import android.content.Context;
import android.text.TextUtils;

import com.maibei.merchants.model.UserLoginBean;
import com.maibei.merchants.utils.SharedPreferencesUtil;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class UserUtil {
    public static String getMerchantId(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("merchant_id");
    }
    /*
    * 设置log上传的状态
    * */

    public static void setLogStatus(Context context,String status){
        SharedPreferencesUtil.getInstance(context).putString("log_status",status);
    }
    public static void setCommiBanlance(Context context,String commiBanlance){
        SharedPreferencesUtil.getInstance(context).putString("commibanlance",commiBanlance);
    }
    public static String getCommiBanlance(Context context){
        return SharedPreferencesUtil.getInstance(context).getString("commibanlance");
    }
    public static String getMobile(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("mobile");
    }

    public static String getBalance(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("balance");
    }

    public static String getName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("name");
    }

    public static String getCategory(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("category");
    }

    public static String getProvince(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("province");
    }

    public static String getCity(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("city");
    }

    public static String getCountry(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("country");
    }

    public static String getAddress(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("address");
    }

    public static String getCoordinate(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("coordinate");
    }

    public static String getLat(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("lat");
    }

    public static String getLng(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("lng");
    }

    public static String getDescription(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("description");
    }

    public static String getImages(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("images");
    }

    public static String getLogo(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("logo");
    }

    public static String getOwnerName(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("owner_name");
    }

    public static String getOperateYear(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("operate_year");
    }

    public static String getSalesPerMonth(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("sales_per_month");
    }

    public static String getIdNum(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("id_num");
    }

    public static String getStatus(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("status");
    }

    public static String getAddTime(Context context) {
        return SharedPreferencesUtil.getInstance(context).getString("add_time");
    }

    public static void setMerchantId(Context context, String merchant_id)
    {
        SharedPreferencesUtil.getInstance(context).putString("merchant_id", merchant_id);
    }

    public static void setMobile(Context context, String mobile)
    {
        SharedPreferencesUtil.getInstance(context).putString("mobile", mobile);
    }

    public static void setBalance(Context context, String balance)
    {
        SharedPreferencesUtil.getInstance(context).putString("balance", balance);
    }

    public static void setName(Context context, String name)
    {
        SharedPreferencesUtil.getInstance(context).putString("name", name);
    }

    public static void setCategory(Context context, String category)
    {
        SharedPreferencesUtil.getInstance(context).putString("category", category);
    }

    public static void setProvince(Context context, String province)
    {
        SharedPreferencesUtil.getInstance(context).putString("province", province);
    }

    public static void setCity(Context context, String city)
    {
        SharedPreferencesUtil.getInstance(context).putString("city", city);
    }

    public static void setCountry(Context context, String country)
    {
        SharedPreferencesUtil.getInstance(context).putString("country", country);
    }

    public static void setAddress(Context context, String address)
    {
        SharedPreferencesUtil.getInstance(context).putString("address", address);
    }

    public static void setCoordinate(Context context, String coordinate)
    {
        SharedPreferencesUtil.getInstance(context).putString("coordinate", coordinate);
    }

    public static void setLat(Context context, String lat)
    {
        SharedPreferencesUtil.getInstance(context).putString("lat", lat);
    }

    public static void setLng(Context context, String lng)
    {
        SharedPreferencesUtil.getInstance(context).putString("lng", lng);
    }

    public static void setDescription(Context context, String description)
    {
        SharedPreferencesUtil.getInstance(context).putString("description", description);
    }

    public static void setImages(Context context, String images)
    {
        SharedPreferencesUtil.getInstance(context).putString("images", images);
    }

    public static void setLogo(Context context, String logo)
    {
        SharedPreferencesUtil.getInstance(context).putString("logo", logo);
    }

    public static void setOwnerName(Context context, String owner_name)
    {
        SharedPreferencesUtil.getInstance(context).putString("owner_name", owner_name);
    }

    public static void setOperateYear(Context context, String operate_year)
    {
        SharedPreferencesUtil.getInstance(context).putString("operate_year", operate_year);
    }

    public static void setSalesPerMonth(Context context, String sales_per_month)
    {
        SharedPreferencesUtil.getInstance(context).putString("sales_per_month", sales_per_month);
    }

    public static void setIdNum(Context context, String id_num)
    {
        SharedPreferencesUtil.getInstance(context).putString("id_num", id_num);
    }

    public static void setStatus(Context context, String status)
    {
        SharedPreferencesUtil.getInstance(context).putString("status", status);
    }

    public static void setAddTime(Context context, String add_time)
    {
        SharedPreferencesUtil.getInstance(context).putString("add_time", add_time);
    }

    public static void setUser(Context context, UserLoginBean user) {
        if (!TextUtils.isEmpty(user.getData().getId())) {
            setMerchantId(context, user.getData().getId());
        }
        if (!TextUtils.isEmpty(user.getData().getMobile())) {
            setMobile(context, user.getData().getMobile());
        }
        if (!TextUtils.isEmpty(user.getData().getBalance())) {
            setBalance(context, user.getData().getBalance());
        }

        if (!TextUtils.isEmpty(user.getData().getName())) {
            setName(context, user.getData().getName());
        }
        if (!TextUtils.isEmpty(user.getData().getCategory())) {
            setCategory(context, user.getData().getCategory());
        }
        if (!TextUtils.isEmpty(user.getData().getProvince())) {
            setProvince(context, user.getData().getProvince());
        }

        if (!TextUtils.isEmpty(user.getData().getCity())) {
            setCity(context, user.getData().getCity());
        }
        if (!TextUtils.isEmpty(user.getData().getCountry())) {
            setCountry(context, user.getData().getCountry());
        }

        if (!TextUtils.isEmpty(user.getData().getAddress())) {
            setAddress(context, user.getData().getAddress());
        }
        if (!TextUtils.isEmpty(user.getData().getCoordinate())) {
            setCoordinate(context, user.getData().getCoordinate());
        }
        if (!TextUtils.isEmpty(user.getData().getLat())) {
            setLat(context, user.getData().getLat());
        }
        if (!TextUtils.isEmpty(user.getData().getLng())) {
            setLng(context, user.getData().getLng());
        }
        if (!TextUtils.isEmpty(user.getData().getDescription())) {
            setDescription(context, user.getData().getDescription());
        }

        if (!TextUtils.isEmpty(user.getData().getImages())) {
            setImages(context, user.getData().getImages());
        }
        if (!TextUtils.isEmpty(user.getData().getLogo())) {
            setLogo(context, user.getData().getLogo());
        }
        if (!TextUtils.isEmpty(user.getData().getOwner_name())) {
            setOwnerName(context, user.getData().getOwner_name());
        }

        if (!TextUtils.isEmpty(user.getData().getOperate_year())) {
            setOperateYear(context, user.getData().getOperate_year());
        }

        if (!TextUtils.isEmpty(user.getData().getSales_per_month())) {
            setSalesPerMonth(context, user.getData().getSales_per_month());
        }
        if (!TextUtils.isEmpty(user.getData().getId_num())) {
            setIdNum(context, user.getData().getId_num());
        }
        if (!TextUtils.isEmpty(user.getData().getStatus())) {
            setStatus(context, user.getData().getStatus());
        }
        if (!TextUtils.isEmpty(user.getData().getAdd_time())) {
            setAddTime(context, user.getData().getAdd_time());
        }
    }
    public static void removeUser(Context context) {
        SharedPreferencesUtil preferencesUtil = SharedPreferencesUtil.getInstance(context);
        preferencesUtil.remove("id");
        preferencesUtil.remove("mobile");
        preferencesUtil.remove("balance");
        preferencesUtil.remove("name");
        preferencesUtil.remove("category");
        preferencesUtil.remove("province");
        preferencesUtil.remove("city");
        preferencesUtil.remove("country");
        preferencesUtil.remove("address");
        preferencesUtil.remove("coordinate");
        preferencesUtil.remove("lat");
        preferencesUtil.remove("lng");
        preferencesUtil.remove("description");
        preferencesUtil.remove("images");
        preferencesUtil.remove("logo");
        preferencesUtil.remove("owner_name");
        preferencesUtil.remove("operate_year");
        preferencesUtil.remove("sales_per_month");
        preferencesUtil.remove("id_num");
        preferencesUtil.remove("status");
        preferencesUtil.remove("add_time");
    }
    /*
   * 获取log状态信息
   *
   * */
    public static String getLogStatus(Context context){
        return SharedPreferencesUtil.getInstance(context).getString("log_status");
    }
}
