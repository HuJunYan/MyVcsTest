package com.tianshen.cash.net.api;

import android.content.Context;

import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.JpushBaseBean;
import com.tianshen.cash.net.base.JpushCallBack;
import com.tianshen.cash.net.push.JpushAddBorrowTerm;
import com.tianshen.cash.net.push.JpushLogUpdateFinished;
import com.tianshen.cash.net.push.JpushLotOfVerifyStatus;
import com.tianshen.cash.net.push.JpushVerifyFinished;
import com.tianshen.cash.net.push.JpushWithdrawalsVerifyFinished;
import com.tianshen.cash.utils.LogUtil;
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
                case GlobalParams.ADD_BORROW_TERM: // 延长借款期限
                    JpushAddBorrowTerm jpushAddBorrowTerm = new JpushAddBorrowTerm();
                    jpushAddBorrowTerm.JpushAddBorrowTerm(type, result, jpushCallBack);
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
