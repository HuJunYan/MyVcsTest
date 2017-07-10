package com.tianshen.cash.net.api;

import android.content.Context;

import com.lidroid.xutils.http.RequestParams;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.IDCardBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.CallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetBase;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.utils.ViewUtil;

import java.io.File;

/**
 * Created by zhangchi on 2016/7/6.
 */
public class IDCardAction extends NetBase {
    private Context mContext;
    private String key;
    private String secret;
    private String url;

    public IDCardAction(Context context,String key ,String secret) {
        super(context);
        mContext = context;
        url = "https://api.faceid.com/faceid/v1/ocridcard";
        this.key = key;
        this.secret = secret;
    }

    public void getIDCardInfo(byte[] data, final BaseNetCallBack<IDCardBean> mCallBack) {
        RequestParams param = new RequestParams();
        param.addBodyParameter("api_key", key);
        param.addBodyParameter("api_secret", secret);
        param.addBodyParameter("legality", "1");
        param.addBodyParameter("image", new File(Utils.saveJPGFile(mContext, data, "idcard_img")));
        uploadFileByPost(url, param, new CallBack() {
            @Override
            public void onSuccess(String result, String url) {
                result = result.replace("ID Photo", "ID_Photo");
                IDCardBean bean = GsonUtil.json2bean(result, IDCardBean.class);
                if (bean.legality.ID_Photo < 0.8) {
                    mCallBack.onFailure(result, -1, -1);
                } else {
                    mCallBack.onSuccess(bean);
                }
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                mCallBack.onFailure(result, errorType, errorCode);
            }
        });
    }
}
