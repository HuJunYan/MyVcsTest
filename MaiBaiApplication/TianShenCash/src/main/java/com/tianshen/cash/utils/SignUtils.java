package com.tianshen.cash.utils;

import android.util.Log;

import com.tianshen.cash.constant.GlobalParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenrongshang on 16/7/3.
 */
public class SignUtils {
    public static final int NOT_JSON = 0;
    public static final int JSONOBJ = 1;
    public static final int JSONARRAY = 2;

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public static JSONObject signJsonNotContainList(JSONObject jsonObject) {
        List<String> keyList = getSortedKeyList(jsonObject);
        String paramString = "";
        try {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                paramString += key + "=" + jsonObject.getString(key);
                if (i != keyList.size() - 1) {
                    paramString += "&";
                }
            }
            String secretKey = Utils.md5(paramString + GlobalParams.getSlot());
            jsonObject.put("sign", secretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static List<String> getSortedKeyList(JSONObject jsonObject) {
        List<String> keyList = copyIterator(jsonObject.keys());
        Collections.sort(keyList);
        return keyList;
    }

    public static JSONObject signJsonContainTwoLevelList(JSONObject jsonObject, String firstLevelList, String secondLevelList) {
        JSONArray jsonArray;
        try {
            JSONObject newSubJson = new JSONObject();
            jsonArray = jsonObject.getJSONArray(firstLevelList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) (jsonArray.get(i));
                String md5 = signListJsonMd5(json, secondLevelList);
                newSubJson.put(i + "", md5);
            }

            String newJsonMd5String = getJsonObjectSignForList(newSubJson);  // 数字大小排
            JSONObject newJson = jsonObject;
            newJson.remove(firstLevelList);
            newJson.put(firstLevelList, newJsonMd5String);
            String signString = getJsonObjectSign(newJson);
            jsonObject.put(firstLevelList, jsonArray);
            jsonObject.put("sign", signString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String signListJsonMd5(JSONObject jsonObject, String listType) {
        JSONArray jsonArray;
        try {
            JSONObject newSubJson = new JSONObject();
            jsonArray = jsonObject.getJSONArray(listType);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) (jsonArray.get(i));
                String md5Str = getJsonObjectSign(json);
                newSubJson.put(i + "", md5Str);
            }

            String newJsonMd5String = getJsonObjectSignForList(newSubJson);  // 数字大小排
            JSONObject newJson = jsonObject;
            newJson.remove(listType);
            newJson.put(listType, newJsonMd5String);
            String md5 = getJsonObjectSign(newJson);
            jsonObject.put(listType, jsonArray);

            return md5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static JSONObject signJsonContainList(JSONObject jsonObject, String... listType) {
        try {
            String jsons = jsonObject.toString();
            JSONObject newJson = new JSONObject(jsons);
            JSONArray jsonArray;
            for (int i = 0; i < listType.length; i++) {
                jsonArray = jsonObject.getJSONArray(listType[i]);
                JSONObject newSubJson = new JSONObject();
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject json = (JSONObject) (jsonArray.get(j));
                    String md5Str = getJsonObjectSign(json);
                    newSubJson.put(j + "", md5Str);
                }
                String newJsonMd5String = getJsonObjectSignForList(newSubJson);  // 数字大小排
                newJson.remove(listType[i]);
                newJson.put(listType[i], newJsonMd5String);
//                jsonObject.put(listType[i], jsonArray);
            }
//            JSONObject jsonObject2 = new JSONObject(s);
            String signString = getJsonObjectSign(newJson);
//            LogUtil.d("aaa2", "sign = " + signString);
            jsonObject.put("sign", signString);
//            LogUtil.d("aaa2", "jsonObject = " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private static String getJsonObjectSign(JSONObject jsonObject) {
        List<String> keyList = SignUtils.getSortedKeyList(jsonObject);
        String secretKey = "";
        String paramString = "";
        try {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                paramString += key + "=" + jsonObject.getString(key);
                if (i != keyList.size() - 1) {
                    paramString += "&";
                }
            }
            secretKey = Utils.md5(paramString + GlobalParams.getSlot());
//            Log.d("rets", paramString + " : " + secretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return secretKey;
    }


    private static String getJsonObjectSignForList(JSONObject jsonObject) {
        List<String> keyList = SignUtils.getSortedKeyListForList(jsonObject);
        String secretKey = "";
        String paramString = "";
        try {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                paramString += key + "=" + jsonObject.getString(key);
                if (i != keyList.size() - 1) {
                    paramString += "&";
                }
            }
            secretKey = Utils.md5(paramString + GlobalParams.getSlot());
            Log.d("rets", paramString + " : " + secretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return secretKey;
    }

    public static List<String> getSortedKeyListForList(JSONObject jsonObject) {
        List<String> keyList = copyIterator(jsonObject.keys());
        List<Integer> keyListInt = new ArrayList<Integer>();
        for (int i = 0; i < keyList.size(); i++) {
            keyListInt.add(Integer.valueOf(keyList.get(i)));
        }
        Collections.sort(keyListInt);
        List<String> keyListResult = new ArrayList<String>();
        for (int i = 0; i < keyList.size(); i++) {
            keyListResult.add(keyListInt.get(i) + "");
        }
        return keyListResult;
    }
}
