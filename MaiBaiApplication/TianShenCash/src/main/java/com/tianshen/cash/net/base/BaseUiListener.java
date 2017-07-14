package com.tianshen.cash.net.base;

import android.content.Context;

import com.tianshen.cash.utils.ToastUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by Administrator on 2016/7/31.
 */
public class BaseUiListener implements IUiListener {

    Context mContext;

    public BaseUiListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onComplete(Object o) {
        ToastUtil.showToast(mContext, "分享成功");
    }

    @Override
    public void onError(UiError e) {
        ToastUtil.showToast(mContext, "分享失败");
    }

    @Override
    public void onCancel() {
        ToastUtil.showToast(mContext, "分享取消");
    }
}
