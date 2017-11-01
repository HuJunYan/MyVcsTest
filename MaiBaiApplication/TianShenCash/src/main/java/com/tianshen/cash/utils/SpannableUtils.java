package com.tianshen.cash.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class SpannableUtils {
    private SpannableUtils() {
    }

    private static int flag = SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE;

    /**
     * 设置去web页面的spannable样式
     * @param textView
     * @param text
     * @param start
     * @param end
     * @param spanList
     * @param spanTextColor
     */
    public static void setWebSpannableString(TextView textView, String text, String start, String end, List<CharacterStyle> spanList, int spanTextColor) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || spanList == null || spanList.size() == 0) {
            return;
        }
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        int startIndex;
        int endIndex = 0;
        int count = 0;
        while ((startIndex = text.indexOf(start, endIndex)) != -1) {
            if (count == spanList.size()) {
                break;
            }
            endIndex = text.indexOf(end, endIndex) + 1;
            ss.setSpan(spanList.get(count), startIndex, endIndex, flag);
            setTransparentBackgoundSpan(ss, startIndex, endIndex);
            setSpanTextColor(ss, startIndex, endIndex, spanTextColor);
            count++;
        }
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static void setTransparentBackgoundSpan(SpannableStringBuilder ss, int start, int end) {
        ss.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), start, end, flag);
    }

    private static void setSpanTextColor(SpannableStringBuilder ss, int start, int end, int color) {
        ss.setSpan(new ForegroundColorSpan(color), start, end, flag);
    }
}
