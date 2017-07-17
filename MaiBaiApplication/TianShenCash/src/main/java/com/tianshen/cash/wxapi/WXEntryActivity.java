package com.tianshen.cash.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tianshen.cash.event.WechatShareEvent;
import com.tianshen.cash.utils.TianShenShareUtils;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/7/12.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IWXAPI wxapi = TianShenShareUtils.getWxapi();
        if (wxapi != null)
            wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = null;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK: {
                result = "分享成功";
                EventBus.getDefault().post(new WechatShareEvent());
            }
            break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = "分享取消";
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "分享取消";
//                break;
//            default:
//                result = "分享取消";
//                break;
        }
        if (result != null){
            ToastUtil.showToast(this, result);
        }
        this.finish();
    }


}
