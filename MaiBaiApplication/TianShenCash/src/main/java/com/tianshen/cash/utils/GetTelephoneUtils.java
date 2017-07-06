package com.tianshen.cash.utils;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.AppInfoBean;
import com.tianshen.cash.model.ContactsBean;
import com.tianshen.cash.model.RecordEntityBean;
import com.tianshen.cash.model.SMSMessageBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.fraudmetrix.sdk.FMAgent;

public class GetTelephoneUtils {

    private Context mContext;

    private List<ContactsBean> mContactsBeanList = new ArrayList<ContactsBean>();

//    private List<RecordEntityBean> mRecordEntityList = new ArrayList<RecordEntityBean>();

    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;

    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,
            Phone.CONTACT_ID};

    private final String SMS_URI_ALL = "content://sms/";
    public GetTelephoneUtils(Context context) {
        this.mContext = context;
    }

    public List<ContactsBean> getPhoneContacts() {
        ContentResolver resolver = mContext.getContentResolver();
        try {
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                    // 过滤字符串，只要数字
                    String phoneNumberF = RegexUtil.getNum(phoneNumber);

                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumberF))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                    ContactsBean mContactsBean = new ContactsBean();
                    mContactsBean.setContact_name(contactName);
                    mContactsBean.setContact_phone(phoneNumberF);
                    mContactsBeanList.add(mContactsBean);
                }
                phoneCursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mContactsBeanList.size() == 0) {
            new PermissionUtils(mContext).showPermissionDialog(1);//通讯录
        }
        return mContactsBeanList;
    }

    public List<RecordEntityBean> getCallRecord() {
        ContentResolver contentResolver = mContext.getContentResolver();
        List<RecordEntityBean> mRecordEntityList = new ArrayList<RecordEntityBean>();
        Cursor cursor = null;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showToast(mContext, "没有打开权限");
            }
            cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");

            List<RecordEntityBean> mRecordList = new ArrayList<RecordEntityBean>();
            while (cursor.moveToNext()) {
                RecordEntityBean mRecordEntity = new RecordEntityBean();
                mRecordEntity.setContact_name(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                mRecordEntity.setPhone_number(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                mRecordEntity.setIn_out(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) + "");
                mRecordEntity.setCall_time(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)) + "");
                mRecordEntity.setDuration_time(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)) + "");
                mRecordEntityList.add(mRecordEntity);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mRecordEntityList;
    }

    public List<RecordEntityBean> getLatestCallLog(Long time) {
        ContentResolver contentResolver = mContext.getContentResolver();
        List<RecordEntityBean> mRecordEntityList = new ArrayList<RecordEntityBean>();
        Cursor cursor = null;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.showToast(mContext, "没有打开权限");
            }
            cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " desc");
            if (cursor == null)
                return null;
//            List<RecordEntityBean> mRecordList = new ArrayList<RecordEntityBean>();
            long timeInMillisecond = time * 1000;
            while (cursor.moveToNext()) {
                long callTimestamp = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                if (callTimestamp > timeInMillisecond) {
                    RecordEntityBean mRecordEntity = new RecordEntityBean();
                    mRecordEntity.setContact_name(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                    mRecordEntity.setPhone_number(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                    mRecordEntity.setIn_out(cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) + "");
                    mRecordEntity.setCall_time(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)) + "");
                    mRecordEntity.setDuration_time(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)) + "");
                    mRecordEntityList.add(mRecordEntity);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mRecordEntityList;
    }

    public JSONObject getCallRecordJsonObject(Long time) {
        if (null==getCallRecord()||getCallRecord().size() == 0) {
            new PermissionUtils(mContext).showPermissionDialog(0);//通话记录
            return null;
        }
        List<RecordEntityBean> recordList = getLatestCallLog(time);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (int i = 0; i < recordList.size(); i++) {
                JSONObject json = new JSONObject();
                json.put("phone_number", recordList.get(i).getPhone_number());
                json.put("in_out", recordList.get(i).getIn_out());
                json.put("duration_time", recordList.get(i).getDuration_time());
                json.put("call_time", recordList.get(i).getCall_time());
                jsonArray.put(i, json);
            }
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));
            jsonObject.put("record_list", jsonArray);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return jsonObject;
    }


    /*
    * 读取手机短信
    * */
    public List<SMSMessageBean> getSMSFromPhone() {


        List<SMSMessageBean> messageList = new ArrayList<SMSMessageBean>();
        try {
            ContentResolver cr = mContext.getContentResolver();
            String[] projection = new String[]{"address", "person", "body", "date", "type"};
            Cursor cur = mContext.getContentResolver().query(Uri.parse(SMS_URI_ALL), projection, null, null, "date desc");
            if (null == cur) {
                return null;
            }
            if (cur.moveToFirst()) {
                int nameColumn = cur.getColumnIndex("person");
                int phoneNumberColumn = cur.getColumnIndex("address");
                int smsbodyColumn = cur.getColumnIndex("body");
                int dateColumn = cur.getColumnIndex("date");
                int typeColumn = cur.getColumnIndex("type");
                do {
                    SMSMessageBean messageBean = new SMSMessageBean();
                    messageBean.setPerson(cur.getString(nameColumn));
                    messageBean.setAddress(cur.getString(phoneNumberColumn));
                    messageBean.setBody(cur.getString(smsbodyColumn));
                    messageBean.setType(cur.getInt(typeColumn));
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(Long.valueOf(cur.getString(dateColumn), 10));
                    messageBean.setDate(dateFormat.format(d));
                    messageList.add(messageBean);
                } while (cur.moveToNext());
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return messageList;
    }

    /*
    * 根据时间获取短信列表
    * */
    public List<SMSMessageBean> getLatestMessage(Long time) {
        List<SMSMessageBean> messageList = new ArrayList<SMSMessageBean>();
        try {
            String[] projection = new String[]{"address", "person", "body", "date", "type"};
            Cursor cur = mContext.getContentResolver().query(Uri.parse(SMS_URI_ALL), projection, null, null, "date desc");
            if (null == cur) {
                return null;
            }
            long timeInMillisecond = time * 1000;
            long callTimestamp = cur.getLong(cur.getColumnIndex(CallLog.Calls.DATE));
            if (cur.moveToFirst()) {
                if (callTimestamp > timeInMillisecond) {
                    int nameColumn = cur.getColumnIndex("person");
                    int phoneNumberColumn = cur.getColumnIndex("address");
                    int smsbodyColumn = cur.getColumnIndex("body");
                    int dateColumn = cur.getColumnIndex("date");
                    int typeColumn = cur.getColumnIndex("type");
                    do {
                        SMSMessageBean messageBean = new SMSMessageBean();
                        messageBean.setPerson(cur.getString(nameColumn));
                        messageBean.setAddress(cur.getString(phoneNumberColumn));
                        messageBean.setBody(cur.getString(smsbodyColumn));
                        messageBean.setType(cur.getInt(typeColumn));
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss");
                        Date d = new Date(Long.valueOf(cur.getString(dateColumn), 10));
                        messageBean.setDate(dateFormat.format(d));
                        messageList.add(messageBean);

                    } while (cur.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        return messageList;
    }

    /*
    * 获取最近短信列表的json
    * */
    public JSONObject getLatestSMSMessageObject(Long time){
        if(getSMSFromPhone().size()==0){
            new PermissionUtils(mContext).showPermissionDialog(4);
            return null;
        }
        List<SMSMessageBean> smsMessageBeans=getLatestMessage(time);
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        try {
            for (int i = 0; i < smsMessageBeans.size(); i++) {
                JSONObject json = new JSONObject();
                json.put("",smsMessageBeans.get(i).getAddress() );
                json.put("",smsMessageBeans.get(i).getPerson());
                json.put("",smsMessageBeans.get(i).getBody());
                json.put("",smsMessageBeans.get(i).getDate());
                json.put("",smsMessageBeans.get(i).getType());
                jsonArray.put(i,json);
            }
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID,TianShenUserUtil.getUserId(mContext));
            jsonObject.put("",jsonArray);

        }catch (Exception e){

        }
        return  jsonObject;
    }


    /*
   * 获取手机中所有app列表
   * */
    public ArrayList<AppInfoBean> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppInfoBean> res = new ArrayList<AppInfoBean>();
        List<PackageInfo> packs = mContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue;
            }
            AppInfoBean newInfo = new AppInfoBean();
            newInfo.setAppName(p.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
           newInfo.setPakegeName(p.packageName);
            newInfo.setVersionName(p.versionName);
            newInfo.setVersionCode(p.versionCode + "");
            newInfo.setIcon(p.applicationInfo.loadIcon(mContext.getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }

/*
* 获取同盾指纹黑盒数据
* */
    public String  getBlackBox(){
        String blackBox=FMAgent.onEvent(mContext);
        if(blackBox==null)
            blackBox="";
        return blackBox;
    }



    public void changeDark(){
        dimBackground(1.0f, 0.5f);
    }
    public void changeLight(){
        dimBackground(0.5f,1.0f);
    }

    public void dimBackground(final float from, final float to) {
        final Window window =((Activity) mContext).getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();}

    public int getWindowWidth(){
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        int width = dm.widthPixels;
        int height3 = dm.heightPixels;
        return width;
    }
}
