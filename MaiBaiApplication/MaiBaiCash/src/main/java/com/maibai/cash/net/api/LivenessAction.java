package com.maibai.cash.net.api;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.http.RequestParams;
import com.maibai.cash.model.ImageVerifyRequestBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.CallBack;
import com.maibai.cash.net.base.NetBase;
import com.maibai.cash.utils.Utils;

import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Created by zhangchi on 2016/7/6.
 */
public class LivenessAction extends NetBase {
    private Context mContex;

    public LivenessAction(Context context) {
        super(context);
        mContex = context;
    }

    public void imageVerify(ImageVerifyRequestBean reqBean, final BaseNetCallBack<String> callBack) {
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("idcard_name", "张驰");
        Log.d("ret", "idcard_name:" + reqBean.name);
        requestParams.addBodyParameter("idcard_number", "413026199101018116");
        Log.d("ret", "idcard_number:" + reqBean.idcard);
//        requestParams.addBodyParameter("image_ref1", new File(reqBean.imageref));// 传入身份证头像照片路径
//        Log.d("ret", "image_ref1:" + reqBean.imageref);
        requestParams.addBodyParameter("delta", "123");
        Log.d("ret", "delta:" + reqBean.delta);
        requestParams.addBodyParameter("api_key", "m1R29AVS6thjVza2OPA9zJw4F7xb3cM_");
        requestParams.addBodyParameter("api_secret", "v40tZD7VdeIxUYN9gJ6VDfrtkqgWrcrr");
        requestParams.addBodyParameter("comparison_type", "1");
        requestParams.addBodyParameter("face_image_type", "meglive");
        for (Map.Entry<String, byte[]> entry : reqBean.images.entrySet()) {
            Log.d("ret", entry.getKey() + "===" + Utils.saveJPGFile(mContex, entry.getValue(), entry.getKey()) + "\n");
            requestParams.addBodyParameter(entry.getKey(), String.valueOf(new ByteArrayInputStream(entry.getValue())));
        }
        String url = "https://api.megvii.com/faceid/v2/verify";
        uploadFileByPost(url, requestParams, new CallBack() {
            @Override
            public void onSuccess(String result, String url) {
                callBack.onSuccess(result);
            }

            @Override
            public void onFailure(String result, int errorType, int errorCode) {
                callBack.onFailure(result, errorType, errorCode);
            }
        });
    }
}
