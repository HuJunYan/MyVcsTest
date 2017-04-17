package com.tianshen.cash.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PhoneUtils {


    /**
     * 获取手机联系人，优先从手机里面获取，如果获取不到再从SIM卡里面获取
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_CONTACTS"/>}</p>
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context context) {
        List<HashMap<String, String>> allContactInfoOfPhone = getAllContactInfoOfPhone(context);
        if (allContactInfoOfPhone.size() == 0) {
            allContactInfoOfPhone = getAllContactInfoOfSIM(context);
        }
        return allContactInfoOfPhone;
    }


    /**
     * 获取手机联系人
     */
    public static List<HashMap<String, String>> getAllContactInfoOfPhone(Context context) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // 1.获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        // 2.获取内容提供者的地址:com.android.contacts
        // raw_contacts表的地址 :raw_contacts
        // view_data表的地址 : data
        // 3.生成查询地址
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri date_uri = Uri.parse("content://com.android.contacts/data");
        // 4.查询操作,先查询raw_contacts,查询contact_id
        // projection : 查询的字段
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);

        if (cursor == null) {
            return list;
        }

        // 5.解析cursor
        while (cursor.moveToNext()) {
            // 6.获取查询的数据
            String contact_id = cursor.getString(0);
            // cursor.getString(cursor.getColumnIndex("contact_id"));//getColumnIndex
            // : 查询字段在cursor中索引值,一般都是用在查询字段比较多的时候
            // 判断contact_id是否为空
            if (!TextUtils.isEmpty(contact_id)) {//null   ""
                // 7.根据contact_id查询view_data表中的数据
                // selection : 查询条件
                // selectionArgs :查询条件的参数
                // sortOrder : 排序
                // 空指针: 1.null.方法 2.参数为null
                Cursor c = resolver.query(date_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?",
                        new String[]{contact_id}, null);
                HashMap<String, String> map = new HashMap<String, String>();
                // 8.解析c
                while (c.moveToNext()) {
                    // 9.获取数据
                    String data1 = c.getString(0);
                    String mimetype = c.getString(1);
                    // 10.根据类型去判断获取的data1数据并保存
                    if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                        // 电话
                        map.put("phone", data1);
                    } else if (mimetype.equals("vnd.android.cursor.item/name")) {
                        // 姓名
                        map.put("name", data1);
                    }
                }
                // 11.添加到集合中数据
                list.add(map);
                // 12.关闭cursor
                c.close();
            }
        }
        // 12.关闭cursor
        cursor.close();
        return list;
    }

    /**
     * 从SIM卡获取联系人
     */
    public static List<HashMap<String, String>> getAllContactInfoOfSIM(Context context) {
        //content://icc/adn
        //content://sim/adn
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        Uri uri = Uri.parse("content://icc/adn");
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) {
            return list;
        }

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(Contacts.People._ID));
            String name = cursor.getString(cursor.getColumnIndex(Contacts.People.NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(Contacts.People.NUMBER));
            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("phone", phoneNumber);
            list.add(map);
        }
        return list;
    }

}
