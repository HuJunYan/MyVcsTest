package com.maibei.merchants.jpushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.net.api.JpushHandle;
import com.maibei.merchants.net.base.JpushCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.service.UploadLogService;
import com.maibei.merchants.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/13.
 */
public class JPushReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        LogUtil.d("JPush", JPushInterface.getRegistrationID(context) + "" + (UserUtil.getMerchantId(mContext) != null));
        LogUtil.d("JPush", JPushInterface.EXTRA_EXTRA);
        if (null != intent && null != intent.getAction()) {
            if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
                if (UserUtil.getMerchantId(context) != null && !"".equals(UserUtil.getMerchantId(context))) {
                    try {
                        Bundle bundle = intent.getExtras();
                        JpushHandle mJpushHandle = new JpushHandle(mContext);
                        String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
                        LogUtil.d("ret", "jpushResult = " + result);
                        mJpushHandle.jpushHandle(result, new JpushCallBack() {
                            @Override
                            public void onResult(int type, Object object) {
                                switch (type) {
                                    case GlobalParams.UPDATE_LOG_STATUS:
                                        UserUtil.setLogStatus(context,GlobalParams.LOG_STATUS_NEED_UPLOAD);
                                        uploadLog(context);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });

                    } catch (Exception e) {
                        MobclickAgent.reportError(mContext, LogUtil.getException(e));
                        LogUtil.e("error", LogUtil.getException(e));
                    }
                }
            }
        }
    }

    private void uploadLog(Context context){
        if(GlobalParams.LOG_STATUS_NEED_UPLOAD.equals(UserUtil.getLogStatus(context))||GlobalParams.LOG_STATUS_IS_UPLOAD_fail.equals(UserUtil.getLogStatus(context))) {
            Intent intent = new Intent(context, UploadLogService.class);
            context.startService(intent);
        }
    }
}
