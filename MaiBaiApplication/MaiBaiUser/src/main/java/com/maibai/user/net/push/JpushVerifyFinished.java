package com.maibai.user.net.push;

import android.support.annotation.NonNull;

import com.maibai.user.model.JpushVerifyFinishedBean;
import com.maibai.user.model.OrderRefreshBean;
import com.maibai.user.net.base.GsonUtil;
import com.maibai.user.net.base.JpushCallBack;

/**
 * Created by m on 16-9-22.
 */
public class JpushVerifyFinished {

    private boolean mIsRelese = true;
    private JpushVerifyFinishedBean jpushVerifyFinishedBean;

    public void VerifyFinished(int type, String result, JpushCallBack callBack) {
        if (this.mIsRelese) {
            jpushVerifyFinishedBean = GsonUtil.json2bean(result, JpushVerifyFinishedBean.class);
        } else {
//            object = k(t);
        }
        OrderRefreshBean orderRefreshBean = jpushVerifyFinishedBean2OrderRefreshBean();

        callBack.onResult(type, (Object)orderRefreshBean);
    }

    @NonNull
    private OrderRefreshBean jpushVerifyFinishedBean2OrderRefreshBean() {
        OrderRefreshBean orderRefreshBean = new OrderRefreshBean();
        orderRefreshBean.setCode(0);
        orderRefreshBean.setMsg("jpush success");
        orderRefreshBean.getData().setStatus(jpushVerifyFinishedBean.getMsg_content().getStatus());
        orderRefreshBean.getData().setConsume_id(jpushVerifyFinishedBean.getMsg_content().getConsume_id());
        orderRefreshBean.getData().setDown_payment(jpushVerifyFinishedBean.getMsg_content().getDown_payment());
        orderRefreshBean.getData().setBind_card(jpushVerifyFinishedBean.getMsg_content().getBind_card());
        orderRefreshBean.getData().setCard_num(jpushVerifyFinishedBean.getMsg_content().getCard_num());
        orderRefreshBean.getData().setBank_name(jpushVerifyFinishedBean.getMsg_content().getBank_name());
        return orderRefreshBean;
    }
}
