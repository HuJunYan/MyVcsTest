package com.maibai.cash.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.maibai.cash.R;
import com.maibai.cash.base.BaseActivity;
import com.maibai.cash.constant.GlobalParams;
import com.maibai.cash.manager.UpdateManager;
import com.maibai.cash.model.CheckUpgradeBean;
import com.maibai.cash.net.api.CheckUpgrade;
import com.maibai.cash.net.base.BaseNetCallBack;
import com.maibai.cash.net.base.UserUtil;
import com.maibai.cash.service.UploadLogService;
import com.maibai.cash.utils.LocationUtil;
import com.maibai.cash.utils.LogUtil;
import com.maibai.cash.utils.TimeCount;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 14658 on 2016/7/4.
 */
public class NavigationActivity extends BaseActivity implements UpdateManager.Control{
    private long startTime;
    private long finishTime;
    private final String TAG = "NavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
      /*  JPushInterface.setAliasAndTags(mContext, UserUtil.getId(mContext), null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });*/
        uploadLog(mContext);
    }
    private void uploadLog(Context context){
        if(GlobalParams.LOG_STATUS_NEED_UPLOAD.equals(UserUtil.getLogStatus(context))||GlobalParams.LOG_STATUS_IS_UPLOAD_fail.equals(UserUtil.getLogStatus(context))) {
            Intent intent = new Intent(context, UploadLogService.class);
            context.startService(intent);
        }
    }
    @Override
    protected int setContentView() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        checkUpdate();
        JPushInterface.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(mContext);
    }

    @Override
    protected void setListensers() {

    }

    private void init() {

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        UserUtil.setDeviceId(mContext,TelephonyMgr.getDeviceId());

        LocationUtil mLocationUtil = LocationUtil.getInstance(mContext);
        mLocationUtil.startLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void gotoMainAcitivity() {
        finishTime = System.currentTimeMillis();
        new TimeCount(3000 + startTime - finishTime, 3000) {
            @Override
            public void onFinish() {
                gotoActivity(NavigationActivity.this, MainActivity.class, null);
                NavigationActivity.this.finish();
            }
        }.start();
    }
    /**
     * 获取当前应用的版本号
     * @return
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            return "Fail";
        }
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        String vesionNo = getVersion();
        CheckUpgrade checkUpgrade = new CheckUpgrade(NavigationActivity.this);
        JSONObject mjson = new JSONObject();
        try {
            mjson.put("current_version", vesionNo);
            mjson.put("app_type", "1");
            mjson.put("device_id",UserUtil.getDeviceId(mContext));
            mjson.put("channel_id", GlobalParams.CHANNEL_ID);
            checkUpgrade.checkUpgrade(mjson, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {
                    if (paramT.getCode() == 0) {//0为应用有升级
                        String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                        String explain = paramT.getData().getIntroduction();//更新说明
                        String upgradeType = paramT.getData().getForce_upgrade();//更新类型
                        UpdateManager mUpdateManager = new UpdateManager(NavigationActivity.this, apkUrl, explain, upgradeType);
                        mUpdateManager.checkUpdateInfo();
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if (errorCode == 118) {
                        gotoMainAcitivity();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void cancelUpdate() {
        gotoMainAcitivity();
    }
}
