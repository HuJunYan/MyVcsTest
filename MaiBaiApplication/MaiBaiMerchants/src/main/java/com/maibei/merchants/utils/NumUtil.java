package com.maibei.merchants.utils;

/**
 * Created by 14658 on 2016/6/28.
 */
public class NumUtil {
    public static String NumberFormat(double f,int m){
        return String.format("%."+m+"f",f);
    }

    public static float NumberFormatFloat(double f,int m){
        String strfloat = NumberFormat(f,m);
        return Float.parseFloat(strfloat);
    }
}
