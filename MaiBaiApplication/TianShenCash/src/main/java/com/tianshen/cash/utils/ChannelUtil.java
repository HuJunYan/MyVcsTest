package com.tianshen.cash.utils;

/**
 * 用于渠道相关
 */

public class ChannelUtil {

    /**
     * 打印一下渠道值
     */
    public static void printChannel() {
        int channelId = 2000;
        String channelIdStr = "";
        for (int i = channelId; i < 2016; i++) {
            channelIdStr = Utils.MD5SHA1AndReverse(Utils.MD5SHA1AndReverse(i + ""));
            LogUtil.d("ret", "channelId" + i + "=" + channelIdStr);
        }
    }
}
