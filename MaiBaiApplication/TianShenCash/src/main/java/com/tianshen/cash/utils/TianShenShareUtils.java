package com.tianshen.cash.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.utils.Utility;
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

import java.util.List;

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
        if (!isWeixinAvilible(context)) {
            ToastUtil.showToast(context, "请先安装微信");
            return;
        }
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


    /**
     * 微博分享
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    public static TextObject getTextObj(String text) {
        TextObject textObject = new TextObject();
        textObject.text = text;
        return textObject;
    }

    /**
     * 微博分享
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    public static ImageObject getImageObj(Context context) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 微博分享
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    public static WebpageObject getWebpageObj(Context context, String url, String title, String description) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = description;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        mediaObject.defaultText = "";
        return mediaObject;
    }

    //判断是否安装微信
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
    }
}
