package com.tianshen.cash.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.idcard.util.Util;

/**
 * Created by Administrator on 2017/7/12.
 */

public class TianShenShareUtils {

    private static IWXAPI wxapi;

    private TianShenShareUtils() {
    }

    public static IWXAPI getWxapi() {
        return wxapi;
    }

    /**
     * 分享到qq
     *
     * @param mContext
     * @param url      分享的url
     */
    public static void shareToQQ(Activity mContext, String url, IUiListener listener) {
        Tencent mTencent = Tencent.createInstance(GlobalParams.APP_QQ_ID, mContext);
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        //本地url 或者网上的图片url

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "天神贷");
        mTencent.shareToQQ(mContext, params, listener);
    }

    /**
     * 分享到微信 /朋友圈
     *
     * @param context
     * @param flag    分享到微信或者朋友圈 标志 {@link GlobalParams#SHARE_TO_WECHAT_SESSION}
     *                {@link GlobalParams#SHARE_TO_WECHAT_TIMELINE}
     */
    public static void shareToWx(Context context, int flag) {
        wxapi = WXAPIFactory.createWXAPI(context, GlobalParams.APP_WX_ID, true);
        wxapi.registerApp(GlobalParams.APP_WX_ID);
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = "http://www.baidu.com";
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = "share title";
        msg.description = "share description";
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        msg.thumbData = Util.bmp2byteArr(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "tianshenjr";
        req.message = msg;
        req.scene = flag == GlobalParams.SHARE_TO_WECHAT_SESSION ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxapi.sendReq(req);
    }
}
