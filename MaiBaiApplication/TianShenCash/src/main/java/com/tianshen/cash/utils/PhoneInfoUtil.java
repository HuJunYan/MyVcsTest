package com.tianshen.cash.utils;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.model.PhoneRecordBean;
import com.tianshen.cash.model.SmsInfoBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/27.
 */

public class PhoneInfoUtil {

    private PhoneInfoUtil() {
    }

    private static final String TAG = "PhoneInfoUtil";

    //获取手机已安装的应用列表 包名
    public static List<String> getPhoneInstalledAppList(Context context) {
        ArrayList<String> packageNameList = new ArrayList<>();
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        if (installedApplications != null || installedApplications.size() != 0) {
            for (ApplicationInfo installedApplication : installedApplications) {
                packageNameList.add(installedApplication.packageName);
            }
        }

        return packageNameList;
    }

    /**
     * 获取应用名列表
     *
     * @param context
     * @return
     */
    private static List<String> getPhoneInstalledAppNameList(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        ArrayList<String> appNameLists = new ArrayList<>();
        for (PackageInfo packageInfo : packageInfos) {
            String name = packageInfo.applicationInfo.loadLabel(pm).toString();
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //非系统应用
                appNameLists.add(name);
            }
//            String packageName = packageInfo.packageName;
            //应用图标
//            Drawable drawable = packageInfo.applicationInfo.loadIcon(pm);
        }
        return appNameLists;
    }

    /**
     * 获取设备id
     *
     * @param context 需要权限 Manifest.permission.READ_PHONE_STATE  动态申请
     * @return
     */
    public static String getDevice_id(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    static Context activity;

    /**
     * 获取短信内容列表
     *
     * @param activity 需要动态获取 Manifest.permission.READ_SMS 权限
     * @return
     */
    private static List<SmsInfoBean> getSmsList(Activity activity) {
//        PhoneInfoUtil.activity = activity;
        //通过loader 读取短信  这种方式需要异步回调 未添加
//        activity.getLoaderManager().initLoader(0, null, new SmsCallBack());
        //通过内容解析者获取短信
        return getSmsListFromResolver(activity);
    }

    //通过内容解析者 获取短信内容
    private static List<SmsInfoBean> getSmsListFromResolver(Context activity) {
        List<SmsInfoBean> smsList = new ArrayList<>();
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
            Cursor cur = activity.getContentResolver().query(uri, projection, null, null, null);      // 获取手机内部短信
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");
                SmsInfoBean smsinfoBean;
                do {
                    String phoneNumber = cur.getString(index_Address);
                    String name = cur.getString(index_Person);
                    String smsBody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String date = dateFormat.format(d);

                    int type = 0;
                    if (intType == 1) {
                        type = 0;//发送
                    } else if (intType == 2) {
                        type = 1;//接收
                    }
                    smsinfoBean = new SmsInfoBean(name, type, phoneNumber, smsBody, date);
                    smsList.add(smsinfoBean);
                } while (cur.moveToNext());
                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
        }
        return smsList;
    }


    final static String SMS_URI_ALL = "content://sms/";

    /**
     * 获取通话记录 权限在外部做处理
     * 需要权限 Manifest.permission.READ_CALL_LOG 需要动态申请
     *
     * @param context
     */
    private static ArrayList<PhoneRecordBean> getPhoneRecod(Context context) {
        ArrayList<PhoneRecordBean> phoneRecordBeans = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return phoneRecordBeans;
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            PhoneRecordBean phoneRecordBean;
            do {
                //号码
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                //呼叫类型
                String type;
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:
                        type = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        type = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        type = "未接";
                        break;
                    default:
                        type = "挂断";//应该是挂断.根据我手机类型判断出的
                        break;
                }
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //呼叫时间
                String time = sfd.format(date);
                //联系人
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                //通话时间,单位:s
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                phoneRecordBean = new PhoneRecordBean(name, type, time, duration, number);
                phoneRecordBeans.add(phoneRecordBean);
            } while (cursor.moveToNext());
        }
        return phoneRecordBeans;
    }

    /**
     * 需要权限 申明权限
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    public static boolean getNetworkType(Context context) {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return "WIFI".equals(strNetworkType);
    }

    private static class SmsCallBack implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] SMS_PROJECTION = {
                "_id", "address", "person",
                "body", "date", "type"
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(activity, Uri.parse(SMS_URI_ALL)
                    , SMS_PROJECTION,
                    null,
                    null, null
            );
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<SmsInfoBean> smsList = new ArrayList<>();
            if (data != null && !data.isClosed() && data.getCount() > 0) {
                if (data.moveToFirst()) {
                    int nameColumn = data.getColumnIndex("person");
                    int phoneNumberColumn = data.getColumnIndex("address");
                    int smsbodyColumn = data.getColumnIndex("body");
                    int dateColumn = data.getColumnIndex("date");
                    int typeColumn = data.getColumnIndex("type");
                    SmsInfoBean smsinfoBean;
                    do {
                        String name = data.getString(nameColumn);
                        String phoneNumber = data.getString(phoneNumberColumn);
                        String smsbody = data.getString(smsbodyColumn);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss");
                        Date d = new Date(Long.parseLong(data.getString(dateColumn)));
                        String date = dateFormat.format(d);
                        String type;
                        int typeId = data.getInt(typeColumn);
                        if (typeId == 1) {
                            type = "接收";
                        } else if (typeId == 2) {
                            type = "发送";
                        } else {
                            type = "";
                        }
//                        smsinfoBean = new SmsInfoBean(name, type, phoneNumber, smsbody, date);
//                        smsList.add(smsinfoBean);
                    } while (data.moveToNext());
                }
                data.close();
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    /**
     * 获取 用户已安装的app 的 json数组
     *
     * @param activity
     * @return
     */
    public static void getApp_list(final Activity activity, final PhoneInfoCallback callback) {
        getApp_listObservable(activity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JSONArray>() {
            @Override
            public void onSubscribe(Disposable d) {
                String loadText = activity.getResources().getText(MemoryAddressUtils.loading()).toString();
                // TODO 此处应该显示dialog  暂时不显示
                //ViewUtil.createLoadingDialog(activity,loadText,false);
            }

            @Override
            public void onNext(JSONArray value) {
                if (callback != null) {
                    callback.sendMessageToRegister(value, "app_list");
                }
            }

            @Override
            public void onError(Throwable e) {
                if (callback != null) {
                    callback.sendMessageToRegister(new JSONArray(), "app_list");
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取  通话记录的Json array
     *
     * @param activity
     * @param callback
     */
    public static void getCall_list(final Activity activity, final PhoneInfoCallback callback) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_CALL_LOG).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                JSONArray jsonArray = new JSONArray();
                if (aBoolean) {
                    getCall_listObservable(activity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<JSONArray>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            String loadText = activity.getResources().getText(MemoryAddressUtils.loading()).toString();
                            // TODO 此处应该显示dialog  暂时不显示
                            //ViewUtil.createLoadingDialog(activity,loadText,false);
                        }

                        @Override
                        public void onNext(JSONArray value) {
                            LogUtil.d("onNext", "value = " + value);
                            if (callback != null) {
                                callback.sendMessageToRegister(value, "call_list");
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (callback != null) {
                                callback.sendMessageToRegister(new JSONArray(), "call_list");
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
//                    ArrayList<PhoneRecordBean> phoneRecod = PhoneInfoUtil.getPhoneRecod(activity.getApplicationContext());
//                    for (int i = 0; i < phoneRecod.size(); i++) {
//                        JSONObject jsonObject = new JSONObject();
//                        PhoneRecordBean phoneRecordBean = phoneRecod.get(i);
//                        jsonObject.put("call_phone_num", phoneRecordBean.number);
//                        jsonObject.put("call_name", phoneRecordBean.name);
//                        jsonObject.put("call_time", phoneRecordBean.date);
//                        if ("-1".equals(phoneRecordBean.duration)) {
//                            jsonObject.put("call_duration", "0");
//                        } else {
//                            jsonObject.put("call_duration", phoneRecordBean.duration);
//                        }
//                        jsonArray.put(i, jsonObject);
//                    }
                }
//                callback.sendMessageToRegister(jsonArray, "call_list");

            }
        });
    }

    /**
     * 获取 短信的JSON array
     *
     * @param activity
     * @param callback
     */
    public static void getMessage_list(final Activity activity, final PhoneInfoCallback callback) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.READ_SMS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    getmessage_ListObservable(activity).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<JSONArray>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    String loadText = activity.getResources().getText(MemoryAddressUtils.loading()).toString();
                                    // TODO 此处应该显示dialog  暂时不显示
                                    //ViewUtil.createLoadingDialog(activity,loadText,false);
                                }

                                @Override
                                public void onNext(JSONArray value) {
                                    if (callback != null) {
                                        callback.sendMessageToRegister(value, "message_list");
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (callback != null) {
                                        callback.sendMessageToRegister(new JSONArray(), "message_list");
                                    }
                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                }
//                JSONArray jsonArray = new JSONArray();
//                List<SmsInfoBean> smsList = getSmsList(activity);
//                JSONObject jsonObject;
//                for (int i = 0; i < smsList.size(); i++) {
//                    SmsInfoBean smsInfoBean = smsList.get(i);
//                    jsonObject = new JSONObject();
//                    jsonObject.put("message_form_num", smsInfoBean.number);
//                    jsonObject.put("message_form_name", smsInfoBean.name);
//                    jsonObject.put("message_form_content", smsInfoBean.smsBody);
//                    jsonObject.put("message_form_time", smsInfoBean.date);
//                    jsonObject.put("message_type", smsInfoBean.type);
//                    jsonArray.put(i, jsonObject);
//                }
//                callback.sendMessageToRegister(jsonArray, "message_list");
            }
        });
    }


    private static Observable<JSONArray> getmessage_ListObservable(final Activity activity) {
        return Observable.create(new ObservableOnSubscribe<JSONArray>() {
            @Override
            public void subscribe(ObservableEmitter<JSONArray> e) throws Exception {
                if (!e.isDisposed()) {
                    JSONArray jsonArray = new JSONArray();
                    List<SmsInfoBean> smsList = getSmsList(activity);
                    JSONObject jsonObject;
                    for (int i = 0; i < smsList.size(); i++) {
                        SmsInfoBean smsInfoBean = smsList.get(i);
                        jsonObject = new JSONObject();
                        jsonObject.put("message_form_num", smsInfoBean.number);
                        jsonObject.put("message_form_name", smsInfoBean.name);
                        jsonObject.put("message_form_content", smsInfoBean.smsBody);
                        jsonObject.put("message_form_time", smsInfoBean.date);
                        jsonObject.put("message_type", smsInfoBean.type);
                        jsonArray.put(i, jsonObject);
                    }
                    Log.i(TAG, "subscribe: jsona = " + jsonArray.toString());
                    e.onNext(jsonArray);
                    e.onComplete();
                }


            }
        });
    }

    private static Observable<JSONArray> getCall_listObservable(final Activity activity) {
        return Observable.create(new ObservableOnSubscribe<JSONArray>() {
            @Override
            public void subscribe(ObservableEmitter<JSONArray> e) throws Exception {
                if (!e.isDisposed()) {
                    JSONArray jsonArray = new JSONArray();
                    ArrayList<PhoneRecordBean> phoneRecod = getPhoneRecod(activity.getApplicationContext());
                    for (int i = 0; i < phoneRecod.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        PhoneRecordBean phoneRecordBean = phoneRecod.get(i);
                        jsonObject.put("call_phone_num", phoneRecordBean.number);
                        jsonObject.put("call_name", phoneRecordBean.name);
                        jsonObject.put("call_time", phoneRecordBean.date);
                        if ("-1".equals(phoneRecordBean.duration)) {
                            jsonObject.put("call_duration", "0");
                        } else {
                            jsonObject.put("call_duration", phoneRecordBean.duration);
                        }
                        jsonArray.put(i, jsonObject);
                    }
                    e.onNext(jsonArray);
                    e.onComplete();

                }


            }
        });
    }

    private static Observable<JSONArray> getApp_listObservable(final Activity activity) {
        return Observable.create(new ObservableOnSubscribe<JSONArray>() {
            @Override
            public void subscribe(ObservableEmitter<JSONArray> e) throws Exception {
                if (!e.isDisposed()) {
                    JSONArray jsonArray = new JSONArray();
                    List<String> phoneInstalledAppNameList = getPhoneInstalledAppNameList(activity);
                    JSONObject jsonObject;
                    for (int i = 0; i < phoneInstalledAppNameList.size(); i++) {
                        jsonObject = new JSONObject();
                        jsonObject.put("app_name", phoneInstalledAppNameList.get(i));
                        jsonArray.put(i, jsonObject);
                    }
                    e.onNext(jsonArray);
                    e.onComplete();
                }

            }


        });
    }

    public interface PhoneInfoCallback {
        void sendMessageToRegister(JSONArray jsonArray, String jsonArrayName);
    }
}
