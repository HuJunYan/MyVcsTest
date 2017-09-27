package com.tianshen.cash.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tencent.tauth.IUiListener;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.TianShenShareUtils;

/**
 * Created by Administrator on 2017/7/10.
 */

public class InviteBottomDialog implements View.OnClickListener {
    public static final int TYPE_WEB = 1;
    public static final int TYPE_NORMAL_SHARE = 2;
    private Dialog bottomDialog;
    private ImageView mIvQRCode;
    private Activity mContext;
    private boolean mIsCheck;
    private IUiListener listener;
    private ShareWeiboListener shareWeiboListener;
    private String mShareUrl;
    private String shareTitle;
    private String shareDescription;
    private int iconRes;
    private String iconName;
    private int type = TYPE_NORMAL_SHARE;

    /**
     * @param context
     * @param listener qq分享回调监听
     */
    public InviteBottomDialog(Activity context, IUiListener listener, String shareTitle, String shareDescription, int type) {
        mContext = context;
        this.listener = listener;
        this.shareTitle = shareTitle;
        this.shareDescription = shareDescription;
        this.type = type;
        initDialog(context);

    }

    private void initDialog(Context context) {
        bottomDialog = new Dialog(context, R.style.invite_dialog_style);
        View contentView;
        if (type == TYPE_WEB) {
            contentView = LayoutInflater.from(context).inflate(R.layout.dialog_share_web, null);
            contentView.findViewById(R.id.tv_share_web_cancel).setOnClickListener(this);
        } else {
            contentView = LayoutInflater.from(context).inflate(R.layout.dialog_invite_share, null);
            mIvQRCode = (ImageView) contentView.findViewById(R.id.iv_qrcode);//二维码
        }
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.invite_animation);
        contentView.findViewById(R.id.ll_invite_share_friends).setOnClickListener(this);
        contentView.findViewById(R.id.ll_invite_share_qq).setOnClickListener(this);
        contentView.findViewById(R.id.ll_invite_share_wechat).setOnClickListener(this);
        contentView.findViewById(R.id.ll_invite_share_weibo).setOnClickListener(this);
    }


    public void show() {
        bottomDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (mIsCheck) {
            return;
        }
        mIsCheck = true;
        switch (v.getId()) {
            case R.id.ll_invite_share_wechat:
                shareToWeChatSession();
                break;
            case R.id.ll_invite_share_weibo:
                shareToWeibo();
                break;
            case R.id.ll_invite_share_qq:
                shareToQQ();
                break;
            case R.id.ll_invite_share_friends:
                shareToWeChatTimeline();
                break;
            case R.id.tv_share_web_cancel:
                cancel();
                break;
        }
        mIsCheck = false;
    }

    private void shareToWeibo() {
        if (shareWeiboListener != null) {
            shareWeiboListener.shareToWeibo();
        }
    }

    public InviteBottomDialog setWeiBoListener(ShareWeiboListener listener) {
        shareWeiboListener = listener;
        return this;
    }

    private void shareToWeChatSession() {
        TianShenShareUtils.shareToWx(mContext.getApplicationContext(), GlobalParams.SHARE_TO_WECHAT_SESSION, mShareUrl, shareTitle, shareDescription, iconRes);
    }

    private void shareToWeChatTimeline() {
        TianShenShareUtils.shareToWx(mContext.getApplicationContext(), GlobalParams.SHARE_TO_WECHAT_TIMELINE, mShareUrl, shareTitle, shareDescription, iconRes);
    }

    private void shareToQQ() {
        TianShenShareUtils.shareToQQ(mContext, mShareUrl, listener, shareTitle, shareDescription, iconRes, iconName);
    }

    /**
     * 设置二维码图片
     *
     * @param bitmap
     * @return
     */
    public InviteBottomDialog setQRCodeBitmap(Bitmap bitmap) {
        if (mIvQRCode == null || type == TYPE_WEB) {
            return this;
        }
        mIvQRCode.setImageBitmap(bitmap);
        return this;
    }

    public void cancel() {
        if (bottomDialog.isShowing()) {
            bottomDialog.cancel();
        }

    }

    public InviteBottomDialog setShareUrl(String mShareUrl) {
        this.mShareUrl = mShareUrl;
        return this;
    }

    public interface ShareWeiboListener {
        void shareToWeibo();
    }

    public InviteBottomDialog setShareIconResAndName(@DrawableRes int res, String name) {
        this.iconRes = res;
        this.iconName = name;
        return this;
    }
}
