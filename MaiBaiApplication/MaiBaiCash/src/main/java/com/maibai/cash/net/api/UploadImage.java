package com.maibai.cash.net.api;

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
import com.maibai.cash.R;
import com.maibai.cash.constant.NetConstantValue;
import com.maibai.cash.model.ResponseBean;
import com.maibai.cash.model.UploadImageBean;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.GsonUtil;
import com.maibai.cash.net.base.NetCheck;
import com.maibai.cash.net.base.XUtilsManager;
import com.maibai.cash.utils.MemoryAddressUtils;
import com.maibai.cash.utils.ToastUtil;
import com.maibai.cash.utils.ViewUtil;

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
    private JSONObject mJSONObject;
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
        mJSONObject = jsonObject;
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
            Log.d("ret", "uploadImage: " + jsonObject.toString());
            requestParams.addBodyParameter("json", jsonObject.toString());
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
                        } else {
                            mUploadImageCallBack.onSuccess(test());
                        }
//                        ToastUtil.showToast(mContext, R.string.SendSuccess);
                    } else {
                        if (isRelease) {
                            ResponseBean mResponseBean = GsonUtil.json2bean(responseInfo.result, ResponseBean.class);
                            mUploadImageCallBack.onFailure(responseInfo.result, -1, mResponseBean.getCode());
//                            ToastUtil.showToast(mContext, R.string.Sendfaile);
                        } else {
                            mUploadImageCallBack.onSuccess(test());
//                            ToastUtil.showToast(mContext, R.string.SendSuccess);
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

                if (isRelease) {
                    mUploadImageCallBack.onFailure("", -3, -1000);
                    ToastUtil.showToast(mContext, R.string.network_error);
                } else {
                    mUploadImageCallBack.onSuccess(test());
                    ToastUtil.showToast(mContext, R.string.SendSuccess);
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
                    requestParams.addBodyParameter("file"+i, new FileInputStream(mPhotoFile), mPhotoFile.length(),
                            mPhotoFile.getName(), "application/octet-stream");
                } else {
                    ToastUtil.showToast(mContext, R.string.not_get_image_uploading_fail);
                    return;
                }
            }
            Log.d("ret", "uploadImage: " + jsonObject.toString());
            requestParams.addBodyParameter("json", jsonObject.toString());
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
                        } else {
                            mUploadImageCallBack.onSuccess(test());
                        }
                        ToastUtil.showToast(mContext, R.string.SendSuccess);
                    } else {
                        if (isRelease) {
                            ResponseBean mResponseBean = GsonUtil.json2bean(responseInfo.result, ResponseBean.class);
                            mUploadImageCallBack.onFailure(responseInfo.result, -1, mResponseBean.getCode());
                            ToastUtil.showToast(mContext, R.string.Sendfaile);
                        } else {
                            mUploadImageCallBack.onSuccess(test());
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

                if (isRelease) {
                    mUploadImageCallBack.onFailure("", -3, -1000);
                    ToastUtil.showToast(mContext, R.string.network_error);
                } else {
                    mUploadImageCallBack.onSuccess(test());
                    ToastUtil.showToast(mContext, R.string.SendSuccess);
                }
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    private boolean isShowNoNetworksPrompt() {
        return true;
    }

    private UploadImageBean test() {
        if (mJSONObject == null) {
            throw new RuntimeException("jsonObject is null");
        }
        String customer_id = "";
        try {
            customer_id = mJSONObject.getString("customer_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (customer_id == null || "".equals(customer_id)) {
            throw new RuntimeException("customer_id is null");
        }

        String type = "";
        try {
            type = mJSONObject.getString("type");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type == null || "".equals(type)) {
            throw new RuntimeException("type is null");
        }

        File file = new File(mFileFullPath);
        if (!file.exists()) {
            throw new RuntimeException("file : " + mFileFullPath + " is not exists");
        }
        UploadImageBean mUploadImageBean = new UploadImageBean();
        mUploadImageBean.setCode(0);
        mUploadImageBean.setMsg("uploadImage in success");
        mUploadImageBean.getData().setFile_name("测试文件名xxxx.png");

        return mUploadImageBean;
    }

}
