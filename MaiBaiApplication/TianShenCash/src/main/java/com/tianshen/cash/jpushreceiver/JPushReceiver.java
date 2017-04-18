package com.tianshen.cash.jpushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.net.api.JpushHandle;
import com.tianshen.cash.net.base.JpushCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/8/13.
 */
public class JPushReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;

        if (intent == null) {
            return;
        }

        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return;
        }

        if (action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            boolean login = TianShenUserUtil.isLogin(mContext);
            if (!login) {
                return;
            }
            try {
                Bundle bundle = intent.getExtras();
                JpushHandle mJpushHandle = new JpushHandle(mContext);
                String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtil.d("abc", "jpushResult-->" + result);
                mJpushHandle.jpushHandle(result, new JpushCallBack() {
                    @Override
                    public void onResult(int type, Object object) {
                        switch (type) {
                            case GlobalParams.VERIFY_FINISHED:
                                // 1
                                EventBus.getDefault().post(new UserConfigChangedEvent());
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
