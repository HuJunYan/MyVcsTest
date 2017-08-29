package com.tianshen.cash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.event.FinishCurrentActivityEvent;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.model.VerifyCodeBean;
import com.tianshen.cash.net.api.GetVerifyCode;
import com.tianshen.cash.net.api.ResetPassword;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.RegexUtil;
import com.tianshen.cash.utils.TianShenUserUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.ChangeInterface;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, MyEditText.MyEditTextListener {
    private String mobile = "", verityCode = "";
    private MyEditText et_mobile, et_card, et_set_new_pwd;
    private TextView bt_next;
    private GetVerifyCode mGetVerifyCodeAction;
    private TextView tv_reset_pwd_back;
    private final int SETTING_PASSWORD_SUCCESS = 3, REQUEST_SETING_PASSWORD = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGetVerifyCodeAction = new GetVerifyCode(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void findViews() {
        et_mobile = (MyEditText) findViewById(R.id.et_mobile);
        et_set_new_pwd = (MyEditText) findViewById(R.id.et_set_new_pwd);
        tv_reset_pwd_back = (TextView) findViewById(R.id.tv_reset_pwd_back);
        et_card = (MyEditText) findViewById(R.id.et_card);
        bt_next = (TextView) findViewById(R.id.bt_next);
    }

    @Override
    protected void setListensers() {
        bt_next.setOnClickListener(this);
        tv_reset_pwd_back.setOnClickListener(this);
        et_card.setListener(this);
        et_card.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void chageAfter(Editable s) {
                verityCode = s.toString();
            }
        });
        et_mobile.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                mobile = s.toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (mobile.equals("")) {
                    ToastUtil.showToast(mContext, "请输入手机号", Toast.LENGTH_SHORT);
                    return;
                }
                if (!et_mobile.getEditTextString().startsWith("1")) {
                    ToastUtil.showToast(mContext, "手机号格式不正确", Toast.LENGTH_SHORT);
                    return;
                }
                if (verityCode.equals("")) {
                    ToastUtil.showToast(mContext, "请输入验证码", Toast.LENGTH_SHORT);
                    return;
                }
                String password = et_set_new_pwd.getEditTextString();
                if (password.isEmpty()) {
                    ToastUtil.showToast(mContext, "密码不能为空", Toast.LENGTH_SHORT);
                    return;
                }
                if (password.length() > 5 && password.length() < 18) {
                    changePassword(password, verityCode, mobile);
                } else {
                    ToastUtil.showToast(mContext, "密码为6~18位", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.tv_reset_pwd_back:
                backActivity();
                break;
        }
    }


    @Override
    public boolean onRightClick(View view) {
        switch (view.getId()) {
            case R.id.et_card:
                boolean login = TianShenUserUtil.isLogin(mContext);
                if (login) {
                    String userPhoneNum = TianShenUserUtil.getUserPhoneNum(mContext);
                    if (!et_mobile.getEditTextString().trim().equals(userPhoneNum)) {
                        ToastUtil.showToast(mContext, "请输入本人手机号");
                        return false;
                    }
                }
                return getVerityCode();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == REQUEST_SETING_PASSWORD) {
            if (arg1 == SETTING_PASSWORD_SUCCESS) {
                backActivity();
            }
        }
    }

    private boolean getVerityCode() {
        if (et_mobile.getEditTextString().length() != 11 || !RegexUtil.IsTelephone(mobile)) {
            ToastUtil.showToast(this, "手机号格式不正确");
            return false;
        }
        JSONObject json = null;
        try {
            json = new JSONObject();
            json.put("mobile", et_mobile.getEditTextString());
            json.put("type", "5");
        } catch (JSONException e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
        mGetVerifyCodeAction.getVerifyCode(json, new BaseNetCallBack<VerifyCodeBean>() {
            @Override
            public void onSuccess(VerifyCodeBean verifyCodeBean) {
                ToastUtil.showToast(mContext, "验证码发送成功");
            }

            @Override
            public void onFailure(String url, int errorType, int errorCode) {
                et_card.finishTimer();
            }
        });
        return true;
    }


    public void changePassword(final String password, String verityCode, String mobile) {
        try {
            JSONObject json = new JSONObject();
            json.put("password", password);
            json.put("verify_code", verityCode);
            json.put("mobile", mobile);
            ResetPassword resetPassword = new ResetPassword(mContext);
            resetPassword.resetPassword(json, bt_next, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                    UserUtil.setLoginPassword(mContext, password);
                    ToastUtil.showToast(mContext, "密码设置成功", Toast.LENGTH_SHORT);
                    TianShenUserUtil.clearUser(mContext);
                    EventBus.getDefault().post(new FinishCurrentActivityEvent());
                    gotoActivity(mContext, LoginActivity.class, null);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    ToastUtil.showToast(mContext, "密码设置失败", Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }

}
