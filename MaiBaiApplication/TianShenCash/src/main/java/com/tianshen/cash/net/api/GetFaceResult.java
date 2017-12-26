package com.tianshen.cash.net.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.orhanobut.logger.Logger;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.GetFaceResultBean;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.DealWithErrorUtils;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetCheck;
import com.tianshen.cash.net.base.XUtilsManager;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.VersionUtil;
import com.tianshen.cash.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by chenrongshang on 16/7/7.
 */
public class GetFaceResult {
    private boolean isRelease = true;
    private String mUrl;
    private String mFileFullPath;
    private Context mContext;
    //    private JSONObject mJSONObject;
    protected HttpUtils mHttpUtils;

    public GetFaceResult(Context context) {
        mContext = context;
        mUrl = NetConstantValue.getFaceResultURL();
        initXUtils();
    }

    private void initXUtils() {
        XUtilsManager xUtilsManager = XUtilsManager.getInstance(mContext);
        mHttpUtils = xUtilsManager.getHttpUtils();
    }

    public void uploadImage(JSONObject jsonObject, String fileFullPath, boolean isShowDialog, final BaseNetCallBack<GetFaceResultBean> mUploadImageCallBack) {
        Log.d("abc", "fileFullPath ==== " + fileFullPath);
        //外层嵌套一层信息
        JSONObject objectRoot = new JSONObject();
        //得到用户登录的token
        String userToken = TianShenUserUtil.getUserToken(mContext);
        //得到版本号
        String version = VersionUtil.getVersion(mContext);
        //得到时间戳
        String timestamp = System.currentTimeMillis() + "";
        try {
            objectRoot.put("token", userToken); //token
            objectRoot.put("version", version);
            objectRoot.put("type", "1"); //客户端类型0h5,1android,2ios,3winphone
            objectRoot.put("timestamp", timestamp);
            objectRoot.put("data", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mFileFullPath = fileFullPath;
        if (!NetCheck.isNetConnected(this.mContext)) {
            if (isShowNoNetworksPrompt()) {
                ToastUtil.showToast(this.mContext, MemoryAddressUtils.check_network());
                mUploadImageCallBack.onFailure("", -2, -1000);
            }
            return;
        }
        if (isShowDialog) {
            String loadText = this.mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
            ViewUtil.createLoadingDialog((Activity) this.mContext, loadText, false);
        }
        XUtilsManager xUtilsManager = XUtilsManager.getInstance(mContext);
        mHttpUtils = xUtilsManager.getHttpUtils();
        RequestParams requestParams = new RequestParams();
        try {

            File mPhotoFile = new File(fileFullPath);
            LogUtil.d("abc", "mPhotoFile exist = " + mPhotoFile.exists() + ",path = " + mPhotoFile.getAbsolutePath() + "size = " + mPhotoFile.length());
            if (mPhotoFile.exists()) {
                requestParams.addBodyParameter("file", new FileInputStream(mPhotoFile), mPhotoFile.length(),
                        mPhotoFile.getName(), "application/octet-stream");
            } else {
                ToastUtil.showToast(mContext, R.string.not_get_image_uploading_fail);
                return;
            }
            Logger.i("上行url-->" + mUrl);
            Log.d("abc", "uploadImage: " + objectRoot.toString());
            Logger.i("uploadImage");
            Logger.json(objectRoot.toString());

            requestParams.addBodyParameter("json", objectRoot.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHttpUtils.send(HttpRequest.HttpMethod.POST, mUrl, requestParams, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    Log.d("abc", "mUrl = " + mUrl + " ; result = " + responseInfo.result);
                    if (responseInfo != null && GsonUtil.isSuccess(responseInfo.result)) {
                        if (isRelease) {
                            GetFaceResultBean mUploadImageBean = GsonUtil.json2bean(responseInfo.result, GetFaceResultBean.class);
                            mUploadImageCallBack.onSuccess(mUploadImageBean);
                        }
                    } else {
                        if (isRelease) {
                            ResponseBean mResponseBean = GsonUtil.json2bean(responseInfo.result, ResponseBean.class);
                            mUploadImageCallBack.onFailure(responseInfo.result, -1, mResponseBean.getCode());
                            DealWithErrorUtils.dealWithErrorCode(mContext, responseInfo.result, null, true);
                        }
                    }
                } catch (Exception e) {
                    ToastUtil.showToast(mContext, R.string.network_error);
                    e.printStackTrace();
                }
                ViewUtil.cancelLoadingDialog();
            }

            @Override
            public void onFailure(HttpException error, String msg) {

                Logger.d("abc uploadImage--failed");
                Logger.d(error);
                Logger.d(msg);

                if (isRelease) {
                    mUploadImageCallBack.onFailure("", -3, -1000);
                    ToastUtil.showToast(mContext, R.string.network_error);
                }
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    private boolean isShowNoNetworksPrompt() {
        return true;
    }


}
