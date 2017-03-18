package com.maibei.merchants.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.maibei.merchants.base.MyApplication;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.model.ResponseBean;
import com.maibei.merchants.model.UserLoginBean;
import com.maibei.merchants.net.api.Logout;
import com.maibei.merchants.net.api.UserLogin;
import com.maibei.merchants.net.base.BaseNetCallBack;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.maibei.merchants.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 14658 on 2016/8/7.
 */
public class ToExamineActivity extends BaseActivity implements View.OnClickListener {
    private boolean isClickReviewBtnWay = false;

    private ImageButton iv_review_qrcode;
    private ImageView iv_review_menu;
    private Button bt_review;
    private PopupWindow popupwindow;
    private RelativeLayout rl_history;
    private RelativeLayout rl_bank_info;
    private RelativeLayout rl_about;
    private RelativeLayout rl_quit;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rushReview();
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_examine;
    }

    @Override
    protected void findViews() {
        iv_review_qrcode = (ImageButton)findViewById(R.id.iv_review_qrcode);
        iv_review_menu = (ImageView)findViewById(R.id.iv_review_menu);
        bt_review = (Button)findViewById(R.id.bt_review);
    }

    @Override
    protected void setListensers() {
        iv_review_qrcode.setOnClickListener(this);
        iv_review_menu.setOnClickListener(this);
        bt_review.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_review_qrcode:
                ToastUtil.showToast(mContext, "审核中，未生成二维码");
                break;
            case R.id.iv_review_menu:
                if (popupwindow != null&&popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    return;
                } else {
                    initmPopupWindowView();
                    popupwindow.showAsDropDown(v, 0, 5);
                }
                break;
            case R.id.rl_bank_info:
                popupwindow.dismiss();
                gotoActivity(mContext, BankCardInfoActivity.class, null);
                break;
            case R.id.bt_review:
                isClickReviewBtnWay = true;
                rushReview();
                break;
            case R.id.rl_about:
                popupwindow.dismiss();
                gotoActivity(ToExamineActivity.this, AboutActivity.class, null);
                break;
            case R.id.rl_quit:
                quitSign();
                break;
        }
    }

    private void rushReview(){
        JSONObject mJson = new JSONObject();
        SharedPreferencesUtil share = SharedPreferencesUtil.getInstance(this);
        try {
            mJson.put("mobile", share.getString("phoneNumber"));
            mJson.put("password", share.getString("password"));
            String push_id = SharedPreferencesUtil.getInstance(mContext).getString(GlobalParams.JPUSH_ID_KEY);
            if (null == push_id || "".equals(push_id)) {
                push_id = JPushInterface.getRegistrationID(mContext);
            }
            mJson.put("push_id", push_id);
        }catch (Exception e){
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        UserLogin mUserLogin = new UserLogin(this);
        mUserLogin.userLogin(mJson, new BaseNetCallBack<UserLoginBean>() {
            @Override
            public void onSuccess(UserLoginBean userLoginBean) {
                String loginStatusStr = userLoginBean.getData().getStatus();
                int loginStatusInt = Integer.parseInt(loginStatusStr, 10);

                switch (loginStatusInt) {
                    case 0:
                        if (isClickReviewBtnWay) {
                            ToastUtil.showToast(mContext, "正在审核，请稍后...");
                        }
                        break;
                    case 1:
                        gotoActivity(mContext, MainActivity.class, null);//审核通过
                        ((MyApplication) getApplication()).clearTempActivityInBackStack(MainActivity.class);
                        break;
                    case 2:
                        ToastUtil.showToast(mContext, "该用户不存在, 请联系客服人员");
                        break;
                    case 3:
                        gotoActivity(mContext, NotCertifiedActivity.class, null);
                        ((MyApplication) getApplication()).clearTempActivityInBackStack(NotCertifiedActivity.class);
                        break;
                }
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
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

    private void quitSign(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ToExamineActivity.this);
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
                gotoActivity(ToExamineActivity.this, NavigationActivity.class, null);
                backActivity();
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast(this, R.string.click_one_exit);
            exitTime = System.currentTimeMillis();
        } else {
            backActivity();
        }
    }
}
