package com.tianshen.cash.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.manager.UpdateManager;
import com.tianshen.cash.model.CashAmountBean;
import com.tianshen.cash.model.CheckUpgradeBean;
import com.tianshen.cash.net.api.CheckUpgrade;
import com.tianshen.cash.net.api.GetCashAmountService;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.service.UploadLogService;
import com.tianshen.cash.utils.LocationUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.MaiDianUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.TimeCount;
import com.tianshen.cash.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;


/**
 * Created by 14658 on 2016/7/4.
 */
public class NavigationActivity extends BaseActivity implements UpdateManager.Control {
    private long startTime;
    private long finishTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        uploadLog(mContext);
        startTime = System.currentTimeMillis();
        checkUpdate();
        MaiDianUtil.ding(mContext, MaiDianUtil.FLAG_1);
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
                if (aBoolean) {
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    UserUtil.setDeviceId(mContext, TelephonyMgr.getDeviceId());
                }
            }
        });

        LocationUtil mLocationUtil = LocationUtil.getInstance();
        mLocationUtil.startLocation(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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
            mjson.put("channel_id", Utils.getChannelId());
            checkUpgrade.checkUpgrade(mjson, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {
                    String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                    String explain = paramT.getData().getIntroduction();//更新说明
                    String upgradeType = paramT.getData().getForce_upgrade();//更新类型
                    String is_ignore = paramT.getData().getIs_ignore();//是否忽略升级
                    if ("1".equals(is_ignore)) {//当前是最新版本
                        checkStatusGoActivity();
                    } else {
                        UpdateManager mUpdateManager = new UpdateManager(NavigationActivity.this, apkUrl, explain, upgradeType, NavigationActivity.this);
                        mUpdateManager.checkUpdateInfo();
                    }
                }

                @Override
                public void onFailure(String result, int errorType, int errorCode) {
                    if (errorCode == 118) {
                        checkStatusGoActivity();
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
        checkStatusGoActivity();
    }

    /**
     * 根据当前用户的状态跳转到不同的页面
     */
    private void checkStatusGoActivity() {

        boolean isLogin = TianShenUserUtil.isLogin(mContext);
        if (!isLogin) {
            finishTime = System.currentTimeMillis();
            gotoOtherActivity(MainActivity.class);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(GlobalParams.USER_CUSTOMER_ID, TianShenUserUtil.getUserId(mContext));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        GetCashAmountService getCashAmountService = new GetCashAmountService(mContext);
        getCashAmountService.getData(jsonObject, new BaseNetCallBack<CashAmountBean>() {
            @Override
            public void onSuccess(CashAmountBean bean) {
                finishTime = System.currentTimeMillis();

                String is_payway = bean.getData().getIs_payway();
                String cash_amount_status = bean.getData().getCash_amount_status();

                String cur_credit_step = bean.getData().getCur_credit_step();
                String total_credit_step = bean.getData().getTotal_credit_step();

                int totalCreditStep = 0;
                int curCreditStep = 0;
                if (!TextUtils.isEmpty(cur_credit_step)) {
                    curCreditStep = Integer.parseInt(cur_credit_step);
                }
                if (!TextUtils.isEmpty(total_credit_step)) {
                    totalCreditStep = Integer.parseInt(total_credit_step);
                }
                if (curCreditStep > 0 && curCreditStep < totalCreditStep) { //跳转到认证中心页面
                    gotoOtherActivity(AuthCenterMenuActivity.class);
                    return;
                }
                if ("0".equals(cash_amount_status)) { //跳转到首页
                    gotoOtherActivity(MainActivity.class);
                } else if ("1".equals(cash_amount_status) && "0".equals(is_payway)) { //跳转到首页
                    gotoOtherActivity(MainActivity.class);
                } else if ("1".equals(cash_amount_status) && "1".equals(is_payway)) { //跳转到掌众借款页面
                    gotoOtherActivity(ConfirmBorrowingActivity.class);
                } else if ("2".equals(cash_amount_status)) { //跳转到跑分等待页面
                    gotoOtherActivity(EvaluateAmountActivity.class);
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    /**
     * 在启动页面停留够至少2秒钟中在跳转其他页面
     */
    private void gotoOtherActivity(final Class clazz) {
        new TimeCount(2000 + startTime - finishTime, 2000) {
            @Override
            public void onFinish() {
                gotoActivity(NavigationActivity.this, clazz, null);
                NavigationActivity.this.finish();
            }
        }.start();
    }

}
