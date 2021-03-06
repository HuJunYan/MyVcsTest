package com.tianshen.cash.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.event.LogoutSuccessEvent;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.Logout;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.MyTextView;
import com.tianshen.cash.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class SettingActivity extends BaseActivity implements View.OnClickListener, TitleBar.TitleBarListener2 {
    private boolean isResume = true;
    private MyTextView bt_unregist;
    private MyTextView my_tv_changepassword;
    private TextView tv_setting_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isResume = getIntent().getBooleanExtra("isResume", true);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void findViews() {
        bt_unregist = (MyTextView) findViewById(R.id.bt_unregist);
        tv_setting_back = (TextView) findViewById(R.id.tv_setting_back);
        my_tv_changepassword = (MyTextView) findViewById(R.id.my_tv_changepassword);
        bt_unregist.setBottomLineVisibility(View.VISIBLE);
        my_tv_changepassword.setBottomLineVisibility(View.VISIBLE);
    }

    @Override
    protected void setListensers() {
        bt_unregist.setOnClickListener(this);
        tv_setting_back.setOnClickListener(this);
        my_tv_changepassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_unregist:
                logout();
                break;
            case R.id.tv_setting_back:
                backActivity();
                break;
            case R.id.my_tv_changepassword:
                if (isResume) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", GlobalParams.CHANGE_LOGIN_PASSWORD);
                    gotoActivity(mContext, ForgetPasswordActivity.class, bundle);
                } else {
                    ToastUtil.showToast(mContext, "您未登录，不可修改密码", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        try {
            boolean login = TianShenUserUtil.isLogin(mContext);
            if (!login) {
                ToastUtil.showToast(mContext, "您未登录，无需退出", Toast.LENGTH_SHORT);
                return;
            }
            final MyApplicationLike app = MyApplicationLike.getMyApplicationLike();

            String customerId = TianShenUserUtil.getUserId(mContext);
            Logout logout = new Logout(mContext);
            JSONObject json = new JSONObject();
            json.put(GlobalParams.USER_CUSTOMER_ID, customerId);
            logout.logout(json, new BaseNetCallBack<ResponseBean>() {

                @Override
                public void onSuccess(ResponseBean paramT) {
                    TianShenUserUtil.clearUser(mContext);
                    JPushInterface.stopPush(app);
                    EventBus.getDefault().post(new LogoutSuccessEvent());

                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    TianShenUserUtil.clearUser(mContext);
                    JPushInterface.stopPush(app);
                    EventBus.getDefault().post(new LogoutSuccessEvent());
                    backActivity();
                }
            });
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

    @Override
    public void onLeftClick(View view) {
        backActivity();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onRightClick(View view) {

    }
}
