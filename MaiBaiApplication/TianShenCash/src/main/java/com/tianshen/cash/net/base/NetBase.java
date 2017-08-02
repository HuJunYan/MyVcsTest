package com.tianshen.cash.net.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.orhanobut.logger.Logger;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MemoryAddressUtils;
import com.tianshen.cash.utils.SignUtils;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.VersionUtil;
import com.tianshen.cash.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class NetBase {
    private Context mContext;
    private HttpUtils mHttpUtils;

    public NetBase(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        XUtilsManager mXUtilsManager = XUtilsManager.getInstance(this.mContext);
        this.mHttpUtils = mXUtilsManager.getHttpUtils();
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
            objectRoot.put("data", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity mStringEntity = null;
        try {
            LogUtil.d("ret", "url = " + url + ";  json = " + objectRoot.toString());
            Logger.i("上行url-->" + url);
            Logger.json(objectRoot.toString());
            mStringEntity = new StringEntity(objectRoot.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            MobclickAgent.reportError(this.mContext, LogUtil.getException(e1));
            e1.printStackTrace();
        }
        final RequestParams mRequestParams = new RequestParams();
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
//                    LogUtil.d("ret", "url = " + url + ";  before handle result = " + result);
                    int fristIntdex = result.indexOf("{");
                    int lastIntdex = result.lastIndexOf("}") + 1;
                    result = result.substring(fristIntdex, lastIntdex);
//                    }
                    LogUtil.d("ret", "url = " + url + ";  after handle result = " + result);
                    Logger.i("下行onSuccess--url->" + url);
                    Logger.json(result);
                    if (GsonUtil.isSuccess(result)) {
                        callBack.onSuccess(result, url);
                    } else {
                        DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, result, view);
                        callBack.onFailure(result, -1, GsonUtil.getErrorCode(result));
                    }
                } catch (Exception e) {
                    String g = (String) responseInfo.result;
                    Logger.i("下行Exception-->" + url + "result--->" + g);
                    callBack.onFailure(g, -1, GsonUtil.getErrorCode(g));
                    DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, "", view);
                    MobclickAgent.reportError(NetBase.this.mContext, LogUtil.getException(e));
                    e.printStackTrace();
                }
                cancelDialog();
            }

            public void onFailure(HttpException e, String m) {
                //如果是下单页面就不弹出来错误toast, 调用这个接口服务器会超时
                // (暂时服务器不处理)呵呵哒
                String withdrawalsApplyURL = NetConstantValue.getWithdrawalsApplyURL();
                if (!withdrawalsApplyURL.equals(url)) {
                    ToastUtil.showToast(NetBase.this.mContext, MemoryAddressUtils.ServiceFaile());
                }
                if (view != null) {
                    view.setEnabled(true);
                }
                LogUtil.d("ret", "failed: url = " + url + " ; m = " + m + "--ExceptionCode-->" + e.getExceptionCode());
                Logger.d("下行failed--url-->" + url);
                Logger.d("下行failed--msg-->" + m);
                Logger.d("下行failed--code-->" + e.getExceptionCode());
                callBack.onFailure("", -3, -1000);
                DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, "", view);
                cancelDialog();
            }
        });
    }


    /**
     * 检测token是否有效
     */
    private void checkTokenIsOK() {

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
                        DealWithErrorUtils.dealWithErrorCode(NetBase.this.mContext, result, view);
                        callBack.onFailure(result, -1, GsonUtil.getErrorCode(result));
                    }
                } catch (Exception e) {
                    String g = (String) responseInfo.result;
                    callBack.onFailure(g, -1, GsonUtil.getErrorCode(g));
                    MobclickAgent.reportError(NetBase.this.mContext, LogUtil.getException(e));
                    e.printStackTrace();
                }
                cancelDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtil.showToast(NetBase.this.mContext, MemoryAddressUtils.ServiceFaile());
                if (view != null) {
                    view.setEnabled(true);
                }
                LogUtil.d("ret", "failed: url = " + url + ",response = " + s + "--ExceptionCode-->" + e.getExceptionCode());
                callBack.onFailure("", -3, -1000);
                cancelDialog();
            }
        });
    }

    private boolean isNeedGotoLoginPage(JSONObject jsonObject) {
        List<String> keyList = SignUtils.copyIterator(jsonObject.keys());
        try {
            if (keyList.contains(GlobalParams.USER_CUSTOMER_ID)) {
                String customer_id = jsonObject.getString(GlobalParams.USER_CUSTOMER_ID);
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

    //安全关闭dialog 防止崩溃
    public void cancelDialog() {
        try {
            if (mContext != null && !((Activity) mContext).isFinishing()) {
                ViewUtil.cancelLoadingDialog();
            }
        } catch (ClassCastException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }
}
