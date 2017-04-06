package com.tianshen.cash.net.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tianshen.cash.R;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.UploadImageBean;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.GsonUtil;
import com.tianshen.cash.net.base.NetCheck;
import com.tianshen.cash.net.base.XUtilsManager;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.VersionUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by chenrongshang on 16/7/7.
 */
public class UploadImage {
    private boolean isRelease = true;
    private String mUrl;
    private String mFileFullPath;
    private Context mContext;
    //    private JSONObject mJSONObject;
    protected HttpUtils mHttpUtils;
    protected BitmapUtils mBitmapUtils;

    public UploadImage(Context context) {
        mContext = context;
        mUrl = NetConstantValue.GetUploadImageUrl();
        initXUtils();
    }

    private void initXUtils() {
        XUtilsManager xUtilsManager = XUtilsManager.getInstance(mContext);
        mHttpUtils = xUtilsManager.getHttpUtils();
        mBitmapUtils = xUtilsManager.getBitmapUtils();
    }

    public void uploadImage(JSONObject jsonObject, String fileFullPath, boolean isShowDialog, final BaseNetCallBack<UploadImageBean> mUploadImageCallBack) {
        Log.d("ret", "fileFullPath ==== " + fileFullPath);


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
            if (mPhotoFile.exists()) {
                requestParams.addBodyParameter("file", new FileInputStream(mPhotoFile), mPhotoFile.length(),
                        mPhotoFile.getName(), "application/octet-stream");
            } else {
                ToastUtil.showToast(mContext, R.string.not_get_image_uploading_fail);
                return;
            }
            Log.d("ret", "uploadImage: " + objectRoot.toString());
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
                    Log.d("ret", "mUrl = " + mUrl + " ; result = " + responseInfo.result);
                    if (responseInfo != null && GsonUtil.isSuccess(responseInfo.result)) {
                        if (isRelease) {
                            UploadImageBean mUploadImageBean = GsonUtil.json2bean(responseInfo.result, UploadImageBean.class);
                            mUploadImageCallBack.onSuccess(mUploadImageBean);
                        }
                    } else {
                        if (isRelease) {
                            ResponseBean mResponseBean = GsonUtil.json2bean(responseInfo.result, ResponseBean.class);
                            mUploadImageCallBack.onFailure(responseInfo.result, -1, mResponseBean.getCode());
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

                Logger.d("uploadImage--failed");
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

    public void uploadImageArray(JSONObject jsonObject, String[] fileFullPathArray, boolean isShowDialog, final BaseNetCallBack<UploadImageBean> mUploadImageCallBack) {
        Log.d("ret", "fileFullPath ==== " + fileFullPathArray[0]);
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
            for (int i = 0; i < fileFullPathArray.length; i++) {
                File mPhotoFile = new File(fileFullPathArray[i]);
                if (mPhotoFile.exists()) {
                    requestParams.addBodyParameter("file" + i, new FileInputStream(mPhotoFile), mPhotoFile.length(),
                            mPhotoFile.getName(), "application/octet-stream");
                } else {
                    ToastUtil.showToast(mContext, R.string.not_get_image_uploading_fail);
                    return;
                }
            }
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

            Log.d("ret", "uploadImage: " + objectRoot.toString());
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
                    Log.d("ret", "mUrl = " + mUrl + " ; result = " + responseInfo.result);
                    Logger.i("下行onSuccess--mUrl-->"+mUrl);
                    Logger.json(responseInfo.result);

                    if (responseInfo != null && GsonUtil.isSuccess(responseInfo.result)) {
                        if (isRelease) {
                            UploadImageBean mUploadImageBean = GsonUtil.json2bean(responseInfo.result, UploadImageBean.class);
                            mUploadImageCallBack.onSuccess(mUploadImageBean);
                        } else
                            ToastUtil.showToast(mContext, R.string.SendSuccess);
                    } else {
                        if (isRelease) {
                            ResponseBean mResponseBean = GsonUtil.json2bean(responseInfo.result, ResponseBean.class);
                            mUploadImageCallBack.onFailure(responseInfo.result, -1, mResponseBean.getCode());
                            ToastUtil.showToast(mContext, R.string.Sendfaile);
                        } else {
                            ToastUtil.showToast(mContext, R.string.SendSuccess);
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

                Logger.d("uploadImage--failed");
                Logger.d(error);
                Logger.d(msg);

                if (isRelease) {
                    mUploadImageCallBack.onFailure("", -3, -1000);
                    ToastUtil.showToast(mContext, R.string.network_error);
                } else {
                    ToastUtil.showToast(mContext, R.string.SendSuccess);
                }
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    private boolean isShowNoNetworksPrompt() {
        return true;
    }


}
