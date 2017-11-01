package com.tianshen.cash.jpushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tianshen.cash.event.PayDataOKEvent;
import com.tianshen.cash.event.RefreshRepayDataEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.JpushBaseBean;
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

        LogUtil.d("abc", "JPushReceiver-onReceive--->");


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
                String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JpushBaseBean jpushBaseBean = GsonUtil.json2bean(result, JpushBaseBean.class);
                String msg_type = jpushBaseBean.msg_type;

                if (TextUtils.isEmpty(msg_type)) {
                    return;
                }

                LogUtil.d("abc", "JPushReceiver-msg_type--->" + msg_type);

                switch (msg_type) {
                    case "1"://审核通过
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "2"://审核失败
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "3"://放款成功
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "4"://还款成功
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "5"://首页右上角有新消息了
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "30"://确认借款数据准备完毕的推送
                        EventBus.getDefault().post(new PayDataOKEvent());
                        break;
                    case "98"://通知栏上展示通用通知
                        break;
                    case "99"://预下单有变化
                        EventBus.getDefault().post(new UserConfigChangedEvent());
                        break;
                    case "100":
                        EventBus.getDefault().post(new UserConfigChangedEvent());//刷新userconfig
                        EventBus.getDefault().post(new RefreshRepayDataEvent());//刷新还款接口
                        break;
                }
            } catch (Exception e) {
                MobclickAgent.reportError(mContext, LogUtil.getException(e));
                LogUtil.e("error", LogUtil.getException(e));
            }
        }
    }
}
