package com.tianshen.cash.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.net.base.BaseUiListener;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/10.
 */
public class ShareUtils {
    Context mContext;
    private IWXAPI wxApi;
    private static final int SHARE_PIC = R.mipmap.ic_launcher;
    private static final String SHARE_PIC_WEB = "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif";
    private static final String SHARE_TITLE = "天神贷";
    private static final String SHARE_DESCRIPTION = "现在用天神贷，可用500-10000信用额度";
    private static final String SHARE_WEBURL = "http://123.56.252.169/Public/Download/index.html";
    private static final String SHARE_BACK = "返回天神贷";
    public ShareUtils(Context context) {
        this.mContext = context;
    }


    public void shareToQQ(Tencent mTencent,BaseUiListener listener){

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, SHARE_TITLE);//标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  SHARE_DESCRIPTION);//摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  SHARE_WEBURL);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, SHARE_PIC_WEB);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, SHARE_BACK);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQQ((Activity)mContext, params, listener);
    }
    public void shareQZone(Tencent mTencent,IUiListener listener) {
        ArrayList<String> imgURLs=new ArrayList<String>();
        imgURLs.add(SHARE_PIC_WEB);
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, SHARE_TITLE);//标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, SHARE_DESCRIPTION);//摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, SHARE_WEBURL);//跳转URL
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgURLs);//图片链接ArrayList

//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "天神贷");//标题
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "返回天神贷");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        mTencent.shareToQzone((Activity)mContext, params, listener);
    }

    public void shareWeixin(int flag){
        //实例化
        wxApi = WXAPIFactory.createWXAPI(mContext, GlobalParams.WX_APP_ID);
        wxApi.registerApp(GlobalParams.WX_APP_ID);
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = SHARE_WEBURL;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = SHARE_TITLE;
        msg.description = SHARE_DESCRIPTION;
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), SHARE_PIC);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }
}
