package com.maibai.cash.net.push;

import com.maibai.cash.model.JpushLotOfVerifyStatusBean;
import com.maibai.cash.model.JpushWithdrawalsVerifyFinishedBean;
import com.maibai.cash.model.WithdrawalsRefreshBean;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.JpushCallBack;

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
