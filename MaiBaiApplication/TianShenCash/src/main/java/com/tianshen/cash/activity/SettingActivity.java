package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
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

public class SettingActivity extends BaseActivity implements View.OnClickListener, TitleBar.TitleBarListener2 {
    private boolean isResume = true;
    private Button bt_unregist;
    private MyTextView my_tv_changepassword;
    private TitleBar titleBar;
    private final int UNREGISTERE = 7;

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
        bt_unregist = (Button) findViewById(R.id.bt_unregist);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        my_tv_changepassword = (MyTextView) findViewById(R.id.my_tv_changepassword);
    }

    @Override
    protected void setListensers() {
        bt_unregist.setOnClickListener(this);
        titleBar.setListener(this);
        my_tv_changepassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_unregist:
                logout();
                break;
            case R.id.my_tv_changepassword:
                if (isResume) {
                    gotoActivity(mContext, ChangePwdSelectActivity.class, null);
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
            String customerId = TianShenUserUtil.getUserId(mContext);
            Logout logout = new Logout(mContext);
            JSONObject json = new JSONObject();
            json.put("customer_id", customerId);
            logout.logout(json, new BaseNetCallBack<ResponseBean>() {

                @Override
                public void onSuccess(ResponseBean paramT) {
                    TianShenUserUtil.clearUser(mContext);
                    EventBus.getDefault().post(new LogoutSuccessEvent());
                    backActivity();
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    TianShenUserUtil.clearUser(mContext);
                    EventBus.getDefault().post(new LogoutSuccessEvent());
                    backActivity();
                }
            });

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
