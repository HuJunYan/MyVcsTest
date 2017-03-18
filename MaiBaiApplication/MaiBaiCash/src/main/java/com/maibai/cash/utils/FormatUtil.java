package com.maibai.cash.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.text.DecimalFormat;

/**
 * Created by zhangchi on 2016/6/21.
 */
public class FormatUtil {
    public static SpannableStringBuilder getStyleText(String text) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#FF17A7E8")), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(20, true), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.GRAY), 4, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(15, true), 4, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
    public String formatDouble(double d ){
        //保留两位
        DecimalFormat df=new DecimalFormat(".##");
        String st=df.format(d);
        return st;
    }
}
