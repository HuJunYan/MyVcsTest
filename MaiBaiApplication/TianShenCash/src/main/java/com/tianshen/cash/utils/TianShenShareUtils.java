package com.tianshen.cash.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;

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
import com.tencent.tauth.UiError;
import com.tianshen.cash.R;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.WechatShareEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class TianShenShareUtils {

    private static IWXAPI wxapi;
    private static int THUMB_SIZE = 150;

    private TianShenShareUtils() {
    }

    public static IWXAPI getWxapi() {
        return wxapi;
    }

    /**
     * 分享到qq
     *
     * @param mContext
     * @param url              分享的url
     * @param shareTitle
     * @param shareDescription
     */
    public static void shareToQQ(Activity mContext, String url, IUiListener listener, String shareTitle, String shareDescription, @DrawableRes int res, String iconName) {
        Tencent mTencent = Tencent.createInstance(GlobalParams.APP_QQ_ID, MyApplicationLike.getmApplication());
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDescription);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        //本地url 或者网上的图片url
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, getQQThumbPath(mContext, res, iconName));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "天神贷");
        mTencent.shareToQQ(mContext, params, listener);
    }

    private static String getQQThumbPath(Context context, @DrawableRes int res, String name) {
        File file = new File(context.getCacheDir().getAbsolutePath(), name + ".png");
        if (!file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }

    /**
     * 分享到微信 /朋友圈
     *
     * @param context
     * @param flag             分享到微信或者朋友圈 标志 {@link GlobalParams#SHARE_TO_WECHAT_SESSION}
     *                         {@link GlobalParams#SHARE_TO_WECHAT_TIMELINE}
     * @param shareTitle
     * @param shareDescription
     */
    public static void shareToWx(Context context, int flag, String mShareUrl, String shareTitle, String shareDescription, @DrawableRes int res) {
        if (!isWeixinAvilible(context)) {
            ToastUtil.showToast(context, "请先安装微信");
            return;
        }
        wxapi = WXAPIFactory.createWXAPI(MyApplicationLike.getmApplication(), GlobalParams.APP_WX_ID);
        wxapi.registerApp(GlobalParams.APP_WX_ID);
//        WXWebpageObject webPage = new WXWebpageObject();
//        webPage.webpageUrl = mShareUrl;
//        WXMediaMessage msg = new WXMediaMessage(webPage);
//        msg.title = context.getResources().getString(R.string.invite_share_text_description);
//        msg.description = context.getResources().getString(R.string.invite_share_text_description);
//        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumb, 150, 150, true);
//        msg.thumbData = bmpToByteArray(scaledBitmap);
////        msg.thumbData = Util.bmp2byteArr(thumb);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "webpage" + System.currentTimeMillis();
//        req.message = msg;
//        req.scene = flag == GlobalParams.SHARE_TO_WECHAT_SESSION ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//        wxapi.sendReq(req);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mShareUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareTitle;
        msg.description = shareDescription;
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = bmpToByteArray(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
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
    public static ImageObject getImageObj(Context context, @DrawableRes int res) {
        ImageObject imageObject = new ImageObject();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
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
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.inviteicon);
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

    public static byte[] bmpToByteArray(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private static IUiListener sIUiListener;

    public static IUiListener getIUiListenerInstance() {
        if (sIUiListener == null) {
            synchronized (TianShenShareUtils.class) {
                if (sIUiListener == null) {
                    sIUiListener = new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            ToastUtil.showToast(MyApplicationLike.getmApplication(), "分享成功");
                            EventBus.getDefault().post(new WechatShareEvent());
                        }

                        @Override
                        public void onError(UiError uiError) {
                        }

                        @Override
                        public void onCancel() {
                        }
                    };
                }
            }
        }
        return sIUiListener;
    }


}
