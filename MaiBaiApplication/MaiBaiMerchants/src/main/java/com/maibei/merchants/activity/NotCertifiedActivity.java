package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.net.api.Logout;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.RequestPermissionUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

/**
 * Created by 14658 on 2016/8/1.
 */
public class NotCertifiedActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton iv_not_qrcode;
    private ImageView iv_not_menu;
    private Button bt_go_auth;

    private RelativeLayout rl_history;
    private RelativeLayout rl_bank_info;
    private RelativeLayout rl_about;
    private RelativeLayout rl_quit;

    private long exitTime = 0;
    private PopupWindow popupwindow;
//    public static NotCertifiedActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RequestPermissionUtil(mContext).requestLocationPermission();
//        instance = this;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_not_certified;
    }

    @Override
    protected void findViews() {
        iv_not_qrcode = (ImageButton)findViewById(R.id.iv_not_qrcode);
        iv_not_menu = (ImageView)findViewById(R.id.iv_not_menu);
        bt_go_auth = (Button)findViewById(R.id.bt_go_auth);
    }

    @Override
    protected void setListensers() {
        iv_not_qrcode.setOnClickListener(this);
        iv_not_menu.setOnClickListener(this);
        bt_go_auth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_not_qrcode:
                gotoActivity(NotCertifiedActivity.this, AuthShopInfoActivity.class, null);
                break;
            case R.id.iv_not_menu:
                if (popupwindow != null&&popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 5);
                }
                break;
            case R.id.bt_go_auth:
                gotoActivity(NotCertifiedActivity.this, AuthShopInfoActivity.class, null);
                break;
            case R.id.rl_bank_info:
                popupwindow.dismiss();
                gotoActivity(mContext, BankCardInfoActivity.class, null);
                break;
            case R.id.rl_about:
                popupwindow.dismiss();
                gotoActivity(NotCertifiedActivity.this, AboutActivity.class, null);
                break;
            case R.id.rl_quit:
                quitSign();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(this, R.string.click_one_exit);
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void initmPopupWindowView(){
        // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.popuwindow_menu,
                null, false);
        // 创建PopupWindow实例,200,150分别是宽度和高度
        popupwindow = new PopupWindow(customView);
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]

        popupwindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupwindow.setBackgroundDrawable(new BitmapDrawable());
        popupwindow.setOutsideTouchable(true);
        popupwindow.setFocusable(true);

        popupwindow.setAnimationStyle(R.style.fadeAnimationFade);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                }
                return false;
            }
        });
        rl_history = (RelativeLayout)customView.findViewById(R.id.rl_history);
        rl_history.setVisibility(View.GONE);
        rl_bank_info = (RelativeLayout)customView.findViewById(R.id.rl_bank_info);
        rl_about = (RelativeLayout)customView.findViewById(R.id.rl_about);
        rl_quit = (RelativeLayout)customView.findViewById(R.id.rl_quit);
        rl_bank_info.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_quit.setOnClickListener(this);
    }

    private void logout(){
        Logout logout = new Logout(this);
        JSONObject mJson = new JSONObject();
        try{
            mJson.put("merchant_id", UserUtil.getMerchantId(mContext));
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        logout.logout(mJson, new BaseNetCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean paramT) {
                gotoActivity(NotCertifiedActivity.this, NavigationActivity.class, null);
                backActivity();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    private void quitSign(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NotCertifiedActivity.this);
        builder.setTitle("提示");
        builder.setMessage("将退出登录");
        builder.setCancelable(false);
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.getInstance(mContext).putBoolean("isLogin", false);
                logout();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("SIGN_OUT_ACTIVITY");
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }
}
