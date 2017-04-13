package com.tianshen.cash.utils;


public class SafeUtil {


    /**
     * 只显示银行卡号后4位前面用三个星号代替
     */
    public static String encodeBankCardNum(String num) {
        return "***" + num.substring(num.length() - 4, num.length());
    }

}
