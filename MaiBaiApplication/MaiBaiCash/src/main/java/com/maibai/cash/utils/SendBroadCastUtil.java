package com.maibai.cash.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.maibai.cash.constant.GlobalParams;

/**
 * Created by Administrator on 2016/9/24.
 */

public class SendBroadCastUtil {
    Context context;
    public SendBroadCastUtil(Context context) {
        this.context=context;
    }

    public void sendBroad(String command, Bundle bundle){
        Intent intent=new Intent();
        intent.setAction(command);
        if (null!=bundle){
            intent.putExtras(bundle);
        }
        context.sendBroadcast(intent);
    }
}
