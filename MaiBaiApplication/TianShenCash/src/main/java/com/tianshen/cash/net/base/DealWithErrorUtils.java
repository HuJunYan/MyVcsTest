package com.tianshen.cash.net.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.litesuits.orm.LiteOrm;
import com.tianshen.cash.R;
import com.tianshen.cash.activity.LoginActivity;
import com.tianshen.cash.base.MyApplication;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.event.FinishCurrentActivityEvent;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.event.ServiceErrorEvent;
import com.tianshen.cash.manager.DBManager;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class DealWithErrorUtils {

    public static void dealWithErrorCode(Context context, String result, View view) {
        ResponseBean mResponseBean = null;
        String errorMsg = "";
        try {
            mResponseBean = GsonUtil.json2bean(result, ResponseBean.class);
            errorMsg = mResponseBean.getMsg();
        } catch (Exception e) {
            errorMsg = context.getResources().getString(R.string.ServiceFaile);
        }
        if (mResponseBean != null) {
            showErrorToast(context, mResponseBean.getCode(), errorMsg , view);
        }
    }

    private static void showErrorToast(Context context, int err_code, String err_msg, View view) {
        switch (err_code) {
            case 10000:
            case 118: // 无升级
                if (view != null) {
                    ToastUtil.showToast(context, err_msg);
                }
                break;
            case -2: //token错误
                LiteOrm liteOrm = DBManager.getInstance(context).getLiteOrm();
                liteOrm.delete(User.class);
                finishActivityAndGotoLoginActivity();
                break;
            case 101: // 下单失败
            case 204: // 坐标信息不正确
            case 211:
                break;
            case 501: // 服务器开小车了，请稍后重试
                ToastUtil.showToast(context, "网络不给力：" + err_code);
                break;
            case 131: // 获取掌中验证码1分钟重复点击了
                break;
            case 999: // 系统维护
                ServiceErrorEvent errorEvent = new ServiceErrorEvent();
                errorEvent.setMsg(err_msg);
                EventBus.getDefault().post(errorEvent);
                break;
            default:
                ToastUtil.showToast(context, err_msg);
                break;
        }
    }

    /**
     * 通知所有的activity关闭,并且打开登录页面
     */
    private static void finishActivityAndGotoLoginActivity() {
        EventBus.getDefault().post(new FinishCurrentActivityEvent());

        MyApplicationLike myApplicationLike = MyApplicationLike.getMyApplicationLike();
        Application application = myApplicationLike.getApplication();

        Intent intent = new Intent(application, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }
}
