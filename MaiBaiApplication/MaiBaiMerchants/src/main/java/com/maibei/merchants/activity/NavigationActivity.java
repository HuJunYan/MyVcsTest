package com.maibei.merchants.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.baidu.location.LocationClient;
import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.manager.UpdateManager;
import com.maibei.merchants.model.CheckUpgradeBean;
import com.maibei.merchants.net.api.CheckUpgrade;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.service.UploadLogService;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 14658 on 2016/7/4.
 */
public class NavigationActivity extends BaseActivity implements View.OnClickListener, UpdateManager.Control {
    private final int SCANSPAN = 1000 * 60 * 10;
    private LocationClient mLocationClient;
    private String f;
    private SharedPreferencesUtil sharedPreferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance(mContext);
        if (isDBNew()) {
            writeDB();
        }
        JPushInterface.setAliasAndTags(mContext, "merchant"+UserUtil.getMerchantId(mContext), null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
        uploadLog(mContext);
        checkUpdate();
    }
    private void uploadLog(Context context){
        if(GlobalParams.LOG_STATUS_NEED_UPLOAD.equals(UserUtil.getLogStatus(context))||GlobalParams.LOG_STATUS_IS_UPLOAD_fail.equals(UserUtil.getLogStatus(context))) {
        Intent intent = new Intent(context, UploadLogService.class);
        context.startService(intent);
        }
    }

    private boolean isDBNew() {
        String dbEdition = sharedPreferencesUtil.getString("dbEdition");
        if (null == dbEdition) {
            return true;
        }
        if (GlobalParams.DB_EDITION.equals(dbEdition)) {
            return false;
        } else {
            return true;
        }
    }

    public void writeDB() {
        String f = getFilesDir() + "\\databases\\" + GlobalParams.DB_NAME;  //此处如果是放在应用包名的目录下,自动放入“databases目录下
        FileOutputStream fout = null;
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.maibeimerchant);
            fout = new FileOutputStream(new File(f));
            byte[] buffer = new byte[128];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fout.write(buffer, 0, len);
            }
            buffer = null;
            sharedPreferencesUtil.putString("dbEdition", GlobalParams.DB_EDITION);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

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
    protected void setListensers() {

    }

    @Override
    public void onClick(View v) {

    }

    private void init() {
        //登陆并
        if (SharedPreferencesUtil.getInstance(mContext).getBoolean("isLogin") == true) {
            //认证
            if (UserUtil.getStatus(mContext).equals("1")) {
                gotoActivity(NavigationActivity.this, MainActivity.class, null);
                backActivity();
                //未审核
            } else if (UserUtil.getStatus(mContext).equals("0")) {
                gotoActivity(NavigationActivity.this, ToExamineActivity.class, null);
                backActivity();
                //待完善资料
            } else if (UserUtil.getStatus(mContext).equals("3")) {
                gotoActivity(NavigationActivity.this, NotCertifiedActivity.class, null);
                backActivity();
            } else if (UserUtil.getStatus(mContext).equals("2")) {
                gotoActivity(NavigationActivity.this, LoginActivity.class, null);
                backActivity();
            } else {
                gotoActivity(NavigationActivity.this, LoginActivity.class, null);
                backActivity();
            }
            //未登陆
        } else {
            gotoActivity(NavigationActivity.this, LoginActivity.class, null);
            backActivity();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(mContext);
    }

    /**
     * 获取当前应用的版本号
     *
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
            return "";
        }
    }

    private void checkUpdate() {
        String vesionNo = getVersion();
        CheckUpgrade checkUpgrade = new CheckUpgrade(NavigationActivity.this);
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("id", UserUtil.getMerchantId(mContext));
            mJSONObject.put("current_version", vesionNo);
            mJSONObject.put("app_type", "2");
            checkUpgrade.checkUpgrade(mJSONObject, new BaseNetCallBack<CheckUpgradeBean>() {
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
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                init();
                            }
                        };
                        timer.schedule(timerTask, 3000);
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
        init();
    }
}
