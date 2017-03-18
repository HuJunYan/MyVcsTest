package com.maibei.merchants.net.api;

import android.content.Context;

import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.net.base.GsonUtil;
import com.maibei.merchants.net.base.JpushBaseBean;
import com.maibei.merchants.net.base.JpushCallBack;
import com.maibei.merchants.net.push.JpushLogUpdateFinished;
import com.maibei.merchants.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

public class JpushHandle {
    private Context mContext;

    public JpushHandle(Context context) {
        this.mContext = context;
    }

    public void jpushHandle(String result, JpushCallBack jpushCallBack) {
        try {
            JpushBaseBean jpushBaseBean = GsonUtil.json2bean(result, JpushBaseBean.class);
            if (jpushBaseBean.msg_type == null || "".equals(jpushBaseBean.msg_type)) {
                return;
            }
            int type = Integer.parseInt(jpushBaseBean.msg_type);
            switch (type) {

                case GlobalParams.UPDATE_LOG_STATUS:
                    JpushLogUpdateFinished jpushLogUpdateFinished=new JpushLogUpdateFinished();
                    jpushLogUpdateFinished.updateLogFinished(type,result,jpushCallBack);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
