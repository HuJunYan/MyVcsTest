package com.tianshen.cash.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplication;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.constant.NetConstantValue;
import com.tianshen.cash.manager.UpdateManager;
import com.tianshen.cash.model.CheckUpgradeBean;
import com.tianshen.cash.net.api.CheckUpgrade;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.MyTextView;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONObject;

public class AboutMaibeiActivity extends BaseActivity implements View.OnClickListener ,UpdateManager.Control{

    private int width = 0, height = 0;
    private TextView tv_code;
    private MyTextView mtv_check_code, mtv_service, mtv_resolve;
    private UpdateManager mUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels;  // 宽度（PX）
        height = metric.heightPixels;  // 高度（PX）
        if(!NetConstantValue.checkIsReleaseService()){
            ToastUtil.showToast(mContext,UserUtil.getId(mContext)+"");
        }
        initData();
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

    public void initData() {
        if(NetConstantValue.checkIsReleaseService()) {
            mtv_service.setTv_right("正式");
        }
        else{
            mtv_service.setTv_right("测试");
        }
        mtv_resolve.setTv_right(width + "*" + height);
        tv_code.setText("当前版本 " + getVersion());
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
            mjson.put("device_id",UserUtil.getDeviceId(mContext));
            mjson.put("channel_id", GlobalParams.CHANNEL_ID);
            checkUpgrade.checkUpgrade(mjson, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {

                    if (paramT.getCode() == 0) {//0为应用有升级

                        //设置当前APP显示什么视图标记位
                        MyApplication myApplication = (MyApplication) mContext.getApplicationContext();
                        myApplication.setOn_verify(paramT.getData().getOn_verify());

                        String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                        String explain = paramT.getData().getIntroduction();//更新说明
                        String upgradeType = paramT.getData().getForce_upgrade();//更新类型
                        String is_ignore = paramT.getData().getIs_ignore();//是否忽略升级
                        if (!"1".equals(is_ignore)) {
                            mUpdateManager = new UpdateManager(mContext, apkUrl, explain, upgradeType);
                            mUpdateManager.checkUpdateInfo();
                        }
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
}