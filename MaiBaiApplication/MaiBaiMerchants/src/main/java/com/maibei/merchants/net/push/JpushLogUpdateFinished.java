package com.maibei.merchants.net.push;

import com.maibei.merchants.net.base.JpushCallBack;

/**
 * Created by Administrator on 2016/11/30.
 */

public class JpushLogUpdateFinished {
    private boolean mIsRelese = true;

    public void updateLogFinished(int type, String result, JpushCallBack callBack) {
       /* if (this.mIsRelese) {
            jpushWithdrawalsVerifyFinishedBean = GsonUtil.json2bean(result, CallBack.class);
        } else {
//            object = k(t);
        }
        WithdrawalsRefreshBean withdrawalsRefreshBean = jpushBean2RefreshBean();
*/
        callBack.onResult(type, null);
    }
}
