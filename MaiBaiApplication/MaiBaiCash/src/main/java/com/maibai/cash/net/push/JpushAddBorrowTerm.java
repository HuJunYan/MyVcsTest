package com.maibai.cash.net.push;

import com.maibai.cash.model.JpushAddBorrowTermBean;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.JpushCallBack;

/**
 * Created by crslljj on 17/1/12.
 */

public class JpushAddBorrowTerm {
    private boolean mIsRelese = true;
    private JpushAddBorrowTermBean jpushAddBorrowTermBean;
    public void JpushAddBorrowTerm(int type, String result, JpushCallBack callBack) {
        if (this.mIsRelese) {
            jpushAddBorrowTermBean = GsonUtil.json2bean(result, JpushAddBorrowTermBean.class);
        } else {
//            object = k(t);
        }

        callBack.onResult(type, (Object)jpushAddBorrowTermBean);
    }

}
