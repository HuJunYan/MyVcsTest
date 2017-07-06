package com.tianshen.cash.net.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.tianshen.cash.R;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.event.FinishCurrentActivityEvent;
import com.tianshen.cash.event.ServiceErrorEvent;
import com.tianshen.cash.event.UpdateEvent;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class DealWithErrorUtils {

    public static void dealWithErrorCode(Context context, String result, View view) {

        JSONObject object = null;
        String msg = "";
        int code = -1;
        try {
            object = new JSONObject(result);
            msg = object.optString("msg");
            code = object.optInt("code");
            if (TextUtils.isEmpty(msg)) {
                msg = context.getResources().getString(R.string.ServiceFaile);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (object == null) {
            ToastUtil.showToast(context, msg);
            return;
        }

        switch (code) {
            case 10000:
            case 118: // 无升级
                if (view != null) {
                    ToastUtil.showToast(context, msg);
                }
                break;
            case -2: //token错误
                TianShenUserUtil.clearUser(context);
                finishActivityAndGotoLoginActivity();
                break;
            case 101: // 下单失败
            case 204: // 坐标信息不正确
            case 211:
                break;
            case 501: // 服务器开小车了，请稍后重试
                ToastUtil.showToast(context, "网络不给力：" + code);
                break;
            case 131: // 获取掌中验证码1分钟重复点击了
                break;
            case 888: // 强制升级
                JSONObject data = object.optJSONObject("data");
                String introduction = data.optString("introduction");
                String download_url = data.optString("download_url");
                LogUtil.d("abc", "introduction---->" + introduction);
                LogUtil.d("abc", "download_url---->" + download_url);

                UpdateEvent updateEvent = new UpdateEvent();
                updateEvent.setIntroduction(introduction);
                updateEvent.setDownload_url(download_url);
                EventBus.getDefault().post(updateEvent);
                break;
            case 999: // 系统维护
                ServiceErrorEvent errorEvent = new ServiceErrorEvent();
                errorEvent.setMsg(msg);
                EventBus.getDefault().post(errorEvent);
                break;
            default:
                ToastUtil.showToast(context, msg);
                break;
        }
    }

    /**
     * 通知所有的activity关闭,并且打开登录页面
     */
    public static void finishActivityAndGotoLoginActivity() {
        EventBus.getDefault().post(new FinishCurrentActivityEvent());

        MyApplicationLike myApplicationLike = MyApplicationLike.getMyApplicationLike();
        Application application = myApplicationLike.getApplication();

        Intent intent = new Intent(application, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}
