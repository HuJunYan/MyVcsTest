package com.tianshen.cash.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.manager.UpdateManager;
import com.tianshen.cash.model.CheckUpgradeBean;
import com.tianshen.cash.model.CompanyInfoBean;
import com.tianshen.cash.model.User;
import com.tianshen.cash.net.api.CheckUpgrade;
import com.tianshen.cash.net.api.GetCompayInfo;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.Config;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.utils.Utils;
import com.tianshen.cash.view.MyTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutMaibeiActivity extends BaseActivity implements View.OnClickListener, UpdateManager.Control {

    private int width = 0, height = 0;
    private TextView tv_code;
    private MyTextView mtv_check_code, mtv_service, mtv_resolve, mtv_test_version, mtv_weixin_num, mtv_test_uid;
    private UpdateManager mUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;  // 宽度（PX）
        height = metric.heightPixels;  // 高度（PX）
        initData();
        initCompanyInfo();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_about_maibei;
    }

    @Override
    protected void findViews() {
        tv_code = (TextView) findViewById(R.id.tv_code);
        mtv_check_code = (MyTextView) findViewById(R.id.mtv_check_code);
        mtv_service = (MyTextView) findViewById(R.id.mtv_service);
        mtv_resolve = (MyTextView) findViewById(R.id.mtv_resolve);
        mtv_test_version = (MyTextView) findViewById(R.id.mtv_test_version);
        mtv_weixin_num = (MyTextView) findViewById(R.id.mtv_weixin_num);
        mtv_test_uid = (MyTextView) findViewById(R.id.mtv_test_uid);
    }

    @Override
    protected void setListensers() {
        mtv_check_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_check_code:
                checkUpdate();
                break;
        }
    }

    /**
     * 获取当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            return "Fail";
        }
    }

    /**
     * 得到公司信息
     */
    private void initCompanyInfo() {
        JSONObject jsonObject = new JSONObject();
        String userId = TianShenUserUtil.getUserId(mContext);
        try {
            jsonObject.put("customer_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final GetCompayInfo getCompayInfo = new GetCompayInfo(mContext);
        getCompayInfo.compayInfo(jsonObject, null, true, new BaseNetCallBack<CompanyInfoBean>() {

            @Override
            public void onSuccess(CompanyInfoBean paramT) {
                String wechatId = paramT.getData().getWechat_id();
                String service_telephone = paramT.getData().getService_telephone();
                User user = TianShenUserUtil.getUser(mContext);
                user.setService_telephone(service_telephone);
                user.setWechat_id(wechatId);
                TianShenUserUtil.saveUser(mContext, user);
                mtv_weixin_num.setTv_right(wechatId);
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    public void initData() {
        if (NetConstantValue.checkIsReleaseService()) {
            mtv_service.setTv_right("正式");
            mtv_test_version.setVisibility(View.GONE);
            mtv_test_uid.setVisibility(View.GONE);
        } else {
            mtv_service.setTv_right("测试");
            mtv_test_version.setVisibility(View.VISIBLE);
            mtv_test_version.setTv_right(String.format("编译: %s (%s)", getString(R.string.build_time), getString(R.string.githash)));
            mtv_test_uid.setVisibility(View.VISIBLE);
            mtv_test_uid.setTv_right("用户ID:" + TianShenUserUtil.getUserId(mContext));
        }
        mtv_resolve.setTv_right(width + "*" + height);
        tv_code.setText("当前版本 " + getVersion());

        String weiXin = TianShenUserUtil.getWeiXin(mContext);
        mtv_weixin_num.setTv_right(weiXin);
    }

    /**
     * 检测更新
     */
    private void checkUpdate() {
        String vesionNo = getVersion();
        CheckUpgrade checkUpgrade = new CheckUpgrade(mContext);
        JSONObject mjson = new JSONObject();
        try {
            mjson.put("current_version", vesionNo);
            mjson.put("app_type", "1");
            mjson.put("device_id", UserUtil.getDeviceId(mContext));

            String channel = WalleChannelReader.getChannel(this);
            String channel_id = Utils.channelName2channelID(channel);

            mjson.put("channel_id", channel_id);
            checkUpgrade.checkUpgrade(mjson, mtv_check_code, true, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {
                    if (paramT.getCode() == 0) {//0为应用有升级
                        String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                        String explain = paramT.getData().getIntroduction();//更新说明
                        String upgradeType = paramT.getData().getForce_upgrade();//更新类型
                        mUpdateManager = new UpdateManager(mContext, apkUrl, explain, upgradeType);
                        mUpdateManager.checkUpdateInfo();
                    } else {
                        ToastUtil.showToast(mContext, paramT.getMsg());
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void cancelUpdate() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 666) {
            String tinker_new = Config.TINKER_CACHE_DIR + "tianshen";
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), tinker_new);
        }
        return super.onKeyDown(keyCode, event);
    }
}
