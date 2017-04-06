package com.tianshen.cash.utils;


public class StringUtil {


    public static String getTianShenCardNum(String cardNum) {
        int content = 4;
        StringBuilder sb = new StringBuilder();
        char[] cs = cardNum.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            System.out.println(cs[i]);
            if (i != 0 && i % content == 0) {
                sb.append(" ");
            }
            sb.append(cs[i]);
        }
        return sb.toString();
    }
}
