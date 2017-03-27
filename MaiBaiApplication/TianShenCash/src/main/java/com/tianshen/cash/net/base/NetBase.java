package com.tianshen.cash.net.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class NetBase {
    private Context mContext;
    private HttpUtils mHttpUtils;
    private BitmapUtils mBitmapUtils;

    public NetBase(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        XUtilsManager mXUtilsManager = XUtilsManager.getInstance(this.mContext);
        this.mHttpUtils = mXUtilsManager.getHttpUtils();
        this.mBitmapUtils = mXUtilsManager.getBitmapUtils();
    }

    public void getDataFromServerByPost(String url, JSONObject object, CallBack callBack) {
        getDataFromServerByPost(url, object, null, false, callBack);
    }

    public void getDataFromServerByPost(String url, JSONObject object, boolean isShowDialog, CallBack callBack) {
        getDataFromServerByPost(url, object, null, isShowDialog, callBack);
    }

    public void getDataFromServerByPost(final String url, JSONObject object, final View view, boolean isShowDialog, final CallBack callBack) {
        if (!NetCheck.isNetConnected(this.mContext)) {
            if (isShowNoNetworksPrompt()) {
                ToastUtil.showToast(this.mContext, MemoryAddressUtils.check_network());
                callBack.onFailure("", -2, -1000);
            }
            if (view != null) {
                view.setEnabled(true);
            }
            return;
        }
//        if (isNeedGotoLoginPage(object)) {
//            return;
//        }
        if (isShowDialog) {
            String loadText = this.mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
            ViewUtil.createLoadingDialog((Activity) this.mContext, loadText, false);
        }
        StringEntity mStringEntity = null;
        try {
            LogUtil.d("ret", "url = " + url + ";  json = " + object);
            mStringEntity = new StringEntity(object.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            MobclickAgent.reportError(this.mContext, LogUtil.getException(e1));
            e1.printStackTrace();
        }
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.setBodyEntity(mStringEntity);
        this.mHttpUtils.send(HttpMethod.POST, url, mRequestParams, new RequestCallBack<String>() {
            public void onStart() {
                if (view != null)
                    view.setEnabled(false);
            }

            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    String result = (String) responseInfo.result;
//                    if (url != null && url.contains("bindVerifySms")) {
//                        LogUtil.d("ret", "url = " + url + ";  result = " + result);
                    LogUtil.d("ret", "url = " + url + ";  before handle result = " + result);
                    int fristIntdex = result.indexOf("{");
                    int lastIntdex = result.lastIndexOf("}") + 1;
                    result = result.substring(fristIntdex, lastIntdex);
//                    }
                    LogUtil.d("ret", "url = " + url + ";  after handle result = " + result);
                    if (GsonUtil.isSuccess(result)) {
                        callBack.onSuccess(result, url);
                    } else {
                        DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, result);
                        callBack.onFailure(result, -1, GsonUtil.getErrorCode(result));
                    }
                } catch (Exception e) {
                    String g = (String) responseInfo.result;
                    callBack.onFailure(g, -1, GsonUtil.getErrorCode(g));
                    DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, "");
                    MobclickAgent.reportError(NetBase.this.mContext, LogUtil.getException(e));
                    e.printStackTrace();
                }

                ViewUtil.cancelLoadingDialog();
            }

            public void onFailure(HttpException e, String m) {
				ToastUtil.showToast(NetBase.this.mContext, MemoryAddressUtils.ServiceFaile());
                if (view != null) {
                    view.setEnabled(true);
                }
                LogUtil.d("ret", "failed: url = " + url + " ; m = " + m + "--ExceptionCode-->" + e.getExceptionCode());
                callBack.onFailure("", -3, -1000);
                DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, "");
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    public BitmapUtils getBitmapUtils() {
        return this.mBitmapUtils;
    }

    public boolean isShowNoNetworksPrompt() {
        return true;
    }



    protected void uploadFileByPost(String url, RequestParams requestParams, final CallBack callBack) {
        this.uploadFileByPost(url, requestParams, null, false, callBack);
    }

    protected void uploadFileByPost(String url, RequestParams requestParams, final View view, boolean isShowDialog, final CallBack callBack) {
        this.uploadFileByPost(url, requestParams, view, isShowDialog, null, callBack);
    }

    protected void uploadFileByPost(final String url, RequestParams requestParams, final View view, boolean isShowDialog, String promp, final CallBack callBack) {
        if (!NetCheck.isNetConnected(this.mContext)) {
            if (isShowNoNetworksPrompt()) {
                ToastUtil.showToast(this.mContext, MemoryAddressUtils.check_network());
                callBack.onFailure("", -2, -1000);
            }
            if (view != null) {
                view.setEnabled(true);
            }
            return;
        }
        if (isShowDialog) {
            String loadText = this.mContext.getResources().getText(MemoryAddressUtils.loading()).toString();
            ViewUtil.createLoadingDialog((Activity) this.mContext, promp == null ? loadText : promp, false);
        }
        LogUtil.d("ret", "url = " + url + ";  json = " + requestParams.toString());
        this.mHttpUtils.send(HttpMethod.POST, url, requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    if (view != null) {
                        view.setEnabled(true);
                    }
                    String result = (String) responseInfo.result;
                    LogUtil.d("ret", "url = " + url + ";  result = " + result);
                    if (GsonUtil.isSuccess(result)) {
                        callBack.onSuccess(result, url);
                    } else {
                        ResponseBean mResponseBean = GsonUtil.json2bean(result, ResponseBean.class);
                        DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, result);
                        callBack.onFailure(result, -1, GsonUtil.getErrorCode(result));
                    }
                } catch (Exception e) {
                    String g = (String) responseInfo.result;
                    callBack.onFailure(g, -1, GsonUtil.getErrorCode(g));
                    MobclickAgent.reportError(NetBase.this.mContext, LogUtil.getException(e));
                    e.printStackTrace();
                }
                ViewUtil.cancelLoadingDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtil.showToast(NetBase.this.mContext, MemoryAddressUtils.ServiceFaile());
                if (view != null) {
                    view.setEnabled(true);
                }
                LogUtil.d("ret", "failed: url = " + url + ",response = " + s + "--ExceptionCode-->" + e.getExceptionCode());
                callBack.onFailure("", -3, -1000);
                ViewUtil.cancelLoadingDialog();
            }
        });
    }

    private boolean isNeedGotoLoginPage(JSONObject jsonObject) {
        List<String> keyList = SignUtils.copyIterator(jsonObject.keys());
        try {
            if (keyList.contains("customer_id")) {
                String customer_id = jsonObject.getString("customer_id");
                if (customer_id == null || "".equals(customer_id)) {
                    Intent intent = new Intent();
                    intent.setAction(GlobalParams.GOTO_LOGIN_ACTIVITY);
                    mContext.sendBroadcast(intent);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(NetBase.this.mContext, LogUtil.getException(e));

        }
        return false;
    }

}
