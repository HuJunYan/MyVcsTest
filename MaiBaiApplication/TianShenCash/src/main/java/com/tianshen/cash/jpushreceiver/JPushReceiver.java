package com.tianshen.cash.jpushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tianshen.cash.activity.NavigationActivity;
import com.tianshen.cash.activity.NotificationWebActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.PayDataOKEvent;
import com.tianshen.cash.event.RefreshRepayDataEvent;
import com.tianshen.cash.event.RiskPreFinishEvent;
import com.tianshen.cash.event.UserConfigChangedEvent;
import com.tianshen.cash.model.MsgContent;
import com.tianshen.cash.net.api.UpdateMessageStatus;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.JpushBaseBean;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

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
                    case "6"://风控评测完毕
                        EventBus.getDefault().post(new RiskPreFinishEvent());
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
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            // 自定义通知 事件
            Bundle bundle = intent.getExtras();
            String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JpushBaseBean jpushBaseBean = GsonUtil.json2bean(result, JpushBaseBean.class);
            String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String msg_type = jpushBaseBean.msg_type;
            if (TextUtils.isEmpty(msg_type)) {
                return;
            }

            MsgContent msg_content = jpushBaseBean.msg_content;
            switch (msg_type) {
                case "5"://打开消息中心
                    processMsg(context, msg_content);
                    break;
                default: //打开首页
                    if (MyApplicationLike.isOnResume) {
                        return;
                    }
                    Intent realIntent = new Intent(context, NavigationActivity.class);
                    realIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    toIntent(context, realIntent);
                    break;
            }

            LogUtil.d("wangchen", "onReceive: ACTION_NOTIFICATION_OPENED = " + "result = " + result + ",msgtype =" + msg_type + ",alert = " + alert
                    + ",title = " + title + ",msg_content = " + msg_content);
            ;


        }
    }

    private void processMsg(Context context, MsgContent msg_content) {
        if (msg_content == null) {
            return;
        }
        Intent realIntent;
        if (TextUtils.isEmpty(msg_content.message_share_url)) {
            realIntent = new Intent(context, NavigationActivity.class);
        } else {
            realIntent = new Intent(context, NotificationWebActivity.class);
        }
        //  调用已读接口
        tellServerHasRead(msg_content,context);
        realIntent.putExtra(GlobalParams.NOTIFICATION_MESSAGE_KEY, msg_content);
        if (MyApplicationLike.isOnResume) {
            realIntent.putExtra(GlobalParams.NOTIFICATION_IS_ONRESUME_CLICK, true);
        } else {
            realIntent.putExtra(GlobalParams.NOTIFICATION_IS_ONRESUME_CLICK, false);
        }
        realIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toIntent(context, realIntent);

    }

    private void toIntent(Context context, Intent realIntent) {
        context.startActivity(realIntent);
    }

    private void tellServerHasRead(MsgContent msgContent, Context context) {
        if (msgContent == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(context));
            jsonObject.put("msg_id", msgContent.message_id);
            jsonObject.put("msg_type", msgContent.message_type);
            if (!TextUtils.isEmpty(msgContent.message_mark))
                jsonObject.put("message_mark", msgContent.message_mark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UpdateMessageStatus updateMessageStatus = new UpdateMessageStatus(context);
        updateMessageStatus.update(jsonObject, null, false, null);
    }
}
