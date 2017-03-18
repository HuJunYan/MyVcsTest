package com.maibei.merchants.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.NetConstantValue;
import com.maibei.merchants.manager.UpdateManager;
import com.maibei.merchants.model.CheckUpgradeBean;
import com.maibei.merchants.net.api.CheckUpgrade;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.view.MyTextView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/7/28.
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener,UpdateManager.Control {

    private ImageButton ib_return_home;
    private MyTextView itv_update;
    private MyTextView itv_service;
    private MyTextView itv_resolution;
    private TextView tv_version_no;

    private UpdateManager mUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getResolution();
        String vesionNo = getVersion();
        tv_version_no.setText("当前版本"+vesionNo);
        checkUpdate();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void findViews() {
        ib_return_home = (ImageButton)findViewById(R.id.ib_return_home);
        itv_update = (MyTextView)findViewById(R.id.itv_update);
        itv_service = (MyTextView)findViewById(R.id.itv_service);
        itv_resolution = (MyTextView)findViewById(R.id.itv_resolution);
        tv_version_no = (TextView)findViewById(R.id.tv_version_no);
    }

    @Override
    protected void setListensers() {
        ib_return_home.setOnClickListener(this);
    }

    /**
     * 获取分辨率和服务器
     */
    private void getResolution(){
        Display display = getWindowManager().getDefaultDisplay();
        String resolution = display.getWidth() + "x" + display.getHeight();
        itv_resolution.setTv_right(resolution);
        boolean isisReleaseVersion = NetConstantValue.isReleaseVersion();
        if (isisReleaseVersion){
            itv_service.setTv_right("正式");
        }else {
            itv_service.setTv_right("测试");
        }
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
            return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_return_home:
                backActivity();
                break;
            default:
                break;
        }
    }

    private void checkUpdate(){
        String vesionNo = getVersion();
        CheckUpgrade checkUpgrade = new CheckUpgrade(AboutActivity.this);
        JSONObject mJSONObject = new JSONObject();
        try{
            mJSONObject.put("id", UserUtil.getMerchantId(mContext));
            mJSONObject.put("current_version",vesionNo);
            mJSONObject.put("app_type", "2");
            checkUpgrade.checkUpgrade(mJSONObject, new BaseNetCallBack<CheckUpgradeBean>() {
                @Override
                public void onSuccess(CheckUpgradeBean paramT) {
                    if (paramT.getCode() == 0){//0为应用有升级
                        final String apkUrl = paramT.getData().getDownload_url();//更新下载路径
                        final String explain = paramT.getData().getIntroduction();//更新说明
                        final String upgradeType = paramT.getData().getForce_upgrade();//更新类型

                        itv_update.setIvRightDraw(R.mipmap.icon_my_next);
                        itv_update.setTv_right("有新版本");
                        itv_update.setListener(new MyTextView.MyTextViewListener() {
                            @Override
                            public void clickFatherLayout(View view) {
                                mUpdateManager = new UpdateManager(AboutActivity.this, apkUrl, explain, upgradeType);
                                mUpdateManager.checkUpdateInfo();
                            }
                        });
                    }else {
                        itv_update.setIvRightDraw(0);
                        itv_update.setTv_right("已是最新版本");
                    }
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    if (errorType == 118){
                        itv_update.setIvRightDraw(0);
                        itv_update.setTv_right("已是最新版本");
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }

    }

    @Override
    public void cancelUpdate() {

    }
}
