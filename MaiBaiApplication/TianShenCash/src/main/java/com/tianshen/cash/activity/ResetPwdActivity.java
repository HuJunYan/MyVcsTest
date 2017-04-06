package com.tianshen.cash.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tianshen.cash.R;
import com.tianshen.cash.base.BaseActivity;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.ResponseBean;
import com.tianshen.cash.net.api.ResetPassword;
import com.tianshen.cash.net.base.BaseNetCallBack;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.LogUtil;
import com.tianshen.cash.utils.ToastUtil;
import com.tianshen.cash.view.ChangeInterface;
import com.tianshen.cash.view.MyEditText;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {
    private String password = "", confirmPassword = "";

    private Button bt_next;
    private ResetPassword resetPassword;
    private MyEditText et_password, et_confirm_password;
    private Bundle mBundle;

    private final int SETTING_PASSWORD_SUCCESS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetPassword = new ResetPassword(mContext);
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void findViews() {
        mBundle = getIntent().getExtras();
        et_password = (MyEditText) findViewById(R.id.et_password);
        et_confirm_password = (MyEditText) findViewById(R.id.et_confirm_password);
        bt_next = (Button) findViewById(R.id.bt_next);
        if (GlobalParams.CHANGE_PAY_PASSWORD.equals(mBundle.get("type"))) {
            et_password.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_password.setHint("请输入6位数字密码");
            et_password.setLength(6);
            et_confirm_password.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_confirm_password.setHint("请重复输入6位数字密码");
            et_confirm_password.setLength(6);
        } else {
            et_password.setInputType(InputType.TYPE_CLASS_TEXT);
            et_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
            et_password.setHint("请输入6~18位密码");
            et_confirm_password.setHint("请重复输入密码");
            et_password.setLength(18);
            et_confirm_password.setLength(18);
        }
    }

    @Override
    protected void setListensers() {
        bt_next.setOnClickListener(this);
        bt_next.setClickable(false);
        et_password.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                password = s.toString();
                if (GlobalParams.CHANGE_PAY_PASSWORD.equals(mBundle.getString("type"))) {
                    if (password.length() == 6 && confirmPassword.length() == 6) {
                        if (password.equals(confirmPassword)) {
                            bt_next.setClickable(true);
                            bt_next.setBackgroundResource(R.drawable.select_bt);
                        } else {
                            ToastUtil.showToast(mContext, "两次输入不一致", Toast.LENGTH_SHORT);
                            bt_next.setClickable(false);
                            bt_next.setBackgroundResource(R.drawable.button_gray);
                        }
                    } else {
                        bt_next.setClickable(false);
                        bt_next.setBackgroundResource(R.drawable.button_gray);
                    }
                } else {
                    if (password.length() > 5 && password.length() < 18 && confirmPassword.length() > 5 && confirmPassword.length() < 18) {
                        if (password.equals(confirmPassword)) {
                            bt_next.setClickable(true);
                            bt_next.setBackgroundResource(R.drawable.select_bt);
                        } else {
                            bt_next.setClickable(false);
                            bt_next.setBackgroundResource(R.drawable.button_gray);
                        }
                    } else {
                        bt_next.setClickable(false);
                        bt_next.setBackgroundResource(R.drawable.button_gray);
                    }
                }
            }
        });
        et_confirm_password.setChangeListener(new ChangeInterface() {
            @Override
            public void change(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void changeBefore(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void chageAfter(Editable s) {
                confirmPassword = s.toString();
                if (GlobalParams.CHANGE_PAY_PASSWORD.equals(mBundle.getString("type"))) {
                    if (password.length() == 6 && confirmPassword.length() == 6) {
                        if (password.equals(confirmPassword)) {
                            bt_next.setClickable(true);
                            bt_next.setBackgroundResource(R.drawable.select_bt);
                        } else {
                            ToastUtil.showToast(mContext, "两次输入不一致");
                            bt_next.setClickable(false);
                            bt_next.setBackgroundResource(R.drawable.button_gray);
                        }
                    } else {
                        bt_next.setClickable(false);
                        bt_next.setBackgroundResource(R.drawable.button_gray);
                    }
                } else {
                    if (password.length() > 5 && password.length() < 18 && confirmPassword.length() > 5 && confirmPassword.length() < 18) {
                        if (password.equals(confirmPassword)) {
                            bt_next.setClickable(true);
                            bt_next.setBackgroundResource(R.drawable.select_bt);
                        } else {
                            bt_next.setClickable(false);
                            bt_next.setBackgroundResource(R.drawable.button_gray);
                        }
                    } else {
                        bt_next.setClickable(false);
                        bt_next.setBackgroundResource(R.drawable.button_gray);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (password.equals("") || confirmPassword.equals("")) {
                    ToastUtil.showToast(mContext, "请输入完整", Toast.LENGTH_SHORT);
                } else {
                    if (password.equals(confirmPassword)) {
                        changePassword(password);
                    } else {
                        ToastUtil.showToast(mContext, "两次输入不一致", Toast.LENGTH_SHORT);
                    }
                }
                break;
        }
    }

    public void changePassword(final String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("password", password);
            json.put("verify_code", mBundle.getString("verify_code"));
            json.put("type", mBundle.getString("type"));
            json.put("mobile", mBundle.getString("mobile"));
            resetPassword.resetPassword(json, bt_next, true, new BaseNetCallBack<ResponseBean>() {
                @Override
                public void onSuccess(ResponseBean paramT) {
                     if(GlobalParams.CHANGE_LOGIN_PASSWORD.equals(mBundle.getString("type"))) {
                         UserUtil.setLoginPassword(mContext, password);
                     }
                    setResult(SETTING_PASSWORD_SUCCESS);
                    backActivity();
                    ToastUtil.showToast(mContext, "密码设置成功", Toast.LENGTH_SHORT);
                }

                @Override
                public void onFailure(String url, int errorType, int errorCode) {
                    ToastUtil.showToast(mContext, "密码设置失败", Toast.LENGTH_SHORT);
                    et_password.setText("");
                    et_confirm_password.setText("");
                }
            });
        } catch (Exception e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        }
    }
}
