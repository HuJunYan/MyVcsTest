package com.maibei.merchants.utils;

import com.maibei.merchants.constant.GlobalParams;

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
    private static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public static JSONObject signJSONbject(JSONObject jsonObject) {
        List<String> keyList = getSortedKeyList(jsonObject);
        String paramString = "";
        try {
            for (int i = 0; i < keyList.size(); i++) {
                String key = keyList.get(i);
                paramString += key+"="+jsonObject.getString(key);
                if (i != keyList.size()-1) {
                    paramString += "&";
                }
            }
            String secretKey = Utils.md5(paramString+ GlobalParams.getSlot());
            jsonObject.put("sign", secretKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private static List<String> getSortedKeyList(JSONObject jsonObject) {
        List<String> keyList = copyIterator(jsonObject.keys());
        Collections.sort(keyList);
        return keyList;
    }
}
