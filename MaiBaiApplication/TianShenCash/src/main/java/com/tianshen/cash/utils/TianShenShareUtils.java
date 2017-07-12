package com.tianshen.cash.utils;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.tianshen.cash.constant.GlobalParams;

/**
 * Created by Administrator on 2017/7/12.
 */

public class TianShenShareUtils {
    private TianShenShareUtils() {
    }

    public static void shareToQQ(Activity mContext, String url) {
        Tencent mTencent = Tencent.createInstance(GlobalParams.APP_QQ_ID, mContext);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
        //本地url 或者网上的图片url
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "天神贷");
        mTencent.shareToQQ(mContext, params, null);
    }
}
