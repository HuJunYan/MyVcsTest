package com.maibai.cash.net.push;

import android.support.annotation.NonNull;

import com.maibai.cash.model.JpushWithdrawalsVerifyFinishedBean;
import com.maibai.cash.model.WithdrawalsRefreshBean;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.JpushCallBack;

/**
 * Created by m on 16-10-18.
 */
public class JpushWithdrawalsVerifyFinished {
    private boolean mIsRelese = true;
    private JpushWithdrawalsVerifyFinishedBean jpushWithdrawalsVerifyFinishedBean;

    public void VerifyFinished(int type, String result, JpushCallBack callBack) {
        if (this.mIsRelese) {
            jpushWithdrawalsVerifyFinishedBean = GsonUtil.json2bean(result, JpushWithdrawalsVerifyFinishedBean.class);
        } else {
//            object = k(t);
        }
        WithdrawalsRefreshBean withdrawalsRefreshBean = jpushBean2RefreshBean();

        callBack.onResult(type, (Object)withdrawalsRefreshBean);
    }

    @NonNull
    private WithdrawalsRefreshBean jpushBean2RefreshBean() {
        WithdrawalsRefreshBean withdrawalsRefreshBean = new WithdrawalsRefreshBean();
        withdrawalsRefreshBean.setCode(0);
        withdrawalsRefreshBean.setMsg("jpush success");
        withdrawalsRefreshBean.getData().setStatus(jpushWithdrawalsVerifyFinishedBean.getMsg_content().getStatus());
        withdrawalsRefreshBean.getData().setReason(jpushWithdrawalsVerifyFinishedBean.getMsg_content().getReason());
        withdrawalsRefreshBean.getData().setAmount(jpushWithdrawalsVerifyFinishedBean.getMsg_content().getAmount());
        return withdrawalsRefreshBean;
    }
}
