package com.maibei.merchants.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.UploadLogBean;
import com.maibei.merchants.net.api.UploadLog;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class UploadLogService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    public UploadLogService(){
        super("UploadLogService");
    }
    public UploadLogService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String logPath=getExternalStoragePath() + "/" + GlobalParams.LOG_FILE;
        Log.d("uploadService","启动上传线程");
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("customer_id",UserUtil.getMerchantId(this));
            jsonObject.put("person_type","2");
            jsonObject.put("type","22");
            UploadLog uploadLog=new UploadLog(this);
            UserUtil.setLogStatus(this,GlobalParams.LOG_STATUS_IS_UPLOADING);
            Log.d("uploadService","开始上传");
            uploadLog.uploadLog(jsonObject, logPath, false, new BaseNetCallBack<UploadLogBean>() {
                @Override
                public void onSuccess(UploadLogBean paramT) {
                    Log.d("uploadService","上传成功");
                    UserUtil.setLogStatus(UploadLogService.this,GlobalParams.LOG_STATUS_NOT_NEED_UPLOAD);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    Log.d("uploadService","上传失败");
                    UserUtil.setLogStatus(UploadLogService.this,GlobalParams.LOG_STATUS_IS_UPLOAD_fail);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static String getExternalStoragePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
       /* if(GlobalParams.LOG_STATUS_IS_UPLOADING.equals(UserUtil.getLogStatus(UploadLogService.this))) {
            Log.d("uploadService","destroy退出上传");
            UserUtil.setLogStatus(UploadLogService.this, GlobalParams.LOG_STATUS_IS_UPLOAD_fail);
        }*/
        super.onDestroy();
    }
}
