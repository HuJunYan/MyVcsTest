package com.tianshen.cash.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.meituan.android.walle.WalleChannelReader;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.ServiceErrorEvent;
import com.tianshen.cash.manager.UpdateManager;
import com.tianshen.cash.model.CheckUpgradeBean;
import com.tianshen.cash.net.api.CheckUpgrade;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.service.UploadLogService;
import com.tianshen.cash.utils.Config;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TimeCount;
import com.tianshen.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;

/**
 * Created by 14658 on 2016/7/4.
 */
public class NavigationActivity extends BaseActivity implements UpdateManager.Control {
    private long startTime;
    private long finishTime;
    private final String TAG = "NavigationActivity";

    //补丁包
    public static final String TINKER = Config.TINKER_CACHE_DIR + "tianshen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
      /*  JPushInterface.setAliasAndTags(mContext, TianShenUserUtil.getUserId(mContext), null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });*/

        uploadLog(mContext);
        startTime = System.currentTimeMillis();
        checkUpdate();

    }


    private void uploadLog(Context context) {
        if (GlobalParams.LOG_STATUS_NEED_UPLOAD.equals(UserUtil.getLogStatus(context)) || GlobalParams.LOG_STATUS_IS_UPLOAD_fail.equals(UserUtil.getLogStatus(context))) {
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

        RxPermissions rxPermissions = new RxPermissions(NavigationActivity.this);
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                UserUtil.setDeviceId(mContext, TelephonyMgr.getDeviceId());
            }
        });

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
            mjson.put("device_id", UserUtil.getDeviceId(mContext));

            String channel = WalleChannelReader.getChannel(this);
            String channel_id = Utils.channelName2channelID(channel);

            mjson.put("channel_id", channel_id);
            checkUpgrade.checkUpgrade(mjson, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {
                    String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                    String explain = paramT.getData().getIntroduction();//更新说明
                    String upgradeType = paramT.getData().getForce_upgrade();//更新类型
                    String is_ignore = paramT.getData().getIs_ignore();//是否忽略升级
                    if ("1".equals(is_ignore)) {
                        gotoMainAcitivity();
                    } else {
                        UpdateManager mUpdateManager = new UpdateManager(NavigationActivity.this, apkUrl, explain, upgradeType);
                        mUpdateManager.checkUpdateInfo();
                    }
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
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

    /**
     * 显示系统维护的dialog
     */
    private void showServiceErrorDialog(String msg) {
        if (isFinishing()) {
            return;
        }
        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mLayoutInflater.inflate(R.layout.dialog_service_error, null, false);
        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        TextView tv_dialog_error_tips = (TextView) view.findViewById(R.id.tv_dialog_error_tips);
        tv_dialog_error_tips.setText(msg);
        ImageView iv_dialog_error_close = (ImageView) view.findViewById(R.id.iv_dialog_error_close);
        iv_dialog_error_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finish();
            }
        });
        mDialog.show();
    }

    /**
     * 收到了服务器维护的消息
     */
    @Subscribe
    public void onServiceErrorEvent(ServiceErrorEvent event) {
        showServiceErrorDialog(event.getMsg());
    }

    /**
     * 下载补丁包
     */
    private void downloadTinker(final String tinker_url) {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            FileDownloader.getImpl().create(tinker_url)
                                    .setPath(TINKER)
                                    .setListener(new FileDownloadListener() {
                                        @Override
                                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        }

                                        @Override
                                        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                                            LogUtil.d("abc", "FileDownloadListener---connected");
                                        }

                                        @Override
                                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        }

                                        @Override
                                        protected void blockComplete(BaseDownloadTask task) {
                                        }

                                        @Override
                                        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                                        }

                                        @Override
                                        protected void completed(BaseDownloadTask task) {
                                            LogUtil.d("abc", "FileDownloadListener---completed");
                                            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), TINKER);
                                        }

                                        @Override
                                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                                        }

                                        @Override
                                        protected void error(BaseDownloadTask task, Throwable e) {
                                            LogUtil.d("abc", "FileDownloadListener---error");
                                        }

                                        @Override
                                        protected void warn(BaseDownloadTask task) {
                                        }
                                    }).start();


                        }
                    }
                });
    }

}
