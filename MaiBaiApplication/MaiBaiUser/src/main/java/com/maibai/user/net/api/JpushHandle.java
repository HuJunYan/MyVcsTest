package com.maibai.user.net.api;

import android.content.Context;

import com.maibai.user.constant.GlobalParams;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.JpushBaseBean;
import com.maibai.user.net.base.JpushCallBack;
import com.maibai.user.net.push.JpushLogUpdateFinished;
import com.maibai.user.net.push.JpushLotOfVerifyStatus;
import com.maibai.user.net.push.JpushVerifyFinished;
import com.maibai.user.net.push.JpushWithdrawalsVerifyFinished;
import com.maibai.user.utils.LogUtil;
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
                case GlobalParams.VERIFY_FINISHED:
                    JpushVerifyFinished mJpushVerifyFinished = new JpushVerifyFinished();
                    mJpushVerifyFinished.VerifyFinished(type, result, jpushCallBack);
                    break;
                case GlobalParams.WITHDRAWALS_VERIFY_FINISHED:
                    JpushWithdrawalsVerifyFinished mJpushWithdrawalsVerifyFinished = new JpushWithdrawalsVerifyFinished();
                    mJpushWithdrawalsVerifyFinished.VerifyFinished(type, result, jpushCallBack);
                    break;
                case GlobalParams.UPDATE_LOG_STATUS:
                    JpushLogUpdateFinished jpushLogUpdateFinished=new JpushLogUpdateFinished();
                    jpushLogUpdateFinished.updateLogFinished(type,result,jpushCallBack);
                    break;
                case GlobalParams.A_LOT_OF_VERIFY: // 各种审核状态
                    JpushLotOfVerifyStatus jpushLotOfVerifyStatus = new JpushLotOfVerifyStatus();
                    jpushLotOfVerifyStatus.JpushLotOfVerifyStatus(type, result, jpushCallBack);
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
