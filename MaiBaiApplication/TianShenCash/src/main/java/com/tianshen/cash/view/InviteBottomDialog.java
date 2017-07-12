package com.tianshen.cash.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tencent.tauth.Tencent;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenShareUtils;

/**
 * Created by Administrator on 2017/7/10.
 */

public class InviteBottomDialog implements View.OnClickListener {

    private Dialog bottomDialog;
    private ImageView mIvQRCode;
    private Activity mContext;
    private Tencent mTencent;
    private boolean mIsCheck;

    public InviteBottomDialog(Activity context) {
        mContext = context;
        initDialog(context);

    }

    private void initDialog(Context context) {
        bottomDialog = new Dialog(context, R.style.invite_dialog_style);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_invite_share, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.invite_animation);
        contentView.findViewById(R.id.iv_invite_share_friends).setOnClickListener(this);
        contentView.findViewById(R.id.iv_invite_share_qq).setOnClickListener(this);
        contentView.findViewById(R.id.iv_invite_share_wechat).setOnClickListener(this);
        contentView.findViewById(R.id.iv_invite_share_weibo).setOnClickListener(this);
        mIvQRCode = (ImageView) contentView.findViewById(R.id.iv_qrcode);
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
            case R.id.iv_invite_share_wechat:
                shareToWeChatSession();
                break;
            case R.id.iv_invite_share_weibo:
                LogUtil.d("aaa", "weibo");
                break;
            case R.id.iv_invite_share_qq:
                shareToQQ();
                break;
            case R.id.iv_invite_share_friends:
                shareToWeChatTimeline();
                break;
        }
        mIsCheck = false;
    }

    private void shareToWeChatSession() {
        TianShenShareUtils.shareToWx(mContext.getApplicationContext(), GlobalParams.SHARE_TO_WECHAT_SESSION);
    }

    private void shareToWeChatTimeline() {
        TianShenShareUtils.shareToWx(mContext.getApplicationContext(), GlobalParams.SHARE_TO_WECHAT_TIMELINE);
    }

    private void shareToQQ() {
        TianShenShareUtils.shareToQQ(mContext, "");
    }

    /**
     * 设置二维码图片
     *
     * @param bitmap
     * @return
     */
    public InviteBottomDialog setQRCodeBitmap(Bitmap bitmap) {
        mIvQRCode.setImageBitmap(bitmap);
        return this;
    }

    public void cancel() {
        bottomDialog.cancel();
    }

}
