package com.tianshen.cash.net.push;

import com.tianshen.cash.model.JpushLotOfVerifyStatusBean;
import com.tianshen.cash.model.JpushWithdrawalsVerifyFinishedBean;
import com.tianshen.cash.model.WithdrawalsRefreshBean;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.JpushCallBack;

/**
 * Created by crslljj on 16/12/28.
 */

public class JpushLotOfVerifyStatus {
    private boolean mIsRelese = true;
    private JpushLotOfVerifyStatusBean jpushLotOfVerifyStatusBean;
    public void JpushLotOfVerifyStatus(int type, String result, JpushCallBack callBack) {
        if (this.mIsRelese) {
            jpushLotOfVerifyStatusBean = GsonUtil.json2bean(result, JpushLotOfVerifyStatusBean.class);
        } else {
//            object = k(t);
        }

        callBack.onResult(type, (Object)jpushLotOfVerifyStatusBean);
    }

}
