package com.maibai.user.net.push;

import com.maibai.user.model.JpushLotOfVerifyStatusBean;
import com.maibai.user.model.JpushWithdrawalsVerifyFinishedBean;
import com.maibai.user.model.WithdrawalsRefreshBean;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.JpushCallBack;

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
