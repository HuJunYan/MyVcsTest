package com.maibai.user.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.constant.GlobalParams;
import com.maibai.user.model.ResponseBean;
import com.maibai.user.net.api.Logout;
import com.maibai.user.net.base.BaseNetCallBack;
import com.maibai.user.net.base.UserUtil;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.SendBroadCastUtil;
import com.maibai.user.utils.ToastUtil;
import com.maibai.user.view.MyTextView;
import com.maibai.user.view.TitleBar;
import com.umeng.analytics.MobclickAgent;

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
                try {
                    String customerId = UserUtil.getId(mContext);
                    if (customerId == null || "".equals(customerId)) {
                        ToastUtil.showToast(mContext, "您未登录，无需退出", Toast.LENGTH_SHORT);
                        return;
                    }
                    Logout logout = new Logout(mContext);
                    JSONObject json = new JSONObject();
                    json.put("customer_id", customerId);
                    logout.logout(json, new BaseNetCallBack<ResponseBean>() {

                        @Override
                        public void onSuccess(ResponseBean paramT) {
                            new SendBroadCastUtil(mContext).sendBroad(GlobalParams.LOGOUT_ACTION, null);
                            UserUtil.removeUser(mContext);
                            isResume = true;
                            GlobalParams.isNotice = true;
                            setResult(UNREGISTERE);
                            backActivity();
                        }

                        @Override
                        public void onFailure(String url, int errorType, int errorCode) {
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e));
                }
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

    @Override
    public void onLeftClick(View view) {
        setResult(isResume ? UNREGISTERE : 0);
        finish();
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
