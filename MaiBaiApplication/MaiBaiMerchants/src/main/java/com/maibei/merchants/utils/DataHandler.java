package com.maibei.merchants.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/12.
 */

public class DataHandler {
    public static double stringToDouble(String content){
        double result=0.0;
        if(content.contains(".")){
            String before=content.substring(0,content.indexOf("."));
            String after=content.substring(content.indexOf(".")+1,content.length());
            result=Integer.parseInt(before)+Double.parseDouble(after)/100;
        }else{
            result=Integer.parseInt(content);
        }
        BigDecimal bg = new BigDecimal(result);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
